package com.richemont.windchill;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laurent.linck
 */
public class ProxyWindActivity implements Cloneable {

    public static final String CLASS = ProxyWindActivity.class.getName() ;
    public static final String WIND_CLASSNAME = "com.ptc.projectmanagement.plan.PlanActivity";

    public static final Integer STATUS_DEFAULT = 0;  // non-processed
    public static final Integer STATUS_CREATED = 1;
    public static final Integer STATUS_UPDATED = 2;
    public static final Integer STATUS_FAILED_SELF_CREATE = 101;
    public static final Integer STATUS_FAILED_PARENT_CREATE = 102;
    public static final Integer STATUS_FAILED_SELF_INTEND = 103;
    public static final Integer STATUS_FAILED_PARENT_INTEND = 104;

    //specific to Ehour side
    //otherwise, ARTIFICIAL_WORK_ADDITION will be added twice, eHour side, then PJL side
    public static final float ARTIFICIAL_WORK_ADDITION = new Float(0.1); // to be sure the PJL activity cannot be terminated automatically

    public String ActivityName = "" ;
    public String ActivityDescription = "" ;
    public String ActivityId = "" ;
    public String ActivityUrl = "" ;
    public String OrgName = "" ;
    public String OrgId = "" ;
    public String ProjectId = "" ;
    public String ProjectName = "" ;
    public String ProjectDescription = "" ;
    public String ProjectManager = "" ;
    public String startDate = "" ;
    public String endDate = "" ;
    private boolean isSummaryActivity = false;
    private int Status = 0;    // O=non processed
    public Float work = new Float(0) ;
    public Float performedWork = new Float(0) ;
    public List<ProxyWindActivity> Parents = new ArrayList<ProxyWindActivity>() ;
    //public JsonArray JsonParents = Json.createArrayBuilder().addNull().build();


    public ProxyWindActivity() {
    }

    public void setActivityName(String ActivityName) {
        this.ActivityName = ActivityName;
    }

    public String getActivityName() {
        return this.ActivityName;
    }

    public void setActivityDescription(String description) {
        this.ActivityDescription = description;
    }

    public String getActivityDescription() {
        return this.ActivityDescription;
    }

    public void setActivityId(String id) {
        this.ActivityId = id;
    }

    public String getActivityId() {
        return this.ActivityId;
    }

    public void setActivityUrl(String url) {
        this.ActivityUrl = url;
    }

    public String getActivityUrl() {
        return this.ActivityUrl;
    }

    // Nice display inside Jira
    public String getNiceActivityUrl(){
        if ( !"Not set".equals(this.ActivityUrl)) {
            return "[" + getActivityId() + "|" + this.ActivityUrl + "]";
        }else {
            return this.ActivityUrl;
        }
    }

    public void setOrgName(String orgName) {
        this.OrgName = orgName;
    }

    public String getOrgName() {
        return this.OrgName;
    }

    public void setOrgId(String orgId) {
        this.OrgId = OrgId;
    }

    public String getOrgId() {
        return this.OrgId;
    }

    public void setProjectId(String projectId) {
        this.ProjectId = projectId;
    }

    public String getProjectId() {
        return this.ProjectId;
    }

    public void setProjectName(String projectName) {
        this.ProjectName = projectName;
    }

    public String getProjectName() {
        return this.ProjectName;
    }

    public void setProjectDescription(String projectDescription) {
        this.ProjectDescription = projectDescription;
    }

    public String getProjectDescription() {
        return this.ProjectDescription;
    }

    public void setProjectManager(String projectManager) {
        this.ProjectManager = projectManager;
    }

    public String getProjectManager() {
        return this.ProjectManager;
    }

    public void setstartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getstartDate() {
        return this.startDate;
    }

    public void setendDate(String endDate) {
        this.endDate = endDate;
    }

    public String getendDate() {
        return this.endDate;
    }

    public void isSummaryActivity(boolean bool) {
        this.isSummaryActivity = bool;
    }

    public boolean isSummaryActivity() {
        return this.isSummaryActivity;
    }

    /**
     * specific to Ehour side
     * otherwise, ARTIFICIAL_WORK_ADDITION will be added twice
     * @param work
     */
    public void setwork(Float work) {
        if ( work % 1 - ARTIFICIAL_WORK_ADDITION >= 0  ){
            this.work = work ;
        } else {
            this.work = work + ARTIFICIAL_WORK_ADDITION ;
        }
    }

    public Float getwork() {
        return this.work;
    }

    public void setperformedWork(Float performedWork) {
        this.performedWork = performedWork;
    }

    public Float getperformedWork() {
        return this.performedWork;
    }

    public List<ProxyWindActivity> getParents(){
        return this.Parents;
    }

    public void setStatus( int status){
        this.Status = status ;
    }

    public int getStatus() {
        return this.Status;
    }


    /*
    public void setJsonParents(JsonArray jsonArray){
        this.JsonParents = jsonArray;
    }

    public JsonArray getJsonParents(){
        return this.JsonParents;
    }
    */

    public JsonObject toJsonObject(){
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("ActivityName", getActivityName() )
                .add("ActivityDescription", getActivityDescription() )
                .add("ActivityId", getActivityId() )
                .add("ActivityUrl", getActivityUrl() )
                .add("OrgName", getOrgName() )
                .add("OrgId", getOrgId() )
                .add("ProjectId", getProjectId() )
                .add("ProjectName", getProjectName() )
                .add("ProjectDescription", getProjectDescription() )
                .add("ProjectManager", getProjectManager() )
                .add("startDate", getstartDate() )
                .add("endDate", getendDate() )
                .add("Parents", toJsonArray(getParents()))
                .add("work", getwork())
                .add("performedWork", getperformedWork() )
                .add("status", getStatus() )
                .build();
        return jsonObject;
    }

    public JsonArray toJsonArray (List<ProxyWindActivity> parents){
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (ProxyWindActivity aProxyWindActivity : parents){
            jsonArrayBuilder.add( aProxyWindActivity.toJsonObject() );
        }
        return jsonArrayBuilder.build();
    }


    @Override
    public String toString() {
        return (new StringBuilder("")
                .append("\tproxyWindActivity Name='" + getActivityName() ).append("'").append("\n")
                .append("\tproxyWindActivity Description='" + getActivityDescription() ).append("'").append("\n")
                .append("\tproxyWindActivity Id='" + getActivityId() ).append("'").append("\n")
                .append("\tproxyWindActivity Url='" + getActivityUrl() ).append("'").append("\n")
                .append("\tproxyWindActivity OrgName='" + getOrgName() ).append("'").append("\n")
                .append("\tproxyWindActivity OrgId='" + getOrgId() )).append("'") .append("\n")
                .append("\tproxyWindActivity ProjectId='" + getProjectId()).append("'") .append("\n")
                .append("\tproxyWindActivity ProjectName='" + getProjectName()).append("'") .append("\n")
                .append("\tproxyWindActivity startDate='" + getstartDate()).append("'").append("\n")
                .append("\tproxyWindActivity endDate='" + getendDate()).append("'") .append("\n")
                .append("\tproxyWindActivity work='" + getwork()).append("'").append("\n")
                .append("\tproxyWindActivity performedWork='" + getperformedWork()).append("'").append("\n")
                .append("\tproxyWindActivity Status='" + getStatus() ).append("'").append("\n")
                .append("\tproxyWindActivity NbOfParents='" + getParents().size()).append("'").append("\n")
                .append("\t\tproxyWindActivity Parents=" + Parents).append("\n")
                .toString();
    }

    @Override
    public ProxyWindActivity clone() {
        try {
            return (ProxyWindActivity)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return  null;
    }

}
