FROM ubuntu:22.04
MAINTAINER smartlinks smartlinks@smartlinks.com.cn
COPY ./jdk-17.0.5 /usr/local/java/
ENV TZ Asia/Shanghai
RUN echo "${TZ}" > /etc/timezone \
	&& ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime \
	&& apt update \
	&& apt install -y telnet curl wget tzdata curl  fontconfig \
	&& rm -rf /var/lib/apt/lists/*
ENV LC_ALL C.UTF-8
ENV LANG C.UTF-8
ENV JAVA_HOME /usr/local/java
ENV JRE_HOME ${JAVA_HOME}/jre
ENV CLASSPATH .:${JAVA_HOME}/lib:${JRE_HOME}/lib
ENV PATH ${JAVA_HOME}/bin:$PATH
#RUN echo "export JAVA_HOME=/usr/local/java" >> /etc/profile
#RUN echo "export CLASSPATH=.:/usr/local/java/lib/dt.jar:/usr/local/java/lib/tools.jar" >> /etc/profile
#RUN echo "export PATH=$PATH:/usr/local/java/bin" >> /etc/profile
#RUN echo "source /etc/profile" >> ~/.bashrc
#RUN source /etc/profile