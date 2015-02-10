package net.rrm.ehour.backup.service.backup;

import org.apache.commons.lang.StringUtils;

import javax.xml.stream.XMLStreamWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 2:02:58 AM
 */
public class PrettyPrintHandler implements InvocationHandler {
    private XMLStreamWriter target;

    private int depth = 0;
    private Map<Integer, Boolean> hasChildElement = new HashMap<>();

    private static final String INDENT_CHAR = " ";
    private static final String LINEFEED_CHAR = "\n";

    public PrettyPrintHandler(XMLStreamWriter target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {

        String m = method.getName();


        if ("writeStartElement".equals(m)) {
            // update state of parent node
            if (depth > 0) {
                hasChildElement.put(depth - 1, true);
            }

            // reset state of current node
            hasChildElement.put(depth, false);

            // indent for current depth
            target.writeCharacters(LINEFEED_CHAR);
            target.writeCharacters(repeat(depth, INDENT_CHAR));

            depth++;
        }

        if ("writeEndElement".equals(m)) {
            depth--;

            if (hasChildElement.get(depth)) {
                target.writeCharacters(LINEFEED_CHAR);
                target.writeCharacters(repeat(depth, INDENT_CHAR));
            }

        }

        if ("writeEmptyElement".equals(m)) {
            // update state of parent node
            if (depth > 0) {
                hasChildElement.put(depth - 1, true);
            }

            // indent for current depth
            target.writeCharacters(LINEFEED_CHAR);
            target.writeCharacters(repeat(depth, INDENT_CHAR));

        }


        method.invoke(target, args);

        return null;
    }

    private String repeat(int d, String s) {
        return StringUtils.repeat(s, d);
    }
}