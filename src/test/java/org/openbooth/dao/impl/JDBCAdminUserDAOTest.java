package org.openbooth.dao.impl;

import org.junit.Test;
import org.openbooth.dao.exceptions.PersistenceException;

import java.sql.SQLException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class JDBCAdminUserDAOTest extends JDBCTestEnvironment{

    private JDBCAdminUserDAO adminUserDAO;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        adminUserDAO = new JDBCAdminUserDAO(mockDBHandler);
    }

    @Test(expected = PersistenceException.class)
    public void testReadWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        adminUserDAO.read("test");
    }
}
