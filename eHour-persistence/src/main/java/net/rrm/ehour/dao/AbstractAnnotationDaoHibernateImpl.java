package net.rrm.ehour.dao;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

@Repository
public class AbstractAnnotationDaoHibernateImpl
{
	@Autowired
	private HibernateTemplate hibernateTemplate;

	protected HibernateTemplate getHibernateTemplate()
	{
		return hibernateTemplate;
	}
	
	protected Session getSession()
	{
		return SessionFactoryUtils.getSession(getHibernateTemplate().getSessionFactory(), true);
	}
}
