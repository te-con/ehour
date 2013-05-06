package net.rrm.ehour.ui.common.panel.datepicker;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.validator.ConditionalRequiredValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatePickerPanel extends Panel {
    private static final long serialVersionUID = -7769909552498244968L;

    private DateTextField dateInputField;

    public DatePickerPanel(String id, IModel<Date> dateModel, IModel<Boolean> infiniteModel) {
        super(id);

        addDates(dateModel, infiniteModel);
    }

    @SuppressWarnings("serial")
    private void addDates(IModel<Date> dateModel, IModel<Boolean> infiniteModel) {
        // container for hiding input field
        final WebMarkupContainer updateTarget = new WebMarkupContainer("updateTarget");
        add(updateTarget);
        updateTarget.setOutputMarkupId(true);

        Locale locale = EhourWebSession.getSession().getEhourConfig().getLocale();
        String pattern = ((SimpleDateFormat)SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, locale)).toPattern();

        dateInputField = new DatePicker("date", dateModel, pattern, new Options());
        updateTarget.add(dateInputField);

        dateInputField.add(new ConditionalRequiredValidator<Date>(infiniteModel));
        dateInputField.setVisible(!infiniteModel.getObject());

        // indicator for validation issues
        updateTarget.add(new AjaxFormComponentFeedbackIndicator("dateValidationError", dateInputField));

        // infinite start date toggle
        AjaxCheckBox infiniteDate = new AjaxCheckBox("infiniteDate", infiniteModel) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String input = this.getInput();
                boolean visible = StringUtils.isNotBlank(input) && "on".equalsIgnoreCase(input);

                dateInputField.setVisible(!visible);

                target.add(updateTarget);
            }
        };

        updateTarget.add(infiniteDate);
    }

    public FormComponent<Date> getDateInputFormComponent() {
        return dateInputField;
    }
}
