package com.richemont.jira;

import com.richemont.windchill.ProxyWindActivity;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author laurent.linck
 */
@Component
public class JiraHelper {

    private static Logger LOGGER = Logger.getLogger("ext.service.JiraServiceImpl");

    /**
     *
     * @param assigneeUserName
     * @throws AuthenticationException
     * @throws JSONException
     * @throws IOException
     */
    public static ArrayList<JiraIssue> getAllJiraIssuesForUser(String assigneeUserName) throws AuthenticationException, JSONException, IOException {
        HashMap<String,JiraIssue > hm = new HashMap<String,JiraIssue>(); // tous les Jira not closed
        ArrayList<JiraIssue> ar = new ArrayList<JiraIssue>();
        ArrayList<String> rmIssueList = new ArrayList<String>(); // contient les parents à ne pas pousser dans eHour

        String getIssues = InvokeHelper.invokeGetMethod( String.format(JiraConst.GET_ISSUE_URL, assigneeUserName) );
        //LOGGER.debug(getIssues);
        getIssues = getIssues.substring(getIssues.indexOf("["), getIssues.length()).trim();

        ObjectMapper mapper = new ObjectMapper();
        JSONArray issueArray = new JSONArray(getIssues);

        int j = 1;
        for (int i = 0; i < issueArray.length(); i++) {
            JSONObject issues = issueArray.getJSONObject(i);
            j = i+1;
            LOGGER.debug("==========> " + j + "/" + issueArray.length() + " <==========");
            //LOGGER.debug(issues.toString());
            try{
                JiraIssue jr = mapper.readValue(issues.toString() , JiraIssue.class);
                LOGGER.debug("\tkey = " + jr.getKey());
                LOGGER.debug("\tidType = " + jr.getFields().getIssueType().getId());
                LOGGER.debug("\tshortName = " + jr.getFields().getIssueType().getShortName());
                LOGGER.debug("\tJira name = " + jr.getFields().getIssueType().getName());
                LOGGER.debug("\tStatus name = " + jr.getFields().getStatus().getName());
                LOGGER.debug("\tEstimated work = " + jr.getFields().getTimeoriginalestimate() );
                LOGGER.debug("\tAggregate Estimated work = " + jr.getFields().getAggregatetimeestimate());
                LOGGER.debug("\tStatus id = " + jr.getFields().getStatus().getId());
                LOGGER.debug("\tdescription = " + jr.getFields().getIssueType().getDescription());
                LOGGER.debug("\tsubtask = " + jr.getFields().getIssueType().getSubtask());
                LOGGER.debug("\ttoBeSendToEHour = " + jr.getFields().getEHour().getValue() );
                LOGGER.debug("\thasParent = " + jr.getFields().hasParent() );
                LOGGER.debug("\tEpic link = " + jr.getFields().getEpicName() );
                //LOGGER.debug("\tWARM = " + jr.getFields().getWarm().getValue() );
                //LOGGER.debug("\tPJL = " + jr.getFields().getPJL().getValue() );
                LOGGER.debug("\tPjlOrgId=" + jr.getFields().getProjectLinkAttributes().getPjlOrgId() );
                LOGGER.debug("\tPjlOrgName=" + jr.getFields().getProjectLinkAttributes().getPjlOrgName());
                LOGGER.debug("\tPjlProjectName=" + jr.getFields().getProjectLinkAttributes().getPjlProjectName());
                LOGGER.debug("\tPjlProjectId=" + jr.getFields().getProjectLinkAttributes().getPjlProjectId() );

                JiraIssue topJira;
                JiraIssue aParent;
                String jiraPath = "";
                ArrayList jiraLongPath = new ArrayList();

                // According to Jira Api, Epic is not a parent, just an attribute
                // OK with the custom JiraIssue
                if (jr.getFields().hasParent()) {  // Technical task
                    LOGGER.debug("\tNavigate the parents"); // story
                    LOGGER.debug("\t\tParent +1 =" + jr.getFields().getParent().getKey() );
                    // Les stories ayant des enfants (Tache techniques par ex) ne doivent pas être gardées, identifions les.
                    // si on est un enfant, alors on vire le parent qui ne doit pas aller dans ehour
                    // note: dans ce cas: parent et enfant ont le meme owner
                    aParent =  jr.getFields().getParent() ;

                    // Si le parent a un EPIC, il faut prendre ses attributs
                    if (aParent.getFields().getEpicName() != null){
                        // EPIC
                        topJira = aParent.getFields().getParent(); // Epic
                        LOGGER.debug("\t\tParent +2 =" + topJira.getKey() );
                        jiraPath = jiraPath + "/" + topJira.getKey();
                        jiraLongPath.add(topJira);

                        // epic becomes the parent of the parent
                        aParent.getFields().setParent(topJira);

                        // be sure that PJL attributes are set for parent +1
                        aParent = copyProjectLinkAttributes(topJira, aParent);

                        // Les Epic a ne doivent pas être gardées, identifions les.
                        rmIssueList.add( topJira.getKey() );
                        //topJira.getFields().getEHour().setValue("No");
                        //LOGGER.debug("\t\tnew toBeSendToEHour=" + topJira.getFields().getEHour().getValue() );

                    } else {
                        // On prend les attributs de la Story
                        topJira = jr.getFields().getParent();
                    }
                    rmIssueList.add( aParent.getKey() );

                    jiraPath = jiraPath + "/" + jr.getFields().getParent().getKey();
                    jiraLongPath.add(jr.getFields().getParent());

                } else {
                    // on prend les attributs du jr
                    topJira = jr;
                }
                jiraPath = jiraPath + "/" + jr.getKey();

                //assign jiraPath
                jr.setJiraPath(jiraPath);
                jr.setJiraObjPath(jiraLongPath);

                LOGGER.debug("\t\tjiraPath = " + jr.getJiraPath() );
                LOGGER.debug("\t\tjiraObjPath = " + jr.getJiraObjPath() );

                // true
                if (topJira.getFields().getEHour().getValue()) {
                    jr = copyProjectLinkAttributes(topJira, jr);
                    hm.put(jr.getKey(), jr);
                } else {
                    LOGGER.debug("\tNOT ELIGIBLE to eHour: type has been changed or not the right type ... ");
                }

                // Display worklogs for debug
                ArrayList<JiraWorklog> jiraWorkload = jr.getWorklogs();
                LOGGER.debug("\tworklogs: " + jiraWorkload.size());
                if (jiraWorkload.size() > 0){
                    for (JiraWorklog aWorkload :jiraWorkload){
                        LOGGER.debug("\t\t" + aWorkload);
                    }
                }

            } catch(org.codehaus.jackson.map.exc.UnrecognizedPropertyException ue){
                LOGGER.debug(ue.getMessage());
            }
        }

        // remove stories with technical tasks
        //LOGGER.debug("\tRemove Epic and Stories with technical tasks");
        for( String aKey :rmIssueList) {
            if (hm.containsKey(aKey)) hm.remove(aKey);
            LOGGER.debug("\t\tRemove the parent story " + aKey);
        }

        ar.addAll(hm.values());

        return ar;
    }

    /**
     *
      * @param aJiraIssueFrom
     * @param aJiraIssueTo
     * @return
     */
    public static JiraIssue copyProjectLinkAttributes(JiraIssue aJiraIssueFrom, JiraIssue aJiraIssueTo){
        LOGGER.debug("\tCopy PJL Attributes from " + aJiraIssueFrom.getKey() + " to " + aJiraIssueTo.getKey()) ;
        String topJiraPjlOrgId = aJiraIssueFrom.getFields().getProjectLinkAttributes().getPjlOrgId();
        String topJiraPjlOrgName = aJiraIssueFrom.getFields().getProjectLinkAttributes().getPjlOrgName();
        String topJiraPjlProjectName=  aJiraIssueFrom.getFields().getProjectLinkAttributes().getPjlProjectName();
        String topJiraPjlProjectId =  aJiraIssueFrom.getFields().getProjectLinkAttributes().getPjlProjectId();

        // assign eHour props
        aJiraIssueTo.getFields().getProjectLinkAttributes().setPjlOrgId(topJiraPjlOrgId);
        aJiraIssueTo.getFields().getProjectLinkAttributes().setPjlOrgName(topJiraPjlOrgName);
        aJiraIssueTo.getFields().getProjectLinkAttributes().setPjlProjectName(topJiraPjlProjectName);
        aJiraIssueTo.getFields().getProjectLinkAttributes().setPjlProjectId(topJiraPjlProjectId);

        LOGGER.debug("\t\tNew value for JiraPjlOrgId to " + aJiraIssueTo.getFields().getProjectLinkAttributes().getPjlOrgId() );
        LOGGER.debug("\t\tNew value for JiraPjlOrgName to " + aJiraIssueTo.getFields().getProjectLinkAttributes().getPjlOrgName() );
        LOGGER.debug("\t\tNew value for JiraPjlProjectName to " + aJiraIssueTo.getFields().getProjectLinkAttributes().getPjlProjectName() );
        LOGGER.debug("\t\tNew value for JiraPjlProjectId to " + aJiraIssueTo.getFields().getProjectLinkAttributes().getPjlProjectId() );

        return aJiraIssueTo;
    }


    /**
     *
     * @param jr
     * @return
     */
        public static HashMap<String,JiraIssue > getEligibleStories (JiraIssue jr) {
        HashMap<String,JiraIssue > hm = new HashMap<String,JiraIssue>();
        ArrayList<String> rmParentList = new ArrayList<String>(); // contient les parents à ne pas pousser dans eHour

        return hm;
    }

    /**
     * @param issueKey
     * @return
     * @throws AuthenticationException
     * @throws JSONException
     * @throws IOException
     */
    public static JiraIssue getJiraIssue (String issueKey) throws AuthenticationException, JSONException, IOException {
        HashMap<String,JiraIssue > hm = new HashMap<String,JiraIssue>(); // tous les Jira not closed
        JiraIssue jr = null;
        String getIssue = InvokeHelper.invokeGetMethod( String.format(JiraConst.UPDATE_ISSUE_URL, issueKey) );
        //System.out.println(">getJiraIssue: " + getIssue);
        //getIssue = getIssue.substring(getIssue.indexOf("["), getIssue.length()).trim();

        ObjectMapper mapper = new ObjectMapper();
        JSONObject issue = new JSONObject(getIssue);
        //LOGGER.info("\n>> Get Jira issues for " + issue + " <<");
        try{
            jr = mapper.readValue(issue.toString() , JiraIssue.class);
        } catch(org.codehaus.jackson.map.exc.UnrecognizedPropertyException ue){
            System.out.println(ue.getMessage());
        }
        return jr;

    }

    /**
     *
     * @param anIssue
     * @return
     * @throws JSONException
     * @throws IOException
     * @throws AuthenticationException
     */
    public static ArrayList<JiraWorklog> getWorkLogsForJiraIssue (String anIssue) throws JSONException, IOException, AuthenticationException {
        int j = 1;
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<JiraWorklog> ar = new ArrayList<JiraWorklog>();
        JSONArray issueArray = queryWorkLogsForJiraIssue(anIssue);

        for (int i = 0; i < issueArray.length(); i++) {
            JSONObject issues = issueArray.getJSONObject(i);
            //Utilities.printOut(issues.toString());
            j = i+1;
            //Utilities.printOut("========== " + j + "/" + issueArray.length() + " ==========");
            //Utilities.printOut(issues.toString());
            try{
                JiraWorklog w = mapper.readValue(issues.toString() , JiraWorklog.class);
                ar.add(w);
                //Utilities.printOut("key = " + w.getAuthor());
                //Utilities.printOut("key = " + w.getComment());
                //Utilities.printOut("key = " + w.getTimeSpent());

            } catch(org.codehaus.jackson.map.exc.UnrecognizedPropertyException ue){
                System.out.println(ue.getMessage());
            }
        }
        return ar;
    }


    public static JSONArray queryWorkLogsForJiraIssue (String anIssue) throws AuthenticationException, JSONException, IOException {
        ArrayList<JiraIssue> ar = new ArrayList<JiraIssue>();
        String res = InvokeHelper.invokeGetMethod( String.format(JiraConst.GET_WORKLOAD_URL, anIssue) );
        //LOGGER.debug(res);
        res = res.substring(res.indexOf("["), res.length()).trim();

        return new JSONArray(res);
    }

    public static JSONObject queryJiraUser (String username) throws AuthenticationException, JSONException, IOException {
        String res = InvokeHelper.invokeGetMethod( String.format( JiraConst.GET_USER_URL,username) );
        LOGGER.debug(res);
        return new JSONObject(res);
    }

    public static JSONObject queryJiraUserWithGroups (String username) throws AuthenticationException, JSONException, IOException {
        String res = InvokeHelper.invokeGetMethod(String.format( JiraConst.GET_USER_WITH_GROUPS_URL, username) );
        LOGGER.debug(res);
        return new JSONObject(res);
    }

    public static void updateJiraIssue(String issueKey) throws AuthenticationException {
        String editIssueData = "{\"fields\":{\"assignee\":{\"name\":\"test\"}}}";
        InvokeHelper.invokePutMethod( String.format( JiraConst.UPDATE_ISSUE_URL,issueKey), editIssueData);
    }

    public static void deleteJiraIssue( String issueKey) throws AuthenticationException, JSONException {
        InvokeHelper.invokeDeleteMethod( String.format( JiraConst.UPDATE_ISSUE_URL,issueKey) );
    }

    public static void deleteJiraWorklog(String issueKey, String worklogId) throws AuthenticationException, JSONException {
        InvokeHelper.invokeDeleteMethod( String.format(JiraConst.DELETE_WORKLOG_URL, issueKey, worklogId) );
    }

    public static void addJiraWorklog(String issueKey, String date, String author, String time, String comment){
        //String addWorklog = "{\"comment\":\"" + comment "\",\"started\":\"2013-04-30T10:30:18.932+0530\",\"timeSpent\":\"5h\"}";
        // invokePostMethod(auth, BASE_URL + "/rest/api/latest/issue/" + issueKey + "/worklog?adjustEstimate=AUTO&newEstimate&reduceBy" , worklog);
        //LOGGER.debug(addWorklog);
    }

    public static void createJiraIssue(String auth) throws AuthenticationException, JSONException {
        String createIssueData = "{\"fields\":{\"project\":{\"key\":\"DEMO\"},\"summary\":\"REST Test\",\"issuetype\":{\"name\":\"Bug\"}}}";
        String issue = InvokeHelper.invokePostMethod( String.format( JiraConst.UPDATE_ISSUE_URL), createIssueData);
        LOGGER.debug(issue);
        JSONObject issueObj = new JSONObject(issue);
        String newKey = issueObj.getString("key");
        LOGGER.debug("Key:"+newKey);
    }

    private static void getAllJiraProjects(String auth) throws AuthenticationException, JSONException {
        LOGGER.debug(">>> Get all Jira projects");
        String projects = InvokeHelper.invokeGetMethod( String.format( JiraConst.GET_PROJECTS_URL) );
        LOGGER.debug(projects);
        JSONArray projectArray = new JSONArray(projects);
        for (int i = 0; i < projectArray.length(); i++) {
            JSONObject proj = projectArray.getJSONObject(i);
            LOGGER.debug("Key:"+proj.getString("key")+", Name:"+proj.getString("name"));
        }
    }

    /**
     *
     * @param issueKey
     * @param comment
     * @param started
     * @param timespent
     * @return
     * @throws AuthenticationException
     */
    public static String addNewJiraWorklog(String issueKey, String comment, String started, String timespent) throws AuthenticationException {
        String newWorklogData = JsonHelper.generateNewJsonWorklog(comment, started, timespent);
        //LOGGER.debug("\tNew worklog " + newWorklogData);
        String createWorklogUrl = String.format (JiraConst.CREATE_WORKLOG_URL , issueKey);
        //LOGGER.debug("\tcreateWorklogUrl " + createWorklogUrl);
        return InvokeHelper.invokePostMethod(createWorklogUrl, newWorklogData);
    }


    /**
     * check existance of attribute: http://ismjira.ch.rccad.net/jira/rest/api/2/field
     * @param issueKey
     * @param newValue
     * @return
     */
    public static boolean setActivityId(String issueKey, String newValue) {
        String jiraAttributeName = "customfield_11610"; // activityId

        boolean success = false;
        String editIssueData = "{\"fields\":{\"customfield_11610\":\"" + newValue + "\"}}";
        try {
            success = InvokeHelper.invokePutMethod( String.format( JiraConst.UPDATE_ISSUE_URL, issueKey), editIssueData);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        return success;
    }


    public static List<String> updateActivityId(List<ProxyWindActivity> updatedActivitiesList){
        List<String> failedActivitiesList = new ArrayList<String>();
        String issueKey;
        String activityId;
        int status;
        for (ProxyWindActivity aProxyWindActivity :updatedActivitiesList ){
            issueKey = aProxyWindActivity.getActivityName();
            activityId = aProxyWindActivity.getNiceActivityUrl();
            status = aProxyWindActivity.getStatus();

            if (status >= 99){
                LOGGER.debug("failedActivitiesList.add(): " + issueKey );
                failedActivitiesList.add(issueKey);
            } else if (status == 1){
                LOGGER.debug("Need to update " + issueKey + " for activityId=" + activityId + " (Status=" + status + ")");
                boolean res = JiraHelper.setActivityId(issueKey, activityId);
                LOGGER.debug("Success: " + res);
            }

        }
        return failedActivitiesList;
    }


}