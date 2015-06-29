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

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Panel that output it's markup id
 * <p/>
 * Created on Feb 1, 2009, 3:48:32 AM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class PlaceholderPanel extends Panel {
    private static final long serialVersionUID = 1516231242188706097L;

    public PlaceholderPanel(String id) {
        super(id);

        setOutputMarkupId(true);
    }

    public static PlaceholderPanel hidden(String id) {
        PlaceholderPanel placeholderPanel = new PlaceholderPanel(id);
        placeholderPanel.setVisible(false);
        return placeholderPanel;
    }
}
