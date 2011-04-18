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

package net.rrm.ehour.ui.test;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.ITestPageSource;
import org.apache.wicket.util.tester.ITestPanelSource;
import org.apache.wicket.util.tester.WicketTester;

/**
 * WicketTester with some enhancements: 
 *  - uses a StrictFormTester
 *  - WICKET-254 inclusion
 **/

public class StrictWicketTester extends WicketTester
{
	public StrictWicketTester(WebApplication webApplication)
	{
		super(webApplication);
	}
	
	public final Panel startPanelWithHead(final ITestPanelSource testPanelSource)
	{
		return (Panel)startPage(new ITestPageSource()
		{
			private static final long serialVersionUID = 1L;

			public Page getTestPage()
			{
				return new DummyHeadPanelPage(testPanelSource);
			}
		}).get(DummyHeadPanelPage.TEST_PANEL_ID);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.util.getTester().BaseWicketTester#newFormTester(java.lang.String, boolean)
	 */
	@Override
	public FormTester newFormTester(String path, boolean fillBlankString)
	{
		return new StrictFormTester(path, (Form<?>)getComponentFromLastRenderedPage(path), this, fillBlankString);
	}
}
