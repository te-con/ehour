package net.rrm.ehour.persistence.approvalstatus.dao;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.dao.GenericDao;

public interface ApprovalStatusDao extends GenericDao<ApprovalStatus, Integer>{

	/**
	 * Returns all {@link ApprovalStatus}s corresponding to {@link User} and {@link Customer} within passed {@link DateRange}.
	 * @param user
	 * @param customer
	 * @param dateRange
	 * @return
	 */
	List<ApprovalStatus> findApprovalStatusesForUserWorkingForCustomer(User user, Customer customer, DateRange dateRange);
	
	/**
	 * Returns all {@link ApprovalStatus}s corresponding to a {@link Customer}s within passed {@link DateRange}.
	 * @param customer
	 * @param dateRange
	 * @return
	 */
	List<ApprovalStatus> findApprovalStatusesForCustomers(List<Customer> customers, DateRange dateRange);
}
