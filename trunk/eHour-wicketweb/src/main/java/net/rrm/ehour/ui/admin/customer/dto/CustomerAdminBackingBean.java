/**
 * Created on Aug 20, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.admin.customer.dto;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl;

/**
 * Backing bean for customer administration
 **/

public class CustomerAdminBackingBean extends AdminBackingBeanImpl
{
	private static final long serialVersionUID = 343538274642620123L;
	private	Customer	customer;
	
	private String 	originalCustomerName;
	private String	originalCustomerCode;
	
	public CustomerAdminBackingBean(Customer customer)
	{
		this.customer = customer;
		
		this.originalCustomerCode = customer.getCode();
		this.originalCustomerName = customer.getName();
	}
	
	/**
	 * Create fresh new customer admin backing bean
	 * @return
	 */
	public static CustomerAdminBackingBean createCustomerAdminBackingBean()
	{
		final Customer customer = new Customer();
		customer.setActive(true);
		
		return new CustomerAdminBackingBean(customer);
	}	


	public Customer getCustomer()
	{
		return customer;
	}

	public void setCustomer(Customer customer)
	{
		this.customer = customer;
	}


	/**
	 * @return the originalCustomerName
	 */
	public String getOriginalCustomerName()
	{
		return originalCustomerName;
	}


	/**
	 * @param originalCustomerName the originalCustomerName to set
	 */
	public void setOriginalCustomerName(String originalCustomerName)
	{
		this.originalCustomerName = originalCustomerName;
	}


	/**
	 * @return the originalCustomerCode
	 */
	public String getOriginalCustomerCode()
	{
		return originalCustomerCode;
	}


	/**
	 * @param originalCustomerCode the originalCustomerCode to set
	 */
	public void setOriginalCustomerCode(String originalCustomerCode)
	{
		this.originalCustomerCode = originalCustomerCode;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.model.AdminBackingBean#getDomainObject()
	 */
	public Customer getDomainObject()
	{
		return getCustomer();
	}
}
