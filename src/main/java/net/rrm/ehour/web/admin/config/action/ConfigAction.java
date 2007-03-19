/**
 * Created on Mar 17, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.web.admin.config.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.web.admin.config.form.ConfigForm;
import net.rrm.ehour.web.admin.config.util.LocaleComparator;

import org.apache.commons.lang.ArrayUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class ConfigAction extends BaseConfigAction
{
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response) 
	{
		ActionForward	fwd;
		ConfigForm		configForm = (ConfigForm)form;
		
		request.setAttribute("form", configForm);
		
		request.setAttribute("config", configService.getConfiguration());
		
		setAvailableCountries(request);
		setAvailableLanguages(request, configForm.isShowTranslationsOnly());

		fwd = mapping.findForward("success");
		
		return fwd;
	}
	
	
	/**
	 * Set available languages
	 * @param request
	 */
	private void setAvailableLanguages(HttpServletRequest request, boolean onlyAvailable)
	{
		SortedSet<Locale>	localeSet = new TreeSet<Locale>(new LocaleComparator());
		Locale[]			locales = Locale.getAvailableLocales();
		Map<String, Locale>	localeMap = new HashMap<String, Locale>();
		
		// remove all variants
		for (Locale locale : locales)
		{
			if (onlyAvailable && !ArrayUtils.contains(config.getAvailableTranslations(), locale.getLanguage()))
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
		
		for (Locale locale : localeMap.values())
		{
			localeSet.add(locale);
		}
		
		request.setAttribute("languages", localeSet);
	}
	
	/**
	 * Set available countries in the request context under countries
	 * @param request
	 */
	private void setAvailableCountries(HttpServletRequest request)
	{
		String[]		countries;
		List<Locale>	locales = new ArrayList<Locale>();
		Locale			localeCountry;
		
		countries = Locale.getISOCountries();
		
		for (String country: countries)
		{
			localeCountry = new Locale("", country);
			locales.add(localeCountry);
		}
		
		request.setAttribute("countries", locales);
	}
}
