
    package at.ac.tuwien.sepm.ws16.qse01.dao;

    import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
    import at.ac.tuwien.sepm.ws16.qse01.dao.impl.TestEnvironment;
    import at.ac.tuwien.sepm.ws16.qse01.entities.AdminUser;
    import org.junit.Test;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import java.io.UnsupportedEncodingException;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import java.util.Arrays;

    import static junit.framework.TestCase.assertFalse;
    import static junit.framework.TestCase.assertTrue;

    /**
     * Abstract test-class for AdminUserDAOs.
     */
    public class AdminUserDAOTest extends TestEnvironment {

        static final Logger LOGGER = LoggerFactory.getLogger(AdminUserDAOTest.class);

        /**
         * Check if nonexistent-user-read returns null
         */
        @Test
        public void readNonExistentUser(){
            try {
                assertTrue(adminUserDAO.read("nonExistentUser-NoOneIsNamedLikeThis")==null);
            } catch (PersistenceException e) {
                LOGGER.error("readNonExistentUser - ",e);
            }
        }

        /**
         * Checks if read(null) returns null and not an exception
         */
        @Test
        public void readNullUser(){
            try {
                assertTrue(adminUserDAO.read(null)==null);
            } catch (PersistenceException e) {
                LOGGER.error("readNullUser - ",e);
            }
        }

        /**
         * Checks if read of empty string returns null
         */
        @Test
        public void readEmptyUserName(){
            try {
                assertTrue(adminUserDAO.read("")==null);
            } catch (PersistenceException e) {
                LOGGER.error("readEmptyUserName - ",e);
            }
        }

        /**
         * Checks if existing user is returned and same values are returned as are put in the database
         */
        @Test
        public void readExistingUser(){
            try {
                String nameToLookFor="admin";
                String correspondingPassword="martin";

                MessageDigest md=MessageDigest.getInstance("SHA-256");
                md.update(correspondingPassword.getBytes("UTF-8"));
                byte[] correspondingPasswordBytes=md.digest();
                AdminUser user=adminUserDAO.read(nameToLookFor);
                assertFalse(user==null);
                assertFalse(!user.getAdminName().equals(nameToLookFor));
                assertTrue(Arrays.equals(correspondingPasswordBytes, user.getPassword()));
            } catch (PersistenceException|NoSuchAlgorithmException|UnsupportedEncodingException e) {
                LOGGER.error("readEmptyUserName - ",e);
            }
        }
    }


