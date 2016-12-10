package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.PairLogoRelativeRectangleDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;

import java.util.List;

/**
 * H2 database-Specific PairLogoRelativeRectangleDAO Implementation
 */
public class JDCBPairLogoRelativeRecangleDAO implements PairLogoRelativeRectangleDAO {
    @Override
    public Profile.PairLogoRelativeRectangle create(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException {
        return null;
    }

    @Override
    public List<Profile.PairLogoRelativeRectangle> readAll() throws PersistenceException {
        return null;
    }

    @Override
    public boolean delete(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException {
        return false;
    }
}
