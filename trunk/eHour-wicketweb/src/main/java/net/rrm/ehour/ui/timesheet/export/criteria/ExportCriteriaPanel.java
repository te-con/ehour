/**
 * Created on Feb 16, 2009
 * Author: Thies
 *
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

package net.rrm.ehour.ui.timesheet.export.criteria;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.timesheet.export.ExportCriteriaParameter;
import net.rrm.ehour.ui.timesheet.export.print.PrintMonth;

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

/**
 * ExportCriteriaPanel holding the form for month based exports for consultants
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
	 * @param id
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
				
			}
			else if (type == ExportType.PRINT)
			{
				setResponsePage(new PrintMonth(criteria));
			}
			
		}
	}
}
