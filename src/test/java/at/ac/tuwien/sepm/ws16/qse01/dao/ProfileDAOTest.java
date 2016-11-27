package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCProfileDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.MatcherAssert.*;

/**
 * ProfileDAO Tester
 */
public class ProfileDAOTest {
    private ProfileDAO profileDAO;

    @Before
    public void before() throws Exception {
        this.profileDAO = new JDBCProfileDAO();
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProfileWithNullArgument() throws PersistenceException {
        profileDAO.create(null);
    }

    @Test
    public void createProfileWithValidArgument() throws PersistenceException {
        Profile profile = new Profile("default");
        Profile profileAfterCreate = profileDAO.create(profile);
        //assetTrue(true, true);
    }

}

