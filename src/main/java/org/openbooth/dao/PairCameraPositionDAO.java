package org.openbooth.dao;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Profile;

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
