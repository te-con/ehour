package net.rrm.ehour.persistence.user.dao

import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertEquals

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 15, 2010 - 11:51:19 PM
 */
class UserRoleDaoHibernateImplTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private	UserRoleDao	userRoleDAO;

	@Test
	void shouldFindById()
	{
		def role = userRoleDAO.findById("ROLE_ADMIN")
		assertEquals "Administrator", role.roleName
	}

	@Test
	void shouldFindUserRoles()
	{
		def list = userRoleDAO.findAll()
		assertEquals(4, list.size())
	}

}
