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
