package org.openbooth.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Background;

import java.sql.SQLException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class JDBCBackgroundDAOTest extends JDBCTestEnvironment{

    private JDBCBackgroundDAO backgroundDAO;
    private Background testBackground = new Background("test", "test", null);

    @Before
    public void setUp() throws Exception{
        super.setUp();

        backgroundDAO = new JDBCBackgroundDAO(mockDBHandler, new JDBCBackgroundCategoryDAO(mockDBHandler));
    }

    @Test(expected = PersistenceException.class)
    public void testCreateWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        backgroundDAO.create(testBackground);
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        backgroundDAO.update(testBackground);
    }

    @Test(expected = PersistenceException.class)
    public void testReadAllWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        backgroundDAO.readAll();
    }

    @Test(expected = PersistenceException.class)
    public void testReadWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        backgroundDAO.read(-1);
    }

    @Test(expected = PersistenceException.class)
    public void testReadAllWithCategoryWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        backgroundDAO.readAllWithCategory(new Background.Category("test"));
    }

    @Test(expected = PersistenceException.class)
    public void testDeleteWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        backgroundDAO.delete(testBackground);
    }

}
