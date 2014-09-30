package com.richemont.jira;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.json.JSONException;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * @author laurent.linck
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue {
    //{"id":"10007","expand":"editmeta,renderedFields,transitions,changelog,operations","self":"http://localhost:8081/rest/api/latest/issue/10007","key":"MPT-2","fields":{"progress":{"total":432000,"progress":0,"percent":0},"summary":"Bug01","issuetype":{"subtask":false,"id":"1","description":"A problem which impairs or prevents the functions of the product.","name":"Bug","iconUrl":"http://localhost:8081/images/icons/bug.gif","self":"http://localhost:8081/rest/api/2/issuetype/1"},"votes":{"hasVoted":false,"votes":0,"self":"http://localhost:8081/rest/api/2/issue/MPT-2/votes"},"fixVersions":[],"resolution":null,"resolutiondate":null,"timespent":null,"reporter":{"name":"laurent.linck","active":true,"emailAddress":"laurent.linck@richemont.com","self":"http://localhost:8081/rest/api/2/user?username=laurent.linck","displayName":"Laurent Linck","avatarUrls":{"16x16":"http://localhost:8081/secure/useravatar?size=small&avatarId=10122","48x48":"http://localhost:8081/secure/useravatar?avatarId=10122"}},"aggregatetimeoriginalestimate":432000,"created":"2013-04-22T06:48:06.000+0200","updated":"2013-04-22T06:48:06.000+0200","description":null,"priority":{"id":"3","name":"Major","iconUrl":"http://localhost:8081/images/icons/priority_major.gif","self":"http://localhost:8081/rest/api/2/priority/3"},"duedate":null,"issuelinks":[],"watches":{"watchCount":1,"isWatching":true,"self":"http://localhost:8081/rest/api/2/issue/MPT-2/watchers"},"subtasks":[],"status":{"id":"1","description":"The issue is open and ready for the assignee to start work on it.","name":"Open","iconUrl":"http://localhost:8081/images/icons/status_open.gif","self":"http://localhost:8081/rest/api/2/status/1"},"labels":[],"assignee":{"name":"laurent.linck","active":true,"emailAddress":"laurent.linck@richemont.com","self":"http://localhost:8081/rest/api/2/user?username=laurent.linck","displayName":"Laurent Linck","avatarUrls":{"16x16":"http://localhost:8081/secure/useravatar?size=small&avatarId=10122","48x48":"http://localhost:8081/secure/useravatar?avatarId=10122"}},"workratio":0,"aggregatetimeestimate":432000,"project":{"id":"10001","name":"MON PROJET TEST","self":"http://localhost:8081/rest/api/2/project/MPT","avatarUrls":{"16x16":"http://localhost:8081/secure/projectavatar?size=small&pid=10001&avatarId=10011","48x48":"http://localhost:8081/secure/projectavatar?pid=10001&avatarId=10011"},"key":"MPT"},"versions":[],"environment":null,"timeestimate":432000,"lastViewed":"2013-04-22T06:48:06.522+0200","aggregateprogress":{"total":432000,"progress":0,"percent":0},"components":[],"timeoriginalestimate":432000,"aggregatetimespent":null}}

    private final static String pjlDefaultOrgName = JiraConst.PJL_DEFAULT_ORG_NAME;
    private final static String pjlDefaultOrgId = JiraConst.PJL_DEFAULT_ORG_ID;
    private final static String pjlDefaultProjectName = JiraConst.PJL_DEFAULT_PROJECT_NAME;
    private final static String pjlDefaultProjectId = JiraConst.PJL_DEFAULT_PROJECT_ID;
    private final static String pjlDefaultActivityId= "Not set";

    private String id;
    private String key;
    private String expand;
    private String self;
    private String jiraPath;
    private ArrayList<JiraIssue> jiraObjPath;
    private JiraFields fields;
    private ArrayList<JiraWorklog> jiraWorklog ;
    //private EHourMapping eHourMapping;

    public JiraIssue() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setKey(String key) {
        this.key = key;
        setWorklogs(key);
    }

    public String getKey() {
        return key;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public String getExpand() {
        return expand;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getSelf() {
        return self;
    }

    /**
     *
     * @param path
     * index1 = Epic if exist
     * index0 = Story if exist
     */
    public void setJiraPath(String path) {
        this.jiraPath = path;
    }

    public String getJiraPath() {
        return jiraPath;
    }

    public void setJiraObjPath(ArrayList<JiraIssue> objPath) {
        this.jiraObjPath = objPath;
    }

    public ArrayList<JiraIssue> getJiraObjPath() {
        return jiraObjPath;
    }

    public void setFields(JiraFields fields) {
        this.fields = fields;
    }

    public JiraFields getFields() {
        return fields;
    }

    public void setWorklogs(String key){
        try {
            this.jiraWorklog = JiraHelper.getWorkLogsForJiraIssue(key);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }


    public void removeAllWorklogs(){
        for (JiraWorklog jw: getWorklogs() ){
                removeWorklog(jw);
        }
    }

    public void removeAllWorklogsForUser(String author){
        ArrayList<JiraWorklog> allWorklogs = getWorklogs();
        if ( allWorklogs.size()> 0 ) {
            Iterator<JiraWorklog> iter = allWorklogs.iterator();
            while(iter.hasNext()){
                JiraWorklog w = iter.next();
                    if ( author.equals(w.getAuthor().getName()) ) {
                        if (removeWorklog(w) ) iter.remove();
                    }
            }
        }
    }

    public boolean removeWorklog(JiraWorklog jw){
        boolean res = false;
        try {
            JiraHelper.deleteJiraWorklog(this.key, jw.getId());
            System.out.println(jw.getId() + " removed");
            res = true;
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }


    public ArrayList<JiraWorklog> getWorklogs(){
            return this.jiraWorklog ;
    }

    public String setShortKey(String Key){
        if ( key.startsWith("")) return "";
        else return key;
    }


    @Override
    public String toString() {
        return (new StringBuilder("JiraIssue{")
                .append("id='" + id).append("',")
                .append("key='" + key).append("',")
                .append("expand='" + expand).append("',")
                .append("self='" + self).append("',")
                .append("fields='" + fields.toString())
                .append("}")).toString();
    }

    @JsonIgnoreProperties({
            "worklog",
            "comment",
            "timetracking",
            "originalEstimate",
            "attachment",
            "customfield_10000",
            "customfield_10001",
            "customfield_10002",
            "customfield_10003",
            "customfield_10004",
            "customfield_10005",
            "customfield_10006",
            "customfield_10010",
            "customfield_10110",
            "customfield_10111",
            "customfield_10210",
            "customfield_10212",
            "customfield_10310",
            "customfield_10311",
            "customfield_10312",
            "customfield_10313",
            "customfield_10411",
            "customfield_10510",
            "customfield_10610",
            "customfield_10611",
            "customfield_10612",
            "customfield_10710",
            "customfield_10711",
            "customfield_10810",
            "customfield_10811",
            "customfield_10812",
            "customfield_10813",
            "customfield_10814",
            "customfield_10815",
            "customfield_10910",
            "customfield_10911",
            "customfield_10912",
            "customfield_11211",
            "customfield_11212",  // PTC
            "customfield_10315",
            "customfield_10314",
            "customfield_10316",
            "customfield_11010",
            "customfield_11011",
            "customfield_11012",
            "customfield_11013",
            "customfield_11014",
            "customfield_11015",
            "customfield_11110",
            "customfield_11111",
            "customfield_11112",
            "customfield_11211",  //Epic Name
            "customfield_11212",  //Epic Status
            "customfield_11213",
            "customfield_11310",
            "customfield_11410",
            "customfield_11911",  // DEV
            "customfield_11912",  // marque
            "customfield_11913",  // MCL
            "customfield_12010",  // PJL sync status
            "customfield_12110",
            "customfield_12210",
            "customfield_12310",
            "customfield_12311",
            "customfield_12312",
            "customfield_12410",
            "customfield_12510",
            "customfield_12610",
            "customfield_12710",
            "customfield_12810",
            "customfield_12910",
            "customfield_13010",
            "customfield_13110",
            "customfield_13210",
            "customfield_13310",
            "customfield_13410",
            "customfield_13510",
            "customfield_13610",
            "customfield_13710",
            "customfield_13810",
            "customfield_13910",
            "customfield_14010",
            "customfield_14110",
            "customfield_14210",
            "customfield_14310",
            "customfield_14410",
            "customfield_14510",
            "customfield_14610",
            "customfield_14710",
            "customfield_14810",
            "customfield_14910",
            "customfield_15010",
            "customfield_15110",
            "customfield_15210",
            "customfield_15310",
            "customfield_15410",
            "customfield_15510",
            "customfield_15511", // Installation guide
            "customfield_15610",
            "customfield_15710",
            "customfield_15810",
            "customfield_15910",
            "customfield_16010",
            "customfield_16110",
            "customfield_16210",
            "customfield_16310",
            "customfield_16410",
            "customfield_16510",
            "customfield_16610",
            "customfield_16710",
            "customfield_16810",
            "customfield_16910",
            "customfield_17010",
            "customfield_17110"
            })

    public static class JiraFields{
        private JiraIssueType issuetype;
        private JiraProgress progress;
        private JiraCreator creator;
        private JiraTimeTracking timetracking;
        private String summary;
        private JiraVote votes;
        private JiraResolution resolution;
        private String[] fixVersions;
        private Date resolutiondate;
        private Long timespent;
        private JiraReporter reporter;
        private String aggregatetimeoriginalestimate;
        private Date updated;
        private Date created;
        private String description;
        private JiraPriority priority;
        private Date duedate;
        private String[] issuelinks;
        private JiraWatches watches;
        private String[] subtasks;
        private JiraStatus status;
        private String[]labels;
        private JiraAssignee assignee;
        private String workratio;
        private Long aggregatetimeestimate;
        private String[] versions;
        private String environment;
        private Long remainingTimeestimate;
        private JiraAggregateprogress aggregateprogress;
        private Date lastViewed;
        private String[] components;
        private Long timeoriginalestimate;
        private Long aggregatetimespent;
        private JiraProject project;
        //private JiraParent parent;
        private JiraIssue parent;

        private boolean hasParent = false;

        private String customfield_11510; // ProjectLink IDA2A2 project Ex: Richemont~wt.projmgmt.admin.Project2:0123456789~TimeSheet & Ticketing BUXX
        private String customfield_11610; // ProjectLink IDA2A2 activity Ex: com.ptc.projectmanagement.assignment.ResourceAssignment:0123456789
        private JiraEHour customfield_11710; // to be sent to eHour  Yes/No
        private JiraWarm customfield_11810;  // yes no Obsolète
        private JiraPJL customfield_11811; // yes no Obsolète
        private String customfield_11210; // EPIC

        private ProjectLinkAttributes pjlAttributes = new  ProjectLinkAttributes();

        public void setCreator(JiraCreator creator) {
            this.creator = creator;
        }

        public JiraCreator getCreator() {
            return creator;
        }

        public void setIssuetype(JiraIssueType issuetype) {
            this.issuetype = issuetype;
        }

        public JiraIssueType getIssueType() {
            return issuetype;
        }

        public void setProgress(JiraProgress progress) {
            this.progress = progress;
        }

        public JiraProgress getProgress() {
            return progress;
        }

        public void setTimetracking(JiraTimeTracking timetracking) {
            this.timetracking = timetracking;
        }

        public JiraTimeTracking getTimetracking() {
            return timetracking;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getSummary() {
            return summary;
        }

        public void setVotes(JiraVote votes) {
            this.votes = votes;
        }

        public JiraVote getVotes() {
            return votes;
        }

        public void setResolution(JiraResolution Resolution) {
            this.resolution = resolution;
        }

        public JiraResolution getResolution() {
            return resolution;
        }

        public void setFixVersions(String[] fixVersions) {
            this.fixVersions = fixVersions;
        }

        public String[] getFixVersions() {
            return fixVersions;
        }

        public void setResolutiondate(Date resolutiondate) {
            this.resolutiondate = resolutiondate;
        }

        public Date getResolutiondate() {
            return resolutiondate;
        }

        // This is the "total time spent" working on this issue, in hours.
        public void setTimespent(long timespent) {
            this.timespent = timespent / 3600 ;
        }

        public Long getTimespent() {
            return timespent;
        }

        public void setReporter(JiraReporter reporter) {
            this.reporter = reporter;
        }

        public JiraReporter getReporter() {
            return reporter;
        }

        public void setAggregatetimeoriginalestimate(String aggregatetimeoriginalestimate) {
            this.aggregatetimeoriginalestimate = aggregatetimeoriginalestimate;
        }

        public String getAggregatetimeoriginalestimate() {
            return aggregatetimeoriginalestimate;
        }

        public void setUpdated(Date updated) {
            this.updated = updated;
        }

        public Date getUpdated() {
            return updated;
        }

        public void setCreated(Date created) {
            this.created = created;
        }

        public Date getCreated() {
            return created;
        }

        public String getCreatedStr(SimpleDateFormat simpleDateFormat){
            if (simpleDateFormat == null )simpleDateFormat =  new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");  //17-10-12 06:00:00
            return simpleDateFormat.format(this.created);
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setPriority(JiraPriority priority) {
            this.priority = priority;
        }

        public JiraPriority getPriority() {
            return priority;
        }

        public void setDuedate(Date duedate) {
            this.duedate = duedate;
        }

        public Date getDuedate() {
            return duedate;
        }

        public void setIssuelinks(String [] issuelinks) {
            this.issuelinks = issuelinks;
        }

        public String [] getIssuelinks() {
            return issuelinks;
        }

        public void setWatches(JiraWatches watches) {
            this.watches = watches;
        }

        public JiraWatches getWatches () {
            return watches;
        }

        public void setSubtasks(String[] subtasks) {
            this.subtasks = subtasks;
        }

        public String[] getSubtasks () {
            return subtasks;
        }

        public void setStatus(JiraStatus status) {
            this.status = status;
        }

        public JiraStatus getStatus () {
            return status;
        }

        public void setLabels(String [] labels) {
            this.labels = labels;
        }

        public String [] getLabels() {
            return labels;
        }

        public void setAssignee(JiraAssignee assignee) {
            this.assignee = assignee;
        }

        public JiraAssignee getAssignee() {
            return assignee;
        }

        // workRatio = timeSpent / originalEstimate) x 100
        public void setWorkratio(String workratio) {
            this.workratio = workratio;
        }

        public String getWorkratio() {
            return workratio;
        }

        public void setAggregatetimeestimate(Long aggregatetimeestimate) {
            this.aggregatetimeestimate = aggregatetimeestimate;
        }

        public Long getAggregatetimeestimate() {
            return aggregatetimeestimate;
        }

        public void setVersions(String[] versions) {
            this.versions = versions;
        }

        public String[] getversions() {
            return versions;
        }

        public void setEnvironment(String environment) {
            this.environment = environment;
        }

        public String getEnvironment() {
            return environment;
        }

        // Remaining time estimate in nb of hours
        public void setTimeestimate(long timeestimate) {
            this.remainingTimeestimate = timeestimate / 3600;
        }

        public Long getRemainingTimeestimate() {
            return remainingTimeestimate;
        }

        public void setAggregateprogress(JiraAggregateprogress aggregateprogress) {
            this.aggregateprogress = aggregateprogress;
        }

        public JiraAggregateprogress getAggregateprogress() {
            return aggregateprogress;
        }

        public void setLastViewed(Date lastViewed) {
            this.lastViewed = lastViewed;
        }

        public Date getLastViewed() {
            return lastViewed;
        }

        // Creo, Custo, Windchill
        public void setComponents(String[] components) {
            this.components = components;
        }

        public String[] getComponents() {
            return components;
        }

        public void setTimeoriginalestimate(long timeoriginalestimate) {
            this.timeoriginalestimate = timeoriginalestimate / 3600;
        }

        public Long getTimeoriginalestimate() {
            return timeoriginalestimate;
        }

        public void setAggregatetimespent(Long aggregatetimespent) {
            this.aggregatetimespent = aggregatetimespent;
        }

        public Long getAggregatetimespent() {
            return aggregatetimespent;
        }

        public void setProject(JiraProject project) {
            this.project = project;
        }

        public JiraProject getProject() {
            return project;
        }

       public void setParent(JiraIssue parent) {
           //this.parent = parent;
           // need to query again cause default information provided here does not contain any attributes
           try {
               this.parent = JiraHelper.getJiraIssue(parent.getKey());
           } catch (AuthenticationException e) {
               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
           } catch (JSONException e) {
               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
           } catch (IOException e) {
               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
           }
           this.hasParent = true;

       }

       public JiraIssue getParent() {
           return parent;
       }

       public boolean hasParent(){
            return hasParent;
        }

        // EPIC Link (name)
       private void setCustomfield_11210(String customfield_11210) {
            this.customfield_11210 = customfield_11210;
            if (customfield_11210 != null){
                try {
                    this.parent = JiraHelper.getJiraIssue(customfield_11210);
                } catch (AuthenticationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (JSONException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                this.hasParent = true;
            }
        }

        public String getEpicName() {
            return customfield_11210;
        }

        // eHour attribute
        public void setCustomfield_11710(JiraEHour customfield_11710) {
            this.customfield_11710 = customfield_11710;
        }

        public JiraEHour getEHour() {
            if  (customfield_11710 != null) return customfield_11710;
            else return new JiraEHour();
        }

        public void setCustomfield_11810(JiraWarm customfield_11810) {
            this.customfield_11810 = customfield_11810;
        }

        public JiraWarm getWarm() {
            if  (customfield_11810 != null) return customfield_11810;
            else return new JiraWarm();
        }

        public void setCustomfield_11811(JiraPJL customfield_11811) {
            this.customfield_11811 = customfield_11811;
        }

        public JiraPJL getPJL() {
            if  (customfield_11811 != null) return customfield_11811;
            else return new JiraPJL();
        }

        // ProjectLink IDA2A2 activity
        public void setCustomfield_11610(String customfield_11610) {
            if (customfield_11610 != null) this.pjlAttributes.setPjlActivityId(customfield_11610);
            this.customfield_11610 = customfield_11610;
        }

        // ProjectLink IDA2A2 project
        //Richemont~~4530~~TEST-LLI-EHOUR-01~~027295~~wt.projmgmt.admin.Project2:6590512736
        public void setCustomfield_11510(String customfield_11510) {
            this.customfield_11510 = customfield_11510;
            if (customfield_11510 != null) {
                if (!customfield_11510.contains("null")) { // null~~4530~~null~~null~~wt.projmgmt.admin.Project2:6590790224
                    this.pjlAttributes.setPjlProjectId(customfield_11510);
                    String[] prjProps = customfield_11510.split(JiraConst.PJL_ATTR_SEPARATOR);
                    if (prjProps.length == 5 ){
                        this.getProjectLinkAttributes().setPjlOrgName( prjProps[0] );
                        this.getProjectLinkAttributes().setPjlOrgId(prjProps[1]);
                        this.getProjectLinkAttributes().setPjlProjectName( prjProps[2] );
                        this.getProjectLinkAttributes().setPjlProjectId( prjProps[4] );
                    }

                }
            }
        }

        public ProjectLinkAttributes getProjectLinkAttributes() {
            return this.pjlAttributes;
        }


        @Override
        public String toString() {
                   /*
            return (new StringBuilder("JiraFields{")
                    .append("progress='" + progress.toString()).append("',")
                    .append("summary='" + summary).append("',")
                    .append("votes='" + votes.toString()).append("',")
                    .append("resolution='" + resolution.toString()).append("',")
                    .append("fixVersions='" + fixVersions).append("',")
                    .append("resolutiondate='" + resolutiondate).append("',")
                    .append("timespent='" + timespent).append("',")
                    .append("reporter='" + reporter.toString()).append("',")
                    .append("aggregatetimeoriginalestimate='" + aggregatetimeoriginalestimate).append("',")
                    .append("updated='" + updated).append("',")
                    .append("created='" + created).append("',")
                    .append("createdStr='" + getCreatedStr(null)).append("',")
                    .append("description='" + description).append("',")
                    .append("priority='" + priority.toString()).append("',")
                    .append("duedate='" + duedate).append("',")
                    .append("issuelinks='" + issuelinks).append("',")
                    .append("watches='" + watches.toString()).append("',")
                    .append("subtasks='" + subtasks).append("',")
                    .append("status='" + status.toString()).append("',")
                    .append("labels='" + labels).append("',")
                    .append("assignee='" + assignee.toString()).append("',")
                    .append("workratio='" + workratio).append("',")
                    .append("aggregatetimeestimate='" + aggregatetimeestimate).append("',")
                    .append("versions='" + versions).append("',")
                    .append("environment='" + environment).append("',")
                    .append("remaining timeestimate='" + remainingTimeestimate).append("',")
                    .append("aggregateprogress='" + aggregateprogress.toString()).append("',")
                    .append("lastViewed='" + lastViewed).append("',")
                    .append("components='" + components).append("',")
                    .append("timeoriginalestimate='" + timeoriginalestimate).append("',")
                    .append("aggregatetimespent='" + aggregatetimespent).append("',")
                    .append("project='" + project.toString()).append("',").append("',")
                    .append("hasParent='" + hasParent()).append("',").append("',")
                    .append("customfield_11610='" + customfield_11610).append("',")
                    .append("customfield_11510='" + customfield_11510).append("',")
                    .append("customfield_11710='" + customfield_11710).append("',")
                    .append("pjlAttributes='" + pjlAttributes.toString()).append("',")
                    .append("}")).toString();
                            */
            return "";
        }
    }

    public static class JiraCreator {

        public JiraCreator() {
        }

        private String name;
        private String active;
        private String emailAddress;
        private String self;
        private String displayName;
        private JiraAvatarUrls avatarUrls;

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public void setActive(String active) {
            this.active = active;
        }
        public String getActive() {
            return active;
        }
        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }
        public String getEmailAddress() {
            return emailAddress;
        }
        public void setSelf(String self) {
            this.self = self;
        }
        public String getSelf() {
            return self;
        }
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
        public String getDisplayName() {
            return displayName;
        }
        public void setAvatarUrls(JiraAvatarUrls avatarUrls) {
            this.avatarUrls = avatarUrls;
        }
        public JiraAvatarUrls getAvatarUrls() {
            return avatarUrls;
        }

    }

    /**
     *
     */
    public static class JiraResolution {

        public JiraResolution(){
        }

        // "resolution":{"id":"6","description":"Corrigé","name":"Corrigé","self":"http://ismjira.ch.rccad.net/jira/rest/api/2/resolution/6"}
        private String self ;
        private String id ;
        private String description;
        private String name;

        public void setSelf(String self) {
            this.self = self;
        }

        public String getSelf() {
            return self;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return (new StringBuilder("JiraResolution{")
                    .append("id='" + id).append("',")
                    .append("description='" + description).append("',")
                    .append("name='" + name).append("',")
                    .append("self='" + self)
                    .append("}")).toString();
        }

    }

    /**
     *
     */
    public static class JiraProgress {

        public JiraProgress(){
        }

        //progress":{"progress":0,"total":432000,"percent":0}
        private String progress ;
        private String total;
        private String percent;

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public String getProgress() {
            return progress;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getTotal() {
            return total;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getPercent() {
            return percent;
        }

        @Override
        public String toString() {
            return (new StringBuilder("JiraProgress{")
                    .append("progress='" + progress).append("',")
                    .append("total='" + total).append("',")
                    .append("percent='" + percent)
                    .append("}")).toString();
        }

    }


    // For the Tâche technique
    public static class JiraTimeTracking {

        public JiraTimeTracking(){
        }

        // "timetracking":{"remainingEstimate":"1d","remainingEstimateSeconds":28800,"timeSpentSeconds":0,"timeSpent":"0m"}
        private String remainingEstimate ;
        private long remainingEstimateSeconds;
        private long timeSpentSeconds;
        private String timeSpent;

        public void setRemainingEstimate(String remainingEstimate) {
            this.remainingEstimate = remainingEstimate;
        }

        public String getRemainingEstimate() {
            return remainingEstimate;
        }

        public void setRemainingEstimateSeconds(long remainingEstimateSeconds) {
            this.remainingEstimateSeconds = remainingEstimateSeconds;
        }

        public long getRemainingEstimateSeconds() {
            return remainingEstimateSeconds;
        }

        public void setTimeSpentSeconds(long timeSpentSeconds) {
            this.timeSpentSeconds = timeSpentSeconds;
        }

        public long getTimeSpentSeconds() {
            return timeSpentSeconds;
        }

        public String getTimeSpent() {
            return timeSpent;
        }

        public void setTimeSpent(String timeSpent) {
            this.timeSpent = timeSpent;
        }

        @Override
        public String toString() {
            return (new StringBuilder("JiraTimeTracking{")
                    .append("remainingEstimate='" + remainingEstimate).append("',")
                    .append("remainingEstimateSeconds='" + remainingEstimateSeconds).append("',")
                    .append("timeSpentSeconds='" + timeSpentSeconds).append("',")
                    .append("timeSpent='" + timeSpent)
                    .append("}")).toString();
        }

    }

    /**
     *
     */
    public static class JiraIssueType {

        // issuetype":{"self":"http://localhost:8081/rest/api/2/issuetype/1","id":"1","description":"A problem which impairs or prevents the functions of the product.","iconUrl":"http://localhost:8081/images/icons/bug.gif","name":"Bug","subtask":false}
        // id = 6 Demande de support
        // id = 9 Epic
        // id = 10 Story
        // id = 11 Tache technique

        public JiraIssueType(){
        }

        private String self;
        private int id;
        private String description;
        private String iconUrl;
        private String name;
        private boolean subtask;
        private String shortName;

        public void setSelf(String self) {
            this.self = self;
        }

        public String getSelf() {
            return self;
        }

        public void setId(int id) {
            this.id = id;
            setShortName(id);
        }

        public int getId() {
            return id;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setName(String name) {
            // Story, Bug, Improvement, Task, Epic
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setSubtask(boolean subtask) {
            this.subtask = subtask;
        }

        public boolean getSubtask() {
            return subtask;
        }

        public boolean isDemandeSupport() {
            if (this.id == 6) return true;
            else return false;
        }

        public boolean isStory() {
            if (this.id == 10) return true;
            else return false;
        }

        public boolean isEpic() {
            if (this.id == 9) return true;
            else return false;
        }

        public boolean isTechnicalTask() {
            if (this.id == 11) return true;
            else return false;
        }

        public void setShortName(int id) {
            switch (id){
                case 6: this.shortName = JiraConst.JIRA_ISSUE_TYPE_SHORTNAME_6;  // Demande de support
                    break;
                case 9: this.shortName = JiraConst.JIRA_ISSUE_TYPE_SHORTNAME_9;  // Epic
                    break;
                case 10: this.shortName = JiraConst.JIRA_ISSUE_TYPE_SHORTNAME_10;  // Story
                    break;
                case 11: this.shortName = JiraConst.JIRA_ISSUE_TYPE_SHORTNAME_11;  // Tache technique
                    break;
                case 15: this.shortName = JiraConst.JIRA_ISSUE_TYPE_SHORTNAME_15;  // Bug
                    break;
                case 10100: this.shortName = JiraConst.JIRA_ISSUE_TYPE_SHORTNAME_10100;  // Improvement
                    break;
                default: this.shortName = "???";
                    break;
            }
        }

        public String getShortName() {
            return this.shortName;
        }

        public String toString() {
            return (new StringBuilder("JiraIssueType{")
                    .append("self='" + self).append("',")
                    .append("id='" + id).append("',")
                    .append("description='" + description).append("',")
                    .append("name='" + name).append("',")
                    .append("shortName='" + shortName).append("',")
                    .append("subtask='" + subtask).append("',")
                    .append("iconUrl='" + iconUrl)
                    .append("}")).toString();
        }

    }


    /**
     *
     */
    public static class JiraVote {
        //votes":{"self":"http://localhost:8081/rest/api/2/issue/MPT-2/votes","votes":0,"hasVoted":false}

        public JiraVote(){
        }

        private String self;
        private String votes;
        private boolean hasVoted;

        public void setSelf(String self) {
            this.self = self;
        }

        public String getSelf() {
            return self;
        }

        public void setVotes(String votes) {
            this.votes = votes;
        }

        public String getVotes() {
            return votes;
        }

        public void setHasVoted(boolean hasVoted) {
            this.hasVoted = hasVoted;
        }

        public boolean getHasVoted(){
            return hasVoted ;
        }

        public String toString() {
            return (new StringBuilder("JiraVote{")
                    .append("self='" + self).append("',")
                    .append("votes='" + votes).append("',")
                    .append("hasVoted='" + hasVoted)
                    .append("}")).toString();
        }

    }

    /**
     *
     */
    public static class JiraReporter {
        // reporter":{"self":"http://localhost:8081/rest/api/2/user?username=laurent.linck","name":"laurent.linck","emailAddress":"laurent.linck@richemont.com","avatarUrls":{"16x16":"http://localhost:8081/secure/useravatar?size=small&avatarId=10122","48x48":"http://localhost:8081/secure/useravatar?avatarId=10122"},"displayName":"Laurent Linck","active":true}

        public JiraReporter(){
        }

        private String self;
        private String name;
        private String emailAddress;
        private String displayName;
        private boolean active;
        private JiraAvatarUrls avatarUrls;

        public void setSelf(String self) {
            this.self = self;
        }

        public String getSelf(String self) {
            return self;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName(){
            return name ;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getEmailAddress(){
            return emailAddress ;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName(){
            return displayName ;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean getActive(){
            return active ;
        }

        public void setAvatarUrls(JiraAvatarUrls avatarUrls) {
            this.avatarUrls = avatarUrls;
        }

        public JiraAvatarUrls getAvatarUrls(){
            return avatarUrls ;
        }

        public String toString() {
            return (new StringBuilder("JiraReporter{")
                    .append("self='" + self).append("',")
                    .append("name='" + name).append("',")
                    .append("emailAddress='" + emailAddress).append("',")
                    .append("displayName='" + displayName).append("',")
                    .append("active='" + active).append("',")
                    .append("avatarUrls='" + avatarUrls)
                    .append("}")).toString();
        }

    }


    @JsonIgnoreProperties({
            "24x24",
            "32x32"
    })
    public static class JiraAvatarUrls {
        // "avatarUrls":{"16x16":"http://localhost:8081/secure/useravatar?size=small&avatarId=10122","48x48":"http://localhost:8081/secure/useravatar?avatarId=10122}
        private String smallAvatar;
        private String largeAvatar;

        public JiraAvatarUrls(){
        }

        public void set16x16(String smallAvatar) {
            this.smallAvatar = smallAvatar;
        }

        public String get16x16(){
            return smallAvatar ;
        }

        public void set48x48(String largeAvatar) {
            this.largeAvatar = largeAvatar;
        }

        public String get48x48(){
            return largeAvatar ;
        }

        public String toString() {
            return (new StringBuilder("JiraAvatarUrls{")
                    .append("smallAvatar='" + smallAvatar).append("',")
                    .append("largeAvatar='" + largeAvatar)
                    .append("}")).toString();
        }
    }



    /**
     *
     */
    public static class JiraPriority {
        // priority":{"self":"http://localhost:8081/rest/api/2/priority/3","iconUrl":"http://localhost:8081/images/icons/priority_major.gif","name":"Major","id":"3"}
        private String self;
        private String iconUrl;
        private String name;
        private String id;

        public JiraPriority(){
        }

        public void setSelf(String self) {
            this.self = self;
        }

        public String getSelf(){
            return self ;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getIconUrl(){
            return iconUrl ;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName(){
            return name ;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId(){
            return id ;
        }

        @Override
        public String toString() {
            return (new StringBuilder("JiraPriority{")
                    .append("self='" + self).append("',")
                    .append("name='" + name).append("',")
                    .append("id='" + id).append("',")
                    .append("iconUrl='" + iconUrl)
                    .append("}")).toString();
        }

    }
        /**
         *
         */
        public static class JiraWatches {
            // watches":{"self":"http://localhost:8081/rest/api/2/issue/MPT-2/watchers","watchCount":1,"isWatching":true}
            private String self;
            private int watchCount;
            private boolean isWatching;

            public JiraWatches(){
            }

            public void setSelf(String self) {
                this.self = self;
            }

            public String getSelf(){
                return self ;
            }

            public void setWatchCount(int watchCount) {
                this.watchCount = watchCount;
            }

            public int getWatchCount(){
                return watchCount ;
            }

            public void setIsWatching(boolean isWatching) {
                this.isWatching = isWatching;
            }

            public boolean getIsWatching(){
                return isWatching ;
            }

            public String toString() {
                return (new StringBuilder("JiraWatches{")
                        .append("self='" + self).append("',")
                        .append("watchCount='" + watchCount).append("',")
                        .append("isWatching='" + isWatching)
                        .append("}")).toString();
            }
         }

    /**
     *
     */
    public static class JiraStatus {
        // "status":{"self":"http://localhost:8081/rest/api/2/status/1","description":"The issue is open and ready for the assignee to start work on it." ,"iconUrl":"http://localhost:8081/images/icons/status_open.gif","name":"Open","id":"1"}
        // http://ismjira.ch.rccad.net/jira/rest/api/2/status/6
        // new = 10000
        // Open = 1 ?
        // reopened =
        // On Hold = 10001
        // Resolved
        // Closed = 6
        private String self;
        private String description;
        private String iconUrl;
        private String name;
        private String id;
        private JiraStatusCategory statusCategory;

        public JiraStatus(){
        }

        public void setSelf(String self) {
            this.self = self;
        }

        public String getSelf(){
            return self ;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription(){
            return description ;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName(){
            return name ;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId(){
            return id ;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getIconUrl(){
            return iconUrl ;
        }

        public void setStatusCategory(JiraStatusCategory statusCategory) {
            this.statusCategory = statusCategory;
        }
        public JiraStatusCategory getStatusCategory() {
            return this.statusCategory;
        }

        @Override
        public String toString() {
            return (new StringBuilder("JiraStatus{")
                    .append("self='" + self).append("',")
                    .append("description='" + description).append("',")
                    .append("name='" + name).append("',").append("',")
                    .append("id='" + id).append("',").append("',")
                    .append("iconUrl='" + iconUrl)
                    .append("}")).toString();
        }
    }

    public static class JiraStatusCategory {
        private String self; /* http://ismjira.ch.rccad.net/jira/rest/api/2/statuscategory/2  */
        private String id; /* 2 */
        private String key; /* new */
        private String colorName; /* blue-gray */
        private String name; /* New */

        public JiraStatusCategory (){
        }

        public void setSelf(String self) {
            this.self = self;
        }
        public String getSelf(){
            return self ;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getId(){
            return id ;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public String getKey(){
            return key ;
        }
        public void setColorName(String colorName) {
            this.colorName = colorName;
        }
        public String getColorName(){
            return colorName ;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getName(){
            return name ;
        }

        @Override
        public String toString() {
            return (new StringBuilder("JiraStatusCategory{")
                    .append("self='" + self).append("',")
                    .append("name='" + name)
                    .append("}")).toString();
        }
    }



    /**
     *
     */
    public static class JiraAssignee {
        //"assignee":{"self":"http://localhost:8081/rest/api/2/user?username=laurent.linck","name":"laurent.linck","emailAddress":"laurent.linck@richemont.com","avatarUrls":{"16x16":"http://localhost:8081/secure/useravatar?size=small&avatarId=10122","48x48":"http://localhost:8081/secure/useravatar?avatarId=10122"},"displayName":"Laurent Linck","active":true}
        private String self;
        private String name;
        private String emailAddress;
        private JiraAvatarUrls avatarUrls;
        private String displayName;
        private String active;

        public JiraAssignee(){
        }

        public void setSelf(String self) {
            this.self = self;
        }

        public String getSelf(){
            return self ;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName(){
            return name ;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getEmailAddress(){
            return emailAddress ;
        }

        public void setAvatarUrls(JiraAvatarUrls avatarUrls) {
            this.avatarUrls = avatarUrls;
        }

        public JiraAvatarUrls getAvatarUrls(){
            return avatarUrls ;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName(){
            return displayName ;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getActive(){
            return active ;
        }

        @Override
        public String toString() {
            return (new StringBuilder("JiraAssignee{")
                    .append("self='" + self).append("',")
                    .append("name='" + name).append("',")
                    .append("emailAddress='" + emailAddress).append("',")
                    .append("displayName='" + displayName).append("',")
                    .append("active='" + active).append("',")
                    .append("avatarUrls='" + avatarUrls)
                    .append("}")).toString();
        }
    }


    /**
     *
     */
    public static class JiraProject {
        //"project":{"self":"http://localhost:8081/rest/api/2/project/MPT","id":"10001","key":"MPT","name":"MON PROJET TEST","avatarUrls":{"16x16":"http://localhost:8081/secure/projectavatar?size=small&pid=10001&avatarId=10011","48x48":"http://localhost:8081/secure/projectavatar?pid=10001&avatarId=10011"}}
        private String self;
        private String name;
        private String id;
        private String key;
        private JiraAvatarUrls avatarUrls;

        public JiraProject(){
        }

        public void setSelf(String self) {
            this.self = self;
        }

        public String getSelf(){
            return self ;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
           return name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setAvatarUrls(JiraAvatarUrls avatarUrls) {
            this.avatarUrls = avatarUrls;
        }

        public JiraAvatarUrls getAvatarUrls(){
            return avatarUrls ;
        }

        @Override
        public String toString() {
            return (new StringBuilder("JiraProject{")
                    .append("self='" + self).append("',")
                    .append("name='" + name).append("',")
                    .append("id='" + id).append("',")
                    .append("key='" + key).append("',")
                    .append("avatarUrls='" + avatarUrls)
                    .append("}")).toString();
        }
    }

    /**
     *
     */
    public static class JiraAggregateprogress {
        //{"progress":0,"total":432000,"percent":0}
        private String progress;
        private String total;
        private String percent;

        public JiraAggregateprogress(){
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public String getProgress(){
            return progress ;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getTotal(){
            return total ;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getPercent(){
            return percent ;
        }

        public String toString() {
            return (new StringBuilder("JiraAggregateprogress{")
                    .append("progress='" + progress).append("',")
                    .append("total='" + total).append("',")
                    .append("percent='" + percent)
                    .append("}")).toString();
        }

    }


    public static class JiraEHour {
        // {"id":"10310","value":"Yes","self":"http://ismjira.ch.rccad.net/jira/rest/api/2/customFieldOption/10310"}
        public JiraEHour(){
        }

        private String id = null;
        private String value = "No";
        private String self = null;

        private void setId(String id){
            this.id = id ;
        }

        public String getId(){
            return this.id;
        }

        public void setValue(String value){
            this.value = value;
        }

        public boolean getValue(){
            if (!this.value.equalsIgnoreCase("Yes")) return false;
            else return true ;
        }

        private void setSelf(String self){
            this.self = self;
        }

        public String getSelf(){
            return this.self ;
        }

        @Override
        public String toString() {
            return (new StringBuilder("JiraEHour{")
                    .append("id='" + id).append("',")
                    .append("value='" + value).append("',")
                    .append("self='" + self).append("',")
                    .append("}")).toString();
        }

    }

    public static class JiraWarm {
        // {"id":"10310","value":"Yes","self":"http://ismjira.ch.rccad.net/jira/rest/api/2/customFieldOption/11810"}
        public JiraWarm(){
        }

        private String id = null;
        private String value = "No";
        private String self = null;

        private void setId(String id){
            this.id = id ;
        }

        public String getId(){
            return this.id;
        }

        private void setValue(String value){
            this.value = value;
        }

        public boolean getValue(){
            if (!this.value.equalsIgnoreCase("Yes")) return false;
            else return true ;
        }

        private void setSelf(String self){
            this.self = self;
        }

        public String getSelf(){
            return this.self ;
        }

        @Override
        public String toString() {
            return (new StringBuilder("JiraWarm{")
                    .append("id='" + id).append("',")
                    .append("value='" + value).append("',")
                    .append("self='" + self).append("',")
                    .append("}")).toString();
        }

    }

    public static class JiraPJL {
        // {"id":"10310","value":"Yes","self":"http://ismjira.ch.rccad.net/jira/rest/api/2/customFieldOption/11811"}
        public JiraPJL(){
        }

        private String id = null;
        private String value = "No";
        private String self = null;

        private void setId(String id){
            this.id = id ;
        }

        public String getId(){
            return this.id;
        }

        private void setValue(String value){
            this.value = value;
        }

        public boolean getValue(){
            if (!this.value.equalsIgnoreCase("Yes")) return false;
            else return true ;
        }

        private void setSelf(String self){
            this.self = self;
        }

        public String getSelf(){
            return this.self ;
        }

        @Override
        public String toString() {
            return (new StringBuilder("JiraPJL{")
                    .append("id='" + id).append("',")
                    .append("value='" + value).append("',")
                    .append("self='" + self).append("',")
                    .append("}")).toString();
        }

    }

    /**
     * PJL Attribues
     */
    public static class ProjectLinkAttributes{
        private String pjlProjectId = pjlDefaultProjectId;
        private String pjlProjectName = pjlDefaultProjectName;
        private String pjlOrgId = pjlDefaultOrgId;
        private String pjlOrgName = pjlDefaultOrgName;
        private String pjlActivityId = pjlDefaultActivityId;
        boolean updated ;

        public ProjectLinkAttributes(){
        }

        // ProjectLink IDA2A2 project
        // wt.projmgmt.admin.Project2:6590512736
        // OR
        //  richemont~~TEST-LLI-EHOUR-01~~027295~~wt.projmgmt.admin.Project2:6590512736
        public void setPjlProjectId(String pjlProjectId) {
            this.pjlProjectId = pjlProjectId;
        }

        // ProjectLink
        public String getPjlProjectId() {
            return this.pjlProjectId;
        }

        public void setPjlProjectName(String pjlProjectName) {
            this.pjlProjectName = pjlProjectName;
        }

        public String getPjlProjectName() {
            return this.pjlProjectName;
        }

        public void setPjlOrgName(String pjlOrgName) {
            this.pjlOrgName = pjlOrgName;
        }

        public String getPjlOrgName() {
            return this.pjlOrgName;
        }

        public void setPjlOrgId(String pjlOrgId) {
            this.pjlOrgId = pjlOrgId;
        }

        public String getPjlOrgId() {
            return this.pjlOrgId;
        }

        public void setPjlActivityId(String pjlActivityId) {
            this.pjlActivityId = pjlActivityId;
        }

        public String getPjlActivityId() {
            return this.pjlActivityId;
        }

        // has been updated from PJL ?
        public void updated (boolean upToDate) {
            this.updated = upToDate;
        }

        public boolean updated() {
            return this.updated;
        }

        @Override
        public String toString() {
            return (new StringBuilder("ProjectLinkAttributes{")
                    .append("pjlProjectId='" + pjlProjectId).append("',")
                    .append("pjlProjectName='" + pjlProjectName).append("',")
                    .append("pjlOrgId='" + pjlOrgId).append("',")
                    .append("pjlOrgName='" + pjlOrgName).append("',")
                    .append("pjlActivityId='" + pjlActivityId).append("',")
                    .append("updated='" + updated)
                    .append("}")).toString();
        }
    }


    /**
     *
     */
    public static class JiraParent{
        // "parent":{"id":"14497","self":"http://ismjira.ch.rccad.net/jira/rest/api/2/issue/14497","key":"RIC-391",
        // "fields":{"summary":"modif ExtractionHelper pour Cartier","issuetype":{"subtask":false,"id":"6","description":"","name":"Demande de support","iconUrl":"http://ismjira.ch.rccad.net/jira/images/icons/issuetypes/undefined.png","self":"http://ismjira.ch.rccad.net/jira/rest/api/2/issuetype/6"}
        // ....

        public JiraParent(){
        }

        private String id;
        private String self;
        private String key;
        private JiraIssue jiraParentIssue;

        public void setId(String id) {
            this.id = id;
        }

        public String getId(){
            return id ;
        }

        public void setSelf(String self) {
            this.self = self;
        }

        public String getSelf(){
            return self ;
        }

        public void setKey(String key) throws IOException, JSONException, AuthenticationException {
            this.key = key;
            this.jiraParentIssue = JiraHelper.getJiraIssue(key);
        }

        public String getKey(){
            return key ;
        }

        public JiraIssue getObject(){
            return jiraParentIssue ;
        }

        @Override
        public String toString() {
           /* return (new StringBuilder("JiraParent{")
                    .append("id='" + id).append("',")
                    .append("key='" + key).append("',")
                    .append("self='" + self).append("',")
                    .append("jiraIssue='" + jiraParentIssue.toString())
                    .append("}")).toString();
           */
            System.out.println(id);
            System.out.println(key);
            System.out.println(self);
            System.out.println(jiraParentIssue);
            return "\n\nEND";
        }
    }


}