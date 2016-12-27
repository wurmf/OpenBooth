package at.ac.tuwien.sepm.ws16.qse01.dao;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import org.junit.After;
import org.junit.Before;

import at.ac.tuwien.sepm.ws16.qse01.dao.impl.TestEnvironment;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



/**
 * Test Shooting dao
 */
public class ShootingDAOTest extends TestEnvironment {



    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    private Shooting shooting1 = new Shooting(1, 1, "home/foto/shooting1", true);
    private Shooting shooting2 = new Shooting(2, 2, "home/foto/shooting2", false);
    private Shooting shooting3 = new Shooting(3, 1, "home/foto/shooting3", false);
    private Shooting shooting4 = new Shooting(4, 2, "home/foto/shooting4", false);
    private int id;

    @Test(expected = IllegalArgumentException.class)
    public void create_withNullArguments() throws Exception {
        mockShootingDAO.create(null);
    }

    @Test(expected = AssertionError.class)
    public void create_withExeption() throws Exception {
        assertTrue(shooting3.getId() == 3);
        assertTrue(shooting3.getProfileid() == 1);
        assertTrue(shooting3.getStorageDir().equals("home/foto/shooting3"));
        assertTrue(!shooting3.getActive());
        Shooting s1 = shootingDAO.create(shooting3);
        assertTrue(shooting3.getId() == 3);
        assertTrue(shooting3.getProfileid() == 1);
        assertTrue(shooting3.getStorageDir().equals("home/foto/shooting3"));
        assertTrue(!shooting3.getActive());
        Shooting s2 = shootingDAO.create(shooting3);

    }

    @Test
    public void create_SootingShwithValidInputArguments() throws Throwable {

        Shooting createdShooting = shootingDAO.create(shooting1);
        assertTrue(createdShooting.getId() >= 0);
        assertTrue(createdShooting.getActive());
        assertTrue(createdShooting.getStorageDir().equals("home/foto/shooting1"));
        assertTrue(createdShooting.getProfileid() == 1);
    }

    @Test
    public void mockShootingCreat() throws Throwable {

        when(mockResultSet.next()).thenReturn(Boolean.TRUE);
        when(mockResultSet.getInt(2)).thenReturn(id);
        Shooting shooting = mockShootingDAO.create(shooting2);
        verify(mockPreparedStatement).executeUpdate();
        assertTrue(shooting.getId() >=0);
        assertTrue(shooting.getStorageDir().equals("home/foto/shooting2"));
        assertTrue(shooting.getProfileid() == 2);
        assertTrue(!shooting.getActive());

    }


    @Test
    public void findactiveShooting() throws Throwable {
        shootingDAO.create(shooting1);
        Shooting shooting = shootingDAO.searchIsActive();
        //Check if read function returns the saved image.
        assertTrue(shooting.getId() > 0);
        assertTrue(shooting.getActive());
        assertTrue(shooting.getProfileid() == 1);
        assertTrue(shooting.getStorageDir().equals("home/foto/shooting1"));
    }

    @Test
    public void endShootingwithValideArguments() throws Throwable {
        shootingDAO.create(shooting1);
        assertTrue(shootingDAO.searchIsActive().getActive());

        shootingDAO.endShooting();
        assertTrue(!shootingDAO.searchIsActive().getActive());
    }

    @Test(expected = AssertionError.class)
    public void endShootingwithInValideArguments() throws Throwable {

        shootingDAO.endShooting();
        shootingDAO.create(shooting4);
        assertTrue(shootingDAO.searchIsActive().getActive());

        shootingDAO.endShooting();
        assertTrue(!shootingDAO.searchIsActive().getActive());
    }

}