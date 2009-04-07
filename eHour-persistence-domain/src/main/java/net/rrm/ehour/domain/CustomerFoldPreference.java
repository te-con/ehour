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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Customer folded preference. Not folded by default
 **/

public class CustomerFoldPreference extends DomainObject<CustomerFoldPreferenceId, CustomerFoldPreference>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2008450265035488198L;
	
	private CustomerFoldPreferenceId	foldPreferenceId;
	private	Boolean						folded;
	
	public CustomerFoldPreference()
	{
		
	}
	
	public CustomerFoldPreference(User user, Customer customer, boolean folded)
	{
		foldPreferenceId = new CustomerFoldPreferenceId();
		foldPreferenceId.setCustomer(customer);
		foldPreferenceId.setUser(user);
		
		this.folded = Boolean.valueOf(folded);
	}
	
	/**
	 * @return the foldPreferenceId
	 */
	public CustomerFoldPreferenceId getFoldPreferenceId()
	{
		return foldPreferenceId;
	}
	/**
	 * @param foldPreferenceId the foldPreferenceId to set
	 */
	public void setFoldPreferenceId(CustomerFoldPreferenceId foldPreferenceId)
	{
		this.foldPreferenceId = foldPreferenceId;
	}
	/**
	 * @return the folded
	 */
	public Boolean getFolded()
	{
		return folded;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFolded()
	{
		return (folded != null) ? folded.booleanValue() : false;
	}
	
	/**
	 * Toggle folded status
	 */
	public void toggleFolded()
	{
		folded = Boolean.valueOf(!folded.booleanValue());
	}
	
	/**
	 * @param folded the folded to set
	 */
	public void setFolded(Boolean folded)
	{
		this.folded = folded;
	}
	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(CustomerFoldPreference myClass)
	{
		return new CompareToBuilder()
					.append(this.isFolded(), myClass.isFolded())
					.append(this.getFoldPreferenceId(), myClass.getFoldPreferenceId()).toComparison();
	}
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object)
	{
		if (!(object instanceof CustomerFoldPreference))
		{
			return false;
		}
		CustomerFoldPreference rhs = (CustomerFoldPreference) object;
		return new EqualsBuilder()
			.append(this.isFolded(), rhs.isFolded())
			.append(this.getFoldPreferenceId(), rhs.getFoldPreferenceId())
			.isEquals();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return new HashCodeBuilder(578491229, 13938277)
			.append(this.isFolded())
			.append(this.getFoldPreferenceId()).toHashCode();
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new ToStringBuilder(this).append("PK", this.getPK()).append("foldPreferenceId", this.foldPreferenceId).append("folded", this.folded).toString();
	}
	@Override
	public CustomerFoldPreferenceId getPK()
	{
		return foldPreferenceId;
	}
}
