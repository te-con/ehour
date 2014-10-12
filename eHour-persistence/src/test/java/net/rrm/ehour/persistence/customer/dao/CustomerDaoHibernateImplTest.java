package net.rrm.ehour.persistence.customer.dao;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserObjectMother;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.persistence.user.dao.UserDao;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 2:08:30 PM
 */
public class CustomerDaoHibernateImplTest extends AbstractAnnotationDaoTest {
	public CustomerDaoHibernateImplTest() {
		super("dataset-users-customers.xml");
	}

	@Test
	public void shouldDeleteCustomerOnId() {
		customerDao.deleteOnId(2);

		assertNull(customerDao.findById(2));
	}

	@Test
	public void shouldFindAllCustomers() {
		List<Customer> customers = customerDao.findAll();
		assertEquals(3, customers.size());
	}

	@Test
	public void shouldFindAllActiveCustomers() {
		List<Customer> customers = customerDao.findAllActive();
		assertEquals(2, customers.size());
	}

	@Test
	public void shouldFindOnId() {
		Customer customer = customerDao.findById(2);
		assertEquals("BA", customer.getName());
	}

	@Test
	public void shouldFindOnNameAndCode() {
		assertEquals("TE-CON", customerDao.findOnNameAndCode("te-con", "TE-CON").getCode());
	}

	@Test
	public void shouldGetAssociatedReviewersCorrectly() {
		Customer retrievedCustomer = customerDao.findById(1);
		Assert.assertNotNull(retrievedCustomer);
		List<User> reviewers = retrievedCustomer.getReviewers();
		Assert.assertNotNull(reviewers);
		Assert.assertEquals(1, reviewers.size());
	}

	@Test
	public void customerShouldBeAbleToExistWithoutReviewersOrReporters() {
		Customer retrievedCustomer = customerDao.findById(3);
		Assert.assertNotNull(retrievedCustomer);
		List<User> reviewers = retrievedCustomer.getReviewers();
		Assert.assertNotNull(reviewers);
		Assert.assertEquals(0, reviewers.size());

		List<User> reporters = retrievedCustomer.getReporters();
		Assert.assertNotNull(reporters);
		Assert.assertEquals(0, reporters.size());
	}

	@Test
	public void shouldPersistCustomerWithReviewerCorrectly() {
		Customer retrievedCustomer = customerDao.findById(3);
		Assert.assertNotNull(retrievedCustomer);
		List<User> reviewers = retrievedCustomer.getReviewers();
		Assert.assertNotNull(reviewers);
		Assert.assertEquals(0, reviewers.size());

		User retrievedUser = userDao.findById(1);
		retrievedCustomer.addReviewer(retrievedUser);
		customerDao.merge(retrievedCustomer);

		Customer updatedCustomer = customerDao.findById(3);
		Assert.assertEquals(1, updatedCustomer.getReviewers().size());
	}

	@Test
	public void shouldPersistCustomerWithReporterCorrectly() {
		Customer retrievedCustomer = customerDao.findById(3);
		Assert.assertNotNull(retrievedCustomer);
		List<User> reporters = retrievedCustomer.getReporters();
		Assert.assertNotNull(reporters);
		Assert.assertEquals(0, reporters.size());

		User retrievedUser = userDao.findById(2);
		retrievedCustomer.addReporter(retrievedUser);
		customerDao.merge(retrievedCustomer);

		Customer updatedCustomer = customerDao.findById(3);
		List<User> customerReporters = updatedCustomer.getReporters();
		Assert.assertEquals(1, customerReporters.size());

		Assert.assertEquals("admin", customerReporters.get(0).getUsername());
	}

	@Test
	public void shouldGetAssociatedReportersCorrectly() {
		Customer retrievedCustomer = customerDao.findById(2);
		Assert.assertNotNull(retrievedCustomer);
		List<User> reporters = retrievedCustomer.getReporters();
		Assert.assertNotNull(reporters);
		Assert.assertEquals(1, reporters.size());
		User reporterUser = reporters.get(0);
		Assert.assertNotNull(reporterUser);
		Assert.assertEquals("thies", reporterUser.getUsername());
	}

	@Test
	public void shouldPersistCustomerWithBothReporterAndReviewerCorrectly() {
		Customer retrievedCustomer = customerDao.findById(3);
		Assert.assertNotNull(retrievedCustomer);

		List<User> reporters = retrievedCustomer.getReporters();
		Assert.assertNotNull(reporters);
		Assert.assertEquals(0, reporters.size());

		List<User> reviewers = retrievedCustomer.getReviewers();
		Assert.assertNotNull(reviewers);
		Assert.assertEquals(0, reviewers.size());

		User userAsReporter = userDao.findById(2);
		retrievedCustomer.addReporter(userAsReporter);

		User userAsReviewer = userDao.findById(3);
		retrievedCustomer.addReviewer(userAsReviewer);

		customerDao.merge(retrievedCustomer);

		Customer updatedCustomer = customerDao.findById(3);

		List<User> customerReporters = updatedCustomer.getReporters();
		Assert.assertEquals(1, customerReporters.size());
		Assert.assertEquals("admin", customerReporters.get(0).getUsername());

		List<User> customerReviewers = updatedCustomer.getReviewers();
		Assert.assertEquals(1, customerReviewers.size());
		Assert.assertEquals("testacc", customerReviewers.get(0).getUsername());
	}

	@Test
	public void shouldReturnCorrectlyWhenCustomersAreHavingPassedUserAsReporterAndAskedForInActiveCustomersAlso() {
		User user = UserObjectMother.createUser();

		List<Customer> customers = customerDao.findAllCustomersHavingReporter(user);

		Assert.assertNotNull(customers);
		Assert.assertEquals(2, customers.size());
	}

	@Test
	public void shouldReturnCorrectlyWhenCustomersAreHavingPassedUserAsReporter() {
        User user = UserObjectMother.createUser();

        List<Customer> customers = customerDao.findAllCustomersHavingReporter(user);

        Assert.assertNotNull(customers);
        Assert.assertEquals(2, customers.size());
    }

	@Test
	public void shouldReturnCorrectlyWhenCustomersAreHavingPassedUserAsReviewer() {
		User user = UserObjectMother.createUser();

		List<Customer> customers = customerDao.findAllCustomersForWhichUserIsaReviewer(user);

		Assert.assertNotNull(customers);
		Assert.assertEquals(2, customers.size());
	}

	@Test
	public void shouldReturnEmptyListWhenCustomersAreHavingPassedUserAsReviewer() {
		User user = new User(2, "thoos");

		List<Customer> customers = customerDao.findAllCustomersForWhichUserIsaReviewer(user);

		Assert.assertNotNull(customers);
		Assert.assertEquals(0, customers.size());
	}

	@Test
	public void shouldFindCustomerWithExistingCode() {
		Customer customer = customerDao.findByCustomerCode("TE-CON");

		Assert.assertNotNull(customer);
		Assert.assertEquals("TE-CON", customer.getName());
	}

	@Test
	public void shouldReturnNullWhenFindingCustomerWithNonExistentCode() {
		Customer customer = customerDao.findByCustomerCode("NON-EXISTING-CODE");

		Assert.assertNull(customer);
	}

	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private UserDao userDao;
}
