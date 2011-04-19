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

package net.rrm.ehour.domain;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USER_ROLE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserRole extends DomainObject<String, UserRole> implements GrantedAuthority
{
	private static final long serialVersionUID = 3806904191272349157L;

	public final static String	ROLE_CONSULTANT = "ROLE_CONSULTANT";
	public final static String	ROLE_REPORT = "ROLE_REPORT";
	public final static String	ROLE_PROJECTMANAGER = "ROLE_PROJECTMANAGER";
	public final static String	ROLE_ADMIN = "ROLE_ADMIN";

	public static final UserRole CONSULTANT = new UserRole(ROLE_CONSULTANT);
	public static final UserRole REPORT = new UserRole(ROLE_REPORT);
	public static final UserRole PROJECTMANAGER = new UserRole(ROLE_PROJECTMANAGER);
	public static final UserRole ADMIN = new UserRole(ROLE_ADMIN);
	

    @Id
    @Column(name = "ROLE", length = 128)
    @NotNull
	private String 	role;


    @NotNull
    @Column(name = "NAME", length = 128, nullable = false)
    private String	roleName;

	public UserRole()
	{
	}
	
	public UserRole(String role)
	{
		this.role = role;
	}

	public UserRole(String role, String roleName)
	{
		this.role = role;
		this.roleName = roleName;
		
	}

	public String getRoleName()
	{
		return roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public String getRole()
	{
		return role;
	}

	public void setRole(String role)
	{
		this.role = role;
	}

	public String getAuthority()
	{
		return role;
	}
	
    public String toString()
    {
    	return role;
    }

	@Override
	public String getPK()
	{
		return role;
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(UserRole object)
	{
		return new CompareToBuilder()
			.append(this.getRole(), object.getRole())
			.append(this.getRoleName(), object.getRoleName()).toComparison();
	}
	
    /**
     * 
     */
	@Override
    public int hashCode()
    {
    	return new HashCodeBuilder().append(getRole()).toHashCode();
    }	

	/**
	 * 
	 */
	@Override
    public boolean equals(Object obj)
    {
    	boolean isEqual = false;
    	
        if (obj instanceof String)
        {
            isEqual = obj.equals(this.getRole());
        }
        else if (obj instanceof UserRole)
        {
        	isEqual = ((UserRole)obj).getRole().equals(this.getRole());
        }

        return isEqual;
    }	
}
