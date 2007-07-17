/**
 * Created on Jul 17, 2007
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

package net.rrm.ehour.ui.common;

import java.util.Locale;
import java.util.Map;

import org.apache.wicket.Request;

/**
 * Mock request
 **/

public class MockRequest extends Request
{

	@Override
	public Locale getLocale()
	{
		return Locale.getDefault();
	}

	@Override
	public String getParameter(String key)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map getParameterMap()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getParameters(String key)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPath()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRelativePathPrefixToContextRoot()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRelativePathPrefixToWicketHandler()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getURL()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
