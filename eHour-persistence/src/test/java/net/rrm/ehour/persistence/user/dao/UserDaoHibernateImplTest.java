package net.rrm.ehour.persistence.user.dao;

import com.google.common.collect.Sets;
import net.rrm.ehour.data.LegacyUserDepartment;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.util.EhourConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 14, 2010 - 10:41:38 PM
 */
public class UserDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void shouldFindUsers() {
        List<User> results = userDao.findActiveUsers();
        assertEquals(4, results.size());
    }

    @Test
    public void shouldFindById() {
        User user = userDao.findById(1);

        assertEquals("thies", user.getUsername());
    }

    @Test
    public void shouldFindByUsername() {
        User user = userDao.findByUsername("thies");

        assertEquals("thies", user.getUsername());
    }

    @Test
    public void shouldPersist() {
        UserDepartment org = UserDepartmentObjectMother.createUserDepartment();

        User user = UserObjectMother.createUser();
        user.setUserId(5);

        ProjectAssignment assignment = new ProjectAssignment();

        assignment.setUser(user);
        assignment.setAssignmentId(1);
        assignment.setProject(new Project(1));
        assignment.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));

        Set<ProjectAssignment> assignments = Sets.newHashSet(assignment);

        user.setUserDepartment(org);
        user.setProjectAssignments(assignments);
        userDao.persist(user);

        assertNotNull(user.getUserId());
    }

    @Test
    public void shouldFindAllActiveUsers() {
        List<User> results = userDao.findActiveUsers();

        assertEquals(4, results.size());
    }

    @Test
    public void shouldFindAllActiveUsersWithEmailSet() {
        List<User> results = userDao.findAllActiveUsersWithEmailSet();

        assertEquals(2, results.size());
    }

    @Test
    public void shouldDeletePmWithoutProject() {
        userDao.deletePmWithoutProject();

        User user = userDao.findById(2);

        assertThat(user.getUserRoles(), not(hasItem(UserRole.PROJECTMANAGER)));
    }

    @Test
    public void shouldFindWithLegacyUserDepartment() {
        List<LegacyUserDepartment> legacyUserDepartments = userDao.findLegacyUserDepartments();
        assertThat(legacyUserDepartments.size(), greaterThan(0));
    }
}