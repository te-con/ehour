/**
 * Created on Jan 20, 2008
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

package net.rrm.ehour.ui.panel.timesheet;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.timesheet.dto.CustomerFoldPreferenceList;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.common.DummyDataGenerator;
import net.rrm.ehour.user.service.UserService;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Before;
import org.junit.Test;

public class TimesheetPanelTest extends BaseUIWicketTester
{
	private TimesheetService	timesheetService;
	private UserService			userService;
	private User				user;
	private Calendar			cal;
	
	@Before
	public void setup()
	{
		timesheetService = createMock(TimesheetService.class);
		mockContext.putBean("timesheetService", timesheetService);

		userService = createMock(UserService.class);
		mockContext.putBean("userService", userService);
		
		user = new User(1);
		cal = new GregorianCalendar();
		WeekOverview overview = new WeekOverview();
		overview.setUser(new User(1));
		overview.setWeekRange(new DateRange(new Date(), new Date()));
		
		TimesheetEntry entry = DummyDataGenerator.getTimesheetEntry(1, new Date(), 5);
		List<TimesheetEntry> entries = new ArrayList<TimesheetEntry>();
		entries.add(entry);
		overview.setTimesheetEntries(entries);

		List<ProjectAssignment> ass = new ArrayList<ProjectAssignment>();
		ass.add(DummyDataGenerator.getProjectAssignment(1));
		overview.setProjectAssignments(ass);
		
		overview.setFoldPreferences(new CustomerFoldPreferenceList());
		
		expect(timesheetService.getWeekOverview(isA(User.class), isA(Calendar.class), isA(EhourConfig.class)))
				.andReturn(overview);			
	}
	
	@Test
	public void shouldSubmitSuccessful()
	{
		expect(timesheetService.persistTimesheetWeek(isA(Collection.class), isA(TimesheetComment.class), isA(DateRange.class)))
			.andReturn(new ArrayList<ProjectAssignmentStatus>());
		
		replay(timesheetService);
		replay(userService);
		
		tester.startPanel(new TestPanelSource()
		{
			private static final long serialVersionUID = -8296677055637030118L;

			public Panel getTestPanel(String panelId)
			{
				return new TimesheetPanel(panelId, user, cal);
			}
		});

//		FormTester timesheetFormTester = tester.newFormTester("panel:timesheetFrame:timesheetForm");
		
		tester.executeAjaxEvent("panel:timesheetFrame:timesheetForm:commentsFrame:submitButton", "onclick");
		
		tester.assertNoErrorMessage();

		verify(timesheetService);
		verify(userService);
	}
}
