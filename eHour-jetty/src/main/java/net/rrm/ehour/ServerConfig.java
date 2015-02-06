package net.rrm.ehour;

import org.apache.commons.lang.StringUtils;

public class ServerConfig
{
    public static final int DEFAULT_PORT = 8000;
    private int port;
	private String defaultConfigFileName;

	public ServerConfig() {
		port = DEFAULT_PORT;
		defaultConfigFileName = "jetty.xml";
	}

    public int getPort() {
        return port;
    }

    public String getDefaultConfigFileName() {
        return defaultConfigFileName;
    }

    public ServerConfig setPort(Integer port)
	{
		if (port != null) {
			this.port = port;
		}
		
		return this;
	}
	
	public ServerConfig setDefaultConfigFileName(String defaultConfigFileName)
	{
		if (StringUtils.isNotBlank(defaultConfigFileName)) {
			this.defaultConfigFileName = defaultConfigFileName;
		}
		
		return this;
	}

}
