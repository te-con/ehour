package net.rrm.ehour.persistence.approvalstatus.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;

@Repository("approvalStatusDao")
public class ApprovalStatusDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<ApprovalStatus, Integer> implements ApprovalStatusDao {

	protected final static String CACHEREGION = "query.ApprovalStatus";
	
	public ApprovalStatusDaoHibernateImpl() {
		super(ApprovalStatus.class);
	}

	@Override
	public List<ApprovalStatus> findApprovalStatusesForUserWorkingForCustomer(User user, Customer customer, DateRange dateRange) {
		List<ApprovalStatus> results = null;

		String[] keys = new String[] { "user", "customer", "dateStart", "dateEnd"};
		Object[] params = new Object[] { user, customer, dateRange.getDateStart(), dateRange.getDateEnd() };
		
		results = findByNamedQueryAndNamedParam("ApprovalStatus.findApprovalStatusesForUserWorkingForCustomer", keys, params, true, CACHEREGION);
		
		return results;		
	}

	@Override
	public List<ApprovalStatus> findApprovalStatusesForCustomers(List<Customer> customers, DateRange dateRange) {
		List<ApprovalStatus> results = null;

		String[] keys = new String[] {"customers", "dateStart", "dateEnd"};
		Object[] params = new Object[] {customers, dateRange.getDateStart(), dateRange.getDateEnd() };
		
		results = findByNamedQueryAndNamedParam("ApprovalStatus.findApprovalStatusesForCustomers", keys, params, true, CACHEREGION);
		
		return results;				
	}
}
