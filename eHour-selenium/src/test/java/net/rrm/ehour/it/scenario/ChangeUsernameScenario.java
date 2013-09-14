package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.loginAdmin;
import static net.rrm.ehour.it.driver.UserManagementApplicationDriver.*;

// EHO-339
public class ChangeUsernameScenario extends AbstractScenario {
    @Test
    public void should_change_username() {
        loginAdmin();
        String lastName = "Edeling2";
        createUser("thies2", "a", lastName);

        editUser(lastName);

        setFormFieldTo("username", "thies3");
        submitForm();

        assertDataSaved();
    }
}
