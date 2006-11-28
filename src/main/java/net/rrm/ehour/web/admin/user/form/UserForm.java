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

package net.rrm.ehour.web.admin.user.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class UserForm extends ActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -726486176963963693L;
	private	boolean	fromForm = false;
	private	String	filterPattern;

	/**
	 * 
	 *
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		fromForm = false;
	}
	/**
	 * @return the filterPattern
	 */
	public String getFilterPattern()
	{
		return filterPattern;
	}

	/**
	 * @param filterPattern the filterPattern to set
	 */
	public void setFilterPattern(String filterPattern)
	{
		this.filterPattern = filterPattern;
	}
	/**
	 * @return the fromForm
	 */
	public boolean isFromForm()
	{
		return fromForm;
	}
	/**
	 * @param fromForm the fromForm to set
	 */
	public void setFromForm(boolean fromForm)
	{
		this.fromForm = fromForm;
	}
}
