package com.richemont.windchill;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.User;

import javax.json.JsonArray;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

/**
 * @author laurent.linck
 */
public interface WindChillUpdateService {

	// called by net.rrm.ehour.ui.timesheet.panel

    List<HashMap<String,Comparable>> updateProjectLink2(User user , List<Activity> activities) throws Exception, RemoteException;

    List<String> updateProjectLink(User user, List<Activity> activities) throws Exception;

    List<ProxyWindActivity> createMissingPjlActivities (User user, JsonArray activitiesPjlToBeCreated,  List<Activity> modifiedJiraActivities) throws Exception;

    JsonArray updateSessionParam ( List<ProxyWindActivity> resultActivitiesList, JsonArray jSonAllJiraActivities);
}

