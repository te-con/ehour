package net.rrm.ehour.activity.service;

import java.util.List;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;

/**
 * 
 * Interface for exposing all functionality related to {@link Activity}.
 */
public interface ActivityService {

	/**
	 * Returns an Activity with passed activityId
	 * @param activityid
	 * @return
	 * @throws ObjectNotFoundException
	 */
	Activity getActivity(Integer activityid) throws ObjectNotFoundException;
	
	/**
	 * Saves or Updates an already persisted {@link Activity} in underlying data-storage
	 * @param activity
	 * @return
	 */
	Activity persistActivity(Activity activity);

	/**
	 * Archives and not hard delete the passed {@link Activity} in underlying data-storage
	 * @param activityId
	 */
	void deleteActivity(Integer activityId);
	
	/**
	 * Returns all Active/Inactive {@link Activity}s for passed {@link User}
	 * @param assignedUser
	 * @return
	 */
	List<Activity> getAllActivitiesForUser(User assignedUser);

	/**
	 * Returns all Active/Inactive {@link Activity}s for passed {@link Project}
	 * @param project
	 * @return
	 */
	List<Activity> getAllActivitiesForProject(Project project);

	/**
	 * Returns all Active/Inactive {@link Activity}s
	 * @return
	 */
	List<Activity> getActivities();

}
