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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * ServerMessage label which disappears after 5 seconds and is not visible when no content is provided
 *
 * @author Thies
 */
public class ServerMessageLabel extends Label {
    private static final long serialVersionUID = -6276174722682301972L;
    private boolean overrideVisibility = false;

    public ServerMessageLabel(String id, String cssClass) {
        this(id, cssClass, null);
    }

    public ServerMessageLabel(String id, String cssClass, IModel<String> model) {
        super(id, model);
        add(AttributeModifier.replace("class", cssClass));
        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

//        if (isVisible()) {
//            add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(3)) {
//                @Override
//                protected void onPostProcessTarget(AjaxRequestTarget target) {
//                    overrideVisibility = true;
//
//                    ServerMessageLabel.this.setEnabled(false);
//
//
//                        target.add(ServerMessageLabel.this);
//                }
//            });
//        }
    }

    public boolean isVisible() {
        return !overrideVisibility && getDefaultModel() != null && getDefaultModelObject() != null;
    }
}