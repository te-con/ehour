package net.rrm.ehour.persistence.customer.dao

import net.rrm.ehour.domain.CustomerMother
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import static junit.framework.Assert.*

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 2:08:30 PM
 */
class CustomerDaoHibernateImplTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private	CustomerDao	customerDao;

	public CustomerDaoHibernateImplTest()
	{
		super("dataset-customer.xml");
	}

	@Test
	void shouldDeleteCustomerOnId()
	{
		customerDao.delete 2

		def customer = customerDao.findById(2)
		assertNull(customer);
	}

	@Test
	void shouldFindAll()
	{
		def customers = customerDao.findAll()
		assertEquals(4, customers.size())
	}

	@Test
	void shouldFindAllActive()
	{
		def customers = customerDao.findAllActive();
		assertEquals(3, customers.size());
	}

	@Test
	void shouldFindOnId()
	{
		def customer = customerDao.findById(2);
		assertEquals("Tester", customer.name);
	}

	@Test
	void shouldPersist()
	{
		def customer = CustomerMother.createCustomer()
        customer.customerId = null

		customerDao.persist(customer)

		assertNotNull(customer.customerId)
	}

	@Test
	void shouldFindOnNameAndCode()
	{
		assertEquals("TEC", customerDao.findOnNameAndCode("te-con", "TEC").code)
	}

}
