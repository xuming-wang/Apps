package org.air.bigearth.apps.system.service.impl;

import org.air.bigearth.apps.system.domain.basic.User;
import org.air.bigearth.apps.system.mapper.basic.UserMapper;
import org.air.bigearth.apps.system.mapper.extend.UserExtendMapper;
import org.air.bigearth.apps.system.service.IUserRoleService;
import org.air.bigearth.apps.system.service.IUserService;
import org.air.bigearth.apps.util.DataKeyUtil;
import org.air.bigearth.apps.exception.service.DataValidateFiledException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户 业务层处理
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {

	@Autowired
	UserExtendMapper userExtendMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	IUserRoleService userRoleService;
	
	/**
	 * 根据用户名查用户对象
	 * @param account
	 * @return
	 */
	@Override
	public User selectByAccount(String account){
		User user = userExtendMapper.selectByName(account);
		return user;
	}

	@Override
	public void saveUser(User user) {
		user.setId(DataKeyUtil.getDataKey());
		user.setSuperman(false);
		userMapper.insert(user);
	}

	@Override
	public void updateUser(User user) {
		userMapper.updateByPrimaryKey(user);
	}

	@Override
	public void deleteUser(String id) {
		if(StringUtils.isBlank(id)){
			throw new DataValidateFiledException("删除的用户id不能为空");
		}
		
		//校验用户id是否存在
		
		userRoleService.deleteByUserId(id);//删除用户下的角色关系
		userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<User> findAllUser() {
		List<User> selectAll = userMapper.selectAll();
		return selectAll;
	}

	@Override
	public Boolean isExist(String loginName) {
		return userExtendMapper.isExist(loginName)>0;
	}

	@Override
	public Boolean phoneIsExist(String phome) {
		return userExtendMapper.phoneUnique(phome) > 0;
	}

	@Override
	public Boolean emailIsExist(String email) {
		return userExtendMapper.emailUnique(email) > 0;
	}


}
