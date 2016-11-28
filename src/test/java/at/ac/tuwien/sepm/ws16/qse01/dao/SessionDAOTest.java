package at.ac.tuwien.sepm.ws16.qse01.dao;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCSessionDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Session;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by Aniela on 28.11.2016.
 */
public class SessionDAOTest {
    private SessionDAO dao;

    @Before
    public void before() throws Exception {
        dao = new JDBCSessionDAO();
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
        Session session =new Session(0,"ab",true);
        dao.add_session(session);
    }
}
