package com.richemont.jira;

import com.richemont.windchill.DateUtils;
import com.richemont.windchill.ProxyWindActivity;
import net.rrm.ehour.domain.Activity;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import javax.json.*;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * @author laurent.linck
 *
 * use Maven Artefact "json-lib" de "net.sf.json-lib"
 * http://json-lib.sourceforge.net/
 * http://answers.oreilly.com/topic/263-how-to-generate-json-from-java/
 */
public class JsonHelper {

    private static Logger logger = Logger.getLogger(JsonHelper.class.getName());
    private static boolean DEBUG = JiraConst.DEBUG;

    public static void main(String[] args) throws Exception {
        String newWorklog = generateNewJsonWorklog("Adding Worklog", "2013-04-30T10:30:18.932+0530", "5h");
        System.out.println(newWorklog);
    }

    public static String generateNewJsonWorklog (String comment, String date, String timeSpent) {
        JSONObject json = new JSONObject();

        json.put( "comment", comment);
        json.put( "started", date);
        json.put( "timeSpent", timeSpent);

        return json.toString(1);
    }

    /**
     *
     * @param jsonString
     * @return
     */
    public static ArrayList<ProxyWindActivity> convertJsonArrayStringToArrayList(String jsonString){
        ArrayList<ProxyWindActivity> list = new ArrayList<ProxyWindActivity>();
        StringReader sr = new StringReader( jsonString );

        log("===> String to be converted to Json:");
        log(jsonString);

        JsonReader jsonReader = Json.createReader(sr);
        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close() ;

        int j = 1;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonActivity = jsonArray.getJsonObject(i);
            j = i+1;
            log("====>> convert String " + j + "/" + jsonArray.size() + " to Json:");
            log("" + jsonActivity);
            try {
                ProxyWindActivity proxyWindActivity = mapper.readValue(jsonActivity.toString() , ProxyWindActivity.class);
                list.add(proxyWindActivity);

                //mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ProxyWindctivity);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     *
     * @param issue
     * @param activity
     * @return
     */
    public static JsonObject createJsonObjectForProxyWindActivity (JiraIssue issue, Activity activity){
        ProxyWindActivity proxyWindActivity = newWindActivity(issue, activity );

        log("--------------------------------------------------------");
        log("" + proxyWindActivity.toString() );
        log("--------------------------------------------------------");
        return proxyWindActivity.toJsonObject();
    }


    /**
     *  Build the ProxyWindActivity from JiraIssue and eHour Activity
     *
     * @param issue
     * @param activity
     * @return
     */
    public static ProxyWindActivity newWindActivity(JiraIssue issue, Activity activity){
        log("Build the ProxyWindActivity from JiraIssue " + issue.getKey());

        ProxyWindActivity proxyWindActivity = new ProxyWindActivity();
        ProxyWindActivity proxyWindParentActivity = new ProxyWindActivity();

        proxyWindActivity.setOrgId( issue.getFields().getProjectLinkAttributes().getPjlOrgId() );
        proxyWindActivity.setOrgName(issue.getFields().getProjectLinkAttributes().getPjlOrgName());
        proxyWindActivity.setProjectId(issue.getFields().getProjectLinkAttributes().getPjlProjectId());
        proxyWindActivity.setProjectName(issue.getFields().getProjectLinkAttributes().getPjlProjectName());
        proxyWindActivity.setProjectDescription("");
        proxyWindActivity.setProjectManager("");

        String activityId = issue.getFields().getProjectLinkAttributes().getPjlActivityId();
        if (activityId != null && activityId.trim().length() >0){
            if ( activityId.indexOf("http") >-1 ){
                log ("\tWe get an Activity URL");
                    log ("\tActivity URL: " + activityId);
                    proxyWindActivity.setActivityUrl( activityId );
                    String[] id = activityId.split("%3A");
                    if (id.length>0) {
                        proxyWindActivity.setActivityId ( id[1].substring(0, id[1].length()-2) );
                        log ("\tActivity ID: " + id[1].substring(0, id[1].length()-2) );
                    } else {
                        log ("\tActivity ID: ???");
                    }
            } else {
                proxyWindActivity.setActivityId( issue.getFields().getProjectLinkAttributes().getPjlActivityId() );
                proxyWindActivity.setActivityUrl( activityId );
            }
        }

        proxyWindActivity.setActivityName(issue.getKey() );
        proxyWindActivity.setActivityDescription(issue.getFields().getSummary());

        proxyWindActivity.setstartDate(DateUtils.convertDateToString(activity.getDateStart(), JiraConst.JIRA_DATE_FORMAT));   // for parent : todays
        proxyWindActivity.setendDate( DateUtils.convertDateToString(activity.getDateEnd(), JiraConst.JIRA_DATE_FORMAT) );     // for parent: end of fiscal year
        proxyWindActivity.setwork( activity.getAllottedHours() );  // Float: Remaining Work = projectAllocatedHours     // for parent: not set ???
        proxyWindActivity.isSummaryActivity(false);

        /*
        if (activity == null){ //we are working on a present not present in eHour
            // TO DO LLI
            proxyWindActivity.setstartDate(DateUtils.convertDateToString(activity.getDateStart(), JiraConst.JIRA_DATE_FORMAT));   // will be set by PJL from children
            proxyWindActivity.setendDate( DateUtils.convertDateToString(activity.getDateEnd(), JiraConst.JIRA_DATE_FORMAT) );     // will be set by PJL from children
            proxyWindActivity.setwork( activity.getAllottedHours() );                                                             // will be set by PJL from children
            proxyWindActivity.isSummaryActivity(true);
        } else {
            proxyWindActivity.setstartDate(DateUtils.convertDateToString(activity.getDateStart(), JiraConst.JIRA_DATE_FORMAT));   // for parent : todays
            proxyWindActivity.setendDate( DateUtils.convertDateToString(activity.getDateEnd(), JiraConst.JIRA_DATE_FORMAT) );     // for parent: end of fiscal year
            proxyWindActivity.setwork( activity.getAllottedHours() );  // Float: Remaining Work = projectAllocatedHours     // for parent: not set ???
            proxyWindActivity.isSummaryActivity(false);
        }

*/
        log("\t\tParents.size() before: " + proxyWindActivity.Parents.size() );
        proxyWindActivity = buildParentsReference(proxyWindActivity, issue, activity);
        log("\t\tParents.size() after: " + proxyWindActivity.Parents.size() );

        return proxyWindActivity;
    }


    /**
     * recursive function for parents navigation of a JiraIssue
     *
     * @param issue
     * @param activity
     * @return
     */
    public static ProxyWindActivity buildParentsReference ( ProxyWindActivity proxyWindActivity, JiraIssue issue, Activity activity ){
        log ("\tSet the parents for proxyWindActivity " + proxyWindActivity.getActivityName());
        log("\t\tissue.getJiraObjPath()=" + issue.getJiraObjPath() );


        ProxyWindActivity proxyWindParentActivity = null;
        ProxyWindActivity previousProxyWindParentActivity = null;
        if (issue.getJiraObjPath() != null && issue.getJiraObjPath().size() > 0){
            log("\t\tbuildParentsReferences(): ");
            int i =0;
            for (JiraIssue parentIssue : issue.getJiraObjPath() ) {
                // le n est le parent du n+1 Par ex: // le premier (epic) et le parent du 2ème (Story)
                i++;
                System.out.println("loop " + i);
                proxyWindParentActivity = newWindActivity(parentIssue, activity);
                proxyWindParentActivity.isSummaryActivity(true);

                if (previousProxyWindParentActivity != null){
                    proxyWindParentActivity.Parents.add(previousProxyWindParentActivity);
                }
                previousProxyWindParentActivity = proxyWindParentActivity;
                proxyWindActivity.Parents.add(proxyWindParentActivity);
                log("\t\tParent " + proxyWindParentActivity.getActivityName() + " added to " + proxyWindActivity.getActivityName());

                /**
                proxyWindParentActivity = buildParentsReference(proxyWindParentActivity, parentIssue, activity);
                if ( proxyWindParentActivity != null){
                    proxyWindActivity.Parents.add( proxyWindParentActivity );
                }
                **/
            }
        } else {
            log("\t\tNo more parent.") ;
        }
        return  proxyWindActivity;
    }


    public static String convertJsonArrayToString( JsonArray jsonArray){
        StringWriter sw = new StringWriter();
        JsonWriter jw = Json.createWriter(sw);
        jw.writeArray(jsonArray);
        jw.close();
        return sw.toString();
    }


    public static void log(String aMessage) {
        if (DEBUG) System.out.println( aMessage);
        else logger.debug(aMessage);
    }
}

