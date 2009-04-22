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

package net.rrm.ehour.ui.admin.assignment.panel;

import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.util.WebWidth;

import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

/**
 * Same as NoEntrySelected just with a hidden dojo date picker. Bug in DOjo prevents
 * to load the dojo js through ajax, needs to be there on initial load
 **/

public class NoUserSelectedPanel extends Panel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoUserSelectedPanel(String id, String resourceId)
	{
		super(id);
		
		this.setOutputMarkupId(true);
		Border greyBorder = new GreyRoundedBorder("border", WebWidth.CONTENT_ADMIN_TAB);
		add(greyBorder);

		greyBorder.add(new Label("noEntry", new ResourceModel(resourceId)));

		// this is what we call a hack sir
        final DateTextField dateStart = new DateTextField("dummyDate", new Model(), new StyleDateConverter("S-", true));
        dateStart.add(new DatePicker());		
		greyBorder.add(dateStart);
		
	}
}
