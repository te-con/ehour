package net.rrm.ehour.export.service;

/**
 * Service which exports the whole database to XML
 *
 * @author thies
 *
 */
public interface ExportService
{
	/**
	 * Export the database
	 * @return
	 */
	public String exportDatabase();
}
