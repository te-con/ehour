/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.common;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.ISpringContextLocator;
import org.apache.wicket.spring.injection.annot.AnnotSpringInjector;
import org.junit.Before;
import org.springframework.context.ApplicationContext;

/**
 * Created on Mar 17, 2009, 5:36:18 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public abstract class AbstractSpringInjectorTester extends AbstractSpringTester
{
	@Before
	public void springLocatorSetup() throws Exception
	{
		super.springContextSetup();
		
		ISpringContextLocator springContextLocator = new ISpringContextLocator()
		{
			private static final long serialVersionUID = 4009835114662176903L;

			public ApplicationContext getSpringContext()
			{
				return mockContext;
			}
			
		};
		
		InjectorHolder.setInjector(new AnnotSpringInjector(springContextLocator));
	}
}
