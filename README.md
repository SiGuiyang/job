# Job任务调度
基于spring boot + mybatis + quartz +redis架构实现Job任务调度
***
## job
后端服务，使用spring boot + mybatis + redis + quartz方案设计。
## job-view
使用webpack 打包，vue.js和element-ui实现控制台页面渲染。采用axios实现http协议访问，与后台数据交互
## 项目特色
* mybatis mapper 设计方案使用注解@xxxProvider实现;
* 采用redis消息队列实现统一的job任务调度，所有的job调度都在此RedisMessageListener监听器实现
* job任务都存放在数据库中，当服务停止时，未完成执行的job任务将会存储在数据库中，直到下次服务启动将从数据库中执行未完成的job任务。
* 前后端数据分离，http服务访问跨域
