package net.rrm.ehour.persistence.userpref.dao;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserPreference;
import net.rrm.ehour.domain.UserPreferenceType;
import net.rrm.ehour.domain.UserPreferenceValueType;
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
	}
	
	@Test
	public void shouldPersistUserPreferenceThatWeekendsBeLocked() {
		User user = userDao.findById(1);
		Assert.assertNotNull(user);
		UserPreference userPreference = new UserPreference(user, UserPreferenceType.DISABLE_WEEKENDS);
		userPreferencesDao.persist(userPreference);
		Assert.assertNotNull(userPreference.getUserPreferenceKey());
		UserPreference retrievedUserPreference = userPreferencesDao.findById(userPreference.getUserPreferenceKey());
		Assert.assertEquals(UserPreferenceType.DISABLE_WEEKENDS.getValue(), retrievedUserPreference.getUserPreferenceKey());
		Assert.assertEquals(UserPreferenceValueType.ENABLE.name(), retrievedUserPreference.getUserPreferenceValue());
	}
	
	@Test
	public void shouldPersistUserPreferenceThatWeekendsBeNotLocked() {
		User user = userDao.findById(1);
		Assert.assertNotNull(user);
		UserPreference userPreference = new UserPreference(user, UserPreferenceType.ENABLE_WEEKENDS);
		userPreferencesDao.persist(userPreference);
		Assert.assertNotNull(userPreference.getUserPreferenceKey());
		UserPreference retrievedUserPreference = userPreferencesDao.findById(userPreference.getUserPreferenceKey());
		Assert.assertEquals(UserPreferenceType.ENABLE_WEEKENDS.getValue(), retrievedUserPreference.getUserPreferenceKey());
		Assert.assertEquals(UserPreferenceValueType.DISABLE.name(), retrievedUserPreference.getUserPreferenceValue());
	}
	
	@Test
	public void shouldModifyUserPreference() {
		User user = userDao.findById(1);
		Assert.assertNotNull(user);
		UserPreference userPreference = new UserPreference(user, UserPreferenceType.ENABLE_WEEKENDS);
		userPreferencesDao.persist(userPreference);
		Assert.assertNotNull(userPreference.getUserPreferenceKey());
		
		UserPreference retrievedUserPreference = userPreferencesDao.findById(userPreference.getUserPreferenceKey());
		
		Assert.assertEquals(UserPreferenceType.ENABLE_WEEKENDS.getValue(), retrievedUserPreference.getUserPreferenceKey());
		Assert.assertEquals(UserPreferenceValueType.DISABLE.name(), retrievedUserPreference.getUserPreferenceValue());
		
		retrievedUserPreference.setUserPrefence(UserPreferenceType.DISABLE_WEEKENDS);
		UserPreference retrievedUserPreferenceAfterMerge = userPreferencesDao.merge(retrievedUserPreference);
		
		Assert.assertEquals(UserPreferenceType.DISABLE_WEEKENDS.getValue(), retrievedUserPreferenceAfterMerge.getUserPreferenceKey());
		Assert.assertEquals(UserPreferenceValueType.ENABLE.name(), retrievedUserPreferenceAfterMerge.getUserPreferenceValue());
	}
}