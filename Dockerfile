FROM openjdk:21-slim-buster AS Builder
WORKDIR /usr/bin/app
COPY build/libs/*.jar app.jar
ENV TZ=Asia/Seoul
RUN ["java", "-Djarmode=layertools", "-Dspring.profiles.active=dev", "-Duser.timezone=$TZ" ,"-jar", "app.jar", "extract"]

FROM openjdk:21-slim-buster AS runner
ARG WORK_DIR=/usr/bin/app
WORKDIR ${WORK_DIR}
ENV TZ=Asia/Seoul

COPY --from=builder ${WORK_DIR}/dependencies/ ./
COPY --from=builder ${WORK_DIR}/spring-boot-loader/ ./
COPY --from=builder ${WORK_DIR}/snapshot-dependencies/ ./
COPY --from=builder ${WORK_DIR}/application/ ./
COPY docker/run-java.sh /usr/bin/run-java.sh

RUN apt-get update && apt-get install -y wget
# RUN wget -O dd-java-agent.jar 'https://github.com/DataDog/dd-trace-java/releases/latest/download/dd-java-agent.jar'

RUN apt-get update && apt-get install -y wget dos2unix
RUN dos2unix /usr/bin/run-java.sh
RUN chmod +x /usr/bin/run-java.sh

ENV JAVA_MAIN_CLASS org.springframework.boot.loader.launch.JarLauncher
ENV JAVA_APP_DIR /usr/bin/app
ENV JAVA_LIB_DIR /usr/bin/app
ENV JAVA_OPTIONS "-ea -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ExitOnOutOfMemoryError -Duser.timezone=$TZ"

ENTRYPOINT [ "sh", "/usr/bin/run-java.sh" ]
