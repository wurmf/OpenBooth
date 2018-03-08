package org.openbooth.dao;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Profile;

import java.util.List;

/**
 * Profile.LogoRelativeRectangle DAO
 */
public interface PairLogoRelativeRectangleDAO {

    Profile.PairLogoRelativeRectangle create(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException;

    List<Profile.PairLogoRelativeRectangle> createAll(List<Profile.PairLogoRelativeRectangle> pairLogoRelativeRectangles) throws PersistenceException;

    boolean update(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle)throws PersistenceException;

    Profile.PairLogoRelativeRectangle read(int id) throws PersistenceException;

    List<Profile.PairLogoRelativeRectangle> readAllWithProfileID(int profileId) throws PersistenceException;

    boolean delete(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException;

    boolean deleteAllWithProfileID(int profileId) throws PersistenceException;
}
