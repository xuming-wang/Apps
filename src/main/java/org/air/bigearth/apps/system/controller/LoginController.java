package org.air.bigearth.apps.system.controller;

import org.air.bigearth.apps.base.BaseResult;
import org.air.bigearth.apps.constant.ResultConstants;
import org.air.bigearth.apps.system.domain.basic.User;
import org.air.bigearth.apps.system.service.IResourceService;
import org.air.bigearth.apps.system.service.IUserService;
import org.air.bigearth.apps.util.EdsUtil;
import org.air.bigearth.apps.util.ResultUtil;
import org.air.bigearth.apps.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 登陆 控制层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Controller
public class LoginController {
	@Resource
	private IUserService userService;
	
	@Autowired
	IResourceService resourceService;
	
	@RequestMapping(value="/system/userlogin")
	@ResponseBody
	public BaseResult<String> userLogin(User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
		}
		if(user == null){
			//return "login";
			return ResultUtil.error(ResultConstants.ACCOUNT_NOT_NULL);
		}

		String account=user.getLoginName();
		//if (StringUtil.isEmpty(account)) {
		//	return ResultUtil.error(ResultConstants.ACCOUNT_NOT_NULL);
		//}
		String password=user.getPassword();
		if (StringUtil.isEmpty(password)) {
			return ResultUtil.error(ResultConstants.PASSWORD_NOT_NULL);
		}

		if (StringUtil.isNotNullOrBlank(password)) {
			password = EdsUtil.encryptBasedDes(password);
		}

		UsernamePasswordToken token = new UsernamePasswordToken(account,password,false);
		token.setRememberMe(true);
		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.login(token);
			//SecurityUtils.getSubject().getSession().setTimeout(1800000);
			System.out.println(SecurityUtils.getSubject().getSession().getId());
			System.out.println(currentUser.getSession().getAttribute("loginUser"));
			currentUser.getSession().setAttribute("loginUser", user);

			// 此步将 调用realm的认证方法
		} catch(IncorrectCredentialsException e){
			return ResultUtil.error(ResultConstants.PASSWORD_ERROR);
		} catch (AuthenticationException e) {
			return ResultUtil.error(ResultConstants.ACCOUNT_NOT_EXIST);
		}
		
		return ResultUtil.success(ResultConstants.LOGIN_SUCCESS);
	}
	
	/**
	 * 配合shiro配置中的默认访问url
	 * @param request
	 * @param model
	 * @param session
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String getLogin(HttpServletRequest request,Model model,HttpSession session,HttpServletResponse response){
		return "login";
	}
	
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String index(){
		System.out.println("访问了后端 /  请求");
		return "login";
	}
	
	/**
	* 退出
	 * @return
	 */
	@RequestMapping(value="/system/logout",method =RequestMethod.GET)
	public String logout(HttpServletRequest request){
		
		//subject的实现类DelegatingSubject的logout方法，将本subject对象的session清空了
		//即使session托管给了redis ，redis有很多个浏览器的session
		//只要调用退出方法，此subject的、此浏览器的session就没了
		try {
			User user = (User)SecurityUtils.getSubject().getPrincipal();
			String userId=user.getId();
			resourceService.deleteRedis(userId);
			//退出
			SecurityUtils.getSubject().logout();
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return "login";
	}
	
	@RequestMapping(value="403",method=RequestMethod.GET)
	public String unAuth(){
		
		return "403";
	}

	
	/*@RequestMapping(value="play")
	public String play(){
		System.out.println("11111");
		return "NewFile";
	}*/

	/**
	 * 用户注册
	 *
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/register",method=RequestMethod.POST)
	@ResponseBody
	public BaseResult<String> register(User user, BindingResult result){
		if (StringUtil.isNotNullOrBlank(user.getPassword()) && !user.getPassword().equals(user.getConfirmPassword())) {
			return ResultUtil.error(ResultConstants.PASSWORD_NOT_SAME);
		}
		if (result.hasErrors()) {
			List<ObjectError> list =  result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println(error.getCode() + "---" + error.getArguments() + "---" + error.getDefaultMessage());
			}
			return ResultUtil.error(result.getAllErrors().get(0).getDefaultMessage());
		}
		boolean flag = userService.isExist(user.getLoginName());
		if (flag) {
			return ResultUtil.error(ResultConstants.ACCOUNT_EXIST);
		}
		if (StringUtil.isNotNullOrBlank(user) && StringUtil.isNotNullOrBlank(user.getPassword())) {
			user.setPassword(EdsUtil.encryptBasedDes(user.getPassword()));
		}

		userService.saveUser(user);
		return ResultUtil.success(ResultConstants.REGISTER_SUCCESS);
	}

	/**
	 * 密码重置
	 *
	 * @param user
	 * @return
	 */
	public BaseResult<String> resetPassword(User user) {
		if (StringUtil.isNotNullOrBlank(user) && StringUtil.isNotNullOrBlank(user.getPassword())) {
			user.setPassword(EdsUtil.encryptBasedDes("000000"));
		}
		//userService.saveUser(user);
		return ResultUtil.success(ResultConstants.RESET_PASSWORD);
	}




}
