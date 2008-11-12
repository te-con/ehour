package net.rrm.ehour.audit.dao;

import java.util.List;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.domain.Audit;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

public class AuditDAOHibernateImpl extends GenericDAOHibernateImpl<Audit, Number>  implements AuditDAO
{
	/**
	 * @todo fix this a bit better
	 */
	public AuditDAOHibernateImpl()
	{
		super(Audit.class);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.audit.dao.AuditDAO#findAudit(net.rrm.ehour.audit.service.dto.AuditReportRequest)
	 */
	@SuppressWarnings("unchecked")
	public List<Audit> findAudit(AuditReportRequest request)
	{
		Criteria criteria = getSession().createCriteria(Audit.class);
		
		if (request.getOffset() != null)
		{
			criteria.setFirstResult(request.getOffset());
		}
		
		if (request.getMax() != null)
		{
			criteria.setMaxResults(request.getMax());
		}

		criteria.addOrder(Order.asc("date"));
		
		// TODO implement rest of request
		
		return criteria.list();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.audit.dao.AuditDAO#findAuditCount(net.rrm.ehour.audit.service.dto.AuditReportRequest)
	 */
	public Number findAuditCount(AuditReportRequest request)
	{
		Criteria criteria = getSession().createCriteria(Audit.class);
		criteria.setProjection(Projections.rowCount());

		// TODO implement rest of request
		return ((Integer)criteria.list().get(0)).intValue();
	}
}
