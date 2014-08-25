package net.rrm.ehour.persistence.activity.dao;

import java.util.GregorianCalendar;
import java.util.List;

<<<<<<< HEAD
=======
import net.rrm.ehour.data.DateRange;
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.customer.dao.CustomerDao;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.user.dao.UserDao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class ActivityDaoHibernateImplTest extends AbstractAnnotationDaoTest {

	@Autowired
	private ActivityDao activityDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private ProjectDao projectDao;

	public ActivityDaoHibernateImplTest() {
		super("dataset-users-customers.xml");
	}
	
	
	@Test
	public void shouldPersistActivity() {
		Activity activity = createActivity("testActivity", true, null);
		
		activityDao.persist(activity);
		
		Assert.assertNotNull(activity.getId());
	}
	
	@Test
	public void shouldDeleteActivity() {
		Activity activity = createActivity("testActivity", true, null);
		
		activityDao.persist(activity);
		assertNotNull(activity.getId());
		Assert.assertEquals(1, activityDao.findAll().size());
		
		activityDao.delete(activity);
		Assert.assertEquals(0, activityDao.findAll().size());
	}
	
	@Test
	public void shouldretrieveActivityById() {
		Activity activity = createActivity("testActivity", true, null);
		
		activityDao.persist(activity);
		
		Assert.assertNotNull(activity.getId());
		
		Activity retrievedId = activityDao.findById(activity.getId());
		Assert.assertNotNull(retrievedId);
		Assert.assertEquals("testActivity", retrievedId.getName());
	}
	
	@Test
	public void shouldPersistActivitiesWithAssignedUser() {
		User retrievedUser = userDao.findById(1);
		Assert.assertNotNull(retrievedUser);
		Assert.assertEquals("thies", retrievedUser.getUsername());
		
		Activity activity1 = createActivity("activity1", Boolean.TRUE, retrievedUser);
		activityDao.persist(activity1);
		
		Activity activity2 = createActivity("activity2", Boolean.TRUE, retrievedUser);
		activityDao.persist(activity2);

		Activity retrievedActivity1 = activityDao.findById(activity1.getId());
		Assert.assertNotNull(retrievedActivity1);
		Assert.assertEquals("activity1", retrievedActivity1.getName());
		Assert.assertEquals("1", retrievedActivity1.getAssignedUser().getUserId().toString());
		
		Activity retrievedActivity2 = activityDao.findById(activity2.getId());
		Assert.assertNotNull(retrievedActivity2);
		Assert.assertEquals("activity2", retrievedActivity2.getName());
		Assert.assertEquals("1", retrievedActivity2.getAssignedUser().getUserId().toString());
	}


	
	@Test
	public void shouldPersistActivitiesWithAssignedUserAndProject() {
		Customer customer = new Customer("IBM", "International Business Machine", "Hardware and Software",true);
		customerDao.persist(customer);
		Assert.assertNotNull(customer.getCustomerId());
		
		Project project = createProject("Grid Computing", "GC", "contact", true, true, customer);
		projectDao.persist(project);
		Assert.assertNotNull(project.getProjectId());
		
		User retrievedUser = userDao.findById(1);
		Assert.assertNotNull(retrievedUser);
		Assert.assertEquals("thies", retrievedUser.getUsername());
		
		Activity activity1 = createActivity("activity1", Boolean.TRUE, retrievedUser);
		activity1.setProject(project);
		activityDao.persist(activity1);
		
		Activity activity2 = createActivity("activity2", Boolean.TRUE, retrievedUser);
		activity2.setProject(project);
		activityDao.persist(activity2);

		Activity retrievedActivity1 = activityDao.findById(activity1.getId());
		Assert.assertNotNull(retrievedActivity1);
		Assert.assertEquals("activity1", retrievedActivity1.getName());
		Assert.assertEquals("1", retrievedActivity1.getAssignedUser().getUserId().toString());
		Assert.assertEquals("GC", retrievedActivity1.getProject().getProjectCode());
		Assert.assertEquals("IBM", retrievedActivity1.getProject().getCustomer().getCode());
		
		Activity retrievedActivity2 = activityDao.findById(activity2.getId());
		Assert.assertNotNull(retrievedActivity2);
		Assert.assertEquals("activity2", retrievedActivity2.getName());
		Assert.assertEquals("1", retrievedActivity2.getAssignedUser().getUserId().toString());
		Assert.assertEquals("GC", retrievedActivity2.getProject().getProjectCode());
		Assert.assertEquals("IBM", retrievedActivity2.getProject().getCustomer().getCode());
	}
	
	@Test
	public void shouldFindAllActivitiesOfUser() {
		User retrievedUser = userDao.findById(1);
		Assert.assertNotNull(retrievedUser);
		Assert.assertEquals("thies", retrievedUser.getUsername());
		
		Activity activity1 = createActivity("activity1", Boolean.TRUE, null);
		activityDao.persist(activity1);
		Activity retrievedActivity = activityDao.findById(activity1.getId());
		Assert.assertNotNull(retrievedActivity);
		Assert.assertEquals("activity1", retrievedActivity.getName());
		
		Activity activity2 = createActivity("activity2", Boolean.TRUE, null);
		activityDao.persist(activity2);
		retrievedActivity = activityDao.findById(activity2.getId());
		Assert.assertNotNull(retrievedActivity);
		Assert.assertEquals("activity2", retrievedActivity.getName());
		
		activity1.setAssignedUser(retrievedUser);
		activity2.setAssignedUser(retrievedUser);
		
		activityDao.merge(activity1);
		activityDao.merge(activity2);
		
		List<Activity> allActivitiesOfUser = activityDao.findAllActivitiesOfUser(retrievedUser);
		Assert.assertEquals(2, allActivitiesOfUser.size());
	}
	
	@Test
	public void shouldFindAllActivitiesOfProject() {
		Customer customer = new Customer("IBM", "International Business Machine", "Hardware and Software",true);
		customerDao.persist(customer);
		Assert.assertNotNull(customer.getCustomerId());
		
		Project project = createProject("Grid Computing", "GC", "contact", true, true, customer);
		projectDao.persist(project);
		Assert.assertNotNull(project.getProjectId());
		
		User retrievedUser = userDao.findById(1);
		Assert.assertNotNull(retrievedUser);
		Assert.assertEquals("thies", retrievedUser.getUsername());
		
		Activity activity1 = createActivity("activity1", Boolean.TRUE, retrievedUser);
		activity1.setProject(project);
		activityDao.persist(activity1);
		
		Activity activity2 = createActivity("activity2", Boolean.TRUE, retrievedUser);
		activity2.setProject(project);
		activityDao.persist(activity2);

		Activity retrievedActivity1 = activityDao.findById(activity1.getId());
		Assert.assertNotNull(retrievedActivity1);
		Assert.assertEquals("activity1", retrievedActivity1.getName());
		Assert.assertEquals("1", retrievedActivity1.getAssignedUser().getUserId().toString());
		Assert.assertEquals("GC", retrievedActivity1.getProject().getProjectCode());
		Assert.assertEquals("IBM", retrievedActivity1.getProject().getCustomer().getCode());
		
		Activity retrievedActivity2 = activityDao.findById(activity2.getId());
		Assert.assertNotNull(retrievedActivity2);
		Assert.assertEquals("activity2", retrievedActivity2.getName());
		Assert.assertEquals("1", retrievedActivity2.getAssignedUser().getUserId().toString());
		Assert.assertEquals("GC", retrievedActivity2.getProject().getProjectCode());
		Assert.assertEquals("IBM", retrievedActivity2.getProject().getCustomer().getCode());
		
		List<Activity> allActivitiesOfProject = activityDao.findAllActivitiesOfProject(project);
		Assert.assertEquals(2, allActivitiesOfProject.size());
	}

	@Test
	public void shouldFindAllActivitiesForUserInDateRange() {
		Customer customer = new Customer("IBM", "International Business Machine", "Hardware and Software",true);
		customerDao.persist(customer);
		Assert.assertNotNull(customer.getCustomerId());
		
		Project project = createProject("Grid Computing", "GC", "contact", true, true, customer);
		projectDao.persist(project);
		Assert.assertNotNull(project.getProjectId());
		
		User retrievedUser = userDao.findById(1);
		Assert.assertNotNull(retrievedUser);
		Assert.assertEquals("thies", retrievedUser.getUsername());
		
		
		Activity activity1 = createActivity("activity1", Boolean.TRUE, retrievedUser);
		activity1.setDateStart(new GregorianCalendar(2010, 5, 1).getTime());
		activity1.setDateEnd(new GregorianCalendar(2010, 7, 1).getTime());
		activity1.setProject(project);
		activityDao.persist(activity1);
		
		
		GregorianCalendar startDateCalendar = new GregorianCalendar(2010, 1, 1);
		GregorianCalendar endDateCalendar = new GregorianCalendar(2011, 11, 1);
		List<Activity> allActivitiesForUser = activityDao.findActivitiesForUser(1, new DateRange(startDateCalendar.getTime(), endDateCalendar.getTime()));
		Assert.assertNotNull(allActivitiesForUser);
		Assert.assertEquals(1, allActivitiesForUser.size());
	}

	private Project createProject(String projectName, String projectCode, String contractName, boolean isActive, boolean isBillable, Customer customer) {
		Project project = new Project();
		project.setActive(isActive);
		project.setBillable(isBillable);
		project.setContact(contractName);
		project.setName(projectName);
		project.setProjectCode(projectCode);
		project.setCustomer(customer);
		return project;
	}
	

	private Activity createActivity(String activityName, Boolean isActive, User assignedUser) {
		Activity activity = new Activity();
		activity.setName(activityName);
		activity.setActive(isActive);
		activity.setAssignedUser(assignedUser);
		return activity;
	}
}