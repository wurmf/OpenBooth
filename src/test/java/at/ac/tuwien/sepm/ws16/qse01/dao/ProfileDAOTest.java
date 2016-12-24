package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.TestEnvironment;
import at.ac.tuwien.sepm.ws16.qse01.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    private Profile profileB;
    private Profile profileC;
    private List<Profile.PairCameraPosition> pairCameraPositions;
    private List<Profile.PairLogoRelativeRectangle> pairLogoRelativeRectangles;
    private Camera camera1;
    private Camera camera2;
    private Position position1;
    private Position position2;
    private Logo logo1;
    private Logo logo2;
    private RelativeRectangle relativeRectangleA;
    private RelativeRectangle relativeRectangleB;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        profileA = new Profile("Profile A");
        profileB = new Profile("Profile B",
                true,
                true,
                true,
                true,
                "/dev/null/watermarkB.jpg");

        camera1 = cameraDAO.read(1);
        camera2 = cameraDAO.read(2);
        position1 = positionDAO.read(1);
        position2 = positionDAO.read(2);
        logo1 = logoDAO.read(1);
        logo2 = logoDAO.read(2);
        relativeRectangleA = new RelativeRectangle(10.1,10.2, 30.3,30.4);
        relativeRectangleB = new RelativeRectangle(80.1,80.2,10.3,10.4);
        pairCameraPositions = new ArrayList<>();
        pairCameraPositions.add(new Profile.PairCameraPosition(camera1, position1,true));
        pairCameraPositions.add(new Profile.PairCameraPosition(camera2, position2, false));
        pairLogoRelativeRectangles = new ArrayList<>();
        pairLogoRelativeRectangles.add(new Profile.PairLogoRelativeRectangle(logo1,relativeRectangleA));
        pairLogoRelativeRectangles.add(new Profile.PairLogoRelativeRectangle(logo2,relativeRectangleB));

        profileC = new Profile(20,
                "Profile C",
                pairCameraPositions,
                pairLogoRelativeRectangles,
                true,
                true,
                true,
                true,
                "/dev/null/watermarkC.jpg",
                false);
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

    @Test
    public void test_create_withValidInputArguments() throws Exception {
        assertTrue(profileA.getId() == Integer.MIN_VALUE);
        assertTrue(profileA.getName() == "Profile A");
        assertTrue(profileA.getPairCameraPositions().size() == 0);
        assertTrue(profileA.getPairLogoRelativeRectangles().size() == 0);
        assertTrue(!profileA.isPrintEnabled());
        assertTrue(!profileA.isFilerEnabled());
        assertTrue(!profileA.isGreenscreenEnabled());
        assertTrue(!profileA.isMobilEnabled());
        assertTrue(profileA.getWatermark().isEmpty());
        assertTrue(!profileA.isDeleted());
        Profile returnValue = profileDAO.create(profileA);
        assertTrue(returnValue.getId()>=1);
        assertTrue(returnValue.getName() == "Profile A");
        assertTrue(profileA.getPairCameraPositions().size() == 0);
        assertTrue(profileA.getPairLogoRelativeRectangles().size() == 0);
        assertTrue(!profileA.isPrintEnabled());
        assertTrue(!profileA.isFilerEnabled());
        assertTrue(!profileA.isGreenscreenEnabled());
        assertTrue(!profileA.isMobilEnabled());
        assertTrue(profileA.getWatermark().isEmpty());
        assertTrue(!returnValue.isDeleted());

        assertTrue(profileB.getId() == Integer.MIN_VALUE);
        assertTrue(profileB.getName() == "Profile B");
        assertTrue(profileB.getPairCameraPositions().size() == 0);
        assertTrue(profileB.getPairLogoRelativeRectangles().size() == 0);
        assertTrue(profileB.isPrintEnabled());
        assertTrue(profileB.isFilerEnabled());
        assertTrue(profileB.isGreenscreenEnabled());
        assertTrue(profileB.isMobilEnabled());
        assertTrue(profileB.getWatermark().equals("/dev/null/watermarkB.jpg"));
        assertTrue(!profileA.isDeleted());
        Profile returnValue2 = profileDAO.create(profileB);
        assertTrue(returnValue2.getId()>=1);
        assertTrue(returnValue2.getName() == "Profile B");
        assertTrue(returnValue2.getPairCameraPositions().size() == 0);
        assertTrue(returnValue2.getPairLogoRelativeRectangles().size() == 0);
        assertTrue(returnValue2.isPrintEnabled());
        assertTrue(returnValue2.isFilerEnabled());
        assertTrue(returnValue2.isGreenscreenEnabled());
        assertTrue(returnValue2.isMobilEnabled());
        assertTrue(returnValue2.getWatermark().equals("/dev/null/watermarkB.jpg"));
        assertTrue(!returnValue2.isDeleted());

        assertTrue(profileC.getId() == 20);
        assertTrue(profileC.getName() == "Profile C");
        assertTrue(profileC.getPairCameraPositions().size() == 2);
        assertTrue(profileC.getPairLogoRelativeRectangles().size() == 2);
        assertTrue(profileC.isPrintEnabled());
        assertTrue(profileC.isFilerEnabled());
        assertTrue(profileC.isGreenscreenEnabled());
        assertTrue(profileC.isMobilEnabled());
        assertTrue(profileC.getWatermark().equals("/dev/null/watermarkC.jpg"));
        assertTrue(!profileC.isDeleted());
        Profile returnValue3 = profileDAO.create(profileC);
        assertTrue(returnValue3.getId() == 20);
        assertTrue(returnValue3.getName() == "Profile C");
        assertTrue(returnValue3.getPairCameraPositions().size() == 2);
        assertTrue(returnValue3.getPairLogoRelativeRectangles().size() == 2);
        assertTrue(returnValue3.isPrintEnabled());
        assertTrue(returnValue3.isFilerEnabled());
        assertTrue(returnValue3.isGreenscreenEnabled());
        assertTrue(returnValue3.isMobilEnabled());
        assertTrue(returnValue3.getWatermark().equals("/dev/null/watermarkC.jpg"));
        assertTrue(!returnValue3.isDeleted());

    }

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

