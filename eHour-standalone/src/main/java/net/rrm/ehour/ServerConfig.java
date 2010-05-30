package net.rrm.ehour;

public class ServerConfig
{
	private int port;
	private String defaultConfigFileName;
	private String dataBase;
	private String dataBaseDriver;
	private String dataBaseURL;
	private String dataBaseUsername;
	private String dataBasePassword;
	
	public ServerConfig() {
		port = 8000;
		defaultConfigFileName = "jetty.xml";
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
		this.defaultConfigFileName = defaultConfigFileName;
		return this;
	}
	
	public ServerConfig setDataBase(String dataBase)
	{
		this.dataBase = dataBase;
		return this;
	}
	
	public String getDataBase()
	{
		return dataBase;
	}

	public String getDefaultConfigFileName()
	{
		return defaultConfigFileName;
	}
	
	public int getPort()
	{
		return port;
	}

	public String getDataBaseDriver()
	{
		return dataBaseDriver;
	}

	public ServerConfig setDataBaseDriver(String dataBaseDriver)
	{
		this.dataBaseDriver = dataBaseDriver;
		return this;
	}

	public String getDataBaseURL()
	{
		return dataBaseURL;
	}

	public ServerConfig setDataBaseURL(String dataBaseURL)
	{
		this.dataBaseURL = dataBaseURL;
		return this;
	}

	public String getDataBaseUsername()
	{
		return dataBaseUsername;
	}

	public ServerConfig setDataBaseUsername(String dataBaseUsername)
	{
		this.dataBaseUsername = dataBaseUsername;
		return this;
	}

	public String getDataBasePassword()
	{
		return dataBasePassword;
	}

	public ServerConfig setDataBasePassword(String dataBasePassword)
	{
		this.dataBasePassword = dataBasePassword;
		return this;
	}
	
}
