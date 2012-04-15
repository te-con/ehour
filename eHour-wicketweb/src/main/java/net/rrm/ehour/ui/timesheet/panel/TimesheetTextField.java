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

package net.rrm.ehour.ui.timesheet.panel;

import net.rrm.ehour.ui.common.component.CommonModifiers;
import net.rrm.ehour.ui.timesheet.converter.TimesheetFloatConverter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * Timesheet textfield which remembers its previous validation state
 */

public class TimesheetTextField extends TextField<Float> {
    private static final long serialVersionUID = 7033801704569935582L;
    private boolean previousValidity = false;
    private Float previousValue;

    public TimesheetTextField(final String id, IModel<Float> model, int tabIndex) {
        super(id, model, Float.class);

        setConvertEmptyInputStringToNull(true);

        if (model != null) {
            previousValue = model.getObject();
        }

        add(CommonModifiers.tabIndexModifier(tabIndex));
    }

    @Override
    public IConverter getConverter(Class<?> c) {
        return TimesheetFloatConverter.getInstance();
    }

    /**
     * @return Is changed since previous submit
     */
    public boolean isValueChanged() {
        return ! new EqualsBuilder().append(getModelObject(), previousValue).isEquals();
    }

    public void rememberCurrentValue() {
        previousValue = getModelObject();
    }

    public void rememberCurrentValidity() {
        previousValidity = isValid();
    }

    public boolean isPreviousValid() {
        return previousValidity;
    }

    @Override
    public boolean isInputNullable() {
        return true;
    }
}
