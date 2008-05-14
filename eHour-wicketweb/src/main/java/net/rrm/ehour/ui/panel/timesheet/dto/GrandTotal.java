/**
 * Created on Jul 9, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.timesheet.dto;

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
	private Map<Integer, List<PropertyModel>>	weekMatrix;
	private Map<Integer, Integer> renderDayOrder;
	
	/**
	 * Default constructor initializing the weekmatrix
	 */
	public GrandTotal()
	{
		weekMatrix = new HashMap<Integer, List<PropertyModel>>();
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
	public void addValue(int index, PropertyModel model)
	{
		List<PropertyModel>	dayModels;
		
		if (weekMatrix.containsKey(index))
		{
			dayModels = weekMatrix.get(index);
		}
		else
		{
			dayModels = new ArrayList<PropertyModel>();
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
		List<PropertyModel>	dayModels;
		Object				modelValue;
		
		for (Integer dayInWeek : weekMatrix.keySet())
		{
			dayModels = weekMatrix.get(dayInWeek);
			
			for (PropertyModel propertyModel : dayModels)
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
		
		for (int i = 0; i < values.length; i++)
		{
			val += values[i];
		}
		
		return val;
	}
}