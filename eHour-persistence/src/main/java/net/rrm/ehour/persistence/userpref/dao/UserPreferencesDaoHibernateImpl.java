package net.rrm.ehour.persistence.userpref.dao;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserPreference;
import net.rrm.ehour.domain.UserPreferenceId;
import net.rrm.ehour.domain.UserPreferenceType;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("userPreferenceDao")
public class UserPreferencesDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<UserPreference, UserPreferenceId> implements UserPreferencesDao {

	public UserPreferencesDaoHibernateImpl() {
		super(UserPreference.class);
	}

	@Override
	@Transactional
	public UserPreference merge(UserPreference domobj) {
		return getHibernateTemplate().merge(domobj);
	}

//	@Override
//	public UserPreference getUserPreferenceForUserForType(User user, UserPreferenceType userPreferenceType) {
//		List<UserPreference> results = new ArrayList<UserPreference>();
//
//		String[] keys = new String[] { "user", "userPreferenceKey"};
//		Object[] params = new Object[] { user, userPreferenceType.getValue() };
//		
//		results = findByNamedQueryAndNamedParam("UserPreference.getUserPreferenceForUserForType", keys, params, false, null);
//		return null;
//	}

}
