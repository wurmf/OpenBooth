package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.TestEnvironment;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * ProfileDAO Tester
 */
public class ProfileDAOTest extends TestEnvironment{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileDAOTest.class);
    private Profile profileA;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        profileA = new Profile("Profile A");
    }

    @After
    public void tearDown() throws PersistenceException {
        super.tearDown();
    }

    /**
     *
     *TESTING method: Profile create(Profile profile)
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_create_withNullArguments_Fail() throws Exception{
        mockProfileDAO.create(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_create_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString(),anyInt())).thenThrow(SQLException.class);
        mockProfileDAO.create(this.profileA);
    }

    /**
    @Test
    public void createProfileWithValidArgumentWithAutoID() throws PersistenceException {
        Profile profile = new Profile("Testprofile");
        assertTrue(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profileDAO.create(profile);
        assertTrue(profile.getId()>=1);
        assertFalse(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
    }
    */

    /**
    @Test
    public void createProfileWithValidArgumentWithoutAutoID() throws PersistenceException {
        Profile profile = new Profile(10,"TestprofileNoAutoID",null,null,false,false,false,false);
        assertTrue(profile.getId() == 10);
        assertTrue(profile.getName() == "TestprofileNoAutoID");

        profileDAO.create(profile);
        assertTrue(profile.getId()==10);
        assertFalse(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "TestprofileNoAutoID");
    }

     */

    @Test(expected = IllegalArgumentException.class)
    public void updateProfileWithNullArgument() throws PersistenceException {
        profileDAO.update(null);
    }

    /**
    @Test
    public void updateProfileWithValidArgument() throws PersistenceException {
        Profile profile = new Profile("Testprofile");
        assertTrue(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profileDAO.create(profile);
        assertTrue(profile.getId()>=1);
        assertFalse(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profile.setName("Testprofile2");

        profileDAO.update(profile);
        long id = profile.getId();
        profile = profileDAO.read(id);
        assertTrue(profile.getName() == "Testprofile2");
    }
    */

    /**
    @Test
    public void updateProfileWithValidValidArgumentButDoesNotExistInPersistenceStore() throws PersistenceException{
        Profile profile = new Profile("Testprofile");
        assertTrue(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profileDAO.create(profile);
        assertTrue(profile.getId()>=1);
        assertFalse(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profile.setName("Testprofile2");
        profile.setId(profile.getId()+1);
        profileDAO.update(profile);
        long id = profile.getId();
        profile = profileDAO.read(id-1);
        assertFalse(profile.getName()=="Testprofile2");
    }
     */

    /**
    @Test
    public void readProfileWithValidArgumentThatExistsInPersistenceStore() throws PersistenceException{
        Profile profile = new Profile(100, "Testprofile100",null,null,false,false,false,false);
        profileDAO.create(profile);
        profile = profileDAO.read(100);
        assertTrue(profile.getName() == "Testprofile100" && profile.getId() == 100);
    }
    */

    public void readProfileWithValidArgumentThatDoesNotExistInPersistenceStore() throws PersistenceException{
        Profile profile = new Profile(100, "Testprofile100",null,null,false,false,false,false,"",false);
        profileDAO.create(profile);
        profile = profileDAO.read(101);
        assertTrue(profile == null);
    }

    public void deleteProfileWithValidArgumentThatExistsInPersistenceStore() throws PersistenceException{
        Profile profile = new Profile(100, "Testprofile100",null,null,false,false,false,false,"",false);
        profileDAO.create(profile);
        profile = profileDAO.read(100);
        assertTrue(profile.getName() == "Testprofile100" && profile.getId() == 100);
        profileDAO.delete(profile);
        profile = profileDAO.read(100);
        assertTrue(profile == null);
    }

}

