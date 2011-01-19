package net.rrm.ehour.export.service;

import net.rrm.ehour.domain.BinaryConfiguration;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.domain.MailLog;
import net.rrm.ehour.domain.MailLogAssignment;
import net.rrm.ehour.persistence.export.dao.ExportType;
import net.rrm.ehour.persistence.export.dao.ImportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/18/11 - 7:30 PM
 */
@Component
public class DatabaseTruncater
{
    @Autowired
    private ImportDao importDao;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void truncateDatabase()
    {
        List<ExportType> types = ExportType.reverseOrderedValues();

        for (ExportType type : types)
        {
            if (type.getDomainObjectClass() != null)
            {
                importDao.delete(type.getDomainObjectClass());
            }
        }

        importDao.delete(Configuration.class);
        importDao.delete(BinaryConfiguration.class);
        importDao.delete(MailLogAssignment.class);
        importDao.delete(MailLog.class);

    }

    public void setImportDao(ImportDao importDao)
    {
        this.importDao = importDao;
    }
}
