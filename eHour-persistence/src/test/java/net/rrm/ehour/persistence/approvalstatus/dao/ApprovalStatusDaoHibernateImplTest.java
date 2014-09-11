package net.rrm.ehour.persistence.approvalstatus.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.ApprovalStatusType;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.CustomerMother;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserMother;
import net.rrm.ehour.persistence.customer.dao.CustomerDao;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.persistence.user.dao.UserDao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * Tests for {@link ApprovalStatusDaoHibernateImplTest}
 */
public class ApprovalStatusDaoHibernateImplTest extends AbstractAnnotationDaoTest {

	@Autowired
	private ApprovalStatusDao approvalStatusDao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private UserDao userDao;

	public ApprovalStatusDaoHibernateImplTest() {
		super("dataset-approval-status.xml");
	}

	@Test
	public void shouldPersistApprovalStatusWithCustomerAndUser() {

		Customer customer = CustomerMother.createCustomer(1);
		User user = UserMother.createUser();

		ApprovalStatus approvalStatus = new ApprovalStatus();
		approvalStatus.setId(6);
		approvalStatus.setUser(user);
		approvalStatus.setCustomer(customer);

		approvalStatus.setStatus(ApprovalStatusType.IN_PROGRESS);

		approvalStatusDao.persist(approvalStatus);

		ApprovalStatus retrievedApprovalStatus = approvalStatusDao.findById(6);
		assertNotNull(retrievedApprovalStatus);
		assertEquals(ApprovalStatusType.IN_PROGRESS, retrievedApprovalStatus.getStatus());
		assertEquals(user.getUserId(), retrievedApprovalStatus.getUser().getUserId());
		assertEquals(customer.getCustomerId(), retrievedApprovalStatus.getCustomer().getCustomerId());
	}

	@Test
	public void shouldFindAllApprovalStatusesForSpecifiedUserAndCustomerWithInSpecifiedDateRange() {
		User retrievedUser = userDao.findById(2);
		Customer retrievedCustomer = customerDao.findById(2);

		List<ApprovalStatus> approvalStatuses = approvalStatusDao.findApprovalStatusesForUserWorkingForCustomer(retrievedUser,
				retrievedCustomer, new DateRange(new GregorianCalendar(2007, 10, 1).getTime(), new GregorianCalendar(2007, 10, 30)
						.getTime()));
		assertNotNull(approvalStatuses);
		assertEquals(1, approvalStatuses.size());
		ApprovalStatus associatedApprovalStatus = approvalStatuses.get(0);
		assertNotNull(associatedApprovalStatus);
		assertEquals(ApprovalStatusType.READY_FOR_APPROVAL, associatedApprovalStatus.getStatus());
	}

	@Test
	public void shouldReturnEmptyListWhenCombinationOfUserAndCustomerIsNotCorrect() {
		User retrievedUser = userDao.findById(1);
		Customer retrievedCustomer = customerDao.findById(2);

		List<ApprovalStatus> approvalStatuses = approvalStatusDao.findApprovalStatusesForUserWorkingForCustomer(retrievedUser,
				retrievedCustomer, new DateRange(new GregorianCalendar(2007, 10, 1).getTime(), new GregorianCalendar(2007, 10, 30)
						.getTime()));
		assertNotNull(approvalStatuses);
		assertEquals(0, approvalStatuses.size());
	}

	@Test
	public void shouldReturnEmptyListWhenDateRangeIsNotCorrect() {
		User retrievedUser = userDao.findById(2);
		Customer retrievedCustomer = customerDao.findById(2);

		List<ApprovalStatus> approvalStatuses = approvalStatusDao
				.findApprovalStatusesForUserWorkingForCustomer(retrievedUser, retrievedCustomer, new DateRange(new GregorianCalendar(2007,
						9, 1).getTime(), new GregorianCalendar(2007, 9, 31).getTime()));
		assertNotNull(approvalStatuses);
		assertEquals(0, approvalStatuses.size());
	}

	@Test
	public void shouldFindAllApprovalStatusesForDifferentCustomersWithInDateRangeCorrectly() {
		Customer customer1 = customerDao.findById(1);
		Customer customer2 = customerDao.findById(2);
		
		List<Customer> allCustomers = new ArrayList<Customer>();
		allCustomers.add(customer1);
		allCustomers.add(customer2);
		
		List<ApprovalStatus> approvalStatuses = approvalStatusDao.findApprovalStatusesForCustomers(allCustomers, new DateRange(
				new GregorianCalendar(2007, 9, 1).getTime(), new GregorianCalendar(2007, 9, 31).getTime()));
		assertNotNull(approvalStatuses);
		assertEquals(3, approvalStatuses.size());		
	}
}
