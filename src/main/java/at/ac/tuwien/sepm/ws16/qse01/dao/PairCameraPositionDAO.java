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

    boolean update(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle)throws PersistenceException;

    List<Profile.PairCameraPosition> readAll(int profileId) throws PersistenceException;
    boolean delete(int profileId,Profile.PairCameraPosition pairCameraPosition) throws PersistenceException;
    boolean deleteAll(Profile profile) throws PersistenceException;
}
