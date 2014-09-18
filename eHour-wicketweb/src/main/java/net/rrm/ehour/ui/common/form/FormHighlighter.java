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

package net.rrm.ehour.ui.common.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.io.Serializable;

/**
 * Marks invalid form entries red
 *
 * @author Thies
 */
public class FormHighlighter implements IVisitor<FormComponent<?>, Object>, Serializable {
    private static final long serialVersionUID = 6905807838333630105L;

    private transient AjaxRequestTarget target;

    public FormHighlighter(AjaxRequestTarget target) {
        this.target = target;
    }

    @Override
    public void component(FormComponent<?> formComponent, IVisit<Object> visit) {
        if (target != null) {
            // paint it red if invalid
            if (!formComponent.isValid()) {
                formComponent.add(getColorModifier("#ff0000"));

                if (formComponent instanceof TextFieldWithHistory) {
                    final TextFieldWithHistory textFieldWithHistory = (TextFieldWithHistory) formComponent;
                    textFieldWithHistory.rememberCurrentValidity();
                    textFieldWithHistory.rememberCurrentValue();
                }

                target.add(formComponent);
            }
            // reset color if it was invalid and needs to be reset
            else if (formComponent instanceof TextFieldWithHistory) {
                TextFieldWithHistory ttField = (TextFieldWithHistory) formComponent;

                if (ttField.isValueChanged()) {
                    if (!ttField.isPreviousValid()) {
                        formComponent.add(getColorModifier("#536e87;"));
                        ttField.rememberCurrentValidity();
                    }

                    target.add(formComponent);
                }

                ttField.rememberCurrentValue();
            }
        }
    }

    @SuppressWarnings("serial")
    private AttributeModifier getColorModifier(final String color) {
        return new AttributeModifier("style", new AbstractReadOnlyModel<String>() {
            public String getObject() {
                return "color: " + color;
            }
        });
    }
}
