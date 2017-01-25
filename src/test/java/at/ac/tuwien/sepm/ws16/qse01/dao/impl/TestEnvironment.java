package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2EmbeddedHandler;
import at.ac.tuwien.sepm.ws16.qse01.dao.*;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.*;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.CameraServiceImpl;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ProfileServiceImpl;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ShootingServiceImpl;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test Environment
 * provides setup (Database connections and mocks) for all DAO tests
 */

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class TestEnvironment {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestEnvironment.class);
    protected LogoDAO logoDAO;
    protected LogoDAO mockLogoDAO;

    protected CameraDAO cameraDAO;
    protected CameraDAO mockCameraDAO;

    protected PositionDAO positionDAO;
    protected PositionDAO mockPositionDAO;

    protected PairLogoRelativeRectangleDAO pairLogoRelativeRectangleDAO;
    protected PairLogoRelativeRectangleDAO mockPairLogoRelativeRectangleDAO;

    protected PairCameraPositionDAO pairCameraPositionDAO;
    protected PairCameraPositionDAO mockPairCameraPositionDAO;

    protected ProfileDAO profileDAO;
    protected ProfileDAO mockProfileDAO;

    protected ImageDAO imageDAO;
    protected ImageDAO mockImageDAO;

    protected ShootingDAO shootingDAO;
    protected ShootingDAO mockShootingDAO;

    protected BackgroundCategoryDAO backgroundCategoryDAO;
    protected BackgroundCategoryDAO mockbackgroundCategoryDAO;

    protected ProfileService profileService;

    protected AdminUserDAO adminUserDAO;

    protected Connection con;

    @Mock protected H2EmbeddedHandler mockH2Handler;
    @Mock protected Connection mockConnection;
    @Mock protected Statement mockStatement;
    @Mock protected PreparedStatement mockPreparedStatement;
    @Mock protected ResultSet mockResultSet;

    protected Camera cameraA;
    protected Position positionA;
    protected Position positionB;
    protected Position positionC;
    protected Position position1000000;
    protected Logo logoA;
    protected Logo logoB;
    protected Logo logoC;
    protected Logo logo1000000;
    protected Camera camera1;
    protected Camera camera2;
    protected Position position1;
    protected Position position2;
    protected Logo logo1;
    protected Logo logo2;
    protected Profile.PairCameraPosition pairCameraPositionA;
    protected Profile.PairCameraPosition pairCameraPositionB;
    protected Profile.PairCameraPosition pairCameraPositionC;
    protected Profile.PairLogoRelativeRectangle pairLogoRelativeRectangleA;
    protected Profile.PairLogoRelativeRectangle pairLogoRelativeRectangleB;
    protected Profile.PairLogoRelativeRectangle pairLogoRelativeRectangleC;
    protected RelativeRectangle relativeRectangleA;
    protected RelativeRectangle relativeRectangleB;
    protected RelativeRectangle relativeRectangleC;
    protected List<Profile.PairCameraPosition> pairCameraPositions;
    protected List<Profile.PairLogoRelativeRectangle> pairLogoRelativeRectangles;
    protected Profile profileA;
    protected Profile profileB;
    protected Profile profileC;
    protected Background.Category backgroundCategoryA;
    protected Background.Category backgroundCategoryB;
    protected Background.Category backgroundCategory10;

    @Before public void setUp() throws Exception
    {
        this.con = H2EmbeddedHandler.getInstance().getTestConnection();
        /* Setup test mocks
        *  Please don't mess with these ones,
        *  if you don't understand completely what implications it has
        */
        when(mockH2Handler.getConnection()).thenReturn(mockConnection);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.prepareStatement(anyString(),anyInt())).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE,Boolean.FALSE);

        mockLogoDAO = new JDBCLogoDAO(mockH2Handler);
        mockCameraDAO = new JDBCCameraDAO(mockH2Handler);
        mockPositionDAO = new JDBCPositionDAO(mockH2Handler);
        mockPairLogoRelativeRectangleDAO = new JDCBPairLogoRelativeRectangleDAO(mockH2Handler);
        mockPairCameraPositionDAO = new JDCBPairCameraPositionDAO(mockH2Handler);
        mockProfileDAO = new JDBCProfileDAO(mockH2Handler);
        mockImageDAO = new JDBCImageDAO(mockH2Handler);
        mockShootingDAO = new JDBCShootingDAO(mockH2Handler);
        mockbackgroundCategoryDAO = new JDBCBackgroundCategoryDAO(mockH2Handler);


        /* Setup DAOs for all testing
         */
        logoDAO = new JDBCLogoDAO(H2EmbeddedHandler.getInstance());
        cameraDAO = new JDBCCameraDAO(H2EmbeddedHandler.getInstance());
        positionDAO = new JDBCPositionDAO(H2EmbeddedHandler.getInstance());
        pairLogoRelativeRectangleDAO = new JDCBPairLogoRelativeRectangleDAO(H2EmbeddedHandler.getInstance());
        pairCameraPositionDAO = new JDCBPairCameraPositionDAO(H2EmbeddedHandler.getInstance());
        profileDAO = new JDBCProfileDAO(H2EmbeddedHandler.getInstance());
        imageDAO = new JDBCImageDAO(H2EmbeddedHandler.getInstance());
        shootingDAO = new JDBCShootingDAO(H2EmbeddedHandler.getInstance());
        adminUserDAO = new JDBCAdminUserDAO(H2EmbeddedHandler.getInstance());
        backgroundCategoryDAO = new JDBCBackgroundCategoryDAO(H2EmbeddedHandler.getInstance());

        /*
        * Setup Services for all testing
         */
        profileService = new ProfileServiceImpl(
                new JDBCProfileDAO(H2EmbeddedHandler.getInstance()),
                new JDBCPositionDAO(H2EmbeddedHandler.getInstance()),
                new JDBCLogoDAO(H2EmbeddedHandler.getInstance()),
                new JDBCCameraDAO(H2EmbeddedHandler.getInstance()),
                new ShootingServiceImpl(shootingDAO),
                new CameraServiceImpl(cameraDAO));

        try {
            con.setAutoCommit(false);
            LOGGER.debug("Turn off AutoCommit before beginning testing");
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! AutoCommit couldn't be deactivated:" + e);
        }

        //Run delete.sql, insert.sql
        String deletePath=this.getClass().getResource("/sql/delete.sql").getPath();
        String insertPath=this.getClass().getResource("/sql/insert.sql").getPath();

        ResultSet rs= RunScript.execute(con, new FileReader(deletePath));
        if(rs!=null&&!rs.isClosed()) rs.close();
        rs= RunScript.execute(con, new FileReader(insertPath));
        if(rs!=null&&!rs.isClosed()) rs.close();

        /*
        * Setup Test objects for all testing
         */
        cameraA = new Camera(Integer.MIN_VALUE,"Apple iPhone 8","USBC","8","SN123456");
        positionA = new Position("Position A","/dev/null/positionA.jpg");
        positionB = new Position("Position B", "/dev/null/positionB.jpg");
        positionC = new Position("Position C", "/dev/null/positionC.jpg");
        position1000000 = new Position(1000000,"Position 1000000", "/dev/null/position1000000.jpg", false);
        logoA = new Logo("Logo A","/dev/null/logoA.jpg");
        logoB = new Logo("Logo B", "/dev/null/logoB.jpg");
        logoC = new Logo("Logo C", "/dev/null/logoC.jpg");
        logo1000000 = new Logo(1000000,"Logo 1000000", "/dev/null/logo1000000.jpg", false);
        camera1 = cameraDAO.read(1);
        camera2 = cameraDAO.read(2);
        position1 = positionDAO.read(1);
        position2 = positionDAO.read(2);
        logo1 = logoDAO.read(1);
        logo2 = logoDAO.read(2);
        relativeRectangleA = new RelativeRectangle(10.1, 10.2, 30.3, 30.4);
        relativeRectangleB = new RelativeRectangle(80.1, 80.2, 10.3, 10.4);
        relativeRectangleC = new RelativeRectangle(1, 2, 3, 4);
        pairCameraPositions = new ArrayList<>();
        pairCameraPositionA = new Profile.PairCameraPosition(camera1, position1, true);
        pairCameraPositionB = new Profile.PairCameraPosition(camera2, position2, false);
        pairCameraPositionC = new Profile.PairCameraPosition(cameraA, positionA, true);
        pairCameraPositions.add(pairCameraPositionA);
        pairCameraPositions.add(pairCameraPositionB);
        pairLogoRelativeRectangles = new ArrayList<>();
        pairLogoRelativeRectangleA = new Profile.PairLogoRelativeRectangle(logo1, relativeRectangleA);
        pairLogoRelativeRectangleB = new Profile.PairLogoRelativeRectangle(logo2, relativeRectangleB);
        pairLogoRelativeRectangleC = new Profile.PairLogoRelativeRectangle(logoA, relativeRectangleC);
        pairLogoRelativeRectangles.add(pairLogoRelativeRectangleA);
        pairLogoRelativeRectangles.add(pairLogoRelativeRectangleB);

        profileA = new Profile("Profile A");
        profileB = new Profile("Profile B",
                true,
                true,
                true,
                true,
                "/dev/null/watermarkB.jpg");

        profileC = new Profile(20,
                "Profile C",
                pairCameraPositions,
                pairLogoRelativeRectangles,
                new ArrayList<>(),
                true,
                true,
                true,
                true,
                "/dev/null/watermarkC.jpg",
                false);

        backgroundCategoryA = new Background.Category("Taufe");
        backgroundCategoryB = new Background.Category("Firmung");
        backgroundCategory10
                = new Background.Category(10,"Verlobung",false);
    }

    @After public void tearDown() throws Exception {
        // Good tests clean up their environment and reset to initial condition
        // Therefore a database session rollback is performed
        try {
            con.rollback();
            LOGGER.debug("Rollback after finished testing");
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Rollback couldn't be performed:" + e);
        }
    }
}


