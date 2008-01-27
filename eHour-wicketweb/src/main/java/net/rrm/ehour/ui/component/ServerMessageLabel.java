/**
 * Created on Aug 21, 2007
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

package net.rrm.ehour.ui.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.time.Duration;

/**
 * ServerMessage label which disappears after 5 seconds and is not visible when no content is provided
 * @author Thies
 *
 */
public class ServerMessageLabel extends Label
{
	private static final long serialVersionUID = -6276174722682301972L;
	private boolean overrideVisibility = false;
	
	public ServerMessageLabel(String id, String cssClass)
	{
		this(id, cssClass, null);
	}
	
	public ServerMessageLabel(String id, String cssClass, IModel model)
	{
		super(id, model);

		add(new SimpleAttributeModifier("class", cssClass));
		setOutputMarkupId(true);

		add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5))
		{
			private static final long serialVersionUID = 3340397062856229947L;

			@Override
			protected void onPostProcessTarget(AjaxRequestTarget target)
			{
				target.addComponent(ServerMessageLabel.this);
				overrideVisibility = true;
			}
		});		
	}

	public boolean isVisible()
	{
		return overrideVisibility ? false : getModel() != null && getModel().getObject()!= null;
	}		
}