package org.openbooth.dao;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.dao.impl.TestEnvironment;
import org.openbooth.entities.Background;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * BackgroundCategoryDAO Tester
 */
public class BackgroundCategoryDAOTest extends TestEnvironment {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundCategoryDAOTest.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     *
     * TESTING method: Background.Category create(Background.Category backgroundCategory
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_create_withNullArguments_Fail() throws Exception{
        mockbackgroundCategoryDAO.create(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_create_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString(),anyInt())).thenThrow(SQLException.class);
        mockbackgroundCategoryDAO.create(this.backgroundCategoryA);
    }

    @Test
    public void test_create_withValidInputArguments() throws Exception {
        assertTrue(backgroundCategoryA.getId() == Integer.MIN_VALUE);
        assertTrue(backgroundCategoryA.getName().equals("Taufe"));
        assertTrue(!backgroundCategoryA.isDeleted());
        Background.Category returnValue = backgroundCategoryDAO.create(backgroundCategoryA);
        assertTrue(returnValue.getId()>=1);
        assertTrue(returnValue.getName().equals("Taufe"));
        assertTrue(!returnValue.isDeleted());
    }

    @Test
    public void testmock_create_withValidInputArguments() throws Exception {
        when(mockResultSet.next()).thenReturn(Boolean.TRUE);
        when(mockResultSet.getInt(1)).thenReturn(1);
        Background.Category returnvalue = mockbackgroundCategoryDAO.create(backgroundCategoryA);
        verify(mockPreparedStatement).executeUpdate();
        assertTrue(returnvalue.getId() == 1);
        assertTrue(returnvalue.getName().equals("Taufe"));
    }

    @Test(expected = PersistenceException.class)
    public void test_create_withAlreadyExistingInputparameter_fail() throws Exception{
        assertTrue(backgroundCategory10.getId() == 10);
        assertTrue(backgroundCategory10.getName().equals("Verlobung"));
        assertTrue(!backgroundCategory10.isDeleted());
        Background.Category returnValue = backgroundCategoryDAO.create(backgroundCategory10);
        assertTrue(backgroundCategory10.getId() == 10);
        assertTrue(backgroundCategory10.getName().equals("Verlobung"));
        assertTrue(!backgroundCategory10.isDeleted());
        Background.Category returnValue2 = backgroundCategoryDAO.create(backgroundCategory10);
    }

    /**
     *
     * TESTING method: boolean update(Background.Category backgroundCategory)
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_update_withNullArguments_Fail() throws Exception{
        mockbackgroundCategoryDAO.update(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_update_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockbackgroundCategoryDAO.update(backgroundCategoryA);
    }

    @Test
    public void test_update_withValidInputParameter()throws Exception{
        backgroundCategoryA = backgroundCategoryDAO.create(backgroundCategoryA);
        backgroundCategoryA.setName("Taufe +");
        boolean returnBoolean = backgroundCategoryDAO.update(backgroundCategoryA);
        assertTrue(returnBoolean);
    }

    @Test
    public void test_update_WithNotExisting()throws Exception{
        backgroundCategoryA = backgroundCategoryDAO.create(backgroundCategoryA);
        backgroundCategoryA.setId(backgroundCategoryA.getId() + 1);
        positionA.setName("Taufe +");
        boolean returnBoolean = backgroundCategoryDAO.update(backgroundCategoryA);
        assertTrue(!returnBoolean);
    }

    /**
     *
     * TESTING method: Background.Category read(int id)
     * throws PersistenceException;
     */

    @Test
    public void test_read_withValidInt() throws Exception{
        backgroundCategoryA = backgroundCategoryDAO.create(backgroundCategoryA);
        backgroundCategoryB = backgroundCategoryDAO.create(backgroundCategoryB);
        backgroundCategory10 = backgroundCategoryDAO.create(backgroundCategory10);
        Background.Category returnValue = backgroundCategoryDAO.read(backgroundCategoryA.getId());
        Background.Category returnValue2 = backgroundCategoryDAO.read(backgroundCategoryB.getId());
        Background.Category returnValue3 = backgroundCategoryDAO.read(backgroundCategory10.getId());
        assertTrue(returnValue.equals(backgroundCategoryA));
        assertTrue(returnValue2.equals(backgroundCategoryB));
        assertTrue(returnValue3.equals(backgroundCategory10));
    }

    @Test
    public void test_read_NotExisting() throws Exception{
        backgroundCategoryA = backgroundCategoryDAO.create(backgroundCategoryA);
        assertTrue(backgroundCategoryDAO.read(backgroundCategoryA.getId() + 1) == null);
    }

    /**
     *
     * TESTING method: boolean delete(Background.Category backgroundCategory)
     * throws PersistenceException;
     */

    @Test
    public void test_readAll_withNonEmptyReturnList() throws Exception{
        backgroundCategoryA = backgroundCategoryDAO.create(backgroundCategoryA);
        backgroundCategoryB = backgroundCategoryDAO.create(backgroundCategoryB);
        backgroundCategory10 = backgroundCategoryDAO.create(backgroundCategory10);
        List<Background.Category> returnList = backgroundCategoryDAO.readAll();
        assertTrue(returnList.contains(backgroundCategoryA)
                && returnList.contains(backgroundCategoryB)
                && returnList.contains(backgroundCategory10));
    }

    @Test
    public void test_readAll_withEmptyReturnList() throws Exception{
        backgroundCategoryDAO.delete(backgroundCategoryDAO.read(1));
        backgroundCategoryDAO.delete(backgroundCategoryDAO.read(2));
        backgroundCategoryDAO.delete(backgroundCategoryDAO.read(3));
        backgroundCategoryDAO.delete(backgroundCategoryDAO.read(4));
        assertTrue(backgroundCategoryDAO.readAll().size() == 0);
    }

}
