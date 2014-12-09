/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.admin.config;

import com.google.common.collect.Lists;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.common.sort.LocaleComparator;
import net.rrm.ehour.util.DateUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;

/**
 * Configuration backing bean
 * Created on Apr 21, 2009, 3:13:25 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */

public class MainConfigBackingBean implements Serializable {
    private static final long serialVersionUID = -682573285773646807L;

    private static final Logger LOGGER = Logger.getLogger(MainConfigBackingBean.class);

    public static final List<String> VALID_REMINDER_DAYS = Lists.newArrayList("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT");

    private boolean translationsOnly = false;
    private boolean smtpAuthentication = false;
    private EhourConfigStub config;
    private Date firstWeekStart;
    private UserRole convertManagersTo = UserRole.ADMIN;

    private Integer reminderMinute;
    private Integer reminderHour;
    private String reminderDay;

    public MainConfigBackingBean(EhourConfigStub config) {
        this.config = config;

        smtpAuthentication = StringUtils.isNotBlank(config.getSmtpUsername()) && StringUtils.isNotBlank(config.getSmtpPassword());

        Calendar cal = new GregorianCalendar();
        DateUtil.dayOfWeekFix(cal);
        cal.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());
        firstWeekStart = cal.getTime();

        splitCronExpression(config.getReminderTime());
        updateReminderCronExpression();
    }

    private void splitCronExpression(String cron) {
        if (StringUtils.isBlank(cron)) {
            resetReminderTimeToDefault();
            return;
        }

        String[] splittedCron = cron.split(" ");

        if (splittedCron.length != 6) {
            LOGGER.error(cron + " is not a valid cron expression, expecting 6 elements. Defaulting to FRI 17:30");
            resetReminderTimeToDefault();
            return;
        }

        try {
            reminderMinute = Integer.valueOf(splittedCron[1]);
            reminderHour = Integer.valueOf(splittedCron[2]);
            reminderDay = splittedCron[5];

            if (reminderMinute >= 60 || reminderHour >= 24 || !VALID_REMINDER_DAYS.contains(reminderDay)) {
                LOGGER.error(cron + " contains illegal time elements.");
                resetReminderTimeToDefault();
            }

        } catch (NumberFormatException nfe) {
            LOGGER.error(cron + " contains items that can not be parsed for the UI (although it may be a legal cron expression).");
            resetReminderTimeToDefault();
        }

    }

    private void resetReminderTimeToDefault() {
        LOGGER.info("Defaulting reminder time to FRI 17:30");

        reminderMinute = 30;
        reminderHour = 17;
        reminderDay = "FRI";
    }

    @SuppressWarnings("UnusedDeclaration")
    public List<Locale> getAvailableLanguages() {
        Locale[] locales = Locale.getAvailableLocales();
        Map<String, Locale> localeMap = new HashMap<>();

        // remove all variants
        for (Locale locale : locales) {
            if (isTranslationsOnly()
                    && !ArrayUtils.contains(config.getAvailableTranslations(), locale.getLanguage())) {
                continue;
            }

            if (localeMap.containsKey(locale.getLanguage()) && locale.getDisplayName().indexOf('(') != -1) {
                continue;
            }

            localeMap.put(locale.getLanguage(), locale);
        }

        SortedSet<Locale> localeSet = new TreeSet<>(new LocaleComparator(LocaleComparator.CompareType.LANGUAGE));

        for (Locale locale : localeMap.values()) {
            localeSet.add(locale);
        }

        return new ArrayList<>(localeSet);
    }

    public boolean isTranslationsOnly() {
        return translationsOnly;
    }

    public void setTranslationsOnly(boolean translationsOnly) {
        this.translationsOnly = translationsOnly;
    }

    public static List<Locale> getAvailableLocales() {
        List<Locale> locales = new ArrayList<>();

        for (Locale locale : Locale.getAvailableLocales()) {
            if (!StringUtils.isBlank(locale.getDisplayCountry())) {
                locales.add(locale);
            }
        }

        Collections.sort(locales, new LocaleComparator(LocaleComparator.CompareType.COUNTRY));

        return locales;
    }

    public static List<Locale> getAvailableCurrencies() {
        List<Locale> locales = getAvailableLocales();
        SortedSet<Locale> currencyLocales = new TreeSet<>(new Comparator<Locale>() {
            public int compare(Locale o1, Locale o2) {
                Currency curr1 = Currency.getInstance(o1);
                Currency curr2 = Currency.getInstance(o2);

                return (o1.getDisplayCountry() + ": " + curr1.getSymbol(o1))
                        .compareTo(o2.getDisplayCountry() + ": " + curr2.getSymbol(o2));
            }
        }
        );

        for (Locale locale : locales) {
            if (!StringUtils.isBlank(locale.getCountry())) {
                currencyLocales.add(locale);
            }
        }

        return new ArrayList<>(currencyLocales);
    }

    public Locale getLocaleLanguage() {
        return config.getLanguageLocale();
    }

    public void setLocaleLanguage(Locale localeLanguage) {
        config.setLocaleLanguage(localeLanguage);
    }

    public Locale getLocaleCountry() {
        return config.getFormattingLocale();
    }

    public void setLocaleCountry(Locale localeCountry) {
        config.setLocaleFormatting(localeCountry);
    }

    public void setCurrency(Locale currencySymbol) {
        this.config.setCurrency(currencySymbol);
    }

    public EhourConfig getConfig() {
        return config;
    }

    public boolean isSmtpAuthentication() {
        return smtpAuthentication;
    }

    public void setSmtpAuthentication(boolean smtpAuthentication) {
        this.smtpAuthentication = smtpAuthentication;
    }

    public Date getFirstWeekStart() {
        return firstWeekStart;
    }


    public void setFirstWeekStart(Date firstWeekStart) {
        this.firstWeekStart = firstWeekStart;

        Calendar cal = new GregorianCalendar();
        cal.setTime(firstWeekStart);
        config.setFirstDayOfWeek(cal.get(Calendar.DAY_OF_WEEK));
    }

    public UserRole getConvertManagersTo() {
        return convertManagersTo;
    }

    public void setConvertManagersTo(UserRole convertManagersTo) {
        this.convertManagersTo = convertManagersTo;
    }

    public Integer getReminderMinute() {
        return reminderMinute;
    }

    public void setReminderMinute(Integer reminderMinute) {
        this.reminderMinute = reminderMinute;
        updateReminderCronExpression();
    }

    public Integer getReminderHour() {
        return reminderHour;
    }

    public void setReminderHour(Integer reminderHour) {
        this.reminderHour = reminderHour;
        updateReminderCronExpression();
    }

    public String getReminderDay() {
        return reminderDay;
    }

    public void setReminderDay(String reminderDay) {
        this.reminderDay = reminderDay;
        updateReminderCronExpression();
    }

    private void updateReminderCronExpression() {
        String cron = String.format("0 %d %d * * %s", reminderMinute, reminderHour, reminderDay);
        config.setReminderTime(cron);
    }
}
