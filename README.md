# yue-chip-platform
用gradle构建的DDD微服务架构

# yue-chip-platform
DDD microservices architecture built with gradle

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