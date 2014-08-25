package net.rrm.ehour.persistence.customer.dao;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.persistence.user.dao.UserDao;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TempCustomerDaoHibernateImplTest extends AbstractAnnotationDaoTest {
	// All these tests need to be ported to corresponding Groovy class.
	// Just a temporary work-around because of not been able to run Groovy test
	// cases under Eclipse

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private UserDao userDao;
	
	public TempCustomerDaoHibernateImplTest() {
		super("dataset-customer.xml");
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
	public void customerShouldBeAbleToExistWithoutReviewers() {
		Customer retrievedCustomer = customerDao.findById(3);
		Assert.assertNotNull(retrievedCustomer);
		List<User> reviewers = retrievedCustomer.getReviewers();
		Assert.assertNotNull(reviewers);
		Assert.assertEquals(0, reviewers.size());
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
	
}
