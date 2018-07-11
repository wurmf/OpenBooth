package org.openbooth.dao;

import org.junit.Test;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.TestEnvironment;
import org.openbooth.entities.Background;
import org.openbooth.util.TestDataProvider;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BackgroundDAOTest extends TestEnvironment {

    private BackgroundDAO backgroundDAO = getApplicationContext().getBean(BackgroundDAO.class);
    private TestDataProvider testDataProvider;


    private List<Background> storedBackgrounds;
    private Background testBackground;

    @Override
    protected void prepareTestData() {
        testDataProvider = getNewTestDataProvider();
        storedBackgrounds = testDataProvider.getStoredBackgrounds();
        testBackground = testDataProvider.getNewBackground();
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
    public void testCreateWithNotExistingCategory() throws PersistenceException{
        Background.Category notExistingCategory = testDataProvider.getNewCategory();
        testBackground.setCategory(notExistingCategory);
        backgroundDAO.create(testBackground);
    }


    @Test
    public void testValidUpdateAndRead() throws PersistenceException{
        Background storedBackground = storedBackgrounds.get(0);
        storedBackground.setName("New Name");
        storedBackground.setPath("New Path");
        storedBackground.setCategory(testDataProvider.getStoredBackgroundCategories().get(0));
        backgroundDAO.update(storedBackground);
        Background updatedBackground = backgroundDAO.read(storedBackground.getId());
        assertEquals(storedBackground, updatedBackground);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNull() throws PersistenceException{
        backgroundDAO.update(null);
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
    public void testValidReadAll() throws PersistenceException{

        List<Background> backgroundList = backgroundDAO.readAll();
        assertTrue(backgroundList.containsAll(storedBackgrounds));
        assertTrue(storedBackgrounds.containsAll(backgroundList));
    }


    @Test(expected = PersistenceException.class)
    public void testReadWithNonExistingBackground() throws PersistenceException{
        backgroundDAO.read(-1);
    }

    @Test
    public void testValidReadAllWithCategory() throws PersistenceException{

        Background.Category category = storedBackgrounds.get(0).getCategory();


        storedBackgrounds = storedBackgrounds.stream().filter(b -> b.getCategory().equals(category)).collect(Collectors.toList());

        List<Background> readBackgrounds = backgroundDAO.readAllWithCategory(category);

        assertTrue(readBackgrounds.containsAll(storedBackgrounds));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadAllWithCategoryWithNull() throws PersistenceException{
        backgroundDAO.readAllWithCategory(null);
    }

    @Test
    public void testReadAllWithCategoryWithNotExistingCategory() throws PersistenceException{
        Background.Category notExistingCategory = new Background.Category(-1, "test", false);
        assertTrue(backgroundDAO.readAllWithCategory(notExistingCategory).isEmpty());
    }


    @Test
    public void testValidDeleteAndReadAll() throws PersistenceException {
        Background storedBackground = storedBackgrounds.get(0);
        backgroundDAO.delete(storedBackground);
        List<Background> backgroundList = backgroundDAO.readAll();
        assertTrue(!backgroundList.contains(storedBackground));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteWithNull() throws PersistenceException{
        backgroundDAO.delete(null);
    }


    @Test(expected = PersistenceException.class)
    public void testDeleteWithNotExistingBackground() throws PersistenceException{
        testBackground.setId(-1);
        backgroundDAO.delete(testBackground);
    }














}
