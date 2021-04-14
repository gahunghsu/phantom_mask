FROM openjdk:11.0.2
ADD target/springboot-postgres-docker-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

#COPY requirements.txt /app/requirements.txt
#ENTRYPOINT ["python", "./index.py"]
# 指令：docker build -t tom861012/imagedemo:0.0.1.RELEASE . 注意最後面的"."為當前目錄"
# FROM https://hub.docker.com/_/python
# WORKDIR 工作目錄
# COPY  複製的位置
# RUN    Build 時會執行的指令，下載相依套件
# EXPOSE   container 啟動時會監聽的port
# CMD   執行 Container 時的指令 -Dserver.port=$PORT $JAVA_OPTS