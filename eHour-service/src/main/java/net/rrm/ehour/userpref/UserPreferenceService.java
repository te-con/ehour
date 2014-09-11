package net.rrm.ehour.userpref;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserPreference;
import net.rrm.ehour.domain.UserPreferenceType;

public interface UserPreferenceService {
	
	UserPreference getUserPreferenceForUserForType(User user, UserPreferenceType userPreferenceType);
	
	void persist(UserPreference userPreference);
	
	void merge(UserPreference userPreference);

}
