# 1. PIN THE EXACT VERSION (Change to 15.0.2.Final if this branch is for the older server)
FROM quay.io/keycloak/keycloak:15.0.2

# 2. COPY CUSTOM EXTENSIONS
COPY ./target/KeycloakCustomEventListener-0.0.1-SNAPSHOT.jar /opt/jboss/keycloak/standalone/deployments

# 3. COPY THEMES & TEMPLATES
COPY ./src/main/resources/themes /opt/jboss/keycloak/themes
COPY ./src/main/resources/WelcomeMailTemplates /opt/jboss/keycloak/welcome-content

# 4. COPY PROPERTIES & OVERRIDES (Includes your new login.ftl line)
COPY ./src/main/resources/properties/messages_en.properties /opt/jboss/keycloak/themes/base/email/messages
COPY ./src/main/resources/themes/base/messages_en.properties /opt/jboss/keycloak/themes/base/login/messages
COPY ./src/main/resources/themes/base/login-reset-password.ftl /opt/jboss/keycloak/themes/base/login
COPY ./src/main/resources/themes/base/login-update-password.ftl /opt/jboss/keycloak/themes/base/login
COPY ./src/main/resources/themes/base/login.ftl /opt/jboss/keycloak/themes/base/login

# Note: CMD and ENTRYPOINT are intentionally left to the base image's defaults.
# We handle the network binding (-b 0.0.0.0) directly inside the AWS ECS Task Definition.
