package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;

import java.util.List;

/**
 * Profile.LogoRelativeRectangle DAO
 */
public interface PairLogoRelativeRectangleDAO {
    Profile.PairLogoRelativeRectangle create(int profileId,Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException;

    List<Profile.PairLogoRelativeRectangle> createAll(Profile profile) throws PersistenceException;

    List<Profile.PairLogoRelativeRectangle> readAll(int profileId) throws PersistenceException;

    boolean delete(int profileId,Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException;

    boolean deleteAll(Profile profile) throws PersistenceException;
}
