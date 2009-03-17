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

import net.rrm.ehour.ui.test.StrictWicketTester;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.settings.Settings;
import org.apache.wicket.spring.injection.annot.test.AnnotApplicationContextMock;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;

/**
 * Base class for wicket unit tests 
 **/
@SuppressWarnings("unchecked")
public abstract class AbstractSpringWebAppTester extends AbstractSpringTester
{
	protected WicketTester	tester;
	protected AnnotApplicationContextMock	mockContext;
	protected TestEhourWebApplication webapp;
	
	@Before
	public void setUp() throws Exception
	{
		webapp =  new TestEhourWebApplication(mockContext);
		tester = new StrictWicketTester(webapp);
		
		((Settings)webapp.getApplicationSettings()).addStringResourceLoader(new IStringResourceLoader()
		{

			public String loadStringResource(Component component, String key)
			{
				return key;
			}

			public String loadStringResource(Class clazz, String key, Locale locale, String style)
			{
				return key;
			}
			
		});
	}
	
	protected void setFormValue(FormTester tester, String path, String value)
	{
		Component comp = tester.getForm().get(path);
		
		if (comp != null && (comp instanceof IFormSubmittingComponent || comp instanceof FormComponent))
		{
			tester.setValue(path, value);
		}
		else if (comp == null)
		{
			throw new IllegalArgumentException(path + " component not found");
		}
		else
		{
			throw new IllegalArgumentException(path + " not a formcomponent");
		}
		
	}
}
