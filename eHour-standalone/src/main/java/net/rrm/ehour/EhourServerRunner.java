package net.rrm.ehour;

import org.apache.log4j.Logger;

/**
 * 
 * @author thies
 *
 */
public class EhourServerRunner
{
	private final static Logger LOGGER = Logger.getLogger(EhourServerRunner.class);
	
	public static void main(String[] args) {
		String filename = args != null && args.length >= 1 ? args[0] : "ehour.properties";
		
		ServerPropertiesConfigurator configuration = new ServerPropertiesConfigurator();
		try
		{
			ServerConfig config = configuration.configureFromProperties(filename);
			new EhourServer().startServer(config);
		} catch (Exception e)
		{
			LOGGER.error("Failed to start server", e);
		}
	}
}
