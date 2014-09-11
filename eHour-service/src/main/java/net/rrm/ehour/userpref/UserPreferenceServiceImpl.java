package net.rrm.ehour.userpref;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserPreference;
import net.rrm.ehour.domain.UserPreferenceType;
import net.rrm.ehour.persistence.userpref.dao.UserPreferencesDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userPreferenceService")
public class UserPreferenceServiceImpl implements UserPreferenceService {
	
	@Autowired
	private UserPreferencesDao userPreferencesDao;

	@Override
	public UserPreference getUserPreferenceForUserForType(User user, UserPreferenceType userPreferenceType) {
//		return userPreferencesDao.getUserPreferenceForUserForType(user, userPreferenceType);
		return null;
	}

	@Override
	public void persist(UserPreference userPreference) {
		userPreferencesDao.persist(userPreference);

	}

	@Override
	public void merge(UserPreference userPreference) {
		userPreferencesDao.merge(userPreference);

	}

}
