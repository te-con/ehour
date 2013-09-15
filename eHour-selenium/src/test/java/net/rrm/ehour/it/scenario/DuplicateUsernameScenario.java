package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.loginAdmin;
import static net.rrm.ehour.it.driver.UserManagementApplicationDriver.*;

public class DuplicateUsernameScenario extends AbstractScenario {
    @Test
    public void should_attempt_to_create_user_with_duplicate_username() {
        loginAdmin();
        String lastName = "Edeling3";
        createUser("thies3", "a", lastName);
        assertUserDataSaved();

        createUser("thies3", "a", lastName);
        assertUserServerMessage("Username already in use");
    }
}
