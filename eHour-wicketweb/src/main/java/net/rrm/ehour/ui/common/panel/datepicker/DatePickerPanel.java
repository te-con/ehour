package net.rrm.ehour.ui.common.panel.datepicker;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.validator.ConditionalRequiredValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.model.IModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatePickerPanel extends AbstractBasePanel<Date> {
    private static final long serialVersionUID = -7769909552498244968L;

    private static final List<String> KNOWN_COUNTRIES = Arrays.asList("af", "ar-dz", "ar", "az", "be", "bg", "bs", "ca", "cs", "cy-gb", "da", "de", "el", "en-au", "en-gb", "en-nz", "eo", "es", "et", "eu", "fa", "fi", "fo", "fr-ca", "fr-ch", "fr", "gl", "he", "hi", "hr", "hu", "hy", "id", "is", "it", "ja", "ka", "kk", "km", "ko", "ky", "lb", "lt", "lv", "mk", "ml", "ms", "nb", "nl-be", "nl", "nn", "no", "pl", "pt-br", "pt", "rm", "ro", "ru", "sk", "sl", "sq", "sr", "sr-sr", "sv", "ta", "th", "tj", "tr", "uk", "vi", "zh-cn", "zh-hk", "zh-tw");

    private DateTextField dateInputField;
    private IModel<Boolean> infiniteModel;

    public DatePickerPanel(String id, IModel<Date> dateModel, IModel<Boolean> infiniteModel) {
        super(id, dateModel);

        this.infiniteModel = infiniteModel;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        addDates(getPanelModel(), infiniteModel);
    }

    @Override
    public void renderHead(HtmlHeaderContainer container) {
        super.renderHead(container);

        Locale locale = EhourWebSession.getSession().getEhourConfig().getFormattingLocale();
        String languageTag = getLanguageTag(locale);

        if (isSupported(languageTag)) {
            container.getHeaderResponse().render(JavaScriptHeaderItem.forUrl("js/i18n/jquery-ui-i18n.js"));
            container.getHeaderResponse().render(JavaScriptHeaderItem.forUrl(String.format("js/i18n/jquery.ui.datepicker-%s.js", languageTag)));
        }
    }

    @SuppressWarnings("serial")
    private void addDates(IModel<Date> dateModel, IModel<Boolean> infiniteModel) {
        final WebMarkupContainer updateTarget = new WebMarkupContainer("updateTarget");
        addOrReplace(updateTarget);
        updateTarget.setOutputMarkupId(true);

        Locale locale = EhourWebSession.getSession().getEhourConfig().getFormattingLocale();
        String languageTag = getLanguageTag(locale);

        if (isSupported(languageTag)) {
            String pattern = ((SimpleDateFormat) SimpleDateFormat.getDateInstance(DateFormat.SHORT, locale)).toPattern();
            Options option = new Options("option", String.format("$.datepicker.regional['%s']", languageTag));
            dateInputField = new DatePicker("date", dateModel, pattern, option);
        } else {
            String pattern = ((SimpleDateFormat) SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.US)).toPattern();
            dateInputField = new DatePicker("date", dateModel, pattern, new Options());
        }

        updateTarget.add(dateInputField);

        dateInputField.add(new ConditionalRequiredValidator<Date>(infiniteModel));
        dateInputField.add(new ValidatingFormComponentAjaxBehavior());
        dateInputField.setVisible(!infiniteModel.getObject());

        // indicator for validation issues
        updateTarget.add(new AjaxFormComponentFeedbackIndicator("dateValidationError", dateInputField));

        // infinite start date toggle
        AjaxCheckBox infiniteDate = new AjaxCheckBox("infiniteDate", infiniteModel) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String input = this.getInput();
                boolean visible = StringUtils.isNotBlank(input) && "on".equalsIgnoreCase(input);

                dateInputField.getFeedbackMessages().clear();
                dateInputField.setVisible(!visible);

                target.add(updateTarget);
            }
        };

        updateTarget.add(infiniteDate);
    }

    private boolean isSupported(String languageTag) {
        return KNOWN_COUNTRIES.contains(languageTag);
    }

    private String getLanguageTag(Locale locale) {
        String languageTag = locale.toLanguageTag().toLowerCase();
        String[] languageTags = languageTag.split("-");

        // Dutch locale is nl-NL, remove the -NL part if the language is the same as the country
        return languageTags[0].equalsIgnoreCase(languageTags[1]) ? languageTags[0] : languageTag;
    }

    public FormComponent<Date> getDateInputFormComponent() {
        return dateInputField;
    }
}
