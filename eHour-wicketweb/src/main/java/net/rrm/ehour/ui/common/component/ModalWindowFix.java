/**
 * Created on Dec 17, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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

package net.rrm.ehour.ui.common.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

/**
 * Fix for https://issues.apache.org/jira/browse/WICKET-1003
 * ModalWindow not closable in some cases when it contains a form
 **/

public class ModalWindowFix extends ModalWindow
{
	private static final long serialVersionUID = -2241630406885326835L;

	public ModalWindowFix(String id)
	{
		super(id);
	}
	

	/**
	 * Closes the modal window.
	 * 
	 * @param target
	 *            Request target associated with current ajax request.
	 */
	@Override
	public void close(AjaxRequestTarget target)
	{
		target.appendJavascript(getCloseJavacript());
//		shown = false;
	}
	
	/**
	 * See jira issue for JS
	 * @return javascript that closes current modal window
	 */
	private String getCloseJavacript()
	{
	     return "var win;var par\n" //
	        + "try {\n"
	        + " win = window.parent.Wicket.Window;par=window.parent;\n"
	        + "} catch (ignore) {\n"
	        + " }\n"
	        + "if (typeof(win) == \"undefined\" || typeof(win.current) == \"undefined\") {\n"
	        + " try {\n"
	        + " win = window.Wicket.Window;par=window\n"
	        + " } catch (ignore) {\n"
	        + " }\n"
	        + "}\n"
	        + "if (typeof(win) != \"undefined\" && typeof(win.current) != \"undefined\") {\n"
	        + " par.setTimeout(function() {\n"
	        + " win.current.close();\n"
	        + " }, 0);\n" + "}";
	}
}
