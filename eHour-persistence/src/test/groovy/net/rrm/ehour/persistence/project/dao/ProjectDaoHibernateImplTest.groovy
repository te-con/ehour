package net.rrm.ehour.persistence.project.dao

import net.rrm.ehour.domain.*
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.*

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 2:29:43 PM
 */
class ProjectDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private ProjectDao projectDAO;

    ProjectDaoHibernateImplTest() {
        super("dataset-project.xml");
    }


    @Test
    void shouldDelete() {
        projectDAO.deleteOnId 20

        def project = projectDAO.findById(20);
        assertNull(project);
    }

    @Test
    void shouldFindAll() {
        def projects = projectDAO.findAll();

        assertEquals(5, projects.size());
    }

    @Test
    void shouldFindById() {
        def prj = projectDAO.findById(10)
        assertEquals("eHour", prj.name)
    }

    @Test
    void shouldPersist() {
        def customer = CustomerObjectMother.createCustomer()
        customer.customerId = 10
        customer.projects.clear()

        def project = ProjectObjectMother.createProject(null, customer)

        def prj = projectDAO.persist(project)

        assertNotNull(prj.projectId)
    }

    @Test
    void shouldFindAllActive() {
        def r = projectDAO.findAllActive()

        assertEquals(3, r.size())
    }

    @Test
    void shouldFindDefaultProjects() {
        assertEquals(2, projectDAO.findDefaultProjects().size())
    }

    @Test
    void shouldFindProjectForCustomersAll() {
        def ids = [new Customer(30), new Customer(10)]
        List<Project> r = projectDAO.findProjectForCustomers(ids, false)

        assertEquals(3, r.size())
    }

    @Test
    public void shouldFindProjectForCustomersOnlyActive() {
        def ids = [new Customer(30), new Customer(10)]
        List<Project> r = projectDAO.findProjectForCustomers(ids, true)

        assertEquals(2, r.size())
    }

    @Test
    public void shouldFindActiveProjectsWhereUserIsPM() {
        def res = projectDAO.findActiveProjectsWhereUserIsPM(new User(1))
        assertEquals(10, res.iterator().next().getPK())
    }
}
