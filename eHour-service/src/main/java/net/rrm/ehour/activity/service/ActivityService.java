package net.rrm.ehour.activity.service;

import net.rrm.ehour.domain.Activity;

/**
 * 
 * Interface for exposing all functionality related to {@link Activity}.
 */
public interface ActivityService {

	/**
	 * Saves or Updates an already persisted {@link Activity} in underlying data-storage
	 * @param activity
	 * @return
	 */
	Activity persistActivity(Activity activity);
	
}
