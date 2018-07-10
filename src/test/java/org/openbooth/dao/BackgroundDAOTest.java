package org.openbooth.dao;

import org.junit.Test;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.TestEnvironment;
import org.openbooth.entities.Background;
import org.openbooth.util.TestDataProvider;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BackgroundDAOTest extends TestEnvironment {

    private BackgroundDAO backgroundDAO = getApplicationContext().getBean(BackgroundDAO.class);
    private TestDataProvider dataProvider = getApplicationContext().getBean(TestDataProvider.class);

    private Background testBackground;
    List<Background> backgrounds;


    @Override
    public void setUp() throws Exception{
        super.setUp();

        backgrounds = dataProvider.getStoredBackgrounds();
        testBackground = backgrounds.get(0);

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

        for(Background background : backgrounds){
            background.setId(backgroundDAO.create(background).getId());
        }

        List<Background> backgroundList = backgroundDAO.readAll();
        assertTrue(backgroundList.containsAll(backgrounds));
    }


    @Test(expected = PersistenceException.class)
    public void testReadWithNonExistingBackground() throws PersistenceException{
        backgroundDAO.read(-1);
    }

    @Test
    public void testValidReadAllWithCategory() throws PersistenceException{

        for(Background background : backgrounds){
            background.setId(backgroundDAO.create(background).getId());
        }

        Background.Category category = backgrounds.get(0).getCategory();

        //TODO: backgrounds with existing categories?


        //backgrounds = backgrounds.stream().filter(ba)

        List<Background> backgroundList = backgroundDAO.readAllWithCategory(backgroundCategory1);

        //assertTrue(backgroundList.containsAll());
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
    public void testDeleteWithNotExistingBackground() throws PersistenceException{
        testBackground.setId(-1);
        backgroundDAO.delete(testBackground);
    }














}
