/**
 * Created on Nov 5, 2006
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

package net.rrm.ehour.data;

/**
 * TODO 
 **/

public class KeyValue
{
	private	Object	key;
	private	Object	value;
	
	public KeyValue()
	{
		
	}
	
	public KeyValue(Object key, Object value)
	{
		this.key = key;
		this.value = value;
	}
	
	public Object getKey()
	{
		return key;
	}
	public void setKey(Object key)
	{
		this.key = key;
	}
	public Object getValue()
	{
		return value;
	}
	public void setValue(Object value)
	{
		this.value = value;
	}
}
