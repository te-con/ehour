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

package net.rrm.ehour.ui.common.border;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * CSS in ehour.css as it's very common
 */

public class GreyRoundedWideBorder extends Border {
    private static final long serialVersionUID = 7184643596615028876L;

    /**
     * Default border. No title with default width
     */
    public GreyRoundedWideBorder(String id) {
        this(id, new Model<String>());
    }


    /**
     * With title
     */
    public GreyRoundedWideBorder(String id, String title) {
        this(id, new Model<>(title));
    }


    /**
     * With title model
     */
    public GreyRoundedWideBorder(String id, IModel<String> title) {
        this(id, title, true);
    }

    public GreyRoundedWideBorder(String id, IModel<String> title, boolean showTitle) {
        super(id);

        Label label = new Label("greyTabTitle", title);
        addToBorder(label);
        label.setVisible(showTitle);

    }
}
