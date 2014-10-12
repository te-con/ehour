package net.rrm.ehour.activity.service;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;

import java.util.List;

/**
 * 
 * Interface for exposing all functionality related to {@link Activity}.
 */
public interface ActivityService {

	/**
	 * Returns an Activity with passed activityId
	 * 
	 * @param activityid
	 * @return
	 * @throws ObjectNotFoundException
	 */
	Activity getActivity(Integer activityid) throws ObjectNotFoundException;

	/**
	 * Searches and if found returns an {@link Activity} having the code same as
	 * passed in the parameter.
	 * 
	 * @param activityCode
	 * @return
	 */
	Activity getActivity(String code);

	/**
	 * Saves or Updates an already persisted {@link Activity} in underlying
	 * data-storage
	 * 
	 * @param activity
	 * @return
	 */
	Activity persistActivity(Activity activity);


	/**
	 * Archives and not hard delete the passed {@link Activity} in underlying
	 * data-storage
	 * 
	 * @param activityId
	 */
	void deleteActivity(Integer activityId);

	/**
	 * Returns all Active/Inactive {@link Activity}s for passed {@link User}
	 * 
	 * @param assignedUser
	 * @return
	 */
	List<Activity> getAllActivitiesForUser(User assignedUser);

	/**
	 * Returns all Active/Inactive {@link Activity}s for passed {@link Project}
	 * 
	 * @param project
	 * @return
	 */
	List<Activity> getAllActivitiesForProject(Project project);

	List<Activity> getActivities(Project project, DateRange dateRange);

	/**
	 * Returns all Active/Inactive {@link Activity}s
	 * 
	 * @return
	 */
	List<Activity> getActivities();

	/**
	 * gets all activities of an user
	 * 
	 * @param userId
	 * @param dateRange
	 * @return
	 */
	List<Activity> getActivitiesForUser(Integer userId, DateRange dateRange);

	/**
	 * gets all activity of a customer
	 * 
	 * @param customer
	 * @return
	 */
	List<Activity> findAllActivityForCustomer(Customer customer);

	List<Activity> getAllActivitiesForcustomers(List<Customer> customers, DateRange dateRange);
}
