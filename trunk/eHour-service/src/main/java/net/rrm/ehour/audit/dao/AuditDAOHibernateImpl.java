package net.rrm.ehour.audit.dao;

import java.util.List;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.domain.Audit;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

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
	private List<Audit> findAudit(AuditReportRequest request, boolean ignoreOffset)
	{
		Criteria criteria = buildCriteria(request, ignoreOffset);
		criteria.addOrder(Order.asc("date"));
		
		return criteria.list();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.audit.dao.AuditDAO#findAuditCount(net.rrm.ehour.audit.service.dto.AuditReportRequest)
	 */
	public Number findAuditCount(AuditReportRequest request)
	{
		Criteria criteria = buildCriteria(request, false);
		criteria.setProjection(Projections.rowCount());

		return (Integer)criteria.uniqueResult();
	}
	
	/**
	 * Build criteria
	 * @param request
	 * @param ignoreOffset
	 * @return
	 */
	private Criteria buildCriteria(AuditReportRequest request, boolean ignoreOffset)
	{
		Criteria criteria = getSession().createCriteria(Audit.class);
		
		if (!ignoreOffset)
		{
			if (request.getOffset() != null)
			{
				criteria.setFirstResult(request.getOffset());
			}
			
			if (request.getMax() != null)
			{
				criteria.setMaxResults(request.getMax());
			}
		}
		
		if (!StringUtils.isBlank(request.getAction()))
		{
			criteria.add(Restrictions.like("action", "%" + request.getAction().toLowerCase() + "%").ignoreCase());
		}

		if (!StringUtils.isBlank(request.getName()))
		{
			criteria.add(Restrictions.like("userFullName", "%" + request.getName().toLowerCase() + "%").ignoreCase());
		}

		
		if (request.getReportRange().getDateStart() != null)
		{
			criteria.add(Restrictions.ge("date", request.getReportRange().getDateStart()));
		}

		if (request.getReportRange().getDateEnd() != null)
		{
			criteria.add(Restrictions.le("date", request.getReportRange().getDateEnd()));
		}

		return criteria;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.audit.dao.AuditDAO#findAuditAll(net.rrm.ehour.audit.service.dto.AuditReportRequest)
	 */
	public List<Audit> findAuditAll(AuditReportRequest request)
	{
		return findAudit(request, true);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.audit.dao.AuditDAO#findAudit(net.rrm.ehour.audit.service.dto.AuditReportRequest)
	 */
	public List<Audit> findAudit(AuditReportRequest request)
	{
		return findAudit(request, false);
	}
}
