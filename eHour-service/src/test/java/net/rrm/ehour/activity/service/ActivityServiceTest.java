package net.rrm.ehour.activity.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
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
	
	@Before
	public void setUp() {
		activityService = new ActivityServiceImpl();
		activityDao = createMock(ActivityDao.class);
		ReflectionTestUtils.setField(activityService, "activityDao", activityDao);
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
}
