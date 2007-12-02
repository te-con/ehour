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

import junit.framework.TestCase;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;

import org.apache.wicket.spring.injection.annot.test.AnnotApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Base class for wicket unit tests 
 **/

public class BaseUIWicketTester extends TestCase 
{
	protected WicketTester	tester;
	protected AnnotApplicationContextMock	mockContext;
	protected EhourConfig	config;
	protected TestEhourWebApplication webapp;
	
	protected void setUp() throws Exception
	{
		webapp =  new TestEhourWebApplication();
		
		config = new EhourConfigStub();
		
		mockContext = new AnnotApplicationContextMock();
		mockContext.putBean("EhourConfig", config);

		tester = new EhourWicketTester(webapp);
		
		mockContext = ((TestEhourWebApplication)tester.getApplication()).getMockContext();
	}
}
