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
import net.rrm.ehour.ui.ajax.PayloadAjaxEvent;
import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.panel.BaseAjaxPanel;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

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

//	/*
//	 * (non-Javadoc)
//	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int)
//	 */
//	public void ajaxRequestReceived(AjaxRequestTarget target, int type)
//	{
//		((AjaxAwareContainer)getPage()).ajaxRequestReceived(target, type);
//		
//	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
//	 */
//	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
//	{
//		AdminBackingBean backingBean = (AdminBackingBean) ((((IWrapModel) params)).getWrappedModel()).getObject();
//		
//		try
//		{
//			processFormSubmit(backingBean, type);
//		
//			postSubmit(target, type, params, backingBean);
//			
//			((AjaxAwareContainer)getPage()).ajaxRequestReceived(target, CommonWebUtil.AJAX_FORM_SUBMIT, backingBean.getDomainObject());
//			
//		} catch (Exception e)
//		{
//			logger.error("While trying to persist/delete", e);
//			backingBean.setServerMessage(getLocalizer().getString("general.saveError", this));
//			target.addComponent(this);
//		}
//	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxEventReceived(net.rrm.ehour.ui.ajax.AjaxEvent)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		PayloadAjaxEvent<IWrapModel> payloadEvent = (PayloadAjaxEvent<IWrapModel>)ajaxEvent;
		
		IWrapModel model  = payloadEvent.getPayload();
		
		AdminBackingBean backingBean = (AdminBackingBean) ((model).getWrappedModel()).getObject();
		AjaxEventType type = ajaxEvent.getEventType();
		AjaxRequestTarget target = ajaxEvent.getTarget();
		
		try
		{
			processFormSubmit(target, backingBean, type);
		
			postSubmit(target, type, model, backingBean);
			
		} catch (Exception e)
		{
			logger.error("While trying to persist/delete", e);
			backingBean.setServerMessage(getLocalizer().getString("general.saveError", this));
			target.addComponent(this);
			
			return false;
		}		
		
		return true;
	}	
	
	/**
	 * Post submit hook
	 * @param success
	 * @param target
	 * @param type
	 * @param params
	 * @param backingBean
	 */
	protected void postSubmit(AjaxRequestTarget target, AjaxEventType type, IModel model, AdminBackingBean backingBean)
	{
		
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
}
