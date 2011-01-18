package net.rrm.ehour.export.service;

import net.rrm.ehour.export.service.importer.*;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import net.rrm.ehour.persistence.export.dao.ExportDao;
import net.rrm.ehour.persistence.export.dao.ExportType;
import net.rrm.ehour.util.IoUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.List;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 5:34:24 PM
 */
@Service("importService")
public class ImportServiceImpl implements ImportService
{
    private static final Logger LOG = Logger.getLogger(ImportServiceImpl.class);

    @Autowired
    private ConfigurationDao configurationDao;

    @Autowired
    private ConfigurationParserDao configurationParserDao;

    @Autowired
    private DomainObjectParserDao domainObjectParserDao;

    @Autowired
    private UserRoleParserDao userRoleParserDao;

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private ExportDao exportDao;

    @Override
    @Transactional
    public ParseSession importDatabase(ParseSession session)
    {
        try
        {
            clearDatabase();

            session.clearSession();
            String xmlDataFromFile = getXmlDataFromFile(session.getFilename());
            XMLEventReader eventReader = createXmlReader(xmlDataFromFile);
            XmlImporter importer = new XmlImporterBuilder()
                    .setConfigurationDao(configurationDao)
                    .setConfigurationParserDao(configurationParserDao)
                    .setDomainObjectParserDao(domainObjectParserDao)
                    .setUserRoleParserDao(userRoleParserDao)
                    .setXmlReader(eventReader)
                    .setTxTemplate(new TransactionTemplate(txManager))
                    .build();

            importer.importXml(session, eventReader);
        } catch (Exception e)
        {
            session.setGlobalError(true);
            session.setGlobalErrorMessage(e.getMessage());
            LOG.error(e);
            e.printStackTrace();

            LOG.error(e);
        } finally
        {
            session.deleteFile();
            session.setImported(true);
        }

        return session;
    }

    private void clearDatabase()
    {
        List<ExportType> types = ExportType.reverseOrderedValues();

        for (ExportType type : types)
        {
            exportDao.deleteType(type);
        }

    }


    private XMLEventReader createXmlReader(String xmlData)
            throws XMLStreamException
    {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        return inputFactory.createXMLEventReader(new StringReader(xmlData));
    }

    private String getXmlDataFromFile(String filename) throws IOException
    {
        FileReader reader = null;
        BufferedReader bufferedReader = null;

        try
        {
            File file = new File(filename);
            reader = new FileReader(file);
            bufferedReader = new BufferedReader(reader);

            String line;
            StringBuffer xmlData = new StringBuffer();

            while ((line = bufferedReader.readLine()) != null)
            {
                xmlData.append(line);
            }

            return xmlData.toString();
        } finally
        {
            IoUtil.close(bufferedReader);
            IoUtil.close(reader);
        }
    }

    @Override
    public ParseSession prepareImportDatabase(String xmlData)
    {
        ParseSession session;

        try
        {
            String tempFilename = writeToTempFile(xmlData);
            session = validateXml(xmlData);
            session.setFilename(tempFilename);
        } catch (Exception e)
        {
            session = new ParseSession();
            session.setGlobalError(true);
            session.setGlobalErrorMessage(e.getMessage());
            LOG.error(e);
        }

        return session;
    }

    private String writeToTempFile(String xmlData) throws IOException
    {
        FileWriter writer = null;
        File file;

        try
        {
            file = File.createTempFile("import", "xml");
            file.deleteOnExit();

            writer = new FileWriter(file);
            writer.write(xmlData);
        } finally
        {
            IoUtil.close(writer);
        }

        return file.getAbsolutePath();
    }

    private ParseSession validateXml(String xmlData) throws Exception
    {
        ParseSession status = new ParseSession();

        XMLEventReader eventReader = createXmlReader(xmlData);

        DomainObjectParserDaoValidatorImpl domainObjectParserDaoValidator = new DomainObjectParserDaoValidatorImpl();
        ConfigurationParserDaoValidatorImpl configurationParserDaoValidator = new ConfigurationParserDaoValidatorImpl();
        UserRoleParserDaoValidatorImpl userRoleParserDaoValidator = new UserRoleParserDaoValidatorImpl();

        XmlImporter importer = new XmlImporterBuilder()
                .setConfigurationDao(configurationDao)
                .setConfigurationParserDao(configurationParserDaoValidator)
                .setDomainObjectParserDao(domainObjectParserDaoValidator)
                .setUserRoleParserDao(userRoleParserDaoValidator)
                .setXmlReader(eventReader)
                .build();

        importer.importXml(status, eventReader);

        return status;
    }

    public void setConfigurationDao(ConfigurationDao configurationDao)
    {
        this.configurationDao = configurationDao;
    }

    public void setExportDao(ExportDao exportDao)
    {
        this.exportDao = exportDao;
    }
}
