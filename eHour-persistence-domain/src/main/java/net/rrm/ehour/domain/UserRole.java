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


import org.acegisecurity.GrantedAuthority;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class UserRole extends DomainObject<String, UserRole> implements GrantedAuthority
{
	private static final long serialVersionUID = 3806904191272349157L;

	private String 	role;
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
