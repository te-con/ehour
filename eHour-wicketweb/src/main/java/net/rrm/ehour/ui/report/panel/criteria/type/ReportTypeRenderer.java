/**
 * Created on Jan 20, 2008
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

package net.rrm.ehour.ui.report.panel.criteria.type;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.ResourceModel;

/**
 * Renderer which looks up a resource key
 **/

public class ReportTypeRenderer implements IChoiceRenderer
{
	private static final long serialVersionUID = 1L;

	public Object getDisplayValue(Object object)
	{
		return new  ResourceModel(object.toString()).getObject();
	}

	public String getIdValue(Object object, int index)
	{
		return Integer.toString(index);
	}
}
