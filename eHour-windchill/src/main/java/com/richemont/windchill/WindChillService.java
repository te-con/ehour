package com.richemont.windchill;


import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @author laurent.linck
 */
public interface WindChillService {

    boolean updateDataForUser(Map<String, Activity> allAssignedActivitiesByCode, String username);

    Map<String, Activity> getAllAssignedActivitiesByCode(User assignedUser);
    Map<String, Customer> getAllCustomersByCode();

    Activity createNewActivity(Map<String, Comparable> hm, Map<String, Activity> allAssignedActivitiesByCode, User assignedUser, SimpleDateFormat dateFormat);
}
