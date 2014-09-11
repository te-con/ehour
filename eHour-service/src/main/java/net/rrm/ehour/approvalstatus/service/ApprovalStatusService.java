package net.rrm.ehour.approvalstatus.service;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ApprovalStatus;

public interface ApprovalStatusService {
	
	/**
	 * gets {@link ApprovalStatus} for the Date duration for the {@link Activity}
	 * @param activity
	 * @param dateRange
	 * @return
	 */
	List<ApprovalStatus> getApprovalStatusForActivity(Activity activity, DateRange dateRange);
	
	void persist(ApprovalStatus approvalStatus);

}
