su postgres
bash-4.2$ psql
--\l 列出当前库
postgres=# \l
--创建数据库
postgres=# create database apps owner postgres;
--进入
postgres=# \c apps
--安装PostGis扩展
apps=# CREATE EXTENSION postgis;
apps=# CREATE EXTENSION postgis_topology;
apps=# CREATE EXTENSION ogr_fdw;

-- 图片合成表
drop table if exists apps_t_composite;
create table apps_t_composite (
  datakey     varchar(32) not null,
  sync   boolean ,
  context   varchar(30)      ,
  appName   varchar(30)      ,
  timeout         integer ,
  classPath   varchar(60),
  bandsList     varchar(60)    ,
  images       varchar(2000),
  outputPath        varchar(500),
  polygonStr   varchar(500),
  cellSize double precision ,
  selectMethod   varchar(32),
  hdfsUrl varchar(60),
  numPartitions      integer,
  userId varchar(32),
  primary key (datakey)
);

DROP TABLE IF EXISTS apps_t_data_access;
create table apps_t_data_access(
  id           VARCHAR(32) not null,
  url          VARCHAR(600) not null,
  param        text,
  pattern      VARCHAR(10),
  dataSet      VARCHAR(50),
  resultSet       text,
  creator       VARCHAR(32),
  createTime   VARCHAR(30) not null
);

alter table apps_t_data_access
  add constraint pk_apps_t_data_access primary key (id);

--postgresql 修改列类型
#alter table apps_t_sys_attachment alter COLUMN filesize type bigint ;
--postgresql删除列
#ALTER TABLE apps_t_sys_attachment DROP COLUMN uploader;
--添加字段
#ALTER TABLE apps_t_sys_attachment ADD COLUMN fileShare char(2)  DEFAULT '0';















