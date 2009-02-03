/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.timesheet.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.common.sort.ProjectAssignmentComparator;
import net.rrm.ehour.ui.common.util.AuthUtil;
import net.rrm.ehour.ui.timesheet.export.ExportParameters.Action;
import net.rrm.ehour.ui.timesheet.export.print.PrintMonth;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created on Feb 3, 2009, 7:50:39 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportMonthSelectionForm extends Form
{
	private static final long serialVersionUID = -1947256964429211340L;

	@SpringBean
	private ProjectService	projectService;
	private List<AssignmentWrapper>	wrappers;
	
	@SuppressWarnings("serial")
	public ExportMonthSelectionForm(String id, final ExportParameters exportParameters)
	{
		super(id, new CompoundPropertyModel(exportParameters));
		
		setOutputMarkupId(true);

		add(getProjectAssignmentList(exportParameters));
		
		// signoff
		add(new CheckBox("signOff", new PropertyModel(this.getModel(), "includeSignoff")));

		// print button
		add(new SubmitLink("submitButton")
		{
			@Override
			public void onSubmit()
			{
				exportParameters.setAction(Action.PRINT);
			}
		});

		// excel button
		add(new SubmitLink("excelButton")
		{
			@Override
			public void onSubmit()
			{
				exportParameters.setAction(Action.EXCEL);
			}
		});
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
	 */
	@Override
	public void onSubmit()
	{
		ExportParameters exportParameters = getExportParameters();
		
		List<Serializable>	assignmentIds = getSelectedAssignmentIds();
		
		exportParameters.setAssignmentIds(assignmentIds);
		
		if (exportParameters.getAction() == Action.EXCEL)
		{
			excelExport(exportParameters);
		}
		else
		{
			printExport(exportParameters);
		}
	}
	
	
	private void excelExport(ExportParameters exportParameters)
	{
	}

	private void printExport(ExportParameters exportParameters)
	{
		setResponsePage(new PrintMonth(exportParameters));
	}

	/**
	 * Get listView of current project assignments
	 * @param exportParameters
	 * @return
	 */
	private ListView getProjectAssignmentList(ExportParameters exportParameters)
	{
		getAssignmentWrappers(exportParameters);
		
		ListView list = new ListView("assignments", wrappers)
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
				CheckBox checkbox = new CheckBox("check", new PropertyModel(wrapper, "selected"));
			item.add(checkbox);
			}
		};
		
		return list;
	}

	/**
	 * Get export params from model
	 * @return
	 */
	private ExportParameters getExportParameters()
	{
		return (ExportParameters)getModelObject();
	}
	
	/**
	 * Get selected assignments
	 * @return
	 */
	private List<Serializable> getSelectedAssignmentIds()
	{
		List<Serializable>	assignmentIds = new ArrayList<Serializable>();
		
		for (AssignmentWrapper wrapper : wrappers)
		{
			if (wrapper.isSelected())
			{
				assignmentIds.add(wrapper.getAssignment().getPK());
			}
		}

		return assignmentIds;
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
	 * Get wrapped assignments for date range
	 * @param exportParameters
	 * @return
	 */
	private List<AssignmentWrapper> getAssignmentWrappers(ExportParameters exportParameters)
	{
		List<ProjectAssignment> assignments = getAssignments(exportParameters.getExportRange());
		
		wrappers = new ArrayList<AssignmentWrapper>();
		
		for (ProjectAssignment projectAssignment : assignments)
		{
			wrappers.add(new AssignmentWrapper(projectAssignment));
		}
		
		return wrappers;
	}

	
	/**
	 * 
	 * Created on Feb 3, 2009, 8:03:17 PM
	 * @author Thies Edeling (thies@te-con.nl) 
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
			this.selected = true;
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
