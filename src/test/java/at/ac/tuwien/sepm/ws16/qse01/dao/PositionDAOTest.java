package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.TestEnvironment;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
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
 * PositionDAO Tester
 */
public class PositionDAOTest extends TestEnvironment {
    private static final Logger LOGGER = LoggerFactory.getLogger(PositionDAOTest.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws PersistenceException {
        super.tearDown();
    }

    private Position positionA = new Position("Position A", "/dev/null/positionA.jpg");
    private Position positionB = new Position("Position B", "/dev/null/positionB.jpg");
    private Position positionC = new Position("Position C", "/dev/null/positionC.jpg");
    private Position position1000000 = new Position(1000000,"Position 1000000", "/dev/null/position1000000.jpg", false);
    private int id = 1;
    /**
     *
     *TESTING method: Position create(Position position) throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_create_withNullArguments_Fail() throws Exception{
        mockPositionDAO.create(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_create_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString(),anyInt())).thenThrow(SQLException.class);
        mockPositionDAO.create(this.positionA);
    }

    @Test
    public void test_create_withValidInputArguments() throws Exception {
        assertTrue(positionA.getId() == Integer.MIN_VALUE);
        assertTrue(positionA.getName() == "Position A");
        assertTrue(positionA.getButtonImagePath() == "/dev/null/positionA.jpg");
        assertTrue(!positionA.isDeleted());
        Position returnValue = positionDAO.create(positionA);
        assertTrue(returnValue.getId()>=1);
        assertTrue(returnValue.getName() == "Position A");
        assertTrue(returnValue.getButtonImagePath() == "/dev/null/positionA.jpg");
        assertTrue(!returnValue.isDeleted());
    }

    @Test
    public void testmock_create_withValidInputArguments() throws Exception {
        when(mockResultSet.next()).thenReturn(Boolean.TRUE);
        when(mockResultSet.getInt(1)).thenReturn(id);
        Position returnvalue = mockPositionDAO.create(this.positionA);
        verify(mockPreparedStatement).executeUpdate();
        assertTrue(returnvalue.getId() == 1);
        assertTrue(returnvalue.getName() == "Position A");
        assertTrue(returnvalue.getButtonImagePath() == "/dev/null/positionA.jpg");
    }

    @Test(expected = PersistenceException.class)
    public void test_create_withAlreadyExistingInputparameter_fail() throws Exception{
        assertTrue(position1000000.getId() == 1000000);
        assertTrue(position1000000.getName() == "Position 1000000");
        assertTrue(position1000000.getButtonImagePath() == "/dev/null/position1000000.jpg");
        assertTrue(!position1000000.isDeleted());
        Position returnValue = positionDAO.create(position1000000);
        assertTrue(position1000000.getId() == 1000000);
        assertTrue(position1000000.getName() == "Position 1000000");
        assertTrue(position1000000.getButtonImagePath() == "/dev/null/position1000000.jpg");
        assertTrue(!position1000000.isDeleted());
        Position returnValue2 = positionDAO.create(position1000000);
    }

    /**
     *
     *TESTING method: boolean update(Position position) throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_update_withNullArguments_Fail() throws Exception{
        mockLogoDAO.update(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_update_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockPositionDAO.update(this.positionA);
    }

    @Test
    public void test_update_withValidInputParameter()throws Exception{
        assertTrue(positionA.getId() == Integer.MIN_VALUE);
        assertTrue(positionA.getName() == "Position A");
        assertTrue(positionA.getButtonImagePath() == "/dev/null/positionA.jpg");
        assertTrue(!positionA.isDeleted());
        Position returnValue = positionDAO.create(positionA);
        assertTrue(positionA.getId() >= 1);
        assertTrue(positionA.getName() == "Position A");
        assertTrue(positionA.getButtonImagePath() == "/dev/null/positionA.jpg");
        assertTrue(!positionA.isDeleted());
        returnValue.setName("Modified Position A1");
        returnValue.setButtonImagePath("/dev/null/modifiedpositionA1.jpg");
        boolean returnBoolean = positionDAO.update(returnValue);
        assertTrue(returnBoolean);
    }

    @Test
    public void test_update_WithNotExisting()throws Exception{
        assertTrue(positionA.getId() == Integer.MIN_VALUE);
        assertTrue(positionA.getName() == "Position A");
        assertTrue(positionA.getButtonImagePath() == "/dev/null/positionA.jpg");
        assertTrue(!positionA.isDeleted());
        Position returnValue = positionDAO.create(positionA);
        assertTrue(positionA.getId() >= 1);
        assertTrue(positionA.getName() == "Position A");
        assertTrue(positionA.getButtonImagePath() == "/dev/null/positionA.jpg");
        assertTrue(!positionA.isDeleted());
        returnValue.setId(returnValue.getId() + 1);
        returnValue.setName("Modified Position A1");
        returnValue.setButtonImagePath("/dev/null/modifiedpositionA1.jpg");
        boolean returnBoolean = positionDAO.update(returnValue);
        assertTrue(!returnBoolean);
    }

    /**
     *
     *TESTING method: Position read(int id) throws PersistenceException;
     */

    @Test
    public void test_read_withValidInt() throws Exception{
        Position returnValue1 = positionDAO.create(positionA);
        Position returnValue2 = positionDAO.create(positionB);
        Position returnValue3 = positionDAO.create(positionC);
        assertTrue(positionDAO.read(returnValue1.getId()).equals(returnValue1));
        assertTrue(positionDAO.read(returnValue2.getId()).equals(returnValue2));
        assertTrue(positionDAO.read(returnValue3.getId()).equals(returnValue3));
    }

    @Test
    public void test_read_NotExisting() throws Exception{
        Position returnValue1 = positionDAO.create(positionA);
        assertTrue(positionDAO.read(returnValue1.getId() + 1) == null);
    }

    /**
     *
     *List<Position> readAll() throws PersistenceException;
     */

    @Test
    public void test_readAll_withNonEmptyReturnList() throws Exception{
        Position returnValue1 = positionDAO.create(positionA);
        Position returnValue2 = positionDAO.create(positionB);
        Position returnValue3 = positionDAO.create(positionC);
        List<Position> returnList = positionDAO.readAll();
        assertTrue(returnList.contains(returnValue1)
                && returnList.contains(returnValue2)
                && returnList.contains(returnValue3));
    }

    @Test
    public void test_readAll_withEmptyReturnList() throws Exception{
        positionDAO.delete(positionDAO.read(1));
        positionDAO.delete(positionDAO.read(2));
        assertTrue(positionDAO.readAll().size() == 0);
    }

    /**
     *
     *TESTING method: boolean delete(Position position) throws PersistenceException;
     */

    @Test(expected = IllegalArgumentException.class)
    public void testmock_delete_withNullArguments_Fail() throws Exception{
        mockPositionDAO.delete(null);
    }

    @Test(expected = PersistenceException.class)
    public void testmock_delete_withPersistenceTroubles_Fail() throws Exception{
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        mockPositionDAO.delete(this.positionA);
    }

    @Test
    public void test_delete_withValidInputArguments() throws Exception {
        Position returnValue1 = positionDAO.create(positionA);
        Position returnValue2 = positionDAO.create(positionB);
        Position returnValue3 = positionDAO.create(positionC);
        assertTrue(positionDAO.delete(returnValue1));
        assertTrue(positionDAO.read(returnValue1.getId())== null);
        assertTrue(positionDAO.delete(returnValue2));
        assertTrue(positionDAO.read(returnValue2.getId())== null);
        assertTrue(positionDAO.delete(returnValue3));
        assertTrue(positionDAO.read(returnValue3.getId())== null);
    }

    @Test
    public void test_delete_withNotExistingInputparameter_fail() throws Exception{
        Position returnValue = positionDAO.create(position1000000);
        assertTrue(position1000000.getId() == 1000000);
        assertTrue(position1000000.getName() == "Position 1000000");
        assertTrue(position1000000.getButtonImagePath() == "/dev/null/position1000000.jpg");
        assertTrue(!position1000000.isDeleted());
        assertTrue(positionDAO.delete(returnValue));
        assertFalse(positionDAO.delete(returnValue));
    }
}
