package net.rrm.ehour.persistence.userpref.dao;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserPreferenceType;
import net.rrm.ehour.domain.UserPreference;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;

import org.springframework.stereotype.Repository;

@Repository("userPreferencesDao")
public class UserPreferencesDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<UserPreference, Integer> implements UserPreferencesDao {

	public UserPreferencesDaoHibernateImpl() {
		super(UserPreference.class);
	}

	@Override
	public void addUserPreferenceToUser(UserPreferenceType userPreferenceType, User user) {
		//UserPreferences preferences = new UserPreferences(userPreferenceType.toString(), userPreferenceType.getValue().toString(), user);
		//getHibernateTemplate().persist(preferences);
	}

}
