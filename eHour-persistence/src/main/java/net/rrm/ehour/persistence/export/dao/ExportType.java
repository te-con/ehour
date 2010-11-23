package net.rrm.ehour.persistence.export.dao;

import net.rrm.ehour.domain.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 2:35:50 AM
 */
public enum ExportType
{
    USER_DEPARTMENT(UserDepartment.class, 0),
    USER_ROLE(UserRole.class, 1),
    USERS(User.class, "USERLIST", 2),
    CUSTOMER(Customer.class, 3),
    PROJECT(Project.class, 4),
    PROJECT_ASSIGNMENT_TYPE(ProjectAssignmentType.class, 5),
    PROJECT_ASSIGNMENT(ProjectAssignment.class, 6),
    TIMESHEET_ENTRY(TimesheetEntry.class, "TIMESHEET_ENTRIES", 7),
    TIMESHEET_COMMENT(TimesheetComment.class, 8),
    AUDIT(Audit.class, 9),
    USER_TO_USERROLE(10);

    private String parentName;
    private Class<? extends DomainObject<?, ?>> domainObjectClass;
    private int order;

    private ExportType(int order)
    {
        this(null, order);
    }
    
    private ExportType(Class<? extends DomainObject<?, ?>> domainObjectClass, int order)
    {
        this.domainObjectClass = domainObjectClass;
        this.parentName = name() + "S";
        this.order = order;
    }

    private ExportType(Class<? extends DomainObject<?, ?>> domainObjectClass, String parentName, int order)
    {
        this.domainObjectClass = domainObjectClass;
        this.parentName = parentName;
        this.order = order;
    }

    public String getParentName()
    {
        return parentName;
    }

    public Class<? extends DomainObject<?, ?>> getDomainObjectClass()
    {
        return domainObjectClass;
    }

    public int getOrder()
    {
        return order;
    }

    public static List<ExportType> orderedValues()
    {
        List<ExportType> types = Arrays.asList(ExportType.values());

        Collections.sort(types, new Comparator<ExportType>()
        {
            @Override
            public int compare(ExportType o1, ExportType o2)
            {
                return o1.order - o2.order;
            }
        });

        return types;
    }
}
