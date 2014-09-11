package net.rrm.ehour.persistence.approvalstatus.dao;

import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ActivityMother;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.ApprovalStatusType;
import net.rrm.ehour.persistence.activity.dao.ActivityDao;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
/**
 * 
 * Tests for {@link ApprovalStatusDaoHibernateImplTest}
 */
public class ApprovalStatusDaoHibernateImplTest extends AbstractAnnotationDaoTest {

	@Autowired
	private ApprovalStatusDao approvalStatusDao;

	@Autowired
	private ActivityDao activityDao;
	
	public ApprovalStatusDaoHibernateImplTest() {
		super("dataset-approval-status.xml");
	}

	@Test
	public void shouldPersistApprovalStatusWithActivityProperly() {
		Activity activity = ActivityMother.createActivity(6, 6, 6, 6);
		
		ApprovalStatus approvalStatus = new ApprovalStatus();
		approvalStatus.setId(6);
		approvalStatus.setActivity(activity);
		approvalStatus.setStatus(ApprovalStatusType.IN_PROGRESS);
		
		approvalStatusDao.persist(approvalStatus);
		
		ApprovalStatus retrievedApprovalStatus = approvalStatusDao.findById(6);
		assertNotNull(retrievedApprovalStatus);
		assertEquals(ApprovalStatusType.IN_PROGRESS, retrievedApprovalStatus.getStatus());
	}

	@Test
	public void shouldFindAllApprovalStatusesWithInSpecifiedDateRange() {
		Activity retrivedActivity = activityDao.findById(1);
		List<ApprovalStatus> approvalStatuses = approvalStatusDao.findApprovalStatusesForActivity(retrivedActivity, new DateRange(new GregorianCalendar(2007, 9, 1).getTime(), new GregorianCalendar(2007, 9, 31).getTime()));
		assertNotNull(approvalStatuses);
		assertEquals(1, approvalStatuses.size());
		ApprovalStatus associatedApprovalStatus = approvalStatuses.get(0);
		assertNotNull(associatedApprovalStatus);
		assertEquals(ApprovalStatusType.IN_PROGRESS, associatedApprovalStatus.getStatus());
	}
	
}
