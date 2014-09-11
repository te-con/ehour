package net.rrm.ehour.approvalstatus.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.AbstractServiceTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.approvalstatus.dao.ApprovalStatusDao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class ApprovalStatusServiceTest extends AbstractServiceTest {

	private ApprovalStatusService approvalStatusService;
	
	private ApprovalStatusDao approvalStatusDao;
	
	@Before
	public void setUp() {
		approvalStatusService = new ApprovalStatusServiceImpl();
		approvalStatusDao = createMock(ApprovalStatusDao.class);

		ReflectionTestUtils.setField(approvalStatusService, "approvalStatusDao", approvalStatusDao);
	}

	@Test
	public void shouldReturnApprovalStatusesForUserAndCustomerAndDateRangeProperly() {
		DateRange dateRange = new DateRange();
		
		User user = new User();
		
		Customer customer = new Customer();
		
		ArrayList<ApprovalStatus> allApprovalStatuses = new ArrayList<ApprovalStatus>();
		
		expect(approvalStatusDao.findApprovalStatusesForUserWorkingForCustomer(user, customer, dateRange)).andReturn(allApprovalStatuses);
		
		replay(approvalStatusDao);
		
		List<ApprovalStatus> result = approvalStatusService.getApprovalStatusForUserWorkingForCustomer(user, customer, dateRange);
		
		verify(approvalStatusDao);
		
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	public void shouldReturnApprovalStatusesForCustomerAndDateRangeProperly() {
		DateRange dateRange = new DateRange();

		List<Customer> allCustomers = new ArrayList<Customer>();
		
		ArrayList<ApprovalStatus> allApprovalStatuses = new ArrayList<ApprovalStatus>();
		
		expect(approvalStatusDao.findApprovalStatusesForCustomers(allCustomers, dateRange)).andReturn(allApprovalStatuses);
		
		replay(approvalStatusDao);
		
		List<ApprovalStatus> result = approvalStatusService.getApprovalStatusesForCustomers(allCustomers, dateRange);
		
		verify(approvalStatusDao);
		
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	
	@Test
	public void shouldPersistApprovalStatus() {
		ApprovalStatus approvalStatus = new ApprovalStatus();

		expect(approvalStatusDao.persist(approvalStatus)).andReturn(approvalStatus);
		
		replay(approvalStatusDao);

		approvalStatusService.persist(approvalStatus);
		
		verify(approvalStatusDao);
	}
}
