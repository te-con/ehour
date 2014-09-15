package com.richemont.windchill;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.json.JsonArray;
import javax.xml.soap.SOAPMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author laurent.linck
 * @version 01/05/2012
 */
@Service
public class WindChillUpdateServiceImpl implements WindChillUpdateService {

    private static Logger LOGGER = Logger.getLogger("ext.service.WindchillServiceImpl");

    @Autowired
    private ReportAggregatedDao reportAggregatedDao;

    @Autowired
    private QueryTimeSheets queryTimeSheets;

    @Value("${ehour.windchill.enabled}")
    private String enabled;

    private static String soapEndpoint;
    private static String soapUsername;
    private static String soapPassword;

    @Value("${richemont.windchill.user}")
    private void setUsername(String username) {
        this.soapUsername = username;
    }

    @Value("${richemont.windchill.pwd}")
    private void setPassword(String pwd){
        this.soapPassword = pwd ;
    }

    @Value("${richemont.windchill.soap.endpoint}")
    private void setEndpoint(String ep) {
        this.soapEndpoint = ep;
    }


    public List<String> updateProjectLink(User user, List<Activity> activities) throws Exception {
        List<String> windFailedActivitieslist  = new ArrayList<String>();
        List<HashMap<String,Comparable>> updatedActivitiesList = null;
        updatedActivitiesList = updateProjectLink2(user, activities);
        return windFailedActivitieslist;
    }

    /**
     * Update PJL from Ehour
     * @param user
     * @param activities
     * @return
     * @throws Exception
     */
    public List<HashMap<String,Comparable>> updateProjectLink2(User user, List<Activity> activities) throws Exception {
        List< HashMap<String, Comparable> > updatedActivitiesList = null ;

        LOGGER.info("ehour.windchill.enabled=" + enabled);
        if (!isEnabled()) {
            LOGGER.info("Windchill sync is disabled");
        }

        List<String> listToSend = new ArrayList<String>();
        String spentHours;
        for (Activity activity : activities) {
            spentHours = getSpentHours(activity);
            LOGGER.debug("\t" + activity.getId() + "|" + activity.getCode() + "|" + activity.getName() + "|" + spentHours);
            listToSend.add( activity.getCode() + "~" + spentHours );
        }

        SOAPMessage aResponse = null;
        aResponse = queryTimeSheets.updateTimeSheets(user.getUsername(), listToSend, "updateTimeSheets", soapEndpoint);

        LOGGER.debug("\n");
        LOGGER.debug("aResponse: " + aResponse);

        LOGGER.debug("\n");
        LOGGER.debug("getSOAPBody(): " + aResponse.getSOAPBody() );

        updatedActivitiesList = SoapHelper.parseResponse(aResponse.getSOAPBody());
        return updatedActivitiesList;
    }


    public String getSpentHours( Activity activity ){
        String spentHours = "0.00";
        ActivityAggregateReportElement hoursForActivity = reportAggregatedDao.getCumulatedHoursForActivity(activity);
        try {
            spentHours = hoursForActivity.getHours().toString( );
        } catch (Exception e) {
            // la timesheet correspondante a ete detruite de ehour puisque le user a entre 0
            spentHours = "0.00";
        }
        return spentHours;
    }


    private boolean isEnabled() {
        return enabled == null || Boolean.parseBoolean(enabled);
    }


    /**
     *
     * @param ehourUserName
     * @param jSonAllJiraActivities
     * @param modifiedJiraActivities
     * @return
     * @throws Exception
     */
    public List<ProxyWindActivity> createMissingPjlActivities (User ehourUserName, JsonArray jSonAllJiraActivities, List<Activity> modifiedJiraActivities) throws Exception {
        return createMissingPjlActivities(ehourUserName, jSonAllJiraActivities, modifiedJiraActivities, SoapHelper.getEncodedAuth(soapUsername, soapPassword), soapEndpoint);
    }


    /**
     * @param ehourUserName : the user for the ehour session
     * @param jSonAllJiraActivities  : Json ProxyWindActivity : all the Jira activities from the eHour user session
     * @param modifiedJiraActivities : eHour Activity : the Jira activities the eHour user has just modified
     * @return resultActivitiesList : ProxyWindActivity : just modified Jira activities sync wih PJL
     * @throws Exception
     */
    public List<ProxyWindActivity> createMissingPjlActivities (User ehourUserName, JsonArray jSonAllJiraActivities, List<Activity> modifiedJiraActivities, String auth, String endpoint) throws Exception {
        List<ProxyWindActivity> resultActivitiesList = new ArrayList();

        ArrayList<ProxyWindActivity> allOriJiraActivitiesList = JsonHelper.convertJsonArrayToArray(jSonAllJiraActivities) ;
        ArrayList<ProxyWindActivity> updatedActivitiesList = removeUnModifiedJiraActivities(allOriJiraActivitiesList, modifiedJiraActivities);

        JsonArray jsonUpdatedActivitiesList = JsonHelper.convertArrayListToJsonArray(updatedActivitiesList);
        SOAPMessage aRequest = SoapHelper.createSOAPRequestForUpdateProjectPlan(WindchillConst.IE_TASK_DELEGATE_NAME_TIMESHEET_MGT, ehourUserName.getUsername(), "updateProjectPlan", jsonUpdatedActivitiesList, auth);

        LOGGER.debug("\n");
        LOGGER.debug("endpoint=" + endpoint );
        LOGGER.debug("Built soap message...");
        aRequest.writeTo(System.out);
        SOAPMessage aResponse = SoapHelper.requestWebService(aRequest, endpoint);

        LOGGER.debug("\n");
        LOGGER.debug("Receiving SOAP Response...");
        //aResponse.writeTo(System.out);
        LOGGER.debug("\n");

        if (aResponse != null) {

            final Iterator<?> iter = aResponse.getSOAPBody().getChildElements();

            // Retrieve the Json from the Windchill Soap response
            // attention au "=" dans la valeur de l'attribut !!
            String jsonArrayStr = SoapHelper.getAttributeValue(iter, "jsonArrayActivities=");
            if (jsonArrayStr != null) {
                resultActivitiesList = JsonHelper.convertJsonArrayStringToArrayList(jsonArrayStr) ;
            } else {
                LOGGER.debug("SOAP Response is empty : SoapHelper.getAttributeValue() returns null");
                return null;
            }
        } else {
            LOGGER.debug("SOAP Response is null");
            return null;
        }

        return resultActivitiesList ;
    }


    /**
     *
     * @param resultActivitiesList : list got from PJL
     * @param jSonAllJiraActivities : all jira activities in eHour session (before PJL update), must be updated
     * @return
     * @throws Exception
     */
    public JsonArray updateSessionParam ( List<ProxyWindActivity> resultActivitiesList, JsonArray jSonAllJiraActivities ) {

        LOGGER.debug("\n");
        LOGGER.debug("jSonAllJiraActivities before update:");
        LOGGER.debug(jSonAllJiraActivities);
        LOGGER.debug("\n");

        ArrayList<ProxyWindActivity> allOriJiraActivitiesList = JsonHelper.convertJsonArrayToArray(jSonAllJiraActivities) ;

        // in order to update the ehour user session activities
        // the goal is to update the parents of the activities that are not in PJL yet, in case several activities have the same parent
        HashMap<String, ProxyWindActivity> allActivitiesMap = new HashMap<String, ProxyWindActivity>();
        HashMap<String, ProxyWindActivity> allModifiedParentActivitiesMap = new HashMap<String, ProxyWindActivity>();

        for (ProxyWindActivity aProxyWindActivity :resultActivitiesList ){
            // the jira activities we've just modified
            allActivitiesMap.put(aProxyWindActivity.getActivityName(), aProxyWindActivity);
            allModifiedParentActivitiesMap.putAll(getAllParents(aProxyWindActivity));
        }

        for (ProxyWindActivity anOriInitActivity : allOriJiraActivitiesList) {
            String activityName = anOriInitActivity.getActivityName();
            if ( !allActivitiesMap.containsKey( activityName ) ) {

                // must update the parent in case they have just been updated
                if (anOriInitActivity.getParents().size() > 0){
                    int i = 0;
                    for ( ProxyWindActivity p: anOriInitActivity.getParents() ){
                        LOGGER.debug("\tThe activity " + activityName + " may contain one or more parents not up to date");
                        if ( allModifiedParentActivitiesMap.containsKey(p.getActivityName()) ){
                            LOGGER.debug("\t\tUpdate the parent " + p.getActivityName());
                            anOriInitActivity.getParents().set(i, p);
                        }
                        i++;
                    }
                }

                // complete with all the other orig jira activities
                allActivitiesMap.put(anOriInitActivity.getActivityName(), anOriInitActivity);
            }
        }

        return  JsonHelper.convertArrayListToJsonArray(  new ArrayList ( allActivitiesMap.values() ) );
    }



    public static HashMap<String, ProxyWindActivity> getAllParents(ProxyWindActivity proxyWindActivity){
        HashMap<String, ProxyWindActivity> hm = new HashMap<String, ProxyWindActivity>() ;
        for ( ProxyWindActivity p: proxyWindActivity.getParents() ){
            hm.put(p.getActivityName(), p);
        }
        return hm;
    }


    /**
     *
     * @param newJiraActivitiesList
     * @param modifiedJiraActivities
     * @return
     */
    public static ArrayList<ProxyWindActivity> removeUnModifiedJiraActivities( ArrayList<ProxyWindActivity> newJiraActivitiesList, List<Activity>  modifiedJiraActivities){
        ArrayList<ProxyWindActivity> filteredUpdatedActivitiesList = new ArrayList<ProxyWindActivity>() ;
        ProxyWindActivity aProxyWindActivity;
        boolean found;
        for (Activity aModifiedActivity : modifiedJiraActivities) {
            LOGGER.debug("removeUnModifiedJiraActivities() - check for activity " + aModifiedActivity.getCode());
            LOGGER.debug("\t\tNew performedHours: " +  aModifiedActivity.getAllottedHours() + "-" + aModifiedActivity.getAvailableHours() );
            //LOGGER.debug("Searching " + aModifiedActivity.getCode() + " in newJiraActivitiesList");
            int j=0;
            found = false;
            if ( !aModifiedActivity.getProject().getName().equals( WindchillConst.PJL_DEFAULT_PROJECT_NAME ) ) {
                //activity got an id project

                while (newJiraActivitiesList.size() > j && !found) {
                    aProxyWindActivity = newJiraActivitiesList.get(j);
                    //LOGGER.debug("\t\tcode=" + aModifiedActivity.getCode() + "    ?=?    name=" + aProxyWindActivity.getActivityName());
                    // code=jira:EVO-2391
                    // name=EVO-2391
                    if ( aModifiedActivity.getCode().contains(aProxyWindActivity.getActivityName()) ){
                        found = true;
                        aProxyWindActivity.setwork( aModifiedActivity.getAllottedHours() );
                        aProxyWindActivity.setperformedWork( aModifiedActivity.getAllottedHours()-aModifiedActivity.getAvailableHours() );

                        // activite ok mais son parent eventuel peut ne pas avoir de id project (or a different Id), dans ce cas, il ne faut garder l'activite, ni son parent
                        if (aProxyWindActivity.Parents.size() > 0){
                            LOGGER.debug( "\t" + aProxyWindActivity.getActivityName() + " is eligible BUT we need to check its parent(s)");
                            // The first parent should be The Top parent
                            ProxyWindActivity TopParentProxyWindActivity = aProxyWindActivity.getParents().get(0);
                            String TopParentProjectName = TopParentProxyWindActivity.getProjectName();

                            if ( TopParentProjectName.equals( WindchillConst.PJL_DEFAULT_PROJECT_NAME ) ){
                                // ignore the current ProxyWindActivity
                                LOGGER.debug( "\t\t" + TopParentProxyWindActivity.getActivityName() + " not eligible cause ProjectName not defined.");
                                LOGGER.debug( "\t\tThis " + aProxyWindActivity.getActivityName() + " cannot be eligible ! Ignoring ...");
                            } else {
                                filteredUpdatedActivitiesList.add(aProxyWindActivity);
                                LOGGER.debug("\tAdd tofilteredUpdatedActivitiesList (The parent(s) is OK)");
                            }

                        } else {
                            filteredUpdatedActivitiesList.add(aProxyWindActivity);
                            LOGGER.debug("\tAdd tofilteredUpdatedActivitiesList (no existing parent)");
                        }
                    }
                    j++;
                }
            } else {
                LOGGER.debug("\tIgnore activity because stored in the default project " + WindchillConst.PJL_DEFAULT_PROJECT_NAME );
            }
        }
        return filteredUpdatedActivitiesList;
    }


}
