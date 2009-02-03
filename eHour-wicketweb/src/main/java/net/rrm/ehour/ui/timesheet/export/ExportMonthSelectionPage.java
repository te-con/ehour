/**
 * Created on Sep 30, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
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

package net.rrm.ehour.ui.timesheet.export;

import java.util.Calendar;

import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.page.BasePage;
import net.rrm.ehour.ui.common.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.Session;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * Print month page
 **/
@AuthorizeInstantiation("ROLE_CONSULTANT")
public class ExportMonthSelectionPage extends BasePage
{
	private static final long serialVersionUID = 1891959724639181159L;
	
	private Label			titleLabel;
	
	/**
	 * 
	 */
	public ExportMonthSelectionPage()
	{	
		this(new CompoundPropertyModel(new ExportParameters(DateUtil.getDateRangeForMonth( ((EhourWebSession) Session.get()).getNavCalendar()))));
	}
	
	/**
	 * 
	 */
	public ExportMonthSelectionPage(IModel requestModel)
	{
		super(new ResourceModel("printMonth.title"), requestModel);
		
		// add calendar panel
		CalendarPanel calendarPanel = new CalendarPanel("sidePanel", 
														getEhourWebSession().getUser().getUser(), 
														false);
		add(calendarPanel);
		
		ExportParameters params = (ExportParameters)getModelObject();
		
		titleLabel = getTitleLabel(DateUtil.getCalendar(params.getExportRange().getDateStart()));
		titleLabel.setOutputMarkupId(true);
		
		CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder("printSelectionFrame", titleLabel);
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueBorder");
		greyBorder.add(blueBorder);
		add(greyBorder);
		
		blueBorder.add(new ExportMonthSelectionForm("selectionForm", params));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.page.BasePage#ajaxEventReceived(net.rrm.ehour.ui.common.ajax.AjaxEvent)
	 */
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		// TODO catch calendar update events
		return true;
//		
//		
//		ExportParameters exportParams = new ExportParameters(DateUtil.getDateRangeForMonth(getEhourWebSession().getNavCalendar()));
//		
//		SelectionForm newForm = new SelectionForm("printSelectionForm", getAssignments(exportParams.getExportRange()), exportParams);
//		form.replaceWith(newForm);
//		form = newForm;
//		target.addComponent(newForm);
//
//		Label newLabel = getTitleLabel(getEhourWebSession().getNavCalendar());
//		newLabel.setOutputMarkupId(true);
//		titleLabel.replaceWith(newLabel);
//		titleLabel = newLabel;
//		target.addComponent(newLabel);
	}		
	
	/**
	 * Get title label
	 * @return
	 */
	private Label getTitleLabel(Calendar cal)
	{
		return new Label( "title", new StringResourceModel("printMonth.header", 
				this,  null,
				new Object[]{new DateModel(cal, getEhourConfig(), DateModel.DATESTYLE_MONTHONLY)}));
	}
}
