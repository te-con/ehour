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

package net.rrm.ehour.persistence.audit.dao;

import net.rrm.ehour.data.AuditReportRequest;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("auditDao")
public class AuditDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<Audit, Number>  implements AuditDao
{
	/**
	 * @todo fix this a bit better
	 */
	public AuditDaoHibernateImpl()
	{
		super(Audit.class);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.audit.dao.AuditDAO#findAudit(net.rrm.ehour.persistence.persistence.audit.service.dto.AuditReportRequest)
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
	 * @see net.rrm.ehour.persistence.persistence.audit.dao.AuditDAO#findAuditCount(net.rrm.ehour.persistence.persistence.audit.service.dto.AuditReportRequest)
	 */
	public Number count(AuditReportRequest request)
	{
		Criteria criteria = buildCriteria(request, false);
		criteria.setProjection(Projections.rowCount());

		return (Number)criteria.uniqueResult();
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
	 * @see net.rrm.ehour.persistence.persistence.audit.dao.AuditDAO#findAuditAll(net.rrm.ehour.persistence.persistence.audit.service.dto.AuditReportRequest)
	 */
	public List<Audit> findAllAudits(AuditReportRequest request)
	{
		return findAudit(request, true);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.audit.dao.AuditDAO#findAudit(net.rrm.ehour.persistence.persistence.audit.service.dto.AuditReportRequest)
	 */
	public List<Audit> findAudit(AuditReportRequest request)
	{
		return findAudit(request, false);
	}
}
