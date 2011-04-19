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

package net.rrm.ehour.ui.common.panel.sidepanel;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.IModel;

/**
 * Blue navigation side panel
 * TODO convert to border
 **/

public abstract class SidePanel extends Panel
{
	private static final long serialVersionUID = 4573739445050690761L;


	public SidePanel(String id)
	{
		this(id, null);
	}

	public SidePanel(String id, IModel<?> model)
	{
		super(id, model);
		add(new StyleSheetReference("navSidePanelStyle", navSidePanelStyle()));
	}

	
	/**
	 * Create a style
	 * 
	 * @return a style
	 */
	protected ResourceReference navSidePanelStyle()
	{
		return new CompressedResourceReference(SidePanel.class, "style/sidePanel.css");
	}		
}
