/**
 * Created on Jul 9, 2007
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

import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.util.tester.WicketTester;

/**
 * TODO 
 **/

public class EhourWicketTester extends WicketTester
{
	public EhourWicketTester(WebApplication webapp)
	{
		super(webapp);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.protocol.http.MockWebApplication#setupRequestAndResponse()
	 */
	public WebRequestCycle setupRequestAndResponse()
	{
		// is ajax
		return setupRequestAndResponse(true);
	}	
}
