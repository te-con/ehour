package net.rrm.ehour.config;

public enum ConfigurationItem
{
	AVAILABLE_TRANSLATIONS("availableTranslations"),
	COMPLETE_DAY_HOURS("completeDayHours"),
	LOCALE_CURRENCY("localeCurrency"),
	LOCALE_LANGUAGE("localeLanguage"),
	LOCALE_COUNTRY("localeCountry"),
	SHOW_TURNOVER("showTurnOver"),
	TIMEZONE("timeZone"),
	MAIL_FROM("mailFrom"),
	MAIL_SMTP("mailSmtp"),
	MAIL_SMTP_USERNAME("smtpUsername"),
	MAIL_SMTP_PASSWORD("smtpPassword"),
	MAIL_SMTP_PORT("smtpPort"),
	DEMO_MODE("demoMode"),
	INITIALIZED("initialized"),
	FIRST_DAY_OF_WEEK("firstDayOfWeek"),
	AUDIT_TYPE("auditType"),
	VERSION("version"),
	DONT_FORCE_LANGUAGE("dontForceLanguage")
	;
	private String dbField;

	private ConfigurationItem(String dbField)
	{
		this.dbField = dbField;
	}

	public String getDbField()
	{
		return dbField;
	}



}
