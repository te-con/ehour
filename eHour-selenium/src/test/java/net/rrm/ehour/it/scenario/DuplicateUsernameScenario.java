package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.loginAdmin;
import static net.rrm.ehour.it.driver.UserManagementDriver.*;

public class DuplicateUsernameScenario extends AbstractScenario {
    @Test
    public void shouldAttemptToCreateUserWithDuplicateUsername() {
        loginAdmin();
        String lastName = "Edeling3";

        loadUserManagement();
        createUser(new ItUser("thies3", "a"), lastName, "User");
        assertUserDataSaved();

        loadUserManagement();
        createUser(new ItUser("thies3", "a"), lastName, "User");
        assertUserServerMessage("Username already in use");
    }
}
