package net.rrm.ehour.persistence.user.dao;

import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 15, 2010 - 11:51:19 PM
 */
public class UserRoleDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private UserRoleDao userRoleDAO;

    @Test
    public void shouldFindById() {
        UserRole role = userRoleDAO.findById("ROLE_ADMIN");
        assertEquals("Administrator", role.getRoleName());
    }

    @Test
    public void shouldFindUserRoles() {
        List<UserRole> list = userRoleDAO.findAll();
        assertEquals(4, list.size());
    }
}
