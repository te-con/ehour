/**
 * Created on Dec 4, 2006
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

package net.rrm.ehour.dao;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.domain.DomainObject;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Generic dao stuff
 **/

public abstract class GenericDAOHibernateImpl <T extends DomainObject, PK extends Serializable>
			extends HibernateDaoSupport
			implements GenericDAO<T, PK>
{
	private Class<T>	type;

	/**
	 * 1.4 aware dao's
	 * @todo remove 1.4 dao's
	 */
	public GenericDAOHibernateImpl()
	{
	}

	/**
	 * 1.5 aware dao's
	 * @param type
	 */
	public GenericDAOHibernateImpl(Class<T> type)
	{
		super();
		
		this.type = type;
	}
	
	/**
	 * Find all domain objects
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll()
	{
		return getHibernateTemplate().loadAll(type);
	}
	
	/**
	 * Delete domain object
	 * @param domObj
	 */
	public void delete(T domObj)
	{
		getHibernateTemplate().delete(domObj);
	}
	
	/**
	 * 
	 */
	public void delete(PK id)
	{
		T dom = findById(id);
		delete(dom);
	}
	
	/**
	 * Persist domain object
	 * @param domObj
	 * @return
	 */
	public T persist(T domObj)
	{
		getHibernateTemplate().saveOrUpdate(domObj);
		
		return domObj;
	}	
	
	/**
	 * Find by ID
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T findById(PK id)
	{
		return (T)getHibernateTemplate().get(type, id);
	}	
}
