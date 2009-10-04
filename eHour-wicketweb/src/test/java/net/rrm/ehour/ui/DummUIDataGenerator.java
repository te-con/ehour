package net.rrm.ehour.ui;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;

public class DummUIDataGenerator
{
	public static Project createProject()
	{
		Project prj = new Project(1);
		prj.setCustomer(createCustomer());
		prj.setActive(true);
		prj.setName("eHour");
		
		return prj;
	}

	public static Customer createCustomer()
	{
		Customer cust = new Customer(1);
		cust.setActive(true);
		cust.setName("TE-CON");
		
		return cust;
	}

}
