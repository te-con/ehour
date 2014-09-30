package com.richemont.jira;

import java.text.SimpleDateFormat;

/**
 * @author laurent.linck
 */
public class JiraConst {

    protected static boolean DEBUG = true;

    // Authentication
    public static final String JIRA_SERVER = "http://ismjira.ch.rccad.net/jira";
    public static final String JIRA_USER = "admin_jira";
    public static final String JIRA_PWD = "Jira!2013";
    /**
     private static final String BASE_URL = "http://localhost:8081";
     private static final String jiraUser = "laurent.linck";
     private static final String jiraPwd = "jira";
     **/

    // LDAP entry
    public static String GET_JIRA_ISSUES_FOR_USER_MEMBER_OF = "cn=timesheet-tracking,ou=people,cn=AdministrativeLdap,cn=Windchill,o=ptc";

    // Jira object type
    public static final String JIRA_ISSUE_TYPE_SHORTNAME_6 = "SUP";  //Demande de support
    public static final String JIRA_ISSUE_TYPE_SHORTNAME_9 = "EPI";  //Epic
    public static final String JIRA_ISSUE_TYPE_SHORTNAME_10 = "STO";  //Story
    public static final String JIRA_ISSUE_TYPE_SHORTNAME_11 = "TEC";  //Tache technique
    public static final String JIRA_ISSUE_TYPE_SHORTNAME_15 = "BUG";  //Bug
    public static final String JIRA_ISSUE_TYPE_SHORTNAME_10100 = "IMP";  //Improvement

    public static final int JIRA_ACTIVITY_NAME_MAX_LENGTH = 255; //as defined in Oracle for column_size
    public static final SimpleDateFormat JIRA_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    // REST URLs

    public static final String CREATE_WORKLOG_URL = "/rest/api/2/issue/%s/worklog?adjustEstimate=auto";

    public static final String GET_WORKLOAD_URL = "/rest/api/2/issue/%s/worklog";
    public static final String GET_ISSUE_URL = "/rest/api/2/search?jql=status!=Closed+AND+assignee='%s'&maxResults=-1"; // attention a l'ordre de &maxResults

    public static final String GET_USER_URL = "/rest/api/2/user?username=%s";
    public static final String GET_USER_WITH_GROUPS_URL = "/rest/api/2/user?username=%s&expand=groups";
    public static final String GET_PROJECTS_URL = "/rest/api/2/project";

    // http://ismjira.ch.rccad.net/jira/rest/api/latest/issue/EVO-2504?fields&expand
    // http://ismjira.ch.rccad.net/jira/rest/api/2/issue/EVO-2504/editmeta
    public static final String UPDATE_ISSUE_URL = "/rest/api/2/issue/%s";

    // /rest/api/2/issue/EVO-2504{"fields":{"customfield_11610":"xx"}}"
    // curl -D- -u user:pwd -X PUT --data {"fields":{"customfield_11610":"xx"}} -H "Content-Type: application/json" http://ismjira.ch.rccad.net/jira/rest/api/2/issue/EVO-2504

    public static final String DELETE_WORKLOG_URL = "/rest/api/2/issue/%s/worklog/%S";


    // eHour DB
    public static final String ACTIVITY_CODE_PREFIX_FOR_JIRA = "jira:";   // in Table ACTIVITY Column CODE

    // PJL
    public static final String PJL_ATTR_SEPARATOR = "~~" ;  // Richemont~~4530~~TEST-LLI-EHOUR-01~~027295~~wt.projmgmt.admin.Project2:6590512736

    public static final String PJL_DEFAULT_ORG_NAME = "Richemont";
    public static final String PJL_DEFAULT_ORG_ID = "4530";
    public static final String PJL_DEFAULT_PROJECT_NAME = "TEMP JIRA PROJECT";
    public static final Integer PJL_DEFAULT_PROJECT_INTERNAL_ID = 2538357;  // must exist as project in eHour
    public static final String PJL_DEFAULT_PROJECT_ID = "Not set";  //projectId in Jira = Project Code in eHour = ida2a2 in Windchill
    public static final String PJL_DEFAULT_ACTIVITY_ID = "Not set";


    // HASHMAP KEYS
    public static final String orgId = "orgId" ;
    public static final String orgName = "orgName" ;
    public static final String projectID = "projectID" ;
    public static final String projectName = "projectName" ;
    public static final String projectDescription = "projectDescription" ;
    public static final String activityId = "activityId" ;
    public static final String activityName = "activityName" ;
    public static final String activityDescription = "activityDescription" ;
    public static final String summaryActivities = "summaryActivities" ;
    public static final String projectManager = "projectManager" ;
    public static final String work = "work";  // Float: Remaining Work = projectAllocatedHours
    public static final String performedWork = "performedWork"; // Float: Actual Work = projectPerformedHours
    public static final String projectActivityStartDate = "projectActivityStartDate";
    public static String projectActivityEndDate = "projectActivityEndDate";

}
