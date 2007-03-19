/**
 * Created on Mar 19, 2007
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

package net.rrm.ehour.web.admin.config.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class ConfigForm extends ActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5616191733233138171L;
	private String	language;
	private	boolean	showTranslationsOnly;
	private	String	currency;
	private	boolean	noForce;
	private boolean	showTurnOver;
	private boolean	fromForm;
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		showTranslationsOnly = false;
		fromForm = false;
		showTurnOver = false;
		noForce = false;
	}
	
	/**
	 * @return the language
	 */
	public String getLanguage()
	{
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language)
	{
		this.language = language;
	}
	/**
	 * @return the showTranslationsOnly
	 */
	public boolean isShowTranslationsOnly()
	{
		return showTranslationsOnly;
	}
	/**
	 * @param showTranslationsOnly the showTranslationsOnly to set
	 */
	public void setShowTranslationsOnly(boolean showTranslationsOnly)
	{
		this.showTranslationsOnly = showTranslationsOnly;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency()
	{
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	/**
	 * @return the noForce
	 */
	public boolean isNoForce()
	{
		return noForce;
	}

	/**
	 * @param noForce the noForce to set
	 */
	public void setNoForce(boolean noForce)
	{
		this.noForce = noForce;
	}

	/**
	 * @return the fromForm
	 */
	public boolean isFromForm()
	{
		return fromForm;
	}

	/**
	 * @param fromForm the fromForm to set
	 */
	public void setFromForm(boolean fromForm)
	{
		this.fromForm = fromForm;
	}

	/**
	 * @return the hideTurnOver
	 */
	public boolean isShowTurnOver()
	{
		return showTurnOver;
	}

	/**
	 * @param hideTurnOver the hideTurnOver to set
	 */
	public void setShowTurnOver(boolean hideTurnOver)
	{
		this.showTurnOver = hideTurnOver;
	}
	
}
