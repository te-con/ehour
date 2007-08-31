/**
 * Created on Aug 31, 2007
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

package net.rrm.ehour.ui.ajax;

import org.apache.wicket.ajax.IAjaxCallDecorator;

/**
 * Display demo decorator
 **/

public class DemoDecorator implements IAjaxCallDecorator
{
	private static final long serialVersionUID = 1432993030793501257L;

	private String	demoMsg;
	
	public DemoDecorator(String msg)
	{
		this.demoMsg = msg;
	}
	
	public CharSequence decorateOnFailureScript(CharSequence script)
	{
		return "alert('" + demoMsg + "');";
	}

	public CharSequence decorateOnSuccessScript(CharSequence script)
	{
		return "alert('" + demoMsg + "');";
	}

	public CharSequence decorateScript(CharSequence script)
	{
		return "alert('" + demoMsg + "');";
	}
}
