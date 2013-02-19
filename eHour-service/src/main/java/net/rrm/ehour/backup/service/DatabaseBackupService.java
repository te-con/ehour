package net.rrm.ehour.backup.service;

/**
 * Service which exports the whole database to XML
 *
 * @author thies
 *
 */
public interface DatabaseBackupService
{
	/**
	 * Export the database
	 * @return
	 */
	String exportDatabase();
}
