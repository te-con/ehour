package net.rrm.ehour.export.service;

import net.rrm.ehour.export.service.importer.*;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

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

    @Override
    public void importDatabase(ParseSession session) throws ImportException
    {
        try
        {
            String xmlDataFromFile = getXmlDataFromFile(session.getFilename());
            XMLEventReader eventReader = createXmlReader(xmlDataFromFile);
            XmlImporter importer = new XmlImporterBuilder()
                    .setConfigurationDao(configurationDao)
                    .setConfigurationParserDao(configurationParserDao)
                    .setDomainObjectParserDao(domainObjectParserDao)
                    .setUserRoleParserDao(userRoleParserDao)
                    .build();

            importer.importXml(session, eventReader);

            session.deleteFile();
        } catch (Exception e)
        {
            LOG.error(e);
            throw new ImportException("Failed to import", e);
        } finally
        {
            session.deleteFile();
        }


    }

    private ParseSession inputXml(String xmlData) throws XMLStreamException, ImportException, IllegalAccessException, InstantiationException, ClassNotFoundException
    {
        ParseSession session = new ParseSession();

        XMLEventReader eventReader = createXmlReader(xmlData);

        XmlImporter importer = new XmlImporterBuilder()
                .setConfigurationDao(configurationDao)
                .setConfigurationParserDao(configurationParserDao)
                .setDomainObjectParserDao(domainObjectParserDao)
                .setUserRoleParserDao(userRoleParserDao)
                .setXmlReader(eventReader)
                .build();

        importer.importXml(session, eventReader);

        return session;
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
            closeCloseable(bufferedReader);
            closeCloseable(reader);
        }
    }

    @Override
    public ParseSession prepareImportDatabase(String xmlData) throws ImportException
    {
        try
        {
            String tempFilename = writeToTempFile(xmlData);
            ParseSession status = validateXml(xmlData);
            status.setFilename(tempFilename);
            return status;
        } catch (Exception e)
        {
            LOG.error(e);
            throw new ImportException("import.error.failedToParse", e);
        }
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
            closeCloseable(writer);
        }

        return file.getAbsolutePath();
    }

    private ParseSession validateXml(String xmlData) throws XMLStreamException, ImportException, IllegalAccessException, InstantiationException, ClassNotFoundException
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

    private void closeCloseable(Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            } catch (IOException e)
            {

            }
        }
    }
}
