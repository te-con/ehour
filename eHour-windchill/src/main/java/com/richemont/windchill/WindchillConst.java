package com.richemont.windchill;

import java.text.SimpleDateFormat;

/**
 * @author laurent.linck
 */
public class WindchillConst {

    protected WindchillConst() {
    }

    protected static final boolean DEBUG = true;

    // used in the ProjectConnectionHelper.xml car la reponse soap renvoie VALUE et NAME comme 2 noeuds quelconques
    protected static final String VALUES_PAIR_SEPARATOR = "=";
    protected static final String PROPERTIES_SEPARATOR = "~";

    protected static String IE_TASK_DELEGATE_NAME_TIMESHEET_MGT = "TimeSheetMgt";

    public static final String ORG_ID = "OrgId";
    public static final String ORG_NAME = "OrgName";

    public static final String PROJECT_ID = "ProjectId";
    public static final String PROJECT_NAME = "ProjectName";
    public static final String PROJECT_DESCRIPTION = "ProjectDescription";
    public static final String PROJECT_MANAGER = "projectManager";
    public static final String PROJECT_TEMPLATE = "projectTemplate";
    public static final String PROJECT_TYPE = "projectType";

    public static final String ACTIVITY_ID = "ActivityId";
    public static final String ACTIVITY_START_DATE = "startDate";
    public static final String ACTIVITY_END_DATE = "endDate";
    public static final String ACTIVITY_WORK = "work";                     //  TRAVAIL TOTAL
    public static final String ACTIVITY_PERFORMED_WORK = "performedWork";  // TRAVAIL REEL
    public static final String ACTIVITY_WORK_COST = "workCost";
    public static final String ACTIVITY_NAME = "ActivityName";
    public static final String ACTIVITY_DESC = "ActivityDescription";
    public static final String SUMMARY_ACTIVITIES = "SummaryActivities" ;
    public static final String REMAINING_WORK = "work";  // Float: Remaining Work = projectAllocatedHours
    public static final String PERWORMED_WORK = "performedWork"; // Float: Actual Work = projectPerformedHours
    public static final String IS_SUMMARY = "isSummary";
    public static final String PARENT_ACTIVITY_ID = "parentActivityId";

    protected static String ESTIMATED_EFFORT_CHANGEACTIVITY_IBA_NAME = "";
    protected static String ACTUAL_EFFORT_CHANGEACTIVITY_IBA_NAME = "";

    protected static String IS_CHANGEACTIVITY_EHOUR_IBA_NAME = "";
    protected static String IS_CHANGEACTIVITY_EHOUR_IBA_VALUE = "";

    protected static String WORKITEMS_IN_STATE = "";

    protected static String IS_PROJECT_EHOUR_IBA_VALUE = "";
    protected static String IS_PROJECT_EHOUR_IBA_NAME = "";

    public static final String PJL_DEFAULT_PROJECT_NAME = "TEMP JIRA PROJECT";

    public static final SimpleDateFormat WIND_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    //public final static SimpleDateFormat WIND_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    // eHour DB
    public static final String ACTIVITY_CODE_PREFIX_FOR_PJL = "com.ptc.";   // in Table ACTIVITY Column CODE



}
