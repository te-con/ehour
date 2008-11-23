/**
 * Created on Feb 24, 2008
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

package net.rrm.ehour.ui.common.panel.assignment;

import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.DynamicAttributeModifier;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.model.FloatModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.renderers.ProjectAssignmentTypeRenderer;
import net.rrm.ehour.ui.validator.ConditionalRequiredValidator;
import net.rrm.ehour.ui.validator.DateOverlapValidator;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.NumberValidator;

/**
 * Assignment type part of form 
 **/

public class AssignmentTypeFormPartPanel extends Panel
{
	private static final long serialVersionUID = 1L;
	private Component[] notifiableComponents;
	
	
	@SpringBean
	private ProjectAssignmentService	projectAssignmentService;
	
	public AssignmentTypeFormPartPanel(String id, IModel model, Form form)
	{
		super(id, model);
		
		addAssignmentType(model);
		addDates(form, model);
	}

	/**
	 * 
	 * @return
	 */
	public Component[] getNotifiableComponents()
	{
		return notifiableComponents;
	}
	
	/**
	 * Add assignment types and options
	 * @param form
	 * @param assignmenTypes
	 */
	private void addAssignmentType(IModel model)
	{
		List<ProjectAssignmentType> assignmentTypes = projectAssignmentService.getProjectAssignmentTypes();		

		EhourConfig config = ((EhourWebSession)getSession()).getEhourConfig();
		
		final PropertyModel	showAllottedHoursModel = new PropertyModel(model, "showAllottedHours");
		final PropertyModel	showOverrunHoursModel = new PropertyModel(model, "showOverrunHours");
		
		// assignment type
		final DropDownChoice assignmentTypeChoice = new DropDownChoice("projectAssignment.assignmentType", assignmentTypes, new ProjectAssignmentTypeRenderer());
		assignmentTypeChoice.setRequired(true);
		assignmentTypeChoice.setNullValid(false);
		assignmentTypeChoice.setLabel(new ResourceModel("admin.assignment.type"));
		assignmentTypeChoice.add(new ValidatingFormComponentAjaxBehavior());
		add(assignmentTypeChoice);
		add(new AjaxFormComponentFeedbackIndicator("typeValidationError", assignmentTypeChoice));
		
		// allotted hours 
		final TextField allottedHours = new RequiredTextField("projectAssignment.allottedHours",
												new FloatModel(new PropertyModel(model, "projectAssignment.allottedHours"), config, null));
		allottedHours.setType(float.class);
		allottedHours.add(new ValidatingFormComponentAjaxBehavior());
		allottedHours.add(NumberValidator.POSITIVE);
		allottedHours.setOutputMarkupId(true);
		allottedHours.setLabel(new ResourceModel("admin.assignment.timeAllotted"));
		allottedHours.setEnabled(((Boolean)showAllottedHoursModel.getObject()).booleanValue());
		
		// allotted hours row
		final WebMarkupContainer allottedRow = new WebMarkupContainer("allottedRow");
		allottedRow.setOutputMarkupId(true);
		allottedRow.add(allottedHours);
		allottedRow.add(new AjaxFormComponentFeedbackIndicator("allottedHoursValidationError", allottedHours));
		allottedRow.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), showAllottedHoursModel));
		add(allottedRow);
		
		// overrun hours 
		final TextField overrunHours = new RequiredTextField("projectAssignment.allowedOverrun",
											new FloatModel(new PropertyModel(model, "projectAssignment.allowedOverrun"), config, null));
		overrunHours.setType(float.class);
		overrunHours.add(new ValidatingFormComponentAjaxBehavior());
		overrunHours.add(NumberValidator.POSITIVE);
		overrunHours.setOutputMarkupId(true);
		overrunHours.setEnabled(((Boolean)showOverrunHoursModel.getObject()).booleanValue());
		overrunHours.setLabel(new ResourceModel("admin.assignment.allowedOverrun"));
		
		// overrun hours row
		final WebMarkupContainer overrunRow = new WebMarkupContainer("overrunRow");
		overrunRow.setOutputMarkupId(true);
		overrunRow.add(overrunHours);
		overrunRow.add(new AjaxFormComponentFeedbackIndicator("overrunHoursValidationError", overrunHours));
		overrunRow.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), showOverrunHoursModel));
		add(overrunRow);
		
		// notify PM when possible
		CheckBox notifyPm = new CheckBox("projectAssignment.notifyPm");
		notifyPm.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), new PropertyModel(model, "notifyPmEnabled")));
		notifyPm.setOutputMarkupId(true);
		
		Label notifyDisabled = new Label("notifyDisabled", new ResourceModel("admin.assignment.cantNotify"));
		notifyDisabled.add(new DynamicAttributeModifier("style", true, 
														new Model("display: none;"), new PropertyModel(model, "notifyPmEnabled"), true));
		notifyDisabled.setOutputMarkupId(true);
		
		// notify PM row
		final WebMarkupContainer notifyPmRow = new WebMarkupContainer("notifyPmRow");
		notifyPmRow.setOutputMarkupId(true);
		notifyPmRow.add(notifyPm);
		notifyPmRow.add(notifyDisabled);
		notifyPmRow.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), showAllottedHoursModel));
		add(notifyPmRow);
		
		assignmentTypeChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target)
            {
				// to disable the required validation
				allottedHours.setEnabled(((Boolean)showAllottedHoursModel.getObject()).booleanValue());
				overrunHours.setEnabled(((Boolean)showOverrunHoursModel.getObject()).booleanValue());
				target.addComponent(allottedHours);
				target.addComponent(overrunHours);
				
				// show/hide rows dependent on the assignment type selected
				target.addComponent(allottedRow);
				target.addComponent(overrunRow);
				target.addComponent(notifyPmRow);
            }
        });	
		
		notifiableComponents = new Component[]{notifyPm, notifyDisabled};
	}
	
	/**
	 * Add start & end dates
	 * @param form
	 * @param model
	 */
	private void addDates(Form form, final IModel model)
	{
		PropertyModel	infiniteStartDateModel = new PropertyModel(model, "infiniteStartDate");
		PropertyModel	infiniteEndDateModel = new PropertyModel(model, "infiniteEndDate");
		
		// start date
        final DateTextField dateStart = new DateTextField("projectAssignment.dateStart", new PropertyModel(model,
        "projectAssignment.dateStart"), new StyleDateConverter("S-", true));
		
		dateStart.add(new ConditionalRequiredValidator(infiniteStartDateModel));
		dateStart.add(new ValidatingFormComponentAjaxBehavior());
		dateStart.setLabel(new ResourceModel("admin.assignment.dateStart"));
        dateStart.add(new DatePicker());

        // container for hiding
		final WebMarkupContainer startDateHider = new WebMarkupContainer("startDateHider");
		startDateHider.setOutputMarkupId(true);
		
		// indicator for validation issues
		startDateHider.add(new AjaxFormComponentFeedbackIndicator("dateStartValidationError", dateStart));

		
		// the inner hider is just there to hide the <br /> as well
		final WebMarkupContainer innerStartDateHider = new WebMarkupContainer("innerStartDateHider");
		innerStartDateHider.setOutputMarkupId(true);
		innerStartDateHider.add(dateStart);
		innerStartDateHider.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), infiniteStartDateModel, true));

		startDateHider.add(innerStartDateHider);

		add(startDateHider);
		
		// infinite start date toggle
		AjaxCheckBox infiniteStart = new AjaxCheckBox("infiniteStartDate")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(startDateHider);
			}
		};
		
		startDateHider.add(infiniteStart);

		// end date
        final DateTextField dateEnd = new DateTextField("projectAssignment.dateEnd", new PropertyModel(model,
        										"projectAssignment.dateEnd"), new StyleDateConverter("S-", false));
        dateEnd.add(new DatePicker());
		// container for hiding
		
		dateEnd.add(new ValidatingFormComponentAjaxBehavior());
		dateEnd.add(new ConditionalRequiredValidator(infiniteEndDateModel));
		dateEnd.setLabel(new ResourceModel("admin.assignment.dateEnd"));
		
		final WebMarkupContainer	endDateHider = new WebMarkupContainer("endDateHider");
		endDateHider.setOutputMarkupId(true);
		
		// indicator for validation issues
		endDateHider.add(new AjaxFormComponentFeedbackIndicator("dateEndValidationError", dateEnd));
		
		// the inner hider is just there to hide the <br /> as well
		final WebMarkupContainer	innerEndDateHider = new WebMarkupContainer("innerEndDateHider");
		innerEndDateHider.setOutputMarkupId(true);
		innerEndDateHider.add(dateEnd);
		innerEndDateHider.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), infiniteEndDateModel, true));
		endDateHider.add(innerEndDateHider);		
		add(endDateHider);	
		
		// infinite end date toggle
		AjaxCheckBox infiniteEnd = new AjaxCheckBox("infiniteEndDate")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(endDateHider);
			}
		};	

		endDateHider.add(infiniteEnd);
		
		form.add(new DateOverlapValidator(dateStart, dateEnd));
	}	
}
