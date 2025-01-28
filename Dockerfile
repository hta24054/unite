# 1. Base image 선택
FROM openjdk:17-jdk-slim

# 2. 컨테이너에서 작업 디렉토리 설정
WORKDIR /app

# 3. 애플리케이션 JAR 파일 복사
COPY build/libs/*SNAPSHOT.jar app.jar

# 4. 컨테이너 실행 시 실행할 명령어 지정
CMD ["java", "-jar", "/app/app.jar"]