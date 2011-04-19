package net.rrm.ehour.export.service;

import net.rrm.ehour.util.IoUtil;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 1/20/11 5:36 PM
 */
public final class BackupFileUtil
{
    private BackupFileUtil()
    {
    }

    /**
     *  Write XML to temp file
     * @param xmlData
     * @return
     * @throws IOException
     */
    static String writeToTempFile(String xmlData) throws IOException
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

    /**
     * Create XML reader from data string
     *
     * @param xmlData
     * @return
     * @throws XMLStreamException
     */
    static XMLEventReader createXmlReader(String xmlData)
            throws XMLStreamException
    {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        return inputFactory.createXMLEventReader(new StringReader(xmlData));
    }

    /**
     * Create XML reader from file
     *
     * @param filename
     * @return
     * @throws IOException
     * @throws XMLStreamException
     */
    static XMLEventReader createXmlReaderFromFile(String filename) throws IOException, XMLStreamException
    {
        String data = getXmlDataFromFile(filename);
        return createXmlReader(data);
    }

    private static String getXmlDataFromFile(String filename) throws IOException
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
}
