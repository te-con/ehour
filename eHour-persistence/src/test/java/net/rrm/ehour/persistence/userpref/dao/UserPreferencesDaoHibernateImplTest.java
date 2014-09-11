package net.rrm.ehour.persistence.userpref.dao;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserPreference;
import net.rrm.ehour.domain.UserPreferenceType;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.persistence.user.dao.UserDao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link UserPreferencesDaoHibernateImpl}.
 * 
 */
public class UserPreferencesDaoHibernateImplTest extends AbstractAnnotationDaoTest {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserPreferencesDao userPreferencesDao;

	public UserPreferencesDaoHibernateImplTest() {
		super("dataset-users.xml");
	}

	@Test
	public void shouldPersistUserPreferences() {
		User user = userDao.findById(1);
		Assert.assertNotNull(user);
		UserPreference userPreference = new UserPreference(user, UserPreferenceType.DISABLE_WEEKENDS);
		userPreferencesDao.persist(userPreference);
		Assert.assertNotNull(userPreference.getUserPreferenceId());
		UserPreference retrievedUserPreferences = userPreferencesDao.findById(userPreference.getPK());
		Assert.assertEquals(UserPreferenceType.DISABLE_WEEKENDS, retrievedUserPreferences.getUserPreferenceType());
	}

}