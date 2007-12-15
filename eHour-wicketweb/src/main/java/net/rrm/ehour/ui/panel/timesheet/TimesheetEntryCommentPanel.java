/**
 * Created on Dec 14, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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

package net.rrm.ehour.ui.panel.timesheet;

import net.rrm.ehour.ui.component.KeepAliveTextArea;
import net.rrm.ehour.ui.panel.timesheet.dto.TimesheetRow;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * Comments panel for timesheet entries
 **/

public class TimesheetEntryCommentPanel extends Panel
{
	private static final long serialVersionUID = -3287327439195576543L;

	public TimesheetEntryCommentPanel(String id, TimesheetRow row, int index)
	{
		super(id);
		
		TextArea	textArea = new KeepAliveTextArea("comments", 
														new PropertyModel(row, "timesheetCells[" + index + "].timesheetEntry.comments"));

		add(textArea);
	}
}
