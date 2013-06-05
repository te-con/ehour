package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Test;

import static net.rrm.ehour.it.EhourApplicationDriver.createUser;
import static net.rrm.ehour.it.EhourApplicationDriver.login;

public class BookHourAndExportScenario extends AbstractScenario {
    @Test
    public void should_book_hours() {
        login();
        createUser();
    }
}
