/**
 * Created on Dec 4, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
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
 * GenericDAO interface for CRUD on domain objects
 **/

public abstract class GenericDAOHibernateImpl <T extends DomainObject, PK extends Serializable>
			extends HibernateDaoSupport
			implements GenericDAO<T, PK>
{
	private Class<T>	type;

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
	
	/**
	 * Merge
	 * @param domobj
	 */
	@SuppressWarnings("unchecked")
	public T merge(T domobj)
	{
		return (T)getHibernateTemplate().merge(domobj);
	}

	/**
	 * Convert pattern to sql pattern prefixing and suffixing with %
	 * @param pattern
	 * @return
	 */
	protected String patternToSqlPattern(String pattern)
	{
		if (pattern != null && !pattern.trim().equals(""))
		{
			pattern = pattern.toLowerCase();
			pattern = "%" + pattern + "%";
		}
		else
		{
			pattern = "%";
		}
		
		return pattern;
	}
}
