package com.nexasi;

import org.jboss.logging.Logger;
import org.keycloak.email.DefaultEmailSenderProvider;
import org.keycloak.email.EmailException;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.UserModel;

public class CustomEventListenerProvider implements EventListenerProvider {

    private static final Logger log = Logger.getLogger(CustomEventListenerProvider.class);

    private final KeycloakSession session;
    private final RealmProvider model;

    public CustomEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();
    }

    public void onEvent(Event event) {
    	
    	log.infof("New Event is created in keycloak ###################################### NEW %s EVENT", event.getType());
        if (EventType.REGISTER.equals(event.getType())) {
            log.infof("## NEW %s EVENT", event.getType());
            log.info("-----------------------------------------------------------");

            RealmModel realm = this.model.getRealm(event.getRealmId());
            UserModel newRegisteredUser = this.session.users().getUserById(event.getUserId(), realm);

            String emailPlainContent = "New user registration\n\n" +
                    "Email: " + newRegisteredUser.getEmail() + "\n" +
                    "Username: " + newRegisteredUser.getUsername() + "\n" +
                    "Client: " + event.getClientId();

            String emailHtmlContent = "<h1>New user registration</h1>" +
                    "<ul>" +
                    "<li>Email: " + newRegisteredUser.getEmail() + "</li>" +
                    "<li>Username: " + newRegisteredUser.getUsername() + "</li>" +
                    "<li>Client: " + event.getClientId() + "</li>" +
                    "</ul>";

            DefaultEmailSenderProvider senderProvider = new DefaultEmailSenderProvider(session);
            AdminUser user = new AdminUser();
            log.info("Sending email to "+user.getEmail());
            try {
                senderProvider.send(session.getContext().getRealm().getSmtpConfig(), new AdminUser(), "Keycloak - New Registration", emailPlainContent, emailHtmlContent);
            } catch (EmailException e) {
                log.error("Failed to send email", e);
            }catch (Exception ex) {
                log.error("Failed to send email", ex);
            }
            log.info("-----------------------------------------------------------");
        }

    }

    public void onEvent(AdminEvent adminEvent, boolean b) {
    	log.infof("New Admin Event is created in keycloak ###################################### NEW %s EVENT", adminEvent.getOperationType());
    	log.info("Admin Event Occurred:" + toString(adminEvent));
    	log.info("adminEvent.getRepresentation(): "+adminEvent.getRepresentation());
    	log.info("adminEvent.getResourceTypeAsString(): "+adminEvent.getResourceTypeAsString());
    	log.info("getAuthDetails().toString(): "+adminEvent.getAuthDetails().toString());
    	if (OperationType.CREATE.equals(adminEvent.getOperationType()) && ResourceType.USER.equals(adminEvent.getResourceType()) ) {
    		RealmModel realm = this.model.getRealm(adminEvent.getRealmId());
    		String userDetails = adminEvent.getRepresentation();
    		String userName = userDetails.substring(userDetails.indexOf(":",userDetails.indexOf("\"username\":"))+2, userDetails.indexOf(",", userDetails.indexOf(":",userDetails.indexOf("\"username\":")))-1);
    		String password = userDetails.substring(userDetails.indexOf("value\":",userDetails.indexOf("\"type\":\"password\",",userDetails.indexOf("\"credentials\":[")))+8, userDetails.indexOf(",", userDetails.indexOf("value\":",userDetails.indexOf("\"type\":\"password\",",userDetails.indexOf("\"credentials\":["))))-1);
			/*
			 * 
			 * JSONObject userDetails = new JSONObject(adminEvent.getRepresentation());
			 * UserModel newRegisteredUser =
			 * this.session.users().getUserByUsername(userDetails.getString("username"),
			 * realm); JSONArray creds = userDetails.getJSONArray("credentials"); JSONObject
			 * passwordDetails = creds.getJSONObject(0);
			 */    
            String emailPlainContent = "New user registration\n\n" +
                    "Email: " +userName + "\n" +
                    "Username: " + userName + "\n" +
                    "Password: " +password;

            String emailHtmlContent = "<h1>New user registration</h1>" +
                    "<ul>" +
                    "<li>Email: " + userName + "</li>" +
                    "<li>Username: " +userName + "</li>" +
                    "<li>Password: " + password + "</li>" +
                    "</ul>";

            DefaultEmailSenderProvider senderProvider = new DefaultEmailSenderProvider(session);
            AdminUser user = new AdminUser();
            user.setEmail(userName);
            log.info("userName: "+userName);
            log.info("Sending email to user.getEmail() "+user.getEmail());
            try {
                senderProvider.send(session.getContext().getRealm().getSmtpConfig(), user, "Keycloak - New Registration", emailPlainContent, emailHtmlContent);
            } catch (EmailException e) {
                log.error("Failed to send email", e);
            }catch (Exception ex) {
                log.error("Failed to send email", ex);
            }

    	}
    }

    public void close() {

    }
    
    private String toString(AdminEvent adminEvent) {

        StringBuilder sb = new StringBuilder();


        sb.append("operationType=");

        sb.append(adminEvent.getOperationType());

        sb.append(", realmId=");

        sb.append(adminEvent.getAuthDetails().getRealmId());

        sb.append(", clientId=");

        sb.append(adminEvent.getAuthDetails().getClientId());

        sb.append(", userId=");

        sb.append(adminEvent.getAuthDetails().getUserId());

        sb.append(", ipAddress=");

        sb.append(adminEvent.getAuthDetails().getIpAddress());

        sb.append(", resourcePath=");

        sb.append(adminEvent.getResourcePath());


        if (adminEvent.getError() != null) {

            sb.append(", error=");

            sb.append(adminEvent.getError());

        }


        return sb.toString();

    }
}
