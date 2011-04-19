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

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Tooltip label with info image next to it.
 * Displays a tooltip when hovered over the text using SweetTitles
 * When the tooltipText is empty the tooltip img button is now shown
 * 
 * Created on Feb 1, 2009, 6:57:30 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class TooltipLabel extends Panel
{
	private static final long serialVersionUID = -2407607082770130038L;

	public TooltipLabel(final String id, String label, String tooltipText)
	{
        super(id);

        WebMarkupContainer title = new WebMarkupContainer("title");

        if (StringUtils.isNotBlank(tooltipText)) {
            title.add(new SimpleAttributeModifier("title", tooltipText));
        }

        add(title);
        title.add(new Label("content", label));
	}
}
