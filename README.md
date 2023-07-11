# yue-chip-platform
用gradle构建的DDD微服务架构

# yue-chip-platform
DDD microservices architecture built with gradle

# yue-chip-platform
请拉取DDD的具体实现业务 https://github.com/yue-chip/yue-chip-common-business-module  
前端代码 https://github.com/yue-chip/yue-chip-frontend  
前端代码（前端模块代码与为微服务接口一一对应拆分 具体实现逻辑在yue-chip-frontend仓库下的gateway项目以及 https://github.com/yue-chip/yue-chip-common-business-module 仓库下面的
upms工程->[frontend]的 [registerGateway.js]）

# yue-chip-platform
[common-business-module code] git clone https://github.com/yue-chip/yue-chip-common-business-module  
[frontend code]  --- git clone https://github.com/yue-chip/yue-chip-frontend

# 修改 [gradle.properties](gradle.properties) 中的 maven 发布到私服的地址  不发布到私服不需要改
```java 
mavenRepository=http://192.168.8.213:32129/repository/maven-public/
mavenSnapshots=http://192.168.8.213:32129/repository/maven-snapshots/
mavenUsername=admin
mavenPassword=
```

# 修改 modify [build.gradle](build.gradle) 中的repositories，如果没有自己的私服，需要将 maven { allowInsecureProtocol true; url mavenRepository } 删除，如果有需要修改 [gradle.properties](gradle.properties) 中的mavenRepository和mavenSnapshots地址

# modify [gradle.properties](gradle.properties)  Maven is published to the address of the nexus, and does not need to be changed if it is not published to the nexus
```java 
mavenRepository=http://192.168.8.213:32129/repository/maven-public/
mavenSnapshots=http://192.168.8.213:32129/repository/maven-snapshots/
mavenUsername=admin
mavenPassword=
```