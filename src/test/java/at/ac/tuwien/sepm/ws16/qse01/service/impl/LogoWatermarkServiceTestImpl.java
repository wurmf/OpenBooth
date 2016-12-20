package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.entities.Logo;
import at.ac.tuwien.sepm.ws16.qse01.entities.RelativeRectangle;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
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

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    protected String logoPath1;
    protected String logoPath2;
    protected String watermarkPath = null;
    protected String srcImgPath;
    protected String logoDestImgPath;
    protected String compareImagePath;
    protected String watermarkDestImgPath = "/images/img1_with_watermark.jpg";

    @Before
    public void setUp(){
        logoPath1 = this.getClass().getResource("/logos/logofamp.jpg").getPath();
        logoPath2 = logoPath1;

        srcImgPath = this.getClass().getResource("/images/img1.jpg").getPath();
        compareImagePath = this.getClass().getResource("/images/img1_with_logo_test.jpg").getPath();
        logoDestImgPath = testFolder.getRoot().getPath() + "/image1_with_logo.jpg";

        when(mockLogo1.getPath()).thenReturn(logoPath1);
        when(mockLogo2.getPath()).thenReturn(logoPath2);

        when(mockWatermark.getPath()).thenReturn(watermarkPath);

    }

    @Test
    public void addLogosWithValidParameters() throws ServiceException, IOException{
        when(mockPosition1.getX()).thenReturn(10D);
        when(mockPosition1.getY()).thenReturn(10D);
        when(mockPosition1.getWidth()).thenReturn(30D);
        when(mockPosition1.getHeight()).thenReturn(20D);

        when(mockPosition2.getX()).thenReturn(60D);
        when(mockPosition2.getY()).thenReturn(70D);
        when(mockPosition2.getWidth()).thenReturn(30D);
        when(mockPosition2.getHeight()).thenReturn(20D);

        List<Logo> mockedLogosList =Arrays.asList(mockLogo1, mockLogo2);

        when(mockProfileService.getAllLogosOfProfile()).thenReturn(mockedLogosList);
        when(mockProfileService.getRelativeRectangleOfLogoOfProfile(mockLogo1)).thenReturn(mockPosition1);
        when(mockProfileService.getRelativeRectangleOfLogoOfProfile(mockLogo2)).thenReturn(mockPosition2);

        LogoWatermarkService logoWatermarkService = new LogoWatermarkServiceImpl(mockProfileService);

        logoWatermarkService.addLogosCreateNewImage(srcImgPath, logoDestImgPath);

        BufferedImage testImage = ImageIO.read(new File(compareImagePath));

        BufferedImage resultImage = ImageIO.read(new File(logoDestImgPath));

        for(int y = 0; y < resultImage.getHeight(); y++){
            for(int x = 0; x < resultImage.getWidth(); x++){
                int testClr = testImage.getRGB(x,y);
                int resultClr = resultImage.getRGB(x,y);
                assertTrue("image comparison failed at pixel (" + x  + "," + y + ")",testClr == resultClr);
            }
        }
    }

}
