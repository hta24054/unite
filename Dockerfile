# 1. Base image 선택
FROM openjdk:17-jdk-slim

# 2. 컨테이너에서 작업 디렉토리 설정
WORKDIR /app

# 3. 애플리케이션 JAR 파일 복사
COPY build/libs/*SNAPSHOT.jar app.jar

# 4. 로그 디렉터리 생성 (로그를 파일로 저장하려면 필요)
RUN mkdir -p /app/logs

# 5. 실행 명령어 (stdout 및 stderr를 파일로 리디렉션)
CMD ["sh", "-c", "java -jar /app/app.jar > /app/logs/app.log 2>&1"]