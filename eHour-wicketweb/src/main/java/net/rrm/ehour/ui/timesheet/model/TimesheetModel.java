<<<<<<< HEAD
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

package net.rrm.ehour.ui.timesheet.model;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.timesheet.service.IOverviewTimesheet;
import net.rrm.ehour.timesheet.service.IPersistTimesheet;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebUtils;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.timesheet.dto.TimesheetFactory;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Model that holds the timesheet
 */

public class TimesheetModel implements IModel<Timesheet> {
    private static final long serialVersionUID = 4134613450587087107L;

    @SpringBean
    private transient IPersistTimesheet persistTimesheet;

    @SpringBean
    private transient IOverviewTimesheet overviewTimesheet;

    private User user;
    private Calendar forWeek;
    private Timesheet timesheet;

    public TimesheetModel(User user, Calendar forWeek) {
        WebUtils.springInjection(this);

        this.user = user;
        this.forWeek = forWeek;

        timesheet = load();
    }

    public List<ProjectAssignmentStatus> persistTimesheet() {
        WebUtils.springInjection(this);

        Timesheet timesheet = getObject();

        return persistTimesheet.persistTimesheetWeek(timesheet.getTimesheetEntries(),
                timesheet.getCommentForPersist(),
                new DateRange(timesheet.getWeekStart(), timesheet.getWeekEnd()));
    }

    public Date getWeekStart() {
        return getObject().getWeekStart();
    }

    public Date getWeekEnd() {
        return getObject().getWeekEnd();
    }


    private Timesheet load() {
        EhourConfig config = EhourWebSession.getEhourConfig();
        WeekOverview weekOverview = overviewTimesheet.getWeekOverview(user, forWeek);

        Timesheet timesheet = new TimesheetFactory(config, weekOverview).createTimesheet();

        if (timesheet.getComment() == null) {
            TimesheetComment comment = new TimesheetComment();
            comment.setNewComment(Boolean.TRUE);
            timesheet.setComment(comment);
        }

        return timesheet;
    }


    public Timesheet getObject() {
        return timesheet;
    }

    public void setObject(Timesheet sheet) {
        this.timesheet = sheet;

    }

    public void detach() {
        overviewTimesheet = null;
    }
}
=======
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

package net.rrm.ehour.ui.timesheet.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.activity.status.ActivityStatus;
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

public class TimesheetModel implements IModel<Timesheet>
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
	public List<ActivityStatus> persistTimesheet()
	{
		CommonWebUtil.springInjection(this);
		
		Timesheet timesheet = getObject();
		
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
		return getObject().getWeekStart();
	}

	/**
	 * 
	 * @return
	 */
	public Date getWeekEnd()
	{
		return getObject().getWeekEnd();
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


	public Timesheet getObject()
	{
		return timesheet;
	}

	public void setObject(Timesheet sheet)
	{
		this.timesheet = sheet;
		
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
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure
