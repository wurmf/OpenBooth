package org.openbooth.dao;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.dao.impl.TestEnvironment;
import org.openbooth.entities.Profile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * PairCameraPosition Tester
 */
public class PairCameraPositionDAOTest extends TestEnvironment {
    private static final Logger LOGGER = LoggerFactory.getLogger(PairCameraPositionDAOTest.class);

    private int id;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        pairCameraPositionA.setProfileId(3);
        pairCameraPositionB.setProfileId(3);
        pairCameraPositionC.setProfileId(4);
        cameraDAO.create(cameraA);
        positionDAO.create(positionA);
        id = 1;
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     *
     *TESTING method:
     * Profile.PairCameraPosition create(Profile.PairCameraPosition pairCameraPosition)
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_create_withNullArguments_Fail() throws Exception{
        mockPairCameraPositionDAO.create(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_create_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockPairCameraPositionDAO.create(this.pairCameraPositionA);
    }

    @Test
    public void test_create_withValidInputArguments() throws Exception {
        assertTrue(pairCameraPositionA.getId() == Integer.MIN_VALUE);
        assertTrue(pairCameraPositionA.getProfileId()== 3 );
        assertTrue(pairCameraPositionA.getCamera().equals(camera1));
        assertTrue(pairCameraPositionA.getPosition().equals(position1));
        assertTrue(pairCameraPositionA.isGreenScreenReady());
        pairCameraPositionA.setProfileId(3);
        Profile.PairCameraPosition returnValue = pairCameraPositionDAO.create(pairCameraPositionA);
        assertTrue(returnValue.getId()>=1);
        assertTrue(pairCameraPositionA.getProfileId() == 3);
        assertTrue(pairCameraPositionA.getCamera().equals(camera1));
        assertTrue(pairCameraPositionA.getPosition().equals(position1));
        assertTrue(pairCameraPositionA.isGreenScreenReady());
    }

    @Test
    public void testmock_create_withValidInputArguments() throws Exception {
        when(mockResultSet.next()).thenReturn(Boolean.TRUE);
        when(mockResultSet.getInt(1)).thenReturn(id);
        Profile.PairCameraPosition returnValue
                = mockPairCameraPositionDAO.create(this.pairCameraPositionA);
        verify(mockPreparedStatement).executeUpdate();
        assertTrue(returnValue.getId() == 1);
        assertTrue(returnValue.getProfileId() == 3);
        assertTrue(pairCameraPositionA.getCamera().equals(camera1));
        assertTrue(pairCameraPositionA.getPosition().equals(position1));
        assertTrue(pairCameraPositionA.isGreenScreenReady());
    }

    @Test(expected = PersistenceException.class)
    public void test_create_withAlreadyExistingInputparameter_fail() throws Exception{
        assertTrue(pairCameraPosition1000000.getId() == 1000000);
        assertTrue(pairCameraPosition1000000.getProfileId() == 3);
        assertTrue(pairCameraPosition1000000.getCamera().equals(camera1));
        assertTrue(pairCameraPosition1000000.getPosition().equals(position1));
        assertTrue(!pairCameraPosition1000000.isGreenScreenReady());
        Profile.PairCameraPosition returnValue
                = pairCameraPositionDAO.create(pairCameraPosition1000000);
        assertTrue(returnValue.getId() == 1000000);
        assertTrue(returnValue.getProfileId() == 3);
        assertTrue(pairCameraPosition1000000.getCamera().equals(camera1));
        assertTrue(pairCameraPosition1000000.getPosition().equals(position1));
        assertTrue(!pairCameraPosition1000000.isGreenScreenReady());
        Profile.PairCameraPosition returnValue2
                = pairCameraPositionDAO.create(pairCameraPosition1000000);
    }

    /**
     * TESTING method:
     * boolean update(Profile.PairCameraPosition pairCameraPosition)throws PersistenceException;
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_update_withNullArguments_Fail() throws Exception{
        mockPairCameraPositionDAO.update(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_update_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockPairCameraPositionDAO.update(this.pairCameraPositionA);
    }

    @Test
    public void test_update_withValidInputParameter()throws Exception{
        assertTrue(pairCameraPositionA.getId() == Integer.MIN_VALUE);
        assertTrue(pairCameraPositionA.getProfileId() == 3);
        assertTrue(pairCameraPositionA.getCamera().equals(camera1));
        assertTrue(pairCameraPositionA.getPosition().equals(position1));
        assertTrue(pairCameraPositionA.isGreenScreenReady());
        Profile.PairCameraPosition returnValue = pairCameraPositionDAO.create(pairCameraPositionA);
        assertTrue(returnValue.getId()>=1);
        assertTrue(pairCameraPositionA.getProfileId() == 3);
        assertTrue(pairCameraPositionA.getCamera().equals(camera1));
        assertTrue(pairCameraPositionA.getPosition().equals(position1));
        assertTrue(pairCameraPositionA.isGreenScreenReady());
        returnValue.setProfileId(4);
        returnValue.setCamera(camera2);
        returnValue.setPosition(position2);
        returnValue.setGreenScreenReady(false);
        boolean returnBoolean = pairCameraPositionDAO.update(returnValue);
        assertTrue(returnBoolean);
    }

    @Test
    public void test_update_WithNotExisting()throws Exception{
        assertTrue(pairCameraPositionA.getId() == Integer.MIN_VALUE);
        assertTrue(pairCameraPositionA.getProfileId() == 3);
        assertTrue(pairCameraPositionA.getCamera().equals(camera1));
        assertTrue(pairCameraPositionA.getPosition().equals(position1));
        assertTrue(pairCameraPositionA.isGreenScreenReady());
        Profile.PairCameraPosition returnValue = pairCameraPositionDAO.create(pairCameraPositionA);
        assertTrue(returnValue.getId()>=1);
        assertTrue(pairCameraPositionA.getProfileId() == 3);
        assertTrue(pairCameraPositionA.getCamera().equals(camera1));
        assertTrue(pairCameraPositionA.getPosition().equals(position1));
        assertTrue(pairCameraPositionA.isGreenScreenReady());
        returnValue.setId(returnValue.getId()+1);
        returnValue.setProfileId(4);
        returnValue.setCamera(camera1);
        returnValue.setPosition(position1);
        returnValue.setGreenScreenReady(false);
        boolean returnBoolean = pairCameraPositionDAO.update(returnValue);
        assertTrue(!returnBoolean);
    }

    /**
     * TESTING method:
     * Profile.PairCameraPosition read(int id) throws PersistenceException;
     */

    @Test
    public void test_read_withValidInt() throws Exception{
        Profile.PairCameraPosition returnValue1
                = pairCameraPositionDAO.create(pairCameraPositionA);
        Profile.PairCameraPosition returnValue2
                = pairCameraPositionDAO.create(pairCameraPositionB);
        Profile.PairCameraPosition returnValue3
                = pairCameraPositionDAO.create(pairCameraPositionC);
        assertTrue(pairCameraPositionDAO.read(returnValue1.getId()).equals(returnValue1));
        assertTrue(pairCameraPositionDAO.read(returnValue2.getId()).equals(returnValue2));
        assertTrue(pairCameraPositionDAO.read(returnValue3.getId()).equals(returnValue3));
    }

    @Test
    public void test_read_NotExisting() throws Exception{
        Profile.PairCameraPosition returnValue1
                = pairCameraPositionDAO.create(pairCameraPositionA);
        assertTrue(pairCameraPositionDAO.read(returnValue1.getId() + 1) == null);
    }

    /**
     * TESTING method:
     * List<Profile.PairCameraPosition> readAllWithProfileID(int profileId)
     * throws PersistenceException;
     */

    @Test
    public void test_readAll_withNonEmptyReturnList() throws Exception{
        Profile.PairCameraPosition returnValue1
                = pairCameraPositionDAO.create(pairCameraPositionA);
        Profile.PairCameraPosition returnValue2
                = pairCameraPositionDAO.create(pairCameraPositionB);
        List<Profile.PairCameraPosition> returnList
                = pairCameraPositionDAO.readAllWithProfileID(3);
        assertTrue(returnList.contains(returnValue1)
                && returnList.contains(returnValue2)
                );
    }

    @Test
    public void test_readAll_withEmptyReturnList() throws Exception{
        assertTrue(pairCameraPositionDAO.readAllWithProfileID(10).size() == 0);
    }

    /**
     * TESTING method:
     * boolean delete(Profile.PairCameraPosition pairCameraPosition)
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_delete_withNullArguments_Fail() throws Exception{
        mockPairCameraPositionDAO.delete(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_delete_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockPairCameraPositionDAO.delete(this.pairCameraPositionA);
    }

    @Test
    public void test_delete_withValidInputArguments() throws Exception {
        Profile.PairCameraPosition returnValue1
                = pairCameraPositionDAO.create(pairCameraPositionA);
        Profile.PairCameraPosition returnValue2
                = pairCameraPositionDAO.create(pairCameraPositionB);
        Profile.PairCameraPosition returnValue3
                = pairCameraPositionDAO.create(pairCameraPositionC);
        assertTrue(pairCameraPositionDAO.delete(returnValue1));
        assertTrue(pairCameraPositionDAO.read(returnValue1.getId())== null);
        assertTrue(pairCameraPositionDAO.delete(returnValue2));
        assertTrue(pairCameraPositionDAO.read(returnValue2.getId())== null);
        assertTrue(pairCameraPositionDAO.delete(returnValue3));
        assertTrue(pairCameraPositionDAO.read(returnValue3.getId())== null);
    }

    @Test
    public void test_delete_withNotExistingInputparameter_fail() throws Exception{
        Profile.PairCameraPosition returnValue
                = pairCameraPositionDAO.create(pairCameraPosition1000000);
        assertTrue(pairCameraPosition1000000.getId() == 1000000);
        assertTrue(pairCameraPosition1000000.getProfileId() == 3);
        assertTrue(pairCameraPosition1000000.getCamera().equals(camera1));
        assertTrue(pairCameraPosition1000000.getPosition().equals(position1));
        assertTrue(pairCameraPositionDAO.delete(returnValue));
        assertFalse(pairCameraPositionDAO.delete(returnValue));
    }

}
