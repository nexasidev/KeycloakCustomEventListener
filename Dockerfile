# Use the stable official mirror for Keycloak 15.0.2
FROM quay.io/keycloak/keycloak:15.0.2

# Copy the Custom Event Listener JAR
COPY ./target/KeycloakCustomEventListener-0.0.1-SNAPSHOT.jar /opt/jboss/keycloak/standalone/deployments

# Copy Themes and Welcome Mail Templates
COPY ./src/main/resources/themes /opt/jboss/keycloak/themes
COPY ./src/main/resources/WelcomeMailTemplates /opt/jboss/keycloak/welcome-content

# Copy Email Property Overrides
COPY ./src/main/resources/properties/messages_en.properties /opt/jboss/keycloak/themes/base/email/messages
