package net.rrm.ehour;

import net.rrm.ehour.appconfig.EhourHomeFinder;
import net.rrm.ehour.appconfig.EhourHomeUtil;
import org.apache.log4j.PropertyConfigurator;

public class EhourServerRunner {

    private EhourServerRunner() {
    }

    public static void main(String[] args) throws Exception {
        startServer(args);
    }

    public static EhourServer startServer(String[] args) throws Exception {
        EhourHomeFinder.fixEhourHome();

        // log4j configuring
        PropertyConfigurator.configure(EhourHomeUtil.getFileInConfDir("log4j.properties").getAbsolutePath());

        String filename = args != null && args.length >= 1 ? args[0] : "${EHOUR_HOME}/conf/ehour.properties";

        EhourServer server = new EhourServer();
        server.start(filename);

        return server;
    }


}
