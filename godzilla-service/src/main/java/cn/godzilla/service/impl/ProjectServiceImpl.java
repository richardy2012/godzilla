package cn.godzilla.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.dao.ProjectMapper;
import cn.godzilla.model.Project;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.SvnService;
import cn.godzilla.util.GodzillaServiceApplication;

public class ProjectServiceImpl extends GodzillaServiceApplication implements ProjectService {

	@Autowired
	private OperateLogService operateLogService;
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private SvnService svnService;
	@Autowired
	private ClientConfigService clientConfigService;

	@Override
	public Project queryByProCode(String projectCode, String profile) {
		
		return projectMapper.qureyByProCode(projectCode);
	}

	@Override
	public List<Project> queryAll(String projectCode, String profile) {
		List<Project> projectlist = projectMapper.queryAll();
		return projectlist;
	}

	@Override
	public ReturnCodeEnum srcEdit(String projectCode, String profile, String repositoryUrl, String checkoutPath) {
		/**
		 * 1.update trunk version
		 */
		ReturnCodeEnum versionreturn = svnService.getVersion(repositoryUrl, projectCode);
		if(!versionreturn.equals(ReturnCodeEnum.getByReturnCode(OK_SVNVERSION))) {
			return versionreturn;
		}
		Project project = this.queryByProCode(projectCode, profile);
		String version = svnVersionThreadLocal.get();
		Map<String, String> parameterMap = new HashMap<String, String>();
		
		parameterMap.put("projectId", project.getId()+"");
		parameterMap.put("repositoryUrl", repositoryUrl);
		parameterMap.put("checkoutPath", checkoutPath);
		parameterMap.put("version", version);
		
		int index = projectMapper.updateProjectById(parameterMap);
		/**
		 * 2.update branches version
		 */
		
		if(index>0) {
			return ReturnCodeEnum.getByReturnCode(OK_SRCEDIT);
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_SRCEDIT);
		}
	}
	
	public boolean refreshProjectVersion(String projectCode, String profile) {
		Project project = this.queryByProCode(projectCode, profile);
		String trunkPath = project.getRepositoryUrl();
		
		ReturnCodeEnum versionreturn = svnService.getVersion(trunkPath, projectCode);
		if(!versionreturn.equals(ReturnCodeEnum.getByReturnCode(OK_SVNVERSION))) {
			return false;
		}
		String version = svnVersionThreadLocal.get();
		
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("project_code", projectCode);
		parameterMap.put("version", version);
		int index = projectMapper.updateVersionByProjectcode(parameterMap);
		
		return index>0;
	}

	@Override
	public List<Project> queryProjectsByUsername(String projectCode, String profile, String username) {
		List<Project> projects = projectMapper.queryProjectsByUsername(username);
		return projects;
	}

	@Override
	public ReturnCodeEnum editMergestatusByProjectCode(String projectCode, String profile, Map<String, String> parameterMap) {
		int index = projectMapper.updateMergestatusByProjectcode(parameterMap);
		if(index>0) {
			return ReturnCodeEnum.getByReturnCode(OK_EDITMERGESTATUS);
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_EDITMERGESTATUS);
		}
	}

}
