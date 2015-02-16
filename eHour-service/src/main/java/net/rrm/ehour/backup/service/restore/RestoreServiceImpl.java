package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.common.BackupConfig;
import net.rrm.ehour.backup.domain.ImportException;
import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.service.DatabaseTruncater;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    private EhourConfig ehourConfig;

    @Autowired
    public RestoreServiceImpl(ConfigurationDao configurationDao, ConfigurationParserDao configurationParserDao, EntityParserDao entityParserDao, DatabaseTruncater databaseTruncater, EhourConfig ehourConfig, BackupConfig backupConfig) {
        this.configurationDao = configurationDao;
        this.configurationParserDao = configurationParserDao;
        this.entityParserDao = entityParserDao;
        this.databaseTruncater = databaseTruncater;
        this.ehourConfig = ehourConfig;
        this.backupConfig = backupConfig;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ImportException.class)
    public ParseSession importDatabase(ParseSession session) throws ImportException {
        try {
            if (!ehourConfig.isInDemoMode()) {
                databaseTruncater.truncateDatabase();

                session.clearSession();

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
        }

        return session;
    }

    @Override
    public ParseSession prepareImportDatabase(String xmlData) {
        ParseSession session;

        try {
            String tempFilename = BackupFileUtil.writeToTempFile(xmlData);
            session = validateXml(xmlData);
            session.setFilename(tempFilename);
        } catch (Exception e) {
            session = new ParseSession();
            session.setGlobalError(true);
            session.setGlobalErrorMessage(e.getMessage());
            LOG.error(e.getMessage(), e);
        }

        return session;
    }

    private ParseSession validateXml(String xmlData) throws Exception {
        ParseSession status = new ParseSession();

        XMLEventReader eventReader = BackupFileUtil.createXmlReader(xmlData);

        EntityParserDaoValidatorImpl domainObjectParserDaoValidator = new EntityParserDaoValidatorImpl();
        ConfigurationParserDaoValidatorImpl configurationParserDaoValidator = new ConfigurationParserDaoValidatorImpl();

        XmlParser importer = new XmlParserBuilder()
                .setConfigurationDao(configurationDao)
                .setConfigurationParserDao(configurationParserDaoValidator)
                .setEntityParserDao(domainObjectParserDaoValidator)
                .setBackupConfig(backupConfig)
                .setXmlReader(eventReader)
                .build();

        importer.parseXml(status, eventReader);

        return status;
    }

    public void setConfigurationDao(ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }

    public void setDatabaseTruncater(DatabaseTruncater databaseTruncater) {
        this.databaseTruncater = databaseTruncater;
    }
}
