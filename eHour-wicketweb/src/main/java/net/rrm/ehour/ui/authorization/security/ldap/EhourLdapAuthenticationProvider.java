package net.rrm.ehour.ui.authorization.security.ldap;

import net.rrm.ehour.ui.authorization.AuthService;
import net.rrm.ehour.ui.authorization.AuthUser;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.ldap.LdapAuthenticationProvider;
import org.acegisecurity.providers.ldap.LdapAuthenticator;
import org.acegisecurity.providers.ldap.LdapAuthoritiesPopulator;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.ldap.LdapUserDetails;

public class EhourLdapAuthenticationProvider extends LdapAuthenticationProvider {
   
	
	private AuthService authService;
	
	public EhourLdapAuthenticationProvider(LdapAuthenticator authenticator) {
        super(authenticator);
    }

    /**
     * Creates a <code>UserDetails</code> instance ({@link YourAppUserDetails}) that provides the additional properties
     * <code>email</code> and <code>displayname</code>.
     */
    protected UserDetails createUserDetails(LdapUserDetails ldapUser, String username, String password) {
        
    	UserDetails userDetails = super.createUserDetails(ldapUser, username, password);
    	
    	AuthUser authUser = null;
    	if(authService!=null)
    		authUser = (AuthUser) authService.loadUserByUsername(username);
    	
    	AuthUser authUser1 = new AuthUser(username, password, true, true, true, true,
    			authUser.getAuthorities());
    	authUser1.setUser(authUser.getUser());
    	
        return authUser1;
    }

	public AuthService getAuthService() {
		return authService;
	}

	public void setAuthService(AuthService authService) {
		this.authService = authService;
	}
    
    
    
    
}