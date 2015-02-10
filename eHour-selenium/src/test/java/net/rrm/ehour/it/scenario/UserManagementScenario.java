package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.driver.ItUtil;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.loginAdmin;
import static net.rrm.ehour.it.driver.UserManagementDriver.*;
import static org.junit.Assert.assertTrue;

public class UserManagementScenario extends AbstractScenario {
    // EHO-339
    @Test
    public void shouldChangeUsername() {
        String lastName = "Edeling2";

        loginAdmin();

        createUser(new ItUser("thies2", "a"), lastName, "User");
        assertUserDataSaved();

        loadUserManagement();
        showOnlyActiveUsers();
        editUser(lastName);

        waitForUsernameToBe("thies2");

        setFormFieldTo("username", "thies3");
        submitUserForm();

        assertUserDataSaved();
    }

    @Test
    public void shouldMoveToAssignmentsAfterCreatingUserWhenTickingShowAssignments() {
        loginAdmin();

        loadUserManagement();
        fillUserForm(new ItUser("thies2", "a"), "Edeling2", "User");
        tickShowAssignments();
        submitUserForm();

        assertTrue(ItUtil.findElement("assignmentPanel_assignmentList_border_greyTabTitle").getText().startsWith("Project"));
    }

    @Test
    public void shouldStayInUserAdminAfterCreatingUserWithoutTickingShowAssignments() {
        loginAdmin();

        loadUserManagement();
        fillUserForm(new ItUser("thies2", "a"), "Edeling2", "User");
        submitUserForm();

        assertTrue(ItUtil.findElement("userSelection_border_greyTabTitle").getText().startsWith("User"));
    }
}
