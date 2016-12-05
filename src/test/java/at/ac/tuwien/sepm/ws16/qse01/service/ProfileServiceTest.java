package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCProfileDAOTest;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * ProfileService Tester
 */
public abstract class ProfileServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCProfileDAOTest.class);
    @Autowired
    private ProfileService profileService;
    protected void setProfileService(ProfileService profileService){
        this.profileService=profileService;
    }

    @Test(expected = IllegalArgumentException.class)
    public void addProfileWithNullArgument() throws ServiceException {
        profileService.add(null);
    }

    @Test
    public void addProfileWithValidArgumentWithAutoID() throws ServiceException {
        Profile profile = new Profile("Testprofile");
        assertTrue(profile.getId() == Integer.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profileService.add(profile);
        assertTrue(profile.getId()>=1);
        assertFalse(profile.getId() == Integer.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
    }

    @Test
    public void addProfileWithValidArgumentWithoutAutoID() throws ServiceException {
        Profile profile = new Profile(10,"TestprofileNoAutoID",false);
        assertTrue(profile.getId() == 10);
        assertTrue(profile.getName() == "TestprofileNoAutoID");
        assertTrue(!profile.isActive());
        profileService.add(profile);
        assertTrue(profile.getId()==10);
        assertFalse(profile.getId() == Integer.MIN_VALUE);
        assertTrue(profile.getName() == "TestprofileNoAutoID");
        assertTrue(!profile.isActive());
    }

    @Test(expected = IllegalArgumentException.class)
    public void editProfileWithNullArgument() throws ServiceException {
        profileService.edit(null);
    }

    @Test
    public void editProfileWithValidArgument() throws ServiceException {
        Profile profile = new Profile("Testprofile");
        assertTrue(profile.getId() == Integer.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profileService.add(profile);
        assertTrue(profile.getId()>=1);
        assertFalse(profile.getId() == Integer.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profile.setName("Testprofile2");
        profile.setIsActive(true);
        profileService.edit(profile);
        int id = profile.getId();
        profile = profileService.get(id);
        assertTrue(profile.getName() == "Testprofile2");
        assertTrue(profile.isActive());
    }

    @Test
    public void editProfileWithValidValidArgumentButDoesNotExistInPersistenceStore() throws ServiceException{
        Profile profile = new Profile("Testprofile");
        assertTrue(profile.getId() == Integer.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profileService.add(profile);
        assertTrue(profile.getId()>=1);
        assertFalse(profile.getId() == Integer.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profile.setName("Testprofile2");
        profile.setIsActive(true);
        profile.setId(profile.getId()+1);
        profileService.edit(profile);
        int id = profile.getId();
        profile = profileService.get(id-1);
        assertFalse(profile.getName()=="Testprofile2");
        assertFalse(profile.isActive());
    }

    @Test
    public void getProfileWithValidArgumentThatExistsInPersistenceStore() throws ServiceException{
        Profile profile = new Profile(100, "Testprofile100", false);
        profileService.add(profile);
        profile = profileService.get(100);
        assertTrue(profile.getName() == "Testprofile100" && profile.getId() == 100 && !profile.isActive());
    }

    public void readProfileWithValidArgumentThatDoesNotExistInPersistenceStore() throws ServiceException{
        Profile profile = new Profile(100, "Testprofile100", false);
        profileService.add(profile);
        profile = profileService.get(101);
        assertTrue(profile == null);
    }

    public void deleteProfileWithValidArgumentThatExistsInPersistenceStore() throws ServiceException{
        Profile profile = new Profile(100, "Testprofile100", false);
        profileService.add(profile);
        profile = profileService.get(100);
        assertTrue(profile.getName() == "Testprofile100" && profile.getId() == 100 && !profile.isActive());
        profileService.erase(profile);
        profile = profileService.get(100);
        assertTrue(profile == null);
    }

}