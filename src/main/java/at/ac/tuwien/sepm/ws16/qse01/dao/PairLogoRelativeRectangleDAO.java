package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;

import java.util.List;

/**
 * Profile.LogoRelativeRectangleDAO
 */
public interface PairLogoRelativeRectangleDAO {
    Profile.PairLogoRelativeRectangle create(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException;
    List<Profile.PairLogoRelativeRectangle> readAll() throws PersistenceException;
    boolean delete(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException;
}
