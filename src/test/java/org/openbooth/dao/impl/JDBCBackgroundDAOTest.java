package org.openbooth.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.openbooth.dao.BackgroundDAO;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Background;
import org.openbooth.service.exceptions.ServiceException;
import org.openbooth.util.exceptions.DatabaseException;

import java.sql.SQLException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class JDBCBackgroundDAOTest extends JDBCTestEnvironment{

    private BackgroundDAO mockBackgroundDAO;
    private Background testBackground = new Background("test", "test", null);

    @Before
    public void setUp() throws Exception{
        super.setUp();
        mockBackgroundDAO = new JDBCBackgroundDAO(mockDBHandler, mockbackgroundCategoryDAO);
    }

    @Test(expected = PersistenceException.class)
    public void testCreateWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockBackgroundDAO.create(testBackground);
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockBackgroundDAO.update(testBackground);
    }

    @Test(expected = PersistenceException.class)
    public void testReadAllWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockBackgroundDAO.readAll();
    }

    @Test(expected = PersistenceException.class)
    public void testReadWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockBackgroundDAO.read(-1);
    }

    @Test(expected = PersistenceException.class)
    public void testReadAllWithCategoryWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockBackgroundDAO.readAllWithCategory(new Background.Category("test"));
    }

    @Test(expected = PersistenceException.class)
    public void testDeleteWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockBackgroundDAO.delete(testBackground);
    }

}
