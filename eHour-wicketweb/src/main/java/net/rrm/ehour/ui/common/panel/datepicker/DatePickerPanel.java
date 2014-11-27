package net.rrm.ehour.ui.common.panel.datepicker;

import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.validator.ConditionalRequiredValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

import java.util.Date;

public class DatePickerPanel extends AbstractBasePanel<Date> {
    private static final long serialVersionUID = -7769909552498244968L;

    private LocalizedDatePicker datePicker;

    public DatePickerPanel(String id, IModel<Date> dateModel, IModel<Boolean> infiniteModel) {
        super(id, dateModel);

        addDates(getPanelModel(), infiniteModel);
    }

    @SuppressWarnings("serial")
    private void addDates(IModel<Date> dateModel, IModel<Boolean> infiniteModel) {
        final WebMarkupContainer updateTarget = new WebMarkupContainer("updateTarget");
        addOrReplace(updateTarget);
        updateTarget.setOutputMarkupId(true);

        datePicker = new LocalizedDatePicker("date", dateModel);
        updateTarget.add(datePicker);

        datePicker.add(new ConditionalRequiredValidator<Date>(infiniteModel));
        datePicker.add(new ValidatingFormComponentAjaxBehavior());
        datePicker.setVisible(!infiniteModel.getObject());

        updateTarget.add(new AjaxFormComponentFeedbackIndicator("dateValidationError", datePicker));

        // infinite date toggle
        AjaxCheckBox infiniteDate = new AjaxCheckBox("infiniteDate", infiniteModel) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String input = this.getInput();
                boolean visible = StringUtils.isNotBlank(input) && "on".equalsIgnoreCase(input);

                datePicker.getFeedbackMessages().clear();
                datePicker.setVisible(!visible);

                target.add(updateTarget);
            }
        };

        updateTarget.add(infiniteDate);
    }

    public FormComponent<Date> getDateInputFormComponent() {
        return datePicker;
    }
}
