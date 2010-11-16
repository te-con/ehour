package net.rrm.ehour.persistence.export.dao;

import net.rrm.ehour.domain.*;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 2:35:50 AM
 */
public enum ExportType
{
    AUDIT(Audit.class),
    CUSTOMER(Customer.class),
    PROJECT(Project.class),
    PROJECT_ASSIGNMENT(ProjectAssignment.class),
    PROJECT_ASSIGNMENT_TYPE(ProjectAssignmentType.class),
    TIMESHEET_ENTRY(TimesheetEntry.class, "TIMESHEET_ENTRIES"),
    USERS(User.class, "USERLIST"),
    TIMESHEET_COMMENT(TimesheetComment.class),
    USER_ROLE(UserRole.class),
    USER_TO_USERROLE;

    private String parentName;
    private Class<? extends DomainObject<?, ?>> domainObjectClass;

    private ExportType()
    {
        this(null);
    }
    
    private ExportType(Class<? extends DomainObject<?, ?>> domainObjectClass)
    {
        this.domainObjectClass = domainObjectClass;
        this.parentName = name() + "S";
    }

    private ExportType(Class<? extends DomainObject<?, ?>> domainObjectClass, String parentName)
    {
        this.domainObjectClass = domainObjectClass;
        this.parentName = parentName;
    }

    public String getParentName()
    {
        return parentName;
    }

    public Class<? extends DomainObject<?, ?>> getDomainObjectClass()
    {
        return domainObjectClass;
    }
}
