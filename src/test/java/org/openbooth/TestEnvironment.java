package org.openbooth;

import org.openbooth.dao.impl.*;
import org.openbooth.service.ProfileService;
import org.openbooth.service.impl.BackgroundServiceImpl;
import org.openbooth.entities.*;
import org.openbooth.service.impl.ProfileServiceImpl;
import org.openbooth.service.impl.ShootingServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openbooth.dao.*;
import org.openbooth.service.impl.CameraServiceImpl;
import org.openbooth.util.dbhandler.DBHandler;
import org.openbooth.util.dbhandler.prep.DataPrepper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Test Environment
 * provides setup (Database connections and mocks) for all DAO tests
 */

@ComponentScan("org.openbooth")
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class TestEnvironment {


    private static ApplicationContext applicationContext = null;

    protected LogoDAO logoDAO,mockLogoDAO;
    protected CameraDAO cameraDAO,mockCameraDAO;
    protected PositionDAO positionDAO,mockPositionDAO;
    protected PairLogoRelativeRectangleDAO pairLogoRelativeRectangleDAO,mockPairLogoRelativeRectangleDAO;
    protected PairCameraPositionDAO pairCameraPositionDAO,mockPairCameraPositionDAO;
    protected ProfileDAO profileDAO,mockProfileDAO;
    protected ImageDAO imageDAO,mockImageDAO;
    protected ShootingDAO shootingDAO,mockShootingDAO;
    protected BackgroundCategoryDAO backgroundCategoryDAO,mockbackgroundCategoryDAO;
    protected BackgroundDAO backgroundDAO, mockBackgroundDAO;
    protected ProfileService profileService;
    protected AdminUserDAO adminUserDAO;



    protected Camera camera1,camera2,camera3,cameraA,cameraB,cameraC,camera1000000;
    protected Position position1,position2,positionA,positionB,positionC,position1000000;
    protected Logo logo1,logo2,logoA,logoB,logoC,logo1000000;
    protected RelativeRectangle relativeRectangleA,relativeRectangleB,relativeRectangleC,relativeRectangleD;
    protected Profile.PairCameraPosition pairCameraPositionA,pairCameraPositionB,pairCameraPositionC,pairCameraPosition1000000;
    protected Profile.PairLogoRelativeRectangle pairLogoRelativeRectangleA,pairLogoRelativeRectangleB,pairLogoRelativeRectangleC,pairLogoRelativeRectangle1000000;
    protected List<Profile.PairCameraPosition> pairCameraPositions;
    protected List<Profile.PairLogoRelativeRectangle> pairLogoRelativeRectangles;
    protected List<Background.Category> categories;
    protected Profile profileA,profileB,profileC,profile1,profile2;
    protected Background.Category backgroundCategory1,backgroundCategory2,backgroundCategory3,backgroundCategory4;

    protected ApplicationContext getApplicationContext(){
        if(applicationContext == null) applicationContext = new AnnotationConfigApplicationContext(TestEnvironment.class);
        return applicationContext;
    }



    @Before public void setUp() throws Exception
    {
        DBHandler dbHandler = getApplicationContext().getBean(DBHandler.class);
        Connection con = dbHandler.getConnection();


        /* Setup DAOs for all testing
         */
        logoDAO = applicationContext.getBean(LogoDAO.class);
        cameraDAO = applicationContext.getBean(CameraDAO.class);
        positionDAO = applicationContext.getBean(PositionDAO.class);
        pairLogoRelativeRectangleDAO = applicationContext.getBean(PairLogoRelativeRectangleDAO.class);
        pairCameraPositionDAO = applicationContext.getBean(PairCameraPositionDAO.class);
        profileDAO = applicationContext.getBean(ProfileDAO.class);
        imageDAO = applicationContext.getBean(ImageDAO.class);
        shootingDAO = applicationContext.getBean(ShootingDAO.class);
        adminUserDAO = applicationContext.getBean(AdminUserDAO.class);
        backgroundCategoryDAO = applicationContext.getBean(BackgroundCategoryDAO.class);

        /*
        * Setup Services for all testing
         */
        profileService = new ProfileServiceImpl(
                new JDBCProfileDAO(dbHandler),
                new JDBCPositionDAO(dbHandler),
                new JDBCLogoDAO(dbHandler),
                new JDBCCameraDAO(dbHandler),
                new ShootingServiceImpl(shootingDAO),
                new CameraServiceImpl(cameraDAO),
                new BackgroundServiceImpl(backgroundDAO,backgroundCategoryDAO)
                );


        //Run delete.sql, insert.sql
        DataPrepper.deleteTestData(con);
        DataPrepper.insertTestData(con);


        /*
        * Setup Test objects for all testing
         */
        cameraA = new Camera(Integer.MIN_VALUE,"Apple iPhone 8","USBC","8","SN123456");
        cameraB = new Camera(Integer.MIN_VALUE,"Camera B", "B", "Bronica B", "42");
        cameraC = new Camera(Integer.MIN_VALUE,"Camera C", "C", "Canon C", "999");
        camera1000000 = new Camera(1000000,"Camera 1000000", "ZZZZZZZ", "OneMillion OM" , "XYZ" );
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
        camera3 = cameraDAO.read(3);
        position1 = positionDAO.read(1);
        position2 = positionDAO.read(2);
        logo1 = logoDAO.read(1);
        logo2 = logoDAO.read(2);
        relativeRectangleA = new RelativeRectangle(10.1, 10.2, 30.3, 30.4);
        relativeRectangleB = new RelativeRectangle(80.1, 80.2, 10.3, 10.4);
        relativeRectangleC = new RelativeRectangle(1, 2, 3, 4);
        relativeRectangleD = new RelativeRectangle(20,21,22,23);

        pairCameraPositions = new ArrayList<>();
        pairCameraPositionA = new Profile.PairCameraPosition(camera1, position1, true);
        pairCameraPositionB = new Profile.PairCameraPosition(camera2, position2, false);
        pairCameraPositionC = new Profile.PairCameraPosition(camera3, positionA, true);
        pairCameraPosition1000000
                = new Profile.PairCameraPosition(1000000,3,camera1,position1,false);
        pairCameraPositions.add(pairCameraPositionA);
        pairCameraPositions.add(pairCameraPositionB);
        pairLogoRelativeRectangles = new ArrayList<>();
        pairLogoRelativeRectangleA = new Profile.PairLogoRelativeRectangle(logo1, relativeRectangleA);
        pairLogoRelativeRectangleB = new Profile.PairLogoRelativeRectangle(logo2, relativeRectangleB);
        pairLogoRelativeRectangleC = new Profile.PairLogoRelativeRectangle(logoA, relativeRectangleC);
        pairLogoRelativeRectangle1000000
                = new Profile.PairLogoRelativeRectangle(1000000,2,logo1,relativeRectangleD);
        pairLogoRelativeRectangles.add(pairLogoRelativeRectangleA);
        pairLogoRelativeRectangles.add(pairLogoRelativeRectangleB);


        categories = new ArrayList<>();
        backgroundCategory1 = backgroundCategoryDAO.read(1);
        backgroundCategory2 = backgroundCategoryDAO.read(2);
        backgroundCategory3 = backgroundCategoryDAO.read(3);
        backgroundCategory4 = backgroundCategoryDAO.read(4);

        categories.add(backgroundCategory1);
        categories.add(backgroundCategory2);
        categories.add(backgroundCategory3);
        categories.add(backgroundCategory4);


        profile1 = profileService.get(1);
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
    }

    @After public void tearDown() throws Exception {

    }
}


