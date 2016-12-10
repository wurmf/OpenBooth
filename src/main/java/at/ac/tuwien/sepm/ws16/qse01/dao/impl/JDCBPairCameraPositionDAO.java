package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.PairCameraPositionDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;

import java.util.List;

/**
 * H2 database-Specific PairCameraPositionDAO Implementation
 */
public class JDCBPairCameraPositionDAO implements PairCameraPositionDAO {
    @Override
    public Profile.PairCameraPosition create(Profile.PairCameraPosition pairCameraPosition) throws PersistenceException {
        return null;
    }

    @Override
    public List<Profile.PairCameraPosition> readAll() throws PersistenceException {
        return null;
    }

    @Override
    public boolean delete(Profile.PairCameraPosition pairCameraPosition) throws PersistenceException {
        return false;
    }
}
