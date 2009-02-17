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

import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.ajax.AjaxUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
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
	public enum ExportCriteriaEvent implements AjaxEventType
	{
		PRINT,
		EXCEL;
	}

	
	private static final long serialVersionUID = -3732529050866431376L;

	public ExportCriteriaPanel(String id, IModel model)
	{
		super(id, model);
		setOutputMarkupId(true);
		
		add(createCriteriaPanel("criteriaForm"));
	}

	/**
	 * Create the criteria panel with the form, assignments and submit buttons
	 * @param id
	 * @return
	 */
	private Form createCriteriaPanel(String id)
	{
		Form form = new Form(id);

		form.add(createAssignmentCheckboxes("projectGroup"));
		
		form.add(new CheckBox("signOff", new PropertyModel(this.getModel(), "userCriteria.customParameters[INCL_SIGN_OFF]")));
		
		form.add(createSubmitButton("printButton", form, ExportCriteriaEvent.PRINT));
		form.add(createSubmitButton("excelButton", form, ExportCriteriaEvent.EXCEL));
		
		return form;
	}

	/**
	 * Create ajax submit buttons which publish their events upwards in the component hierarchy
	 * @param id
	 * @param form
	 * @param event
	 * @return
	 */
	@SuppressWarnings("serial")
	private AjaxFallbackButton createSubmitButton(String id, Form form, final ExportCriteriaEvent event)
	{
		AjaxFallbackButton button = new AjaxFallbackButton(id, form)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				AjaxUtil.publishAjaxEvent(ExportCriteriaPanel.this, new AjaxEvent(target, event));
			}
		};
		
		return button;
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
}
