package net.rrm.ehour.persistence.backup.dao;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoHibernate4Impl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 19, 2010 - 12:09:19 AM
 */
@Repository("importDao")
public class RestoreDaoHibernateImpl extends AbstractAnnotationDaoHibernate4Impl implements RestoreDao {
    @Override
    public <T extends DomainObject<?, ?>> Serializable persist(T object) {
        getSession().persist(object);

        return object.getPK();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, PK extends Serializable> T find(PK primaryKey, Class<T> type) {
        return (T) getSession().get(type, primaryKey);
    }

    public void flush() {
        Session session = getSession();
        session.flush();
        session.clear();
    }

    @Override
    public <T> void delete(Class<T> type) {
        if (type == User.class) {
            getSession().createSQLQuery("DELETE FROM USER_TO_USERROLE").executeUpdate();
        }

        getSession().createQuery("DELETE FROM " + type.getName()).executeUpdate();
    }
}
