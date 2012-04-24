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

package net.rrm.ehour.ui.timesheet.common;

import net.rrm.ehour.ui.timesheet.panel.TimesheetTextField;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IFormVisitorParticipant;
import org.apache.wicket.model.AbstractReadOnlyModel;

import java.io.Serializable;

/**
 * Marks invalid form entries red
 *
 * @author Thies
 */
public class FormHighlighter implements FormComponent.IVisitor, Serializable {
    private static final long serialVersionUID = 6905807838333630105L;

    private transient AjaxRequestTarget target;

    private	static final Logger LOGGER = Logger.getLogger(FormHighlighter.class);

    public FormHighlighter(AjaxRequestTarget target) {
        this.target = target;
    }

    public Object formComponent(IFormVisitorParticipant visitor) {
        FormComponent<?> formComponent = (FormComponent<?>) visitor;

        if (target != null) {
            String markupId = formComponent.getMarkupId();

            // paint it red if invalid
            if (!formComponent.isValid()) {
                LOGGER.trace(markupId + " is not valid");
                formComponent.add(getColorModifier("#ff0000"));

                if (formComponent instanceof TimesheetTextField) {
                    ((TimesheetTextField) formComponent).rememberCurrentValidity();
                    ((TimesheetTextField) formComponent).rememberCurrentValue();
                }

                target.addComponent(formComponent);
            }
            // reset color if it was invalid and needs to be reset
            else if (formComponent instanceof TimesheetTextField) {
                TimesheetTextField ttField = (TimesheetTextField) formComponent;

                if (ttField.isValueChanged()) {
                    LOGGER.trace(markupId + " is changed");
                    if (!ttField.isPreviousValid()) {
                        LOGGER.trace(markupId + " was invalid");
                        formComponent.add(getColorModifier("#536e87;"));
                        ttField.rememberCurrentValidity();
                    }

                    target.addComponent(formComponent);
                }else {
                    LOGGER.trace(markupId + " is not changed");
                }

                ttField.rememberCurrentValue();
            }
        }

        return formComponent;
    }

    @SuppressWarnings("serial")
    private AttributeModifier getColorModifier(final String color) {
        return new AttributeModifier("style", true, new AbstractReadOnlyModel<String>() {
            public String getObject() {
                return "color: " + color;
            }
        });
    }

}
