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

package net.rrm.ehour.ui.report.chart.rowkey;

import net.rrm.ehour.domain.User;

import org.apache.commons.lang.StringUtils;

/**
 * TODO 
 **/

public class UserRowKey extends ChartRowKey
{
	private	User 	user;
	
	public UserRowKey(User user)
	{
		this.user = user;
	}
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.web.report.charts.rowkey.ChartRowKey#getName()
	 */
	public String getName()
	{
		if (StringUtils.isBlank(user.getFirstName()))
		{
			return user.getLastName();
		}
		else
		{
			return user.getLastName() +", " + user.getFirstName().substring(0, 1).toUpperCase() + ".";
		}
	}

	public Integer getId()
	{
		return user.getUserId();
	}

}
