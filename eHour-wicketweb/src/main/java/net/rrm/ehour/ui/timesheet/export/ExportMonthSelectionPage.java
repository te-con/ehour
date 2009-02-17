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

import java.util.ArrayList;
import java.util.Calendar;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.calendar.CalendarAjaxEventType;
import net.rrm.ehour.ui.common.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.page.AbstractReportPage;
import net.rrm.ehour.ui.timesheet.export.criteria.ExportCriteriaPanel;

import org.apache.wicket.Component;
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
public class ExportMonthSelectionPage extends AbstractReportPage
{
	private static final long serialVersionUID = 1891959724639181159L;
	private static final String ID_SELECTION_FORM = "selectionForm";
	private static final String ID_FRAME = "printSelectionFrame";
	private static final String ID_BLUE_BORDER = "blueBorder";
	
	private Label			titleLabel;
	
	public ExportMonthSelectionPage()
	{	
		this(((EhourWebSession) Session.get()).getNavCalendar());
	}
	
	/**
	 * 
	 */
	public ExportMonthSelectionPage(Calendar forMonth)
	{
		super(new ResourceModel("printMonth.title"));
		
		setCriteriaModel(forMonth);
		
		// add calendar panel
		add(createCalendarPanel("sidePanel"));
		
		titleLabel = getTitleLabel(forMonth);
		titleLabel.setOutputMarkupId(true);
		
		CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder(ID_FRAME, titleLabel);
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder(ID_BLUE_BORDER);
		greyBorder.add(blueBorder);
		add(greyBorder);
		
		blueBorder.add(createExportCriteriaPanel(ID_SELECTION_FORM));
		
	}
	
	private ExportCriteriaPanel createExportCriteriaPanel(String id)
	{
		ExportCriteriaPanel criteriaPanel = new ExportCriteriaPanel(id, getModel());
		
		return criteriaPanel;
	}
	
	private CalendarPanel createCalendarPanel(String id)
	{
		CalendarPanel calendarPanel = new CalendarPanel(id, 
														getEhourWebSession().getUser().getUser(), 
														false);
		
		return calendarPanel;
	}

	private void setCriteriaModel(Calendar forMonth)
	{
		ReportCriteria reportCriteria = getReportCriteria(true);
		
		if (reportCriteria.getUserCriteria().getProjects() == null)
		{
			reportCriteria.getUserCriteria().setProjects(new ArrayList<Project>());
		}
		
		IModel	model = new CompoundPropertyModel(reportCriteria);
		setModel(model);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.page.BasePage#ajaxEventReceived(net.rrm.ehour.ui.common.ajax.AjaxEvent)
	 */
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		if (ajaxEvent.getEventType() == CalendarAjaxEventType.MONTH_CHANGE)
		{
			changeMonth(ajaxEvent);
		}
		return true;
	}

	private void changeMonth(AjaxEvent ajaxEvent)
	{
		setCriteriaModel(getEhourWebSession().getNavCalendar());
		
		Component originalForm = get(ID_FRAME + ":" + ID_BLUE_BORDER + ":" + ID_SELECTION_FORM);
		
		ExportCriteriaPanel replacementPanel = createExportCriteriaPanel(ID_SELECTION_FORM);
		
		originalForm.replaceWith(replacementPanel);
		ajaxEvent.getTarget().addComponent(replacementPanel);

		Label newLabel = getTitleLabel(getEhourWebSession().getNavCalendar());
		newLabel.setOutputMarkupId(true);
		titleLabel.replaceWith(newLabel);
		titleLabel = newLabel;
		ajaxEvent.getTarget().addComponent(newLabel);
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
