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

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.BaseWicketTester;
import org.apache.wicket.util.tester.FormTester;

/**
 * FormTester that checks whether a path exists when setting a value 
 **/

public class StrictFormTester extends FormTester
{
	protected StrictFormTester(String path, Form<?> workingForm, BaseWicketTester wicketTester, boolean fillBlankString)
	{
		super(path, workingForm, wicketTester, fillBlankString);
	}
	
	public void selectMultiple(String formComponentId, int[] indexes)
	{
		checkComponentExistence(formComponentId);
		super.selectMultiple(formComponentId, indexes);
	}

	@Override
	public void setValue(final String formComponentId, final String value)
	{
		// not hyper efficient since the lookup is done twice (one in the super) but heck, it's testing env
		checkComponentExistence(formComponentId);
		super.setValue(formComponentId, value);
	}	

	private void checkComponentExistence(String formComponentId)
	{
		Component component = getForm().get(formComponentId);

		if (component == null)
		{
			throw new WicketRuntimeException(formComponentId + " not found");
		}
		
	}
}
