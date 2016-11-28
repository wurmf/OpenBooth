package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.ServiceExeption;
import at.ac.tuwien.sepm.ws16.qse01.entities.Session;
import at.ac.tuwien.sepm.ws16.qse01.dao.SessionDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCSessionDAO;
import at.ac.tuwien.sepm.ws16.qse01.service.SessionControllService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Aniela on 23.11.2016.
 */
public class SessionControllServiceImpl implements SessionControllService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionControllServiceImpl.class);
    SessionDAO sessionDAO;
    public SessionControllServiceImpl() throws Exception {
        sessionDAO = new JDBCSessionDAO();
    }
    String getImageStorage(){
        //DAO.getImageStorage();
        String imagePath ="";
        return imagePath;
    }

    @Override
    public void add_session(Session session) throws ServiceExeption {
        try {
            sessionDAO.add_session(session);
        } catch (PersistenceException e) {
            throw new ServiceExeption(e.getMessage());
        }
    }

    @Override
    public Session search_isactive() throws ServiceExeption {

        try {
            return sessionDAO.search_isactive();
        } catch (PersistenceException e) {
            throw new ServiceExeption(e.getMessage());
        }
    }

    @Override
    public void end_session() {
        sessionDAO.end_session();
    }
}