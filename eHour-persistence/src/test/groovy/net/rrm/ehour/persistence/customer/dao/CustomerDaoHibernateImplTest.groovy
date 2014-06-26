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

}
