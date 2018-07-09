package org.openbooth.dao;

import org.junit.Test;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.TestEnvironment;
import org.openbooth.entities.Background;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class BackgroundDAOTest extends TestEnvironment {

    private BackgroundDAO backgroundDAO = getApplicationContext().getBean(BackgroundDAO.class);

    private Background testBackground;
    private Background background1;
    private Background background2;
    private Background background3;


    @Override
    public void setUp() throws Exception {
        super.setUp();
        testBackground = new Background("Testbackground", "/no/valid/path", backgroundCategory1);
        background1 = new Background("test1", "testpath", backgroundCategory1);
        background2 = new Background("test2", "testpath", backgroundCategory2);
        background3 = new Background("test3", "testpath", backgroundCategory3);
    }

    @Test
    public void testValidCreateAndRead() throws PersistenceException {
        testBackground.setId(backgroundDAO.create(testBackground).getId());
        Background storedBackground = backgroundDAO.read(testBackground.getId());
        assertEquals(testBackground,storedBackground);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNull() throws PersistenceException{
        backgroundDAO.create(null);
    }

    @Test(expected = PersistenceException.class)
    public void testCreateWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockBackgroundDAO.create(testBackground);
    }

    @Test(expected = PersistenceException.class)
    public void testCreateWithNotExistingCategory() throws PersistenceException{
        Background.Category notExistingCategory = new Background.Category(-1, "test", false);
        testBackground.setCategory(notExistingCategory);
        backgroundDAO.create(testBackground);
    }


    @Test
    public void testValidCreateUpdateAndRead() throws PersistenceException{
        testBackground.setId(backgroundDAO.create(testBackground).getId());
        testBackground.setName("New Name");
        testBackground.setPath("New Path");
        testBackground.setCategory(backgroundCategory2);
        backgroundDAO.update(testBackground);
        Background storedBackground = backgroundDAO.read(testBackground.getId());
        assertEquals(testBackground, storedBackground);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNull() throws PersistenceException{
        backgroundDAO.update(null);
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockBackgroundDAO.update(testBackground);
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateWithNotExistingBackground() throws PersistenceException{
        testBackground.setId(-1);
        backgroundDAO.update(testBackground);
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateWithNotExistingCategory() throws PersistenceException{
        Background.Category notExistingCategory = new Background.Category(-1, "test", false);
        testBackground.setCategory(notExistingCategory);
        backgroundDAO.update(testBackground);
    }

    @Test
    public void testValidCreateAndReadAll() throws PersistenceException{
        background1.setId(backgroundDAO.create(background1).getId());
        background2.setId(backgroundDAO.create(background2).getId());
        background3.setId(backgroundDAO.create(background3).getId());


        List<Background> backgroundList = backgroundDAO.readAll();

        assertTrue(backgroundList.contains(background1));
        assertTrue(backgroundList.contains(background2));
        assertTrue(backgroundList.contains(background3));
    }


    @Test(expected = PersistenceException.class)
    public void testReadAllWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockBackgroundDAO.readAll();
    }

    @Test(expected = PersistenceException.class)
    public void testReadWithNonExistingBackground() throws PersistenceException{
        backgroundDAO.read(-1);
    }

    @Test(expected = PersistenceException.class)
    public void testReadWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockBackgroundDAO.read(-1);
    }

    @Test
    public void testValidReadAllWithCategory() throws PersistenceException{
        testBackground.setId(backgroundDAO.create(testBackground).getId());
        background1.setId(backgroundDAO.create(background1).getId());
        background2.setId(backgroundDAO.create(background2).getId());
        background3.setId(backgroundDAO.create(background3).getId());

        List<Background> backgroundList = backgroundDAO.readAllWithCategory(backgroundCategory1);

        assertTrue(backgroundList.contains(background1));
        assertTrue(backgroundList.contains(testBackground));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadAllWithCategoryWithNull() throws PersistenceException{
        backgroundDAO.readAllWithCategory(null);
    }

    @Test(expected = PersistenceException.class)
    public void testReadAllWithCategoryWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockBackgroundDAO.readAllWithCategory(backgroundCategory1);
    }

    @Test
    public void testReadAllWithCategoryWithNotExistingCategory() throws PersistenceException{
        Background.Category notExistingCategory = new Background.Category(-1, "test", false);
        assertTrue(backgroundDAO.readAllWithCategory(notExistingCategory).isEmpty());
    }


    @Test
    public void testValidCreateDeleteAndRead() throws PersistenceException {
        testBackground.setId(backgroundDAO.create(testBackground).getId());
        Background storedBackground = backgroundDAO.read(testBackground.getId());
        assertEquals(testBackground,storedBackground);
        backgroundDAO.delete(testBackground);
        List<Background> backgroundList = backgroundDAO.readAll();
        assertTrue(!backgroundList.contains(testBackground));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteWithNull() throws PersistenceException{
        backgroundDAO.delete(null);
    }

    @Test(expected = PersistenceException.class)
    public void testDeleteWithSQLException() throws SQLException, PersistenceException{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockBackgroundDAO.delete(testBackground);
    }

    @Test(expected = PersistenceException.class)
    public void testDeleteWithNotExistingBackground() throws PersistenceException{
        testBackground.setId(-1);
        backgroundDAO.delete(testBackground);
    }














}
