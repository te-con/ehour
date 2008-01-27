/**
 * Created on Nov 10, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.authorization;

import net.rrm.ehour.domain.User;

import org.acegisecurity.GrantedAuthority;


/**
 * Acegi wrapper around User
 */

public class AuthUser extends org.acegisecurity.userdetails.User
{
	private	User		user;
	/**
	 * 
	 */
	private static final long serialVersionUID = -9086733140310198830L;

	/**
	 * 
	 * @param username
	 * @param password
	 * @param enabled
	 * @param authorities
	 * @throws IllegalArgumentException
	 */
	public AuthUser(String username, String password, boolean enabled, GrantedAuthority[] authorities) throws IllegalArgumentException
	{
		this(username, password, enabled, true, true, authorities);
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param enabled
	 * @param accountNonExpired
	 * @param credentialsNonExpired
	 * @param authorities
	 * @throws IllegalArgumentException
	 */

	public AuthUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, GrantedAuthority[] authorities) throws IllegalArgumentException
	{
		this(username, password, enabled, accountNonExpired, credentialsNonExpired, true, authorities);
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @param enabled
	 * @param accountNonExpired
	 * @param credentialsNonExpired
	 * @param accountNonLocked
	 * @param authorities
	 * @throws IllegalArgumentException
	 */
	public AuthUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, GrantedAuthority[] authorities) throws IllegalArgumentException
	{
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
	
	/**
	 * 
	 * @param user
	 */
	public AuthUser(User user)
	{
		super(user.getUsername(), user.getPassword(), true, true, true, true, 
				(GrantedAuthority[]) (user.getUserRoles().toArray(new GrantedAuthority[user.getUserRoles().size()])));
		
		this.user = user;
	}

	/**
	 * 
	 * @return
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * 
	 * @param user
	 */
	public void setUser(User user)
	{
		this.user = user;
	}
}
