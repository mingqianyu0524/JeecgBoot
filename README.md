# JeecgBoot

## Backend
[后端布署]([http://doc.jeecg.com/3043612](http://doc.jeecg.com/2043886))
### 构建项目
`mvn clean install -P prod`
### 打包容器
`docker-compose up -d`

## Frontend
[前端布署](http://doc.jeecg.com/3043612)

### 构建项目
`yarn install`
`yarn run build`
### 构建容器
`docker build -t jeecgboot-ui2 .`
### 启动容器
`docker run --name jeecgboot-ui-vue2 -p 80:80 -d jeecgboot-ui2`
