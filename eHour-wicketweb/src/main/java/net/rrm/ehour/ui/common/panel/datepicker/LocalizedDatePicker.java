package net.rrm.ehour.ui.common.panel.datepicker;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.model.IModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocalizedDatePicker extends DatePicker {
    private static final List<String> KNOWN_COUNTRIES = Arrays.asList("af", "ar-dz", "ar", "az", "be", "bg", "bs", "ca", "cs", "cy-gb", "da", "de", "el", "en-au", "en-gb", "en-nz", "eo", "es", "et", "eu", "fa", "fi", "fo", "fr-ca", "fr-ch", "fr", "gl", "he", "hi", "hr", "hu", "hy", "id", "is", "it", "ja", "ka", "kk", "km", "ko", "ky", "lb", "lt", "lv", "mk", "ml", "ms", "nb", "nl-be", "nl", "nn", "no", "pl", "pt-br", "pt", "rm", "ro", "ru", "sk", "sl", "sq", "sr", "sr-sr", "sv", "ta", "th", "tj", "tr", "uk", "vi", "zh-cn", "zh-hk", "zh-tw");

    public LocalizedDatePicker(String id, IModel<Date> dateModel) {
        super(id, dateModel, getPattern(), getOptions());
    }

    private static String getPattern() {
        Locale locale = EhourWebSession.getSession().getEhourConfig().getFormattingLocale();
        return ((SimpleDateFormat) SimpleDateFormat.getDateInstance(DateFormat.SHORT, locale)).toPattern();
    }


    public static Options getOptions() {
        Locale locale = EhourWebSession.getSession().getEhourConfig().getFormattingLocale();
        String languageTag = getLanguageTag(locale);

        return isSupported(languageTag) ? new Options("option", String.format("$.datepicker.regional['%s']", languageTag)) : new Options();
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

    private static String getLanguageTag(Locale locale) {
        String languageTag = locale.toLanguageTag().toLowerCase();
        String[] languageTags = languageTag.split("-");

        // Dutch locale is nl-NL, remove the -NL part if the language is the same as the country
        return languageTags[0].equalsIgnoreCase(languageTags[1]) ? languageTags[0] : languageTag;
    }

    private static boolean isSupported(String languageTag) {
        return KNOWN_COUNTRIES.contains(languageTag);
    }

}
