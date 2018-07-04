package org.openbooth.service;

import org.openbooth.util.ImageHandler;
import org.openbooth.util.OpenCVLoader;
import org.openbooth.util.TempStorageHandler;
import org.openbooth.util.exceptions.ImageHandlingException;
import org.openbooth.util.exceptions.StorageHandlingException;
import org.openbooth.service.exceptions.ServiceException;
import org.openbooth.service.impl.FilterServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests possible cases for FilterService
 */
public class FilterServiceTest{
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterServiceTest.class);

    private FilterService filterService;
    private ImageHandler imageHandler;
    private ShootingService mockShootingService = mock(ShootingService.class);


    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    private String srcImgPath;
    private BufferedImage srcImg;
    private String destImgPath;

    @Before
    public void setUp() throws  ServiceException, ImageHandlingException, StorageHandlingException{
        OpenCVLoader openCVLoader = new OpenCVLoader();
        imageHandler = new ImageHandler(openCVLoader);
        TempStorageHandler tempStorageHandler = new TempStorageHandler();

        srcImgPath = this.getClass().getResource("/images/test_logo_img.jpg").getPath();
        destImgPath = testFolder.getRoot().getPath() + "/test_logo_result.jpg";

        srcImg = imageHandler.openImage(srcImgPath);

        //when(mockShootingService.searchIsActive()).thenReturn(new Shooting(1,1,destImgPath,"",true));
       // when(mockShootingService.searchIsActive().getStorageDir()).thenReturn(destImgPath);

        filterService = new FilterServiceImpl(openCVLoader, imageHandler, tempStorageHandler);
    }

    @Test
    public void checkGaussianFilter() throws ServiceException, ImageHandlingException {

        BufferedImage filteredImage = filterService.filter("gaussian",srcImgPath);

        Mat source = Imgcodecs.imread(srcImgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(),source.cols(),source.type());
        Imgproc.GaussianBlur(source, destination,new Size(25,25), 0);
        BufferedImage image = imageHandler.convertMatToBufferedImg(destination);

        testImage(filteredImage,image);

    }
    @Test
    public void checkGrayscaleFilter() throws ServiceException, ImageHandlingException {

        BufferedImage filteredImage = filterService.filter("grayscale",srcImgPath);

        Mat mat = Imgcodecs.imread(srcImgPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat mat1 = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC1);
        Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);
        BufferedImage image = imageHandler.convertMatToBufferedImg(mat1);

        testImage(filteredImage,image);

    }
    @Test
    public void checkColorspaceFilter() throws ServiceException, ImageHandlingException {

        BufferedImage filteredImage = filterService.filter("colorspace",srcImgPath);

        Mat mat = Imgcodecs.imread(srcImgPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat mat1 = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC3);
        Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2HSV);
        BufferedImage image = imageHandler.convertMatToBufferedImg(mat1);

        testImage(filteredImage,image);

    }
    @Test
    public void checkSobelFilter() throws ServiceException, ImageHandlingException {

        BufferedImage filteredImage = filterService.filter("sobel",srcImgPath);

        int kernelSize = 3;
        Mat source = Imgcodecs.imread(srcImgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat destination = new Mat(source.rows(),source.cols(),source.type());
        Mat kernel = new Mat(kernelSize,kernelSize, CvType.CV_32F){
            {
                put(0,0,-1);
                put(0,1,0);
                put(0,2,1);

                put(1,0,-2);
                put(1,1,0);
                put(1,2,2);

                put(2,0,-1);
                put(2,1,0);
                put(2,2,1);
            }
        };

        Imgproc.filter2D(source, destination, -1, kernel);
        BufferedImage image = imageHandler.convertMatToBufferedImg(destination);

        testImage(filteredImage,image);

    }

    @Test
    public void checkTreshzeroFilter() throws ServiceException, ImageHandlingException {

        BufferedImage filteredImage = filterService.filter("threshzero",srcImgPath);

        Mat source = Imgcodecs.imread(srcImgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat destination = source;
        Imgproc.threshold(source,destination,127,255,Imgproc.THRESH_TOZERO);

        BufferedImage image = imageHandler.convertMatToBufferedImg(destination);

        testImage(filteredImage,image);

    }

    @Test
    public void checkTreshbinaryinvertFilter() throws ServiceException, ImageHandlingException {

        BufferedImage filteredImage = filterService.filter("threshbinaryinvert",srcImgPath);

        Mat source = Imgcodecs.imread(srcImgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat destination = source;
        Imgproc.threshold(source,destination,127,255,Imgproc.THRESH_BINARY_INV);

        BufferedImage image = imageHandler.convertMatToBufferedImg(destination);

        testImage(filteredImage,image);

    }
    @Test
    public void check_Not_Existing_Filter() throws ServiceException, ImageHandlingException {

        //If a not existing filter -> then it will return the original image.
        BufferedImage filteredImage = filterService.filter("notExistingFilter",srcImgPath);


        BufferedImage image = imageHandler.openImage(srcImgPath);

        testImage(filteredImage,image);

    }


    private void testImage(BufferedImage testImage, BufferedImage resultImage) {
        for(int y = 0; y < resultImage.getHeight(); y++){
            for(int x = 0; x < resultImage.getWidth(); x++){
                int testClr = testImage.getRGB(x,y);
                int resultClr = resultImage.getRGB(x,y);
                assertTrue("image comparison failed at pixel (" + x  + "," + y + ")",testClr == resultClr);
            }
        }
    }
}