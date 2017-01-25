package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.dao.impl.TestEnvironment;
import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.entities.RelativeRectangle;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * ProfileService Tester
 */
public class ProfileServiceTest extends TestEnvironment{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileServiceTest.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /* Testing method
    Profile add(Profile profile) throws ServiceException;
     */
    @Test
    public void test_add_withValidElementaryProfileEntity() throws ServiceException {
        profileA = profileService.add(profileA);
        assertTrue(profileA != null);
        assertTrue(profileA.getName().equals("Profile A"));
        assertTrue(!profileA.isPrintEnabled());
        assertTrue(!profileA.isFilerEnabled());
        assertTrue(!profileA.isGreenscreenEnabled());
        assertTrue(!profileA.isMobilEnabled());
        assertTrue(profileA.getWatermark().isEmpty());
        assertTrue(profileA.getPairCameraPositions().isEmpty());
        assertTrue(profileA.getPairLogoRelativeRectangles().isEmpty());
    }

        @Test
        public void test_add_withValidFullProfileEntity() throws ServiceException{
        profileB.setPairCameraPositions(pairCameraPositions);
        profileB.setPairLogoRelativeRectangles(pairLogoRelativeRectangles);
        profileB.setBackgroundCategories(categories);
        profileB = profileService.add(profileB);
        assertTrue(this.profileB != null);
        assertTrue(profileB.getName().equals("Profile B"));
        assertTrue(profileB.isPrintEnabled());
        assertTrue(profileB.isFilerEnabled());
        assertTrue(profileB.isGreenscreenEnabled());
        assertTrue(profileB.isMobilEnabled());
        assertTrue(profileB.getWatermark().equals("/dev/null/watermarkB.jpg"));
        assertTrue(!profileB.getPairCameraPositions().isEmpty());
        assertTrue(profileB.getPairCameraPositions().size() == 2);
        assertTrue(profileB.getPairCameraPositions().get(0).getCamera().getId()==1);
        assertTrue(profileB.getPairCameraPositions().get(0).getPosition().getId()==1);
        assertTrue(profileB.getPairCameraPositions().get(1).getCamera().getId()==2);
        assertTrue(profileB.getPairCameraPositions().get(1).getPosition().getId()==2);
        assertTrue(!profileB.getPairLogoRelativeRectangles().isEmpty());
        assertTrue(profileB.getPairLogoRelativeRectangles().size() == 2);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(0).getLogo().getId()==1);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(0).getRelativeRectangle().equals(relativeRectangleA));
        assertTrue(profileB.getPairLogoRelativeRectangles().get(1).getLogo().getId()==2);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(1).getRelativeRectangle().equals(relativeRectangleB));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory1));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory2));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory3));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory4));
    }

    /* Testing method
    Profile get(int id) throws ServiceException;
    */

    @Test
    public void test_get_withValidElementaryProfileEntity() throws ServiceException {
        profileA = profileService.add(profileA);
        assertTrue(this.profileA != null);
        assertTrue(profileA.getName().equals("Profile A"));
        assertTrue(!profileA.isPrintEnabled());
        assertTrue(!profileA.isFilerEnabled());
        assertTrue(!profileA.isGreenscreenEnabled());
        assertTrue(!profileA.isMobilEnabled());
        assertTrue(profileA.getWatermark().isEmpty());
        assertTrue(profileA.getPairCameraPositions().isEmpty());
        assertTrue(profileA.getPairLogoRelativeRectangles().isEmpty());
        profileA = profileService.get(profileA.getId());
        assertTrue(this.profileA != null);
        assertTrue(profileA.getName().equals("Profile A"));
        assertTrue(!profileA.isPrintEnabled());
        assertTrue(!profileA.isFilerEnabled());
        assertTrue(!profileA.isGreenscreenEnabled());
        assertTrue(!profileA.isMobilEnabled());
        assertTrue(profileA.getWatermark().isEmpty());
        assertTrue(profileA.getPairCameraPositions().isEmpty());
        assertTrue(profileA.getPairLogoRelativeRectangles().isEmpty());
    }

    @Test
    public void test_get_withValidFullProfileEntity() throws ServiceException{
        profileB.setPairCameraPositions(pairCameraPositions);
        profileB.setPairLogoRelativeRectangles(pairLogoRelativeRectangles);
        profileB.setBackgroundCategories(categories);
        profileB = profileService.add(profileB);
        assertTrue(this.profileB != null);
        assertTrue(profileB.getName().equals("Profile B"));
        assertTrue(profileB.isPrintEnabled());
        assertTrue(profileB.isFilerEnabled());
        assertTrue(profileB.isGreenscreenEnabled());
        assertTrue(profileB.isMobilEnabled());
        assertTrue(profileB.getWatermark().equals("/dev/null/watermarkB.jpg"));
        assertTrue(!profileB.getPairCameraPositions().isEmpty());
        assertTrue(profileB.getPairCameraPositions().size() == 2);
        assertTrue(profileB.getPairCameraPositions().get(0).getCamera().getId()==1);
        assertTrue(profileB.getPairCameraPositions().get(0).getPosition().getId()==1);
        assertTrue(profileB.getPairCameraPositions().get(1).getCamera().getId()==2);
        assertTrue(profileB.getPairCameraPositions().get(1).getPosition().getId()==2);
        assertTrue(!profileB.getPairLogoRelativeRectangles().isEmpty());
        assertTrue(profileB.getPairLogoRelativeRectangles().size() == 2);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(0).getLogo().getId()==1);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(0).getRelativeRectangle().equals(relativeRectangleA));
        assertTrue(profileB.getPairLogoRelativeRectangles().get(1).getLogo().getId()==2);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(1).getRelativeRectangle().equals(relativeRectangleB));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory1));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory2));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory3));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory4));
        profileB = profileService.get(profileB.getId());
        assertTrue(this.profileB != null);
        assertTrue(profileB.getName().equals("Profile B"));
        assertTrue(profileB.isPrintEnabled());
        assertTrue(profileB.isFilerEnabled());
        assertTrue(profileB.isGreenscreenEnabled());
        assertTrue(profileB.isMobilEnabled());
        assertTrue(profileB.getWatermark().equals("/dev/null/watermarkB.jpg"));
        assertTrue(!profileB.getPairCameraPositions().isEmpty());
        assertTrue(profileB.getPairCameraPositions().size() == 2);
        assertTrue(profileB.getPairCameraPositions().get(0).getCamera().getId()==1);
        assertTrue(profileB.getPairCameraPositions().get(0).getPosition().getId()==1);
        assertTrue(profileB.getPairCameraPositions().get(1).getCamera().getId()==2);
        assertTrue(profileB.getPairCameraPositions().get(1).getPosition().getId()==2);
        assertTrue(!profileB.getPairLogoRelativeRectangles().isEmpty());
        assertTrue(profileB.getPairLogoRelativeRectangles().size() == 2);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(0).getLogo().getId()==1);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(0).getRelativeRectangle().equals(relativeRectangleA));
        assertTrue(profileB.getPairLogoRelativeRectangles().get(1).getLogo().getId()==2);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(1).getRelativeRectangle().equals(relativeRectangleB));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory1));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory2));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory3));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory4));
    }

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
        assertTrue(!profile1.getPairCameraPositions().get(0).isGreenScreenReady());
        assertTrue(profile1.getPairCameraPositions().get(1).getCamera().getId()==2);
        assertTrue(profile1.getPairCameraPositions().get(1).getPosition().getId()==2);
        assertTrue(!profile1.getPairCameraPositions().get(1).isGreenScreenReady());
        assertTrue(profile1.getPairLogoRelativeRectangles().get(0).getLogo().getId()==1);
        assertTrue(profile1.getPairLogoRelativeRectangles().get(0).getRelativeRectangle().equals(new RelativeRectangle(85,95,80,90)));
        assertTrue(profile1.getPairLogoRelativeRectangles().get(1).getLogo().getId()==2);
        assertTrue(profile1.getPairLogoRelativeRectangles().get(1).getRelativeRectangle().equals(new RelativeRectangle(5,10,15,20)));
        profile2 = profileService.get(2);
        assertTrue(profile2.getName().equals("Profile 2"));
        assertTrue(!profile2.isPrintEnabled());
        assertTrue(!profile2.isFilerEnabled());
        assertTrue(!profile2.isGreenscreenEnabled());
        assertTrue(!profile2.isMobilEnabled());
        assertTrue(profile2.getWatermark().isEmpty());
        assertTrue(profile2.getPairCameraPositions().get(0).getCamera().getId()==2);
        assertTrue(profile2.getPairCameraPositions().get(0).getPosition().getId()==1);
        assertTrue(!profile2.getPairCameraPositions().get(0).isGreenScreenReady());
        assertTrue(profile2.getPairCameraPositions().get(1).getCamera().getId()==1);
        assertTrue(profile2.getPairCameraPositions().get(1).getPosition().getId()==2);
        assertTrue(!profile2.getPairCameraPositions().get(1).isGreenScreenReady());
        assertTrue(profile2.getPairLogoRelativeRectangles().get(0).getLogo().getId()==1);
        assertTrue(profile2.getPairLogoRelativeRectangles().get(0).getRelativeRectangle().equals(new RelativeRectangle(75,95,80,90)));
        assertTrue(profile2.getPairLogoRelativeRectangles().get(1).getLogo().getId()==2);
        assertTrue(profile2.getPairLogoRelativeRectangles().get(1).getRelativeRectangle().equals(new RelativeRectangle(15,20,25,30)));
    }

    @Test
    public void test_read_NotExisting() throws Exception{
        Assert.assertTrue(profileService.get(1000000) == null);
    }


    @Test
    public void editProfileWithValidArgument() throws ServiceException {
        profileB.setPairCameraPositions(pairCameraPositions);
        profileB.setPairLogoRelativeRectangles(pairLogoRelativeRectangles);
        profileB.setBackgroundCategories(categories);
        profileB = profileService.add(profileB);
        assertTrue(this.profileB != null);
        assertTrue(profileB.getName().equals("Profile B"));
        assertTrue(profileB.isPrintEnabled());
        assertTrue(profileB.isFilerEnabled());
        assertTrue(profileB.isGreenscreenEnabled());
        assertTrue(profileB.isMobilEnabled());
        assertTrue(profileB.getWatermark().equals("/dev/null/watermarkB.jpg"));
        assertTrue(!profileB.getPairCameraPositions().isEmpty());
        assertTrue(profileB.getPairCameraPositions().size() == 2);
        assertTrue(profileB.getPairCameraPositions().get(0).getCamera().getId()==1);
        assertTrue(profileB.getPairCameraPositions().get(0).getPosition().getId()==1);
        assertTrue(profileB.getPairCameraPositions().get(1).getCamera().getId()==2);
        assertTrue(profileB.getPairCameraPositions().get(1).getPosition().getId()==2);
        assertTrue(!profileB.getPairLogoRelativeRectangles().isEmpty());
        assertTrue(profileB.getPairLogoRelativeRectangles().size() == 2);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(0).getLogo().getId()==1);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(0).getRelativeRectangle().equals(relativeRectangleA));
        assertTrue(profileB.getPairLogoRelativeRectangles().get(1).getLogo().getId()==2);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(1).getRelativeRectangle().equals(relativeRectangleB));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory1));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory2));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory3));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory4));

        //Modifications of the profile
        profileB.setName("Profile B+++");
        profileB.setPrintEnabled(false);
        profileB.setFilerEnabled(false);
        profileB.setGreenscreenEnabled(false);
        profileB.setMobilEnabled(false);
        profileB.setWatermark("/dev/null/watermarkBBB.jpg");
        categories = profileB.getBackgroundCategories();
        categories.remove(backgroundCategory3);
        profileB.setBackgroundCategories(categories);
        profileService.edit(profileB);
        profileB = profileService.get(profileB.getId());
        assertTrue(this.profileB != null);
        assertTrue(profileB.getName().equals("Profile B+++"));
        assertTrue(!profileB.isPrintEnabled());
        assertTrue(!profileB.isFilerEnabled());
        assertTrue(!profileB.isGreenscreenEnabled());
        assertTrue(!profileB.isMobilEnabled());
        assertTrue(profileB.getWatermark().equals("/dev/null/watermarkBBB.jpg"));
        assertTrue(!profileB.getPairCameraPositions().isEmpty());
        assertTrue(profileB.getPairCameraPositions().size() == 2);
        assertTrue(profileB.getPairCameraPositions().get(0).getCamera().getId()==1);
        assertTrue(profileB.getPairCameraPositions().get(0).getPosition().getId()==1);
        assertTrue(profileB.getPairCameraPositions().get(1).getCamera().getId()==2);
        assertTrue(profileB.getPairCameraPositions().get(1).getPosition().getId()==2);
        assertTrue(!profileB.getPairLogoRelativeRectangles().isEmpty());
        assertTrue(profileB.getPairLogoRelativeRectangles().size() == 2);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(0).getLogo().getId()==1);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(0).getRelativeRectangle().equals(relativeRectangleA));
        assertTrue(profileB.getPairLogoRelativeRectangles().get(1).getLogo().getId()==2);
        assertTrue(profileB.getPairLogoRelativeRectangles().get(1).getRelativeRectangle().equals(relativeRectangleB));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory1));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory2));
        assertTrue(!profileB.getBackgroundCategories().contains(backgroundCategory3));
        assertTrue(profileB.getBackgroundCategories().contains(backgroundCategory4));

    }


    @Test
    public void editProfileWithValidValidArgumentButDoesNotExistInPersistenceStore() throws ServiceException{
        Profile profile = new Profile("Testprofile");
        assertTrue(profile.getId() == Integer.MIN_VALUE);
        assertTrue(profile.getName().equals("Testprofile"));
        profileService.add(profile);
        assertTrue(profile.getId()>=1);
        assertTrue(profile.getName().equals("Testprofile"));
        profile.setName("Testprofile changed");
        profile.setId(profile.getId()+1);
        profileService.edit(profile);
        int id = profile.getId();
        profile = profileService.get(id-1);
        assertTrue(!profile.getName().equals("Testprofile changed"));
    }

    @Test
    public void readProfileWithValidArgumentThatDoesNotExistInPersistenceStore() throws ServiceException{
        Profile profile = new Profile("Testprofile");
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

    @Test
    public void testGetActiveProfile() throws ServiceException {
        Profile profile1 = profileService.getActiveProfile();
        assertTrue(profile1.getName().equals("Profile 1"));
        assertTrue(!profile1.isPrintEnabled());
        assertTrue(!profile1.isFilerEnabled());
        assertTrue(!profile1.isGreenscreenEnabled());
        assertTrue(!profile1.isMobilEnabled());
        assertTrue(profile1.getWatermark().isEmpty());
    }
}
