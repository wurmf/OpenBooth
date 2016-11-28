package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shouting;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShoutingDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCShoutingDAO;
import at.ac.tuwien.sepm.ws16.qse01.service.ShoutingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Aniela on 23.11.2016.
 */
public class ShoutingServiceImpl implements ShoutingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoutingServiceImpl.class);
    ShoutingDAO sessionDAO;
    public ShoutingServiceImpl() throws Exception {
        sessionDAO = new JDBCShoutingDAO();
    }
    String getImageStorage(){
        //DAO.getImageStorage();
        String imagePath ="";
        return imagePath;
    }


    public void add_session(Shouting shouting) throws ServiceException {
        try {
            sessionDAO.add_session(shouting);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Shouting search_isactive() throws ServiceException {

        try {
            return sessionDAO.search_isactive();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    public void end_session() {
        sessionDAO.end_session();
    }
}