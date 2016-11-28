package at.ac.tuwien.sepm.ws16.qse01.dao;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCShoutingDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shouting;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by Aniela on 28.11.2016.
 */
public class SessionDAOTest {
    private ShoutingDAO dao;

    @Before
    public void before() throws Exception {
        dao = new JDBCShoutingDAO();
    }

    /**
     * tests non working null session
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void addnullSession() throws Exception {
        dao.add_session(null);
    }

    /**
     * tests working session entry
     * @throws Exception
     */
    @Test
    public void addValideSession () throws Exception{
        Shouting shouting =new Shouting(0,"ab",true);
        dao.add_session(shouting);
    }
}
