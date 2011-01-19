package net.rrm.ehour.export.service.importer;

import net.rrm.ehour.config.ConfigurationItem;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.export.service.ExportElements;
import net.rrm.ehour.export.service.ImportException;
import net.rrm.ehour.export.service.ParseSession;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/6/10 - 3:40 PM
 */
public class XmlImporter
{
    private static final Logger LOG = Logger.getLogger(XmlImporter.class);

    private ConfigurationDao configurationDao;
    private DomainObjectParser domainObjectParser;
    private ConfigurationParser configurationParser;
    private UserRoleParser userRoleParser;

    private TransactionTemplate txTemplate;

    private boolean skipValidation;

    public XmlImporter(ConfigurationDao configurationDao, DomainObjectParser domainObjectParser, ConfigurationParser configurationParser, UserRoleParser userRoleParser)
    {
        this(configurationDao, domainObjectParser, configurationParser, userRoleParser, null);
    }

    public XmlImporter(ConfigurationDao configurationDao, DomainObjectParser domainObjectParser, ConfigurationParser configurationParser, UserRoleParser userRoleParser, TransactionTemplate txTemplate)
    {
        this.configurationDao = configurationDao;
        this.domainObjectParser = domainObjectParser;
        this.configurationParser = configurationParser;
        this.userRoleParser = userRoleParser;
        this.txTemplate = txTemplate;

        if (txTemplate != null)
        {
            txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            skipValidation = true;
        }
    }

    public void importXml(final ParseSession status, final XMLEventReader eventReader) throws Exception
    {
        while (eventReader.hasNext())
        {
            final XMLEvent event = eventReader.nextTag();

            if (event.isStartElement())
            {
                if (txTemplate != null)
                {
                    runInTx(status, eventReader, event);
                } else
                {
                    parseEvent(status, eventReader, event);
                }


            } else if (event.isEndDocument() || event.isEndElement())
            {
                break;
            }
        }
    }

    private void runInTx(final ParseSession status, final XMLEventReader eventReader, final XMLEvent event) throws Exception
    {
        Exception exception = txTemplate.execute(new TransactionCallback<Exception>()
        {
            @Override
            public Exception doInTransaction(TransactionStatus txStatus)
            {
                Exception thrownException = null;

                try
                {
                    parseEvent(status, eventReader, event);
                } catch (Exception e)
                {
                    txStatus.setRollbackOnly();

                    thrownException = e;
                }

                return thrownException;
            }
        });

        if (exception != null)
        {
            throw exception;
        }
    }

    private void parseEvent(ParseSession status, XMLEventReader eventReader, XMLEvent event)
            throws ImportException, XMLStreamException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        StartElement startElement = event.asStartElement();

        String startName = startElement.getName().getLocalPart();

        ExportElements element = safelyGetExportElements(startName);

        LOG.info("Element found in backup file: " + element.name() + " = " + startName);

        switch (element)
        {
            case EHOUR:
                if (!skipValidation)
                {
                    checkDatabaseVersion(startElement);
                }
                break;
            case CONFIGURATION:
                configurationParser.parseConfiguration(eventReader);
                break;
            case USER_TO_USERROLES:
                userRoleParser.parseUserRoles(eventReader, status);
                break;
            case OTHER:
                parseElement(startElement, domainObjectParser, status);
                break;
            default:
                break;
        }
    }

    private ExportElements safelyGetExportElements(String name)
    {
        ExportElements element;

        try
        {
            element = ExportElements.valueOf(name);
        } catch (IllegalArgumentException iae)
        {
            element = ExportElements.OTHER;
        }

        return element;
    }

    @SuppressWarnings("unchecked")
    private void parseElement(StartElement element, DomainObjectParser parser, ParseSession status) throws XMLStreamException, InstantiationException, IllegalAccessException, ClassNotFoundException, ImportException
    {
        Attribute attribute = element.getAttributeByName(new QName("CLASS"));

        if (attribute != null)
        {
            String aClass = attribute.getValue();

            Class<? extends DomainObject> doClass = (Class<? extends DomainObject>) Class.forName(aClass);

            parser.parse(doClass, status);
        } else
        {
            throw new ImportException("Invalid XML, no attribute found for element: " + element.getName().getLocalPart());
        }
    }

    private void checkDatabaseVersion(StartElement element) throws ImportException
    {
        Attribute attribute = element.getAttributeByName(new QName(ExportElements.DB_VERSION.name()));
        String dbVersion = attribute.getValue();

        Configuration version = configurationDao.findById(ConfigurationItem.VERSION.getDbField());

        isDatabaseCompatible(version.getConfigValue(), dbVersion);
    }

    private void isDatabaseCompatible(String version, String dbVersion) throws ImportException
    {
        dbVersion = dbVersion != null && dbVersion.equalsIgnoreCase("0.8.3") ? "0.8.4" : dbVersion;
        version = version != null && version.equalsIgnoreCase("0.8.3") ? "0.8.4" : version;

        if (version == null || !version.equalsIgnoreCase(dbVersion))
        {
            String foundVersion = version != null ? version : "n/a";

            throw new ImportException("Invalid database version (" + dbVersion + ") specified in file, target database should match backup database version (" + foundVersion + ")");
        }
    }
}
