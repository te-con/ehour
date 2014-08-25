package net.rrm.ehour.activity.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.activity.status.ActivityStatus;
import net.rrm.ehour.activity.status.ActivityStatusService;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.persistence.activity.dao.ActivityDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * Tests for {@link ActivityService}.
 */
public class ActivityServiceTest {

	private ActivityService activityService;

	private ActivityDao activityDao;

	private ActivityStatusService activityStatusService;
	
	@Before
	public void setUp() {
		activityService = new ActivityServiceImpl();
		
		activityDao = createMock(ActivityDao.class);
		ReflectionTestUtils.setField(activityService, "activityDao", activityDao);
		
		activityStatusService = createMock(ActivityStatusService.class);
		ReflectionTestUtils.setField(activityService, "activityStatusService", activityStatusService);
	}

	@Test
	public void shouldReturnAnAlreadyExistingActivity() throws ObjectNotFoundException {
		expect(activityDao.findById(1)).andReturn(new Activity());

		replay(activityDao);

		Activity activity = activityService.getActivity(1);

		verify(activityDao);

		Assert.assertNotNull(activity);
	}

	@Test(expected = ObjectNotFoundException.class)
	public void shouldThrowAnExceptionWhenInValidActivityIdIsPassed() throws ObjectNotFoundException {
		int invalidActivityId = 99999;

		expect(activityDao.findById(invalidActivityId)).andReturn(null);

		replay(activityDao);

		activityService.getActivity(invalidActivityId);

		verify(activityDao);
	}

	@Test
	public void shouldPersistActivity() {
		Activity activity = new Activity();

		expect(activityDao.persist(activity)).andReturn(activity);

		replay(activityDao);

		Activity persistedActivity = activityService.persistActivity(activity);

		Assert.assertNotNull(persistedActivity);

		verify(activityDao);
	}

	@Test
	public void shouldDeactivateActivityDuringDeletion() {
		Activity activity = new Activity();
		activity.setActive(Boolean.TRUE);

		expect(activityDao.findById(1)).andReturn(activity);

		expect(activityDao.merge(activity)).andReturn(activity);

		replay(activityDao);

		activityService.deleteActivity(1);

		verify(activityDao);

	}

	@Test
	public void shouldGetAllActivitiesForProject() {
		Project project = new Project();

		expect(activityDao.findAllActivitiesOfProject(project)).andReturn(new ArrayList<Activity>());

		replay(activityDao);

		List<Activity> allActivitiesForProject = activityService.getAllActivitiesForProject(project);

		Assert.assertNotNull(allActivitiesForProject);

		verify(activityDao);
	}

	@Test
	public void shouldGetAllActivitiesForUser() {
		User user = new User();

		expect(activityDao.findAllActivitiesOfUser(user)).andReturn(new ArrayList<Activity>());

		replay(activityDao);

		List<Activity> allActivitiesForUser = activityService.getAllActivitiesForUser(user);

		Assert.assertNotNull(allActivitiesForUser);

		verify(activityDao);
	}

	@Test
	public void shouldGetAllActivities() {
		expect(activityDao.findAll()).andReturn(new ArrayList<Activity>());

		replay(activityDao);

		List<Activity> allActivities = activityService.getActivities();

		Assert.assertNotNull(allActivities);

		verify(activityDao);
	}

	@Test
	public void shouldGetAllActivitiesForaUserWithinSpecifiedDateRange() {
		DateRange dateRange = new DateRange(new GregorianCalendar(2010, 1, 1).getTime(), new GregorianCalendar(2010, 11, 1).getTime());

		ArrayList<Activity> validActivities = new ArrayList<Activity>();
		validActivities.add(new Activity());
		validActivities.add(new Activity());
		
		expect(activityDao.findActivitiesForUser(1, dateRange)).andReturn(validActivities);
		
		expect(activityStatusService.getActivityStatus(new Activity(), dateRange)).andReturn(new ActivityStatus()).times(2);
		
		replay(activityDao, activityStatusService);
		
		List<Activity> validActivitiesForUser = activityService.getActivitiesForUser(1, dateRange);
		
		Assert.assertNotNull(validActivitiesForUser);
		Assert.assertEquals(2, validActivitiesForUser.size());
		verify(activityDao, activityStatusService);
	}
}
