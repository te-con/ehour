/**
 * Created on Jun 29, 2007
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

package net.rrm.ehour.ui.ajax;

import org.apache.wicket.ajax.IAjaxCallDecorator;

/**
 * Generic onClick decorator
 **/

public class OnClickDecorator  implements IAjaxCallDecorator
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5149085181617984033L;
	private	String[]	arguments;
	private String		preAjaxMethod;
	private	String		succesMethod;
	private	String		failMethod;

	public OnClickDecorator(String preAjaxMethod, String argument)
	{
		this(preAjaxMethod, new String[]{argument});
	}

	public OnClickDecorator(String preAjaxMethod, String[] arguments)
	{
		this(preAjaxMethod, arguments, null, null);
	}
	
	
	public OnClickDecorator(String preAjaxMethod, String[] arguments, String postSuccesMethod, String postFailMethod)
	{
		this.arguments = arguments;
		this.preAjaxMethod = preAjaxMethod;
		this.succesMethod = postSuccesMethod;
		this.failMethod = postFailMethod;
	}

	/**
	 * 
	 */
	public CharSequence decorateOnFailureScript(CharSequence script)
	{
		if (failMethod != null)
		{
			return failMethod + "();" + script;
		}
		else
		{
			return script;
		}
	}

	/**
	 * 
	 */
	
	public CharSequence decorateOnSuccessScript(CharSequence script)
	{
		if (succesMethod != null)
		{
			return succesMethod + "();" + script;
		}
		else
		{
			return script;
		}
	}

	/**
	 * Called before ajax req is sent
	 */
	public CharSequence decorateScript(CharSequence script)
	{
		if (preAjaxMethod != null)
		{
			StringBuffer	sb = new StringBuffer(preAjaxMethod);
			sb.append("(");
			
			for (int i = 0; i < arguments.length; i++, sb.append(((i < arguments.length) ? "," : "")))
			{
				sb.append(arguments[i]);
			}
			
			sb.append(");");
			sb.append(script);
			
			return sb.toString();
		}
		else
		{
			return script;
		}
	}
}
