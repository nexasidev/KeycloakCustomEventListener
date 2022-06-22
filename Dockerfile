#ARG USER=socialnexasi
#ARG UID=1000
#ARG GID=1000
# default password for user
#ARG PW=Summer@91


#RUN useradd -m ${USER} --uid=${UID} && echo "${USER}:${PW}" | \
#      chpasswd

# For Java 8, try this
# FROM openjdk:8-jdk-alpine

# For Java 11, try this
FROM jboss/keycloak
#FROM adoptopenjdk/openjdk13
#FROM amazoncorretto:11

# Refer to Maven build -> finalName
#ARG JAR_FILE=target/KeycloakCustomEventListener-0.0.1-SNAPSHOT.jar

#COPY ./theme/zzz-base /opt/jboss/keycloak/themes/zzz-base

COPY ./target/KeycloakCustomEventListener-0.0.1-SNAPSHOT.jar /opt/jboss/keycloak/standalone/deployments
COPY ./src/main/resources/themes /opt/jboss/keycloak/themes
COPY ./src/main/resources/WelcomeMailTemplates /opt/jboss/keycloak/welcome-content
COPY ./src/main/resources/properties/messages_en.properties /opt/jboss/keycloak/themes/base/email/messages
COPY ./src/main/resources/themes/base/messages_en.properties /opt/jboss/keycloak/themes/base/login/messages
COPY ./src/main/resources/themes/base/login-reset-password.ftl /opt/jboss/keycloak/themes/base/login
COPY ./src/main/resources/themes/base/login-update-password.ftl /opt/jboss/keycloak/themes/base/login

# cd /opt/app
#WORKDIR /opt/app

# cp target/ntauction-0.0.1-SNAPSHOT.jar /opt/app/app.jar
#COPY ${JAR_FILE} app.jar

#EXPOSE 8080

# java -jar /opt/app/app.jar
#ENTRYPOINT ["java","-jar","app.jar"]

#USER 1000
#ENTRYPOINT [ “/opt/jboss/tools/docker-entrypoint.sh” ]
#CMD ["-b", “0.0.0.0”]