/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.PropertyConverter;
import org.apache.commons.logging.LogFactory;

/**
 * Extension which adds blob support. 90% from the code is copy-paste from DatabaseConfiguration
 * 
 * Created on Apr 1, 2009, 2:13:55 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExtDatabaseConfiguration extends AbstractConfiguration
{
    /** The datasource to connect to the database. */
    private DataSource datasource;

    /** The name of the table containing the configurations. */
    private String table;

    /** The column containing the keys. */
    private String keyColumn;

    /** The column containing the values. */
    private String valueColumn;
    
    private String blobValueColumn;

    /**
     * Build a configuration from a table containing multiple configurations.
     *
     * @param datasource    the datasource to connect to the database
     * @param table         the name of the table containing the configurations
     * @param nameColumn    the column containing the name of the configuration
     * @param keyColumn     the column containing the keys of the configuration
     * @param valueColumn   the column containing the values of the configuration
     * @param name          the name of the configuration
     */
    public ExtDatabaseConfiguration(DataSource datasource, String table, 
            String keyColumn, String valueColumn, String blobValueColumn)
    {
        this.datasource = datasource;
        this.table = table;
        this.keyColumn = keyColumn;
        this.valueColumn = valueColumn;
        this.blobValueColumn = blobValueColumn;
        setLogger(LogFactory.getLog(getClass()));
        addErrorLogListener();  // log errors per default
    }

    /**
     * Returns the value of the specified property. If this causes a database
     * error, an error event will be generated of type
     * <code>EVENT_READ_PROPERTY</code> with the causing exception. The
     * event's <code>propertyName</code> is set to the passed in property key,
     * the <code>propertyValue</code> is undefined.
     *
     * @param key the key of the desired property
     * @return the value of this property
     */
    public Object getProperty(String key)
    {
        Object result = null;

        Connection conn = null;
        
        try
		{
        	conn = getConnection();
        	
            result = findValue(key, valueColumn, conn);
            
            if (result == null)
            {
            	result = findValue(key, blobValueColumn, conn);
            }
		} catch (SQLException e)
		{
			fireError(EVENT_READ_PROPERTY, key, null, e);
		}
        finally
        {
			close(conn);
		}

        return result;
    }

	private Object findValue(String key, String column, Connection conn) throws SQLException
	{
		StringBuffer query = new StringBuffer("SELECT * FROM ");
		query.append(table).append(" WHERE ");
		query.append(keyColumn).append("=?");        	
		return execute(conn, key, column, query.toString());
	}
	
	/**
	 * @param key
	 * @param result
	 * @param query
	 * @return
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	private Object execute(Connection connection, String key, String column, String query) throws SQLException
	{
        PreparedStatement pstmt = null;
        Object result = null;
        
        try
        {
            // bind the parameters
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, key);

            ResultSet rs = pstmt.executeQuery();

            List<Object> results = new ArrayList<Object>();
            
            while (rs.next())
            {
                Object value = rs.getObject(column);
                if (isDelimiterParsingDisabled())
                {
                    results.add(value);
                }
                else
                {
                    // Split value if it containts the list delimiter
                    CollectionUtils.addAll(results, PropertyConverter.toIterator(value, getListDelimiter()));
                }
            }

            if (!results.isEmpty())
            {
                result = (results.size() > 1) ? results : results.get(0);
            }
        }
        finally
        {
            close(pstmt);
        }
        
		return result;
	}

    /**
     * Adds a property to this configuration. If this causes a database error,
     * an error event will be generated of type <code>EVENT_ADD_PROPERTY</code>
     * with the causing exception. The event's <code>propertyName</code> is
     * set to the passed in property key, the <code>propertyValue</code>
     * points to the passed in value.
     *
     * @param key the property key
     * @param obj the value of the property to add
     */
    protected void addPropertyDirect(String key, Object obj)
    {
        // build the query
        StringBuffer query = new StringBuffer("INSERT INTO " + table);
           query.append(" (" + keyColumn + ", " + valueColumn + ") VALUES (?, ?)");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try
        {
            conn = getConnection();

            // bind the parameters
            pstmt = conn.prepareStatement(query.toString());
            int index = 1;

            pstmt.setString(index++, key);
            pstmt.setString(index++, String.valueOf(obj));

            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            fireError(EVENT_ADD_PROPERTY, key, obj, e);
        }
        finally
        {
            // clean up
            close(conn, pstmt);
        }
    }

    /**
     * Adds a property to this configuration. This implementation will
     * temporarily disable list delimiter parsing, so that even if the value
     * contains the list delimiter, only a single record will be written into
     * the managed table. The implementation of <code>getProperty()</code>
     * will take care about delimiters. So list delimiters are fully supported
     * by <code>DatabaseConfiguration</code>, but internally treated a bit
     * differently.
     *
     * @param key the key of the new property
     * @param value the value to be added
     */
    public void addProperty(String key, Object value)
    {
        boolean parsingFlag = isDelimiterParsingDisabled();
        try
        {
            if (value instanceof String)
            {
                // temporarily disable delimiter parsing
                setDelimiterParsingDisabled(true);
            }
            super.addProperty(key, value);
        }
        finally
        {
            setDelimiterParsingDisabled(parsingFlag);
        }
    }

    /**
     * Checks if this configuration is empty. If this causes a database error,
     * an error event will be generated of type <code>EVENT_READ_PROPERTY</code>
     * with the causing exception. Both the event's <code>propertyName</code>
     * and <code>propertyValue</code> will be undefined.
     *
     * @return a flag whether this configuration is empty.
     */
    public boolean isEmpty()
    {
        boolean empty = true;

        // build the query
        StringBuffer query = new StringBuffer("SELECT count(*) FROM " + table);

        Connection conn = null;
        PreparedStatement pstmt = null;

        try
        {
            conn = getConnection();

            // bind the parameters
            pstmt = conn.prepareStatement(query.toString());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
            {
                empty = rs.getInt(1) == 0;
            }
        }
        catch (SQLException e)
        {
            fireError(EVENT_READ_PROPERTY, null, null, e);
        }
        finally
        {
            // clean up
            close(conn, pstmt);
        }

        return empty;
    }

    /**
     * Checks whether this configuration contains the specified key. If this
     * causes a database error, an error event will be generated of type
     * <code>EVENT_READ_PROPERTY</code> with the causing exception. The
     * event's <code>propertyName</code> will be set to the passed in key, the
     * <code>propertyValue</code> will be undefined.
     *
     * @param key the key to be checked
     * @return a flag whether this key is defined
     */
    public boolean containsKey(String key)
    {
        boolean found = false;

        // build the query
        StringBuffer query = new StringBuffer("SELECT * FROM " + table + " WHERE " + keyColumn + "=?");
        Connection conn = null;
        PreparedStatement pstmt = null;

        try
        {
            conn = getConnection();

            // bind the parameters
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setString(1, key);

            ResultSet rs = pstmt.executeQuery();

            found = rs.next();
        }
        catch (SQLException e)
        {
            fireError(EVENT_READ_PROPERTY, key, null, e);
        }
        finally
        {
            // clean up
            close(conn, pstmt);
        }

        return found;
    }

    /**
     * Removes the specified value from this configuration. If this causes a
     * database error, an error event will be generated of type
     * <code>EVENT_CLEAR_PROPERTY</code> with the causing exception. The
     * event's <code>propertyName</code> will be set to the passed in key, the
     * <code>propertyValue</code> will be undefined.
     *
     * @param key the key of the property to be removed
     */
    public void clearProperty(String key)
    {
        // build the query
        StringBuffer query = new StringBuffer("DELETE FROM " + table + " WHERE " + keyColumn + "=?");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try
        {
            conn = getConnection();

            // bind the parameters
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setString(1, key);

            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            fireError(EVENT_CLEAR_PROPERTY, key, null, e);
        }
        finally
        {
            // clean up
            close(conn, pstmt);
        }
    }

    /**
     * Removes all entries from this configuration. If this causes a database
     * error, an error event will be generated of type
     * <code>EVENT_CLEAR</code> with the causing exception. Both the
     * event's <code>propertyName</code> and the <code>propertyValue</code>
     * will be undefined.
     */
    public void clear()
    {
        // build the query
        StringBuffer query = new StringBuffer("DELETE FROM " + table);

        Connection conn = null;
        PreparedStatement pstmt = null;

        try
        {
            conn = getConnection();

            // bind the parameters
            pstmt = conn.prepareStatement(query.toString());
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            fireError(EVENT_CLEAR, null, null, e);
        }
        finally
        {
            // clean up
            close(conn, pstmt);
        }
    }

    /**
     * Returns an iterator with the names of all properties contained in this
     * configuration. If this causes a database
     * error, an error event will be generated of type
     * <code>EVENT_READ_PROPERTY</code> with the causing exception. Both the
     * event's <code>propertyName</code> and the <code>propertyValue</code>
     * will be undefined.
     * @return an iterator with the contained keys (an empty iterator in case
     * of an error)
     */
    public Iterator<String> getKeys()
    {
        Collection<String> keys = new ArrayList<String>();

        // build the query
        StringBuffer query = new StringBuffer("SELECT DISTINCT " + keyColumn + " FROM " + table);

        Connection conn = null;
        PreparedStatement pstmt = null;

        try
        {
            conn = getConnection();

            // bind the parameters
            pstmt = conn.prepareStatement(query.toString());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                keys.add(rs.getString(1));
            }
        }
        catch (SQLException e)
        {
            fireError(EVENT_READ_PROPERTY, null, null, e);
        }
        finally
        {
            // clean up
            close(conn, pstmt);
        }

        return keys.iterator();
    }

    /**
     * Returns the used <code>DataSource</code> object.
     *
     * @return the data source
     * @since 1.4
     */
    public DataSource getDatasource()
    {
        return datasource;
    }

    /**
     * Returns a <code>Connection</code> object. This method is called when
     * ever the database is to be accessed. This implementation returns a
     * connection from the current <code>DataSource</code>.
     *
     * @return the <code>Connection</code> object to be used
     * @throws SQLException if an error occurs
     * @since 1.4
     * @deprecated Use a custom data source to change the connection used by the
     * class. To be removed in Commons Configuration 2.0
     */
    protected Connection getConnection() throws SQLException
    {
        return getDatasource().getConnection();
    }

    /**
     * Close a <code>Connection</code> and, <code>Statement</code>.
     * Avoid closing if null and hide any SQLExceptions that occur.
     *
     * @param conn The database connection to close
     * @param stmt The statement to close
     */
    private void close(Connection conn, Statement stmt)
    {
        close(stmt);

        close(conn);
    }

	/**
	 * @param conn
	 */
	private void close(Connection conn)
	{
		try
        {
            if (conn != null)
            {
                conn.close();
            }
        }
        catch (SQLException e)
        {
            getLogger().error("An error occured on closing the connection", e);
        }
	}

	/**
	 * @param stmt
	 */
	private void close(Statement stmt)
	{
		try
        {
            if (stmt != null)
            {
                stmt.close();
            }
        }
        catch (SQLException e)
        {
            getLogger().error("An error occured on closing the statement", e);
        }
	}
}
