package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.TestEnvironment;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.TestCase.assertTrue;

/**
 * ProfileService Tester
 */
public class ProfileServiceTest extends TestEnvironment{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileServiceTest.class);
    //@Autowired
    //private ProfileService profileService;

    private Profile profile1;
    private Profile profile2;

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /* Testing method
    Profile get(int id) throws ServiceException;
    */
    @Test
    public void test_get_withValidInt() throws ServiceException{
        profile1 = profileService.get(1);
        assertTrue(profile1.getName().equals("Profile 1"));
        assertTrue(!profile1.isPrintEnabled());
        assertTrue(!profile1.isFilerEnabled());
        assertTrue(!profile1.isGreenscreenEnabled());
        assertTrue(!profile1.isMobilEnabled());
        assertTrue(profile1.getWatermark().isEmpty());
        assertTrue(profile1.getPairCameraPositions().get(0).getCamera().getId()==1);
        assertTrue(profile1.getPairCameraPositions().get(0).getPosition().getId()==1);
        assertTrue(profile1.getPairCameraPositions().get(1).getCamera().getId()==2);
        assertTrue(profile1.getPairCameraPositions().get(1).getPosition().getId()==2);
        profile2 = profileService.get(2);
        assertTrue(profile2.getName().equals("Profile 2"));
        assertTrue(!profile2.isPrintEnabled());
        assertTrue(!profile2.isFilerEnabled());
        assertTrue(!profile2.isGreenscreenEnabled());
        assertTrue(!profile2.isMobilEnabled());
        assertTrue(profile2.getWatermark().isEmpty());
        assertTrue(profile2.getPairCameraPositions().get(0).getCamera().getId()==2);
        assertTrue(profile2.getPairCameraPositions().get(0).getPosition().getId()==1);
        assertTrue(profile2.getPairCameraPositions().get(1).getCamera().getId()==1);
        assertTrue(profile2.getPairCameraPositions().get(1).getPosition().getId()==2);
    }

    @Test
    public void test_read_NotExisting() throws Exception{
        Assert.assertTrue(profileService.get(1000000) == null);
    }

    /*
    @Test
    public void addProfileWithValidArgumentWithAutoID() throws ServiceException {
        Profile profile = new Profile("Testprofile");
        assertTrue(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profileService.add(profile);
        assertTrue(profile.getId()>=1);
        assertFalse(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
    }
    */

    /*
    @Test
    public void addProfileWithValidArgumentWithoutAutoID() throws ServiceException {
        Profile profile = new Profile(10,"TestprofileNoAutoID",null,null,false,false,false,false);
        assertTrue(profile.getId() == 10);
        assertTrue(profile.getName() == "TestprofileNoAutoID");
        profileService.add(profile);
        assertTrue(profile.getId()==10);
        assertFalse(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "TestprofileNoAutoID");
    }
    */

    /*
    @Test
    public void editProfileWithValidArgument() throws ServiceException {
        Profile profile = new Profile("Testprofile");
        assertTrue(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profileService.add(profile);
        assertTrue(profile.getId()>=1);
        assertFalse(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profile.setName("Testprofile2");

        profileService.edit(profile);
        int id = profile.getId();
        profile = profileService.get(id);
        assertTrue(profile.getName() == "Testprofile2");
    }
     */

    /*
    @Test
    public void editProfileWithValidValidArgumentButDoesNotExistInPersistenceStore() throws ServiceException{
        Profile profile = new Profile("Testprofile");
        assertTrue(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profileService.add(profile);
        assertTrue(profile.getId()>=1);
        assertFalse(profile.getId() == Long.MIN_VALUE);
        assertTrue(profile.getName() == "Testprofile");
        profile.setName("Testprofile2");
        profile.setId(profile.getId()+1);
        profileService.edit(profile);
        int id = profile.getId();
        profile = profileService.get(id-1);
        assertFalse(profile.getName()=="Testprofile2");
    }
    */

    /*
    public void getProfileWithValidArgumentThatExistsInPersistenceStore() throws ServiceException{
        Profile profile = new Profile(100, "Testprofile100", null,null,false,false,false,false);
        profileService.add(profile);
        profile = profileService.get(100);
        assertTrue(profile.getName() == "Testprofile100" && profile.getId() == 100);
    }
    */


    @Test
    public void readProfileWithValidArgumentThatDoesNotExistInPersistenceStore() throws ServiceException{
        Profile profile = new Profile("Testprofile100");
        profile = profileService.add(profile);
        profile = profileService.get(profile.getId()+1);
        assertTrue(profile == null);
    }

    @Test
    public void deleteProfileWithValidArgumentThatExistsInPersistenceStore() throws ServiceException{
        Profile profile = new Profile("Testprofile100");
        profile = profileService.add(profile);
        profile = profileService.get(profile.getId());
        assertTrue(profile.getName().equals("Testprofile100"));
        int i = profile.getId();
        profileService.erase(profile);
        profile = profileService.get(i);
        assertTrue(profile == null);
    }

}
