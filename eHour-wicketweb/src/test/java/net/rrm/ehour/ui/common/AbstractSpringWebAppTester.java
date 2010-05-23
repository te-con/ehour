/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common;

import java.util.Locale;

import net.rrm.ehour.ui.test.StrictWicketTester;

import org.apache.wicket.Component;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.settings.Settings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.junit.Before;

/**
 * Base class for wicket unit tests 
 **/
public abstract class AbstractSpringWebAppTester extends AbstractSpringTester
{
	protected StrictWicketTester tester;
	protected TestEhourWebApplication webApp;
	
	@SuppressWarnings("serial")
	@Before
	public final void setUp() throws Exception
	{
		webApp =  new TestEhourWebApplication()
		{
			@Override
			protected void springInjection()
			{
				addComponentInstantiationListener(new SpringComponentInjector(this, getMockContext(), true));
			}
		};
		
		tester = new StrictWicketTester(webApp);
		
		bypassStringResourceLoading();
	}

	private void bypassStringResourceLoading()
	{
		((Settings)webApp.getApplicationSettings()).addStringResourceLoader(new IStringResourceLoader()
		{

			public String loadStringResource(Component component, String key)
			{
				return key;
			}

			public String loadStringResource(Class<?> clazz, String key, Locale locale, String style)
			{
				return key;
			}
			
		});
	}
	
	protected StrictWicketTester getTester()
	{
		return tester;
	}
	
	protected TestEhourWebApplication getWebApp()
	{
		return webApp;
	};
}
