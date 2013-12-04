package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.login;
import static net.rrm.ehour.it.driver.ProjectManagementDriver.createProjectWithRegularUserAsPM;
import static net.rrm.ehour.it.driver.ProjectManagementDriver.navigateToPm;
import static net.rrm.ehour.it.driver.UserManagementDriver.ItUser;

public class ProjectManagementScenario  extends AbstractScenario {
    @Test
    public void access_pm_report() {
        ItUser pm = createProjectWithRegularUserAsPM();

        login(pm);
        navigateToPm();
    }
}