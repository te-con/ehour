package net.rrm.ehour.persistence.project.dao;

import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 2:29:43 PM
 */
public class ProjectDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    public ProjectDaoHibernateImplTest() {
        super("dataset-project.xml");
    }

    @Test
    public void shouldDelete() {
        projectDAO.deleteOnId(20);

        Project project = projectDAO.findById(20);
        assertNull(project);
    }

    @Test
    public void shouldFindAll() {
        List<Project> projects = projectDAO.findAll();

        assertEquals(5, projects.size());
    }

    @Test
    public void shouldFindById() {
        Project prj = projectDAO.findById(10);
        assertEquals("eHour", prj.getName());
    }

    @Test
    public void shouldPersist() {
        Customer customer = CustomerObjectMother.createCustomer();
        customer.setCustomerId(10);
        customer.getProjects().clear();

        Project project = ProjectObjectMother.createProject(null, customer);

        Project prj = projectDAO.persist(project);

        Assert.assertNotNull(prj.getProjectId());
    }

    @Test
    public void shouldFindAllActive() {
        List<Project> r = projectDAO.findAllActive();

        assertEquals(3, r.size());
    }

    @Test
    public void shouldFindDefaultProjects() {
        assertEquals(2, projectDAO.findDefaultProjects().size());
    }

    @Test
    public void shouldFindProjectForCustomersAll() {
        ArrayList<Customer> ids = new ArrayList<>(Arrays.asList(new Customer(30), new Customer(10)));
        List<Project> r = projectDAO.findProjectForCustomers(ids, false);

        assertEquals(3, r.size());
    }

    @Test
    public void shouldFindProjectForCustomersOnlyActive() {
        ArrayList<Customer> ids = new ArrayList<>(Arrays.asList(new Customer(30), new Customer(10)));
        List<Project> r = projectDAO.findProjectForCustomers(ids, true);

        assertEquals(2, r.size());
    }

    @Test
    public void shouldFindActiveProjectsWhereUserIsPM() {
        List<Project> res = projectDAO.findActiveProjectsWhereUserIsPM(new User(1));
        assertEquals(10, res.iterator().next().getPK().intValue());
    }

    @Autowired
    private ProjectDao projectDAO;
}
