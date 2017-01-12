package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.util.ImageHelper;
import at.ac.tuwien.sepm.util.OpenCVLoader;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkServiceTest;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.junit.Before;

/**
 * This class implements the tests for the LogoWatermarkService for LogoWatermarkServiceImpl
 */
public class LogoWatermarkServiceTestImpl extends LogoWatermarkServiceTest {

    @Before
    public void setUp() throws ServiceException{
        super.setUp();
        LogoWatermarkService impl = new LogoWatermarkServiceImpl(getMockProfileService(), new ImageHelper(new OpenCVLoader()));
        setLogoWatermarkService(impl);
    }
}
