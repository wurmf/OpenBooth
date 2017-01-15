package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.util.OpenCVLoader;
import at.ac.tuwien.sepm.util.exceptions.ImageHandlingException;
import at.ac.tuwien.sepm.util.exceptions.LibraryLoadingException;
import at.ac.tuwien.sepm.ws16.qse01.service.FilterService;
import at.ac.tuwien.sepm.ws16.qse01.service.FilterServiceTest;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.junit.Before;

/**
 * Created by macdnz on 13.01.17.
 */
public class FilterServiceTestImpl extends FilterServiceTest {


    @Before
    public void setUp() throws ServiceException, ImageHandlingException, LibraryLoadingException {
        ImageHandler imageHandler = new ImageHandler(new OpenCVLoader());
        setImageHandler(imageHandler);

        super.setUp();


        FilterService impl = new FilterServiceImpl(getMockShootingService(),new OpenCVLoader(),imageHandler);

        setFilterService(impl);



    }
}
