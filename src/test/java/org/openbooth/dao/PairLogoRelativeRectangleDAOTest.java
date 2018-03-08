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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * PairLogoRelativeRectangleDAO Tester
 */
public class PairLogoRelativeRectangleDAOTest extends TestEnvironment {
    private static final Logger LOGGER = LoggerFactory.getLogger(PairLogoRelativeRectangleDAOTest.class);

    private int id;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        pairLogoRelativeRectangleA.setProfileId(2);
        pairLogoRelativeRectangleB.setProfileId(2);
        pairLogoRelativeRectangleC.setProfileId(2);
        logoDAO.create(logoA);
        id = 1;
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     *
     *TESTING method:
     * Profile.PairLogoRelativeRectangle create(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle)
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_create_withNullArguments_Fail() throws Exception{
        mockPairLogoRelativeRectangleDAO.create(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_create_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString(),anyInt())).thenThrow(SQLException.class);
        mockPairLogoRelativeRectangleDAO.create(this.pairLogoRelativeRectangleA);
    }

    @Test
    public void test_create_withValidInputArguments() throws Exception {
        assertTrue(pairLogoRelativeRectangleA.getId() == Integer.MIN_VALUE);
        assertTrue(pairLogoRelativeRectangleA.getProfileId() == 2);
        assertTrue(pairLogoRelativeRectangleA.getLogo().equals(logo1));
        assertTrue(pairLogoRelativeRectangleA.getRelativeRectangle().equals(relativeRectangleA));
        Profile.PairLogoRelativeRectangle returnValue = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangleA);
        assertTrue(returnValue.getId()>=1);
        assertTrue(returnValue.getProfileId() == 2);
        assertTrue(returnValue.getLogo().equals(logo1));
        assertTrue(returnValue.getRelativeRectangle().equals(relativeRectangleA));
    }

    @Test
    public void testmock_create_withValidInputArguments() throws Exception {
        when(mockResultSet.next()).thenReturn(Boolean.TRUE);
        when(mockResultSet.getInt(1)).thenReturn(id);
        Profile.PairLogoRelativeRectangle returnValue
                = mockPairLogoRelativeRectangleDAO.create(this.pairLogoRelativeRectangleA);
        verify(mockPreparedStatement).executeUpdate();
        assertTrue(returnValue.getId() == 1);
        assertTrue(returnValue.getProfileId() == 2);
        assertTrue(returnValue.getLogo().equals(logo1));
        assertTrue(returnValue.getRelativeRectangle().equals(relativeRectangleA));
    }

    @Test(expected = PersistenceException.class)
    public void test_create_withAlreadyExistingInputparameter_fail() throws Exception{
        assertTrue(pairLogoRelativeRectangle1000000.getId() == 1000000);
        assertTrue(pairLogoRelativeRectangle1000000.getProfileId() == 2);
        assertTrue(pairLogoRelativeRectangle1000000.getLogo().equals(logo1));
        assertTrue(pairLogoRelativeRectangle1000000.getRelativeRectangle().equals(relativeRectangleD));
        Profile.PairLogoRelativeRectangle returnValue
                = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangle1000000);
        assertTrue(returnValue.getId() == 1000000);
        assertTrue(returnValue.getProfileId() == 2);
        assertTrue(returnValue.getLogo().equals(logo1));
        assertTrue(returnValue.getRelativeRectangle().equals(relativeRectangleD));
        Profile.PairLogoRelativeRectangle returnValue2
                = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangle1000000);
    }

    /**
     * TESTING method:
     * boolean update(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle)
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_update_withNullArguments_Fail() throws Exception{
        mockPairLogoRelativeRectangleDAO.update(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_update_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockPairLogoRelativeRectangleDAO.update(this.pairLogoRelativeRectangleA);
    }

    @Test
    public void test_update_withValidInputParameter()throws Exception{
        assertTrue(pairLogoRelativeRectangleA.getId() == Integer.MIN_VALUE);
        assertTrue(pairLogoRelativeRectangleA.getProfileId() == 2);
        assertTrue(pairLogoRelativeRectangleA.getLogo().equals(logo1));
        assertTrue(pairLogoRelativeRectangleA.getRelativeRectangle().equals(relativeRectangleA));
        Profile.PairLogoRelativeRectangle returnValue = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangleA);
        assertTrue(returnValue.getId()>=1);
        assertTrue(returnValue.getProfileId() == 2);
        assertTrue(returnValue.getLogo().equals(logo1));
        assertTrue(returnValue.getRelativeRectangle().equals(relativeRectangleA));
        returnValue.setProfileId(1);
        returnValue.setLogo(logo2);
        returnValue.setRelativeRectangle(relativeRectangleB);
        boolean returnBoolean = pairLogoRelativeRectangleDAO.update(returnValue);
        assertTrue(returnBoolean);
    }

    @Test
    public void test_update_WithNotExisting()throws Exception{
        assertTrue(pairLogoRelativeRectangleA.getId() == Integer.MIN_VALUE);
        assertTrue(pairLogoRelativeRectangleA.getProfileId() == 2);
        assertTrue(pairLogoRelativeRectangleA.getLogo().equals(logo1));
        assertTrue(pairLogoRelativeRectangleA.getRelativeRectangle().equals(relativeRectangleA));
        Profile.PairLogoRelativeRectangle returnValue = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangleA);
        assertTrue(returnValue.getId()>=1);
        assertTrue(returnValue.getProfileId() == 2);
        assertTrue(returnValue.getLogo().equals(logo1));
        assertTrue(returnValue.getRelativeRectangle().equals(relativeRectangleA));
        returnValue.setId(returnValue.getId()+1);
        returnValue.setProfileId(1);
        returnValue.setLogo(logo2);
        returnValue.setRelativeRectangle(relativeRectangleB);
        boolean returnBoolean = pairLogoRelativeRectangleDAO.update(returnValue);
        assertTrue(!returnBoolean);
    }

    /**
     * TESTING method:
     * Profile.PairLogoRelativeRectangle read(int id) throws PersistenceException;
     */

    @Test
    public void test_read_withValidInt() throws Exception{
        Profile.PairLogoRelativeRectangle returnValue1
                = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangleA);
        Profile.PairLogoRelativeRectangle returnValue2
                = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangleB);
        Profile.PairLogoRelativeRectangle returnValue3
                = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangleC);
        assertTrue(pairLogoRelativeRectangleDAO.read(returnValue1.getId()).equals(returnValue1));
        assertTrue(pairLogoRelativeRectangleDAO.read(returnValue2.getId()).equals(returnValue2));
        assertTrue(pairLogoRelativeRectangleDAO.read(returnValue3.getId()).equals(returnValue3));
    }

    @Test
    public void test_read_NotExisting() throws Exception{
        Profile.PairLogoRelativeRectangle returnValue1
                = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangleA);
        assertTrue(pairLogoRelativeRectangleDAO.read(returnValue1.getId() + 1) == null);
    }

    /**
     * TESTING method:
     * List<Profile.PairLogoRelativeRectangle> readAllWithProfileID(int profileId)
     * throws PersistenceException;
     */

    @Test
    public void test_readAll_withNonEmptyReturnList() throws Exception{
        Profile.PairLogoRelativeRectangle returnValue1
                = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangleA);
        Profile.PairLogoRelativeRectangle returnValue2
                = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangleB);
        Profile.PairLogoRelativeRectangle returnValue3
                = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangleC);
        List<Profile.PairLogoRelativeRectangle> returnList
                = pairLogoRelativeRectangleDAO.readAllWithProfileID(2);
        assertTrue(returnList.contains(returnValue1)
                && returnList.contains(returnValue2)
                && returnList.contains(returnValue3));
    }

    @Test
    public void test_readAll_withEmptyReturnList() throws Exception{
        assertTrue(pairLogoRelativeRectangleDAO.readAllWithProfileID(10).size() == 0);
    }

    /**
     * TESTING method:
     * boolean delete(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle)
     * throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_delete_withNullArguments_Fail() throws Exception{
        mockPairLogoRelativeRectangleDAO.delete(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_delete_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockPairLogoRelativeRectangleDAO.delete(this.pairLogoRelativeRectangleA);
    }

    @Test
    public void test_delete_withValidInputArguments() throws Exception {
        Profile.PairLogoRelativeRectangle returnValue1
                = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangleA);
        Profile.PairLogoRelativeRectangle returnValue2
                = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangleB);
        Profile.PairLogoRelativeRectangle returnValue3
                = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangleC);
        assertTrue(pairLogoRelativeRectangleDAO.delete(returnValue1));
        assertTrue(pairLogoRelativeRectangleDAO.read(returnValue1.getId())== null);
        assertTrue(pairLogoRelativeRectangleDAO.delete(returnValue2));
        assertTrue(pairLogoRelativeRectangleDAO.read(returnValue2.getId())== null);
        assertTrue(pairLogoRelativeRectangleDAO.delete(returnValue3));
        assertTrue(pairLogoRelativeRectangleDAO.read(returnValue3.getId())== null);
    }

    @Test
    public void test_delete_withNotExistingInputparameter_fail() throws Exception{
        Profile.PairLogoRelativeRectangle returnValue
                = pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangle1000000);
        assertTrue(pairLogoRelativeRectangle1000000.getId() == 1000000);
        assertTrue(pairLogoRelativeRectangle1000000.getProfileId() == 2);
        assertTrue(pairLogoRelativeRectangle1000000.getLogo().equals(logo1));
        assertTrue(pairLogoRelativeRectangle1000000.getRelativeRectangle().equals(relativeRectangleD));
        assertTrue(pairLogoRelativeRectangleDAO.delete(returnValue));
        assertFalse(pairLogoRelativeRectangleDAO.delete(returnValue));
    }

}
