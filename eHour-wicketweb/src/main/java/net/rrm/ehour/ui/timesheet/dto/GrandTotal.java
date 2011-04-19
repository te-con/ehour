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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.model.PropertyModel;

/**
 * GrandTotal of all days
 * @author Thies
 *
 */
public class GrandTotal implements Serializable
{
	private static final long serialVersionUID = -8908496992290848165L;
	private Map<Integer, List<PropertyModel<Float>>>	weekMatrix;
	private Map<Integer, Integer> renderDayOrder;
	
	/**
	 * Default constructor initializing the weekmatrix
	 */
	public GrandTotal()
	{
		weekMatrix = new HashMap<Integer, List<PropertyModel<Float>>>();
		renderDayOrder= new HashMap<Integer, Integer>();
	}

	/**
	 * Add render vs index order. Render order can be different when Sunday is not the first day
	 * while Sunday is still has a key of 1 in the weekMatrix
	 * @param renderOrder
	 * @param indexOrder
	 */
	public void addOrder(Integer renderOrder, Integer indexOrder)
	{
		renderDayOrder.put(indexOrder, renderOrder);
	}
	
	/**
	 * 
	 * @param indexOrder
	 * @return
	 */
	public Integer getOrderForIndex(Integer indexOrder)
	{
		return  renderDayOrder.get(indexOrder);
	}
	
	/**
	 * 
	 * Add model for specific week 
	 * @param index
	 * @param model
	 */
	public void addValue(int index, PropertyModel<Float> model)
	{
		List<PropertyModel<Float>>	dayModels;
		
		if (weekMatrix.containsKey(index))
		{
			dayModels = weekMatrix.get(index);
		}
		else
		{
			dayModels = new ArrayList<PropertyModel<Float>>();
		}
		
		dayModels.add(model);
		
		weekMatrix.put(index, dayModels);
	}
	
	/**
	 * Get all the values for a single week
	 * @return
	 */
	public float[] getValues()
	{
		float[]				dayValues = new float[7];
		List<PropertyModel<Float>>	dayModels;
		Object				modelValue;
		
		for (Integer dayInWeek : weekMatrix.keySet())
		{
			dayModels = weekMatrix.get(dayInWeek);
			
			for (PropertyModel<Float> propertyModel : dayModels)
			{
				modelValue = propertyModel.getObject();
				
				if (modelValue != null)
				{
					dayValues[dayInWeek.intValue()] += ((Number)modelValue).floatValue();
				}
				
			}
		}
		
		return dayValues;
	}
	
	/**
	 * Get grand total, combination of all
	 * @return
	 */
	public float getGrandTotal()
	{
		float[]	values = getValues();
		float	val = 0;

        for (float value : values) {
            val += value;
        }
		
		return val;
	}
}