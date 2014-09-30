package com.richemont.windchill;


import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @author laurent.linck
 */
public interface WindChillService {

    boolean updateDataForUser(Map<String, Activity> allAssignedActivitiesByCode, String username);

    Project checkProject(String projectCode, String newProjectName);

    Project createProject(String projectCode, String projectName,
                          String customerCode, String customerName,
                          Map<String, Customer> hmAllCustomerByCode);

    Map<String, Activity> getAllAssignedActivitiesByCode(User assignedUser);

    Map<String, Customer> getAllCustomersByCode();

    Activity createNewActivity(Map<String, Comparable> hm, Map<String, Activity> allAssignedActivitiesByCode, User assignedUserName, SimpleDateFormat dateFormat);

    void desactivateObsoleteActivity(Map<String, Activity> hmAllAssignedActivitiesByCode, Map<String, Activity> hmDealedActivities);

}
