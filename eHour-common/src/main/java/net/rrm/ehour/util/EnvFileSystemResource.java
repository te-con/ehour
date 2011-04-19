package net.rrm.ehour.util;

import org.springframework.core.io.FileSystemResource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FileSystemResource which replaces system env
 *
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/22/11 - 4:50 PM
 */
public class EnvFileSystemResource extends FileSystemResource
{
    private static final Pattern ENV_PATTERN = Pattern.compile("\\$\\{(.*)\\}");

    public EnvFileSystemResource(String path)
    {
        super(replaceEnvInPath(path));
    }

    private static final String replaceEnvInPath(String path)
    {
        Matcher m = ENV_PATTERN.matcher(path);

        StringBuffer sb = new StringBuffer();

        while (m.find())
        {
            String sysPropKey = m.group(1);

            String systemProp;

            if (System.getProperties().containsKey(sysPropKey))
            {
                systemProp = (String) System.getProperties().get(sysPropKey);
            } else
            {
                systemProp = System.getenv(sysPropKey);
            }

            if (systemProp != null)
            {
                m.appendReplacement(sb, systemProp);
            }
        }

        m.appendTail(sb);

        return sb.toString();
    }
}
