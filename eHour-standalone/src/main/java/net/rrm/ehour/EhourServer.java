/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour;

import java.io.InputStream;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import net.rrm.ehour.datasource.DerbyDataSourceFactory;

import org.apache.commons.dbcp.BasicDataSource;
import org.eclipse.jetty.jndi.NamingUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.xml.XmlConfiguration;

/**
 * Start jetty
 **/

public class EhourServer
{
	public void startServer(ServerConfig config) throws Exception
	{
		Server server = new Server(config.getPort());

		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(config.getDefaultConfigFileName());
		
		XmlConfiguration configuration = new XmlConfiguration(inputStream);
		configuration.configure(server);
		
		registerJndiDS(config);

		server.start();
		server.join();
	}

	private void registerJndiDS(ServerConfig config)
	{
		DataSource dataSource = createDataSource(config);
			
		Context context;

		try
		{
			context = new InitialContext();
			Context compCtx = (Context) context.lookup("java:comp");
			Context envCtx = compCtx.createSubcontext("env");
			NamingUtil.bind(envCtx, "jdbc/eHourDS", dataSource);
		}

		catch (NamingException e)
		{

		}

	}

	private DataSource createDataSource(ServerConfig config)
	{
		DataSource dataSource;

		if ("derby".equalsIgnoreCase(config.getDataBase()))
		{
			dataSource = new DerbyDataSourceFactory().createDataSource("ehourDb");
		} 
		else
		{
			BasicDataSource dbcpDataSource = new BasicDataSource();
		
			dbcpDataSource.setDriverClassName(config.getDataBaseDriver());
			dbcpDataSource.setUrl(config.getDataBaseURL());
			dbcpDataSource.setUsername(config.getDataBaseUsername());
			dbcpDataSource.setPassword(config.getDataBasePassword());

			dataSource = dbcpDataSource;
		}
		return dataSource;
	}
}
