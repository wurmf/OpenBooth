package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * ProfileDAO Tester
 */
public abstract class ProfileDAOTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileDAOTest.class);
    private ProfileDAO profileDAO;
    protected void setProfileDAO(ProfileDAO profileDAO){
        this.profileDAO=profileDAO;
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProfileWithNullArgument() throws PersistenceException {
        profileDAO.create(null);
    }

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

    @Test
    public void readProfileWithValidArgumentThatExistsInPersistenceStore() throws PersistenceException{
        Profile profile = new Profile(100, "Testprofile100",null,null,false,false,false,false);
        profileDAO.create(profile);
        profile = profileDAO.read(100);
        assertTrue(profile.getName() == "Testprofile100" && profile.getId() == 100);
    }

    public void readProfileWithValidArgumentThatDoesNotExistInPersistenceStore() throws PersistenceException{
        Profile profile = new Profile(100, "Testprofile100",null,null,false,false,false,false);
        profileDAO.create(profile);
        profile = profileDAO.read(101);
        assertTrue(profile == null);
    }

    public void deleteProfileWithValidArgumentThatExistsInPersistenceStore() throws PersistenceException{
        Profile profile = new Profile(100, "Testprofile100",null,null,false,false,false,false);
        profileDAO.create(profile);
        profile = profileDAO.read(100);
        assertTrue(profile.getName() == "Testprofile100" && profile.getId() == 100);
        profileDAO.delete(profile);
        profile = profileDAO.read(100);
        assertTrue(profile == null);
    }

}

