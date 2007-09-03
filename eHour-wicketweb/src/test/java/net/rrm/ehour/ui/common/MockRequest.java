/**
 * Created on Jul 17, 2007
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
