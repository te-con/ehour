package net.rrm.ehour.persistence.customer.dao;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.CustomerObjectMother;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 2:08:30 PM
 */
public class CustomerDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private CustomerDao customerDao;

    public CustomerDaoHibernateImplTest() {
        super("dataset-customer.xml");
    }

    @Test
    public void shouldDeleteCustomerOnId() {
        customerDao.deleteOnId(2);

        assertNull(customerDao.findById(2));
    }

    @Test
    public void shouldFindAllCustomers() {
        List<Customer> customers = customerDao.findAll();
        assertEquals(4, customers.size());
    }

    @Test
    public void shouldFindAllActiveCustomers() {
        List<Customer> customers = customerDao.findAllActive();
        assertEquals(3, customers.size());
    }

    @Test
    public void shouldFindOnId() {
        Customer customer = customerDao.findById(2);
        assertEquals("Tester", customer.getName());
    }

    @Test
    public void shouldPersist() {
        Customer customer = CustomerObjectMother.createCustomer();
        customer.setName("aa");
        customer.setCode("bb");
        customer.setCustomerId(null);

        customerDao.persist(customer);

        assertNotNull(customer.getCustomerId());
    }

    @Test
    public void shouldFindOnNameAndCode() {
        assertEquals("TEC", customerDao.findOnNameAndCode("te-con", "TEC").getCode());
    }
}
