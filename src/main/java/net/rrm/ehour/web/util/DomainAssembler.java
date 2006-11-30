/**
 * Created on Nov 24, 2006
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

package net.rrm.ehour.web.util;

import java.util.HashSet;
import java.util.Set;

import net.rrm.ehour.project.domain.Customer;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.domain.UserRole;
import net.rrm.ehour.web.admin.customer.form.CustomerForm;
import net.rrm.ehour.web.admin.dept.form.UserDepartmentForm;
import net.rrm.ehour.web.admin.user.form.UserForm;

/**
 * Various form to domain assemblers
 **/

public class DomainAssembler
{
	/**
	 * UserDepartmentForm to UserDepartment
	 * @param udf
	 * @return
	 */
	public static UserDepartment getUserDepartment(UserDepartmentForm udf)
	{
		UserDepartment ud = new UserDepartment();
		
		if (udf.getDepartmentId() > 0)
		{
			ud.setDepartmentId(udf.getDepartmentId());
		}
		
		ud.setName(udf.getName());
		ud.setCode(udf.getCode());
		return ud;
	}
	
	/**
	 * CustomerForm to Customer
	 * @param cf
	 * @return
	 */
	public static Customer getCustomer(CustomerForm cf)
	{
		Customer cust = new Customer();
		
		if (cf.getCustomerId() > 0)
		{
			cust.setCustomerId(cf.getCustomerId());
		}
		
		cust.setName(cf.getName());
		cust.setDescription(cf.getDescription());
		cust.setCode(cf.getCode());
		cust.setActive(cf.isActive());
		
		return cust;
	}
	
	/**
	 * UserForm to User
	 * @param uf
	 * @return
	 */

	public static User getUser(UserForm uf)
	{
		User	user = new User();
		UserDepartment dept;
		UserRole	role;
		Set			roles;
		int			i;
		
		if (uf.getUserId() > 0)
		{
			user.setUserId(uf.getUserId());
		}
		
		user.setActive(uf.isActive());
		user.setEmail(uf.getEmail());
		user.setFirstName(uf.getFirstName());
		user.setLastName(uf.getLastName());
		user.setPassword(uf.getPassword());
		user.setUsername(uf.getUsername());

		dept = new UserDepartment();
		dept.setDepartmentId(uf.getDepartmentId());
		user.setUserDepartment(dept);
		
		roles = new HashSet();
		
		for (i = 0;
			 i < uf.getRoles().length;
			 i++)
		{
			role = new UserRole(uf.getRoles()[i]);
			roles.add(role);
		}
		
		user.setUserRoles(roles);

		return user;
	}
}
