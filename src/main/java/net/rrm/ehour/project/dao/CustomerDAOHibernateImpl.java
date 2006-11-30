/**
 * Created on Nov 25, 2006
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

package net.rrm.ehour.project.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import net.rrm.ehour.project.domain.Customer;
import net.rrm.ehour.user.domain.User;

/**
 * TODO 
 **/

public class CustomerDAOHibernateImpl extends HibernateDaoSupport  implements CustomerDAO
{

	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.dao.CustomerDAO#delete(java.lang.Integer)
	 */
	public void delete(Customer customer)
	{
		getHibernateTemplate().delete(customer);
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.dao.CustomerDAO#findAll()
	 */
	public List findAll()
	{
		return getHibernateTemplate().loadAll(Customer.class);
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.dao.CustomerDAO#findById(java.lang.Integer)
	 */
	public Customer findById(Integer customerId)
	{
		return (Customer)getHibernateTemplate().get(Customer.class, customerId);
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.dao.CustomerDAO#persist(net.rrm.ehour.project.domain.Customer)
	 */
	public Customer persist(Customer customer)
	{
		getHibernateTemplate().saveOrUpdate(customer);
		
		return customer;
	}

	public List findAll(boolean active)
	{
		List	l;
		
		l = getHibernateTemplate().findByNamedQueryAndNamedParam("Customer.findAllWithActive", "active", new Boolean(active));
		
		return l;	
	}

}
