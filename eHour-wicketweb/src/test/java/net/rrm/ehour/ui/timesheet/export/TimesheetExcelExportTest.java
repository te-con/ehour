/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.timesheet.export;

import net.rrm.ehour.appconfig.EhourHomeUtil;
import net.rrm.ehour.config.service.ConfigurationServiceImpl;
import net.rrm.ehour.persistence.config.dao.BinaryConfigurationDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.manage.config.panel.SkinConfigPanel;
import net.rrm.ehour.util.DateUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

/**
 * Created on Apr 10, 2009, 2:05:13 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class TimesheetExcelExportTest extends BaseSpringWebAppTester {
    private ConfigurationServiceImpl configService;
    private DetailedReportService detailedReportService;

    @Before
    public void before() throws Exception {
        getConfig().setFirstDayOfWeek(Calendar.MONDAY);

        detailedReportService = createMock(DetailedReportService.class);
        getMockContext().putBean("detailedReportService", detailedReportService);

        EhourHomeUtil.setEhourHome(".");
        configService = new ConfigurationServiceImpl();
        getMockContext().putBean("configurationService", configService);

        BinaryConfigurationDao binConfigfDao = createMock(BinaryConfigurationDao.class);
        configService.setBinConfigDAO(binConfigfDao);
    }

    @Test
    public void produceExcelReport() throws IOException {
        List<FlatReportElement> elements = SkinConfigPanel.TimesheetExportDummyDataGenerator.createMonthData(getConfig());

        ReportData data = new ReportData(elements, SkinConfigPanel.TimesheetExportDummyDataGenerator.getDateRangeForCurrentMonth(), new UserSelectedCriteria());

        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.getCustomParameters().put(TimesheetExportParameter.INCL_SIGN_OFF.name(), Boolean.TRUE);
        userSelectedCriteria.setReportRange(SkinConfigPanel.TimesheetExportDummyDataGenerator.getDateRangeForCurrentMonth());
        ReportCriteria criteria = new ReportCriteria(userSelectedCriteria);

        expect(detailedReportService.getDetailedReportData(criteria)).andReturn(data);

        replay(detailedReportService);
        byte[] excelData = new TimesheetExcelExport().getExcelData(criteria);
        assertTrue(excelData.length > 0);

        verify(detailedReportService);
    }


    @Test
    public void produceForEmptyMonth() throws IOException {
        List<FlatReportElement> elements = new ArrayList<FlatReportElement>();

        ReportData data = new ReportData(elements, DateUtil.getDateRangeForMonth(new Date()), new UserSelectedCriteria());
        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.setReportRange(SkinConfigPanel.TimesheetExportDummyDataGenerator.getDateRangeForCurrentMonth());
        ReportCriteria criteria = new ReportCriteria(userSelectedCriteria);

        expect(detailedReportService.getDetailedReportData(criteria))
                .andReturn(data);

        replay(detailedReportService);
        byte[] excelData = new TimesheetExcelExport().getExcelData(criteria);
        assertTrue(excelData.length > 0);

        verify(detailedReportService);
    }

    @SuppressWarnings("unused")
    private void writeByteData(byte[] excelData) throws IOException {
        File outfile = new File("/tmp/test.xls");
        FileOutputStream fos = new FileOutputStream(outfile);
        fos.write(excelData);
        fos.close();
    }
}
