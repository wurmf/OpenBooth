package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Background;

import java.util.List;

/**
 * EventDAO
 */
public interface EventDAO {
    Background.Event create(Background.Event event) throws PersistenceException;
    boolean update(Background.Event event) throws PersistenceException;
    Background.Event read(int id) throws PersistenceException;
    List<Background.Event> readAll() throws PersistenceException;
    boolean delete(Background.Event event) throws PersistenceException;
}
