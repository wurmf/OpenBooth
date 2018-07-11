
    package org.openbooth.dao;

    import org.openbooth.dao.exceptions.PersistenceException;
    import org.openbooth.TestEnvironment;
    import org.openbooth.entities.AdminUser;
    import org.junit.Test;

    import static org.junit.Assert.assertEquals;
    import static org.junit.Assert.assertNotNull;
    import static org.junit.Assert.assertNull;

    /**
     * Abstract test-class for AdminUserDAOs.
     */
    public class AdminUserDAOTest extends TestEnvironment {

        private AdminUser storedAdminUser;
        private AdminUserDAO adminUserDAO = getApplicationContext().getBean(AdminUserDAO.class);

        @Override
        protected void prepareTestData() {
            storedAdminUser = getNewTestDataProvider().getStoredAdminUsers().get(0);
        }

        /**
         * Check if nonexistent-user-read returns null
         */
        @Test
        public void readNonExistentUser() throws PersistenceException{
            assertNull(adminUserDAO.read("nonExistentUser-NoOneIsNamedLikeThis"));
        }

        /**
         * Checks if read(null) throws an IllegalArgumentException
         */
        @Test(expected = IllegalArgumentException.class)
        public void readNullUser() throws PersistenceException{

            assertNull(adminUserDAO.read(null));
        }

        /**
         * Checks if read of empty string returns null
         */
        @Test
        public void readEmptyUserName() throws PersistenceException{

            assertNull(adminUserDAO.read(""));
        }

        /**
         * Checks if existing user is returned and same values are returned as are put in the database
         */
        @Test
        public void readExistingUser() throws PersistenceException{

            AdminUser userFromDatabase = adminUserDAO.read(storedAdminUser.getAdminName());
            assertNotNull(userFromDatabase);
            assertEquals(storedAdminUser, userFromDatabase);

        }
    }


