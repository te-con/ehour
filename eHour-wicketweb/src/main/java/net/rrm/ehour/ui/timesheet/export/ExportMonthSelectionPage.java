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

package net.rrm.ehour.ui.timesheet.export;

import java.util.ArrayList;
import java.util.Calendar;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.event.AjaxEventListener;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.calendar.CalendarAjaxEventType;
import net.rrm.ehour.ui.common.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.common.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.page.AbstractReportPage;
import net.rrm.ehour.ui.timesheet.export.criteria.ExportCriteriaPanel;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.Component;
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
public class ExportMonthSelectionPage extends AbstractReportPage implements AjaxEventListener
{
	private static final long serialVersionUID = 1891959724639181159L;
	
	private static final String ID_SELECTION_FORM = "selectionForm";
	private static final String ID_FRAME = "printSelectionFrame";
	private static final String ID_BLUE_BORDER = "blueBorder";
	
	private Label			titleLabel;
	
	public ExportMonthSelectionPage()
	{	
		this(EhourWebSession.getSession().getNavCalendar());
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
		
		add(new ContextualHelpPanel("contextHelp", "printMonth.help.header", "printMonth.help.body", "Export+month"));
		
		CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder(ID_FRAME, titleLabel);
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder(ID_BLUE_BORDER);
		greyBorder.add(blueBorder);
		add(greyBorder);
		
		blueBorder.add(createExportCriteriaPanel(ID_SELECTION_FORM));
	}
	
	private ExportCriteriaPanel createExportCriteriaPanel(String id)
	{
		ExportCriteriaPanel criteriaPanel = new ExportCriteriaPanel(id, getDefaultModel());
		
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
		
		reportCriteria.getUserCriteria().setReportRange(DateUtil.getDateRangeForMonth(forMonth));
		
		if (reportCriteria.getUserCriteria().getProjects() == null)
		{
			reportCriteria.getUserCriteria().setProjects(new ArrayList<Project>());
		}
		
		IModel	model = new CompoundPropertyModel(reportCriteria);
		setDefaultModel(model);
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
				new Object[]{new DateModel(cal, getConfig(), DateModel.DATESTYLE_MONTHONLY)}));
	}
}
