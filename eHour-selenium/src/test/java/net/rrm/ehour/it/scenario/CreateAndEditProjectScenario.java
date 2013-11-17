package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.createCustomer;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.loginAdmin;
import static net.rrm.ehour.it.driver.ProjectApplicationDriver.*;

public class CreateAndEditProjectScenario extends AbstractScenario {
    @Test
    public void create_and_edit_project() {
        loginAdmin();

        createCustomer("VU", "VU");

        createProject("Custom", "CUST", "VU", "VU");

        loadProjectAdmin();

        showOnlyActiveProjects();

        editProject("Custom");

        assertProjectLoaded("Custom");

    }
}
