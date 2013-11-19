package net.rrm.ehour.domain;

/**
 * Created on Feb 7, 2010 2:35:44 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public abstract class CustomerObjectMother
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
		
		ProjectObjectMother.createProject(1, cust);

		return cust;
	}

}
