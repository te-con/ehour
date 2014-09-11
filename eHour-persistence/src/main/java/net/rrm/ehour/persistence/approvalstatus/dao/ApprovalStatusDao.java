package net.rrm.ehour.persistence.approvalstatus.dao;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.persistence.dao.GenericDao;

public interface ApprovalStatusDao extends GenericDao<ApprovalStatus, Integer>{

	/**
	 * Returns all {@link ApprovalStatus}s corresponding to an {@link Activity} within passed {@link DateRange}.
	 * @param activity
	 * @param dateRange
	 * @return
	 */
	List<ApprovalStatus> findApprovalStatusesForActivity(Activity activity, DateRange dateRange);
}
