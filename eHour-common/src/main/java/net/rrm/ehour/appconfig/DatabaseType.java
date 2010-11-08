package net.rrm.ehour.appconfig;

public enum DatabaseType
{
	DERBY("derby"),
	MYSQL("mysql"),
	POSTGRESQL("postgresql")
	;
	
	private String databaseName;
	
	private DatabaseType(String databaseName)
	{
		this.databaseName = databaseName;
	}
	
	public String getDatabaseName()
	{
		return databaseName;
	}
	
	public boolean isDatabase(String database)
	{
		return databaseName.equalsIgnoreCase(database);
	}
}
