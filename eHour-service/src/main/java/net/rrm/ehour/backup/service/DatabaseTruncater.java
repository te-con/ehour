package net.rrm.ehour.backup.service;

import net.rrm.ehour.backup.common.BackupConfig;
import net.rrm.ehour.backup.common.BackupEntityType;
import net.rrm.ehour.domain.BinaryConfiguration;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.domain.MailLog;
import net.rrm.ehour.persistence.backup.dao.RestoreDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/18/11 - 7:30 PM
 */
@Service
public class DatabaseTruncater {
    private static final Logger LOG = Logger.getLogger(DatabaseTruncater.class);

    private RestoreDao restoreDao;

    private BackupConfig backupConfig;

    @Autowired
    public DatabaseTruncater(RestoreDao restoreDao, BackupConfig backupConfig) {
        this.restoreDao = restoreDao;
        this.backupConfig = backupConfig;
    }

    @Transactional
    public void truncateDatabase() {
        List<BackupEntityType> types = backupConfig.reverseOrderedValues();

        for (BackupEntityType type : types) {
            Class<? extends DomainObject<? extends Serializable, ?>> domainObjectClass = type.getDomainObjectClass();

            if (domainObjectClass != null) {
                LOG.debug("Truncating table " + domainObjectClass);
                restoreDao.delete(domainObjectClass);
            }
        }

        restoreDao.delete(Configuration.class);
        restoreDao.delete(BinaryConfiguration.class);
        restoreDao.delete(MailLog.class);

        restoreDao.clearCache();
    }

    public void setRestoreDao(RestoreDao restoreDao) {
        this.restoreDao = restoreDao;
    }
}
