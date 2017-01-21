package at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.util.OpenCVLoader;
import at.ac.tuwien.sepm.util.exceptions.ImageHandlingException;
import at.ac.tuwien.sepm.util.exceptions.LibraryLoadingException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.gui.RefreshManager;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.*;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.impl.ImageProcessorImpl;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This class provides tests for ImageProcessorImpl
 */
public class ImageProcessorImplTest {

    private ImageProcessorImpl imageProcessor;

    private ShotFrameController mockShotFrameController = mock(ShotFrameController.class);

    private ShootingService mockShootingService = mock(ShootingService.class);
    private ProfileService mockProfileService = mock(ProfileService.class);
    private ImageService mockImageService = mock(ImageService.class);

    private LogoWatermarkService mockLogoWatermarkService = mock(LogoWatermarkService.class);
    private FilterService mockFilterService = mock(FilterService.class);
    private GreenscreenService mockGreenscreenService = mock(GreenscreenService.class);

    private RefreshManager mockRefreshManager = mock(RefreshManager.class);

    private ImageHandler mockImageHandler = mock(ImageHandler.class);

    private Position position;
    private Camera camera;
    private Profile.PairCameraPosition pairCameraPosition;


    private ImageHandler imageHandler;
    private String testImgPath = "/images/test_imageprocessor_img.jpg";
    private BufferedImage testImage;

    @Before
    public void setUp() throws ServiceException, ImageHandlingException, LibraryLoadingException{
        imageHandler = new ImageHandler(new OpenCVLoader());

        position = new Position("testposition");
        camera = new Camera(1, "testcamera", "testport", "testmodel", "testserialnumber");

        pairCameraPosition= new Profile.PairCameraPosition(camera, position, false);
        when(mockProfileService.getCameraOfPositionOfProfile(position)).thenReturn(camera);
        when(mockProfileService.getPairCameraPosition(camera)).thenReturn(pairCameraPosition);


        InputStream testImageStream = this.getClass().getResourceAsStream(testImgPath);
        testImage = imageHandler.openImage(testImageStream);
        when(mockImageHandler.openImage(testImgPath)).thenReturn(testImage);

        imageProcessor = new ImageProcessorImpl(mockShotFrameController, mockShootingService, mockProfileService, mockImageService, mockLogoWatermarkService, mockFilterService, mockGreenscreenService, position, mockImageHandler, mockRefreshManager);
    }


    @Test
    public void testProcessPreviewWithoutFilterOrGreenscreen() throws ServiceException{

        imageProcessor.processPreview(testImgPath);
        verify(mockShotFrameController).refreshShot(testImage);

    }

}
