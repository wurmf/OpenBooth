package org.openbooth.dao.impl;

import org.mockito.Mock;
import org.openbooth.TestEnvironment;
import org.openbooth.util.dbhandler.DBHandler;
import org.openbooth.util.exceptions.DatabaseException;

import java.sql.*;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public abstract class JDBCTestEnvironment extends TestEnvironment {

    @Mock protected DBHandler mockDBHandler;
    @Mock protected Connection mockConnection;
    @Mock protected Statement mockStatement;
    @Mock protected PreparedStatement mockPreparedStatement;
    @Mock protected ResultSet mockResultSet;

    protected void setUpDBMocks() throws DatabaseException, SQLException {
        when(mockDBHandler.getConnection()).thenReturn(mockConnection);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.prepareStatement(anyString(),anyInt())).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE,Boolean.FALSE);
    }

    public void setUp() throws Exception{
        setUpDBMocks();
    }
}
