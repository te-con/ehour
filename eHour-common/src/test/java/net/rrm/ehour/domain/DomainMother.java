package net.rrm.ehour.domain;

import java.util.List;

/**
 * Created on Feb 9, 2010 5:05:56 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class DomainMother
{
	/**
	 * Creates a full object tree from user department to customer via department -> user -> assignment -> project -> customer
	 * @return
	 */
	public static Customer createCustomer()
	{
		UserDepartment department = UserDepartmentMother.createUserDepartment();
		User user = department.getUsers().iterator().next();
		
		Customer customer = CustomerMother.createCustomer();
		Project project = customer.getProjects().iterator().next();
		
		ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(user, project);
		project.addProjectAssignment(assignment);
		
		return customer;
	}
	
	public static List<Customer> createCustomers()
	{
		return MotherUtil.createMultiple(createCustomer());
	}	
}
