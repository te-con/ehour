/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour;

import net.rrm.ehour.appconfig.EhourHomeUtil;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.xml.XmlConfiguration;

import java.io.File;
import java.io.FileInputStream;

/**
 * Start Jetty
 */

public class EhourServer {

    public void start(String configurationFilename) throws Exception {
        ServerPropertiesConfigurator configuration = new ServerPropertiesConfigurator();

        ServerConfig config = configuration.configureFromProperties(replaceSystemEnv(configurationFilename));
        startServer(config);
    }

    private String replaceSystemEnv(String filename) {
        String ehourHome = EhourHomeUtil.getEhourHome();

        if (StringUtils.isBlank(ehourHome)) {
            throw new IllegalArgumentException("EHOUR_HOME is not defined as a environment variable or system property.");
        }

        return filename.replace("${EHOUR_HOME}", ehourHome);
    }

    private void startServer(ServerConfig config) throws Exception {
        Server server = new Server(config.getPort());

        File jettyXmlFile = EhourHomeUtil.getFileInConfDir(config.getDefaultConfigFileName());

        try (FileInputStream stream = new FileInputStream(jettyXmlFile)) {

            XmlConfiguration configuration = new XmlConfiguration(stream);
            configuration.configure(server);

            server.start();

            if (!isInTestMode()) {
                server.join();
            }
        }
    }

    private boolean isInTestMode() {
        return Boolean.parseBoolean(System.getProperty("EHOUR_TEST", "false"));
    }
}
