package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Test;

import static net.rrm.ehour.it.driver.AssignmentAdminDriver.assignToProjects;
import static net.rrm.ehour.it.driver.AssignmentAdminDriver.assignmentExists;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.ItCustomer;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.createActiveCustomer;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.loginAdmin;
import static net.rrm.ehour.it.driver.ProjectDriver.*;
import static net.rrm.ehour.it.driver.UserManagementDriver.ItUser;
import static net.rrm.ehour.it.driver.UserManagementDriver.createRegularUser;
import static org.junit.Assert.assertTrue;

public class AssignmentAdminScenario extends AbstractScenario {
    @Test
    public void assign_user_to_multiple_projects_at_once() {
        loginAdmin();

        ItUser user = createRegularUser();

        ItCustomer customer = createActiveCustomer();
        createActiveProjectFor(customer);
        ItProject projectB = new ItProject("Another Project", "AP");
        createProject(projectB, customer, true);

        assignToProjects(user, 0, 1);

        assertTrue(assignmentExists(1));
    }
}
