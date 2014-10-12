package net.rrm.ehour.persistence.mail.dao;

import net.rrm.ehour.domain.MailLog;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import scala.collection.immutable.List;

import static org.junit.Assert.assertEquals;

public class MailLogDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private MailLogDao mailLogDao;

    public MailLogDaoHibernateImplTest() {
        super("dataset-maillog.xml");
    }

    @Test
    public void shouldFindMailLogOnEvent() {
        List<MailLog> xs = mailLogDao.find("CC");
        assertEquals(1, xs.size());
    }

    @Test
    public void shouldFindMailLogOnEventAndMailTo() {
        List<MailLog> xs = mailLogDao.find("thies@aa.net", "DD");
        assertEquals(1, xs.size());
    }
}
