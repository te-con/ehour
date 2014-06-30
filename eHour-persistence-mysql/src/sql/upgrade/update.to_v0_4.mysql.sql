ALTER TABLE CONFIGURATION MODIFY config_value varchar(255);
ALTER TABLE PROJECT_ASSIGNMENT CHANGE description role varchar(255);

INSERT INTO CONFIGURATION VALUES ('localeLanguage','en'),('currency','Euro'),('localeCountry',NULL),('availableTranslations','en,nl');