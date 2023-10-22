FROM bellsoft/liberica-openjdk-alpine:17.0.7

ARG JAVA_OPTS
EXPOSE 8080

ENV TZ "Europe/Moscow"
WORKDIR /app

ENTRYPOINT exec java $JAVA_OPTS \
    -Duser.language=ru \
    -Duser.region=RU \
    -Duser.timezone=${TZ} \
    -jar app.jar

COPY app-ktor/build/libs/app-ktor-all.jar app.jar
#COPY build/libs/home_work-1.0-SNAPSHOT.jar app.jar