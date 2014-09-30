package com.richemont.jira;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * @author laurent.linck
 */

@JsonIgnoreProperties({ "self", "updateAuthor"  })
public class JiraWorklog{
    // {"id":"12948","author":{"name":"laurent.linck","active":true,"emailAddress":"laurent.linck@richemont.com","self":"http://ismjira.ch.rccad.net/jira/rest/api/2/user?username=laurent.linck","displayName":"LINCK, Laurent","avatarUrls":{"16x16":"http://ismjira.ch.rccad.net/jira/secure/useravatar?size=small&avatarId=10043","48x48":"http://ismjira.ch.rccad.net/jira/secure/useravatar?avatarId=10043"}},"updated":"2013-04-30T12:32:34.000+0200","created":"2013-04-30T12:32:34.000+0200","timeSpentSeconds":18000,"updateAuthor":{"name":"laurent.linck","active":true,"emailAddress":"laurent.linck@richemont.com","self":"http://ismjira.ch.rccad.net/jira/rest/api/2/user?username=laurent.linck","displayName":"LINCK, Laurent","avatarUrls":{"16x16":"http://ismjira.ch.rccad.net/jira/secure/useravatar?size=small&avatarId=10043","48x48":"http://ismjira.ch.rccad.net/jira/secure/useravatar?avatarId=10043"}},"self":"http://ismjira.ch.rccad.net/jira/rest/api/2/issue/20545/worklog/12948","started":"2013-04-30T07:00:18.000+0200","timeSpent":"5h","comment":"Adding Worklog thru Sample ex"}

    final public static String LOCAL_TIMEZONE="Europe/Paris";
    final public static String UTC_TIMEZONE="UTC";

    public JiraWorklog(){

    }

    private String id;
    private WorkLoadAuthor author;
    private String self;
    private DateTime updated;
    private DateTime created;
    private Long timeSpentSeconds;
    private String updateAuthor;
    private DateTime started;
    private String timeSpent;   // 5h
    private String comment;


    private void setId(String id){
        this.id = id ;
    }

    public String getId(){
        return this.id;
    }

    private void setAuthor(WorkLoadAuthor workLoadAuthor){
        this.author = workLoadAuthor ;
    }

    public WorkLoadAuthor getAuthor(){
        return this.author;
    }

    private void setTimeSpent(String timeSpent){
        this.timeSpent = timeSpent ;
    }

    public String getTimeSpent(){
        return this.timeSpent;
    }

    private void setStarted(DateTime started){
        DateTime srcDateTime = started.toDateTime(DateTimeZone.forID( UTC_TIMEZONE) );
        this.started = srcDateTime.withZone(DateTimeZone.forID( LOCAL_TIMEZONE ));
    }

    public Long getTimeSpentSeconds(){
        return this.timeSpentSeconds;
    }

    private void setTimeSpentSeconds(Long timeSpentSeconds){
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public DateTime getStarted(){
    return started ;
    }

    private void setCreated(DateTime created){
        DateTime srcDateTime = created.toDateTime(DateTimeZone.forID( UTC_TIMEZONE) );
        this.created = srcDateTime.withZone(DateTimeZone.forID( LOCAL_TIMEZONE));
    }

    public DateTime getCreated(){
        return this.created;
    }

    private void setUpdated(DateTime updated){
        DateTime srcDateTime = updated.toDateTime(DateTimeZone.forID( UTC_TIMEZONE ) );
        this.updated = srcDateTime.withZone(DateTimeZone.forID(LOCAL_TIMEZONE) );
    }

    public DateTime getUpdated(){
        return this.updated;
    }

    private void setComment(String comment){
        this.comment = comment ;
    }

    public String getComment(){
        return this.comment;
    }

    public String toString() {
        return (new StringBuilder("JiraWorkload{")
                .append("author='" + author.getName()).append("',")
                .append("id='" + id).append("',")
                .append("timeSpent='" + timeSpent).append("',")
                .append("started='" + started).append("',")
                .append("created='" + created).append("',")
                .append("updated='" + updated).append("',")
                .append("comment='" + comment)
                .append("}")).toString();
    }


    /**
     *
     */
    @JsonIgnoreProperties({ "avatarUrls"})
    public static class WorkLoadAuthor {
        // "author":{"name":"laurent.linck","active":true,"emailAddress":"laurent.linck@richemont.com","self":"http://ismjira.ch.rccad.net/jira/rest/api/2/user?username=laurent.linck","displayName":"LINCK, Laurent","avatarUrls":{"16x16":"http://ismjira.ch.rccad.net/jira/secure/useravatar?size=small&avatarId=10043","48x48":"http://ismjira.ch.rccad.net/jira/secure/useravatar?avatarId=10043"}}

        public WorkLoadAuthor(){
        }

        private String name;
        private boolean active;
        private String emailAddress;
        private String self;
        private String displayName;
        //private JiraAvatarUrls avatarUrls;

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

        public String toString() {
            return (new StringBuilder("WorkLoadAuthor{")
                    .append("self='" + self).append("',")
                    .append("name='" + name).append("',")
                    .append("emailAddress='" + emailAddress).append("',")
                    .append("displayName='" + displayName).append("',")
                    .append("active='" + active)
                    .append("}")).toString();
        }

    }
}
