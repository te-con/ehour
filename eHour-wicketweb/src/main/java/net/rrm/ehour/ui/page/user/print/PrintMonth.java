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

package net.rrm.ehour.ui.page.user.print;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.sort.ProjectAssignmentComparator;
import net.rrm.ehour.ui.util.AuthUtil;
import net.rrm.ehour.util.DateUtil;

import org.apache.commons.lang.IncompleteArgumentException;
import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Print month page
 **/
@AuthorizeInstantiation("ROLE_CONSULTANT")
public class PrintMonth extends BasePage
{
	private static final long serialVersionUID = 1891959724639181159L;
	private	static final Logger logger = Logger.getLogger(PrintMonth.class);
	
	@SpringBean
	private ProjectService	projectService;
	
	/**
	 * 
	 */
	public PrintMonth()
	{	
		this(new PageParameters());
	}
	
	/**
	 * 
	 */
	public PrintMonth(PageParameters parameters)
	{
		super(new ResourceModel("printMonth.title"), null);
		
		// add calendar panel
		CalendarPanel calendarPanel = new CalendarPanel("sidePanel", 
														getEhourWebSession().getUser().getUser(), 
														false);
		add(calendarPanel);
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp"));
		
		DateRange	printRange = getPrintRange(parameters);

		GreyRoundedBorder greyBorder = new GreyRoundedBorder("printSelectionFrame", new ResourceModel("printMonth.title"));
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueBorder");
		greyBorder.add(blueBorder);
		add(greyBorder);
	}
	
	private void addAssignments(WebMarkupContainer parent, DateRange printRange)
	{
		List<ProjectAssignment>	assignments = getAssignments(printRange);
		
		ListView view = new ListView("assignments", assignments)
		{
			@Override
			protected void populateItem(ListItem item)
			{
				ProjectAssignment assignment = (ProjectAssignment)item.getModelObject();
				
				item.add(new Label("assignment", assignment.getProject().getFullName()));
			}
			
		};
	}
	
	
	/**
	 * Get assignments
	 * @param printRange
	 * @return
	 */
	private List<ProjectAssignment> getAssignments(DateRange printRange)
	{
		Set<ProjectAssignment>	projectAssignments;
		List<ProjectAssignment>	sortedAssignments;
		
		projectAssignments = projectService.getProjectsForUser(AuthUtil.getUser().getUserId(), printRange);
		
		sortedAssignments = new ArrayList<ProjectAssignment>(projectAssignments);
		
		Collections.sort(sortedAssignments, new ProjectAssignmentComparator());

		return sortedAssignments;
	}
	

	/**
	 * Get the print range
	 * @param parameters
	 * @return
	 */
	private DateRange getPrintRange(PageParameters parameters)
	{
		Calendar cal;
		
		try
		{
			cal = parseParameters(parameters);
		}
		catch (IncompleteArgumentException iae)
		{
			logger.debug("Print page did not have proper parameters. Defaulting to current month", iae);
			
			cal = new GregorianCalendar();
		}
		
		return DateUtil.getDateRangeForMonth(cal);
	}
	
	/**
	 * Parse parameters.
	 * @param parameters should contain a monthYear with format mm-yyyy
	 * @return
	 * @throws IncompleteArgumentException
	 */
	private Calendar parseParameters(PageParameters parameters) throws IncompleteArgumentException
	{
		String 		params = parameters.getString("monthYear");
		Calendar	reqMonth;
		
		if (params == null)
		{
			throw new IncompleteArgumentException("No parameters provided");
		}
			
		String splitted[] = params.split("-");
		
		if (splitted.length != 2)
		{
			throw new IncompleteArgumentException("Invalid parameters provided: " + params);
		}
		
		try
		{
			int month = Integer.parseInt(splitted[0]);
			int year = Integer.parseInt(splitted[1]);
			
			reqMonth = new GregorianCalendar();
			reqMonth.set(Calendar.MONTH, month - 1);
			reqMonth.set(Calendar.YEAR, year);
		}
		catch (NumberFormatException nfe)
		{
			throw new IncompleteArgumentException("Invalid parameters provided: " + params);
		}
		
		return reqMonth;
	}

}
