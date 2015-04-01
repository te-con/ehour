package net.rrm.ehour.backup.service.backup;

import com.google.common.collect.Maps;
import net.rrm.ehour.backup.common.BackupConfig;
import net.rrm.ehour.backup.common.BackupEntityType;
import net.rrm.ehour.backup.common.BackupJoinTable;
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
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
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
    private BackupConfig backupConfig;

    @Autowired
    public DatabaseBackupServiceImpl(BackupDao backupDao, ConfigurationService configurationService, BackupConfig backupConfig) {
        this.backupDao = backupDao;
        this.configurationService = configurationService;
        this.backupConfig = backupConfig;
    }

    @Override
    public synchronized byte[] exportDatabase() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        XMLStreamWriter writer = null;

        try {
            writer = createXmlWriter(outputStream);

            exportDatabase(writer);

            return outputStream.toByteArray();
        } catch (XMLStreamException e) {
            LOGGER.error(e);
            return new byte[0];
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (XMLStreamException e) {

                }
            }
        }
    }

    protected XMLStreamWriter createXmlWriter(OutputStream stream) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();

        XMLStreamWriter writer = factory.createXMLStreamWriter(stream, "UTF-8");

        PrettyPrintHandler handler = new PrettyPrintHandler(writer);

        return (XMLStreamWriter) Proxy.newProxyInstance(
                XMLStreamWriter.class.getClassLoader(),
                new Class[]{XMLStreamWriter.class},
                handler);
    }

    private void exportDatabase(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartDocument("UTF-8", "1.0");

        EhourConfigStub stub = configurationService.getConfiguration();

        writer.writeStartElement(ExportElements.EHOUR.name());
        writer.writeAttribute(ExportElements.DB_VERSION.name(), stub.getVersion());

        backupConfiguration(writer);

        writer.writeStartElement(ExportElements.JOIN_TABLES.name());
        backupJoinTables(writer);
        writer.writeEndElement();

        writer.writeStartElement(ExportElements.ENTITY_TABLES.name());
        backupEntities(writer);
        writer.writeEndElement();

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
        List<BackupJoinTable> joinTables = backupConfig.joinTables();

        for (BackupJoinTable joinTable : joinTables) {
            String container = joinTable.getContainer();
            writer.writeStartElement(container);

            String tableName = joinTable.getTableName();
            List<Map<String, Object>> rows = backupDao.findAll(tableName);

            for (Map<String, Object> row : rows) {
                Map<String, Object> uppercaseRowMap = Maps.newHashMap();

                for (Entry<String, Object> s : row.entrySet()) {
                    uppercaseRowMap.put(s.getKey().toUpperCase(), s.getValue());
                }

                writer.writeStartElement(tableName);

                Object source = uppercaseRowMap.get(joinTable.getAttributeSource().toUpperCase());
                Object target = uppercaseRowMap.get(joinTable.getAttributeTarget().toUpperCase());

                writer.writeAttribute(joinTable.getAttributeSource().toUpperCase(), source.toString());
                writer.writeAttribute(joinTable.getAttributeTarget().toUpperCase(), target.toString());

                writer.writeEndElement();
            }

            writer.writeEndElement();
        }
    }

    private void backupEntities(XMLStreamWriter writer) throws XMLStreamException {
        for (BackupEntityType type : backupConfig.backupEntities()) {
            backupType(type, writer);
        }
    }

    private void backupType(BackupEntityType entity, XMLStreamWriter writer) throws XMLStreamException {
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
