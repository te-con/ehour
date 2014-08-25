package net.rrm.ehour.activity.status;

import java.util.Date;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ActivityStatusServiceTest {
	
	private ActivityStatusService activityStatusService;

	@Before
	public void setUp() {
		activityStatusService = new ActivityStatusServiceImpl();
	}
	
	@Test
	public void shouldReturnActivityStatusCorrectly() {
		ActivityStatus activityStatus = activityStatusService.getActivityStatus(new Activity());
		Assert.assertNotNull(activityStatus);
		Assert.assertTrue(activityStatus.isValid());
	}
	
	@Test
	public void shouldReturnActivityStatusWithDateRangeCorrectly() {
		ActivityStatus activityStatus = activityStatusService.getActivityStatus(new Activity(), new DateRange(new Date(), new Date()));
		Assert.assertNotNull(activityStatus);
		Assert.assertTrue(activityStatus.isValid());		
	}
}
