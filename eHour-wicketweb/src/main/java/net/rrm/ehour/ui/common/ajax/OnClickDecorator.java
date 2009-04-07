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

package net.rrm.ehour.ui.common.ajax;

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
	
	/**
	 * 
	 * @param preAjaxMethod JS method to be invoked before the ajax request is made
	 * @param arguments arguments to be passed to pre-ajax
	 * @param postSuccesMethod JS method called after ajax req
	 * @param postFailMethod upon failure
	 */
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
			
			if (arguments != null)
			{
				sb.append("(");
				
				for (int i = 0; i < arguments.length; i++, sb.append(((i < arguments.length) ? "," : "")))
				{
					sb.append(arguments[i]);
				}
				
				sb.append(");");
			}
			else
			{
				sb.append(";");
			}
			sb.append(script);
			
			return sb.toString();
		}
		else
		{
			return script;
		}
	}
}
