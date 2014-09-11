package net.rrm.ehour.approvalstatus.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import net.rrm.ehour.AbstractServiceTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.persistence.approvalstatus.dao.ApprovalStatusDao;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

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
	public void shouldReturnApprovalStatusesForAnActivityAndDateRangeProperly() {
		DateRange dateRange = new DateRange();
		Activity activity = new Activity();
		
		ArrayList<ApprovalStatus> allApprovalStatuses = new ArrayList<ApprovalStatus>();
		
		expect(approvalStatusDao.findApprovalStatusesForActivity(activity, dateRange)).andReturn(allApprovalStatuses);
		
		replay(approvalStatusDao);
		
		List<ApprovalStatus> result = approvalStatusService.getApprovalStatusForActivity(activity, dateRange);
		
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
