package net.rrm.ehour.persistence.userpref.dao;

import java.util.List;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserPreference;
import net.rrm.ehour.domain.UserPreferenceId;
import net.rrm.ehour.domain.UserPreferenceType;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;

import org.springframework.stereotype.Repository;

@Repository("userPreferenceDao")
public class UserPreferencesDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<UserPreference, UserPreferenceId> implements UserPreferencesDao {

	public UserPreferencesDaoHibernateImpl() {
		super(UserPreference.class);
	}

	@Override
	public UserPreference getUserPreferenceForUserForType(User user, UserPreferenceType userPreferenceType) {
		List<UserPreference> results = null;

		String[] keys = new String[] { "userPreferenceKey", "user" };
		Object[] params = new Object[] { userPreferenceType.getValue(), user };
		
		results = findByNamedQueryAndNamedParam("UserPreference.getUserPreferenceForUserForType", keys, params, false, null);
		
		if(results != null && results.size() >0) {
			return results.get(0);
		} else {
			
			return null;
		}
	}
}
