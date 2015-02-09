package net.rrm.ehour.backup.service.backup;

import net.rrm.ehour.backup.domain.ExportElements;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.persistence.backup.dao.BackupDao;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author thies
 */
@Service("databaseBackupService")
public class DatabaseBackupServiceImpl implements DatabaseBackupService {
    private static final Logger LOGGER = Logger.getLogger(DatabaseBackupServiceImpl.class);

    private BackupDao backupDao;
    private ConfigurationService configurationService;
    private BackupEntityLocator backupEntityLocator;

    @Autowired
    public DatabaseBackupServiceImpl(BackupDao backupDao, ConfigurationService configurationService, BackupEntityLocator backupEntityLocator) {
        this.backupDao = backupDao;
        this.configurationService = configurationService;
        this.backupEntityLocator = backupEntityLocator;
    }

    @Override
    public String exportDatabase() {
        String xmlDocument = null;

        StringWriter stringWriter = new StringWriter();

        XMLStreamWriter writer = null;

        try {
            writer = createXmlWriter(stringWriter);

            exportDatabase(writer);

            xmlDocument = stringWriter.toString();
        } catch (XMLStreamException e) {
            LOGGER.error(e);
        } finally {
/* @TODO add after load test
            if (writer != null) {
                try {
                    writer.close();
                } catch (XMLStreamException e) {

                }
            }
*/
        }


        return xmlDocument;
    }

    protected XMLStreamWriter createXmlWriter(StringWriter stringWriter) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();

        XMLStreamWriter writer;
        writer = factory.createXMLStreamWriter(stringWriter);
        PrettyPrintHandler handler = new PrettyPrintHandler(writer);

        return (XMLStreamWriter) Proxy.newProxyInstance(
                XMLStreamWriter.class.getClassLoader(),
                new Class[]{XMLStreamWriter.class},
                handler);
    }

    private void exportDatabase(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartDocument();

        EhourConfigStub stub = configurationService.getConfiguration();

        writer.writeStartElement(ExportElements.EHOUR.name());
        writer.writeAttribute(ExportElements.DB_VERSION.name(), stub.getVersion());

        backupConfiguration(writer);
        backupJoinTables(writer);
        backupTypes(writer);

        writer.writeEndElement();
        writer.writeEndDocument();
    }

    private void backupConfiguration(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(ExportElements.CONFIGURATION.name());

        List<Configuration> configurationList = configurationService.findAllConfiguration();

        for (Configuration configuration : configurationList) {
            writer.writeStartElement(ExportElements.CONFIG.name());
            writer.writeAttribute(ExportElements.KEY.name(), configuration.getConfigKey());
            writer.writeCharacters(configuration.getConfigValue());
            writer.writeEndElement();
        }

        writer.writeEndElement();
    }

    private void backupJoinTables(XMLStreamWriter writer) throws XMLStreamException {
        List<BackupJoinTable> joinTables = backupEntityLocator.joinTables();


        for (BackupJoinTable joinTable : joinTables) {
            String container = joinTable.getContainer();
            writer.writeStartElement(container);

            String tableName = joinTable.getTableName();
            List<Map<String, Object>> rows = backupDao.findAll(tableName);

            for (Map<String, Object> row : rows) {
                writer.writeStartElement(tableName);

                Object source = row.get(joinTable.getAttributeSource());
                Object target = row.get(joinTable.getAttributeTarget());

                writer.writeAttribute(joinTable.getAttributeSource(), source.toString());
                writer.writeAttribute(joinTable.getAttributeTarget(), target.toString());

                writer.writeEndElement();
            }

            writer.writeEndElement();
        }
    }

    private void backupTypes(XMLStreamWriter writer) throws XMLStreamException {
        for (BackupEntity type : backupEntityLocator.backupEntities()) {
            backupType(type, writer);
        }
    }

    private void backupType(BackupEntity entity, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(entity.getParentName());

        if (entity.getDomainObjectClass() != null) {
            writer.writeAttribute("CLASS", entity.getDomainObjectClass().getName());
        }

        List<Map<String, Object>> rows = backupDao.findAll(entity.name());

        if (entity.getProcessor() != null) {
            rows = entity.getProcessor().processRows(rows);
        }

        for (Map<String, Object> rowMap : rows) {
            writer.writeStartElement(entity.name());

            for (Entry<String, Object> columns : rowMap.entrySet()) {
                if (StringUtils.isNotBlank(columns.getKey()) && columns.getValue() != null) {
                    writer.writeStartElement(columns.getKey());
                    writer.writeCharacters(columns.getValue().toString());
                    writer.writeEndElement();
                }
            }

            writer.writeEndElement();
        }

        writer.writeEndElement();
    }
}
