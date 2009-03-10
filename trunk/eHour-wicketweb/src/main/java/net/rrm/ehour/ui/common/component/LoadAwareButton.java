/**
 * Created on Feb 17, 2009
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

package net.rrm.ehour.ui.common.component;

import net.rrm.ehour.ui.common.ajax.LoadingSpinnerDecorator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.form.Form;

/**
 * AjaxFallbackButton which adds the loading rotating anim when loading
 * and adds the form to the AjaxRequestTarget on validation errors
 * @author Thies
 *
 */
public abstract class LoadAwareButton extends AjaxFallbackButton
{
	private static final long serialVersionUID = -6504165692150025275L;

	public LoadAwareButton(String id, Form form)
	{
		super(id, form);
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator()
	{
		return new LoadingSpinnerDecorator();
	}

	@Override
	protected void onError(AjaxRequestTarget target, Form form)
	{
		target.addComponent(form);
	}
}
