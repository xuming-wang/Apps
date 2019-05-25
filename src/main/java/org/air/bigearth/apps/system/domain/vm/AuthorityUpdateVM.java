package org.air.bigearth.apps.system.domain.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 权限验证对象
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-05-06
 */
@ApiModel("修改权限VM")
public class AuthorityUpdateVM {

	@NotBlank
	private String id;
	
	@ApiModelProperty("名称")
	@NotBlank
    private String name;

	@ApiModelProperty("操作码")
	@NotBlank
    private String code;

	@ApiModelProperty("url")
	@NotBlank
    private String url;

	@ApiModelProperty("请求方式")
	@NotBlank
    private String method;

	@ApiModelProperty("controller")
	@NotBlank
    private String controller;

	@ApiModelProperty("描述")
    private String des;
}
