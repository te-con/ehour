package net.rrm.ehour.ui.audit.panel;

import net.rrm.ehour.ui.audit.AuditConstants;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.ajax.AjaxUtil;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.DynamicAttributeModifier;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.validator.ConditionalRequiredValidator;
import net.rrm.ehour.ui.common.validator.DateOverlapValidator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
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

	public enum Events implements AjaxEventType
	{
		FORM_SUBMIT;
	}

	public AuditReportCriteriaForm(String id, IModel model)
	{
		super(id, model);

		addDates(model);

		AjaxButton submitButton = new AjaxButton(AuditConstants.PATH_FORM_SUBMIT, this)
		{
			private static final long serialVersionUID = -627058322154455051L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				AjaxEvent event = new AjaxEvent(target, Events.FORM_SUBMIT);
				AjaxUtil.publishAjaxEvent(AuditReportCriteriaForm.this, event);
			}
		};

		add(submitButton);

		TextField nameField = new TextField(AuditConstants.PATH_FORM_NAME, new PropertyModel(getModel(), "userCriteria.name"));
		add(nameField);

		TextField actionField = new TextField(AuditConstants.PATH_FORM_ACTION, new PropertyModel(getModel(), "userCriteria.action"));
		add(actionField);
	}

	/**
	 * Add start & end dates
	 * 
	 * @param form
	 * @param model
	 */
	private void addDates(final IModel model)
	{
		PropertyModel infiniteStartDateModel = new PropertyModel(model, "userCriteria.infiniteStartDate");
		PropertyModel infiniteEndDateModel = new PropertyModel(model, "userCriteria.infiniteEndDate");

		// start date
		DateTextField dateStart = new DateTextField("dateStart", new PropertyModel(model, "userCriteria.reportRange.dateStart"), new StyleDateConverter("S-", true));

		dateStart.add(new ConditionalRequiredValidator(infiniteStartDateModel));
		dateStart.add(new ValidatingFormComponentAjaxBehavior());
		dateStart.setLabel(new ResourceModel("admin.assignment.dateStart"));
		dateStart.add(new DatePicker());

		// container for hiding
		WebMarkupContainer startDateHider = new WebMarkupContainer(AuditConstants.PATH_FORM_START_DATE_HIDER);
		startDateHider.setOutputMarkupId(true);

		// indicator for validation issues
		startDateHider.add(new AjaxFormComponentFeedbackIndicator("dateStartValidationError", dateStart));

		// the inner hider is just there to hide the <br /> as well
		WebMarkupContainer innerStartDateHider = new WebMarkupContainer("innerStartDateHider");
		innerStartDateHider.setOutputMarkupId(true);
		innerStartDateHider.add(dateStart);
		innerStartDateHider.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), infiniteStartDateModel, true));

		startDateHider.add(innerStartDateHider);

		add(startDateHider);

		// infinite start date toggle
		AjaxCheckBox infiniteStart = new AjaxCheckBox("infiniteStartDate", infiniteStartDateModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(AuditReportCriteriaForm.this.get(AuditConstants.PATH_FORM_START_DATE_HIDER));
			}
		};

		startDateHider.add(infiniteStart);

		// end date
		DateTextField dateEnd = new DateTextField("dateEnd", new PropertyModel(model, "userCriteria.reportRange.dateEnd"), new StyleDateConverter("S-", false));
		dateEnd.add(new DatePicker());
		// container for hiding

		dateEnd.add(new ValidatingFormComponentAjaxBehavior());
		dateEnd.add(new ConditionalRequiredValidator(infiniteEndDateModel));
		dateEnd.setLabel(new ResourceModel("admin.assignment.dateEnd"));

		WebMarkupContainer endDateHider = new WebMarkupContainer(AuditConstants.PATH_FORM_END_DATE_HIDER);
		endDateHider.setOutputMarkupId(true);

		// indicator for validation issues
		endDateHider.add(new AjaxFormComponentFeedbackIndicator("dateEndValidationError", dateEnd));

		// the inner hider is just there to hide the <br /> as well
		WebMarkupContainer innerEndDateHider = new WebMarkupContainer("innerEndDateHider");
		innerEndDateHider.setOutputMarkupId(true);
		innerEndDateHider.add(dateEnd);
		innerEndDateHider.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), infiniteEndDateModel, true));
		endDateHider.add(innerEndDateHider);
		add(endDateHider);

		// infinite end date toggle
		AjaxCheckBox infiniteEnd = new AjaxCheckBox("infiniteEndDate", infiniteEndDateModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(AuditReportCriteriaForm.this.get(AuditConstants.PATH_FORM_END_DATE_HIDER));
			}
		};

		endDateHider.add(infiniteEnd);

		add(new DateOverlapValidator(dateStart, dateEnd));
	}
}
