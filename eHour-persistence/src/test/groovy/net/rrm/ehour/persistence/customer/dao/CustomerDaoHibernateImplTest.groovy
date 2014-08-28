package net.rrm.ehour.persistence.customer.dao

import net.rrm.ehour.domain.CustomerObjectMother
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 2:08:30 PM
 */
class CustomerDaoHibernateImplTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private	CustomerDao	customerDao

	CustomerDaoHibernateImplTest()
	{
		super("dataset-customer.xml")
	}

	@Test
	void "should delete customer on id"()
	{
		customerDao.deleteOnId 2

		customerDao.findById(2) == null

	}

	@Test
	void "should find all customers"()
	{
		def customers = customerDao.findAll()
		assert 4 == customers.size()
	}

	@Test
	void "should find all active customers"()
	{
		def customers = customerDao.findAllActive()
		assert 3 == customers.size()
	}

	@Test
	void "should find on id"()
	{
		def customer = customerDao.findById(2)
		assert "Tester" == customer.name
	}

	@Test
	void "should persist customer"()
	{
		def customer = CustomerObjectMother.createCustomer()
        customer.name = "aa"
        customer.code = "bb"
        customer.customerId = null

        customerDao.persist(customer)

        assert customer.customerId != null
	}

	@Test
	void "should find on name and code"()
	{
		assert "TEC" == customerDao.findOnNameAndCode("te-con", "TEC").code
	}

	@Test
	public void shouldGetAssociatedReviewersCorrectly() {
		def retrievedCustomer = customerDao.findById(1);
		assertNotNull(retrievedCustomer);
		def reviewers = retrievedCustomer.getReviewers();
		assertNotNull(reviewers);
		assertEquals(1, reviewers.size());
	}

	@Test
	public void customerShouldBeAbleToExistWithoutReviewersOrReporters() {
		def retrievedCustomer = customerDao.findById(3);
		assertNotNull(retrievedCustomer);
		def reviewers = retrievedCustomer.getReviewers();
		assertNotNull(reviewers);
		assertEquals(0, reviewers.size());

		def reporters = retrievedCustomer.getReporters();
		assertNotNull(reporters);
		assertEquals(0, reporters.size());
	}

	@Test
	public void shouldPersistCustomerWithReviewerCorrectly() {
		def retrievedCustomer = customerDao.findById(3);
		assertNotNull(retrievedCustomer);
		def reviewers = retrievedCustomer.getReviewers();
		assertNotNull(reviewers);
		assertEquals(0, reviewers.size());
		
		def retrievedUser = userDao.findById(1);
		retrievedCustomer.addReviewer(retrievedUser);
		customerDao.merge(retrievedCustomer);
		
		def updatedCustomer = customerDao.findById(3);
		assertEquals(1, updatedCustomer.getReviewers().size());
	}

	@Test
	public void shouldPersistCustomerWithReporterCorrectly() {
		def retrievedCustomer = customerDao.findById(3);
		assertNotNull(retrievedCustomer);
		def reporters = retrievedCustomer.getReporters();
		assertNotNull(reporters);
		assertEquals(0, reporters.size());
		
		def retrievedUser = userDao.findById(2);
		retrievedCustomer.addReporter(retrievedUser);
		customerDao.merge(retrievedCustomer);
		
		def updatedCustomer = customerDao.findById(3);
		def customerReporters = updatedCustomer.getReporters();
		assertEquals(1, customerReporters.size());
		
		assertEquals("admin", customerReporters.get(0).getUsername());
	}
	
	@Test
	public void shouldGetAssociatedReportersCorrectly() {
		def retrievedCustomer = customerDao.findById(2);
		assertNotNull(retrievedCustomer);
		def reporters = retrievedCustomer.getReporters();
		assertNotNull(reporters);
		assertEquals(1, reporters.size());
		def reporterUser = reporters.get(0);
		assertNotNull(reporterUser);
		assertEquals("thies", reporterUser.getUsername());
	}

	@Test
	public void shouldPersistCustomerWithBothReporterAndReviewerCorrectly() {
		def retrievedCustomer = customerDao.findById(3);
		assertNotNull(retrievedCustomer);

		def reporters = retrievedCustomer.getReporters();
		assertNotNull(reporters);
		assertEquals(0, reporters.size());

		def reviewers = retrievedCustomer.getReviewers();
		assertNotNull(reviewers);
		assertEquals(0, reviewers.size());
		
		def userAsReporter = userDao.findById(2);
		retrievedCustomer.addReporter(userAsReporter);
	
		def userAsReviewer = userDao.findById(3);
		retrievedCustomer.addReviewer(userAsReviewer);
		
		customerDao.merge(retrievedCustomer);
		
		def updatedCustomer = customerDao.findById(3);
		
		def customerReporters = updatedCustomer.getReporters();
		assertEquals(1, customerReporters.size());
		assertEquals("admin", customerReporters.get(0).getUsername());
		
		def customerReviewers = updatedCustomer.getReviewers();
		assertEquals(1, customerReviewers.size());
		assertEquals("testacc", customerReviewers.get(0).getUsername());
	}
}
