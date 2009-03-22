/**
 * 
 */
package net.rrm.ehour.audit.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.util.DateUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuditDAOHibernateImplTest extends BaseDAOTest
{
	@Autowired
	private AuditDAO auditDAO;
	private AuditReportRequest request;
	
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
	public void testFindAudit()
	{
		request.setMax(20)
			   .setOffset(10);
		
		List<Audit> res = auditDAO.findAudit(request);

		assertEquals(20, res.size());
		assertEquals(70, res.get(0).getAuditId());
		
	}
	
	@Test
	public void testFindAuditCount()
	{
		Number count = auditDAO.findAuditCount(request);
		
		assertEquals(40, count.intValue());
	}
}
