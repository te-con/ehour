package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.WicketBy;
import org.junit.Test;
import org.openqa.selenium.support.ui.Select;

import static net.rrm.ehour.it.EhourApplicationDriver.Login;

public class BookHourAndExportScenario extends AbstractScenario {
    @Test
    public void should_book_hours() {
        Login();

        Driver.get(BASE_URL + "/eh/admin/employee");

        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.username")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.username")).sendKeys("thies");
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.firstName")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.firstName")).sendKeys("Thies");
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.lastName")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.lastName")).sendKeys("Edeling");
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_email_user.email")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_email_user.email")).sendKeys("thies@te-con.nl");
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_password")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_password")).sendKeys("a");
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_confirmPassword")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_confirmPassword")).sendKeys("a");
        new Select(Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.userDepartment"))).selectByVisibleText("Internal");
        new Select(Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.userRoles"))).selectByVisibleText("User");

        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_submitButton")).click();
    }
}
