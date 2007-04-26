/**
 * Created on Nov 26, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.web.admin.customer.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.web.admin.customer.form.CustomerForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * TODO 
 **/

public class DeleteCustomerAction extends AdminCustomerBaseAction
{
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		CustomerForm	customerForm;
		List<Customer>	customers;
		Customer		customer;
		ActionMessages	messages = new ActionMessages();
		ActionForward	fwd;
		
		customerForm = (CustomerForm)form;
		try
		{
			customerService.deleteCustomer(customerForm.getCustomerId());

			customers = customerService.getCustomers();
			request.setAttribute("customers", customers);
			
			fwd = mapping.findForward("success");
			
		} catch (ParentChildConstraintException e)
		{
			messages.add("delete", new ActionMessage("admin.customer.errorProjectsAssigned"));

			customer = customerService.getCustomer(customerForm.getCustomerId());
			request.setAttribute("customer", customer);
			
			fwd = mapping.findForward("failure");
		}

		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");

		saveErrors(request, messages);
		
		return fwd;
	}
}
