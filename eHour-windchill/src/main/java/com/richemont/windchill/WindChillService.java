package com.richemont.windchill;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 3/14/11 - 4:11 PM
 */
public interface WindChillService
{
    void checkUserExist(String username);

    void initDataForUser(String username, HttpServletRequest request);

    List<Customer> getCustomersFromEhour();

    List<ProjectAssignment> getProjectsFromEhour(User user);

    List<ProjectAssignment> getAssignmentsFromEhours(int projectID, User userID);

    Float getBookedHours(ProjectAssignment assignement, User user);

    void updateAssignments(String username, HttpServletRequest r);
}
