package org.openbooth.service.imageprocessing;

import org.junit.Ignore;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.OpenCVLoader;
import org.openbooth.util.exceptions.ImageHandlingException;
import org.openbooth.entities.Camera;
import org.openbooth.entities.Position;
import org.openbooth.entities.Profile;
import org.openbooth.gui.RefreshManager;
import org.openbooth.gui.ShotFrameController;
import org.openbooth.service.exceptions.ServiceException;
import org.openbooth.service.imageprocessing.impl.ImageProcessorImpl;
import org.junit.Before;
import org.junit.Test;
import org.openbooth.service.*;

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
    private String resultImgPath = "/images/result/test_imageprocessor_img_mirrored.jpg";
    private BufferedImage resultImage;

    @Before
    public void setUp() throws ServiceException, ImageHandlingException {
        imageHandler = new ImageHandler(new OpenCVLoader());

        position = new Position("testposition");
        camera = new Camera(1, "testcamera", "testport", "testmodel", "testserialnumber");

        pairCameraPosition= new Profile.PairCameraPosition(camera, position, false);
        when(mockProfileService.getCameraOfPositionOfProfile(position)).thenReturn(camera);
        when(mockProfileService.getPairCameraPosition(camera)).thenReturn(pairCameraPosition);


        InputStream testImageStream = this.getClass().getResourceAsStream(testImgPath);
        resultImage = imageHandler.openImage(testImageStream);
        when(mockImageHandler.openImage(testImgPath)).thenReturn(resultImage);

        imageProcessor = new ImageProcessorImpl(mockShootingService, mockProfileService, mockImageService, mockLogoWatermarkService, mockFilterService, mockGreenscreenService, mockImageHandler, mockRefreshManager);
        imageProcessor.setShotFrameController(mockShotFrameController);
        imageProcessor.setPosition(position);
    }


    @Ignore
    @Test
    public void testProcessPreviewWithoutFilterOrGreenscreen() throws ServiceException{

        imageProcessor.processPreview(testImgPath);
        verify(mockShotFrameController).refreshShot(resultImage);

    }

}
