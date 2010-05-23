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

package net.rrm.ehour.audit.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.data.AuditReportRequest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.util.DateUtil;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuditDaoHibernateImplTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private AuditDao auditDAO;
	private AuditReportRequest request;
	
	public AuditDaoHibernateImplTest()
	{
		super("dataset-audit.xml");
	}
	
	@Before
	public void before()
	{
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
		
		DateRange range = DateUtil.getDateRangeForMonth(cal);
		request = new AuditReportRequest();		
		request.setReportRange(range);
	}
	
	@Test
	public void shouldFind20AuditRecords()
	{
		request.setMax(20)
			   .setOffset(10);
		
		List<Audit> res = auditDAO.findAudit(request);

		assertEquals(20, res.size());
		assertEquals(70, res.get(0).getAuditId());
		
	}
	
	@Test
	public void shouldCouldAllCount()
	{
		Number count = auditDAO.count(request);
		
		assertEquals(40, count.intValue());
	}
	
	@Test
	public void shouldFindAll()
	{
		List<Audit> all = auditDAO.findAllAudits(request);
		
		assertEquals(40, all.size());
	}
}
