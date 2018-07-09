
    package org.openbooth.dao;

    import org.openbooth.dao.exceptions.PersistenceException;
    import org.openbooth.TestEnvironment;
    import org.openbooth.entities.AdminUser;
    import org.junit.Test;

    import java.io.UnsupportedEncodingException;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import java.util.Arrays;

    import static junit.framework.TestCase.assertTrue;
    import static org.junit.Assert.assertEquals;
    import static org.junit.Assert.assertNotNull;
    import static org.junit.Assert.assertNull;

    /**
     * Abstract test-class for AdminUserDAOs.
     */
    public class AdminUserDAOTest extends TestEnvironment {

        /**
         * Check if nonexistent-user-read returns null
         */
        @Test
        public void readNonExistentUser() throws PersistenceException{
            assertNull(adminUserDAO.read("nonExistentUser-NoOneIsNamedLikeThis"));
        }

        /**
         * Checks if read(null) returns null and not an exception
         */
        @Test
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
        public void readExistingUser() throws NoSuchAlgorithmException, UnsupportedEncodingException, PersistenceException{
            String nameToLookFor="admin";
            String correspondingPassword="martin";

            MessageDigest md=MessageDigest.getInstance("SHA-256");
            md.update(correspondingPassword.getBytes("UTF-8"));
            byte[] correspondingPasswordBytes=md.digest();
            AdminUser user=adminUserDAO.read(nameToLookFor);
            assertNotNull(user);
            assertEquals(nameToLookFor, user.getAdminName());
            assertTrue(Arrays.equals(correspondingPasswordBytes, user.getPassword()));

        }
    }


