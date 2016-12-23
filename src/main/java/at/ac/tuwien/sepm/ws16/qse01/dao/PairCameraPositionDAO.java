package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;

import java.util.List;

/**
 * Profile.PairCameraPositionDAO
 */
public interface PairCameraPositionDAO {
    Profile.PairCameraPosition create(Profile.PairCameraPosition pairCameraPosition) throws PersistenceException;

    List<Profile.PairCameraPosition> createAll(List<Profile.PairCameraPosition> pairCameraPositions) throws PersistenceException;

    boolean update(Profile.PairCameraPosition pairCameraPosition)throws PersistenceException;

    Profile.PairCameraPosition read(int id) throws PersistenceException;

    List<Profile.PairCameraPosition> readAllWithProfileID(int profileId) throws PersistenceException;

    boolean delete(Profile.PairCameraPosition pairCameraPosition) throws PersistenceException;

    boolean deleteAllWithProfileID(int profileId) throws PersistenceException;
}
