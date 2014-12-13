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

package net.rrm.ehour.ui.timesheet.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * GrandTotal of all days
 *
 * @author Thies
 */
public class GrandTotal implements Serializable {
    private static final long serialVersionUID = -8908496992290848165L;
    private Map<Integer, List<PropertyModel<Float>>> weekMatrix;
    private List<TimesheetCell> addedCells;

    public GrandTotal() {
        weekMatrix = Maps.newHashMap();
        addedCells = Lists.newArrayList();
    }

    /**
     * Add model for specific week
     */
    public void addValue(int index, TimesheetCell timesheetCell, PropertyModel<Float> model) {
        if (addedCells.contains(timesheetCell)) {
            return;
        }

        List<PropertyModel<Float>> dayModels;

        if (weekMatrix.containsKey(index)) {
            dayModels = weekMatrix.get(index);
        } else {
            dayModels = Lists.newArrayList();
        }

        dayModels.add(model);
        weekMatrix.put(index, dayModels);
        addedCells.add(timesheetCell);
    }

    /**
     * Get all the values for a single week
     */
    public float[] getValues() {
        float[] dayValues = new float[7];
        List<PropertyModel<Float>> dayModels;
        Object modelValue;

        for (Integer dayInWeek : weekMatrix.keySet()) {
            dayModels = weekMatrix.get(dayInWeek);

            for (PropertyModel<Float> propertyModel : dayModels) {
                modelValue = propertyModel.getObject();

                if (modelValue != null) {
                    dayValues[dayInWeek] += ((Number) modelValue).floatValue();
                }

            }
        }

        return dayValues;
    }

    /**
     * Get grand total, combination of all
     */
    @SuppressWarnings("UnusedDeclaration")
    public float getGrandTotal() {
        float val = 0;

        for (float value : getValues()) {
            val += value;
        }

        return val;
    }
}