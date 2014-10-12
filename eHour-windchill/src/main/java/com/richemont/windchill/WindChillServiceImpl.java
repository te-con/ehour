package com.richemont.windchill;

import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.timesheet.service.IPersistTimesheet;
import net.rrm.ehour.user.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author laurent.linck
 */
@Service("chillService")
public class WindChillServiceImpl implements WindChillService {
    @Autowired
    private AggregateReportService aggregateReportService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private IPersistTimesheet timesheetPersister;

    @Autowired
    private ReportAggregatedDao reportAggregatedDao;

    @Autowired
    private TimesheetDao timesheetDao;

    @Value("${ehour.windchill.enabled}")
    private String enabled;

    @Autowired
    private QueryTimeSheets queryTimeSheets;

    @Value("${richemont.windchill.soap.endpoint}")
    private String endpoint;

    private static Logger LOGGER = Logger.getLogger("ext.service.WindchillServiceImpl");

    static {
        try {
            LOGGER.info("ext.service.WindchillServiceImpl loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isEnabled() {
        return enabled == null || Boolean.parseBoolean(enabled);
    }


    /**
     * update EHour from PJL
     * called by net.rrm.ehour.ui.login.page.Login.java
     * called by net.rrm.ehour.ui.common.component.header.HeaderPanel.java
     *
     * @param username
     */
    @Override
    public boolean updateDataForUser(Map<String, Activity> allAssignedActivitiesByCode, String username) {
        boolean isSync = false;
        if (!isEnabled()) {
            LOGGER.info("Windchill sync is disabled");
            return true;
        } else {
            try {
                isSync =  updateProjectForUser (allAssignedActivitiesByCode, username) ;
            } catch (Exception e) {
                LOGGER.error("***** updateDataForUser SYNC FAILED *****");
                e.printStackTrace();
            }
        }
        return isSync;

    }


    /**
     * Update
     * @param username
     * @return
     */
    public boolean updateProjectForUser(Map<String, Activity> allAssignedActivitiesByCode, String username) {
        boolean isSync = false;
        LOGGER.info("***** Initialisation des donnees de l'utilisateur " + username );

        // DISPLAY FOR DEBUG ONLY !!!
        if (LOGGER.isDebugEnabled()) {
            displayAllCustomers(getAllCustomers());
            displayAllAssignedActivities(allAssignedActivitiesByCode);
        }

        // liste des activites  eHour que l on va creer ou mettre a jour
        HashMap<String, Activity>  hmDealedActivities =  new HashMap<String, Activity>();

        // Windchill

        // recuperation de toutes les activites du user
        LOGGER.debug("\n\n");
        LOGGER.debug("***** Appel du Windchill WS TimeSheetMgt *******");

        // Create project and activities for this user.
        List< HashMap<String, Comparable> > listeDesActiviteesDepuisGrutlink;
        int compte=0;

        User assignedUser = userService.getAuthorizedUser(username);

        Map<String, Customer> hmAllCustomersByCode = getAllCustomersByCode();

        try {
            // FROM Windchill: get allProjectActivities for a given user
            listeDesActiviteesDepuisGrutlink = queryTimeSheets.getTimeSheets(username, "getTimeSheets", endpoint);
            if (listeDesActiviteesDepuisGrutlink != null){

                // TO eHour: update/create allProjectActivities for a given user from Windchill

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("\n\n");
                    LOGGER.info("***** Mise a jour de " + listeDesActiviteesDepuisGrutlink.size() + " activites dans eHour a partir de ProjectLink pour " + username);
                }
                for (HashMap<String, Comparable> aListeDesActiviteesDepuisGrutlink : listeDesActiviteesDepuisGrutlink) {
                    compte++;
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("--> Activity #" + compte + "/" + listeDesActiviteesDepuisGrutlink.size() + " for user " + username);
                    }

                    HashMap<String, Comparable> hm = aListeDesActiviteesDepuisGrutlink;
                    displayHashMapContent(hm); // trace for debug

                    Activity currentActivity = createNewActivity(hm, allAssignedActivitiesByCode, assignedUser, WindchillConst.WIND_DATE_FORMAT, hmAllCustomersByCode);
                    // toutes les activites nouvelles ou mises a jour dans eHour
                    if (currentActivity != null) {
                        hmDealedActivities.put( currentActivity.getCode(), currentActivity );
                        LOGGER.debug("\tdealedActivities=" + currentActivity.getName());
                    }
                }

                // Les activites non nouvelles ou non mises a jour dans eHour doivent etre desactivees
                desactivateObsoleteActivity( allAssignedActivitiesByCode, hmDealedActivities);
                isSync = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("***** updateDataForUser SYNC FAILED *****");
        }
        return isSync;
    }

    @Override
    public Activity createNewActivity(Map<String, Comparable> hm, Map<String, Activity> allAssignedActivitiesByCode, User assignedUser, SimpleDateFormat dateFormat) {
        Map<String, Customer> hmAllCustomersByCode = getAllCustomersByCode();

        return createNewActivity(hm, allAssignedActivitiesByCode, assignedUser, dateFormat, hmAllCustomersByCode);
    }

    private Activity createNewActivity(Map<String, Comparable> attributeMap, Map<String, Activity> allAssignedActivitiesByCode, User assignedUser, SimpleDateFormat dateFormat, Map<String, Customer> customerOnCode) {
        Activity currentActivity = null;

        String orgId = (String) attributeMap.get(WindchillConst.ORG_ID);
        String orgName = (String) attributeMap.get(WindchillConst.ORG_NAME);
        String projectID = (String) attributeMap.get(WindchillConst.PROJECT_ID); // ProjectId de Windchill = projectCode de eHour
        String projectName = (String) attributeMap.get(WindchillConst.PROJECT_NAME);
        String activityId = (String) attributeMap.get(WindchillConst.ACTIVITY_ID); // ActivityId de Windchill = activityCode de eHour
        String activityName = (String) attributeMap.get(WindchillConst.ACTIVITY_NAME);

        Float projectAllocatedHours = new Float((String) attributeMap.get(WindchillConst.REMAINING_WORK)); // Remaining Work
        Float projectPerformedHours = new Float((String) attributeMap.get(WindchillConst.PERWORMED_WORK)); //Actual Work

        Date projectActivityStartDate = DateUtils.convertStringToDate((String) attributeMap.get(WindchillConst.ACTIVITY_START_DATE), dateFormat);
        Date projectActivityEndDate = DateUtils.convertStringToDate((String) attributeMap.get(WindchillConst.ACTIVITY_END_DATE), dateFormat);

        Project prj = checkProject(projectID, projectName);
        // activites a creer dans ehour
        if (!isAssignedActivityExist(activityId, allAssignedActivitiesByCode)) {
            LOGGER.debug("\tisAssignedActivityExist = false --> creation dans eHour de " + activityId);

            try {
                currentActivity = createAssignedActivity(assignedUser, activityId, activityName,
                        projectAllocatedHours, projectPerformedHours, projectActivityStartDate, projectActivityEndDate, projectID,
                        projectName, prj, orgId, orgName,
                        allAssignedActivitiesByCode,
                        customerOnCode);
            } catch (OverBudgetException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            // activites deja existantes dans ehour
        } else {
            LOGGER.debug("\tisAssignedActivityExist = true --> synchronisation des modifs...");
            currentActivity = updateAssignedActivity(assignedUser, prj, activityId, activityName, projectAllocatedHours, projectActivityStartDate, projectActivityEndDate, allAssignedActivitiesByCode);
            LOGGER.debug("\tcurrentActivity --> maj " + currentActivity.getName() );
        }
        return currentActivity;
    }

    public Project checkProject(String projectCode, String newProjectName) {
        Project prj = projectService.getProject(projectCode);
        if (prj == null) {
            LOGGER.debug("\tThis project does not exist: " + projectCode);
        } else {
            // on s assure que le projet est actif
            LOGGER.debug("\tActive projet: " + prj.getName());

            boolean modified = false;

            if (!prj.isActive()) {
                prj.setActive(true);
                modified = true;
            }

            // verifie si le projet PJl a ete renomme depuis la derniere mise a jour dans e-Hour
            if (!newProjectName.equals(prj.getName())) {
                LOGGER.debug("\tMise a jour du nom du projet: " + prj.getName() + " --> " + newProjectName);
                prj.setName(newProjectName);
                modified = true;
            }

            LOGGER.debug("\tproject name=ok");

            if (modified) {
                prj = projectService.updateProject(prj);
            }
        }
        return prj;
    }

    /**
     *
     * @param hmAllAssignedActivitiesByCode : Toutes les activites eHour existantes pour l utilisateur
     * les activites *dealedActivities* non presentes dans *hmAllAssignedActivitiesByCode* doivent etre desactivees si pas de booked hours
     *
     * @param hmAllAssignedActivitiesByCode  all the activities from ehour
     * @param hmDealedActivities  all the new and modified activities from windchill to be updated in Ehour
     */
    public void desactivateObsoleteActivity(Map<String, Activity> hmAllAssignedActivitiesByCode, Map<String, Activity> hmDealedActivities) {

        List<Activity> list1 =  new ArrayList<Activity> (hmAllAssignedActivitiesByCode.values());
        List<Activity> list2 =  new ArrayList<Activity> (hmDealedActivities.values());
        list1.removeAll(list2);

        LOGGER.debug("\tdesactivateObsoleteActivity()");
        int i =0;
        for(Activity anActivity: list1){

            if (anActivity.getCode().startsWith(WindchillConst.ACTIVITY_CODE_PREFIX_FOR_PJL)){
                LOGGER.debug("\t\tCheck for JPL activity " + anActivity.getName() + " [active=" + anActivity.getActive() + "]");
                LOGGER.debug("\t\t\tProject " + anActivity.getProject().getName());

                if (anActivity.getActive()){
                    LOGGER.debug("\t\t\tGetAllottedHours(): " + anActivity.getAllottedHours());

                    // S il y a des booked hours(= existe une timesheet), on ne veut pas desactiver l activite. Elle sera juste Locked
                    TimesheetEntry ts = timesheetDao.getLatestTimesheetEntryForActivity(anActivity.getId());

                    if (ts != null){
                        LOGGER.debug("\t\t\tLocking JPL activity" );
                        anActivity.setLocked(true);
                        activityService.persistActivity(anActivity);
                        LOGGER.debug("\t\t\tactive=" + anActivity.getActive() + " / locked=" + anActivity.getLocked());
                    } else  {
                        LOGGER.debug("\t\t\tDesactivate JPL activity" );
                        anActivity.setActive(false);
                        activityService.persistActivity(anActivity);
                        LOGGER.debug("\t\t\tactive=" + anActivity.getActive() + " / locked=" + anActivity.getLocked());
                    }

                } else {
                    LOGGER.debug("\t\t\tactive=" + anActivity.getActive() + " / locked=" + anActivity.getLocked());
                }

            }
        }
        LOGGER.debug("\t\tsubstract " + i +  " obsolete JPL activities in e-Hour ");
    }


    //
    // ORG (CUSTOMER) from Windchill
    //
    private HashMap<String, Customer> getCustomersFromGrutlink(Vector v) {
        HashMap<String, String> h;
        HashMap<String, Customer> hmCustomers = new HashMap<String, Customer>();
        for (int i = 0; i < v.size(); i++) {
            h = (HashMap) v.elementAt(i);

            Customer customer = new Customer((String) h.get("OrgId"),
                    (String) h.get("OrgName"), "", true);
            if (!hmCustomers.containsKey(customer.getCode())) {
                hmCustomers.put((String) h.get("OrgId"), customer);
            }
        }
        return hmCustomers;
    }

    private void displayAllCustomersFromGrutlink(HashMap<String,Customer> hmCustomers) {
        Set<String> clef = hmCustomers.keySet();
        Iterator<String> it = clef.iterator();
        LOGGER.debug("displayAllCustomersFromGrutlink:");
        while (it.hasNext()) {
            Object aKey = it.next();
            Customer customerValue = hmCustomers.get(aKey);
            LOGGER.debug("\t" + customerValue.getCode() + " - " + customerValue.getName());
        }

    }

    //
    // CUSTOMER from eHour
    //

    private List<Customer> getAllCustomers() {
        return customerService.getCustomers();
    }

    public Map<String, Customer> getAllCustomersByCode() {
        Map<String, Customer> allCustomersCode = new HashMap<String, Customer>();
        List<Customer> allCustomers = getAllCustomers();

        for (Customer aCustomer : allCustomers) {
            allCustomersCode.put(aCustomer.getCode(), aCustomer);
        }
        return allCustomersCode;
    }


    private void displayAllCustomers(List<Customer> allCustomers) {
        Iterator<Customer> it = allCustomers.iterator();
        Customer aCustomer;
        LOGGER.debug("displayAllCustomers:");
        while (it.hasNext()) {
            aCustomer = (Customer) it.next();
            LOGGER.debug("\t" + aCustomer.getFullName());
        }
    }

    private Customer isCustomerExist(String customerCode,
                                     Map<String, Customer> hmAllCustomerByCode) {
        if (hmAllCustomerByCode.containsKey(customerCode))
            return hmAllCustomerByCode.get(customerCode);
        return null;
    }

    private Customer createCustomer(String customerCode, String customerName,
                                    Map<String, Customer> hmAllCustomerByCode) {
        Customer newCustomer = new Customer();
        newCustomer.setCode(customerCode);
        newCustomer.setName(customerName);
        try {
            customerService.persistCustomer(newCustomer);
        } catch (ObjectNotUniqueException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        hmAllCustomerByCode.put(customerCode, newCustomer);
        return newCustomer;
    }

    //
    // PROJECT from eHour
    //

    private List<Project> getAllProjects() {
        List<Project> allProjects;
        allProjects = projectService.getProjects();
        return allProjects;
    }

    /**
     * Tous les projets e-Hour
     * @return
     */
    /**
     private HashMap<String, Project> getAllProjectsByCode() {
     HashMap<String, Project> HmAllProjectsByCode = new HashMap<String, Project>();
     Iterator<Project> it = getAllProjects().iterator();
     Project aProject;
     while (it.hasNext()) {
     aProject = (Project) it.next();
     HmAllProjectsByCode.put(aProject.getProjectCode(), aProject);
     }
     return HmAllProjectsByCode;
     }
     **/

    private void displayAllProjects(List<Project> allProjects) {
        Iterator<Project> it = allProjects.iterator();
        Project aProject;
        LOGGER.debug("displayAllProjects:");
        while (it.hasNext()) {
            aProject = (Project) it.next();
            LOGGER.debug("\t" + aProject.getFullName());
        }
    }

    private Project isProjectExist(String projectCode,
                                   Map<String, Project> hmAllProjectsByCode) {
        if (hmAllProjectsByCode.containsKey(projectCode))
            return hmAllProjectsByCode.get(projectCode);
        return null;
    }


    public Project createProject(String projectCode, String projectName,
                                 String customerCode, String customerName,
                                 Map<String, Customer> hmAllCustomerByCode) {

        Project newProject = new Project();
        Customer customer = isCustomerExist(customerCode, hmAllCustomerByCode);
        newProject.setProjectCode(projectCode);
        newProject.setName(projectName);
        if (customer == null) {
            customer = createCustomer(customerCode, customerName, hmAllCustomerByCode);
        }
        newProject.setCustomer(customer);
        projectService.createProject(newProject);
        return newProject;
    }


    //
    // ACTIVITY (Assignment) from eHour
    //

    private List<Activity> getAllAssignedActivities(User assignedUser) {
        List<Activity> allAssignedActivities;
        allAssignedActivities = activityService.getAllActivitiesForUser(assignedUser);
        return allAssignedActivities;
    }

    @Override
    public HashMap<String, Activity> getAllAssignedActivitiesByCode(User assignedUser) {
        HashMap<String, Activity> allAssignedActivitiesByCode = new HashMap<String, Activity>();

        List<Activity> allAssignedActivities = getAllAssignedActivities(assignedUser);

        for (Activity allAssignedActivity : allAssignedActivities) {
            allAssignedActivitiesByCode.put(allAssignedActivity.getCode(), allAssignedActivity);

        }

        return allAssignedActivitiesByCode;
    }


    private void displayAllAssignedActivities(Map<String, Activity> allAssignedActivitiesByCode) {
        Activity theValue;
        if (allAssignedActivitiesByCode != null) {
            LOGGER.debug("\tdisplayAllAssignedActivities from eHour [" + allAssignedActivitiesByCode.size() + "]:");
            for (String theKey : allAssignedActivitiesByCode.keySet()) {
                theValue = allAssignedActivitiesByCode.get(theKey);
                LOGGER.debug("\t" + theValue.getCode() + " --> " + theValue.getFullName() + " [active=" + theValue.getActive() + "/ locked=" + theValue.getLocked() + "]");
            }
        }
    }

    private boolean isAssignedActivityExist(String activityCode, Map<String, Activity> hmAllAssignedActivitiestByCode) {
        LOGGER.debug("\tTest existence of " + activityCode + " : " + hmAllAssignedActivitiestByCode.containsKey(activityCode));
        return hmAllAssignedActivitiestByCode.containsKey(activityCode);
    }


    /**
     *
     * @param assignedUser : nom de l'utilisateur connecte
     * @param activityCode : ida2a2 de l'activite
     * @param activityName : Nom de l activite PJL
     * @param allocatedHours : nombre d'heures allouees PJL
     * @param performedHours : nombre d'heures utilisees PJL
     * @param dateStart : date de demarrage de l'activite PJL
     * @param dateEnd : date de fin de l'activite PJL
     * @param projectCode : ida2a2 du projet
     * @param projectName : Nom du projet PJL
     * @param orgID : ida2a2 de l'organisation PJL
     * @param orgName : Nom de l'org dans PJL
     * @param hmAllAssignedActivitiesByCode : toutes les activites assignees a l'utilisateur de e-Hour
     * @param hmAllCustomersByCode : tous les customers de e-Hour
     * @return
     */
    private Activity createAssignedActivity(User assignedUser, String activityCode, String activityName, Float allocatedHours, Float performedHours,
                                            Date dateStart, Date dateEnd, String projectCode, String projectName, Project project, String orgID, String orgName,
                                            Map<String, Activity> hmAllAssignedActivitiesByCode, Map<String, Customer> hmAllCustomersByCode) throws OverBudgetException {

        if (project == null) {
            project = createProject(projectCode, projectName, orgID, orgName, hmAllCustomersByCode);
        }
        Activity newActivity = new Activity();
        newActivity.setCode(activityCode);
        newActivity.setName(activityName);
        newActivity.setProject(project);
        newActivity.setAssignedUser(assignedUser);
        newActivity.setDateStart(dateStart);
        newActivity.setDateEnd(dateEnd);
        newActivity.setAllottedHours(allocatedHours);

        newActivity=activityService.persistActivity(newActivity);

        LOGGER.debug("performedHours=" + performedHours);
        if (performedHours > 0) {

            // if this activity has been reassigned: in that case this activity already exists for another user
            if (hmAllAssignedActivitiesByCode.containsKey(activityCode)){
                LOGGER.debug("\tThis activity has been reassigned: no need to create virtual timesheet.");


            } else { //  else this activity have hours set only in Windchill
                LOGGER.debug("\tCreating virtual Timesheet because existing performed hours.");
                int tsCreated = createVirtualTimesheet(newActivity, performedHours,assignedUser ) ;
            }

        }

        hmAllAssignedActivitiesByCode.put(activityCode, newActivity);

        return newActivity;
    }


    /**
     *
     * @param activity
     * @param performedHours
     * @param assignedUser
     */
    private int createVirtualTimesheet(Activity activity, Float performedHours, User assignedUser ){
        TimesheetEntry newEntry = new TimesheetEntry();
        TimesheetEntryId entryId = new TimesheetEntryId();
        entryId.setActivity(activity);
        entryId.setEntryDate(activity.getDateStart());
        newEntry.setEntryId(entryId);
        newEntry.setHours(performedHours);

        List<TimesheetEntry> entries = new ArrayList<TimesheetEntry>();
        entries.add(newEntry);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date today = cal.getTime();

        DateRange dateRange = new DateRange(today,today );

        TimesheetComment newTimesheetComment = new TimesheetComment();
        TimesheetCommentId commentid = new TimesheetCommentId();
        commentid.setUserId(assignedUser.getUserId());
        commentid.setCommentDate(today);
        newTimesheetComment.setCommentId(commentid);
        newTimesheetComment.setNewComment(Boolean.TRUE);
        newTimesheetComment.setComment( "ATTENTION: " + Double.toString(performedHours) + " heures issues de ProjectLink pour '"+ activity.getName() + "' au " + today);

        // set ot read only for approval
        // timesheetPersister.validateAndPersist(newActivity , entries, weekRange );

        newEntry.setComment("Mise à jour de ProjectLink");
        LOGGER.debug("VirtualTimesheet created");

        return timesheetPersister.persistTimesheetWeek(entries, newTimesheetComment , dateRange).size();

    }


    /**
     * note:  Les activites non nouvelles ou non mises a jour dans eHour seront desactivees de e-Hour
     *
     * @param assignedUser
     * @param prj
     * @param activityCode
     * @param activityName
     * @param allocatedHours (heures estimees windchill = work hours)
     * @param dateStart
     * @param dateEnd
     * @param hmAllAssignedActivitiesByCode : liste des activites dans ehour
     * @return
     */
    private Activity updateAssignedActivity(User assignedUser, Project prj, String activityCode, String activityName, Float allocatedHours,
                                            Date dateStart, Date dateEnd, Map<String, Activity> hmAllAssignedActivitiesByCode) {

        boolean modified = false;
        Activity activity = hmAllAssignedActivitiesByCode.get(activityCode);

        if (!activity.getName().equals(activityName)) {
            activity.setName(activityName);
            modified = true;
        }
        if (activity.getAssignedUser() != assignedUser) {
            activity.setAssignedUser(assignedUser);
            modified = true;
        }

        if (activity.getDateStart() == null || activity.getDateStart().getTime() != dateStart.getTime()) {
            activity.setDateStart(dateStart);
            modified = true;
        }

        if (activity.getDateEnd() == null || activity.getDateEnd().getTime() != dateEnd.getTime()) {
            activity.setDateEnd(dateEnd);
            modified = true;
        }

        if (activity.getAllottedHours().floatValue() != allocatedHours.floatValue()) {
            activity.setAllottedHours(allocatedHours);
            modified = true;
        }

        if (!activity.getProject().getProjectCode().equals(prj.getProjectCode())) {
            activity.setProject(prj);
            modified = true;
        }

        // getAvailableHours EST TOUJOURS NULL !!!!
        /*
        logger.debug("Check synchro of getAvailableHours: " +  activity.getAvailableHours() );
        if (activity.getAvailableHours() != null ) {
            if (activity.getAvailableHours().compareTo(performedHours)!= 0) {
                logger.debug("\tERROR !!!! : BAD SYNC with 'performedHours' between eHour [" + activity.getAllottedHours() + "] against PJL [" + allocatedHours + "]");
                activity.setLocked(true);
                modified = true;
            }
        }
        */



        if (!activity.getActive()) {
            activity.setActive(true);
            modified = true;
        }

        if (!activity.getLocked()) {
            activity.setLocked(false);
            modified = true;
        }

        if (modified) {
            hmAllAssignedActivitiesByCode.remove(activity.getCode());
            activity = activityService.persistActivity(activity);
            hmAllAssignedActivitiesByCode.put(activity.getCode(), activity);
        }
        return activity;
    }


    private Double getActivityHours(Activity activity) {
        ActivityAggregateReportElement aggregateReport = reportAggregatedDao.getCumulatedHoursForActivity(activity);
        Number number = 0;
        if (aggregateReport != null) number = aggregateReport.getHours() ;
        return number.doubleValue();

    }

    private void displayHashMapContent(HashMap<String, Comparable> hm) {
        if (LOGGER.isDebugEnabled() && hm != null) {
            for (String theKey : hm.keySet()) {
                String theValue;
                String theClass = "null";
                if (hm.get(theKey) != null) theClass = hm.get(theKey).getClass().toString();
                if (hm.get(theKey) instanceof String) theValue = (String) hm.get(theKey);
                else if (hm.get(theKey) instanceof Date)
                    theValue = DateUtils.convertDateToString((Date) hm.get(theKey), WindchillConst.WIND_DATE_FORMAT);
                else theValue = "?";
                LOGGER.debug("\t" + theKey + " = " + theValue + " [" + theClass + "]");

            }
        }
    }
}
