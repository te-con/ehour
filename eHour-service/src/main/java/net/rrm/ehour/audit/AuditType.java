package net.rrm.ehour.audit;


public enum AuditType
{
	NONE("NONE"),
	WRITE("WRITE"),
	ALL("ALL");
	

	private String value;

	AuditType(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	public static AuditType fromString(String value)
	{
		if (NONE.getValue().equalsIgnoreCase(value))
		{
			return NONE;
		} else if (WRITE.getValue().equalsIgnoreCase(value))
		{
			return WRITE;
		} else 
		{
			return ALL;
		} 
	}	
}
