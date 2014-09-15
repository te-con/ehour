package com.richemont.windchill;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author laurent.linck
 */
public class Utilities {

    private static Logger LOGGER = Logger.getLogger("com.richemont.windchill");

    public static void displayHashMapContent(HashMap<String, Comparable> hm) {
        if  (hm != null ) {
            for( Iterator it = hm.keySet().iterator(); it.hasNext();) {
                String theKey = (String)it.next();
                String theValue = "null";
                String theClass = "null";
                if (hm.get(theKey)!= null) theClass = hm.get(theKey).getClass().toString();
                if (hm.get(theKey) instanceof  String) theValue= (String)hm.get(theKey);
                else if (hm.get(theKey) instanceof Date) theValue= DateUtils.convertDateToString( (Date) hm.get(theKey), WindchillConst.WIND_DATE_FORMAT );
                else theValue = "?";
                LOGGER.debug("\t" + theKey + " = " + theValue + " [" + theClass + "]");

            }
        }
    }

    private Properties getAllProps(String propsFile) throws IOException {
        Properties props = new Properties();
        InputStream in = Utilities.class.getClassLoader().getResourceAsStream(propsFile);

        File file = new File(propsFile);
        System.out.println(file.getPath());
        System.out.println("InputStream: "+ in);
        props.load(in);
        in.close();
        /**
         Enumeration em = props.keys();
         while(em.hasMoreElements()){
         String str = (String)em.nextElement();
         System.out.println(str + "=" + props.get(str));
         }
         **/
        return props;
    }

}
