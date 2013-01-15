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
import net.rrm.ehour.data.DateRange;
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
	public AuditDaoHibernateImpl()
	{
		super(Audit.class);
	}

	@SuppressWarnings("unchecked")
    @Override
    public List<Audit> findAudits(AuditReportRequest request)
	{
		Criteria criteria = buildCriteria(request);
		criteria.addOrder(Order.asc("date"));
		
		return criteria.list();
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<Audit> findAudits(AuditReportRequest request, int offset, int max) {
        Criteria criteria = buildCriteria(request);
        criteria.setFirstResult(offset);
        criteria.setMaxResults(max);
        criteria.addOrder(Order.asc("date"));

        return criteria.list();
    }

    public Number count(AuditReportRequest request)
	{
		Criteria criteria = buildCriteria(request);
		criteria.setProjection(Projections.rowCount());

		return (Number)criteria.uniqueResult();
	}
	
	private Criteria buildCriteria(AuditReportRequest request)
	{
		Criteria criteria = getSession().createCriteria(Audit.class);

		if (!StringUtils.isBlank(request.getAction()))
		{
			criteria.add(Restrictions.like("action", "%" + request.getAction().toLowerCase() + "%").ignoreCase());
		}

		if (!StringUtils.isBlank(request.getName()))
		{
			criteria.add(Restrictions.like("userFullName", "%" + request.getName().toLowerCase() + "%").ignoreCase());
		}

        DateRange reportRange = request.getReportRange();
        if (!request.isInfiniteStartDate() && reportRange.getDateStart() != null)
		{
			criteria.add(Restrictions.ge("date", reportRange.getDateStart()));
		}

		if (!request.isInfiniteEndDate() && reportRange.getDateEnd() != null)
		{
			criteria.add(Restrictions.le("date", reportRange.getDateEnd()));
		}

		return criteria;
	}
}
