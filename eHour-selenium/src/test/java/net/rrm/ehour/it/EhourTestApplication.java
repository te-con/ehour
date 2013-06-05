package net.rrm.ehour.it;

import net.rrm.ehour.EhourServer;
import net.rrm.ehour.EhourServerRunner;

public class EhourTestApplication {
    public static EhourServer start() throws Exception {
        System.getProperties().put("EHOUR_HOME", "src/test/resources");
        System.getProperties().put("EHOUR_TEST", "true");

        return EhourServerRunner.startServer(new String[]{});
    }
}
