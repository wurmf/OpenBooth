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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
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
    private Profile.PairCameraPosition pairCameraPositionA;
    private Profile.PairCameraPosition pairCameraPositionB;
    private List<Profile.PairLogoRelativeRectangle> pairLogoRelativeRectangles;
    private Profile.PairLogoRelativeRectangle pairLogoRelativeRectangleA;
    private Profile.PairLogoRelativeRectangle pairLogoRelativeRectangleB;
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
        pairCameraPositionA = new Profile.PairCameraPosition(camera1, position1,true);
        pairCameraPositionB = new Profile.PairCameraPosition(camera2, position2, false);
        pairCameraPositions.add(pairCameraPositionA);
        pairCameraPositions.add(pairCameraPositionB);
        pairLogoRelativeRectangles = new ArrayList<>();
        pairLogoRelativeRectangleA = new Profile.PairLogoRelativeRectangle(logo1,relativeRectangleA);
        pairLogoRelativeRectangleB = new Profile.PairLogoRelativeRectangle(logo2,relativeRectangleB);
        pairLogoRelativeRectangles.add(pairLogoRelativeRectangleA);
        pairLogoRelativeRectangles.add(pairLogoRelativeRectangleB);

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
        assertTrue(profileC.getPairCameraPositions().contains(pairCameraPositionA));
        assertTrue(profileC.getPairCameraPositions().contains(pairCameraPositionB));
        assertTrue(profileC.getPairLogoRelativeRectangles().size() == 2);
        assertTrue(profileC.getPairLogoRelativeRectangles().contains(pairLogoRelativeRectangleA));
        assertTrue(profileC.getPairLogoRelativeRectangles().contains(pairLogoRelativeRectangleB));
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
        assertTrue(profileC.getPairCameraPositions().contains(pairCameraPositionA));
        assertTrue(profileC.getPairCameraPositions().contains(pairCameraPositionB));
        assertTrue(returnValue3.getPairLogoRelativeRectangles().size() == 2);
        assertTrue(profileC.getPairLogoRelativeRectangles().contains(pairLogoRelativeRectangleA));
        assertTrue(profileC.getPairLogoRelativeRectangles().contains(pairLogoRelativeRectangleB));
        assertTrue(returnValue3.isPrintEnabled());
        assertTrue(returnValue3.isFilerEnabled());
        assertTrue(returnValue3.isGreenscreenEnabled());
        assertTrue(returnValue3.isMobilEnabled());
        assertTrue(returnValue3.getWatermark().equals("/dev/null/watermarkC.jpg"));
        assertTrue(!returnValue3.isDeleted());
    }

    @Test(expected = PersistenceException.class)
    public void test_create_withAlreadyExistingInputparameter_fail() throws Exception{
        Profile returnValue = profileDAO.create(profileA);
        Profile returnValue2 = profileDAO.create(returnValue);
    }

    /**
     *
     *TESTING method: boolean update(Profile profile)
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_update_withNullArguments_Fail() throws Exception{
        mockProfileDAO.update(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_update_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockProfileDAO.update(this.profileA);
    }

    @Test
    public void test_update_withValidInputParameter()throws Exception{
        assertTrue(profileC.getId() == 20);
        assertTrue(profileC.getName() == "Profile C");
        assertTrue(profileC.getPairCameraPositions().size() == 2);
        assertTrue(profileC.getPairCameraPositions().contains(pairCameraPositionA));
        assertTrue(profileC.getPairCameraPositions().contains(pairCameraPositionB));
        assertTrue(profileC.getPairLogoRelativeRectangles().size() == 2);
        assertTrue(profileC.getPairLogoRelativeRectangles().contains(pairLogoRelativeRectangleA));
        assertTrue(profileC.getPairLogoRelativeRectangles().contains(pairLogoRelativeRectangleB));
        assertTrue(profileC.isPrintEnabled());
        assertTrue(profileC.isFilerEnabled());
        assertTrue(profileC.isGreenscreenEnabled());
        assertTrue(profileC.isMobilEnabled());
        assertTrue(profileC.getWatermark().equals("/dev/null/watermarkC.jpg"));
        assertTrue(!profileC.isDeleted());
        Profile returnValue = profileDAO.create(profileC);
        assertTrue(returnValue.getId() == 20);
        assertTrue(returnValue.getName() == "Profile C");
        assertTrue(returnValue.getPairCameraPositions().size() == 2);
        assertTrue(returnValue.getPairCameraPositions().contains(pairCameraPositionA));
        assertTrue(returnValue.getPairCameraPositions().contains(pairCameraPositionB));
        assertTrue(returnValue.getPairLogoRelativeRectangles().size() == 2);
        assertTrue(returnValue.getPairLogoRelativeRectangles().contains(pairLogoRelativeRectangleA));
        assertTrue(returnValue.getPairLogoRelativeRectangles().contains(pairLogoRelativeRectangleB));
        assertTrue(returnValue.isPrintEnabled());
        assertTrue(returnValue.isFilerEnabled());
        assertTrue(returnValue.isGreenscreenEnabled());
        assertTrue(returnValue.isMobilEnabled());
        assertTrue(returnValue.getWatermark().equals("/dev/null/watermarkC.jpg"));
        assertTrue(!returnValue.isDeleted());
        returnValue.setName("Profile C+");
        returnValue.setPairCameraPositions(new ArrayList<>());
        returnValue.setPairLogoRelativeRectangles(new ArrayList<>());
        returnValue.setPrintEnabled(false);
        returnValue.setFilerEnabled(false);
        returnValue.setGreenscreenEnabled(false);
        returnValue.setMobilEnabled(false);
        returnValue.setWatermark("");
        assertTrue(profileDAO.update(returnValue));
    }

    @Test
    public void test_update_WithNotExisting()throws Exception{
        Profile returnValue = profileDAO.create(profileC);
        returnValue.setId(returnValue.getId()+1);
        returnValue.setName("Profile C+");
        returnValue.setPairCameraPositions(new ArrayList<>());
        returnValue.setPairLogoRelativeRectangles(new ArrayList<>());
        returnValue.setPrintEnabled(false);
        returnValue.setFilerEnabled(false);
        returnValue.setGreenscreenEnabled(false);
        returnValue.setMobilEnabled(false);
        returnValue.setWatermark("");
        assertTrue(!profileDAO.update(returnValue));
    }

    /**
     *
     *TESTING method: Profile read(int id)
     * throws PersistenceException;
     */

    @Test
    public void test_read_withValidInt() throws PersistenceException{
        Profile returnValue = profileDAO.create(profileC);
        Profile returnValue2 = profileDAO.read(returnValue.getId());
        assertTrue(returnValue2.getName() == "Profile C");
        assertTrue(returnValue2.getPairCameraPositions().size() == 2);
        assertTrue(returnValue2.getPairCameraPositions().contains(pairCameraPositionA));
        assertTrue(returnValue2.getPairCameraPositions().contains(pairCameraPositionB));
        assertTrue(returnValue2.getPairLogoRelativeRectangles().size() == 2);
        assertTrue(returnValue2.getPairLogoRelativeRectangles().contains(pairLogoRelativeRectangleA));
        assertTrue(returnValue2.getPairLogoRelativeRectangles().contains(pairLogoRelativeRectangleB));
        assertTrue(returnValue2.isPrintEnabled());
        assertTrue(returnValue2.isFilerEnabled());
        assertTrue(returnValue2.isGreenscreenEnabled());
        assertTrue(returnValue2.isMobilEnabled());
        assertTrue(returnValue2.getWatermark().equals("/dev/null/watermarkC.jpg"));
        assertTrue(!returnValue2.isDeleted());
    }

    @Test
    public void test_read_NotExisting() throws Exception{
        Profile returnValue = profileDAO.create(profileC);
        assertTrue(profileDAO.read(returnValue.getId() + 1) == null);
    }

    /**
     *
     *TESTING method: List<Profile> readAll()
     * throws PersistenceException;
     */

    public void test_readAll_withNonEmptyReturnList() throws Exception{
        Profile returnValue1 = profileDAO.create(profileA);
        Profile returnValue2 = profileDAO.create(profileB);
        Profile returnValue3 = profileDAO.create(profileC);
        List<Profile> returnList = profileDAO.readAll();
        assertTrue(returnList.contains(returnValue1)
                && returnList.contains(returnValue2)
                && returnList.contains(returnValue3));
    }

    /**
     *
     *TESTING method: boolean delete(Profile profile)
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_delete_withNullArguments_Fail() throws Exception{
        mockProfileDAO.delete(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_delete_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockProfileDAO.delete(this.profileA);
    }

    @Test
    public void test_delete_withValidInputArguments() throws Exception {
        Profile returnValue1 = profileDAO.create(profileA);
        Profile returnValue2 = profileDAO.create(profileB);
        Profile returnValue3 = profileDAO.create(profileC);
        assertTrue(profileDAO.delete(returnValue1));
        assertTrue(profileDAO.read(returnValue1.getId())== null);
        assertTrue(profileDAO.delete(returnValue2));
        assertTrue(profileDAO.read(returnValue2.getId())== null);
        assertTrue(profileDAO.delete(returnValue3));
        assertTrue(profileDAO.read(returnValue3.getId())== null);
    }

    @Test
    public void test_delete_withNotExistingInputparameter_fail() throws Exception{
        Profile returnValue = profileDAO.create(profileA);
        assertTrue(profileDAO.delete(returnValue));
        assertFalse(profileDAO.delete(returnValue));
    }

}

