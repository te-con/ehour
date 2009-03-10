/**
 * Created on Feb 18, 2009
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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
	protected StrictFormTester(String path, Form workingForm, BaseWicketTester wicketTester, boolean fillBlankString)
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
