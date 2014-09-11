package net.rrm.ehour.persistence.userpref.dao;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserPreference;
import net.rrm.ehour.domain.UserPreferenceId;
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
		
		UserPreferenceId userPreferenceId = new UserPreferenceId(UserPreferenceType.DISABLE_WEEKENDS.getValue(), user);
		UserPreference userPreference = new UserPreference(userPreferenceId, UserPreferenceType.DISABLE_WEEKENDS);
		
		userPreferencesDao.persist(userPreference);
	}
	
	@Test
	public void shouldPersistUserPreferenceThatWeekendsBeLocked() {
		User user = userDao.findById(1);
		Assert.assertNotNull(user);
		
		UserPreferenceId userPreferenceId = new UserPreferenceId(UserPreferenceType.DISABLE_WEEKENDS.getValue(), user);
		UserPreference userPreference = new UserPreference(userPreferenceId, UserPreferenceType.DISABLE_WEEKENDS);
		
		userPreferencesDao.persist(userPreference);
		
		Assert.assertNotNull(userPreference.getUserPreferenceId());
		
		UserPreference retrievedUserPreference = userPreferencesDao.findById(userPreference.getUserPreferenceId());
		Assert.assertEquals(UserPreferenceType.DISABLE_WEEKENDS.getValue(), retrievedUserPreference.getUserPreferenceId().getUserPreferenceKey());
		Assert.assertEquals(UserPreferenceValueType.ENABLE.name(), retrievedUserPreference.getUserPreferenceValue());
	}
	
	@Test
	public void shouldPersistUserPreferenceThatWeekendsBeNotLocked() {
		User user = userDao.findById(1);
		Assert.assertNotNull(user);
		
		UserPreferenceId userPreferenceId = new UserPreferenceId(UserPreferenceType.ENABLE_WEEKENDS.getValue(), user);
		UserPreference userPreference = new UserPreference(userPreferenceId, UserPreferenceType.ENABLE_WEEKENDS);
		
		userPreferencesDao.persist(userPreference);
		
		Assert.assertNotNull(userPreference.getUserPreferenceId());
		
		UserPreference retrievedUserPreference = userPreferencesDao.findById(userPreference.getUserPreferenceId());
		Assert.assertEquals(UserPreferenceType.ENABLE_WEEKENDS.getValue(), retrievedUserPreference.getUserPreferenceId().getUserPreferenceKey());
		Assert.assertEquals(UserPreferenceValueType.DISABLE.name(), retrievedUserPreference.getUserPreferenceValue());
	}
	
	@Test
	public void shouldModifyUserPreference() {
		User user = userDao.findById(1);
		Assert.assertNotNull(user);
		
		UserPreferenceId userPreferenceId = new UserPreferenceId(UserPreferenceType.ENABLE_WEEKENDS.getValue(), user);
		UserPreference userPreference = new UserPreference(userPreferenceId, UserPreferenceType.ENABLE_WEEKENDS);
		
		userPreferencesDao.persist(userPreference);
		
		Assert.assertNotNull(userPreference.getUserPreferenceId());
		
		UserPreference retrievedUserPreference = userPreferencesDao.findById(userPreference.getUserPreferenceId());
		
		Assert.assertEquals(UserPreferenceType.ENABLE_WEEKENDS.getValue(), retrievedUserPreference.getUserPreferenceId().getUserPreferenceKey());
		Assert.assertEquals(UserPreferenceValueType.DISABLE.name(), retrievedUserPreference.getUserPreferenceValue());
		
		retrievedUserPreference.setUserPrefence(UserPreferenceType.DISABLE_WEEKENDS);
		UserPreference retrievedUserPreferenceAfterMerge = userPreferencesDao.merge(retrievedUserPreference);
		
		Assert.assertEquals(UserPreferenceType.DISABLE_WEEKENDS.getValue(), retrievedUserPreferenceAfterMerge.getUserPreferenceId().getUserPreferenceKey());
		Assert.assertEquals(UserPreferenceValueType.ENABLE.name(), retrievedUserPreferenceAfterMerge.getUserPreferenceValue());
	}
	
	@Test
	public void shouldPersistTwoUserPreferenceForTwoUsers() {
		User user1 = userDao.findById(1);
		Assert.assertNotNull(user1);
		UserPreferenceId userPreferenceId1 = new UserPreferenceId(UserPreferenceType.ENABLE_WEEKENDS.getValue(), user1);
		UserPreference userPreference1 = new UserPreference(userPreferenceId1, UserPreferenceType.ENABLE_WEEKENDS);
		userPreferencesDao.persist(userPreference1);
		Assert.assertNotNull(userPreference1.getUserPreferenceId());
		
		Assert.assertEquals(1, userPreferencesDao.findAll().size());
		
		User user2 = userDao.findById(2);
		Assert.assertNotNull(user2);
		UserPreferenceId userPreferenceId2 = new UserPreferenceId(UserPreferenceType.ENABLE_WEEKENDS.getValue(), user2);
		UserPreference userPreference2 = new UserPreference(userPreferenceId2, UserPreferenceType.ENABLE_WEEKENDS);
		userPreferencesDao.persist(userPreference2);
		
		Assert.assertNotNull(userPreference2.getUserPreferenceId());
		
		Assert.assertEquals(2, userPreferencesDao.findAll().size());
	}
	
	@Test
	public void testGetUserPreferenceForUserForType() {
		User user1 = userDao.findById(1);
		Assert.assertNotNull(user1);
		UserPreferenceId userPreferenceId1 = new UserPreferenceId(UserPreferenceType.ENABLE_WEEKENDS.getValue(), user1);
		UserPreference userPreference1 = new UserPreference(userPreferenceId1, UserPreferenceType.ENABLE_WEEKENDS);
		userPreferencesDao.persist(userPreference1);
		Assert.assertNotNull(userPreference1.getUserPreferenceId());
		
		Assert.assertEquals(1, userPreferencesDao.findAll().size());
		
		User user2 = userDao.findById(2);
		Assert.assertNotNull(user2);
		UserPreferenceId userPreferenceId2 = new UserPreferenceId(UserPreferenceType.ENABLE_WEEKENDS.getValue(), user2);
		UserPreference userPreference2 = new UserPreference(userPreferenceId2, UserPreferenceType.ENABLE_WEEKENDS);
		userPreferencesDao.persist(userPreference2);
		
		Assert.assertNotNull(userPreference2.getUserPreferenceId());
		
		Assert.assertEquals(2, userPreferencesDao.findAll().size());
		
		UserPreference userPreferenceForUserForType = userPreferencesDao.getUserPreferenceForUserForType(user1, UserPreferenceType.ENABLE_WEEKENDS);
		Assert.assertNotNull(userPreferenceForUserForType);
		Assert.assertEquals(UserPreferenceValueType.DISABLE.name(), userPreferenceForUserForType.getUserPreferenceValue());
	}
	
}