package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.entities.Logo;
import at.ac.tuwien.sepm.ws16.qse01.entities.RelativeRectangle;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by fabian on 12.12.16.
 */
public class LogoWatermarkServiceTestImpl {


    protected ProfileService mockProfileService = mock(ProfileService.class);
    protected Logo mockLogo1 = mock(Logo.class);
    protected Logo mockLogo2 = mock(Logo.class);
    protected RelativeRectangle mockPosition1 = mock(RelativeRectangle.class);
    protected RelativeRectangle mockPosition2 = mock(RelativeRectangle.class);
    protected Logo mockWatermark = mock(Logo.class);

    protected String logoPath1 = System.getProperty("user.dir") + "/src/test/resources/logos/logofamp.jpg";
    protected String logoPath2 = System.getProperty("user.dir") + "/src/test/resources/logos/logofamp.jpg";
    protected String watermarkPath = null;
    protected String srcImgPath = System.getProperty("user.dir") + "/src/test/resources/images/img1.jpg";
    protected String logoDestImgPath = System.getProperty("user.dir") + "src/test/resources/images/img1_with_logo.jpg";
    protected String watermarkDestImgPath = "/images/img1_with_watermark.jpg";

    @Before
    public void setUp(){
        when(mockLogo1.getPath()).thenReturn(logoPath1);
        when(mockLogo2.getPath()).thenReturn(logoPath2);

        when(mockWatermark.getPath()).thenReturn(watermarkPath);

    }

    @Test
    public void addLogosWithValidParameters() throws ServiceException{
        when(mockPosition1.getX()).thenReturn(10D);
        when(mockPosition1.getY()).thenReturn(10D);
        when(mockPosition1.getWidth()).thenReturn(30D);
        when(mockPosition1.getHeight()).thenReturn(20D);

        List<Logo> mockedLogosList =Arrays.asList(mockLogo1);

        when(mockProfileService.getAllLogosOfProfile()).thenReturn(mockedLogosList);
        when(mockProfileService.getRelativeRectangleOfLogoOfProfile(mockLogo1)).thenReturn(mockPosition1);

        LogoWatermarkService logoWatermarkService = new LogoWatermarkServiceImpl(mockProfileService);

        logoWatermarkService.addLogosCreateNewImage(srcImgPath, logoDestImgPath);
    }

}
