/**
 * Created on Oct 29, 2008
 * Author: Thies
 *
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

package net.rrm.ehour.ui.timesheet.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.timesheet.util.TimesheetAssembler;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Model that holds the timesheet
 **/

public class TimesheetModel implements IModel
{
	private static final long serialVersionUID = 4134613450587087107L;

	@SpringBean
	private transient TimesheetService timesheetService;
	private	User				user;
	private Calendar			forWeek;
	private Timesheet			timesheet;
	
	/**
	 * 
	 */
	public TimesheetModel(User user, Calendar forWeek)
	{
		CommonWebUtil.springInjection(this);
		
		this.user = user;
		this.forWeek = forWeek;
		
		timesheet = load();
	}

	/**
	 * Perist Timesheet
	 * @return 
	 */
	public List<ProjectAssignmentStatus> persistTimesheet()
	{
		CommonWebUtil.springInjection(this);
		
		Timesheet timesheet = (Timesheet)getObject();
		
		return timesheetService.persistTimesheetWeek(timesheet.getTimesheetEntries(), 
												timesheet.getCommentForPersist(),
												new DateRange(timesheet.getWeekStart(), timesheet.getWeekEnd()));
	}
	
	/**
	 * 
	 * @return
	 */
	public Date getWeekStart()
	{
		return ((Timesheet)getObject()).getWeekStart();
	}

	/**
	 * 
	 * @return
	 */
	public Date getWeekEnd()
	{
		return ((Timesheet)getObject()).getWeekEnd();
	}

	/**
	 * 
	 * @return
	 */
	private Timesheet load()
	{
		WeekOverview	weekOverview;
		Timesheet		timesheet;
		EhourConfig		config;
		
		config = EhourWebSession.getSession().getEhourConfig();
		
		weekOverview = timesheetService.getWeekOverview(user, forWeek, config);
		
		timesheet = getTimesheetAssembler(config).createTimesheetForm(weekOverview);		
		
		if (timesheet.getComment() == null)
		{
			TimesheetComment comment = new TimesheetComment();
			comment.setNewComment(Boolean.TRUE);
			timesheet.setComment(comment);
		}
		
		return timesheet;
	}


	public Object getObject()
	{
		return timesheet;
	}

	public void setObject(Object object)
	{
		this.timesheet = (Timesheet)object;
		
	}

	public void detach()
	{
		timesheetService = null;
	}	
	
	
	/**
	 * Get timesheet assembler
	 * @param config
	 * @return
	 */
	private TimesheetAssembler getTimesheetAssembler(EhourConfig config)
	{
		return new TimesheetAssembler(config);
	}
	
}
