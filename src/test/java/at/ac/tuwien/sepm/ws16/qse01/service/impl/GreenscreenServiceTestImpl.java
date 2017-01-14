package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.util.OpenCVLoader;
import at.ac.tuwien.sepm.util.exceptions.LibraryLoadingException;
import at.ac.tuwien.sepm.ws16.qse01.service.GreenscreenServiceTest;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.junit.Before;

/**
 * Created by fabian on 04.01.17.
 */
public class GreenscreenServiceTestImpl extends GreenscreenServiceTest {

    private OpenCVLoader openCVLoader;
    private ImageHandler imageHandler;

    public GreenscreenServiceTestImpl() throws ServiceException, LibraryLoadingException{
        this.openCVLoader = new OpenCVLoader();
        this.imageHandler = new ImageHandler(openCVLoader);
    }

    @Before
    public void setUp() throws ServiceException, LibraryLoadingException{
        setGreenscreenService(new GreenscreenServiceImpl(openCVLoader, imageHandler));
        setImageHandler(imageHandler);
        super.setUp();
    }
}
