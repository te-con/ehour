/**
 * Created on Oct 20, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour;

import java.io.File;
import java.io.FileInputStream;

import org.mortbay.jetty.Server;
import org.mortbay.xml.XmlConfiguration;

/**
 * Start jetty 
 **/

public class EhourWrapper
{

	public void start(String filename) throws Exception
	{
		Server server = new Server();
		
		File file = new File(filename);
		System.out.println(file.getAbsolutePath());
		
		XmlConfiguration configuration = new XmlConfiguration(new FileInputStream(filename));
		configuration.configure(server);
		server.start();		
	}
	
	public static void main(String[] arg) throws Exception
	{
		new EhourWrapper().start(arg[0]);
	}
}
