package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.driver.UserManagementDriver;
import org.junit.Test;

import static net.rrm.ehour.it.driver.ConfigDriver.checkSplitAdminRole;
import static net.rrm.ehour.it.driver.ConfigDriver.navigateToConfig;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.assertCustomerManagementLoaded;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.*;
import static net.rrm.ehour.it.driver.UserManagementDriver.*;
import static org.junit.Assert.assertEquals;

public class ManagerScenario extends AbstractScenario {

    @Test
    public void should_show_manager_role_in_user_admin_when_split_manager_is_toggled() {
        loginAdmin();

        checkSplitAdminRole();

        loadUserManagement();

        assertEquals(4, getUserRoles().size());
    }

    @Test
    public void with_just_manager_role_the_user_should_not_have_access_to_system_setup() {
        loginAdmin();
        checkSplitAdminRole();
        UserManagementDriver.ItUser user = new UserManagementDriver.ItUser("dummyuser", "pass");
        createUser(user, "Edeling", "Manager");
        logout();

        login(user);
        navigateToConfig();
        assertCustomerManagementLoaded();
    }

}
