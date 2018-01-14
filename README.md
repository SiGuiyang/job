# Job任务调度
基于spring boot + mybatis + quartz +redis架构实现Job任务调度
***
## job
后端服务，使用spring boot + mybatis + redis + quartz方案设计。
## job-view
使用webpack 打包，vue.js和element-ui实现控制台页面渲染。采用axios实现http协议访问，与后台数据交互
## 项目特色
* Spring boot 项目设计方案；
* mybatis mapper 设计方案使用注解@xxxProvider实现；
* 采用redis消息队列实现统一的job任务调度，所有的job调度都在此RedisMessageListener.java监听器实现；
* 执行job任务都存放在数据库中，当服务停止时，未完成执行的job任务将会存储在数据库中，直到下次服务启动将从数据库中执行未完成的job任务。详细请看JobConfig.java 这个配置类
* 前后端数据分离，http服务访问跨域；
## 项目启动
1. 此项目启动需要如下几个工具：
* Node [https://nodejs.org/en/](https://nodejs.org/en/) 
* git [https://git-scm.com/](https://git-scm.com/) （可选，针对windows操作系统必选）
* redis [https://redis.io/](https://redis.io/)
* mysql [https://dev.mysql.com/downloads/mysql/](https://dev.mysql.com/downloads/mysql/)
* JDK [http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* Maven [https://maven.apache.org/](https://maven.apache.org/)
2. 开发IDE
* eclipse [https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/) 或者 IDEA [https://www.jetbrains.com/idea/](https://www.jetbrains.com/idea/)
* sublime text 3 [https://www.sublimetext.com/](https://www.sublimetext.com/) 或者 VS CODE [https://code.visualstudio.com/](https://code.visualstudio.com/) 或者 webstorm [https://www.jetbrains.com/webstorm/](https://www.jetbrains.com/webstorm/)
3. 启动服务
* 后端服务启动采用spring boot 方式运行mvn spring-boot:run ；
* 前端控制台服务启动，采用npm方式启动。先需要安装Node,Node中已经默认安装了npm；npm install 安装依赖包，npm run dev 启动控制台页面



