package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.TestEnvironment;
import at.ac.tuwien.sepm.ws16.qse01.entities.Logo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * LogoDAO Tester
 */
public class LogoDAOTest extends TestEnvironment{
    private static final Logger LOGGER = LoggerFactory.getLogger(LogoDAOTest.class);

    @Before public void setUp() throws Exception {
        super.setUp();
    }

    @After public void tearDown() throws Exception {
        super.tearDown();
    }

    private Logo logoA = new Logo("Logo A", "/dev/null/logoA.jpg");
    private Logo logoB = new Logo("Logo B", "/dev/null/logoB.jpg");
    private Logo logoC = new Logo("Logo C", "/dev/null/logoC.jpg");
    private Logo logo1000000 = new Logo(1000000,"Logo 1000000", "/dev/null/logo1000000.jpg", false);
    private int id = 1;
    /**
     *
     *TESTING method: Logo create(Logo logo) throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_create_withNullArguments_Fail() throws Exception{
        mockLogoDAO.create(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_create_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString(),anyInt())).thenThrow(SQLException.class);
        mockLogoDAO.create(this.logoA);
    }

    @Test
    public void test_create_withValidInputArguments() throws Exception {
        assertTrue(logoA.getId() == Integer.MIN_VALUE);
        assertTrue(logoA.getLabel() == "Logo A");
        assertTrue(logoA.getPath() == "/dev/null/logoA.jpg");
        assertTrue(logoA.isDeleted() == false);
        Logo returnValue = logoDAO.create(logoA);
        assertTrue(returnValue.getId()>=1);
        assertTrue(logoA.getLabel() == "Logo A");
        assertTrue(logoA.getPath() == "/dev/null/logoA.jpg");
        assertTrue(logoA.isDeleted() == false);
    }

    @Test
    public void testmock_create_withValidInputArguments() throws Exception{
        when(mockResultSet.next()).thenReturn(Boolean.TRUE);
        when(mockResultSet.getInt(1)).thenReturn(id);
        Logo returnvalue = mockLogoDAO.create(this.logoA);
        verify(mockPreparedStatement).executeUpdate();
        assertTrue(returnvalue.getId() == 1);
        assertTrue(returnvalue.getLabel() == "Logo A");
        assertTrue(returnvalue.getPath() == "/dev/null/logoA.jpg");
    }

    @Test(expected = PersistenceException.class)
    public void test_create_withAlreadyExistingInputparameter_fail() throws Exception{
        assertTrue(logo1000000.getId() == 1000000);
        assertTrue(logo1000000.getLabel() == "Logo 1000000");
        assertTrue(logo1000000.getPath() == "/dev/null/logo1000000.jpg");
        assertTrue(!logo1000000.isDeleted());
        Logo returnValue = logoDAO.create(logo1000000);
        assertTrue(logo1000000.getId() == 1000000);
        assertTrue(logo1000000.getLabel() == "Logo 1000000");
        assertTrue(logo1000000.getPath() == "/dev/null/logo1000000.jpg");
        assertTrue(!logo1000000.isDeleted());
        Logo returnValue2 = logoDAO.create(logo1000000);
    }

    /**
    *
    *TESTING method: boolean update(Logo logo) throws PersistenceException;
    */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_update_withNullArguments_Fail() throws Exception{
        mockLogoDAO.update(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_update_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockLogoDAO.update(this.logoA);
    }

    @Test
    public void test_update_withValidInputParameter()throws Exception{
        assertTrue(logoA.getId() == Integer.MIN_VALUE);
        assertTrue(logoA.getLabel() == "Logo A");
        assertTrue(logoA.getPath() == "/dev/null/logoA.jpg");
        assertTrue(logoA.isDeleted() == false);
        Logo returnValue = logoDAO.create(logoA);
        assertTrue(logoA.getId() >= 1);
        assertTrue(logoA.getLabel() == "Logo A");
        assertTrue(logoA.getPath() == "/dev/null/logoA.jpg");
        assertTrue(logoA.isDeleted() == false);
        returnValue.setLabel("Modified Logo A1");
        returnValue.setPath("/dev/null/modifiedlogoA1.jpg");
        boolean returnBoolean = logoDAO.update(returnValue);
        assertTrue(returnBoolean);
    }

    @Test
    public void test_update_WithNotExisting()throws Exception{
        assertTrue(logoA.getId() == Integer.MIN_VALUE);
        assertTrue(logoA.getLabel() == "Logo A");
        assertTrue(logoA.getPath() == "/dev/null/logoA.jpg");
        assertTrue(logoA.isDeleted() == false);
        Logo returnValue = logoDAO.create(logoA);
        assertTrue(logoA.getId() >= 1);
        assertTrue(logoA.getLabel() == "Logo A");
        assertTrue(logoA.getPath() == "/dev/null/logoA.jpg");
        assertTrue(logoA.isDeleted() == false);
        returnValue.setId(returnValue.getId()+1);
        returnValue.setLabel("Modified Logo A1");
        returnValue.setPath("/dev/null/modifiedlogoA1.jpg");
        boolean returnBoolean = logoDAO.update(returnValue);
        assertTrue(!returnBoolean);
    }

    /**
     *
     *TESTING method: Logo read(int id) throws PersistenceException;
     */

    @Test
    public void test_read_withValidInt() throws Exception{
        Logo returnValue1 = logoDAO.create(logoA);
        Logo returnValue2 = logoDAO.create(logoB);
        Logo returnValue3 = logoDAO.create(logoC);
        assertTrue(logoDAO.read(returnValue1.getId()).equals(returnValue1));
        assertTrue(logoDAO.read(returnValue2.getId()).equals(returnValue2));
        assertTrue(logoDAO.read(returnValue3.getId()).equals(returnValue3));
    }

    @Test
    public void test_read_NotExisting() throws Exception{
        Logo returnValue1 = logoDAO.create(logoA);
        assertTrue(logoDAO.read(returnValue1.getId() + 1) == null);
    }

    /**
     *
     *TESTING method: List<Logo> readAll() throws PersistenceException;
     */

    @Test
    public void test_readAll_withNonEmptyReturnList() throws Exception{
        Logo returnValue1 = logoDAO.create(logoA);
        Logo returnValue2 = logoDAO.create(logoB);
        Logo returnValue3 = logoDAO.create(logoC);
        List<Logo> returnList = logoDAO.readAll();
        assertTrue(returnList.contains(returnValue1)
            && returnList.contains(returnValue2)
            && returnList.contains(returnValue3));
    }

    @Test
    public void test_readAll_withEmptyReturnList() throws Exception{
        logoDAO.delete(logoDAO.read(1));
        logoDAO.delete(logoDAO.read(2));
        assertTrue(logoDAO.readAll().size() == 0);
    }

    /**
     *
     *TESTING method: boolean delete(Logo logo) throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_delete_withNullArguments_Fail() throws Exception{
        mockLogoDAO.delete(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_delete_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockLogoDAO.delete(this.logoA);
    }

    @Test
    public void test_delete_withValidInputArguments() throws Exception {
        Logo returnValue1 = logoDAO.create(logoA);
        Logo returnValue2 = logoDAO.create(logoB);
        Logo returnValue3 = logoDAO.create(logoC);
        assertTrue(logoDAO.delete(returnValue1));
        assertTrue(logoDAO.read(returnValue1.getId())== null);
        assertTrue(logoDAO.delete(returnValue2));
        assertTrue(logoDAO.read(returnValue2.getId())== null);
        assertTrue(logoDAO.delete(returnValue3));
        assertTrue(logoDAO.read(returnValue3.getId())== null);
    }

    @Test
    public void test_delete_withNotExistingInputparameter_fail() throws Exception{
        Logo returnValue = logoDAO.create(logo1000000);
        assertTrue(logo1000000.getId() == 1000000);
        assertTrue(logo1000000.getLabel() == "Logo 1000000");
        assertTrue(logo1000000.getPath() == "/dev/null/logo1000000.jpg");
        assertTrue(logo1000000.isDeleted() == false);
        assertTrue(logoDAO.delete(returnValue));
        assertFalse(logoDAO.delete(returnValue));
    }
}