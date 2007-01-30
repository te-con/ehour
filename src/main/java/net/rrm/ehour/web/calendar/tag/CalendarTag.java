/**
 * Created on Dec 27, 2006
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

package net.rrm.ehour.web.calendar.tag;

import java.util.Calendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * TODO 
 **/

public abstract class CalendarTag extends TagSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2422922719761190399L;
	protected Calendar 	calendar;

	// HTML constants
	protected String	HTML_NBSP = "&nbsp;";
	protected String	HTML_CELL_OPEN = "<td>";
	protected String	HTML_CELL_CLOSE = "</td>";
	protected String	HTML_ROW_CLOSE = "</tr>";
	protected String	HTML_DIV_CLOSE = "</div>";
	protected String	HTML_BR = "<br>";
	
	/**
	 * 
	 * @return int
	 * @throws JspException
	 */
	@Override
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}


	public Calendar getCalendar()
	{
		return calendar;
	}

	public void setCalendar(Calendar calendar)
	{
		this.calendar = calendar;
	}
}
