package net.rrm.ehour.persistence.user.dao;

import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserDepartmentObjectMother;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class UserDepartmentDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private UserDepartmentDao userDepartmentDao;

    @Test
    public void shouldDelete() {
        userDepartmentDao.deleteOnId(20);

        UserDepartment dept = userDepartmentDao.findById(20);
        assertNull(dept);
    }

    @Test
    public void shouldFindById() {
        UserDepartment dept = userDepartmentDao.findById(20);
        assertEquals("EHOUR", dept.getName());
    }

    @Test
    public void shouldGetAllDepartments() {
        List<UserDepartment> depts = userDepartmentDao.findAll();
        assertEquals(2, depts.size());
    }

    @Test
    public void shouldPersist() {
        UserDepartment department = UserDepartmentObjectMother.createUserDepartment();

        userDepartmentDao.persist(department);

        UserDepartment dept2 = userDepartmentDao.findById(department.getDepartmentId());

        assertEquals(department.getName(), dept2.getName());
    }

    @Test
    public void shouldFindByNameAndCode() {
        assertNotNull(userDepartmentDao.findOnNameAndCode("TE-CON", "TEC"));
    }

    @Test
    public void shouldFindAllWithoutParent() {
        UserDepartment child = userDepartmentDao.findById(10);
        UserDepartment parent = userDepartmentDao.findById(20);
        child.setParentUserDepartment(parent);

        userDepartmentDao.persist(parent);
        userDepartmentDao.persist(child);
        userDepartmentDao.flush();

        assertEquals(1, userDepartmentDao.findAllWithoutParent().size());
    }
}
