package com.richemont.jira;

import com.richemont.windchill.DateUtils;
import com.richemont.windchill.WindChillService;
import com.richemont.windchill.WindchillConst;
import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;
import net.rrm.ehour.user.service.UserService;
import org.apache.derby.iapi.util.StringUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.*;

/**
 * @author laurent.linck
 */

@Service("JiraService")
public class JiraServiceImpl implements JiraService {

    @Autowired
    private JiraHelper jiraHelper;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private WindChillService windchillservice;

    @Autowired
    private ReportAggregatedDao reportAggregatedDao;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private TimesheetDao timesheetDao;

    @Autowired
    private CustomerService customerService;

    private static Logger LOGGER = Logger.getLogger("ext.service.JiraServiceImpl");
    public static final String activiyCodePrefix = JiraConst.ACTIVITY_CODE_PREFIX_FOR_JIRA;

    //log4j.properties
    static {
        try {
            LOGGER.info( "ext.service.JiraServiceImpl loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  ArrayList<JiraIssue> getAllJiraIssuesForUser(String assigneeUserName) throws AuthenticationException, JSONException, IOException  {
        ArrayList<JiraIssue> arrList = jiraHelper.getAllJiraIssuesForUser(assigneeUserName);
        return arrList;
    }


    /**
     *
     * @param assigneeUserName
     * @return
     * @throws Exception
     */
    public HashMap<JiraIssue, Activity> createJiraIssuesForUser (HashMap<String, Activity> allAssignedActivitiesByCode, String assigneeUserName) {
        boolean isSync = false;
        HashMap<String, Activity>  hmDealedActivities =  new HashMap<String, Activity>();
        HashMap<JiraIssue, Activity>  hmJiraDealedActivities =  new HashMap<JiraIssue, Activity>();

        //boolean isJiraUser = userService.isLdapUserMemberOf(assigneeUserName, JiraConst.GET_JIRA_ISSUES_FOR_USER_MEMBER_OF);
        //LOGGER.info ("isJiraUser=" + isJiraUser );

        //if ( isJiraUser) {
            //LOGGER.info("Found user in " + JiraConst.GET_JIRA_ISSUES_FOR_USER_MEMBER_OF + " createJiraIssuesForUser() for user " + assigneeUserName);
            //HashMap<String, Project> hmAllProjectsByCode = getAllProjectsByCode();

            ArrayList<JiraIssue> arrListIssues = null;

            try {
                LOGGER.info("\n\n");
                LOGGER.info("*************** STEP #1.1 : GET JIRA ISSUES FOR " + assigneeUserName + " ***************");
                arrListIssues = getAllJiraIssuesForUser(assigneeUserName);
                //HashMap<String, Project> hmAllProjectsByCode = getAllProjectsByCode();
                HashMap<String, Customer> hmAllCustomerByCode = windchillservice.getAllCustomersByCode();

                int compte = 0;

                LOGGER.info("\n\n");
                LOGGER.info("*************** STEP #1.2 : CREATE eHOUR ACTIVITIES " + compte + "/" + arrListIssues.size() + " for user " + assigneeUserName + " ***********");
                for (JiraIssue issue: arrListIssues){
                    compte++;

                    LOGGER.info("\t\t*********** Create ehour activity " + compte + "/" + arrListIssues.size() + " for user " + assigneeUserName + " ***********");
                    LOGGER.debug("\t\tgetJiraObjPath(): " + issue.getJiraObjPath());

                    HashMap<String, Object> hm = new HashMap <String, Object> () ;
                    String projectId = issue.getFields().getProjectLinkAttributes().getPjlProjectId();
                    String projectName = issue.getFields().getProjectLinkAttributes().getPjlProjectName();
                    String projectDesc = "";
                    String OrgId = issue.getFields().getProjectLinkAttributes().getPjlOrgId();
                    String OrgName = issue.getFields().getProjectLinkAttributes().getPjlOrgName() ;

                    hm.put(WindchillConst.ORG_ID, OrgId);
                    hm.put(WindchillConst.ORG_NAME, OrgName);
                    hm.put(WindchillConst.PROJECT_ID, projectId); // ProjectId de Windchill = projectCode de eHour
                    hm.put(WindchillConst.PROJECT_NAME, projectName);

                    hm.put(WindchillConst.PROJECT_DESCRIPTION, projectDesc);
                    hm.put(WindchillConst.ACTIVITY_ID, activiyCodePrefix + issue.getKey()); // ActivityId de Windchill = activityCode de eHour

                    String mainSummary = "";
                    if (issue.getJiraObjPath() != null && issue.getJiraObjPath().size() >0){
                        mainSummary = " pour -" + issue.getJiraObjPath().get( issue.getJiraObjPath().size()-1 ).getFields().getSummary();  // parent +1
                    }
                    String activityName = issue.getFields().getIssueType().getShortName() + " [" + issue.getJiraPath()  + "] " + issue.getFields().getSummary() + mainSummary;
                    if (activityName.length() > JiraConst.JIRA_ACTIVITY_NAME_MAX_LENGTH){
                        activityName = StringUtil.truncate(activityName, JiraConst.JIRA_ACTIVITY_NAME_MAX_LENGTH -3) + "...";
                    }
                    hm.put(WindchillConst.ACTIVITY_NAME , activityName);


                    //hm.put(WindchillConst.ACTIVITY_DESCRIPTION , "");
                    //hm.put(WindchillConst.PROJECT_MANAGER , "");
                    hm.put(WindchillConst.REMAINING_WORK, issue.getFields().getTimeoriginalestimate().toString()); // Remaining Work [eHour] = Timeoriginalestimate (hours) [Jira]
                    hm.put(WindchillConst.PERWORMED_WORK, "0"); //Actual Work [eHour] = Timespent (hours) [Jira]
                    hm.put(WindchillConst.ACTIVITY_START_DATE , issue.getFields().getCreatedStr( JiraConst.JIRA_DATE_FORMAT ));
                    hm.put(WindchillConst.ACTIVITY_END_DATE , DateUtils.getEndOfCurrentFiscalYearStr( JiraConst.JIRA_DATE_FORMAT) );

                    LOGGER.info("\t" + issue.getKey());
                    LOGGER.debug(Utilities.getHashMapContentForDisplay(hm));

                    try {
                        Project prj = checkProject(projectId, projectName, OrgId);
                    } catch (ObjectNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                    User assignedUser = userService.getAuthorizedUser(assigneeUserName);
                    Activity currentActivity = windchillservice.createNewActivity(hm , allAssignedActivitiesByCode, assignedUser, JiraConst.JIRA_DATE_FORMAT);

                    // toutes les activites nouvelles ou mises a jour dans eHour
                    if (currentActivity != null) {
                        hmDealedActivities.put( currentActivity.getCode(), currentActivity );
                        hmJiraDealedActivities.put(issue, currentActivity);
                        LOGGER.debug("\tdealedActivities=" + currentActivity.getName());
                    } else {
                        LOGGER.debug ("\t" + issue.getKey() + " CREATION ERROR !!");
                    }

                }

                // Les activites non nouvelles ou non mises a jour dans eHour doivent etre desactivees
                desactivateObsoleteJiraActivity(allAssignedActivitiesByCode, hmDealedActivities);
                isSync = true;

            } catch (AuthenticationException e) {
                e.printStackTrace();
                isSync = false;
            } catch (JSONException e) {
                e.printStackTrace();
                isSync = false;
            } catch (IOException e) {
                e.printStackTrace();
                isSync = false;
            }
        //}else{
        //    LOGGER.info("User do not exist in " + JiraConst.GET_JIRA_ISSUES_FOR_USER_MEMBER_OF + " : skip createJiraIssuesForUser() for user " + assigneeUserName);
        //    isSync = true;
        //}
        if (isSync) LOGGER.info("***** updateJiraData for user " + assigneeUserName + " SYNC SUCCESS *****");
        else  LOGGER.error("***** updateJiraData for user " + assigneeUserName + " SYNC FAILED *****");


        if (isSync) return hmJiraDealedActivities;
        else return null;
    }


    /**
     *
     * @param newProjectCode
     * @param newProjectName
     * @param customerId
     * @return
     */
    private Project checkProject (String newProjectCode, String newProjectName, String customerId) throws ObjectNotFoundException {
        Project prj = null;
        if (newProjectCode != null && !newProjectCode.equals(JiraConst.PJL_DEFAULT_PROJECT_ID) && newProjectName != null && !newProjectName.equals(JiraConst.PJL_DEFAULT_PROJECT_NAME) && customerId != null) {
            prj = projectService.getProject(newProjectCode);
            if ( prj == null){
                LOGGER.debug("\tProject need to be created");
                prj = createProject(newProjectCode, newProjectName, customerId);
            } else {
                LOGGER.debug("\tProject exists");
                // on s assure que le projet est actif
                LOGGER.debug("\t\tActive projet: " + prj.getName() );
                prj.setActive(true);

                // verifie si le projet Jira a ete renomme depuis la derniere mise a jour dans e-Hour
                if (! newProjectName.equals( prj.getName() ) ) {
                    LOGGER.debug("\t\tUpdate project name: " + prj.getName() + " --> " + newProjectName );
                    prj.setName(newProjectName);
                }

                LOGGER.debug("\t\tproject name=ok");
                prj = projectService.updateProject(prj);
            }
        } else {
            prj = projectService.getProject(JiraConst.PJL_DEFAULT_PROJECT_INTERNAL_ID);
        }

        return prj;

    }

    public Project createProject(String projectCode, String projectName, String customerCode) {
        LOGGER.debug("\tCreate project ");
        LOGGER.debug("\t\tprojectCode=" + projectCode );
        LOGGER.debug("\t\tprojectName= " + projectName);
        LOGGER.debug("\t\tcustomerCode= " + customerCode);
        Project newProject = new Project();
        Customer customer = customerService.getCustomer(customerCode);
        newProject.setProjectCode(projectCode);
        newProject.setName(projectName);
        newProject.setCustomer(customer);
        projectService.createProject(newProject);
        return newProject;
    }


    public Number getSpentHoursForActivity(Activity activity){
        ActivityAggregateReportElement hoursForActivity = reportAggregatedDao.getCumulatedHoursForActivity(activity);
        return hoursForActivity.getHours();
    }


    /**
     *
     * @param user
     * @param entries
     * @return
     */
    public ArrayList<String> updateJiraIssues(User user, List<TimesheetEntry> entries) {
        ArrayList<String> failedActivitiesList = new ArrayList<String>() ;
        String issueKey = "";
        String spentHoursUnit = "h";
        String newSpentHours ="0" + spentHoursUnit;
        String newComment = "";
        String newCommentWithDate = "";
        String newStarted = "";
        boolean needToCreateWorkload = true;
        Number performedHours = 0;

        for (TimesheetEntry anEntry :entries) {
            String activityCode = anEntry.getEntryId().getActivity().getCode().toString();
            LOGGER.info("\n");
            LOGGER.info("UPDATE Jira from eHour for " + activityCode);
            //Float previousTimeSpent = new Float(0);
            needToCreateWorkload = true;
            issueKey = activityCode.substring(activiyCodePrefix.length(), activityCode.length());
            ArrayList<JiraWorklog> jw = null;

            Float hours = anEntry.getHours();
            Float updatedHours = anEntry.getUpdatedHours();
            Date updateDate = anEntry.getUpdateDate();
            Date entryDate = anEntry.getEntryId().getEntryDate();

            LOGGER.debug("\thours=" + hours );
            LOGGER.debug("\tupdatedHours=" + updatedHours );
            LOGGER.debug("\tupdateDate=" + updateDate );  // date when the entry has been updated  : Tue Nov 05 16:15:10 CET 2013
            LOGGER.debug("\tentryDate=" + entryDate );   // date of the entry : Thu Nov 07 00:00:00 CET 2013

            try {
                // get info for new Worklog
                LOGGER.info("\tGet info for new Worklog " + issueKey);
                jw = JiraHelper.getWorkLogsForJiraIssue(issueKey);

                performedHours = updatedHours;
                DateTime entryDateTime = new DateTime( entryDate ); //2013-11-07T00:00:00.000+01:00

                LOGGER.info("\t" + jw.size() + " worklog(s) for " + issueKey);
                int i=0;
                for (JiraWorklog aWorklog :jw){
                    i++;
                    LOGGER.debug("\t--- workload #" + i + " ---");
                    LOGGER.debug(aWorklog.toString());

                    //LOGGER.debug("\t\taWorkload.getAuthor()=" + aWorklog.getAuthor().getName());   //admin_jira
                    //LOGGER.debug("\t\tgetUser()=" + user.getUsername()); //laurent.linck

                    DateTime startedWorkLog = aWorklog.getStarted();
                    LOGGER.debug("\t\taWorkload.getStarted()=" + startedWorkLog ); //2013-11-05T23:00:00.000Z

                    if (aWorklog.getComment() != null ) {
                        if ( aWorklog.getAuthor().getName().equals(JiraConst.JIRA_USER) && aWorklog.getComment().contains("from eHour for " + user.getUsername() ) ) {
                            // already exists
                            if ( startedWorkLog.getDayOfYear() == entryDateTime.getDayOfYear() ){
                                // if already exist but not up to date
                                LOGGER.debug("\t\ta worklog already exists for this day" );
                                LOGGER.debug("\t\t\taWorklog.getTimeSpentSeconds()=" + aWorklog.getTimeSpentSeconds() );
                                LOGGER.debug("\t\t\tperformedHours in seconds=" + performedHours.longValue()*3600 );
                                if (aWorklog.getTimeSpentSeconds() != performedHours.longValue()*3600 )  {
                                    LOGGER.debug("\t\t\tbut not up to date !" );
                                    JiraHelper.deleteJiraWorklog(issueKey, aWorklog.getId());
                                    if ( performedHours.longValue() == 0 ) {
                                        LOGGER.debug("\t\t\tworklog has been set to 0: has been deleted" );
                                        needToCreateWorkload = false;
                                    }
                                } else {
                                    LOGGER.debug("\t\t worklog is up to date OR worklog has been set to 0 (deleted)" );
                                    needToCreateWorkload = false;
                                }
                            break;
                            }
                        }
                    }
                }

                // Add new worklog
                if (needToCreateWorkload && performedHours.longValue()>0 ){

                    newSpentHours = String.format(Locale.FRENCH, "%.2f", performedHours) + spentHoursUnit;     // unit = hours
                    LOGGER.debug("\t\tNew added newSpentHours : " + newSpentHours);
                    newStarted = Utilities.formatToWorklogJiraDate(entryDate); // 2013-05-07T21:59:59.999Z

                    LOGGER.info("\tAdd new workload for " + issueKey);
                    newComment = "[" + newSpentHours + "] added from eHour for " + user.getUsername();
                    newCommentWithDate = newComment + " at " + DateUtils.convertDateToString( new Date(), JiraConst.JIRA_DATE_FORMAT ) + " for " + entryDate +  "/" + entryDateTime.getDayOfYear() ; // 03/09/2013 02:15:21
                    String res = JiraHelper.addNewJiraWorklog(issueKey, newCommentWithDate, newStarted, newSpentHours);
                    LOGGER.debug(res);

                    if (res.contains( "errorMessage" )) {
                        if (!failedActivitiesList.contains(activiyCodePrefix + issueKey)) failedActivitiesList.add (activiyCodePrefix + issueKey);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                failedActivitiesList.add (activiyCodePrefix + issueKey);
            } catch (IOException e) {
                e.printStackTrace();
                failedActivitiesList.add (activiyCodePrefix + issueKey);
            } catch (AuthenticationException e) {
                e.printStackTrace();
                failedActivitiesList.add (activiyCodePrefix + issueKey);
            }

        }
        return failedActivitiesList;
    }


    /**
     *
     * @param hmAllAssignedActivitiesByCode : Toutes les activites eHour existantes pour l utilisateur
     * les activites *dealedActivities* non presentes dans *hmAllAssignedActivitiesByCode* doivent etre desactivees si pas de booked hours
     *
     * @param hmAllAssignedActivitiesByCode  all the activities from ehour
     * @param hmDealedActivities  all the new and modified activities from windchill to be updated in Ehour
     */
    public void desactivateObsoleteJiraActivity(HashMap<String, Activity> hmAllAssignedActivitiesByCode, HashMap<String, Activity> hmDealedActivities) {

        List<Activity> list1 =  new ArrayList<Activity> (hmAllAssignedActivitiesByCode.values());
        List<Activity> list2 =  new ArrayList<Activity> (hmDealedActivities.values());
        list1.removeAll(list2);
        LOGGER.debug("\tdesactivateObsoleteJiraActivity() ");

        int i =0;
        for(Activity anActivity: list1){

            if ( anActivity.getCode().startsWith(JiraConst.ACTIVITY_CODE_PREFIX_FOR_JIRA)){
                i++;
                LOGGER.debug("\t\tCheck for Jira activity " + anActivity.getName() + " [active=" + anActivity.getActive() + "]");
                LOGGER.debug("\t\t\tProject " + anActivity.getProject().getName());
                if (anActivity.getActive()){
                    LOGGER.debug("\t\t\tGetAllottedHours(): " + anActivity.getAllottedHours());

                    // S il y a des booked hours(= existe une timesheet), on ne veut pas desactiver l activite. Elle sera juste Locked
                    TimesheetEntry ts = timesheetDao.getLatestTimesheetEntryForActivity(anActivity.getId());

                    if (ts != null){
                        LOGGER.debug("\t\t\tLocking Jira activity" );
                        anActivity.setLocked(true);
                        activityService.persistActivity(anActivity);
                        LOGGER.debug("\t\t\tactive=" + anActivity.getActive() + " / locked=" + anActivity.getLocked());
                    } else  {
                        LOGGER.debug("\t\t\tDesactivate Jira activity" );
                        anActivity.setActive(false);
                        activityService.persistActivity(anActivity);
                        LOGGER.debug("\t\t\tactive=" + anActivity.getActive() + " / locked=" + anActivity.getLocked());
                    }

                } else {
                    LOGGER.debug("\t\t\tactive=" + anActivity.getActive() + " / locked=" + anActivity.getLocked());
                }

            }

        }
        LOGGER.debug("\t\tSubstract " + i +  " Jira obsolete activities in e-Hour ");
    }



    /**
     *
     * @param activitiesMasteredByJira
     * @return
     */
    public JsonArray identifyMissingPjlActivity(HashMap<JiraIssue, Activity> activitiesMasteredByJira){
        //ArrayList< HashMap<String, Comparable> > listToSend = new ArrayList <HashMap<String, Comparable> >();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (Map.Entry<JiraIssue, Activity> entry  : activitiesMasteredByJira.entrySet()){
            JiraIssue issue = entry.getKey();
            Activity activity = entry.getValue();
            LOGGER.debug ("New Jira Activities to be created in PJL :");
            LOGGER.debug ("\teHour Activity: " + activity.getName());
            LOGGER.debug ("\teHour Project ID: " + activity.getProject().getProjectId() );
            LOGGER.debug ("\tJira Isssue: " + issue.getKey());
            String jiraActivityId = issue.getFields().getProjectLinkAttributes().getPjlActivityId();
            LOGGER.debug ("\tJira Activity ID: " + issue.getFields().getProjectLinkAttributes().getPjlActivityId());
            LOGGER.debug ("\tJiraPath " + issue.getJiraPath() );
            LOGGER.debug ("\tJiraObjPath " + issue.getJiraObjPath() );

            if ( activity.getProject().getProjectId().compareTo(JiraConst.PJL_DEFAULT_PROJECT_INTERNAL_ID) != 0  ){
                if ( jiraActivityId == null || !"".equals(jiraActivityId) || !JiraConst.PJL_DEFAULT_ACTIVITY_ID.equals(jiraActivityId)) {
                    jsonArrayBuilder.add( JsonHelper.createJsonObjectForProxyWindActivity(issue, activity) );
                }
            }
        }
        return jsonArrayBuilder.build();
    }

}

