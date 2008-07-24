package net.rrm.ehour.ui.authorization.security.ldap;

import org.acegisecurity.GrantedAuthority;

public class UserDetailsVO extends org.acegisecurity.userdetails.User{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6777211551975495153L;

	public UserDetailsVO(String arg0, String arg1, boolean arg2, boolean arg3, boolean arg4, boolean arg5, GrantedAuthority[] arg6) throws IllegalArgumentException {
		super(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
		// TODO Auto-generated constructor stub
	}

	public void setAuthorities(GrantedAuthority[] arg0) {
		// TODO Auto-generated method stub
		super.setAuthorities(arg0);
	}
	
}
