package org.openbooth.dao.impl;

import org.junit.Test;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Background;

import java.sql.SQLException;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class JDBCBackgroundCategoryDAOTest extends JDBCTestEnvironment {

    private JDBCBackgroundCategoryDAO jdbcBackgroundCategoryDAO;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        jdbcBackgroundCategoryDAO = new JDBCBackgroundCategoryDAO(mockDBHandler);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        //noinspection MagicConstant
        when(mockConnection.prepareStatement(anyString(),anyInt())).thenThrow(SQLException.class);
    }

    @Test(expected = PersistenceException.class)
    public void testCreateWithSQLException() throws PersistenceException {
        jdbcBackgroundCategoryDAO.create(new Background.Category("test"));
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateWithSQLException() throws PersistenceException {
        jdbcBackgroundCategoryDAO.update(new Background.Category("test"));
    }

    @Test(expected = PersistenceException.class)
    public void testReadWithSQLException() throws PersistenceException{
        jdbcBackgroundCategoryDAO.read(1);
    }

    @Test(expected = PersistenceException.class)
    public void testReadAllWithSQLException() throws PersistenceException{
        jdbcBackgroundCategoryDAO.readAll();
    }

    @Test(expected = PersistenceException.class)
    public void testDeleteWithSQLException() throws PersistenceException {
        jdbcBackgroundCategoryDAO.delete(new Background.Category("test"));
    }
}
