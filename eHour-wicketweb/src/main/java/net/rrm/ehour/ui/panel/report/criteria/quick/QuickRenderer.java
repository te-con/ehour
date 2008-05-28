/**
 * Created on Mar 9, 2008
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

package net.rrm.ehour.ui.panel.report.criteria.quick;

import org.apache.wicket.Application;
import org.apache.wicket.Localizer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Quick renderer
 **/

public abstract class QuickRenderer implements IChoiceRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5678351108045640999L;

	/**
	 * Get localizer
	 * @return
	 */
	protected Localizer getLocalizer()
	{
		return Application.get().getResourceSettings().getLocalizer();
	}
}
