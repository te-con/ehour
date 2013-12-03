package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.driver.UserManagementDriver;
import org.junit.Test;

import static net.rrm.ehour.it.driver.CustomerManagementDriver.createActiveCustomer;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.loginAdmin;
import static net.rrm.ehour.it.driver.ProjectDriver.*;
import static net.rrm.ehour.it.driver.UserManagementDriver.createRegularUser;
import static net.rrm.ehour.it.driver.UserManagementDriver.createUser;

public class ProjectAdminScenario extends AbstractScenario {
    @Test
    public void create_and_edit_project() {
        loginAdmin();

        createActiveCustomer();

        createActiveProjectForActiveCustomer();

        loadProjectAdmin();

        showOnlyActiveProjects();

        editProject(ACTIVE_PROJECT.name);

        assertProjectLoaded(ACTIVE_PROJECT.name);
    }

    @Test
    public void create_and_assign_one_user_and_delete_it() {
        // setup users, csutomers and project
        loginAdmin();

        createRegularUser();

        createUser(new UserManagementDriver.ItUser("asco", "b"), "Asco", "User");

        createActiveCustomer();

        createActiveProjectForActiveCustomer();

        loadProjectAdmin();

        editProject(ACTIVE_PROJECT.name);

        // assign a user
        newAssignment();

        editUser(0);
        setRateForUser(0, "125");
        submitAssignment(0);

        storeProject();

        // assert it's stored properly
        loadProjectAdmin();
        editProject(ACTIVE_PROJECT.name);

        assertIsActiveAssignment(0);

        // edit it again to delete the assignment
        editUser(0);
        deleteAssignment(0);

        storeProject();

        // assert it's deleted
        loadProjectAdmin();
        editProject(ACTIVE_PROJECT.name);

        assertNoAssignments();
    }
}
