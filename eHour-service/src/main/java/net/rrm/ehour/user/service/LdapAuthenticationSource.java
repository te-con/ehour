package net.rrm.ehour.user.service;

import org.springframework.ldap.core.AuthenticationSource;

public class LdapAuthenticationSource implements AuthenticationSource {
    private String dn;
    private String password;

    public LdapAuthenticationSource(String dn, String password) {
        this.dn = dn;
        this.password = password;
    }

    @Override
    public String getPrincipal() {
        return dn;
    }

    @Override
    public String getCredentials() {
        return password;
    }
}
