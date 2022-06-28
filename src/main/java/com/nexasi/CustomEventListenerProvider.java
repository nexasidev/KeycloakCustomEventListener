package com.nexasi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
    		log.info("userDetails: "+userDetails);
    		String userName = userDetails.substring(userDetails.indexOf(":",userDetails.indexOf("\"username\":"))+2, userDetails.indexOf(",", userDetails.indexOf(":",userDetails.indexOf("\"username\":")))-1);
    		String password = userDetails.substring(userDetails.indexOf("value\":",userDetails.indexOf("\"type\":\"password\",",userDetails.indexOf("\"credentials\":[")))+8, userDetails.indexOf(",", userDetails.indexOf("value\":",userDetails.indexOf("\"type\":\"password\",",userDetails.indexOf("\"credentials\":["))))-1);
    		String firstName = userDetails.substring(userDetails.indexOf(":",userDetails.indexOf("\"firstName\":"))+2, userDetails.indexOf(",", userDetails.indexOf(":",userDetails.indexOf("\"firstName\":")))-1);

			String emailPlainContent =""; 
			log.info("Fetching mail template for "+realm.getName() +" realm");
        	StringBuffer emailHtmlContent = new StringBuffer();
        	BufferedReader bufferedReader = null;
        	try {
        	        //FileReader fileReader = new FileReader("/opt/jboss/keycloak/welcome-content/"+realm.getName()+".html");
        	        FileReader fileReader = new FileReader("/opt/jboss/keycloak/welcome-content/dotstow.html");
        	        bufferedReader = new BufferedReader(fileReader);
        	        String line;
        	        while ((line = bufferedReader.readLine()) != null) {
        	           emailHtmlContent.append(line);
        	        }
        			log.info("emailHtmlContent: "+emailHtmlContent.toString());
        	} catch (FileNotFoundException e1) {
        		log.info("Unable to send welcome email, template not found: ",e1);
			} catch (IOException e1) {
        		log.info("Unable to send welcome email, I/O exception: ",e1);
			}finally {
				try {
					bufferedReader.close();
				} catch (IOException e) {
	        		log.info("Unable to send welcome email, exception while closing buffered reader: ",e);
				}
		    }
        	
        	String emailHtmlContentString = emailHtmlContent.toString().replace("{Email}", userName);
        	emailHtmlContentString = emailHtmlContentString.replace("{userName}",userName);
            emailHtmlContentString = emailHtmlContentString.replace("{password}", password);
            emailHtmlContentString = emailHtmlContentString.replace("{firstName}", firstName);
            if(realm.getDisplayName() != null)
            	emailHtmlContentString = emailHtmlContentString.replace("{RealmDisplayName}", realm.getDisplayName());
            else
            	emailHtmlContentString = emailHtmlContentString.replace("{RealmDisplayName}", "");
            emailHtmlContentString = emailHtmlContentString.replace("{dotStowAppLink}", "http://dotstow-uat.s3-website.us-east-2.amazonaws.com/");
            DefaultEmailSenderProvider senderProvider = new DefaultEmailSenderProvider(session);
            AdminUser user = new AdminUser();
            user.setEmail(userName);
            log.info("userName: "+userName);
            log.info("Sending email to user.getEmail() "+user.getEmail());
            try {
                senderProvider.send(session.getContext().getRealm().getSmtpConfig(), user, "You have a new user account with DotStow", emailPlainContent, emailHtmlContentString);
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
