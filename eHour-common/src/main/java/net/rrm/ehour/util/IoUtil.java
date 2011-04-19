package net.rrm.ehour.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/9/11 - 10:20 PM
 */
public final class IoUtil
{
    private IoUtil()
    {
    }

    public static void close(Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            } catch (IOException e)
            {
                // safely ignore it
            }
        }
    }
}
