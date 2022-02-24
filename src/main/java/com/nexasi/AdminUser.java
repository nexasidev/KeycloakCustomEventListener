package com.nexasi;

import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdminUser implements UserModel {
	
	public String email;

    public String getId() {
        return null;
    }

   
    public String getUsername() {
        return null;
    }

   
    public void setUsername(String s) {

    }

   
    public Long getCreatedTimestamp() {
        return null;
    }

   
    public void setCreatedTimestamp(Long aLong) {

    }

   
    public boolean isEnabled() {
        return false;
    }

   
    public void setEnabled(boolean b) {

    }

   
    public void setSingleAttribute(String s, String s1) {

    }

   
    public void setAttribute(String s, List<String> list) {

    }

   
    public void removeAttribute(String s) {

    }

   
    public String getFirstAttribute(String s) {
        return null;
    }

   
    public List<String> getAttribute(String s) {
        return null;
    }

   
    public Map<String, List<String>> getAttributes() {
        return null;
    }

   
    public Set<String> getRequiredActions() {
        return null;
    }

   
    public void addRequiredAction(String s) {

    }

   
    public void removeRequiredAction(String s) {

    }

   
    public void addRequiredAction(RequiredAction requiredAction) {

    }

   
    public void removeRequiredAction(RequiredAction requiredAction) {

    }

   
    public String getFirstName() {
        return null;
    }

   
    public void setFirstName(String s) {

    }

   
    public String getLastName() {
        return null;
    }

   
    public void setLastName(String s) {

    }

   
    public String getEmail() {
        return email;
    }

   
    public void setEmail(String s) {
    		email =s;
    }

   
    public boolean isEmailVerified() {
        return false;
    }

   
    public void setEmailVerified(boolean b) {

    }

   
    public Set<GroupModel> getGroups() {
        return null;
    }

   
    public void joinGroup(GroupModel groupModel) {

    }

   
    public void leaveGroup(GroupModel groupModel) {

    }

   
    public boolean isMemberOf(GroupModel groupModel) {
        return false;
    }

   
    public String getFederationLink() {
        return null;
    }

   
    public void setFederationLink(String s) {

    }

   
    public String getServiceAccountClientLink() {
        return null;
    }

   
    public void setServiceAccountClientLink(String s) {

    }

   
    public Set<RoleModel> getRealmRoleMappings() {
        return null;
    }

   
    public Set<RoleModel> getClientRoleMappings(ClientModel clientModel) {
        return null;
    }

   
    public boolean hasRole(RoleModel roleModel) {
        return false;
    }

   
    public void grantRole(RoleModel roleModel) {

    }

   
    public Set<RoleModel> getRoleMappings() {
        return null;
    }

   
    public void deleteRoleMapping(RoleModel roleModel) {

    }
}
