package net.rrm.ehour.persistence.project.dao

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.*
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import net.rrm.ehour.util.EhourConstants
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.*

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 2:56:42 PM
 */
class ProjectAssignmentDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private ProjectAssignmentDao projectAssignmentDAO;

    ProjectAssignmentDaoHibernateImplTest() {
        super("dataset-projectassignment.xml");
    }

    @Test
    void shouldFindProjectAssignmentForProjectIdAndUserId() {
        def pas = projectAssignmentDAO.findProjectAssignmentForUser(1, 1);

        assertEquals(4, pas.size());
    }

    @Test
    void shouldFindProjectAssignmentsForUser() {
        def pas = projectAssignmentDAO.findProjectAssignmentsForUser(new User(1));

        assertEquals(7, pas.size());
    }

    @Test
    void shouldFindAll() {
        def pas = projectAssignmentDAO.findAll();

        assertEquals(12, pas.size());
    }

    @Test
    void shouldDelete() {
        projectAssignmentDAO.deleteOnId(2);

        def pa = projectAssignmentDAO.findById(new Integer(2));
        assertNull(pa);
    }

    @Test
    void shouldPersist() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.MONTH, 1);

        def assignment = ProjectAssignmentObjectMother.createProjectAssignment(new User(5), new Project(1))
        assignment.dateStart = new Date()
        assignment.dateEnd = cal.getTime()
        assignment.assignmentType = new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE)
        assignment.assignmentId = 25


        projectAssignmentDAO.persist(assignment)

        assertNotNull(assignment.assignmentId)
    }

    @Test
    void shouldFindById() {
        ProjectAssignment pa = projectAssignmentDAO.findById(1)

        assertEquals("eHour", pa.project.name)
    }

    @Test
    void shouldFindProjectAssignmentsForUserInRange() {
        DateRange range = new DateRange(new Date(2006 - 1900, 10 - 1, 24), new Date(2007 - 1900, 1 - 1, 10))

        def results = projectAssignmentDAO.findProjectAssignmentsForUser(1, range)

        assertEquals(5, results.size())
    }

    @Test
    void shouldFindProjectAssignmentsForCustomer() {
        DateRange range = new DateRange(new Date(2006 - 1900, 8 - 1, 24), new Date(2007 - 1900, 1 - 1, 10))

        def results = projectAssignmentDAO.findProjectAssignmentsForCustomer(new Customer(3), range)

        assertEquals(2, results.size())
    }

    @Test
    void shouldFindAllAssignmentsForProject() {
        def list = projectAssignmentDAO.findAllProjectAssignmentsForProject(new Project(1))

        assertEquals(4, list.size())
    }

    @Test
    public void shouldFindAllActiveAssignmentsForProject() {
        def list = projectAssignmentDAO.findAllActiveProjectAssignmentsForProject(new Project(1))

        assertEquals(3, list.size())
    }
}
