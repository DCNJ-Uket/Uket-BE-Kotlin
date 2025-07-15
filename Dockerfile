# builder 단계
FROM openjdk:21-slim-bookworm AS builder
WORKDIR /usr/bin/app

COPY build/libs/*.jar app.jar

ENV TZ=Asia/Seoul

RUN java -Djarmode=layertools \
    -Dspring.profiles.active=dev \
    -Duser.timezone=Asia/Seoul \
    -jar app.jar extract

# runtime 단계
FROM openjdk:21-slim-bookworm AS runner

ARG WORK_DIR=/usr/bin/app
WORKDIR ${WORK_DIR}

ENV TZ=Asia/Seoul

COPY --from=builder ${WORK_DIR}/dependencies/ ./
COPY --from=builder ${WORK_DIR}/spring-boot-loader/ ./
COPY --from=builder ${WORK_DIR}/snapshot-dependencies/ ./
COPY --from=builder ${WORK_DIR}/application/ ./
COPY docker/run-java.sh /usr/bin/run-java.sh

# 패키지 설치
RUN apt-get update && \
    apt-get install -y wget dos2unix && \
    rm -rf /var/lib/apt/lists/*

# run-java.sh 권한 설정
RUN dos2unix /usr/bin/run-java.sh && chmod +x /usr/bin/run-java.sh

# 환경 변수 설정
ENV JAVA_MAIN_CLASS=org.springframework.boot.loader.launch.JarLauncher
ENV JAVA_APP_DIR=/usr/bin/app
ENV JAVA_LIB_DIR=/usr/bin/app
ENV JAVA_OPTIONS="-ea -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ExitOnOutOfMemoryError -Duser.timezone=Asia/Seoul"

ENTRYPOINT [ "sh", "/usr/bin/run-java.sh" ]
