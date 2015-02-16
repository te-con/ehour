package net.rrm.ehour;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class ServerPropertiesConfiguratorTest {
    @Test
    public void shouldLoadDefaultConfig() throws IOException {
        ServerPropertiesConfigurator configurator = new ServerPropertiesConfigurator();
        ServerConfig config = configurator.configureFromProperties("src/test/resources/home/conf/ehour.properties.example");

        assertEquals(8000, config.getPort());
    }
}
