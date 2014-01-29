package net.rrm.ehour.ui.common.panel.datepicker;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;
import net.rrm.ehour.config.LocaleUtil;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.model.IModel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocalizedDatePicker extends DatePicker {
    private static final List<String> KNOWN_COUNTRIES = Arrays.asList("af", "ar-DZ", "ar", "az", "be", "bg", "bs", "ca", "cs", "cy-GB", "da", "de", "el", "en-AU", "en-GB", "en-NZ", "eo", "es", "et", "eu", "fa", "fi", "fo", "fr-CA", "fr-CH", "fr", "gl", "he", "hi", "hr", "hu", "hy", "id", "is", "it", "ja", "ka", "kk", "km", "ko", "ky", "lb", "lt", "lv", "mk", "ml", "ms", "nb", "nl-BE", "nl", "nn", "no", "pl", "pt-BR", "pt", "rm", "ro", "ru", "sk", "sl", "sq", "sr", "sr-SR", "sv", "ta", "th", "tj", "tr", "uk", "vi", "zh-CN", "zh-HK", "zh-TW");

    public LocalizedDatePicker(String id, IModel<Date> dateModel) {
        super(id, dateModel, getPattern(), getOptions());
    }

    private static String getPattern() {
        Locale locale = EhourWebSession.getEhourConfig().getFormattingLocale();

        Locale localeToUse = isSupported(getLanguageTag(locale)) ? locale : Locale.US;

        return DateUtil.getPatternForDateLocale(localeToUse);
    }

    public static Options getOptions() {
        Locale locale = EhourWebSession.getEhourConfig().getFormattingLocale();
        String languageTag = getLanguageTag(locale);

        return isSupported(languageTag) ? new Options("option", String.format("$.datepicker.regional['%s']", languageTag)) : new Options();
    }

    @Override
    public void renderHead(HtmlHeaderContainer container) {
        super.renderHead(container);

        Locale locale = EhourWebSession.getEhourConfig().getFormattingLocale();
        String languageTag = getLanguageTag(locale);

        if (isSupported(languageTag)) {
            container.getHeaderResponse().render(JavaScriptHeaderItem.forUrl("js/i18n/jquery-ui-i18n.js"));
            container.getHeaderResponse().render(JavaScriptHeaderItem.forUrl(String.format("js/i18n/jquery.ui.datepicker-%s.js", languageTag)));
        }
    }

    private static String getLanguageTag(Locale locale) {
        String languageTag = LocaleUtil.toLanguageTag(locale);
        String[] languageTags = languageTag.split("-");

        // Dutch locale is nl-NL, remove the -NL part if the language is the same as the country
        return languageTags[0].equalsIgnoreCase(languageTags[1]) ? languageTags[0] : languageTags[0] + "-" + languageTags[1].toUpperCase();
    }

    private static boolean isSupported(String languageTag) {
        return KNOWN_COUNTRIES.contains(languageTag);
    }

}
