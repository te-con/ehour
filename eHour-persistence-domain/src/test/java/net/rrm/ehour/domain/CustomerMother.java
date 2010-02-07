package net.rrm.ehour.domain;

import java.util.List;

/**
 * Created on Feb 7, 2010 2:35:44 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class CustomerMother
{
	public static Customer createCustomer()
	{
		return createCustomer(1);
	}
	
	public static Customer createCustomer(int customerId)
	{
		Customer cust = new Customer(customerId);
		cust.setName(customerId + "");
		cust.setActive(true);
		cust.setCode(customerId + "");
		
		Project project = ProjectMother.createProject(1, cust);
		cust.addProject(project);
		
		return cust;
	}
	
	public static List<Customer> createCustomers()
	{
		return MotherUtil.createMultiple(createCustomer());
	}

}
