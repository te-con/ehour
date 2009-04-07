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

package net.rrm.ehour.ui.common.util;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

/**
 * HTML utility methods & constants
 **/

public class HtmlUtil
{
	public final static String HTML_NBSP = "&nbsp;";
	public final static String HTML_BR = "<br />";
	
	/**
	 * Get a &ampnbsp;label
	 * @param id
	 * @return
	 */
	public static Label getNbspLabel(String id)
	{
		Label label = new Label(id, HTML_NBSP);
		label.setEscapeModelStrings(false);
		
		return label;
	}
	
	/**
	 * Get a <br />label
	 * @param id
	 * @return
	 */
	public static Label getBrLabel(String id)
	{
		Label label = new Label(id, HTML_BR);
		label.setEscapeModelStrings(false);
		
		return label;
	}
	
	/**
	 * Get an invisible link
	 * @param id
	 * @return
	 */
	public static Link getInvisibleLink(String id)
	{
		Link link = new Link(id)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				// not visible anyway
			}
		};
		
		link.setVisible(false);
		
		return link;
	}	
	
	/**
	 * Get an invisible label
	 */
	public static Label getInvisibleLabel(String id)
	{
		Label label = new Label(id, "");
		label.setVisible(false);
		return label;
	}

}
