package org.openbooth.dao;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.dao.impl.TestEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbooth.entities.Profile;
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
public class ProfileDAOTest extends TestEnvironment {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileDAOTest.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * TESTING method: Profile create(Profile profile)
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_create_withNullArguments_Fail() throws Exception {
        mockProfileDAO.create(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_create_withPersistenceTroubles_Fail() throws Exception {
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
        mockProfileDAO.create(this.profileA);
    }

    @Test
    public void test_create_withValidInputArguments() throws Exception {
        assertTrue(profileA.getId() == Integer.MIN_VALUE);
        assertTrue(profileA.getName().equals("Profile A"));
        assertTrue(profileA.getPairCameraPositions().size() == 0);
        assertTrue(profileA.getPairLogoRelativeRectangles().size() == 0);
        assertTrue(!profileA.isPrintEnabled());
        assertTrue(!profileA.isFilerEnabled());
        assertTrue(!profileA.isGreenscreenEnabled());
        assertTrue(!profileA.isMobilEnabled());
        assertTrue(profileA.getWatermark().isEmpty());
        assertTrue(!profileA.isDeleted());
        profileA = profileDAO.create(profileA);
        assertTrue(profileA.getId() >= 1);
        assertTrue(profileA.getName().equals("Profile A"));
        assertTrue(profileA.getPairCameraPositions().size() == 0);
        assertTrue(profileA.getPairLogoRelativeRectangles().size() == 0);
        assertTrue(!profileA.isPrintEnabled());
        assertTrue(!profileA.isFilerEnabled());
        assertTrue(!profileA.isGreenscreenEnabled());
        assertTrue(!profileA.isMobilEnabled());
        assertTrue(profileA.getWatermark().isEmpty());
        assertTrue(!profileA.isDeleted());

        assertTrue(profileB.getId() == Integer.MIN_VALUE);
        assertTrue(profileB.getName().equals("Profile B"));
        assertTrue(profileB.getPairCameraPositions().size() == 0);
        assertTrue(profileB.getPairLogoRelativeRectangles().size() == 0);
        assertTrue(profileB.isPrintEnabled());
        assertTrue(profileB.isFilerEnabled());
        assertTrue(profileB.isGreenscreenEnabled());
        assertTrue(profileB.isMobilEnabled());
        assertTrue(profileB.getWatermark().equals("/dev/null/watermarkB.jpg"));
        assertTrue(!profileB.isDeleted());
        profileB = profileDAO.create(profileB);
        assertTrue(profileB.getId() >= 1);
        assertTrue(profileB.getName().equals("Profile B"));
        assertTrue(profileB.getPairCameraPositions().size() == 0);
        assertTrue(profileB.getPairLogoRelativeRectangles().size() == 0);
        assertTrue(profileB.isPrintEnabled());
        assertTrue(profileB.isFilerEnabled());
        assertTrue(profileB.isGreenscreenEnabled());
        assertTrue(profileB.isMobilEnabled());
        assertTrue(profileB.getWatermark().equals("/dev/null/watermarkB.jpg"));
        assertTrue(!profileB.isDeleted());

        assertTrue(profileC.getId() == 20);
        assertTrue(profileC.getName().equals("Profile C"));
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
        profileC = profileDAO.create(profileC);
        assertTrue(profileC.getId() == 20);
        assertTrue(profileC.getName().equals("Profile C"));
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
    }

    @Test(expected = PersistenceException.class)
    public void test_create_withAlreadyExistingInputparameter_fail() throws Exception {
        Profile returnValue = profileDAO.create(profileA);
        Profile returnValue2 = profileDAO.create(returnValue);
    }

    /**
     * TESTING method: boolean update(Profile profile)
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_update_withNullArguments_Fail() throws Exception {
        mockProfileDAO.update(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_update_withPersistenceTroubles_Fail() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockProfileDAO.update(this.profileA);
    }

    @Test
    public void test_update_withValidInputParameter() throws Exception {
        profileC = profileDAO.create(profileC);
        assertTrue(profileC.getId() == 20);
        assertTrue(profileC.getName().equals("Profile C"));
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
        profileC.setName("Profile C+");
        cameraA = cameraDAO.create(cameraA);
        positionA = positionDAO.create(positionA);
        pairCameraPositions.add(pairCameraPositionC);
        profileC.setPairCameraPositions(pairCameraPositions);
        logoA = logoDAO.create(logoA);
        pairLogoRelativeRectangles.add(pairLogoRelativeRectangleC);
        profileC.setPairLogoRelativeRectangles(pairLogoRelativeRectangles);
        profileC.setPrintEnabled(false);
        profileC.setFilerEnabled(false);
        profileC.setGreenscreenEnabled(false);
        profileC.setMobilEnabled(false);
        profileC.setWatermark("");
        assertTrue(profileDAO.update(profileC));
        profileC = profileDAO.read(profileC.getId());
        pairCameraPositions = profileC.getPairCameraPositions();
        pairLogoRelativeRectangles = profileC.getPairLogoRelativeRectangles();
        assertTrue(pairCameraPositions.size() == 3);
        assertTrue(pairCameraPositions.get(0).isGreenScreenReady());
        assertTrue(pairCameraPositions.get(0).getCamera().equals(camera1));
        assertTrue(pairCameraPositions.get(0).getPosition().equals(position1));
        assertTrue(!pairCameraPositions.get(1).isGreenScreenReady());
        assertTrue(pairCameraPositions.get(1).getCamera().equals(camera2));
        assertTrue(pairCameraPositions.get(1).getPosition().equals(position2));
        assertTrue(pairCameraPositions.get(2).isGreenScreenReady());
        assertTrue(pairCameraPositions.get(2).getCamera().equals(camera3));
        assertTrue(pairCameraPositions.get(2).getPosition().equals(positionA));
        assertTrue(pairLogoRelativeRectangles.size() == 3);
        assertTrue(pairLogoRelativeRectangles.get(0).getLogo().equals(logo1));
        assertTrue(pairLogoRelativeRectangles.get(0).getRelativeRectangle().equals(relativeRectangleA));
        assertTrue(pairLogoRelativeRectangles.get(1).getLogo().equals(logo2));
        assertTrue(pairLogoRelativeRectangles.get(1).getRelativeRectangle().equals(relativeRectangleB));
        assertTrue(pairLogoRelativeRectangles.get(2).getLogo().equals(logoA));
        assertTrue(pairLogoRelativeRectangles.get(2).getRelativeRectangle().equals(relativeRectangleC));
        pairCameraPositions.remove(pairCameraPositionA);
        pairLogoRelativeRectangles.remove(pairLogoRelativeRectangleA);
        assertTrue(profileDAO.update(profileC));
        profileC = profileDAO.read(profileC.getId());
        pairCameraPositions = profileC.getPairCameraPositions();
        pairLogoRelativeRectangles = profileC.getPairLogoRelativeRectangles();
        assertTrue(pairCameraPositions.size() == 2);
        assertTrue(pairCameraPositions.get(0).equals(pairCameraPositionB));
        assertTrue(pairCameraPositions.get(1).equals(pairCameraPositionC));
        assertTrue(!pairCameraPositions.contains(pairCameraPositionA));
        assertTrue(pairLogoRelativeRectangles.size() == 2);
        assertTrue(pairLogoRelativeRectangles.get(0).equals(pairLogoRelativeRectangleB));
        assertTrue(pairLogoRelativeRectangles.get(1).equals(pairLogoRelativeRectangleC));
        assertTrue(!pairLogoRelativeRectangles.contains(pairLogoRelativeRectangleA));
    }

    @Test
    public void test_update_WithNotExisting() throws Exception {
        Profile returnValue = profileDAO.create(profileC);
        returnValue.setId(returnValue.getId() + 1);
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
     * TESTING method: Profile read(int id)
     * throws PersistenceException;
     */

    @Test
    public void test_read_withValidInt() throws PersistenceException {
        Profile returnValue = profileDAO.create(profileC);
        Profile returnValue2 = profileDAO.read(returnValue.getId());
        assertTrue(returnValue2.getName().equals("Profile C"));
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
    public void test_read_NotExisting() throws Exception {
        Profile returnValue = profileDAO.create(profileC);
        assertTrue(profileDAO.read(returnValue.getId() + 1) == null);
    }

    /**
     * TESTING method: List<Profile> readAll()
     * throws PersistenceException;
     */

    public void test_readAll_withNonEmptyReturnList() throws Exception {
        Profile returnValue1 = profileDAO.create(profileA);
        Profile returnValue2 = profileDAO.create(profileB);
        Profile returnValue3 = profileDAO.create(profileC);
        List<Profile> returnList = profileDAO.readAll();
        assertTrue(returnList.contains(returnValue1)
                && returnList.contains(returnValue2)
                && returnList.contains(returnValue3));
    }

    /**
     * TESTING method: boolean delete(Profile profile)
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_delete_withNullArguments_Fail() throws Exception {
        mockProfileDAO.delete(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_delete_withPersistenceTroubles_Fail() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockProfileDAO.delete(this.profileA);
    }

    @Test
    public void test_delete_withValidInputArguments() throws Exception {
        Profile returnValue1 = profileDAO.create(profileA);
        Profile returnValue2 = profileDAO.create(profileB);
        Profile returnValue3 = profileDAO.create(profileC);
        assertTrue(profileDAO.delete(returnValue1));
        assertTrue(profileDAO.read(returnValue1.getId()) == null);
        assertTrue(profileDAO.delete(returnValue2));
        assertTrue(profileDAO.read(returnValue2.getId()) == null);
        assertTrue(profileDAO.delete(returnValue3));
        assertTrue(profileDAO.read(returnValue3.getId()) == null);
    }

    @Test
    public void test_delete_withNotExistingInputparameter_fail() throws Exception {
        Profile returnValue = profileDAO.create(profileA);
        assertTrue(profileDAO.delete(returnValue));
        assertFalse(profileDAO.delete(returnValue));
    }

}

