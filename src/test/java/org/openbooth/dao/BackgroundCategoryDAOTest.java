package org.openbooth.dao;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.dao.impl.JDBCTestEnvironment;
import org.openbooth.entities.Background;
import org.junit.Test;
import org.openbooth.service.exceptions.ServiceException;
import org.openbooth.util.TestDataProvider;
import org.openbooth.util.exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * BackgroundCategoryDAO Tester
 */
public class BackgroundCategoryDAOTest extends JDBCTestEnvironment {

    private BackgroundCategoryDAO backgroundCategoryDAO = getApplicationContext().getBean(BackgroundCategoryDAO.class);

    private TestDataProvider testDataProvider;

    private List<Background.Category> storedCategories;
    private Background.Category storedCategory;
    private Background.Category newCategory;

    @Override
    protected void prepareTestData() {
        testDataProvider = getNewTestDataProvider();
        storedCategories = testDataProvider.getStoredBackgroundCategories();
        storedCategory = storedCategories.get(0);
        newCategory = testDataProvider.getNewCategory();
    }


    /**
     * TESTING method: Background.Category create(Background.Category backgroundCategory)
     */

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNull() throws PersistenceException{
        backgroundCategoryDAO.create(null);
    }

    @Test
    public void testValidCreateAndRead() throws PersistenceException {
        newCategory.setId(backgroundCategoryDAO.create(newCategory).getId());
        assertEquals(newCategory, backgroundCategoryDAO.read(newCategory.getId()));
    }


    /**
     * TESTING method: void update(Background.Category backgroundCategory)
     */


    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNull() throws PersistenceException{
        backgroundCategoryDAO.update(null);
    }


    @Test
    public void testValidUpdate()throws PersistenceException{
        storedCategory.setName("NewName");
        backgroundCategoryDAO.update(storedCategory);
        assertEquals(storedCategory, backgroundCategoryDAO.read(storedCategory.getId()));
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateWithNotExistingCategory()throws PersistenceException{
        backgroundCategoryDAO.update(testDataProvider.getNewCategory());

    }

    /**
     * TESTING method: Background.Category read(int id)
     */

    @Test
    public void testValidRead() throws PersistenceException{
        assertEquals(storedCategory, backgroundCategoryDAO.read(storedCategory.getId()));
    }

    @Test(expected = PersistenceException.class)
    public void testReadWithNotExistingCategory() throws PersistenceException{
        backgroundCategoryDAO.read(testDataProvider.getNewCategory().getId());
    }


    /**
     * TESTING method: List<Background.Category> readAll()
     */

    @Test
    public void testValidReadAll() throws PersistenceException{
        List<Background.Category> readCategories = backgroundCategoryDAO.readAll();
        assertTrue(readCategories.containsAll(storedCategories));
        assertTrue(storedCategories.containsAll(readCategories));

    }

    @Test
    public void testValidDeleteAndReadAll() throws PersistenceException{
       for(Background.Category category : storedCategories){
           backgroundCategoryDAO.delete(category);
       }
        assertTrue(backgroundCategoryDAO.readAll().isEmpty());
    }

    /**
     * TESTING method: void delete(Background.Category backgroundCategory)
     */

    @Test
    public void testValidDelete() throws PersistenceException{
        backgroundCategoryDAO.delete(storedCategory);
        assertTrue(!backgroundCategoryDAO.readAll().contains(storedCategory));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteWithNull() throws PersistenceException{
        backgroundCategoryDAO.delete(null);
    }


}
