package net.rrm.ehour.persistence.user.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link UserDaoHibernateImpl}.
 * 
 */
public class TempUserDaoHibernateImplTest extends AbstractAnnotationDaoTest {

	// All these tests need to be ported to corresponding Groovy class.
	// Just a temporary work-around because of not been able to run Groovy test
	// cases under Eclipse

	@Autowired
	private UserDao userDao;

	public TempUserDaoHibernateImplTest() {
		super("dataset-users-customers.xml");
	}

	@Test
	public void shouldGetAssociatedCustomersCorrectly() {
		User retrievedUser = userDao.findById(1);
		Assert.assertNotNull(retrievedUser);
		Assert.assertEquals("thies", retrievedUser.getUsername());
		Set<Customer> userCustomers = retrievedUser.getCustomers();
		Assert.assertNotNull(userCustomers);
		Assert.assertEquals(2, userCustomers.size());

		List<String> allCustomerNames = new ArrayList<String>();
		for (Customer customer : userCustomers) {
			allCustomerNames.add(customer.getName());
		}

		Assert.assertTrue(allCustomerNames.contains("Tester"));
		Assert.assertTrue(allCustomerNames.contains("BA"));
	}

	@Test
	public void userCanExistWithoutAnyAssociatedCustomer() {
		User retrievedUser = userDao.findById(2);
		Assert.assertNotNull(retrievedUser);

		Assert.assertEquals("admin", retrievedUser.getUsername());
		Set<Customer> userCustomers = retrievedUser.getCustomers();
		Assert.assertNotNull(userCustomers);
		Assert.assertEquals(0, userCustomers.size());
	}

}
