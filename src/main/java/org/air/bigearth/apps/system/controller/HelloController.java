package org.air.bigearth.apps.system.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 控制层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@RestController
//@Controller
public class HelloController {

	
	@RequestMapping(value="/hello",method=RequestMethod.GET)
	@RequiresPermissions(value="how_are_you")
	public String hello(){
		return "HelloWorld";
	}
}
