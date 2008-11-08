package net.rrm.ehour.audit.dao;

import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.domain.Audit;

public class AuditDAOHibernateImpl extends GenericDAOHibernateImpl<Audit, Number>  implements AuditDAO
{
	/**
	 * @todo fix this a bit better
	 */
	public AuditDAOHibernateImpl()
	{
		super(Audit.class);
	}

}
