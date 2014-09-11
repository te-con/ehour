package net.rrm.ehour.approvalstatus.service;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.approvalstatus.dao.ApprovalStatusDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("approvalStatusService")
public class ApprovalStatusServiceImpl implements ApprovalStatusService {

	@Autowired
	private ApprovalStatusDao approvalStatusDao;
	
	@Override
	public List<ApprovalStatus> getApprovalStatusForUserWorkingForCustomer(User user, Customer customer, DateRange dateRange) {
		return approvalStatusDao.findApprovalStatusesForUserWorkingForCustomer(user, customer, dateRange);
	}

	@Override
	public List<ApprovalStatus> getApprovalStatusesForCustomers(List<Customer> customers, DateRange dateRange) {
		return approvalStatusDao.findApprovalStatusesForCustomers(customers, dateRange);
	}
	
	@Override
	@Transactional
	public void persist(ApprovalStatus approvalStatus) {
		approvalStatusDao.persist(approvalStatus);
	}


}
