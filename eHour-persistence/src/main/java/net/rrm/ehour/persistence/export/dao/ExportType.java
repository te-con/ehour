package net.rrm.ehour.persistence.export.dao;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 2:35:50 AM
 */
public enum ExportType
{
    AUDIT,
    CUSTOMER,
    PROJECT,
    PROJECT_ASSIGNMENT,
    PROJECT_ASSIGNMENT_TYPE,
    TIMESHEET_ENTRY("TIMESHEET_ENTRIES"),
    USERS("USERLIST"),
    TIMESHEET_COMMENT,
    USER_ROLE,
    USER_TO_USERROLE;

    private String parentName;

    private ExportType()
    {
        this.parentName = name() + "S";
    }

    private ExportType(String parentName)
    {
        this.parentName = parentName;
    }

    public String getParentName()
    {
        return parentName;
    }
}
