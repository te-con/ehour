package net.rrm.ehour.persistence.project.dao;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.util.EhourConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 2:56:42 PM
 */
@SuppressWarnings("deprecation")
public class ProjectAssignmentDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    public ProjectAssignmentDaoHibernateImplTest() {
        super("dataset-projectassignment.xml");
    }

    @Test
    public void shouldFindProjectAssignmentForProjectIdAndUserId() {
        List<ProjectAssignment> pas = projectAssignmentDAO.findProjectAssignmentForUser(1, 1);

        assertEquals(4, pas.size());
    }

    @Test
    public void shouldFindProjectAssignmentsForUser() {
        List<ProjectAssignment> pas = projectAssignmentDAO.findAllProjectAssignmentsForUser(new User(1));

        assertEquals(9, pas.size());
    }

    @Test
    public void shouldFindAll() {
        List<ProjectAssignment> pas = projectAssignmentDAO.findAll();

        assertEquals(12, pas.size());
    }

    @Test
    public void shouldDelete() {
        projectAssignmentDAO.deleteOnId(2);

        ProjectAssignment pa = projectAssignmentDAO.findById(2);
        assertNull(pa);
    }

    @Test
    public void shouldPersist() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.MONTH, 1);

        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(new User(5), new Project(1));
        assignment.setDateStart(new Date());
        assignment.setDateEnd(cal.getTime());
        assignment.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));
        assignment.setAssignmentId(25);

        projectAssignmentDAO.persist(assignment);

        assertNotNull(assignment.getAssignmentId());
    }

    @Test
    public void shouldFindById() {
        ProjectAssignment pa = projectAssignmentDAO.findById(1);

        assertEquals("eHour", pa.getProject().getName());
    }

    @Test
    public void shouldFindActiveProjectAssignmentsForUserInRange() {
        DateRange range = new DateRange(new Date(2006 - 1900, Calendar.OCTOBER, 24), new Date(2007 - 1900, Calendar.JANUARY, 10));

        List<ProjectAssignment> results = projectAssignmentDAO.findActiveProjectAssignmentsForUser(1, range);

        assertEquals(5, results.size());
    }

    @Test
    public void shouldFindAllProjectAssignmentsForUserInRange() {
        DateRange range = new DateRange(new Date(2006 - 1900, Calendar.OCTOBER, 24), new Date(2007 - 1900, Calendar.JANUARY, 10));

        List<ProjectAssignment> results = projectAssignmentDAO.findAllProjectAssignmentsForUser(1, range);

        assertEquals(8, results.size());
    }

    @Test
    public void shouldFindProjectAssignmentsForCustomer() {
        DateRange range = new DateRange(new Date(2006 - 1900, Calendar.AUGUST, 24), new Date(2007 - 1900, Calendar.JANUARY, 10));

        List<ProjectAssignment> results = projectAssignmentDAO.findProjectAssignmentsForCustomer(new Customer(3), range);

        assertEquals(2, results.size());
    }

    @Test
    public void shouldFindAllAssignmentsForProject() {
        List<ProjectAssignment> list = projectAssignmentDAO.findAllProjectAssignmentsForProject(new Project(1));
        assertEquals(3, list.size());
    }


    @Autowired
    private ProjectAssignmentDao projectAssignmentDAO;
}
