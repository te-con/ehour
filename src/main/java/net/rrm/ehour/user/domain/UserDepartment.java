package net.rrm.ehour.user.domain;

// Generated Sep 26, 2006 11:58:17 PM by Hibernate Tools 3.2.0.beta7


/**
 * UserDepartment generated by hbm2java
 */
public class UserDepartment implements java.io.Serializable
{

	// Fields    

	/**
	 * 
	 */
	private static final long serialVersionUID = 7802944013593352L;

	/**
	 * @uml.property  name="departmentId"
	 */
	private Integer departmentId;

	/**
	 * @uml.property  name="name"
	 */
	private String name;

	/**
	 * @uml.property  name="code"
	 */
	private String code;

	// Constructors

	/** default constructor */
	public UserDepartment()
	{
	}

	/** minimal constructor */
	public UserDepartment(Integer departmentId, String name, String code)
	{
		this.departmentId = departmentId;
		this.name = name;
		this.code = code;
	}


	// Property accessors
	/**
	 * @return  the departmentId
	 * @uml.property  name="departmentId"
	 */
	public Integer getDepartmentId()
	{
		return this.departmentId;
	}

	/**
	 * @param departmentId  the departmentId to set
	 * @uml.property  name="departmentId"
	 */
	public void setDepartmentId(Integer departmentId)
	{
		this.departmentId = departmentId;
	}

	/**
	 * @return  the name
	 * @uml.property  name="name"
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @param name  the name to set
	 * @uml.property  name="name"
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return  the code
	 * @uml.property  name="code"
	 */
	public String getCode()
	{
		return this.code;
	}

	/**
	 * @param code  the code to set
	 * @uml.property  name="code"
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

}
