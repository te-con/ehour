package net.rrm.ehour.activity.status;

import net.rrm.ehour.activity.status.ActivityStatus.Status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * Tests for {@link ActivityStatus}.
 */
public class ActivityStatusTest {

	private ActivityStatus activityStatus;

	@Before
	public void setUp() {
		activityStatus = new ActivityStatus();
	}

	@Test
	public void shouldReturnTrueWhenInAllotedStatus() {
		activityStatus.addStatus(Status.IN_ALLOTTED);

		Assert.assertTrue(activityStatus.isActivityBookable());
	}

	@Test
	public void testShouldReturnFalseWhenOverAllotedStatus() {
		activityStatus.addStatus(Status.OVER_ALLOTTED);
		
		Assert.assertFalse(activityStatus.isActivityBookable());
	}

	@Test
	public void testShouldReturnFalseWhenAfterDeadLineStatus() {
		activityStatus.addStatus(Status.AFTER_DEADLINE);
		activityStatus.addStatus(Status.IN_ALLOTTED);
		
		Assert.assertFalse(activityStatus.isActivityBookable());
	}

	@Test
	public void testShouldReturnFalseWhenBeforeStartStatus() {
		activityStatus.addStatus(Status.BEFORE_START);
		activityStatus.addStatus(Status.IN_ALLOTTED);
		
		Assert.assertFalse(activityStatus.isActivityBookable());
	}
	
	@Test
	public void testShouldReturnTrueWithMultipleStatusesButOverAllottedStatusIsNotPresent() {
		activityStatus.addStatus(Status.IN_ALLOTTED);
		activityStatus.addStatus(Status.RUNNING);

		Assert.assertTrue(activityStatus.isActivityBookable());
	}
	
	@Test
	public void testShouldReturnTrueWithNoStatusesArePresent() {
		Assert.assertTrue(activityStatus.isActivityBookable());
	}
}
