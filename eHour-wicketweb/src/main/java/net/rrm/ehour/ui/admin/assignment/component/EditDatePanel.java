package net.rrm.ehour.ui.admin.assignment.component;

import java.util.Date;

import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.validator.ConditionalRequiredValidator;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class EditDatePanel extends Panel
{
	private static final long serialVersionUID = -7769909552498244968L;

	private TextField<Date> dateInputField;

	public EditDatePanel(String id, IModel<Date> dateModel, IModel<Boolean> infiniteModel)
	{
		super(id);

		addDates(dateModel, infiniteModel);
	}

	@SuppressWarnings("serial")
	private void addDates(IModel<Date> dateModel, IModel<Boolean> infiniteModel)
	{
        // container for hiding input field
		final WebMarkupContainer updateTarget = new WebMarkupContainer("updateTarget");
		add(updateTarget);
		updateTarget.setOutputMarkupId(true);

		// start date
        dateInputField = new TextField<Date>("date", dateModel)
        {
//        	@Override
//        	public IConverter getConverter(Class<?> type)
//        	{
//        		return new PatternDateConverter("dd-MM-yyyy", false);
//        	}
        };
		updateTarget.add(dateInputField);

        dateInputField.add(new ConditionalRequiredValidator<Date>(infiniteModel));
		dateInputField.add(new ValidatingFormComponentAjaxBehavior());
        dateInputField.add(new DatePicker());
        dateInputField.setVisible( !((Boolean)infiniteModel.getObject()).booleanValue());

		// indicator for validation issues
		updateTarget.add(new AjaxFormComponentFeedbackIndicator("dateValidationError", dateInputField));

		// infinite start date toggle
		AjaxCheckBox infiniteDate = new AjaxCheckBox("infiniteDate", infiniteModel)
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				String input = this.getInput();
				boolean visible = StringUtils.isNotBlank(input) && "on".equalsIgnoreCase(input);

				dateInputField.setVisible(!visible);

				target.addComponent(updateTarget);
			}
		};

		updateTarget.add(infiniteDate);
	}

	public FormComponent<Date> getDateInputFormComponent()
	{
		return dateInputField;
	}
}
