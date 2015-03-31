package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.common.BackupConfig;
import net.rrm.ehour.backup.domain.ImportException;
import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.service.DatabaseTruncater;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import net.rrm.ehour.persistence.hibernate.HibernateCache;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLEventReader;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 5:34:24 PM
 */
@Service("importService")
public class RestoreServiceImpl implements RestoreService {
    private static final Logger LOG = Logger.getLogger(RestoreServiceImpl.class);

    private ConfigurationDao configurationDao;

    private ConfigurationParserDao configurationParserDao;

    private EntityParserDao entityParserDao;

    private DatabaseTruncater databaseTruncater;

    private BackupConfig backupConfig;

    private TaskExecutor taskExecutor;

    private EhourConfig ehourConfig;

    @Autowired
    public RestoreServiceImpl(ConfigurationDao configurationDao,
                              ConfigurationParserDao configurationParserDao,
                              EntityParserDao entityParserDao,
                              DatabaseTruncater databaseTruncater,
                              EhourConfig ehourConfig,
                              BackupConfig backupConfig,
                              TaskExecutor taskExecutor) {
        this.configurationDao = configurationDao;
        this.configurationParserDao = configurationParserDao;
        this.entityParserDao = entityParserDao;
        this.databaseTruncater = databaseTruncater;
        this.ehourConfig = ehourConfig;
        this.backupConfig = backupConfig;
        this.taskExecutor = taskExecutor;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ImportException.class)
    public void importDatabase(ParseSession session) throws ImportException {
        try {
            if (!ehourConfig.isInDemoMode()) {
                session.start();

                databaseTruncater.truncateDatabase();

                XMLEventReader xmlEventReader = BackupFileUtil.createXmlReaderFromFile(session.getFilename());

                XmlParser parser = new XmlParserBuilder()
                        .setConfigurationDao(configurationDao)
                        .setConfigurationParserDao(configurationParserDao)
                        .setEntityParserDao(entityParserDao)
                        .setXmlReader(xmlEventReader)
                        .setSkipValidation(true)
                        .setBackupConfig(backupConfig)
                        .build();

                parser.parseXml(session, xmlEventReader);
            }
        } catch (Exception e) {
            session.setGlobalError(true);
            session.setGlobalErrorMessage(e.getMessage());
            LOG.error(e.getMessage(), e);
            throw new ImportException(e.getMessage(), e);
        } finally {
            session.deleteFile();
            session.setImported(true);
            session.finish();
        }
    }

    public void setConfigurationDao(ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }

    public void setDatabaseTruncater(DatabaseTruncater databaseTruncater) {
        this.databaseTruncater = databaseTruncater;
    }
}
