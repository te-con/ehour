package net.rrm.ehour.user.service;

import net.rrm.ehour.domain.User;

import java.io.Serializable;


public class LdapUser implements Serializable {
    public final String uid;
    public final String fullName;
    public final String email;
    public final String dn;
    private User user;

    public LdapUser(String fullName, String uid, String email, String dn) {
        this.fullName = fullName;
        this.uid = uid;
        this.email = email;
        this.dn = dn;
    }

    public boolean isAuthorized() {
        return user != null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
