package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Logo;
import at.ac.tuwien.sepm.ws16.qse01.entities.RelativeRectangle;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


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
 * This class is used for testing the LogoWatermarkService
 */
public abstract class LogoWatermarkServiceTest {


    private LogoWatermarkService logoWatermarkService;
    private ProfileService mockProfileService = mock(ProfileService.class);
    private Logo logo1;
    private Logo logo2;
    private RelativeRectangle position1;
    private RelativeRectangle position2;
    private Logo watermark;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private String logoPath1;
    private String logoPath2;
    private String watermarkPath;
    private String srcImgPath;
    private String destImgPath;
    private String destFileFormat;

    protected void setLogoWatermarkService(LogoWatermarkService logoWatermarkService){
        this.logoWatermarkService = logoWatermarkService;
    }

    protected ProfileService getMockProfileService(){
        return mockProfileService;
    }

    @Before
    public void setUp() throws  ServiceException{
        logoPath1 = this.getClass().getResource("/logos/logofamp.jpg").getPath();
        logoPath2 = logoPath1;
        watermarkPath = this.getClass().getResource("/watermark/watermark.png").getPath();

        srcImgPath = this.getClass().getResource("/images/test_logo_img.jpg").getPath();
        destImgPath = testFolder.getRoot().getPath() + "/test_logo_result.jpg";
        destFileFormat = "jpg";


        logo1 = new Logo("testlogo1", logoPath1);
        logo2 = new Logo("testlogo2", logoPath2);

        watermark = new Logo("testwatermark", watermarkPath);

        position1 = new RelativeRectangle(10D, 10D, 30D, 20D);
        position2 = new RelativeRectangle(60D, 70D, 30D, 20D);


        List<Logo> logosList =Arrays.asList(logo1, logo2);

        when(mockProfileService.getAllLogosOfProfile()).thenReturn(logosList);
        when(mockProfileService.getRelativeRectangleOfLogoOfProfile(logo1)).thenReturn(position1);
        when(mockProfileService.getRelativeRectangleOfLogoOfProfile(logo2)).thenReturn(position2);
        when(mockProfileService.getProfileWaterMark()).thenReturn(watermark);

    }

    @Test
    public void addLogosWithValidParameters() throws ServiceException, IOException{

        logoWatermarkService.addLogosCreateNewImage(srcImgPath, destImgPath);

        String testImagePath = this.getClass().getResource("/images/result/test_logo_normal.jpg").getPath();

        testImage(testImagePath, destImgPath);

    }

    @Test (expected = ServiceException.class)
    public void addLogosWithInvalidSourceImage() throws ServiceException{
        logoWatermarkService.addLogosCreateNewImage("/this/is/not/a/valid/path/image.dnf", destImgPath);
    }

    @Test (expected = ServiceException.class)
    public void addLogosWithInvalidDestImagePath() throws ServiceException{
        logoWatermarkService.addLogosCreateNewImage(srcImgPath, "/this/is/no/path/image.xxx");
    }

    @Test (expected = ServiceException.class)
    public void addLogosWithInvalidLogoDimensions() throws ServiceException{
        position1.setHeight(-100D);
        position1.setWidth(-100D);
        logoWatermarkService.addLogosCreateNewImage(srcImgPath, destImgPath);
    }

    @Test (expected = ServiceException.class)
    public void addLogosWithInvalidLogoPath() throws ServiceException{
        logo1.setPath("/this/is/no/valid/path");
        logoWatermarkService.addLogosCreateNewImage(srcImgPath, destImgPath);
    }

    @Test
    public void addLogosWithScaledHeight() throws ServiceException, IOException{
        position1.setWidth(50D);
        position1.setHeight(-1D);
        logoWatermarkService.addLogosCreateNewImage(srcImgPath, destImgPath);

        String testImagePath = this.getClass().getResource("/images/result/test_logo_scaled_height.jpg").getPath();

        testImage(testImagePath, destImgPath);
    }

    @Test
    public void addLogosWithScaledWidth() throws ServiceException, IOException{
        position1.setHeight(40D);
        position1.setWidth(-1D);
        logoWatermarkService.addLogosCreateNewImage(srcImgPath, destImgPath);

        String testImagePath = this.getClass().getResource("/images/result/test_logo_scaled_width.jpg").getPath();

        testImage(testImagePath, destImgPath);
    }


    @Test
    public void addWatermarkWithValidParameters() throws ServiceException, IOException{
        logoWatermarkService.addWatermarkCreateNewImage(srcImgPath, destImgPath);

        String testImagePath = this.getClass().getResource("/images/result/test_watermark_normal.jpg").getPath();

        testImage(testImagePath, destImgPath);
    }

    @Test (expected = ServiceException.class)
    public void addWatermarkWithInvalidSourceImage() throws ServiceException{
        logoWatermarkService.addWatermarkCreateNewImage("/this/is/no/path", destImgPath);
    }

    @Test (expected = ServiceException.class)
    public void addWatermarkWithInvalidDestImagePath() throws ServiceException{
        logoWatermarkService.addWatermarkCreateNewImage(srcImgPath, "/this/is/no/path");
    }

    @Test (expected = ServiceException.class)
    public void addWatermarkWithInvalidWatermarkPath() throws ServiceException{
        watermark.setPath("/this/is/no/path");
        logoWatermarkService.addWatermarkCreateNewImage(srcImgPath, destImgPath);
    }



    @Test
    public void getPreviewForLogoWithValidParameters() throws ServiceException, IOException{
        BufferedImage img = logoWatermarkService.getPreviewForLogo(logo1, position1, 1024, 768);
        ImageIO.write(img, destFileFormat, new File(destImgPath));

        String testImage = this.getClass().getResource("/images/result/test_preview_logo.jpg").getPath();

        testImage(testImage, destImgPath);
    }

    @Test (expected = ServiceException.class)
    public void getPreviewForLogoWithInvalidLogoPath() throws ServiceException{
        logo1.setPath("/this/is/no/path");
        logoWatermarkService.getPreviewForLogo(logo1, position1, 1024, 768);
    }

    @Test (expected = ServiceException.class)
    public void getPreviewForLogoWithInvalidLogoDimensions() throws ServiceException{
        position1.setWidth(-100);
        position1.setHeight(-100);

        logoWatermarkService.getPreviewForLogo(logo1, position1, 1024, 768);
    }

    @Test (expected = ServiceException.class)
    public void getPreviewForLogoWithInvalidImageDimensions() throws ServiceException{
        logoWatermarkService.getPreviewForLogo(logo1, position1, -100, -200);
    }


    @Test
    public void getPreviewForMultipleLogosWithValidParameters() throws ServiceException, IOException{
        List<RelativeRectangle> positionList = Arrays.asList(position1, position2);
        List<Logo> logoList = Arrays.asList(logo1, logo2);

        BufferedImage img = logoWatermarkService.getPreviewForMultipleLogos(logoList, positionList, 1024, 768);
        ImageIO.write(img, destFileFormat, new File(destImgPath));

        String testImage = this.getClass().getResource("/images/result/test_preview_multiple_logos.jpg").getPath();

        testImage(testImage, destImgPath);

    }

    @Test (expected = ServiceException.class)
    public void getPreviewForMultipleLogosWithInvalidLogoPath() throws ServiceException{
        logo1.setPath("/this/is/no/path");
        List<RelativeRectangle> positionList = Arrays.asList(position1, position2);
        List<Logo> logoList = Arrays.asList(logo1, logo2);

        logoWatermarkService.getPreviewForMultipleLogos(logoList, positionList, 1024, 768);
    }

    @Test (expected = ServiceException.class)
    public void getPreviewForMultipleLogosWithInvalidLogoDimension() throws ServiceException{
        position1.setHeight(-100);
        position2.setWidth(-100);

        List<RelativeRectangle> positionList = Arrays.asList(position1, position2);
        List<Logo> logoList = Arrays.asList(logo1, logo2);

        logoWatermarkService.getPreviewForMultipleLogos(logoList, positionList, 1024, 768);
    }

    @Test (expected = ServiceException.class)
    public void getPreviewForMultipleLogosWithInvalidImageDimensions() throws ServiceException{
        List<RelativeRectangle> positionList = Arrays.asList(position1, position2);
        List<Logo> logoList = Arrays.asList(logo1, logo2);

        logoWatermarkService.getPreviewForMultipleLogos(logoList, positionList, -100, -200);
    }

    @Test (expected = ServiceException.class)
    public void getPreviewForMultipleLogosWithInvalidLists() throws ServiceException{
        List<RelativeRectangle> positionList = Arrays.asList(position1);
        List<Logo> logoList = Arrays.asList(logo1, logo2);

        logoWatermarkService.getPreviewForMultipleLogos(logoList, positionList, 1024, 768);
    }



    @Test
    public void calculateRelativeWidthWithValidParameters() throws ServiceException{
        double relativeWidth = logoWatermarkService.calculateRelativeWidth(15, logo1, 1024, 768);
        assertTrue(Math.round(relativeWidth) == 22);
    }

    @Test (expected = ServiceException.class)
    public void calculateRelativeWidthWithInvalidLogoPath() throws ServiceException{
        logo1.setPath("/this/is/no/path");
        logoWatermarkService.calculateRelativeWidth(15, logo1, 1024, 768);
    }

    @Test (expected = ServiceException.class)
    public void calculateRelativeWidthWithInvalidHeight() throws ServiceException{
        logoWatermarkService.calculateRelativeWidth(-100, logo1, 1024, 768);
    }

    @Test (expected = ServiceException.class)
    public void calculateRelativeWidthWithInvalidImageDimensions() throws ServiceException{
        logoWatermarkService.calculateRelativeWidth(15, logo1, -100, -200);
    }




    @Test
    public void calculateRelativeHeightWithValidParameters() throws ServiceException{
        double relativeHeight = logoWatermarkService.calculateRelativeHeight(22, logo1, 1024, 768);
        assertTrue(Math.round(relativeHeight) == 15);
    }

    @Test (expected = ServiceException.class)
    public void calculateRelativeHeightWithInvalidLogoPath() throws ServiceException{
        logo1.setPath("/this/is/no/path");
        logoWatermarkService.calculateRelativeHeight(22, logo1, 1024, 768);
    }

    @Test (expected = ServiceException.class)
    public void calculateRelativeHeightWithInvalidWidth() throws ServiceException{
        logoWatermarkService.calculateRelativeHeight(-100, logo1, 1024, 768);
    }

    @Test (expected = ServiceException.class)
    public void calculateRelativeHeightWithInvalidImageDimensions() throws ServiceException{
        logoWatermarkService.calculateRelativeHeight(15, logo1, -100, -200);
    }


    private void testImage(String testImagePath, String resultImagePath) throws  IOException{
        BufferedImage testImage = ImageIO.read(new File(testImagePath));

        BufferedImage resultImage = ImageIO.read(new File(resultImagePath));

        for(int y = 0; y < resultImage.getHeight(); y++){
            for(int x = 0; x < resultImage.getWidth(); x++){
                int testClr = testImage.getRGB(x,y);
                int resultClr = resultImage.getRGB(x,y);
                assertTrue("image comparison failed at pixel (" + x  + "," + y + ")",testClr == resultClr);
            }
        }
    }



}
