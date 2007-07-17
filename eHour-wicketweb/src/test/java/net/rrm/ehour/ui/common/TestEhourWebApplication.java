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

import net.rrm.ehour.ui.EhourWebApplication;

import org.apache.wicket.injection.ConfigurableInjector;
import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.ISpringContextLocator;
import org.apache.wicket.spring.injection.annot.AnnotProxyFieldValueFactory;
import org.springframework.context.ApplicationContext;

/**
 * TODO 
 **/

public class TestEhourWebApplication extends EhourWebApplication
{
	private ApplicationContext 	context;

	/**
	 * Get the initialized spring context
	 * @param context
	 */
	public void setSpringContext(ApplicationContext context)
	{
		this.context = context;
	}

	/**
	 * OVerride to provide our mock injector
	 */
	@Override
	protected void springInjection()
	{
		 InjectorHolder.setInjector(new MockSpringInjector());
	}

	/**
	 * Mock spring injector (which isn't a mock at all..)
	 * @author Thies
	 *
	 */
	private class MockSpringInjector extends ConfigurableInjector
	 {
		@Override
		protected IFieldValueFactory getFieldValueFactory()
		{
			return new AnnotProxyFieldValueFactory(new ContextLocator());
		}
	 }
	
	/**
	 * Reuses the provided context
	 * @author Thies
	 *
	 */
	private class ContextLocator implements ISpringContextLocator
	{
		public ApplicationContext getSpringContext()
		{
			return context;
		}

	}	
	 

}
