package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Background;

import java.util.List;

/**
 * BackgroundDAO
 */
public interface BackgroundDAO {
    Background create(Background background) throws PersistenceException;
    boolean update(Background background) throws  PersistenceException;
    Background read(int id) throws PersistenceException;
    List<Background> readAllAssociatedWithProfile(int id) throws PersistenceException;
    List<Background> readAllAssociatedWithEvent(int id) throws PersistenceException;
    List<Background> readAllAssociatedGlobal() throws PersistenceException;
    boolean delete(Background background) throws PersistenceException;
}
