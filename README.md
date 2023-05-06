# yue-chip-platform
用gradle构建的DDD微服务架构

# yue-chip-platform
DDD microservices architecture built with gradle

#修改 [gradle.properties](gradle.properties) 中的 maven 发布到私服的地址  不发布到私服不需要改
```java 
mavenRepository=http://192.168.8.213:32129/repository/maven-public/
mavenSnapshots=http://192.168.8.213:32129/repository/maven-snapshots/
mavenUsername=admin
mavenPassword=
```

#modify [gradle.properties](gradle.properties)  Maven is published to the address of the private server, and does not need to be changed if it is not published to the private server
```java 
mavenRepository=http://192.168.8.213:32129/repository/maven-public/
mavenSnapshots=http://192.168.8.213:32129/repository/maven-snapshots/
mavenUsername=admin
mavenPassword=
```