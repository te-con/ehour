package net.rrm.ehour.export.service;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 5:33:50 PM
 */
public interface ImportService
{
    /**
     * Import XML database
     * @param xmlData
     */
    public boolean prepareImportDatabase(String xmlData) throws ImportException;
}
