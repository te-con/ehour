package net.rrm.ehour.domain;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 15, 2010 - 11:56:16 PM
 */
public class DomainObjects
{
    public static final Class[] DOMAIN_OBJECTS = new Class[]{User.class, UserDepartment.class, UserRole.class,
                                                             Configuration.class, BinaryConfiguration.class, Audit.class,
                                                            Customer.class, Project.class, ProjectAssignment.class, ProjectAssignmentType.class,
                                                            MailLog.class, MailLogAssignment.class, MailType.class,
                                                            TimesheetEntry.class, TimesheetComment.class
    };
}
