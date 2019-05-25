-- ----------------------------
-- Table structure for apps_t_sys_authority
-- ----------------------------
DROP TABLE IF EXISTS apps_t_sys_authority;
CREATE TABLE apps_t_sys_authority (
  id varchar(50) NOT NULL,
  name varchar(202) ,
  code varchar(202) ,
  url varchar(200) ,
  method varchar(200) ,
  controller varchar(200) ,
  des varchar(200) ,
PRIMARY KEY (id)
);

-- ----------------------------
-- Records of apps_t_sys_authority
-- ----------------------------
INSERT INTO apps_t_sys_authority VALUES ('047b2f11-3a43-11e8-af4b-507b9dc8fad2', '校验用户名唯一性，账号', 'user_isExist', '/system/user/isExist', 'GET', 'UserController', '''');
INSERT INTO apps_t_sys_authority VALUES ('12495b97-3a43-11e8-af4b-507b9dc8fad2', '查单个用户所拥有的角色id', 'user_getRoleByUser', '/system/user/getRoleByUser', 'GET', 'UserController', '''');
INSERT INTO apps_t_sys_authority VALUES ('18230089-3a42-11e8-af4b-507b9dc8fad2', '新增角色', 'role_saveRole', '/system/role', 'POST', 'RoleController', '''');
INSERT INTO apps_t_sys_authority VALUES ('1c68960f-3a41-11e8-af4b-507b9dc8fad2', '按controller分组，得到authority列表', 'authority_getAuthority', '/system/authority', 'GET', 'AuthorityController', '''');
INSERT INTO apps_t_sys_authority VALUES ('278e9498-3a42-11e8-af4b-507b9dc8fad2', '删除角色', 'role_deleteRole', '/system/role/{roleId}', 'DELETE', 'RoleController', '''');
INSERT INTO apps_t_sys_authority VALUES ('331a840d-3a42-11e8-af4b-507b9dc8fad2', '修改角色', 'role_updateRole', '/system/role/{roleId}', 'PUT', 'RoleController', '''');
INSERT INTO apps_t_sys_authority VALUES ('3aa50cdc-3a41-11e8-af4b-507b9dc8fad2', '根据请求方式，按关键字模糊查询', 'authority_getLikeAuthority', '/system/getLikeAuthority', 'GET', 'AuthorityController', '''');
INSERT INTO apps_t_sys_authority VALUES ('46d1ae66-3a42-11e8-af4b-507b9dc8fad2', '查询角色列表', 'role_getRoleList', '/system/role', 'GET', 'RoleController', '''');
INSERT INTO apps_t_sys_authority VALUES ('6bd80bdb-3a42-11e8-af4b-507b9dc8fad2', '给角色授权', 'role_authorize', '/system/role/authorize', 'POST', 'RoleController', '''');
INSERT INTO apps_t_sys_authority VALUES ('78e52b25-3a41-11e8-af4b-507b9dc8fad2', '新增资源', 'resource_addResource', '/system/resource', 'POST', 'ResourceController', '''');
INSERT INTO apps_t_sys_authority VALUES ('7eee4505-3a42-11e8-af4b-507b9dc8fad2', '校验code唯一性', 'role_isExist', '/system/role/isExist', 'GET', 'RoleController', '''');
INSERT INTO apps_t_sys_authority VALUES ('880e79f3-3a41-11e8-af4b-507b9dc8fad2', '修改资源', 'resource_updateResource', '/system/resource', 'PUT', 'ResourceController', '''');
INSERT INTO apps_t_sys_authority VALUES ('8bc0323d-3a42-11e8-af4b-507b9dc8fad2', '得到某角色拥有的资源', 'role_getRoleResource', '/system/role/getRoleResource', 'GET', 'RoleController', '''');
INSERT INTO apps_t_sys_authority VALUES ('9e0c17e4-3a41-11e8-af4b-507b9dc8fad2', '删除资源', 'resource_deleteResource', '/system/resource/{resourceId}', 'DELETE', 'ResourceController', '''');
INSERT INTO apps_t_sys_authority VALUES ('a516aa5f-3a42-11e8-af4b-507b9dc8fad2', '新增用户', 'user_saveUser', '/system/user', 'POST', 'UserController', '''');
INSERT INTO apps_t_sys_authority VALUES ('b07a1165-3a41-11e8-af4b-507b9dc8fad2', '查询资源列表', 'resource_getAllResource', '/system/resource', 'GET', 'ResourceController', '''');
INSERT INTO apps_t_sys_authority VALUES ('b75a7c8b-3a42-11e8-af4b-507b9dc8fad2', '修改用户', 'user_updateUser', '/system/user', 'PUT', 'UserController', '''');
INSERT INTO apps_t_sys_authority VALUES ('c5951c74-3a42-11e8-af4b-507b9dc8fad2', '删除用户', 'user_deleteUser', '/system/user/{id}', 'DELETE', 'UserController', '''');
INSERT INTO apps_t_sys_authority VALUES ('c877f065-3a41-11e8-af4b-507b9dc8fad2', '查单个资源 拥有的操作码', 'resource_getAuthorityByResource', '/system/resource/getAuthorityByResource', 'GET', 'ResourceController', '''');
INSERT INTO apps_t_sys_authority VALUES ('d742bcfa-3a42-11e8-af4b-507b9dc8fad2', '将角色授予用户', 'user_delegate', '/system/user/delegate', 'POST', 'UserController', '''');
INSERT INTO apps_t_sys_authority VALUES ('de481f75-3a41-11e8-af4b-507b9dc8fad2', '取消授权,取消一个资源的某个 或一些 权限', 'resource_deleteResourceAuthority', '/system/resource/deleteResourceAuthority', 'POST', 'ResourceController', '''');
INSERT INTO apps_t_sys_authority VALUES ('f0853008-3a41-11e8-af4b-507b9dc8fad2', '给资源授权,单个操作码给单个资源', 'resource_authorize', '/system/resource/authorize', 'POST', 'ResourceController', '''');
INSERT INTO apps_t_sys_authority VALUES ('f927cf1e-3a42-11e8-af4b-507b9dc8fad2', '查所有用户', 'user_findAllUser', '/system/user', 'GET', 'UserController', '''');

-- ----------------------------
-- Table structure for apps_t_sys_resouce_authority
-- ----------------------------
DROP TABLE IF EXISTS apps_t_sys_resouce_authority;
CREATE TABLE apps_t_sys_resouce_authority (
  id varchar(50) NOT NULL,
  resource_id varchar(50) ,
  authority_id varchar(50) ,
PRIMARY KEY (id)
);

-- ----------------------------
-- Records of apps_t_sys_resouce_authority
-- ----------------------------
INSERT INTO apps_t_sys_resouce_authority VALUES ('0f7b28c9-38a3-4121-a0aa-eb4e7a5151f9', '0451e108-da3b-4b87-ae62-f2ee1beb7c9b', '12495b97-3a43-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('133057d7-de6b-49ad-9a6e-16158c5a01c0', '0451e108-da3b-4b87-ae62-f2ee1beb7c9b', 'f927cf1e-3a42-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('13f71763-01e6-4e4e-927a-c0154f3d224a', '2fc56a7f-b29c-42f7-8ade-d26ea3eaa0dc', '18230089-3a42-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('170b5874-edf3-4d50-92b1-bed9df58e968', '0d2adeca-08c7-4c3c-a332-6ec7262fb562', 'b07a1165-3a41-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('175005d6-4d57-4eb7-97e3-68c18e971524', '1efea1bc-aecd-4787-823d-55d63fe54abf', '331a840d-3a42-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('1dde580a-0712-4dd4-80b5-866d3977701c', '47e1a143-0fee-4a1e-8bdc-8d659497aec4', '278e9498-3a42-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('1f7569d6-1054-4e9f-a1f3-f84e6413fd25', 'ee83c7b6-d22d-4013-8172-b397f0f31b51', 'f0853008-3a41-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('2b6ed9cf-2fc1-4fc9-88f5-ef48d1ad7fb0', '376ccf3e-a713-4738-95fd-41e071b3e891', 'b75a7c8b-3a42-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('458e0f1d-5ba5-4be1-81af-e13b9ae7d2c7', '0d2adeca-08c7-4c3c-a332-6ec7262fb562', '8bc0323d-3a42-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('56f13076-7ad1-4c8e-9fee-55bbcd8e26cc', '5a9a6b50-789c-4b6c-83ae-7701b6259c51', '1c68960f-3a41-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('5e4e8229-deed-47ec-8d7e-28ff58644f36', 'c07f19e5-df3e-4893-b9dd-42ee09705b43', 'c5951c74-3a42-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('6aad5d47-f126-4727-9e29-0bee16a0ee3d', '03f1c9d6-b952-4c77-92d6-4ada566567c5', '46d1ae66-3a42-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('6bd89b1f-5112-4cbd-a9bf-b703493f6d7f', '0d2adeca-08c7-4c3c-a332-6ec7262fb562', '6bd80bdb-3a42-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('80dc6290-9817-423a-90e0-d571a7925df6', '376ccf3e-a713-4738-95fd-41e071b3e891', 'f927cf1e-3a42-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('87879b76-1303-4b30-a3b5-2fef65a2aeee', 'ee83c7b6-d22d-4013-8172-b397f0f31b51', 'c877f065-3a41-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('891b5331-576f-4919-8f42-702d4cad96ab', 'ee83c7b6-d22d-4013-8172-b397f0f31b51', 'de481f75-3a41-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('be5cc5b6-0658-4d79-9203-54462bdda829', '6d8cb0bc-c43e-4bf3-a14b-db0141174071', '880e79f3-3a41-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('c1317e64-4c4c-4c8b-a255-6d38f91597c9', 'c07f19e5-df3e-4893-b9dd-42ee09705b43', 'f927cf1e-3a42-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('cddeffa9-c790-4bee-afc3-b572b5348f07', '0451e108-da3b-4b87-ae62-f2ee1beb7c9b', 'd742bcfa-3a42-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('d3edb56b-a90f-4c44-85e9-28825d3be57b', '7a6fdedc-80c8-486a-b545-f7aab65be9c2', '9e0c17e4-3a41-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('e6303f91-41c4-48e7-ba04-1ec5109642dc', 'ee83c7b6-d22d-4013-8172-b397f0f31b51', '78e52b25-3a41-11e8-af4b-507b9dc8fad2');
INSERT INTO apps_t_sys_resouce_authority VALUES ('e863ab45-7c6e-49ad-a324-7f2616a083d6', 'e7307f87-2405-11e8-8806-3ef40d61c1a6', 'b07a1165-3a41-11e8-af4b-507b9dc8fad2');

-- ----------------------------
-- Table structure for apps_t_sys_resource
-- ----------------------------
DROP TABLE IF EXISTS apps_t_sys_resource;
CREATE TABLE apps_t_sys_resource (
  id varchar(50) NOT NULL,
  name varchar(20) ,
  type char(4) ,
  code varchar(200) ,
  pid varchar(50) ,
  des varchar(200) ,
  sort char(4) ,
  path varchar(200) ,
  menu_route varchar(255) ,
PRIMARY KEY (id)
);

-- ----------------------------
-- Records of apps_t_sys_resource
-- ----------------------------
INSERT INTO apps_t_sys_resource VALUES ('03f1c9d6-b952-4c77-92d6-4ada566567c5', '角色管理', '2', 'category_role', 'a4afe6f7-2405-11e8-8806-3ef40d61c1a6', '角色管理，对角色增删改、授权', '1', '/category_authorization/category_role', 'role_manage');
INSERT INTO apps_t_sys_resource VALUES ('0451e108-da3b-4b87-ae62-f2ee1beb7c9b', '用户授权', '3', 'btn_user_authorize', '6bb173da-2405-11e8-8806-3ef40d61c1a6', '用户授权', '2', '/category_usermanage/btn_user_authorize', '''');
INSERT INTO apps_t_sys_resource VALUES ('0d2adeca-08c7-4c3c-a332-6ec7262fb562', '角色授权', '3', 'btn_role_authorize', '03f1c9d6-b952-4c77-92d6-4ada566567c5', '角色授权按钮', '4', '/category_authorization/category_role/btn_role_authorize', '''');
INSERT INTO apps_t_sys_resource VALUES ('1efea1bc-aecd-4787-823d-55d63fe54abf', '修改角色', '3', 'btn_role_update', '03f1c9d6-b952-4c77-92d6-4ada566567c5', '修改角色按钮', '2', '/category_authorization/category_role/btn_role_update', '');
INSERT INTO apps_t_sys_resource VALUES ('200b5315-2406-11e8-8806-3ef40d61c1a6', '博客', '2', 'category_blog', 'pid', '博客栏目', '3', '/category_blog', 'blog');
INSERT INTO apps_t_sys_resource VALUES ('2fc56a7f-b29c-42f7-8ade-d26ea3eaa0dc', '新增角色', '3', 'btn_roeladd', '03f1c9d6-b952-4c77-92d6-4ada566567c5', '新增角色', '1', '/category_authorization/category_role/btn_roeladd', '');
INSERT INTO apps_t_sys_resource VALUES ('376ccf3e-a713-4738-95fd-41e071b3e891', '修改用户', '3', 'btn_user_update', '6bb173da-2405-11e8-8806-3ef40d61c1a6', '修改用户', '4', '/category_usermanage/btn_user_update', '''');
INSERT INTO apps_t_sys_resource VALUES ('3be78ae4-2406-11e8-8806-3ef40d61c1a6', '项目介绍', '2', 'category_introductio', 'pid', '项目介绍栏目', '4', '/category_introduction', 'introduction');
INSERT INTO apps_t_sys_resource VALUES ('47e1a143-0fee-4a1e-8bdc-8d659497aec4', '删除角色', '3', 'btn_role_delete', '03f1c9d6-b952-4c77-92d6-4ada566567c5', '删除角色，按钮', '3', '/category_authorization/category_role/btn_role_delete', '');
INSERT INTO apps_t_sys_resource VALUES ('5a9a6b50-789c-4b6c-83ae-7701b6259c51', '权限码管理', '2', 'category_authority', 'a4afe6f7-2405-11e8-8806-3ef40d61c1a6', '权限码管理', '3', '', 'authority_manage');
INSERT INTO apps_t_sys_resource VALUES ('6bb173da-2405-11e8-8806-3ef40d61c1a6', '用户管理', '2', 'category_usermanage', 'pid', '用户管理栏目，可以对用户增删改', '1', '/category_usermanage', 'user_manage');
INSERT INTO apps_t_sys_resource VALUES ('6d8cb0bc-c43e-4bf3-a14b-db0141174071', '修改资源', '3', 'btn_resource_update', 'e7307f87-2405-11e8-8806-3ef40d61c1a6', '修改资源，按钮', '3', '/category_authorization/category_resource/btn_resource_update', '');
INSERT INTO apps_t_sys_resource VALUES ('7a6fdedc-80c8-486a-b545-f7aab65be9c2', '删除资源', '3', 'btn_resource_delete', 'e7307f87-2405-11e8-8806-3ef40d61c1a6', '删除资源', '2', '/category_authorization/category_resource/btn_resource_delete', '');
INSERT INTO apps_t_sys_resource VALUES ('a4afe6f7-2405-11e8-8806-3ef40d61c1a6', '功能权限', '2', 'category_authorization', 'pid', '功能权限栏目，可以操作角色、资源、权限码', '2', '/category_authorization', '');
INSERT INTO apps_t_sys_resource VALUES ('c07f19e5-df3e-4893-b9dd-42ee09705b43', '删除用户', '3', 'btn_user_delete', '6bb173da-2405-11e8-8806-3ef40d61c1a6', '删除用户', '3', '/category_usermanage/btn_user_delete', '''');
INSERT INTO apps_t_sys_resource VALUES ('e7307f87-2405-11e8-8806-3ef40d61c1a6', '资源管理', '2', 'category_resource', 'a4afe6f7-2405-11e8-8806-3ef40d61c1a6', '资源管理栏目，可以操作资源增删改、授权', '2', '/category_authorization/category_resource', 'resource_manage');
INSERT INTO apps_t_sys_resource VALUES ('e7fdcb7c-43d9-423b-9c00-4847c21cfa35', '新增资源ok', '3', 'btn_resource_add', 'e7307f87-2405-11e8-8806-3ef40d61c1a6', '新增资源按钮', '1', '/category_authorization/category_resource/btn_resource_add', '');
INSERT INTO apps_t_sys_resource VALUES ('ee83c7b6-d22d-4013-8172-b397f0f31b51', '资源授权', '3', 'btn_resource_authorize', 'e7307f87-2405-11e8-8806-3ef40d61c1a6', '资源授权', '4', '/category_authorization/category_resource/btn_resource_authorize', '''');
INSERT INTO apps_t_sys_resource VALUES ('f1e4ec99-2800-4d09-a622-632a05e28f52', '用户新增', '3', 'btn_user_add', '6bb173da-2405-11e8-8806-3ef40d61c1a6', '''', '1', '/category_usermanage/btn_user_add', '''');

-- ----------------------------
-- Table structure for apps_t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS apps_t_sys_role;
CREATE TABLE apps_t_sys_role (
  id varchar(50) NOT NULL,
  name varchar(20) ,
  code varchar(20) ,
  sort char(4) ,
  type char(2) ,
  des varchar(200) ,
PRIMARY KEY (id)
);

-- ----------------------------
-- Records of apps_t_sys_role
-- ----------------------------
INSERT INTO apps_t_sys_role VALUES ('1ed77c6f-93a5-46c0-a865-29ec8901287c', '权限码管理员', 'MANAGER_AUTHORITY', '2', '2', '权限码');
INSERT INTO apps_t_sys_role VALUES ('9bd9b381-dbcc-4a48-bf0a-6951f569d686', '资源管理员', 'MANAGER_RESOURCE', '4', '2', '资源管理员');
INSERT INTO apps_t_sys_role VALUES ('a8bfc2b4-ff73-4c35-9a63-ae2c049d3b01', '普通用户', 'NORMAL', '5', '1', '普通用户');
INSERT INTO apps_t_sys_role VALUES ('eec145c6-59b4-488f-b597-3e3978566492', '角色管理员', 'MANAGER_ROLE', '3', '2', '角色管理员');
INSERT INTO apps_t_sys_role VALUES ('sdfdsfsdfadsfsa', '用户管理员', 'MANAGER_USER', '1', '1', '用户管理员');

-- ----------------------------
-- Table structure for apps_t_sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS apps_t_sys_role_resource;
CREATE TABLE apps_t_sys_role_resource (
  id varchar(50) NOT NULL,
  role_id varchar(50) ,
  resource_id varchar(50) ,
PRIMARY KEY (id)
);

-- ----------------------------
-- Records of apps_t_sys_role_resource
-- ----------------------------
INSERT INTO apps_t_sys_role_resource VALUES ('1d248c91-59c9-4802-a163-b48b3c541804', '1ed77c6f-93a5-46c0-a865-29ec8901287c', '5a9a6b50-789c-4b6c-83ae-7701b6259c51');
INSERT INTO apps_t_sys_role_resource VALUES ('3dcf7693-c063-4864-9b42-8504a1f4593d', '9bd9b381-dbcc-4a48-bf0a-6951f569d686', 'e7fdcb7c-43d9-423b-9c00-4847c21cfa35');
INSERT INTO apps_t_sys_role_resource VALUES ('46873177-8b52-4e8e-85b8-f192d06d2e3f', '9bd9b381-dbcc-4a48-bf0a-6951f569d686', '7a6fdedc-80c8-486a-b545-f7aab65be9c2');
INSERT INTO apps_t_sys_role_resource VALUES ('4ba1c852-cd54-48f8-8b3b-bb4d7e940e50', '9bd9b381-dbcc-4a48-bf0a-6951f569d686', '6d8cb0bc-c43e-4bf3-a14b-db0141174071');
INSERT INTO apps_t_sys_role_resource VALUES ('5caa5cfb-af72-4148-a935-757e722a621e', 'a8bfc2b4-ff73-4c35-9a63-ae2c049d3b01', '3be78ae4-2406-11e8-8806-3ef40d61c1a6');
INSERT INTO apps_t_sys_role_resource VALUES ('609c0f87-3a2b-4101-8fd3-2e3eb2d71629', 'eec145c6-59b4-488f-b597-3e3978566492', '2fc56a7f-b29c-42f7-8ade-d26ea3eaa0dc');
INSERT INTO apps_t_sys_role_resource VALUES ('7a23aae5-02d5-4283-8266-bc663d72ad87', 'eec145c6-59b4-488f-b597-3e3978566492', '03f1c9d6-b952-4c77-92d6-4ada566567c5');
INSERT INTO apps_t_sys_role_resource VALUES ('7e6203d9-9c37-41d1-a3c1-218fed052dea', '9bd9b381-dbcc-4a48-bf0a-6951f569d686', 'e7307f87-2405-11e8-8806-3ef40d61c1a6');
INSERT INTO apps_t_sys_role_resource VALUES ('81835f28-031e-4c43-ba6e-b87ace85b427', 'eec145c6-59b4-488f-b597-3e3978566492', '1efea1bc-aecd-4787-823d-55d63fe54abf');
INSERT INTO apps_t_sys_role_resource VALUES ('83292177-46b8-4fce-a4af-ac8152a1b298', 'a8bfc2b4-ff73-4c35-9a63-ae2c049d3b01', '200b5315-2406-11e8-8806-3ef40d61c1a6');
INSERT INTO apps_t_sys_role_resource VALUES ('a1ac4f7d-87ea-4ccd-a483-b417e4cf52cd', 'eec145c6-59b4-488f-b597-3e3978566492', '47e1a143-0fee-4a1e-8bdc-8d659497aec4');
INSERT INTO apps_t_sys_role_resource VALUES ('eace25e1-99a2-42b7-abfc-5956d1e7a013', 'sdfdsfsdfadsfsa', '6bb173da-2405-11e8-8806-3ef40d61c1a6');
INSERT INTO apps_t_sys_role_resource VALUES ('f7296a9d-fa67-4f7a-9647-fad03e0aae3a', 'eec145c6-59b4-488f-b597-3e3978566492', '0d2adeca-08c7-4c3c-a332-6ec7262fb562');

DROP TABLE IF EXISTS apps_t_sys_user;
CREATE TABLE apps_t_sys_user (
  id varchar(50) NOT NULL,
  login_name varchar(20) ,
  real_name varchar(20) ,
  password varchar(50) ,
  superman boolean ,
  phone varchar(11),
  email varchar(20),
  PRIMARY KEY (id)
);

ALTER TABLE apps_t_sys_user alter COLUMN password type character varying(50);
-- ----------------------------
-- Records of apps_t_sys_user
-- ----------------------------
INSERT INTO apps_t_sys_user VALUES ('05068cb0-131b-11e8-8806-3ef40d61c1a6', 'admin', 'admin', '12345678', true, '', '');
INSERT INTO apps_t_sys_user VALUES ('76fbd9f0-cc74-4314-b007-e66bed436b33', 'james', '勒布朗', '12345678', false, '', '');
INSERT INTO apps_t_sys_user VALUES ('8127a2e6-69d9-4622-98c2-b81d1942080c', 'curry', '库里', '12345678', false,'', '');
INSERT INTO apps_t_sys_user VALUES ('cd3d796e-1ddc-473f-9004-76bc65e19fd3', 'kobe', 'Bryen', '12345678', false, '', '');

-- ----------------------------
-- Table structure for apps_t_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS apps_t_sys_user_role;
CREATE TABLE apps_t_sys_user_role (
  id varchar(50) NOT NULL,
  user_id varchar(50) ,
  role_id varchar(50) ,
  yw_id varchar(50) ,
PRIMARY KEY (id)
);

-- ----------------------------
-- Records of apps_t_sys_user_role
-- ----------------------------
INSERT INTO apps_t_sys_user_role VALUES ('50b403e7-72b2-42f5-a0a7-8271e9b8342f', 'cd3d796e-1ddc-473f-9004-76bc65e19fd3', 'a8bfc2b4-ff73-4c35-9a63-ae2c049d3b01', '');
INSERT INTO apps_t_sys_user_role VALUES ('ae0b234b-43f6-4cd5-9733-d7b8d1e19434', '8127a2e6-69d9-4622-98c2-b81d1942080c', 'eec145c6-59b4-488f-b597-3e3978566492', '');
INSERT INTO apps_t_sys_user_role VALUES ('eaf561d9-8fa8-4a14-9c83-9c1b35eac5b2', '8127a2e6-69d9-4622-98c2-b81d1942080c', 'a8bfc2b4-ff73-4c35-9a63-ae2c049d3b01', '');

-- ----------------------------
-- Table structure for apps_t_sys_role_business
-- ----------------------------
--DROP TABLE IF EXISTS apps_t_sys_role_business;
--CREATE TABLE apps_t_sys_role_business (
  --id varchar(50) NOT NULL,
  --role_id varchar(50) ,
  --class_pk varchar(50) ,
  --class_name varchar(50) ,
--PRIMARY KEY (id)
--);

-- ----------------------------
-- Records of apps_t_sys_role_business
-- ----------------------------

DROP TABLE IF EXISTS apps_t_sys_attachment;
-- Create table
create table apps_t_sys_attachment
(
  attachmentid      VARCHAR(32)  not null,
  busiid            VARCHAR(32),
  busitype          VARCHAR(50),
  filename          VARCHAR(50)  not null,
  originalfilename  VARCHAR(500) not null,
  basepath          VARCHAR(300),
  filepath          VARCHAR(300) not null,
  filesize          integer,
  suffix            VARCHAR(50),
  encryptedtype     VARCHAR(30),
  encryptkey        VARCHAR(50),
  compressedflag    integer,
  signature         VARCHAR(50),
  strategyid        VARCHAR(32),
  uploader          VARCHAR(32),
  uploadtime        VARCHAR(19),
  attachmentversion integer default 1,
  createuserid      VARCHAR(32),
  dbversion         integer default 1,
  status            CHAR(1) default '1',
  updatetime        VARCHAR(30),
  savestate         integer default 1,
  filecode          VARCHAR(32),
  uploadmetadata    varchar(2000),
  uploadsource      varchar(60)
);
-- Add comments to the table
comment on table apps_t_sys_attachment
is '附件表';
-- Add comments to the columns
comment on column apps_t_sys_attachment.attachmentid
is '附件ID';
comment on column apps_t_sys_attachment.busiid
is '业务ID';
comment on column apps_t_sys_attachment.busitype
is '业务类型';
comment on column apps_t_sys_attachment.filename
is '文件名';
comment on column apps_t_sys_attachment.originalfilename
is '原始文件名';
comment on column apps_t_sys_attachment.basepath
is '基于路径(根路径)';
comment on column apps_t_sys_attachment.filepath
is '文件路径(完整路径)';
comment on column apps_t_sys_attachment.filesize
is '文件大小(单位KB)';
comment on column apps_t_sys_attachment.suffix
is '文件后缀名(不带点)';
comment on column apps_t_sys_attachment.encryptedtype
is '加密方式(值为NOCRYPTED时表示未加密)';
comment on column apps_t_sys_attachment.encryptkey
is '加密密钥';
comment on column apps_t_sys_attachment.compressedflag
is '是否压缩(1压缩 2不压缩)';
comment on column apps_t_sys_attachment.signature
is '文件签名';
comment on column apps_t_sys_attachment.strategyid
is '文件策略ID';
comment on column apps_t_sys_attachment.uploader
is '上传人';
comment on column apps_t_sys_attachment.uploadtime
is '上传时间';
comment on column apps_t_sys_attachment.attachmentversion
is '附件版本';
comment on column apps_t_sys_attachment.createuserid
is '创建人';
comment on column apps_t_sys_attachment.dbversion
is '数据版本';
comment on column apps_t_sys_attachment.status
is '逻辑删除标识(1存在 2删除)';
comment on column apps_t_sys_attachment.updatetime
is '最后更新时间';
comment on column apps_t_sys_attachment.savestate
is '保存状态（1为临时文件，2为正式文件）';
comment on column apps_t_sys_attachment.filecode
is '文件编码';
comment on column apps_t_sys_attachment.uploadmetadata
is '元数据';
-- Create/Recreate primary, unique and foreign key constraints
alter table apps_t_sys_attachment
  add constraint pk_apps_t_sys_attachment primary key (attachmentid);

DROP TABLE IF EXISTS apps_t_sys_filestrategy;
-- Create table
create table apps_t_sys_filestrategy
(
  strategyid          VARCHAR(32) not null,
  strategycode        VARCHAR(50) not null,
  strategyname        VARCHAR(50) not null,
  basepath            VARCHAR(50) not null,
  transporttype       VARCHAR(30) not null,
  transporthost       VARCHAR(100),
  transportport       VARCHAR(20),
  transportaccount    VARCHAR(100),
  transportpassword   VARCHAR(100),
  filesizelimit       integer,
  uploadfiletype      VARCHAR(200),
  forbidfiletype      VARCHAR(200),
  encrypttype         VARCHAR(30) not null,
  compressedflag      CHAR(1) default '1' not null,
  filepathrule        VARCHAR(50) not null,
  remark              VARCHAR(600) not null,
  createuserid        VARCHAR(32),
  createorgid         VARCHAR(32),
  dbversion           integer default 1,
  status              CHAR(1) default '1',
  updatetime          VARCHAR(30),
  state               CHAR(1) default '1',
  maxuploadfilenumber integer default 1,
  ismultifileupload   integer
);
-- Add comments to the table
comment on table apps_t_sys_filestrategy
is '文件策略表';
-- Add comments to the columns
comment on column apps_t_sys_filestrategy.strategyid
is '策略ID';
comment on column apps_t_sys_filestrategy.strategycode
is '策略编码';
comment on column apps_t_sys_filestrategy.strategyname
is '策略名称';
comment on column apps_t_sys_filestrategy.basepath
is '上传根路径';
comment on column apps_t_sys_filestrategy.transporttype
is '文件传输类型(方式)';
comment on column apps_t_sys_filestrategy.transporthost
is '文件传输目标主机地址';
comment on column apps_t_sys_filestrategy.transportport
is '文件传输端口号';
comment on column apps_t_sys_filestrategy.transportaccount
is '文件传输账号';
comment on column apps_t_sys_filestrategy.transportpassword
is '文件传输密码';
comment on column apps_t_sys_filestrategy.filesizelimit
is '文件大小限制(单位：M)';
comment on column apps_t_sys_filestrategy.uploadfiletype
is '允许文件上传类型';
comment on column apps_t_sys_filestrategy.forbidfiletype
is '禁止文件上传类型';
comment on column apps_t_sys_filestrategy.encrypttype
is '加密方式';
comment on column apps_t_sys_filestrategy.compressedflag
is '是否压缩(1压缩 2不压缩)';
comment on column apps_t_sys_filestrategy.filepathrule
is '文件存放路径规则';
comment on column apps_t_sys_filestrategy.remark
is '备注';
comment on column apps_t_sys_filestrategy.createuserid
is '创建人';
comment on column apps_t_sys_filestrategy.createorgid
is '创建人组织机构ID';
comment on column apps_t_sys_filestrategy.dbversion
is '数据版本';
comment on column apps_t_sys_filestrategy.status
is '逻辑删除标识(1存在 2删除)';
comment on column apps_t_sys_filestrategy.updatetime
is '最后更新时间';
comment on column apps_t_sys_filestrategy.state
is '状态(1启用 2停用)';
comment on column apps_t_sys_filestrategy.maxuploadfilenumber
is '最大上传文件个数';
comment on column apps_t_sys_filestrategy.ismultifileupload
is '是否多文件上传（1为是，2为否）';
-- Create/Recreate primary, unique and foreign key constraints
alter table apps_t_sys_filestrategy
  add constraint pk_apps_t_sys_filestrategy primary key (strategyid);

INSERT INTO apps_t_sys_filestrategy (strategyid, strategycode, strategyname, basepath, transporttype, transporthost, transportport, transportaccount, transportpassword, filesizelimit, uploadfiletype, forbidfiletype, encrypttype, compressedflag, filepathrule, remark, createuserid, createorgid, dbversion, status, updatetime, state, maxuploadfilenumber, ismultifileupload) VALUES ('41ef2ee8a0867785e0536a05a8c0a45d', 'default', '默认策略', '/localScpDir', '1', '', '', '', '', 2000, '*', '.jsp', '0', '2', '2', '默认文件策略,其他策略停用或删除,将自动采用默认策略', '8e11fbeb9da545b4a74fd1f5dfaef83b', 'd96b2a185e504a269e52f54131c03c9b', 1, '1', '2016-12-01 12:00:00', '1', 3, 2);
