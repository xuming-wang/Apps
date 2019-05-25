package org.air.bigearth.apps.system.controller;


import org.air.bigearth.apps.base.BaseResult;
import org.air.bigearth.apps.util.ResultUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/system")
public class CommunityController {

    @RequiresPermissions(value={"public_community","administrator"},logical=Logical.OR)
    @RequestMapping(value = "/community", method = RequestMethod.GET)
    public BaseResult public_community() {
        return ResultUtil.success();
    }
}
