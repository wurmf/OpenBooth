package at.ac.tuwien.sepm.ws16.qse01.dao;
import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by Aniela on 28.11.2016.
 */
public class ShootingDAOTest {
    private ShootingDAO dao;

    @Before
    public void before() throws Exception {
        dao = new JDBCShootingDAO(H2Handler.getInstance());
    }

    /**
     * tests non working null session
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void addnullSession() throws Exception {
        dao.create(null);
    }

    /**
     * tests working session entry
     * @throws Exception
     */
    @Test
    public void addValideSession () throws Exception{
        Shooting shouting =new Shooting(0,1,"ab",true);
        dao.create(shouting);
    }
}