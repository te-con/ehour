package net.rrm.ehour.export.service;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 5:33:50 PM
 */
public interface ImportService
{
    /**
     * Repare import XML database
     *
     * @param xmlData
     */
    public ParseSession prepareImportDatabase(String xmlData);

    /**
     * Import database
     * @param session
     * @throws ImportException
     */
    public void importDatabase(ParseSession session) throws ImportException;
}
