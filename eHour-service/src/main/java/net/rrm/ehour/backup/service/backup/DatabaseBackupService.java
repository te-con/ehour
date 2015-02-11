package net.rrm.ehour.backup.service.backup;

/**
 * Service which exports the whole database to XML
 *
 * @author thies
 */
public interface DatabaseBackupService {
    /**
     * Export the database to an XML formatted backup fi le
     */
    byte[] exportDatabase();
}
