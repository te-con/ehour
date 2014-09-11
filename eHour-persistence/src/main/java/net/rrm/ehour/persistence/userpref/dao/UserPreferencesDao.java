package net.rrm.ehour.persistence.userpref.dao;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserPreference;
import net.rrm.ehour.domain.UserPreferenceId;
import net.rrm.ehour.domain.UserPreferenceType;
import net.rrm.ehour.persistence.dao.GenericDao;

public interface UserPreferencesDao extends GenericDao<UserPreference, UserPreferenceId> {

	 UserPreference getUserPreferenceForUserForType(User user, UserPreferenceType userPreferenceType);

}
