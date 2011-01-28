package net.rrm.ehour;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class ServerPropertiesConfiguratorTest
{
	@Test
	public void shouldLoadDefaultConfig() throws IOException
	{
		ServerPropertiesConfigurator configurator = new ServerPropertiesConfigurator();
		ServerConfig config = configurator.configureFromProperties("conf/ehour.properties");
		
		assertEquals(8000, config.getPort());
//		assertEquals("derby", config.getDataBase());
	}
}
