package org.openbooth.dao;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.TestEnvironment;
import org.openbooth.dao.impl.JDBCBackgroundCategoryDAO;
import org.openbooth.dao.impl.JDBCTestEnvironment;
import org.openbooth.entities.Background;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * BackgroundCategoryDAO Tester
 */
public class BackgroundCategoryDAOTest extends JDBCTestEnvironment {

    private Background.Category backgroundCategoryA,backgroundCategoryB, backgroundCategoryC;

    private BackgroundCategoryDAO backgroundCategoryDAO = getApplicationContext().getBean(BackgroundCategoryDAO.class);
    private BackgroundCategoryDAO mockBackgroundCategoryDAO;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        backgroundCategoryA = new Background.Category("Taufe");
        backgroundCategoryB = new Background.Category("Firmung");
        backgroundCategoryC = new Background.Category(10,"Verlobung",false);

        mockBackgroundCategoryDAO = new JDBCBackgroundCategoryDAO(mockDBHandler);
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
    public void testmock_create_withNullArguments_Fail() throws PersistenceException{
        mockbackgroundCategoryDAO.create(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_create_withPersistenceTroubles_Fail() throws PersistenceException, SQLException{
        when(mockConnection.prepareStatement(anyString(),anyInt())).thenThrow(SQLException.class);
        mockbackgroundCategoryDAO.create(this.backgroundCategoryA);
    }

    @Test
    public void test_create_withValidInputArguments() throws PersistenceException {
        Background.Category returnValue = backgroundCategoryDAO.create(backgroundCategoryA);
        backgroundCategoryA.setId(returnValue.getId());
        Background.Category persistedCategory = backgroundCategoryDAO.read(returnValue.getId());

        assertTrue(returnValue.getId()>=1);
        assertEquals(backgroundCategoryA, persistedCategory);
    }

    /**
     *
     * TESTING method: boolean update(Background.Category backgroundCategory)
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_update_withNullArguments_Fail() throws PersistenceException{
        mockbackgroundCategoryDAO.update(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_update_withPersistenceTroubles_Fail() throws PersistenceException, SQLException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockbackgroundCategoryDAO.update(backgroundCategoryA);
    }


    @Test
    public void test_update_withValidInputParameter()throws PersistenceException{
        backgroundCategoryA = backgroundCategoryDAO.create(backgroundCategoryA);
        backgroundCategoryA.setName("Taufe +");
        backgroundCategoryDAO.update(backgroundCategoryA);
        Background.Category updatedCategory = backgroundCategoryDAO.read(backgroundCategoryA.getId());
        assertEquals(backgroundCategoryA, updatedCategory);
    }

    @Test(expected = PersistenceException.class)
    public void test_update_WithNotExisting()throws PersistenceException{
        backgroundCategoryA = backgroundCategoryDAO.create(backgroundCategoryA);
        backgroundCategoryA.setId(backgroundCategoryA.getId() + 1);
        positionA.setName("Taufe +");
        backgroundCategoryDAO.update(backgroundCategoryA);

    }

    /**
     *
     * TESTING method: Background.Category read(int id)
     * throws PersistenceException;
     */

    @Test
    public void test_read_withValidInt() throws PersistenceException{
        backgroundCategoryA = backgroundCategoryDAO.create(backgroundCategoryA);
        backgroundCategoryB = backgroundCategoryDAO.create(backgroundCategoryB);
        backgroundCategoryC = backgroundCategoryDAO.create(backgroundCategoryC);
        Background.Category returnValue = backgroundCategoryDAO.read(backgroundCategoryA.getId());
        Background.Category returnValue2 = backgroundCategoryDAO.read(backgroundCategoryB.getId());
        Background.Category returnValue3 = backgroundCategoryDAO.read(backgroundCategoryC.getId());
        assertEquals(backgroundCategoryA, returnValue);
        assertEquals(backgroundCategoryB, returnValue2);
        assertEquals(backgroundCategoryC, returnValue3);
    }

    @Test(expected = PersistenceException.class)
    public void test_read_NotExisting() throws PersistenceException{
        backgroundCategoryA = backgroundCategoryDAO.create(backgroundCategoryA);
        backgroundCategoryDAO.read(backgroundCategoryA.getId() + 1);
    }

    /**
     *
     * TESTING method: boolean delete(Background.Category backgroundCategory)
     * throws PersistenceException;
     */

    @Test
    public void test_readAll_withNonEmptyReturnList() throws PersistenceException{
        backgroundCategoryA = backgroundCategoryDAO.create(backgroundCategoryA);
        backgroundCategoryB = backgroundCategoryDAO.create(backgroundCategoryB);
        backgroundCategoryC = backgroundCategoryDAO.create(backgroundCategoryC);
        List<Background.Category> returnList = backgroundCategoryDAO.readAll();
        assertTrue(returnList.contains(backgroundCategoryA)
                && returnList.contains(backgroundCategoryB)
                && returnList.contains(backgroundCategoryC));
    }

    @Test
    public void test_readAll_withEmptyReturnList() throws PersistenceException{
        backgroundCategoryDAO.delete(backgroundCategoryDAO.read(1));
        backgroundCategoryDAO.delete(backgroundCategoryDAO.read(2));
        backgroundCategoryDAO.delete(backgroundCategoryDAO.read(3));
        backgroundCategoryDAO.delete(backgroundCategoryDAO.read(4));
        assertTrue(backgroundCategoryDAO.readAll().isEmpty());
    }

}
