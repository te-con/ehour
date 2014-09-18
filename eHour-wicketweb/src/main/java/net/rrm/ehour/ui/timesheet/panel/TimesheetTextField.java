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
import net.rrm.ehour.ui.common.converter.FloatConverter;
import net.rrm.ehour.ui.common.form.TextFieldWithHistory;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * Timesheet textfield which remembers its previous validation state
 */

public class TimesheetTextField extends TextFieldWithHistory<Float> {
    private static final long serialVersionUID = 7033801704569935582L;

    public TimesheetTextField(final String id, IModel<Float> model, int tabIndex) {
        super(id, model, Float.class);

        setConvertEmptyInputStringToNull(true);

        setPreviousValue(getModelObject() != null ? getModelObject().toString() : "");

        add(CommonModifiers.tabIndexModifier(tabIndex));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        return (IConverter<C>) new FloatConverter("");
    }

    @Override
    public boolean isInputNullable() {
        return true;
    }
}
