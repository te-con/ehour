package net.rrm.ehour.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Feb 7, 2010 3:07:54 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class ProjectMother
{
	public static List<Project> createProjects(int projectCount)
	{
		List<Project> projects = new ArrayList<Project>();
		
		for (int i = 0; i < projectCount; i++)
		{
			projects.add(createProject(i));
		}
		
		return projects;
	}
	
	public static Project createProject(Integer id)
	{
		return createProject(id, CustomerMother.createCustomer());
	}
	
	public static Project createProject(Integer id, Customer customer)
	{
		Project project = new Project(id);
		project.setActive(true);
		project.setProjectCode("aa" + id);
		project.setName("aa" + id);
		project.setCustomer(customer);
        customer.addProject(project);
		return project;
	}
}
