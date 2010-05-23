/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common.authorization;

import java.util.Arrays;
import java.util.Collection;

import net.rrm.ehour.domain.User;

import org.springframework.security.core.GrantedAuthority;

public class AuthUser extends org.springframework.security.core.userdetails.User
{
	private	User		user;
	private static final long serialVersionUID = -9086733140310198830L;

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
	public AuthUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities) throws IllegalArgumentException
	{
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
	
	public AuthUser(User user)
	{
		this(user.getUsername(), user.getPassword(), true, true, true, true, 
				Arrays.asList((GrantedAuthority[]) (user.getUserRoles().toArray(new GrantedAuthority[user.getUserRoles().size()]))));
		
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
	
	public Object getSalt()
	{
		return (user != null) ? user.getSalt() : null;
	}
}
