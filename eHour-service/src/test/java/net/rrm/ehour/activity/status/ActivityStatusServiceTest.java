package net.rrm.ehour.activity.status;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.activity.status.ActivityStatus.Status;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class ActivityStatusServiceTest {
	
	private ActivityStatusService activityStatusService;

	private ReportAggregatedDao reportAggregatedDAO;
	
	@Before
	public void setUp() {
		activityStatusService = new ActivityStatusServiceImpl();

		reportAggregatedDAO = createMock(ReportAggregatedDao.class);
		
		ReflectionTestUtils.setField(activityStatusService, "reportAggregatedDAO", reportAggregatedDAO);
	}

	@Test
	public void shouldReturnInAllotedAndValidStatusWhenBookedHoursAreLessThanAllottedHours() {
		Activity activity = new Activity();
		activity.setAllottedHours(new Float(20));
		
		ActivityAggregateReportElement aggregateReport = new ActivityAggregateReportElement();
		aggregateReport.setHours(10);
		
		expect(reportAggregatedDAO.getCumulatedHoursForActivity(activity)).andReturn(aggregateReport);
		
		replay(reportAggregatedDAO);
		ActivityStatus activityStatus = activityStatusService.getActivityStatus(activity);

		verify(reportAggregatedDAO);
		
		Assert.assertNotNull(activityStatus);
		Assert.assertTrue(activityStatus.isValid());

		List<Status> statusses = activityStatus.getStatusses();
		Assert.assertEquals(1, statusses.size());
		Status overAllotedStatus = statusses.get(0);
		
		Assert.assertEquals(Status.IN_ALLOTTED, overAllotedStatus);
	}

	@Test
	public void shouldReturnInAllotedAndValidStatusWhenBookedHoursAreEqualToAllottedHours() {
		Activity activity = new Activity();
		activity.setAllottedHours(new Float(20));
		
		ActivityAggregateReportElement aggregateReport = new ActivityAggregateReportElement();
		aggregateReport.setHours(20);
		
		expect(reportAggregatedDAO.getCumulatedHoursForActivity(activity)).andReturn(aggregateReport);
		
		replay(reportAggregatedDAO);
		ActivityStatus activityStatus = activityStatusService.getActivityStatus(activity);

		verify(reportAggregatedDAO);
		
		Assert.assertNotNull(activityStatus);
		Assert.assertTrue(activityStatus.isValid());

		List<Status> statusses = activityStatus.getStatusses();
		Assert.assertEquals(1, statusses.size());
		Status overAllotedStatus = statusses.get(0);
		
		Assert.assertEquals(Status.IN_ALLOTTED, overAllotedStatus);
	}

	
	@Test
	public void shouldReturnOverAllotedStatusWhenBookedExtraHours() {
		Activity activity = new Activity();
		activity.setAllottedHours(new Float(10));

		ActivityAggregateReportElement aggregateReport = new ActivityAggregateReportElement();
		aggregateReport.setHours(20);
		
		expect(reportAggregatedDAO.getCumulatedHoursForActivity(activity)).andReturn(aggregateReport);
		
		replay(reportAggregatedDAO);
		ActivityStatus activityStatus = activityStatusService.getActivityStatus(activity);

		verify(reportAggregatedDAO);


		Assert.assertNotNull(activityStatus);
		Assert.assertFalse(activityStatus.isValid());
		
		List<Status> statusses = activityStatus.getStatusses();
		Assert.assertEquals(1, statusses.size());
		Status overAllotedStatus = statusses.get(0);
		
		Assert.assertEquals(Status.OVER_ALLOTTED, overAllotedStatus);
	}

	@Test
	public void shouldReturnOverAllotedStatusWhenNullAllottedHours() {
		Activity activity = new Activity();

		ActivityAggregateReportElement aggregateReport = new ActivityAggregateReportElement();
		aggregateReport.setHours(20);
		
		expect(reportAggregatedDAO.getCumulatedHoursForActivity(activity)).andReturn(aggregateReport);
		
		replay(reportAggregatedDAO);
		ActivityStatus activityStatus = activityStatusService.getActivityStatus(activity);

		verify(reportAggregatedDAO);


		Assert.assertNotNull(activityStatus);
		Assert.assertFalse(activityStatus.isValid());
		
		List<Status> statusses = activityStatus.getStatusses();
		Assert.assertEquals(1, statusses.size());
		Status overAllotedStatus = statusses.get(0);
		
		Assert.assertEquals(Status.OVER_ALLOTTED, overAllotedStatus);
	}
	

	@Test
	public void shouldReturnInRunningStatusWhenPassedDateRangeFallsBetweenStartAndEndDateOfActivity() {
		Activity activity = new Activity();
		activity.setAllottedHours(new Float(30));
		activity.setDateStart(new GregorianCalendar(2011, 1, 1).getTime());
		activity.setDateEnd(new GregorianCalendar(2011, 11, 1).getTime());
		
		ActivityAggregateReportElement aggregateReport = new ActivityAggregateReportElement();
		aggregateReport.setHours(20);
		
		expect(reportAggregatedDAO.getCumulatedHoursForActivity(activity)).andReturn(aggregateReport);
		
		replay(reportAggregatedDAO);
		DateRange queryDateRange = new DateRange(new GregorianCalendar(2011, 2, 1).getTime(), new GregorianCalendar(2011, 4, 1).getTime());
		ActivityStatus activityStatus = activityStatusService.getActivityStatus(activity, queryDateRange);

		verify(reportAggregatedDAO);


		Assert.assertNotNull(activityStatus);
		Assert.assertTrue(activityStatus.isValid());
		
		List<Status> statusses = activityStatus.getStatusses();
		Assert.assertEquals(2, statusses.size());
		statusses.contains(Status.IN_ALLOTTED);
		statusses.contains(Status.RUNNING);
	}

	@Test
	public void shouldReturnBeforeStartStatusWhenPassedDateRangeFallsBeforeStartDateOfActivity() {
		Activity activity = new Activity();
		activity.setAllottedHours(new Float(30));
		activity.setDateStart(new GregorianCalendar(2011, 1, 1).getTime());
		activity.setDateEnd(new GregorianCalendar(2011, 11, 1).getTime());
		
		ActivityAggregateReportElement aggregateReport = new ActivityAggregateReportElement();
		aggregateReport.setHours(0);
		
		expect(reportAggregatedDAO.getCumulatedHoursForActivity(activity)).andReturn(aggregateReport);
		
		replay(reportAggregatedDAO);
		DateRange queryDateRange = new DateRange(new GregorianCalendar(2010, 2, 1).getTime(), new GregorianCalendar(2010, 4, 1).getTime());
		ActivityStatus activityStatus = activityStatusService.getActivityStatus(activity, queryDateRange);

		verify(reportAggregatedDAO);


		Assert.assertNotNull(activityStatus);
		Assert.assertTrue(activityStatus.isValid());
		
		List<Status> statusses = activityStatus.getStatusses();
		Assert.assertEquals(2, statusses.size());
		statusses.contains(Status.IN_ALLOTTED);
		statusses.contains(Status.BEFORE_START);
	}


	@Test
	public void shouldReturnAfterDeadLineStatusWhenPassedDateRangeFallsAfterEndDateOfActivity() {
		Activity activity = new Activity();
		activity.setAllottedHours(new Float(30));
		activity.setDateStart(new GregorianCalendar(2010, 1, 1).getTime());
		activity.setDateEnd(new GregorianCalendar(2010, 11, 1).getTime());
		
		ActivityAggregateReportElement aggregateReport = new ActivityAggregateReportElement();
		aggregateReport.setHours(30);
		
		expect(reportAggregatedDAO.getCumulatedHoursForActivity(activity)).andReturn(aggregateReport);
		
		replay(reportAggregatedDAO);
		DateRange queryDateRange = new DateRange(new GregorianCalendar(2011, 2, 1).getTime(), new GregorianCalendar(2011, 4, 1).getTime());
		ActivityStatus activityStatus = activityStatusService.getActivityStatus(activity, queryDateRange);

		verify(reportAggregatedDAO);


		Assert.assertNotNull(activityStatus);
		Assert.assertTrue(activityStatus.isValid());
		
		List<Status> statusses = activityStatus.getStatusses();
		Assert.assertEquals(2, statusses.size());
		statusses.contains(Status.IN_ALLOTTED);
		statusses.contains(Status.AFTER_DEADLINE);
	}
	
	@Test
	public void shouldReturnInAllotedStatusWhenNoInformationAboutActivityFromDataBase() {
		Activity activity = new Activity();
		activity.setAllottedHours(new Float(20));
		
		expect(reportAggregatedDAO.getCumulatedHoursForActivity(activity)).andReturn(null);
		
		replay(reportAggregatedDAO);
		
		ActivityStatus activityStatus = activityStatusService.getActivityStatus(activity);

		verify(reportAggregatedDAO);
		
		Assert.assertNotNull(activityStatus);
		Assert.assertTrue(activityStatus.isValid());

		List<Status> statusses = activityStatus.getStatusses();
		Assert.assertEquals(1, statusses.size());
		Status overAllotedStatus = statusses.get(0);
		
		Assert.assertEquals(Status.IN_ALLOTTED, overAllotedStatus);
		
	}
}
