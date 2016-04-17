# 农科110项目 后台管理端
## 项目开发部署
### 项目中 client 文件夹是前端项目
#### 部署方法：

1. 在client项目中右键Git Bash Here
2. 安装nodejs （安装包）
3. 安装bower （npm install -g bower）
4. 安装grunt （npm install -g grunt）
5. 安装grunt-cli （npm install -g grunt-cli）
6. 安装依赖 （bower instal）
7. 安装npm 依赖 （npm install）
8. 运行前端服务器 （grunt serve）
9. 浏览器访问 127.0.0.1:9080 （需要先开启后台服务）

### server 文件夹是后端项目
#### 部署方法：

1. 导入到IDEA
2. 修改数据库配置文件
3. 创建相应数据库（无需建表）
4. sbt console
5. run
6. 访问 127.0.0.1:9000/app/posts 测试
