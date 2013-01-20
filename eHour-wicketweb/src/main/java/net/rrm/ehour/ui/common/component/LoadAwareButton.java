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

import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;

/**
 * AjaxFallbackButton which adds the loading rotating anim when loading
 * and adds the form to the AjaxRequestTarget on validation errors
 *
 * @author Thies
 */
public abstract class LoadAwareButton extends AjaxButton {
    private static final long serialVersionUID = -6504165692150025275L;

    public LoadAwareButton(String id, Form<?> form) {
        super(id, form);
    }

    @Override
    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        super.updateAjaxAttributes(attributes);

        attributes.getAjaxCallListeners().add(new LoadingSpinnerDecorator());
    }

    @Override
    protected void onError(AjaxRequestTarget target, Form<?> form) {
        target.add(form);
    }
}
