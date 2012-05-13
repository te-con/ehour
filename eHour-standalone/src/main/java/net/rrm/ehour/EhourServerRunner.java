package net.rrm.ehour;

import net.rrm.ehour.appconfig.EhourHomeUtil;

public class EhourServerRunner {

    public static void main(String[] args) throws Exception {
        EhourHomeFinder.fixEhourHome();

        System.err.println(EhourHomeUtil.getEhourHome());

        String filename = args != null && args.length >= 1 ? args[0] : "${EHOUR_HOME}/conf/ehour.properties";

        new EhourServer().start(filename);
    }
}
