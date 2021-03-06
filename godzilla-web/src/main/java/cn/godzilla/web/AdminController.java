package cn.godzilla.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.response.ResponseBodyJson;
import cn.godzilla.util.ControllerHelper;
import cn.godzilla.util.GodzillaWebApplication;
@RequestMapping(value="admin")
public class AdminController extends GodzillaWebApplication{
	
	@Autowired
	private ControllerHelper controllerHelper;
	
	/**
	 * 执行命令
	 * upgrade,startclients,stopclients,stoptomcats,starttomcats
	 * @param response
	 * @return 
	*/
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/{command}", method=RequestMethod.GET) 
	@ResponseBody
	public Object upgrade(HttpServletResponse response, @PathVariable String command) {
		
		ReturnCodeEnum returnEnum = controllerHelper.godzillaCommand(command);
		return ResponseBodyJson.custom().setAll(returnEnum, ADMINOPERATOR).build();
	}
	
	/**
	 *  重启所有项目
	 *  
		项目依赖关系
		nuggets-server ->cupid
		xuanyuan->cupid
		message-center->cupid,newmanager
		fso-lark->cupid
		zeus->
		hades->xuanyuan
		uic->cupid
		hera 												
		apollo
		va-schedule 
		va-web
		uicm
		项目启动顺序
		(nuggets-server,message-center,fso-lark,zeus,hades,uic,hera,apollo,va-web,va-schedule)->(xuanyuan,newmanager)->(cupid)
	 * @return
	 
	@RequestMapping(value="/{sid}/restartProjects", method=RequestMethod.GET)
	@ResponseBody
	public Object restartProjects(@PathVariable String sid, HttpServletRequest request, HttpServletResponse response) {
		
		List<Project> projects = projectService.queryAll();
		for(Project project: projects) {
			if(list1.contains(project.getProjectCode())) {
				//---fork join
				ReturnCodeEnum returnenum = mvnService.restartTomcat(project.getProjectCode(), TEST_PROFILE);
				if(!returnenum.equals(ReturnCodeEnum.getByReturnCode(OK_STARTTOMCAT))) {
					return ResponseBodyJson.custom().setAll(NO_AJAX, FAILURE, project.getProjectCode()+"：启动失败", ADMINOPERATOR).build();
				}
			}
		}
		
		return null;
	}*/
	
	private static List<String> list1 = new ArrayList<String>();
	private static List<String> list2 = new ArrayList<String>();
	private static List<String> list3 = new ArrayList<String>();
	static {
		list1.add("nuggets-server");
		list1.add("message-center");
		list1.add("fso-lark");
		list1.add("zeus");
		list1.add("hades");
		list1.add("uic");
		list1.add("hera");
		list1.add("apollo");
		list1.add("va-web");
		list1.add("va-schedule");
		
		list2.add("xuanyuan");
		list2.add("newmanager");
		
		list3.add("cupid");
	}
	/**
	 *  重新发布所有项目
	 * 
		项目依赖关系
		nuggets-server ->cupid
		xuanyuan->cupid
		message-center->cupid,newmanager
		fso-lark->cupid
		zeus->
		hades->xuanyuan
		uic->cupid
		hera 												
		apollo
		va-schedule 
		va-web
		uicm
		项目启动顺序
		(nuggets-server,message-center,fso-lark,zeus,hades,uic,hera,apollo,va-web,va-schedule)->(xuanyuan,newmanager)->(cupid)
	 * @return
	 
	@RequestMapping(value="/{sid}/deployProjects", method=RequestMethod.GET)
	@ResponseBody
	public Object deployProjects(@PathVariable String sid, HttpServletRequest request, HttpServletResponse response) {
		
		
		
		return null;
	}*/
	
}
