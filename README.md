# JeecgBoot

## 数据库

### 导入数据库
1. 在服务器上创建jeecg-boot数据库
2. 在/db目录下，执行jeecg-boot-bak.sql

### 备份数据库
`mysqldump -uroot -proot --default-character-set=utf8mb4 --hex-blob --ignore-table=table_name database_name --result-file=D:\xxx.sql`

## 后端
[后端布署](http://doc.jeecg.com/2043886)
### 构建项目
`mvn clean install -P prod`
### 打包容器
`docker-compose up -d`

## 前端
[前端布署](http://doc.jeecg.com/3043612)

### 构建项目
`yarn install`
`yarn run build`
### 构建容器
`docker build -t jeecgboot-ui2 .`
### 启动容器
`docker run --name jeecgboot-ui-vue2 -p 80:80 -d jeecgboot-ui2`
