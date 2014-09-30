package com.richemont.jira;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;
import org.json.JSONException;

import javax.json.JsonArray;
import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author laurent.linck
 */


public interface JiraService {

    Map<JiraIssue, Activity> createJiraIssuesForUser(Map<String, Activity> allAssignedActivitiesByCode, String assigneeUsername) throws Exception;

    ArrayList<String> updateJiraIssues(User user, List<TimesheetEntry> entries) throws Exception;

    ArrayList<JiraIssue> getAllJiraIssuesForUser(String assigneeUserName) throws AuthenticationException, JSONException, IOException ;

    void desactivateObsoleteJiraActivity(Map<String, Activity> hmAllAssignedActivitiesByCode, Map<String, Activity> hmDealedActivities);

    JsonArray identifyMissingPjlActivity(Map<JiraIssue, Activity> activitiesMasteredByJira);
}