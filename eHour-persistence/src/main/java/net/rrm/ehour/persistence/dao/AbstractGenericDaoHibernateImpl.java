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
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * GenericDAO interface for CRUD on domain objects
 */

@Repository
public abstract class AbstractGenericDaoHibernateImpl<T extends DomainObject<?, ?>, PK extends Serializable>
        extends AbstractAnnotationDaoHibernate4Impl
        implements GenericDao<T, PK> {
    private Class<T> type;

    public AbstractGenericDaoHibernateImpl(Class<T> type) {
        super();

        this.type = type;
    }

    public List<T> findByNamedQueryAndNamedParam(final String queryName,
                                                 final String param,
                                                 final Object value,
                                                 final boolean isCaching,
                                                 final String cachingRegion) {
        return findByNamedQueryAndNamedParam(queryName, new String[]{param}, new Object[]{value}, isCaching, cachingRegion);
    }


    public List<T> findByNamedQueryAndNamedParam(final String queryName,
                                                 final String[] paramNames,
                                                 final Object[] values) {
        return findByNamedQueryAndNamedParam(queryName, paramNames, values, false, "");
    }

    /**
     * Find named query optionally with caching enabled
     */
    @SuppressWarnings("unchecked")
    public List<T> findByNamedQueryAndNamedParam(final String queryName,
                                                 final String[] paramNames,
                                                 final Object[] values,
                                                 boolean isCaching,
                                                 final String cachingRegion) {
        Query query = getSession().getNamedQuery(queryName);

        for (int i = 0; i < values.length; i++) {
            query.setParameter(paramNames[i], values[i]);
        }

        if (isCaching) {
            query.setCacheable(true);
            query.setCacheRegion(cachingRegion);
        }

        return (List<T>)query.list();
    }

    /**
     * Find all domain objects
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        Criteria criteria = getSession().createCriteria(type);
        return (List<T>)criteria.list();
    }

    /**
     * Delete domain object
     */
    public void delete(T domObj) {
        getSession().delete(domObj);
    }

    /**
     *
     */
    public void delete(PK id) {
        T dom = findById(id);
        delete(dom);
    }

    /**
     * Persist domain object
     *
     * @param domObj
     * @return
     */
    public T persist(T domObj) {
        getSession().saveOrUpdate(domObj);
        return domObj;
    }

    /**
     * Find by ID
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public T findById(PK id) {
        return (T) getSession().load(type, id);
    }

    /**
     * Merge
     *
     * @param domobj
     */
    @SuppressWarnings("unchecked")
    public T merge(T domobj) {
        return (T) getSession().merge(domobj);
    }
}
