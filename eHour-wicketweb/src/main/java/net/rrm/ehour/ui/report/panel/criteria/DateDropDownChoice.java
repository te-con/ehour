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

package net.rrm.ehour.ui.report.panel.criteria;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Dropdown choice which generates an event when the dropdown was changed
 */

public class DateDropDownChoice<T> extends DropDownChoice<T> {
    private static final long serialVersionUID = 1L;

    public DateDropDownChoice(final String id, final List<T> data, final IChoiceRenderer<T> renderer) {
        super(id, data, renderer);
    }

    public DateDropDownChoice(final String id, final IModel<T> model, final List<T> data, final IChoiceRenderer<T> renderer) {
        super(id, model, data, renderer);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 507045565542332885L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                send(DateDropDownChoice.this.getPage(), Broadcast.DEPTH, new DateChangedPayload(target));
            }
        });
    }

    public static class DateChangedPayload {
        private final AjaxRequestTarget target;

        public DateChangedPayload(AjaxRequestTarget target) {
            this.target = target;
        }

        public AjaxRequestTarget getTarget() {
            return target;
        }
    }
}
