package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.util.ImageHandler;
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
        ImageHandler imageHandler = new ImageHandler(new OpenCVLoader());
        setImageHandler(imageHandler);

        super.setUp();

        LogoWatermarkService impl = new LogoWatermarkServiceImpl(getMockProfileService(), imageHandler);
        setLogoWatermarkService(impl);
    }
}
