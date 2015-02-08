package net.rrm.ehour.backup.service;

import net.rrm.ehour.backup.service.backup.BackupEntity;
import net.rrm.ehour.backup.service.backup.BackupEntityLocator;
import net.rrm.ehour.domain.BinaryConfiguration;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.domain.MailLog;
import net.rrm.ehour.persistence.backup.dao.RestoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/18/11 - 7:30 PM
 */
@Service
public class DatabaseTruncater {
    private RestoreDao restoreDao;

    private BackupEntityLocator backupEntityLocator;

    @Autowired
    public DatabaseTruncater(RestoreDao restoreDao, BackupEntityLocator backupEntityLocator) {
        this.restoreDao = restoreDao;
        this.backupEntityLocator = backupEntityLocator;
    }

    @Transactional
    public void truncateDatabase() {
        List<BackupEntity> types = backupEntityLocator.reverseOrderedValues();

        for (BackupEntity type : types) {
            if (type.getDomainObjectClass() != null) {
                restoreDao.delete(type.getDomainObjectClass());
            }
        }

        restoreDao.delete(Configuration.class);
        restoreDao.delete(BinaryConfiguration.class);
        restoreDao.delete(MailLog.class);
    }

    public void setRestoreDao(RestoreDao restoreDao) {
        this.restoreDao = restoreDao;
    }
}
