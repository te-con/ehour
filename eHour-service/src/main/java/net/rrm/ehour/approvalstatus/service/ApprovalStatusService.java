package net.rrm.ehour.approvalstatus.service;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;

public interface ApprovalStatusService {
	
	/**
	 * Returns {@link ApprovalStatus} within the {@link DateRange} for specified {@link User} and {@link Customer}
	 * @param user
	 * @param customer
	 * @param dateRange
	 * @return
	 */
	List<ApprovalStatus> getApprovalStatusForUserWorkingForCustomer(User user, Customer customer, DateRange dateRange);

	/**
	 * Returns all {@link ApprovalStatus}s corresponding to a {@link Customer}s within passed {@link DateRange}.
	 * @param customer
	 * @param dateRange
	 * @return
	 */
	List<ApprovalStatus> getApprovalStatusesForCustomers(List<Customer> customers, DateRange dateRange);
	
	/**
	 * Updates the state of passed {@link ApprovalStatus}
	 * @param approvalStatus
	 */
	void persist(ApprovalStatus approvalStatus);

}
