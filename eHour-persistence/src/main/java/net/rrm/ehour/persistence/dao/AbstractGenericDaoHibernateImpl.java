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

package net.rrm.ehour.persistence.dao;

import net.rrm.ehour.domain.DomainObject;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * GenericDAO interface for CRUD on domain objects
 **/

@Repository
public abstract class AbstractGenericDaoHibernateImpl <T extends DomainObject<?, ?>, PK extends Serializable>
	extends AbstractAnnotationDaoHibernateImpl
	implements GenericDao<T, PK>
{
	private Class<T>	type;

	/**
	 * 1.5 aware dao's
	 * @param type
	 */
	public AbstractGenericDaoHibernateImpl(Class<T> type)
	{
		super();
		
		this.type = type;
	}

	/**
	 * 
	 * @param queryName
	 * @param param
	 * @param value
	 * @param isCaching
	 * @param cachingRegion
	 * @return
	 */
	public List<T> findByNamedQueryAndNamedParam(final String queryName, 
												final String param, 
												final Object value,
												final boolean isCaching,
												final String cachingRegion)
	{
		return findByNamedQueryAndNamedParam(queryName, new String[]{param}, new Object[]{value}, isCaching, cachingRegion);
	}
	
	/**
	 * Find named query optionally with caching enabled
	 * @param queryName
	 * @param paramNames
	 * @param values
	 * @param isCaching
	 * @param cachingRegion
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByNamedQueryAndNamedParam(final String queryName, 
												final String[] paramNames, 
												final Object[] values,
												boolean isCaching,
												final String cachingRegion)
					throws DataAccessException 
	{
		if (!isCaching)
		{
			return (List<T>)getHibernateTemplate().findByNamedQueryAndNamedParam(queryName, paramNames, values);
		}
		else
		{
			HibernateTemplate template = new HibernateTemplate(getHibernateTemplate().getSessionFactory())
			{
				@Override
				public boolean isCacheQueries()
				{
					return true;
				}
				
				public String getQueryCacheRegion()
				{
					return cachingRegion;
				}
			};
			
			return template.findByNamedQueryAndNamedParam(queryName, paramNames, values);
		}
	}
	
	/**
	 * Find all domain objects
	 */
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
	public T findById(PK id)
	{
		return getHibernateTemplate().get(type, id);
	}	
	
	/**
	 * Merge
	 * @param domobj
	 */
	public T merge(T domobj)
	{
		return getHibernateTemplate().merge(domobj);
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
