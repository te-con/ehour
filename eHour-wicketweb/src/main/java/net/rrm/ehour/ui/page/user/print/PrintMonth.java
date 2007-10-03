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

import java.io.Serializable;
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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Print month page
 **/
@AuthorizeInstantiation("ROLE_CONSULTANT")
public class PrintMonth extends BasePage
{
	private static final long serialVersionUID = 1891959724639181159L;
	
	@SpringBean
	private ProjectService	projectService;
	
	private SelectionForm	form;
	
	/**
	 * 
	 */
	public PrintMonth()
	{	
		this(new Model(new GregorianCalendar()));
	}
	
	/**
	 * 
	 */
	public PrintMonth(IModel requestModel)
	{
		super(new ResourceModel("printMonth.title"), null);
		
		// add calendar panel
		CalendarPanel calendarPanel = new CalendarPanel("sidePanel", 
														getEhourWebSession().getUser().getUser(), 
														false);
		add(calendarPanel);
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp"));
		
		DateRange printRange = DateUtil.getDateRangeForMonth((Calendar)requestModel.getObject());

		GreyRoundedBorder greyBorder = new GreyRoundedBorder("printSelectionFrame", new ResourceModel("printMonth.title"));
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueBorder");
		greyBorder.add(blueBorder);
		add(greyBorder);
		
		form = new SelectionForm("printSelectionForm", getAssignments(printRange));
		blueBorder.add(form);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.BasePage#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
		DateRange printRange = DateUtil.getDateRangeForMonth(getEhourWebSession().getNavCalendar());
		
		SelectionForm newForm = new SelectionForm("printSelectionForm", getAssignments(printRange));
		form.replaceWith(newForm);
		target.addComponent(newForm);
		form = newForm;
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
	 * 
	 * @author Thies
	 *
	 */
	private class SelectionForm extends Form
	{
		private static final long serialVersionUID = 1L;
		
		private List<AssignmentWrapper>	wrappers;
		private boolean					includeSignOff;
		
		public SelectionForm(String id, List<ProjectAssignment> assignments)
		{
			super(id);
			
			setOutputMarkupId(true);
			wrappers = new ArrayList<AssignmentWrapper>();
			
			for (ProjectAssignment projectAssignment : assignments)
			{
				wrappers.add(new AssignmentWrapper(projectAssignment));
			}
			
			add(new ListView("assignments", wrappers)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem item)
				{
					AssignmentWrapper wrapper = (AssignmentWrapper) item.getModelObject();

					item.add(new Label("project", wrapper.getAssignment().getProject().getFullName()));
					
					Label label = new Label("role", "( " +  wrapper.getAssignment().getRole() + " )");
					label.setVisible(wrapper.getAssignment().getRole() != null && !wrapper.getAssignment().getRole().trim().equals("")); 
					item.add(label);
					item.add(new CheckBox("check", new PropertyModel(wrapper, "selected")));
				}
			});
			
			// signoff
			
			add(new CheckBox("signOff", new PropertyModel(this, "includeSignOff")));
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		public void onSubmit()
		{
			System.out.println("sign: " + isIncludeSignOff());
		}

		/**
		 * @return the includeSignOff
		 */
		public boolean isIncludeSignOff()
		{
			return includeSignOff;
		}

		/**
		 * @param includeSignOff the includeSignOff to set
		 */
		public void setIncludeSignOff(boolean includeSignOff)
		{
			this.includeSignOff = includeSignOff;
		}
	}
	
	/**
	 * 
	 * @author Thies
	 *
	 */
	private class AssignmentWrapper implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		private ProjectAssignment assignment;
		private	boolean	selected;
		
		AssignmentWrapper(ProjectAssignment assignment)
		{
			this.assignment = assignment;
		}


		/**
		 * @return the selected
		 */
		public boolean isSelected()
		{
			return selected;
		}

		/**
		 * @param selected the selected to set
		 */
		public void setSelected(boolean selected)
		{
			this.selected = selected;
		}


		/**
		 * @return the assignment
		 */
		public ProjectAssignment getAssignment()
		{
			return assignment;
		}


		/**
		 * @param assignment the assignment to set
		 */
		public void setAssignment(ProjectAssignment assignment)
		{
			this.assignment = assignment;
		}
		
	}
}
