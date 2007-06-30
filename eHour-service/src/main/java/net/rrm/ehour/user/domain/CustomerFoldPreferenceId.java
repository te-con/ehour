/**
 * Created on Jun 29, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.user.domain;

import java.io.Serializable;

import net.rrm.ehour.customer.domain.Customer;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Customer fold preference ID
 **/

public class CustomerFoldPreferenceId implements Serializable, Comparable<CustomerFoldPreferenceId>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1538293978566677461L;
	
	private User		user;
	private	Customer	customer;
	
	/**
	 * 
	 */
	public int compareTo(CustomerFoldPreferenceId o)
	{
		int compare;
		
		compare = user.compareTo(o.getUser());
		
		if (compare == 0)
		{
			compare = customer.compareTo(o.getCustomer()); 
		}
		
		return compare; 
	}

	/**
	 * @return the user
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user)
	{
		this.user = user;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer()
	{
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer)
	{
		this.customer = customer;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object)
	{
		if (!(object instanceof CustomerFoldPreferenceId))
		{
			return false;
		}
		CustomerFoldPreferenceId rhs = (CustomerFoldPreferenceId) object;
		return new EqualsBuilder().appendSuper(super.equals(object)).append(this.customer, rhs.customer).append(this.user, rhs.user).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return new HashCodeBuilder(774815319, -1166942733).appendSuper(super.hashCode()).append(this.customer).append(this.user).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new ToStringBuilder(this).append("user", this.user).append("customer", this.customer).toString();
	}

}
