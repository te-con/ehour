/**
 * Created on Aug 31, 2007
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

package net.rrm.ehour.ui.panel.admin;

import net.rrm.ehour.ui.ajax.AjaxEvent;
import net.rrm.ehour.ui.ajax.AjaxEventType;
import net.rrm.ehour.ui.ajax.GenericAjaxEventType;
import net.rrm.ehour.ui.ajax.PayloadAjaxEvent;
import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.panel.BaseAjaxPanel;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

/**
 * Default impl of awarecontainer + panel which calls the Page to handle the 
 * ajax request
 **/

public abstract class AbstractAjaxAwareAdminPanel extends BaseAjaxPanel
{
	private static final long serialVersionUID = 1L;
	private	static final Logger	logger = Logger.getLogger(AbstractAjaxAwareAdminPanel.class);


	public AbstractAjaxAwareAdminPanel(String id)
	{
		super(id);
	}	
	
	public AbstractAjaxAwareAdminPanel(String id, IModel model)
	{
		super(id, model);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxEventReceived(net.rrm.ehour.ui.ajax.AjaxEvent)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		if (ajaxEvent.getEventType() == GenericAjaxEventType.SUBMIT_ERROR)
		{
			return processFormSubmitError(ajaxEvent.getTarget());
		}
		else if (ajaxEvent instanceof PayloadAjaxEvent)
		{
			PayloadAjaxEvent<AdminBackingBean> payloadEvent = (PayloadAjaxEvent<AdminBackingBean>)ajaxEvent;
			
			AdminBackingBean backingBean = payloadEvent.getPayload();
			AjaxEventType type = ajaxEvent.getEventType();
			AjaxRequestTarget target = ajaxEvent.getTarget();
			
			try
			{
				processFormSubmit(target, backingBean, type);
				
			} catch (Exception e)
			{
				logger.error("While trying to persist/delete", e);
				backingBean.setServerMessage(getLocalizer().getString("general.saveError", this));
				target.addComponent(this);
				
				return false;
			}
		}
		
		return true;
	}	
	
	/**
	 * Process form submit
	 * @param backingBean
	 * @param type
	 * @throws Exception
	 */
	protected void processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception
	{
		
	}

	/**
	 * Process form submit error (validation)
	 * @param target
	 */
	protected boolean processFormSubmitError(AjaxRequestTarget target)
	{
		return false;
	}
}
