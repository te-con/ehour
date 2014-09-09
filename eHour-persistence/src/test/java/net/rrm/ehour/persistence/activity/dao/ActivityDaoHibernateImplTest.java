package net.rrm.ehour.persistence.activity.dao;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.customer.dao.CustomerDao;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Ignore
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
        super("dataset-customer.xml");
    }


    @Test
    public void shouldPersistActivity() {
        Activity activity = createActivity("testActivity", true, null, "ta");

        Activity retrievedActivity = activityDao.persist(activity);

        Assert.assertNotNull(retrievedActivity.getId());
        Assert.assertEquals("ta", retrievedActivity.getCode());
    }

    @Test
    public void shouldDeleteActivity() {
        Activity activity = createActivity("testActivity", true, null, "testActivity");

        activityDao.persist(activity);
        Assert.assertNotNull(activity.getId());
        Assert.assertEquals(1, activityDao.findAll().size());

        activityDao.delete(activity);
        Assert.assertEquals(0, activityDao.findAll().size());
    }

    @Test
    public void shouldretrieveActivityById() {
        Activity activity = createActivity("testActivity", true, null, "testActivity");

        activityDao.persist(activity);

        Assert.assertNotNull(activity.getId());

        Activity retrievedId = activityDao.findById(activity.getId());
        Assert.assertNotNull(retrievedId);
        Assert.assertEquals("testActivity", retrievedId.getName());
	}
	
	@Test
	public void shouldFindAllActivitiesForCustomersInDateRange() {
		Customer customer1 = new Customer("IBM", "International Business Machine", "Hardware and Software",true);
		customerDao.persist(customer1);
		Assert.assertNotNull(customer1.getCustomerId());

		Project project1 = createProject("Grid Computing", "GC", "contact", true, true, customer1);
		projectDao.persist(project1);
		Assert.assertNotNull(project1.getProjectId());
		
		User retrievedUser1 = userDao.findById(1);
		Assert.assertNotNull(retrievedUser1);
		Assert.assertEquals("thies", retrievedUser1.getUsername());

        Activity activity1 = createActivity("activity1", Boolean.TRUE, retrievedUser1, "activity1");
		activity1.setDateStart(new GregorianCalendar(2010, 5, 1).getTime());
		activity1.setDateEnd(new GregorianCalendar(2010, 7, 1).getTime());
		activity1.setProject(project1);
		
		activityDao.persist(activity1);

        Activity activity2 = createActivity("activity2", Boolean.TRUE, retrievedUser1, "activity2");
		activity2.setDateStart(new GregorianCalendar(2010, 5, 1).getTime());
		activity2.setDateEnd(new GregorianCalendar(2010, 7, 1).getTime());
		activity2.setProject(project1);
		
		activityDao.persist(activity2);

		Customer customer2 = new Customer("MS", "Microsoft", "Software", true);
		customerDao.persist(customer2);
		Assert.assertNotNull(customer2.getCustomerId());

		Project project2 = createProject("Windows 8", "Win8", "contact", true, true, customer2);
		projectDao.persist(project2);
		Assert.assertNotNull(project2.getProjectId());

		User retrievedUser2 = userDao.findById(2);
		Assert.assertNotNull(retrievedUser2);
		Assert.assertEquals("admin", retrievedUser2.getUsername());

        Activity activity3 = createActivity("activity3", Boolean.TRUE, retrievedUser2, "activity3");
		activity3.setDateStart(new GregorianCalendar(2010, 5, 1).getTime());
		activity3.setDateEnd(new GregorianCalendar(2010, 7, 1).getTime());
		activity3.setProject(project2);
		activityDao.persist(activity3);
		
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(customer1);

		DateRange dateRange = new DateRange();
		dateRange.setDateStart(new GregorianCalendar(2010, 5, 1).getTime());
		dateRange.setDateEnd(new GregorianCalendar(2010, 7, 1).getTime());

        List<Activity> activitiesForCustomer1 = activityDao.findActivitiesForCustomers(customers, dateRange);
		Assert.assertEquals(2, activitiesForCustomer1.size());
		Assert.assertTrue(activitiesForCustomer1.contains(activity1));
		Assert.assertTrue(activitiesForCustomer1.contains(activity2));

		customers.clear();
		customers.add(customer2);
		
		List<Activity> activitiesForCustomer2 = activityDao.findActivitiesForCustomers(customers, dateRange);
		Assert.assertEquals(1, activitiesForCustomer2.size());
		Assert.assertTrue(activitiesForCustomer2.contains(activity3));
		
		dateRange.setDateEnd(new GregorianCalendar(2011, 5, 1).getTime());
		dateRange.setDateStart(new GregorianCalendar(2011, 7, 1).getTime());
		
		activitiesForCustomer2 = activityDao.findActivitiesForCustomers(customers, dateRange);
		Assert.assertEquals(0, activitiesForCustomer2.size());
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
	

    @Test
    public void shouldPersistActivitiesWithAssignedUser() {
        User retrievedUser = userDao.findById(1);
        Assert.assertNotNull(retrievedUser);
        Assert.assertEquals("thies", retrievedUser.getUsername());

        Activity activity1 = createActivity("activity1", Boolean.TRUE, retrievedUser, "activity1");
        activityDao.persist(activity1);

        Activity activity2 = createActivity("activity2", Boolean.TRUE, retrievedUser, "activity2");
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

        Activity activity1 = createActivity("activity1", Boolean.TRUE, retrievedUser, "activity1");
        activity1.setProject(project);
        activityDao.persist(activity1);

        Activity activity2 = createActivity("activity2", Boolean.TRUE, retrievedUser, "activity2");
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

        Activity activity1 = createActivity("activity1", Boolean.TRUE, null, "activity1");
        activityDao.persist(activity1);
        Activity retrievedActivity = activityDao.findById(activity1.getId());
        Assert.assertNotNull(retrievedActivity);
        Assert.assertEquals("activity1", retrievedActivity.getName());

        Activity activity2 = createActivity("activity2", Boolean.TRUE, null, "activity2");
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

        Activity activity1 = createActivity("activity1", Boolean.TRUE, retrievedUser, "activity1");
        activity1.setProject(project);
        activityDao.persist(activity1);

        Activity activity2 = createActivity("activity2", Boolean.TRUE, retrievedUser, "activity2");
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
        customer.setCustomerId(100);
        customerDao.persist(customer);
        Assert.assertNotNull(customer.getCustomerId());

        Project project = createProject("Grid Computing", "GC", "contact", true, true, customer);
        project.setProjectId(100);
        projectDao.persist(project);
        Assert.assertNotNull(project.getProjectId());

        User retrievedUser = userDao.findById(1);
        Assert.assertNotNull(retrievedUser);
        Assert.assertEquals("thies", retrievedUser.getUsername());


        Activity activity1 = createActivity("activity1", Boolean.TRUE, retrievedUser, "activity1");
        activity1.setDateStart(new GregorianCalendar(2010, 5, 1).getTime());
        activity1.setDateEnd(new GregorianCalendar(2010, 7, 1).getTime());
        activity1.setProject(project);
        activityDao.persist(activity1);


        GregorianCalendar startDateCalendar = new GregorianCalendar(2010, 1, 1);
        GregorianCalendar endDateCalendar = new GregorianCalendar(2011, 11, 1);
        List<Activity> allActivitiesForUser = activityDao.findActivitiesForUser(1, new DateRange(startDateCalendar.getTime(), endDateCalendar.getTime()));
        System.out.println(allActivitiesForUser.get(0).getProject().getActivities().getClass());
        Assert.assertNotNull(allActivitiesForUser);
        Assert.assertEquals(1, allActivitiesForUser.size());
    }

    @Test
    public void shouldFindAllActivitiesForCustomers() {
        Customer customer1 = new Customer("IBM", "International Business Machine", "Hardware and Software",true);
        customerDao.persist(customer1);
        Assert.assertNotNull(customer1.getCustomerId());

        Project project1 = createProject("Grid Computing", "GC", "contact", true, true, customer1);
        projectDao.persist(project1);
        Assert.assertNotNull(project1.getProjectId());

        User retrievedUser1 = userDao.findById(1);
        Assert.assertNotNull(retrievedUser1);
        Assert.assertEquals("thies", retrievedUser1.getUsername());


        Activity activity1 = createActivity("activity1", Boolean.TRUE, retrievedUser1, "activity1");
        activity1.setDateStart(new GregorianCalendar(2010, 5, 1).getTime());
        activity1.setDateEnd(new GregorianCalendar(2010, 7, 1).getTime());
        activity1.setProject(project1);

        activityDao.persist(activity1);

        Activity activity2 = createActivity("activity2", Boolean.TRUE, retrievedUser1, "activity2");
        activity2.setDateStart(new GregorianCalendar(2010, 5, 1).getTime());
        activity2.setDateEnd(new GregorianCalendar(2010, 7, 1).getTime());
        activity2.setProject(project1);

        activityDao.persist(activity2);

        Customer customer2 = new Customer("MS", "Microsoft", "Software", true);
        customerDao.persist(customer2);
        Assert.assertNotNull(customer2.getCustomerId());

        Project project2 = createProject("Windows 8", "Win8", "contact", true, true, customer2);
        projectDao.persist(project2);
        Assert.assertNotNull(project2.getProjectId());

        User retrievedUser2 = userDao.findById(2);
        Assert.assertNotNull(retrievedUser2);
        Assert.assertEquals("admin", retrievedUser2.getUsername());

        Activity activity3 = createActivity("activity3", Boolean.TRUE, retrievedUser2, "activity3");
        activity3.setDateStart(new GregorianCalendar(2010, 5, 1).getTime());
        activity3.setDateEnd(new GregorianCalendar(2010, 7, 1).getTime());
        activity3.setProject(project2);
        activityDao.persist(activity3);

        List<Customer> customers = new ArrayList<Customer>();
        customers.add(customer1);

        List<Activity> activitiesForCustomer1 = activityDao.findActivitiesForCustomers(customers);
        Assert.assertEquals(2, activitiesForCustomer1.size());
        Assert.assertTrue(activitiesForCustomer1.contains(activity1));
        Assert.assertTrue(activitiesForCustomer1.contains(activity2));

        customers.clear();
        customers.add(customer2);
        List<Activity> activitiesForCustomer2 = activityDao.findActivitiesForCustomers(customers);
        Assert.assertEquals(1, activitiesForCustomer2.size());
        Assert.assertTrue(activitiesForCustomer2.contains(activity3));
    }

 /*   @Test
    public void shouldFindAllActivitiesForCustomersInDateRange() {
        Customer customer1 = new Customer("IBM", "International Business Machine", "Hardware and Software",true);
        customerDao.persist(customer1);
        Assert.assertNotNull(customer1.getCustomerId());

        Project project1 = createProject("Grid Computing", "GC", "contact", true, true, customer1);
        projectDao.persist(project1);
        Assert.assertNotNull(project1.getProjectId());

        User retrievedUser1 = userDao.findById(1);
        Assert.assertNotNull(retrievedUser1);
        Assert.assertEquals("thies", retrievedUser1.getUsername());


        Activity activity1 = createActivity("activity1", Boolean.TRUE, retrievedUser1, "activity1");
        activity1.setDateStart(new GregorianCalendar(2010, 5, 1).getTime());
        activity1.setDateEnd(new GregorianCalendar(2010, 7, 1).getTime());
        activity1.setProject(project1);

        activityDao.persist(activity1);

        Activity activity2 = createActivity("activity2", Boolean.TRUE, retrievedUser1, "activity2");
        activity2.setDateStart(new GregorianCalendar(2010, 5, 1).getTime());
        activity2.setDateEnd(new GregorianCalendar(2010, 7, 1).getTime());
        activity2.setProject(project1);

        activityDao.persist(activity2);

        Customer customer2 = new Customer("MS", "Microsoft", "Software", true);
        customerDao.persist(customer2);
        Assert.assertNotNull(customer2.getCustomerId());

        Project project2 = createProject("Windows 8", "Win8", "contact", true, true, customer2);
        projectDao.persist(project2);
        Assert.assertNotNull(project2.getProjectId());

        User retrievedUser2 = userDao.findById(2);
        Assert.assertNotNull(retrievedUser2);
        Assert.assertEquals("admin", retrievedUser2.getUsername());

        Activity activity3 = createActivity("activity3", Boolean.TRUE, retrievedUser2, "activity3");
        activity3.setDateStart(new GregorianCalendar(2010, 5, 1).getTime());
        activity3.setDateEnd(new GregorianCalendar(2010, 7, 1).getTime());
        activity3.setProject(project2);
        activityDao.persist(activity3);

        List<Customer> customers = new ArrayList<Customer>();
        customers.add(customer1);

        DateRange dateRange = new DateRange();
        dateRange.setDateStart(new GregorianCalendar(2010, 5, 1).getTime());
        dateRange.setDateEnd(new GregorianCalendar(2010, 7, 1).getTime());

        List<Activity> activitiesForCustomer1 = activityDao.findActivitiesForCustomers(customers, dateRange);
        Assert.assertEquals(2, activitiesForCustomer1.size());
        Assert.assertTrue(activitiesForCustomer1.contains(activity1));
        Assert.assertTrue(activitiesForCustomer1.contains(activity2));

        customers.clear();
        customers.add(customer2);

        List<Activity> activitiesForCustomer2 = activityDao.findActivitiesForCustomers(customers, dateRange);
        Assert.assertEquals(1, activitiesForCustomer2.size());
        Assert.assertTrue(activitiesForCustomer2.contains(activity3));

        dateRange.setDateEnd(new GregorianCalendar(2011, 5, 1).getTime());
        dateRange.setDateStart(new GregorianCalendar(2011, 7, 1).getTime());

        activitiesForCustomer2 = activityDao.findActivitiesForCustomers(customers, dateRange);
        Assert.assertEquals(0, activitiesForCustomer2.size());
    }*/
/*

    @Test
    public void shouldFindActivityWithAnExistingCode() {
        Activity activity = createActivity("testActivity", true, null, "ta");

        Activity retrievedActivity = activityDao.persist(activity);
        Assert.assertNotNull(retrievedActivity);

        Activity activityWithExistingCode = activityDao.findByCode("ta");
        Assert.assertNotNull(activityWithExistingCode);
    }
*/

//    @Test
//    public void shouldReturnNullWithANonExistingCode() {
//        Activity activityWithNonExistingCode = activityDao.findByCode("NON-EXISTING-CODE");
//        Assert.assertNull(activityWithNonExistingCode);
//    }

    private Activity createActivity(String activityName, Boolean isActive, User assignedUser, String activityCode) {
        Activity activity = new Activity();
        activity.setName(activityName);
        activity.setActive(isActive);
        activity.setAssignedUser(assignedUser);
        activity.setCode(activityCode);
        return activity;
    }
}