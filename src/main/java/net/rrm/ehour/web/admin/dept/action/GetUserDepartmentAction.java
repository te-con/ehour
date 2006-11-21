/**
 * Created on Nov 18, 2006
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

package net.rrm.ehour.web.admin.dept.action;

import java.io.StringWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.web.action.BaseAjaxAction;
import net.rrm.ehour.web.admin.dept.form.UserDepartmentForm;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Get user department as XML
 **/

public class GetUserDepartmentAction extends BaseAjaxAction
{
	private	UserService	userService;
	private	Logger		logger = Logger.getLogger(GetUserDepartmentAction.class);
	/**
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}
	
	/**
	 * 
	 */
	public String getXmlContent(ActionForm form, HttpServletRequest request) throws Exception
	{
		UserDepartmentForm	udForm = (UserDepartmentForm)form;
		UserDepartment		userDepartment;
		StringWriter		out;
		Iterator			i;
		User				user;
		String				fullname;
		
		logger.debug("id: " + udForm.getDepartmentId());
		userDepartment = userService.getUserDepartment(udForm.getDepartmentId());

	    out = new StringWriter();
	    
	    StreamResult streamResult = new StreamResult(out);
	    SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
	    TransformerHandler hd = tf.newTransformerHandler();
	    Transformer serializer = hd.getTransformer();
	    serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
	    serializer.setOutputProperty(OutputKeys.INDENT,"yes");

	    hd.setResult(streamResult);
	    hd.startDocument();
	    AttributesImpl atts = new AttributesImpl();

	    atts.addAttribute("", "", "ID", "", userDepartment.getDepartmentId().toString());
	    hd.startElement("", "", "DEPARTMENT", atts);

	    atts.clear();
	    hd.startElement("", "", "NAME", atts);
	    hd.characters(userDepartment.getName().toCharArray(), 0, userDepartment.getName().length());
	    hd.endElement("", "", "NAME");

	    hd.startElement("", "", "CODE", atts);
	    hd.characters(userDepartment.getCode().toCharArray(), 0, userDepartment.getCode().length());
	    hd.endElement("", "", "CODE");

	    hd.startElement("", "", "USERS", atts);

	    i = userDepartment.getUsers().iterator();

	    while (i.hasNext())
	    {
	    	user = (User)i.next();
	    	fullname = user.getFirstName() + " " + user.getLastName();
	    	
			atts.addAttribute("", "", "ID", "CDATA", user.getUserId().toString());

			hd.startElement("", "", "USER", atts);
			hd.characters(fullname.toCharArray(), 0, fullname.length()); 
			hd.endElement("", "", "USER");
		}
	    
		hd.endElement("", "", "USERS");
		hd.endElement("", "", "DEPARTMENT");
		hd.endDocument();

		return out.toString();
	}
}
