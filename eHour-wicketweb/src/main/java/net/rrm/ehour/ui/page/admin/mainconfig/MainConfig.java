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

import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.ui.ajax.DemoDecorator;
import net.rrm.ehour.ui.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.page.admin.BaseAdminPage;
import net.rrm.ehour.ui.page.admin.mainconfig.dto.MainConfigBackingBean;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.NumberValidator;

/**
 * Main config page
 **/
public class MainConfig extends BaseAdminPage
{
	private static final long serialVersionUID = 8613594529875207988L;
	private static final Logger logger = Logger.getLogger(MainConfig.class);
	
	@SpringBean
	private ConfigurationService	configService;
	@SpringBean
	private MailService				mailService;
	
	private WebComponent serverMessage;
	private	final 	MainConfigBackingBean configBackingBean;
	
	
	/**
	 * 
	 */
	public MainConfig()
	{
		super(new ResourceModel("admin.config.title"), null,
						"admin.config.help.header",
						"admin.config.help.body");
		
		configBackingBean = new MainConfigBackingBean(getDbConfig());
		
		setUpPage();
	}
	
	/**
	 * Setup page
	 */
	private void setUpPage()
	{
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("configFrame", new ResourceModel("admin.config.title"));
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueBorder");
		greyBorder.add(blueBorder);
		add(greyBorder);

		createForm(blueBorder);
	}

	/**
	 * Create form
	 * 
	 * @param parent
	 */
	private void createForm(WebMarkupContainer parent)
	{
		Form configForm = new Form("configForm", new CompoundPropertyModel(configBackingBean));
		configForm.setOutputMarkupId(true);

		// show locale selections (currency, language, date format)
		addLocaleSelections(configForm);
		
		// show turnover checkbox
		configForm.add(new CheckBox("config.showTurnover"));		
		
		// reply sender
		RequiredTextField mailFrom = new RequiredTextField("config.mailFrom");
		mailFrom.add(EmailAddressValidator.getInstance());
		configForm.add(mailFrom);
		configForm.add(new AjaxFormComponentFeedbackIndicator("mailFromError", mailFrom));
		
		// smtp server, port, username, pass
		TextField mailSmtp = new RequiredTextField("config.mailSmtp");
		configForm.add(new AjaxFormComponentFeedbackIndicator("mailSmtpValidationError", mailSmtp));
		configForm.add(mailSmtp);

		TextField smtpPort = new RequiredTextField("config.smtpPort");
		configForm.add(new AjaxFormComponentFeedbackIndicator("smtpPortValidationError", mailSmtp));
		smtpPort.setType(Float.class);
		smtpPort.add(NumberValidator.POSITIVE);
		configForm.add(smtpPort);
		
		
		configForm.add(new TextField("config.smtpUsername"));
		configForm.add(new TextField("config.smtpPassword"));
		addTestMailSettingsButton(configForm);
		
		// working hours
		TextField workHours = new RequiredTextField("config.completeDayHours");
		workHours.setType(Float.class);
		workHours.add(new ValidatingFormComponentAjaxBehavior());
		workHours.add(NumberValidator.POSITIVE);
		workHours.add(new NumberValidator.MaximumValidator(24));
		configForm.add(new AjaxFormComponentFeedbackIndicator("workHoursValidationError", workHours));
		configForm.add(workHours);
		
		setSubmitButton(configForm);
		
		parent.add(configForm);
		
		serverMessage = new WebComponent("serverMessage");
		serverMessage.setOutputMarkupId(true);
		configForm.add(serverMessage);
	}
	
	/**
	 * Add test mail button
	 * @param form
	 */
	private void addTestMailSettingsButton(Form form)
	{
		form.add(new AjaxButton("testMail", form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				mailService.mailTestMessage(configBackingBean.getConfig());
				
				Label replacementLabel = new Label("serverMessage", new ResourceModel("admin.config.testSmtpSent"));
				replacementLabel.setOutputMarkupId(true);
				replacementLabel.add(new SimpleAttributeModifier("class", "whiteText"));
				serverMessage.replaceWith(replacementLabel);
				serverMessage = replacementLabel;
				target.addComponent(serverMessage);
			}
		});
	}
	
	/**
	 * Set ajax submit button
	 * @param form
	 * @param dbConfig
	 */
	@SuppressWarnings("serial")
	private void setSubmitButton(Form form)
	{
		form.add(new AjaxButton("submitButton", form)
		{
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				IModel msgModel;

				if (!configBackingBean.getConfig().isInDemoMode())
				{
					try
					{
						configService.persistConfiguration(configBackingBean.getConfig());
						msgModel = new ResourceModel("general.dataSaved");
					}
					catch (Throwable t)
					{
						logger.error("While saving config", t);
						msgModel = new ResourceModel("saveError");
					}
					
					getEhourWebSession().reloadConfig();
					
					Label replacementLabel = new Label("serverMessage", msgModel);
					replacementLabel.setOutputMarkupId(true);
					replacementLabel.add(new SimpleAttributeModifier("class", "whiteText"));
					serverMessage.replaceWith(replacementLabel);
					serverMessage = replacementLabel;
					target.addComponent(serverMessage);
				}
            }		


			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				if (configBackingBean.getConfig().isInDemoMode())
				{
					return new DemoDecorator(new ResourceModel("general.demoMode"));
				}
				else
				{
					return new LoadingSpinnerDecorator();
				}
			}			
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form)
			{
				System.out.println("ee");
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
	private void addLocaleSelections(Form configForm)
	{
		final DropDownChoice	localeDropDownChoice;
		final DropDownChoice	languageDropDownChoice;
		final AjaxCheckBox		onlyTranslationsBox;
		final DropDownChoice	currencyDropDownChoice;
		final Label				dateFormat;
		
		configForm.setOutputMarkupId(true);

		// currency dropdown
		currencyDropDownChoice = new DropDownChoice("config.currency",
											new PropertyModel(configBackingBean, "availableCurrencies"),
											new LocaleChoiceRenderer(2));
		currencyDropDownChoice.setOutputMarkupId(true);
		configForm.add(currencyDropDownChoice);
		
		// date format example
		dateFormat = new Label("dateFormat", 
							new DateModel(new Model(new Date()), configBackingBean.getLocaleCountry(), DateModel.DATESTYLE_LONG ) );
		dateFormat.setOutputMarkupId(true);
		configForm.add(dateFormat);

		// language selection
		languageDropDownChoice = new DropDownChoice("localeLanguage",
													new PropertyModel(configBackingBean, "localeLanguage"),
													new PropertyModel(configBackingBean, "availableLanguages"),
													new LocaleChoiceRenderer(1)); 

		// locale selection
		localeDropDownChoice = new DropDownChoice("localeCountry",
													new PropertyModel(configBackingBean, "availableLocales"),
													new LocaleChoiceRenderer(0)); 
		localeDropDownChoice.setOutputMarkupId(true);
		localeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				// update the date format example
				dateFormat.setModel(new DateModel(new Model(new Date()), configBackingBean.getLocaleCountry(), DateModel.DATESTYLE_LONG ) );
				target.addComponent(dateFormat);
				
				// refresh langugae
				target.addComponent(languageDropDownChoice);
				
				// and currency
				if (configBackingBean.getLocaleCountry().getCountry() != null)
				{
					configBackingBean.setCurrency(configBackingBean.getLocaleCountry());
					target.addComponent(currencyDropDownChoice);
				}
			}
		});
		
		configForm.add(localeDropDownChoice);

		// behaviour for language selection
		languageDropDownChoice.setEnabled(!configBackingBean.getConfig().isDontForceLanguage());
		languageDropDownChoice.setOutputMarkupId(true);
		languageDropDownChoice.setRequired(true);
		languageDropDownChoice.setLabel(new ResourceModel("admin.config.locale.languageLabel"));
		
		configForm.add(new AjaxFormComponentFeedbackIndicator("localeLanguageValidationError", localeDropDownChoice));
		configForm.add(languageDropDownChoice);		
		
		// only translations
		onlyTranslationsBox = new AjaxCheckBox("onlyTranslations", new PropertyModel(configBackingBean, "translationsOnly"))
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(languageDropDownChoice);
			}
		};
		onlyTranslationsBox.setOutputMarkupId(true);
		onlyTranslationsBox.setEnabled(!configBackingBean.getConfig().isDontForceLanguage());
		configForm.add(onlyTranslationsBox);
		
		// don't force locale checkbox
		configForm.add(new AjaxCheckBox("config.dontForceLanguage")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				languageDropDownChoice.setEnabled(!configBackingBean.getConfig().isDontForceLanguage());
				target.addComponent(languageDropDownChoice);
				
				onlyTranslationsBox.setEnabled(!configBackingBean.getConfig().isDontForceLanguage());
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
		
		// type == 0 -> country, 1 => language, 2=> currency
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
            else if (type == 1)
            {
	            display = locale.getDisplayLanguage();
            }
            else
            {
            	Currency curr = Currency.getInstance(locale);
            	display = locale.getDisplayCountry() + ": " +  curr.getSymbol(locale);
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
    		
    		if (type == 0)
    		{
    			return locale.getCountry() + "_" + locale.getLanguage();
    		}
    		else if (type == 1)
    		{
    			return locale.getLanguage();
    		}
    		else
    		{
    			return locale.getCountry();
    		}
    	}
    }

	/**
	 * 
	 * @return
	 */
	private EhourConfigStub getDbConfig()
	{
		return configService.getConfiguration();
	}
}
