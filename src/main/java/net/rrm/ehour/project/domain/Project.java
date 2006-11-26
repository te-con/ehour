package net.rrm.ehour.project.domain;

public class Project implements java.io.Serializable
{

	// Fields    

	/**
	 * 
	 */
	private static final long serialVersionUID = 6553709211219335091L;

	private Integer projectId;

	private String projectCode;

	private String contact;

	private String description;

	private String name;

	private Boolean defaultProject;

	private Customer customer;

	// Constructors

	/** default constructor */
	public Project()
	{
	}

	/** full constructor */
	public Project(String projectCode, String contact, String description, String name, Boolean defaultProject, Customer customer)
	{
		this.projectCode = projectCode;
		this.contact = contact;
		this.description = description;
		this.name = name;
		this.defaultProject = defaultProject;
		this.customer = customer;
	}

	
	public String getFullname()
	{
		if (projectCode != null && 
			!projectCode.equals(""))
		{
			return projectCode + " - " + name;
		}
		else
		{
			return name;
		}
			
	}	
	
	// Property accessors
	public Integer getProjectId()
	{
		return this.projectId;
	}

	public void setProjectId(Integer projectId)
	{
		this.projectId = projectId;
	}

	public String getProjectCode()
	{
		return this.projectCode;
	}

	public void setProjectCode(String projectCode)
	{
		this.projectCode = projectCode;
	}

	public String getContact()
	{
		return this.contact;
	}

	public void setContact(String contact)
	{
		this.contact = contact;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Boolean getDefaultProject()
	{
		return this.defaultProject;
	}

	public void setDefaultProject(Boolean defaultProject)
	{
		this.defaultProject = defaultProject;
	}

	public Customer getCustomer()
	{
		return this.customer;
	}

	public void setCustomer(Customer customer)
	{
		this.customer = customer;
	}

}
