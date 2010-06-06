package net.rrm.ehour;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author thies
 * 
 */
public class EhourServerRunner
{
	private final static Logger LOGGER = Logger.getLogger(EhourServerRunner.class);

	public static void main(String[] args)
	{
		String filename = args != null && args.length >= 1 ? args[0] : "${ehour.home}/conf/ehour.properties";

		ServerPropertiesConfigurator configuration = new ServerPropertiesConfigurator();
		
		try
		{
			ServerConfig config = configuration.configureFromProperties(replaceSystemEnv(filename));
			new EhourServer().startServer(config);
		} catch (Exception e)
		{
			LOGGER.error("Failed to start server", e);
		}
	}

	private static String replaceSystemEnv(String filename)
	{
		String ehourHome = System.getenv("ehour.home");
		
		if (StringUtils.isBlank(ehourHome)) {
			throw new IllegalArgumentException("ehour.home is not defined as a environment variable");
		}
		
		return filename.replace("${ehour.home}", ehourHome);
	}
}
