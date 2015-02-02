package net.rrm.ehour.backup.service;


import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;

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
        File file = File.createTempFile("import", "xml");
        file.deleteOnExit();

        try (FileWriter writer = new FileWriter(file))
        {
            writer.write(xmlData);
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
        File file = new File(filename);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file)))
        {
            String line;
            StringBuffer xmlData = new StringBuffer();

            while ((line = bufferedReader.readLine()) != null)
            {
                xmlData.append(line);
            }

            return xmlData.toString();
        }
    }
}
