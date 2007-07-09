/**
 * Created on Jul 9, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.timesheet.dto;

import java.io.Serializable;

/**
 * 
 * @author Thies
 *
 */
public class GrandTotal implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8908496992290848165L;
	private float[]	grandTotals = new float[7];
	
	public void addValue(int index, float value)
	{
		grandTotals[index] += value;
	}
	
	public float[] getValues()
	{
		return grandTotals;
	}
	
	public float getGrandTotal()
	{
		float val = 0;
		
		for (int i = 0; i < grandTotals.length; i++)
		{
			val += grandTotals[i];
		}
		
		return val;
	}
}