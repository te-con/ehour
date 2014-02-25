package net.rrm.ehour.ui.common.panel.datepicker;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.model.IModel;

import java.util.Date;

public class LocalizedDatePicker extends DateTextField {
//    private static final List<String> KNOWN_COUNTRIES = Arrays.asList("af", "ar-DZ", "ar", "az", "be", "bg", "bs", "ca", "cs", "cy-GB", "da", "de", "el", "en-AU", "en-GB", "en-NZ", "eo", "es", "et", "eu", "fa", "fi", "fo", "fr-CA", "fr-CH", "fr", "gl", "he", "hi", "hr", "hu", "hy", "id", "is", "it", "ja", "ka", "kk", "km", "ko", "ky", "lb", "lt", "lv", "mk", "ml", "ms", "nb", "nl-BE", "nl", "nn", "no", "pl", "pt-BR", "pt", "rm", "ro", "ru", "sk", "sl", "sq", "sr", "sr-SR", "sv", "ta", "th", "tj", "tr", "uk", "vi", "zh-CN", "zh-HK", "zh-TW");

    public LocalizedDatePicker(String id, IModel<Date> dateModel) {
        super(id, dateModel, new DateTextFieldConfig().autoClose(true).withLanguage(EhourWebSession.getEhourConfig().getFormattingLocale().getLanguage()));
    }

//    private static String getPattern() {
//        Locale locale = EhourWebSession.getEhourConfig().getFormattingLocale();
//
//        Locale localeToUse = isSupported(getLanguageTag(locale)) ? locale : Locale.US;
//
//        return DateUtil.getPatternForDateLocale(localeToUse);
//    }
//
//    public static Options getOptions() {
//        Locale locale = EhourWebSession.getEhourConfig().getFormattingLocale();
//        String languageTag = getLanguageTag(locale);
//
//        return isSupported(languageTag) ? new Options("option", String.format("$.datepicker.regional['%s']", languageTag)) : new Options();
//    }
//
//    @Override
//    public void renderHead(HtmlHeaderContainer container) {
//        super.renderHead(container);
//
//        Locale locale = EhourWebSession.getEhourConfig().getFormattingLocale();
//        String languageTag = getLanguageTag(locale);
//
//        if (isSupported(languageTag)) {
//            container.getHeaderResponse().render(JavaScriptHeaderItem.forUrl("js/i18n/jquery-ui-i18n.js"));
//            container.getHeaderResponse().render(JavaScriptHeaderItem.forUrl(String.format("js/i18n/jquery.ui.datepicker-%s.js", languageTag)));
//        }
//    }
//
//    private static String getLanguageTag(Locale locale) {
//        String languageTag = LocaleUtil.toLanguageTag(locale);
//        String[] languageTags = languageTag.split("-");
//
//        // Dutch locale is nl-NL, remove the -NL part if the language is the same as the country
//        return languageTags[0].equalsIgnoreCase(languageTags[1]) ? languageTags[0] : languageTags[0] + "-" + languageTags[1].toUpperCase();
//    }
//
//    private static boolean isSupported(String languageTag) {
//        return KNOWN_COUNTRIES.contains(languageTag);
//    }

}
