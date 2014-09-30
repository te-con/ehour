package com.richemont.jira;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;
import org.json.JSONException;

import javax.json.JsonArray;
import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author laurent.linck
 */


public interface JiraService {

    HashMap<JiraIssue, Activity> createJiraIssuesForUser(HashMap<String, Activity> allAssignedActivitiesByCode, String assigneeUsername) throws Exception;

    ArrayList<String> updateJiraIssues(User user, List<TimesheetEntry> entries) throws Exception;

    ArrayList<JiraIssue> getAllJiraIssuesForUser(String assigneeUserName) throws AuthenticationException, JSONException, IOException ;

    void desactivateObsoleteJiraActivity(HashMap<String, Activity> hmAllAssignedActivitiesByCode, HashMap<String, Activity> hmDealedActivities);

    JsonArray identifyMissingPjlActivity(HashMap<JiraIssue, Activity> activitiesMasteredByJira);
}