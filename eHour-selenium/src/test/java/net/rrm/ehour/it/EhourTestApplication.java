package net.rrm.ehour.it;

import net.rrm.ehour.EhourServerRunner;

public class EhourTestApplication {
    public static void start() throws Exception {
        System.getProperties().put("EHOUR_HOME", "src/test/resources");
        System.getProperties().put("EHOUR_TEST", "true");

        EhourServerRunner.main(new String[]{});
    }
}
