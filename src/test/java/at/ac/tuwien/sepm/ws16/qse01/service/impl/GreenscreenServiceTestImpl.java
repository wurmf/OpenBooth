package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.service.GreenscreenServiceTest;
import org.junit.Before;

/**
 * Created by fabian on 04.01.17.
 */
public class GreenscreenServiceTestImpl extends GreenscreenServiceTest {

    @Before
    public void setUp(){
        setGreenscreenService(new GreenscreenServiceImpl());
    }
}
