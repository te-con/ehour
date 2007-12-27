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

package net.rrm.ehour.ui.page.login;

import net.rrm.ehour.ui.common.BaseUIWicketTester;

import org.apache.wicket.util.tester.FormTester;

/**
 * Tests the login tests
 **/

public class LoginTest extends BaseUIWicketTester
{
	public void testLoginPageRender()
	{
		tester.startPage(Login.class);
		tester.assertRenderedPage(Login.class);
		tester.assertNoErrorMessage();

		FormTester form = tester.newFormTester("loginform");
		form.setValue("username", "thies");
		form.setValue("password", "Ttst");
		// FIXME: https://issues.apache.org/jira/browse/WICKET-861?page=com.atlassian.jira.plugin.system.issuetabpanels:all-tabpanel
//		form.submit();

		
	}
}
