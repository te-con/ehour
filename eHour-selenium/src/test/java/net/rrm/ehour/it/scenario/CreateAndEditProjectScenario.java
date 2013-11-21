package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Test;

import static net.rrm.ehour.it.driver.CustomerManagementDriver.createActiveCustomer;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.loginAdmin;
import static net.rrm.ehour.it.driver.ProjectDriver.*;

public class CreateAndEditProjectScenario extends AbstractScenario {
    @Test
    public void create_and_edit_project() {
        loginAdmin();

        createActiveCustomer();

        createActiveProjectForActiveCustomer();

        loadProjectAdmin();

        showOnlyActiveProjects();

        editProject("Custom");

        assertProjectLoaded("Custom");

    }
}
