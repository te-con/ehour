package net.rrm.ehour.persistence.approvalstatus.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;

@Repository("approvalStatusDao")
public class ApprovalStatusDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<ApprovalStatus, Integer> implements ApprovalStatusDao {

	protected final static String CACHEREGION = "query.ApprovalStatus";
	
	public ApprovalStatusDaoHibernateImpl() {
		super(ApprovalStatus.class);
	}

	@Override
	public List<ApprovalStatus> findApprovalStatusesForActivity(Activity activity, DateRange dateRange) {
		List<ApprovalStatus> results = null;

		String[] keys = new String[] { "activity", "dateStart", "dateEnd"};
		Object[] params = new Object[] { activity, dateRange.getDateStart(), dateRange.getDateEnd() };
		
		results = findByNamedQueryAndNamedParam("ApprovalStatus.findApprovalStatusesForActivity", keys, params, true, CACHEREGION);
		
		return results;		
	}
}
