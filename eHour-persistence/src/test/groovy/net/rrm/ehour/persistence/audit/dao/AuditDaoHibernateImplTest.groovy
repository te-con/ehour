package net.rrm.ehour.persistence.audit.dao

import com.google.common.collect.Lists
import net.rrm.ehour.data.AuditReportRequest
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.Audit
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import net.rrm.ehour.util.DateUtil
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertEquals

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 12:55:03 AM
 */
class AuditDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private AuditDao auditDAO;

    private AuditReportRequest request;

    public AuditDaoHibernateImplTest() {
        super("dataset-audit.xml");
    }

    @Before
    public void before() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 2008);
        cal.set(Calendar.MONTH, Calendar.NOVEMBER);

        DateRange range = DateUtil.getDateRangeForMonth(cal);
        request = new AuditReportRequest(reportRange: range);
    }

    @Test
    public void shouldFind20AuditRecords() {
        List<Audit> res = auditDAO.findAudits(request, 10, 20);

        assertEquals(20, res.size());
        assertEquals(79, res[9].getAuditId());

    }

    @Test
    public void shouldCouldAllCount() {
        Number count = auditDAO.count(request);

        assertEquals(40, count);
    }

    @Test
    public void shouldFindAll() {
        List<Audit> all = auditDAO.findAudits(request);

        assertEquals(40, all.size());
    }
}
