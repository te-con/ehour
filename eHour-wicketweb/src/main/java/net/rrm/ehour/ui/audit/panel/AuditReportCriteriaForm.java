package net.rrm.ehour.ui.audit.panel;

import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.DynamicAttributeModifier;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.validator.ConditionalRequiredValidator;
import net.rrm.ehour.ui.validator.DateOverlapValidator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

/**
 * 
 * @author thies
 * 
 */
public class AuditReportCriteriaForm extends Form
{
	private static final long serialVersionUID = -4033279032707727816L;

	public AuditReportCriteriaForm(String id, IModel model)
	{
		super(id, model);
		
		addDates(model);
	}

	/**
	 * Add start & end dates
	 * 
	 * @param form
	 * @param model
	 */
	private void addDates(final IModel model)
	{
		PropertyModel infiniteStartDateModel = new PropertyModel(model, "infiniteStartDate");
		PropertyModel infiniteEndDateModel = new PropertyModel(model, "infiniteEndDate");

		// start date
		final DateTextField dateStart = new DateTextField("dateStart", 
															new PropertyModel(model, "dateStart"), 
															new StyleDateConverter("S-", true));

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
		final DateTextField dateEnd = new DateTextField("projectAssignment.dateEnd", new PropertyModel(model, "projectAssignment.dateEnd"), new StyleDateConverter("S-", false));
		dateEnd.add(new DatePicker());
		// container for hiding

		dateEnd.add(new ValidatingFormComponentAjaxBehavior());
		dateEnd.add(new ConditionalRequiredValidator(infiniteEndDateModel));
		dateEnd.setLabel(new ResourceModel("admin.assignment.dateEnd"));

		final WebMarkupContainer endDateHider = new WebMarkupContainer("endDateHider");
		endDateHider.setOutputMarkupId(true);

		// indicator for validation issues
		endDateHider.add(new AjaxFormComponentFeedbackIndicator("dateEndValidationError", dateEnd));

		// the inner hider is just there to hide the <br /> as well
		final WebMarkupContainer innerEndDateHider = new WebMarkupContainer("innerEndDateHider");
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

		add(new DateOverlapValidator(dateStart, dateEnd));
	}
}
