package net.rrm.ehour.backup.service;

import net.rrm.ehour.domain.BinaryConfiguration;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.domain.MailLog;
import net.rrm.ehour.persistence.backup.dao.BackupEntityType;
import net.rrm.ehour.persistence.backup.dao.RestoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    private RestoreDao restoreDao;

    @Transactional
    public void truncateDatabase()
    {
        List<BackupEntityType> types = BackupEntityType.reverseOrderedValues();

        for (BackupEntityType type : types)
        {
            if (type.getDomainObjectClass() != null)
            {
                restoreDao.delete(type.getDomainObjectClass());
            }
        }

        restoreDao.delete(Configuration.class);
        restoreDao.delete(BinaryConfiguration.class);
        restoreDao.delete(MailLog.class);
    }

    public void setRestoreDao(RestoreDao restoreDao)
    {
        this.restoreDao = restoreDao;
    }
}
