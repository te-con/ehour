/**
 * Created on Jul 17, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.page.admin.mainconfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.ui.ajax.DemoDecorator;
import net.rrm.ehour.ui.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.component.FadeLabel;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.page.admin.BaseAdminPage;
import net.rrm.ehour.ui.page.admin.mainconfig.dto.MainConfigBackingBean;
import net.rrm.ehour.ui.page.admin.mainconfig.dto.MainConfigBackingBean.CurrencyChoice;
import net.rrm.ehour.ui.sort.LocaleComparator;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;

/**
 * Main config page
 **/
public class MainConfig extends BaseAdminPage
{
	private static final long serialVersionUID = 8613594529875207988L;
	private static final Logger logger = Logger.getLogger(MainConfig.class);
	
	@SpringBean
	private ConfigurationService	configService;

	private Label	serverMessage;
	private	final MainConfigBackingBean	configBackingBean = new MainConfigBackingBean();
	
	/**
	 * 
	 */
	public MainConfig()
	{
		super(new ResourceModel("admin.config.title"), null);
		
		EhourConfig	dbConfig;
		
		dbConfig = getDbConfig();
		updateBackingBean(dbConfig, false);
		
		setUpPage(dbConfig);
	}
	
	/**
	 * 
	 * @param config
	 */
	private void updateBackingBean(EhourConfig config, boolean translationsOnly)
	{
		configBackingBean.setDontForceLocale(config.getLocaleLanguage() == null || config.getLocaleLanguage().equals("noForce"));
		configBackingBean.setTranslationsOnly(translationsOnly);
		configBackingBean.setAvailableLanguages(getAvailableLanguages(config));
		configBackingBean.setLocale(config.getLocaleLanguage());
	}
	
	/**
	 * Setup page
	 */
	private void setUpPage(EhourConfig dbConfig)
	{
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("configFrame", new ResourceModel("admin.config.title"));
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueBorder");
		greyBorder.add(blueBorder);
		add(greyBorder);

		createForm(blueBorder, dbConfig);
	}

	/**
	 * Create form
	 * 
	 * @param parent
	 */
	private void createForm(WebMarkupContainer parent, final EhourConfig dbConfig)
	{
		Form configForm = new Form("configForm", new CompoundPropertyModel(dbConfig));
		configForm.setOutputMarkupId(true);

		// show locale selections (currency, language, date format)
		addLocaleSelections(configForm, dbConfig);
		
		// show turnover checkbox
		configForm.add(new CheckBox("showTurnover"));		
		
		// reply sender
		RequiredTextField mailFrom = new RequiredTextField("mailFrom");
		mailFrom.add(EmailAddressValidator.getInstance());
		configForm.add(mailFrom);
		configForm.add(new AjaxFormComponentFeedbackIndicator("mailFromError", mailFrom));
		
		configForm.add(new RequiredTextField("mailSmtp"));
		
		setSubmitButton(configForm, dbConfig);
		
		parent.add(configForm);
		
		serverMessage = new FadeLabel("serverMessage", "&nbsp;");
		serverMessage.setEscapeModelStrings(false);
		serverMessage.setOutputMarkupId(true);
		configForm.add(serverMessage);
	}
	
	/**
	 * Set ajax submit button
	 * @param form
	 * @param dbConfig
	 */
	@SuppressWarnings("serial")
	private void setSubmitButton(Form form, final EhourConfig dbConfig)
	{
		form.add(new AjaxButton("submitButton", form)
		{
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				IModel msgModel;

				if (!dbConfig.isInDemoMode())
				{
					((EhourConfigStub)dbConfig).setLocaleLanguage(configBackingBean.isDontForceLocale() ? "noForce" : configBackingBean.getLocale().getLanguage());
					
					try
					{
						configService.persistConfiguration(dbConfig);
						msgModel = new ResourceModel("dataSaved");
					}
					catch (Throwable t)
					{
						logger.error("While saving config", t);
						msgModel = new ResourceModel("saveError");
					}
					
					getEhourWebSession().reloadConfig();
					
					serverMessage.setModel(msgModel);
					target.addComponent(serverMessage);
				}
            }		


			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				if (dbConfig.isInDemoMode())
				{
					return new DemoDecorator(new ResourceModel("demoModel"));
				}
				else
				{
					return new LoadingSpinnerDecorator();
				}
			}			
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form)
			{
				target.addComponent(form);
            }
        });		
	}
	
	/**
	 * Add locale selections
	 * @param configForm
	 * @param dbConfig
	 */
	@SuppressWarnings("serial")
	private void addLocaleSelections(Form configForm, final EhourConfig dbConfig)
	{
		final DropDownChoice	localeDropDownChoice;
		final DropDownChoice	languageDropDownChoice;
		final AjaxCheckBox		onlyTranslationsBox;
		final DropDownChoice	currencyDropDownChoice;
		final Label				dateFormat;
		
		configForm.setOutputMarkupId(true);

		// currency dropdown
		currencyDropDownChoice = new DropDownChoice("currency",
											new PropertyModel(configBackingBean, "availableCurrencies"),
											new CurrencyChoiceRenderer());
		currencyDropDownChoice.setOutputMarkupId(true);
		configForm.add(currencyDropDownChoice);
		
		// date format example
		dateFormat = new Label("dateFormat", 
							new DateModel(new Model(new Date()), configBackingBean.getLocale(), DateModel.DATESTYLE_LONG ) );
		dateFormat.setOutputMarkupId(true);
		configForm.add(dateFormat);

		// language selection
		languageDropDownChoice = new DropDownChoice("localeLanguage",
													new PropertyModel(configBackingBean, "localeLanguage"),
													new PropertyModel(configBackingBean, "availableLanguages"),
													new LocaleChoiceRenderer(1)); 

		// locale selection
		localeDropDownChoice = new DropDownChoice("locale",
													new PropertyModel(configBackingBean, "locale"),
													new PropertyModel(configBackingBean, "availableLocales"),
													new LocaleChoiceRenderer(0)); 
		localeDropDownChoice.setOutputMarkupId(true);
		localeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				// update the date format example
				dateFormat.setModel(new DateModel(new Model(new Date()), configBackingBean.getLocale(), DateModel.DATESTYLE_LONG ) );
				target.addComponent(dateFormat);
				
				// set the language
				configBackingBean.setLocaleLanguage(configBackingBean.getLocale());
				target.addComponent(languageDropDownChoice);
				
				// and currency
				target.addComponent(currencyDropDownChoice);
			}
		});
		
		configForm.add(localeDropDownChoice);

		// behaviour for language selection
		languageDropDownChoice.setEnabled(!configBackingBean.isDontForceLocale());
		languageDropDownChoice.setOutputMarkupId(true);
		languageDropDownChoice.setRequired(true);
		languageDropDownChoice.setLabel(new ResourceModel("admin.config.locale.languageLabel"));
//		languageDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
//		{
//			@Override
//			protected void onUpdate(AjaxRequestTarget target)
//			{
//				// update the example
//				dateFormat.setModel(new DateModel(new Model(new Date()), configBackingBean.getLocale(), DateModel.DATESTYLE_LONG ) );
//				target.addComponent(dateFormat);
//			}
//		});
		
		configForm.add(new AjaxFormComponentFeedbackIndicator("localeLanguageValidationError", localeDropDownChoice));
		configForm.add(languageDropDownChoice);		
		
		// only translations
		onlyTranslationsBox = new AjaxCheckBox("onlyTranslations", new PropertyModel(configBackingBean, "translationsOnly"))
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				configBackingBean.setAvailableLanguages(getAvailableLanguages(dbConfig));
				target.addComponent(languageDropDownChoice);
			}
		};
		onlyTranslationsBox.setOutputMarkupId(true);
		onlyTranslationsBox.setEnabled(!configBackingBean.isDontForceLocale());
		configForm.add(onlyTranslationsBox);
		
		// don't force locale checkbox
		configForm.add(new AjaxCheckBox("dontForceLocale", new PropertyModel(configBackingBean, "dontForceLocale"))
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				languageDropDownChoice.setEnabled(!configBackingBean.isDontForceLocale());
				target.addComponent(languageDropDownChoice);
				
				onlyTranslationsBox.setEnabled(!configBackingBean.isDontForceLocale());
				target.addComponent(onlyTranslationsBox);
			}
		});				
	}
    
    /**
     * Choice for a locale.
     */
	@SuppressWarnings("serial")
    private final class LocaleChoiceRenderer extends ChoiceRenderer
    {
		int type;
		
		// type == 0 -> country, 1 => language
		public LocaleChoiceRenderer(int type)
		{
			this.type = type;
		}
        /**
         * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(Object)
         */
    	@Override
        public Object getDisplayValue(Object object)
        {
            Locale locale = (Locale)object;
            String display;
            
            if (type == 0)
            {
	            display = locale.getDisplayCountry();
	            
	            display += " (" + locale.getDisplayLanguage();
	
	            if (!StringUtils.isBlank(locale.getVariant()))
	    		{
	            	display += ", " + locale.getDisplayVariant();
	    		}
	            
	            display += ")";
            }
            else
            {
	            display = locale.getDisplayLanguage();
            }

            return display;
        }

    	/*
    	 * (non-Javadoc)
    	 * @see org.apache.wicket.markup.html.form.ChoiceRenderer#getIdValue(java.lang.Object, int)
    	 */
    	@Override
    	public String getIdValue(Object o, int index)
    	{
    		Locale locale = (Locale)o;
    		return locale.toString();
    	}
    }
	
	
    /**
     * Choice for a locale.
     */
	@SuppressWarnings("serial")
    private final class CurrencyChoiceRenderer extends ChoiceRenderer
    {
		/*
		 * (non-Javadoc)
		 * @see org.apache.wicket.markup.html.form.ChoiceRenderer#getDisplayValue(java.lang.Object)
		 */
    	@Override
        public Object getDisplayValue(Object object)
        {
            CurrencyChoice currencyChoice = (CurrencyChoice)object;

            return currencyChoice.displayName;
        }

    	/*
    	 * (non-Javadoc)
    	 * @see org.apache.wicket.markup.html.form.ChoiceRenderer#getIdValue(java.lang.Object, int)
    	 */
    	@Override
    	public String getIdValue(Object object, int index)
    	{
            CurrencyChoice currencyChoice = (CurrencyChoice)object;

            return currencyChoice.localizedSymbol;
    	}
    }

	
	/**
	 * Get available languages
	 * @param config
	 * @param onlyAvailable
	 * @return
	 */
	private List<Locale> getAvailableLanguages(EhourConfig config)
	{
		Locale[]			locales = Locale.getAvailableLocales();
		Map<String, Locale>	localeMap = new HashMap<String, Locale>();
		
		// remove all variants
		for (Locale locale : locales)
		{
			if (configBackingBean.isTranslationsOnly()
					&& !ArrayUtils.contains(config.getAvailableTranslations(), locale.getLanguage()))
			{
				continue;
			}
			
			if (localeMap.containsKey(locale.getLanguage()))
			{
				if (locale.getDisplayName().indexOf('(') != -1)
				{
					continue;
				}
			}

			localeMap.put(locale.getLanguage(), locale);
		}
		
		
		SortedSet<Locale>	localeSet = new TreeSet<Locale>(new LocaleComparator(LocaleComparator.CompareType.LANGUAGE));
		
		for (Locale locale : localeMap.values())
		{
			localeSet.add(locale);
		}
		
		
		return new ArrayList<Locale>(localeSet);
	}	
	
	/**
	 * 
	 * @return
	 */
	private EhourConfig getDbConfig()
	{
		return configService.getConfiguration();
	}
}
