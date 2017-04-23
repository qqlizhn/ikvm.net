/*
  Copyright (C) 2009, 2011 Volker Berlin (i-net software)

  This software is provided 'as-is', without any express or implied
  warranty.  In no event will the authors be held liable for any damages
  arising from the use of this software.

  Permission is granted to anyone to use this software for any purpose,
  including commercial applications, and to alter it and redistribute it
  freely, subject to the following restrictions:

  1. The origin of this software must not be misrepresented; you must not
     claim that you wrote the original software. If you use this software
     in a product, an acknowledgment in the product documentation would be
     appreciated but is not required.
  2. Altered source versions must be plainly marked as such, and must not be
     misrepresented as being the original software.
  3. This notice may not be removed or altered from any source distribution.

  Jeroen Frijters
  jeroen@frijters.net
  
 */
package sun.jdbc.odbc;

import java.sql.*;

import cli.System.Data.*;
import cli.System.Data.Common.*;
import cli.System.Data.Odbc.*;

/**
 * This JDBC Driver is a wrapper to the ODBC.NET Data Provider.
 */
public class JdbcOdbcStatement implements Statement{

    private final JdbcOdbcConnection jdbcConn;

    protected final OdbcCommand command;

    private final int resultSetType;
    
    private final int resultSetConcurrency;
    
    private DbDataReader reader;
    
    private ResultSet rs;
    
    private int updateCount;
    
    private boolean isClosed;
    
    private ResultSet moreResults;

    public JdbcOdbcStatement(JdbcOdbcConnection jdbcConn, OdbcCommand command, int resultSetType, int resultSetConcurrency){
        this.jdbcConn = jdbcConn;
        this.command = command;
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
    }


    public void addBatch(String sql) throws SQLException{
        // TODO Auto-generated method stub

    }


    public void cancel() throws SQLException{
        try{
            command.Cancel();
        }catch(Throwable ex){
            throw JdbcOdbcUtils.createSQLException(ex);
        }
    }


    public void clearBatch() throws SQLException{
        // TODO Auto-generated method stub

    }


    public void clearWarnings() throws SQLException{
        // TODO Auto-generated method stub

    }


    public void close() throws SQLException{
        isClosed = true;
        if(rs != null){
            rs.close();
        }
        if(reader != null){
            reader.Close();
        }
        command.Dispose();
    }


    public boolean execute(String sql) throws SQLException{
        try{
            if(sql != null){
                command.set_CommandText(sql);
            }
            command.ExecuteNonQuery();
            return false;
        }catch(Throwable ex){
            throw JdbcOdbcUtils.createSQLException(ex);
        }
    }


    public boolean execute(String sql, int autoGeneratedKeys){
        throw new UnsupportedOperationException();
    }


    public boolean execute(String sql, int[] columnIndexes){
        throw new UnsupportedOperationException();
    }


    public boolean execute(String sql, String[] columnNames){
        throw new UnsupportedOperationException();
    }


    public int[] executeBatch() throws SQLException{
        // TODO Auto-generated method stub
        return null;
    }


    public ResultSet executeQuery(String sql) throws SQLException{
        try{
            if(sql != null){
                command.set_CommandText(sql);
            }
            if(resultSetConcurrency == ResultSet.CONCUR_UPDATABLE){
                rs = new JdbcOdbcUpdateableResultSet(command);
            }else{
                if(resultSetType == ResultSet.TYPE_FORWARD_ONLY){
                    reader = command.ExecuteReader();
                    rs = new JdbcOdbcResultSet(this, reader);
                }else{
                    OdbcDataAdapter da = new OdbcDataAdapter(command);
                    DataTable dt = new DataTable();
                    da.Fill(dt);
                    rs = new JdbcOdbcDTResultSet(dt);
                }
            }
            return rs;
        }catch(Throwable ex){
            throw JdbcOdbcUtils.createSQLException(ex);
        }
    }


    public int executeUpdate(String sql) throws SQLException{
        try{
            if(sql != null){
                command.set_CommandText(sql);
            }
            updateCount = command.ExecuteNonQuery();
            return updateCount;
        }catch(Throwable ex){
            throw JdbcOdbcUtils.createSQLException(ex);
        }
    }


    public int executeUpdate(String sql, int autoGeneratedKeys){
        throw new UnsupportedOperationException();
    }


    public int executeUpdate(String sql, int[] columnIndexes){
        throw new UnsupportedOperationException();
    }


    public int executeUpdate(String sql, String[] columnNames){
        throw new UnsupportedOperationException();
    }


    public Connection getConnection(){
        return jdbcConn;
    }


    public int getFetchDirection(){
        return ResultSet.FETCH_UNKNOWN;
    }


    public int getFetchSize(){
        return 0;
    }


    public ResultSet getGeneratedKeys(){
        throw new UnsupportedOperationException();
    }


    public int getMaxFieldSize() throws SQLException{
        // TODO Auto-generated method stub
        return 0;
    }


    public int getMaxRows() throws SQLException{
        // TODO Auto-generated method stub
        return 0;
    }


    public boolean getMoreResults() throws SQLException{
        try{
            if(moreResults != null){
                rs = moreResults;
                moreResults = null;
                return true;
            }
            boolean isNext = reader.NextResult();
            if(isNext){
                rs = new JdbcOdbcResultSet(this, reader);
                return true;
            }
            rs = null;
            return false;
        }catch(Throwable th){
            throw JdbcOdbcUtils.createSQLException(th);
        }
    }


    public boolean getMoreResults(int current) throws SQLException{
        // TODO Auto-generated method stub
        return false;
    }


    public int getQueryTimeout(){
        return command.get_CommandTimeout();
    }


    public ResultSet getResultSet(){
        return rs;
    }


    public int getResultSetConcurrency(){
        return resultSetConcurrency;
    }


    public int getResultSetHoldability() throws SQLException{
        // TODO Auto-generated method stub
        return 0;
    }


    public int getResultSetType(){
        return resultSetType;
    }


    public int getUpdateCount(){
        return updateCount;
    }


    public SQLWarning getWarnings() throws SQLException{
        // TODO Auto-generated method stub
        return null;
    }


    public boolean isClosed(){
        return isClosed;
    }


    public void setCursorName(String name) throws SQLException{
        // TODO Auto-generated method stub

    }


    public void setEscapeProcessing(boolean enable) throws SQLException{
        // TODO Auto-generated method stub

    }


    public void setFetchDirection(int direction){
        // ignore it
    }


    public void setFetchSize(int rows){
        // ignore it
    }


    public void setMaxFieldSize(int max) throws SQLException{
        // TODO Auto-generated method stub

    }


    public void setMaxRows(int max) throws SQLException{
        // TODO Auto-generated method stub

    }


    public boolean isPoolable(){
        return false;
    }


    public void setPoolable(boolean poolable) throws SQLException{
        // ignore it
    }


    public void setQueryTimeout(int seconds){
        command.set_CommandTimeout(seconds);
    }


    public boolean isWrapperFor(Class<?> iface){
        return iface.isAssignableFrom(this.getClass());
    }


    public <T>T unwrap(Class<T> iface) throws SQLException{
        if(isWrapperFor(iface)){
            return (T)this;
        }
        throw new SQLException(this.getClass().getName() + " does not implements " + iface.getName() + ".", "01000");
    }
    
    /**
     * Close the DbDataReader if there are no more results.
     * This give some blocking free without calling close() explicit.
     * If there are more results then we need to save it.
     */
    void closeReaderIfPossible(){
        ResultSet currentRs = rs;
        boolean isMoreResults;
        try{
            isMoreResults = getMoreResults();
        }catch(SQLException ex){
            isMoreResults = false;
        }
        if(!isMoreResults){
            reader.Close(); //this give the ODBC cursor free
        }else{
            moreResults = rs;
        }
        rs = currentRs;
    }


    /**
     * {@inheritDoc}
     */
	public void closeOnCompletion() throws SQLException {
	}


    /**
     * {@inheritDoc}
     */
	public boolean isCloseOnCompletion() throws SQLException {
		return false;
	}

}
