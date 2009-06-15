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

package net.rrm.ehour.ui.timesheet.export.criteria;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.trend.PrintReport;
import net.rrm.ehour.ui.timesheet.export.ExportCriteriaParameter;
import net.rrm.ehour.ui.timesheet.export.excel.ExportReportExcel;
import net.rrm.ehour.ui.timesheet.export.print.PrintMonth;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.apache.wicket.util.value.ValueMap;

/**
 * ExportCriteriaPanel holding the for`m for month based exports for consultants
 **/

public class ExportCriteriaPanel extends Panel
{
	private static final long serialVersionUID = -3732529050866431376L;

	private enum ExportType
	{
		EXCEL,
		PRINT;
	}
	
	public ExportCriteriaPanel(String id, IModel model)
	{
		super(id, model);
		setOutputMarkupId(true);
		
		add(createCriteriaPanel("criteriaForm", model));
	}

	/**
	 * Create the criteria panel with the form, assignments and submit buttons
	 * @param idz
	 * @return
	 */
	private Form createCriteriaPanel(String id, IModel model)
	{
		Form form = new SelectionForm(id, model);

		form.add(createAssignmentCheckboxes("projectGroup"));
		
		form.add(createSignOffCheck("signOff"));
		
		form.add(createSubmitButton("printButton", form, ExportType.PRINT));
		form.add(createSubmitButton("excelButton", form, ExportType.EXCEL));
		
		return form;
	}

	@SuppressWarnings("serial")
	private SubmitLink createSubmitButton(String id, Form form, final ExportType type)
	{
		SubmitLink link = new SubmitLink(id, form)
		{
			@Override
			public void onSubmit()
			{
				ReportCriteria criteria = (ReportCriteria)ExportCriteriaPanel.this.getModelObject();
				
				criteria.getUserCriteria().getCustomParameters().put(ExportCriteriaParameter.EXPORT_TYPE, type);
			}
		};
		
		return link;
	}
	
	private CheckBox createSignOffCheck(String id)
	{
		return new CheckBox(id, new PropertyModel(this.getModel(), "userCriteria.customParameters[INCL_SIGN_OFF]"));
	}
	
	private CheckGroup createAssignmentCheckboxes(String id)
	{
		CheckGroup projectGroup = new CheckGroup(id, new PropertyModel(getModel(), "userCriteria.projects"));
		
		ListView projects = new ListView("projects", new PropertyModel(getModel(), "availableCriteria.projects"))
		{
			private static final long serialVersionUID = 6398866296089860246L;

			@Override
			protected void populateItem(ListItem item)
			{
				item.add(new Check("check", item.getModel()));
				item.add(new Label("project", new PropertyModel(item.getModel(), "fullName")));
			}
		};
		
		projectGroup.add(projects);
		
		return projectGroup;
	}

	/**
	 * 
	 * Created on Feb 18, 2009, 5:39:23 PM
	 * @author Thies Edeling (thies@te-con.nl) 
	 *
	 */
	private class SelectionForm extends Form
	{
		private static final long serialVersionUID = -8232635495078008621L;

		public SelectionForm(String id, IModel model)
		{
			super(id, model);
		}
		
		@Override
		protected void onSubmit()
		{
			ReportCriteria criteria = (ReportCriteria)getModelObject();

			ExportType type = (ExportType)criteria.getUserCriteria().getCustomParameters().get(ExportCriteriaParameter.EXPORT_TYPE);
			
			if (type == ExportType.EXCEL)
			{
				excelExport(criteria);
			}
			else if (type == ExportType.PRINT)
			{
				setResponsePage(new PrintMonth(criteria));
			}
		}

		/**
		 * @param criteria
		 */
		private void excelExport(ReportCriteria criteria)
		{
			PrintReport report = new PrintReport(criteria);
			EhourWebSession.getSession().getObjectCache().addObjectToCache(report);
			
			final String reportId = report.getCacheId();
			
			ResourceReference excelResource = new ResourceReference(ExportReportExcel.getId());
			ValueMap params = new ValueMap();
			params.add("reportId", reportId);
			
			excelResource.bind(getApplication());
			CharSequence url = getRequestCycle().urlFor(excelResource, params);
			
			getRequestCycle().setRequestTarget(new RedirectRequestTarget(url.toString()));
		}
	}
}
