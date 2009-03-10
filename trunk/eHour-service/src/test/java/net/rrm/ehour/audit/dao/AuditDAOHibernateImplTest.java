/**
 * 
 */
package net.rrm.ehour.audit.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.domain.Audit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@SuppressWarnings("unchecked")
public class AuditDAOHibernateImplTest extends BaseDAOTest
{
	@Autowired
	private AuditDAO auditDAO;

	@Test
	public void testFindAudit()
	{
		AuditReportRequest request = new AuditReportRequest()
										.setMax(20)
										.setOffset(10);
		
		List<Audit> res = auditDAO.findAudit(request);

		assertEquals(20, res.size());
		assertEquals(70, res.get(0).getAuditId());
		
	}
	
	@Test
	public void testFindAuditCount()
	{
		AuditReportRequest request = new AuditReportRequest();

		Number count = auditDAO.findAuditCount(request);
		
		assertEquals(40, count.intValue());
	}
}
