package com.richemont.windchill;


import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * @author laurent.linck
 */
public interface WindChillService {

    boolean updateDataForUser(HashMap<String, Activity> allAssignedActivitiesByCode , String username);

    Project checkProject (String projectCode, String newProjectName);
    Project createProject(String projectCode, String projectName,
                          String customerCode, String customerName,
                          HashMap<String, Customer> hmAllCustomerByCode) ;

    HashMap<String, Activity> getAllAssignedActivitiesByCode(User assignedUser);
    HashMap<String, Customer> getAllCustomersByCode();

    Activity createNewActivity(HashMap <String, Object > hm, HashMap<String, Activity> allAssignedActivitiesByCode, String assignedUserName, SimpleDateFormat dateFormat);

    void desactivateObsoleteActivity(HashMap<String, Activity> hmAllAssignedActivitiesByCode, HashMap<String, Activity> hmDealedActivities);

}
