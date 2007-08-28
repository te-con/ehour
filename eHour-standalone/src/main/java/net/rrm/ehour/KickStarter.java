/**
 * Created on Jun 15, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * eHour standalone kickstarter
 **/

public class KickStarter
{
	// TODO make configurable
	public final static int PORT	= 8000;
	
	/**
	 * 
	 * @throws Exception
	 */
	
	public void start() throws Exception
	{
//		initDerby();
		startJetty();
	}
	
	/**
	 * Start Jetty
	 * @throws Exception
	 */
	
	private void startJetty() throws Exception
	{
		Server server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(PORT);
        connector.setHost("127.0.0.1");
        server.addConnector(connector);		
		
        WebAppContext wac = new WebAppContext();
        wac.setContextPath("/");
        wac.setWar("../../ehour/eHour-wicketweb-0.7-SNAPSHOT.war");
        server.setHandler(wac);
        server.setStopAtShutdown(true);
        
        server.start();
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		new KickStarter().start();

	}

}
