import com.richemont.jira.*;
import com.richemont.windchill.DateUtils;
import com.sun.jersey.api.client.ClientHandlerException;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.json.JSONException;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * @author laurent.linck
 */
public class JiraMain_TestUnits {

    private static Logger LOGGER = Logger.getLogger("com.richemont.jira");

    public static void main(String[] args) throws JSONException, AuthenticationException, IOException {

        int num =5;

        switch (num) {
            case 1 :
                test1(); // update Jira ActivityId
                break;
            case 2 :
                test2(); // set Jira ActivityId
                break;
            case 3 :
                test3(); // Date Conversion
                break;
            case 4 :
                test4(); // Query Jira issue
                break;
            case 5 :
                test5(); // Query worklogs
                break;
        }


    }


    /**
     * update activity id of an issue
     */
    public static void test1(){
        String issueKey = "EVO-2391";
        String attributeValue = "xxxx";
        boolean res = JiraHelper.setActivityId(issueKey, attributeValue);
        System.out.println("Success: " + res);
    }


    /**
     *  testFormatProjectId
     */
    public static void test2(){
        String id="";
        String pjlProjectName="";
        String pjlOrgName="";
        String pjlProjectId = "richemont~~TEST-LLI-EHOUR-01~~027295~~wt.projmgmt.admin.Project2:6590512736";
        String[] str;
        if (pjlProjectId.startsWith("wt.projmgmt.admin.Project2:")) id = pjlProjectId;
        else if (pjlProjectId != null){
                str = pjlProjectId.split(JiraConst.PJL_ATTR_SEPARATOR);
            System.out.println(""+ str.length);
                if (str.length == 4) {
                    pjlOrgName= str[0];
                    pjlProjectName= str[1];
                    id = str[3];
                }else {
                    System.out.println("pjlProjectId : bad formatted !");
                }
        }else {
            System.out.println ("pjlProjectId: null");
        }

        System.out.println("id=" + id);
        System.out.println("pjlOrgName=" + pjlOrgName);
        System.out.println("pjlProjectName=" + pjlProjectName);
    }

    /**
     *  testDateConversion
     */
    public static void test3() throws AuthenticationException {
        Number hoursForActivity=1.0;
        Date ehourDateEnd = new Date(); // 2013-05-07T21:59:59.999Z // reportAggregatedDao.getMinMaxDateTimesheetEntry().getDateEnd()
        String username = "laurent.linck";  // user.getUsername()
        String issueKey = "EVO-2407";

        String newSpentHours = String.format( Locale.FRENCH, "%.2f", hoursForActivity ) + "h";     // unit = hours
        String newStarted = Utilities.formatToWorklogJiraDate(ehourDateEnd); // 2013-05-07T21:59:59.999Z
        String newComment = "Add cumulated " + newSpentHours + " from eHour for " + username ;
        String newCommentWithDate = newComment + " at " + DateUtils.convertDateToString( new Date(), new SimpleDateFormat("dd/MM/yyyy hh:mm:ss") );

        String createWorklogUrl = String.format (JiraConst.CREATE_WORKLOG_URL , issueKey);
        String newWorklogData = JsonHelper.generateNewJsonWorklog(newCommentWithDate, newStarted, newSpentHours);

        System.out.println("ehourDateEnd=" + ehourDateEnd);
        System.out.println("newStarted=" + newStarted);
        System.out.println("newCommentWithDate=" + newCommentWithDate);
        System.out.println("createWorklogUrl=" + createWorklogUrl);
        System.out.println("newWorklogData=" + newWorklogData);

        String res = JiraHelper.addNewJiraWorklog(issueKey, newCommentWithDate, newStarted, newSpentHours);
        System.out.println(res);
    }


    /**
     *   test Query Jira issue
     */
    public static void test4(){
        try {
            //JiraHelper.queryJiraUser("laurent.linck");

            String assigneeUserName = "laurent.linck";
            JiraHelper.getAllJiraIssuesForUser(assigneeUserName );


            //String issue = "EVO-72";
            //JiraHelper.getWorkLogsForJiraIssue(issue);

        } catch (AuthenticationException e) {
            System.out.println("Username or Password wrong!");
            e.printStackTrace();
        } catch (ClientHandlerException e) {
            System.out.println("Error invoking REST method");
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("Invalid JSON output");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    /**
     * Query worklogs
     * @throws IOException
     * @throws JSONException
     * @throws AuthenticationException
     */
    public static void test5() throws IOException, JSONException, AuthenticationException {
        String issueKey = "EVO-2384";
        ArrayList<JiraWorklog> jw = JiraHelper.getWorkLogsForJiraIssue(issueKey);
        System.out.println("Get worklogs for " +  issueKey)  ;
        // /rest/api/2/issue/EVO-2384/worklog?adjustEstimate=auto

        int i=0;
        for (JiraWorklog aWorklog :jw){
            i++;
            System.out.println("--- workload #" + i + " ---");
            System.out.println("\t" + aWorklog.toString());

            System.out.println("\taWorkload.getAuthor()=" + aWorklog.getAuthor().getName());   //admin_jira

            DateTime startedWorkLog = aWorklog.getStarted();
            System.out.println("\taWorklog.getStarted()=" + startedWorkLog);
            System.out.println("\taWorklog.getStarted().getDayOfYear()=" + startedWorkLog.getDayOfYear()); //309
        }
    }





}
