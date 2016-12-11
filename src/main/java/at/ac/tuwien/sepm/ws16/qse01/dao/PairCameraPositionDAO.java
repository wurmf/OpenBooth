package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;

import java.util.List;

/**
 * Profile.PairCameraPositionDAO
 */
public interface PairCameraPositionDAO {
    Profile.PairCameraPosition create(long profileId, Profile.PairCameraPosition pairCameraPosition) throws PersistenceException;
    List<Profile.PairCameraPosition> readAll(long profileId) throws PersistenceException;
    boolean delete(long profileId,Profile.PairCameraPosition pairCameraPosition) throws PersistenceException;
}
