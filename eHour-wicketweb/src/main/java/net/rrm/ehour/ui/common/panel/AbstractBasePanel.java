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

package net.rrm.ehour.ui.common.panel;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Base Panel
 */

public abstract class AbstractBasePanel<T> extends Panel {
    private static final long serialVersionUID = 8437967307064528806L;

    public AbstractBasePanel(String id) {
        super(id);
    }

    public AbstractBasePanel(String id, IModel<T> model) {
        super(id, model);
    }

    protected final EhourWebSession getEhourWebSession() {
        return EhourWebSession.getSession();
    }

    @SuppressWarnings("unchecked")
    public final IModel<T> getPanelModel() {
        return (IModel<T>) super.getDefaultModel();
    }

    public final T getPanelModelObject() {
        return getPanelModel().getObject();
    }

    protected final EhourConfig getConfig() {
        return EhourWebSession.getEhourConfig();
    }
}
