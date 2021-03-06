package org.openbooth.service.impl;

import org.openbooth.storage.StorageHandler;
import org.openbooth.storage.exception.StorageException;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.OpenCVLoader;
import org.openbooth.util.exceptions.ImageHandlingException;
import org.openbooth.service.FilterService;
import org.openbooth.service.exceptions.ServiceException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilterServiceImpl implements FilterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterServiceImpl.class);


    private List<String> filterList;

    private ImageHandler imageHandler;

    private String storageDir;

    @Autowired
    public FilterServiceImpl(OpenCVLoader openCVLoader, ImageHandler imageHandler, StorageHandler storageHandler) throws StorageException {
        filterList = Arrays.asList("original","gaussian","grayscale","colorspace","sobel","threshzero","threshbinaryinvert");

        openCVLoader.loadLibrary();

        storageDir = storageHandler.getNewTemporaryFolderPath();

        this.imageHandler = imageHandler;
    }
    @Override
    public List<String> getExistingFilters(){
        return filterList;
    }

    @Override
    public Map<String,BufferedImage> getAllFilteredImages(String imgPath) throws ServiceException {
        LOGGER.debug("Entering getAllFilteredImages->imgPath->{}",imgPath);

        Map<String,BufferedImage> filteredImgPaths = new HashMap<>();
        filteredImgPaths.put("original", SwingFXUtils.fromFXImage(new Image("file:"+imgPath),null));
        for(String filterName: filterList)
            filteredImgPaths.put(filterName,filter(filterName,imgPath));

        return filteredImgPaths;
    }
    @Override
    public String resize(String imgPath,int width,int height) {
        LOGGER.debug("Entering resize->imgPath->{}",imgPath);

        Mat source = Imgcodecs.imread(imgPath,  Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat resizeimage = new Mat();
        Size sz = new Size(width,height);
        Imgproc.resize( source, resizeimage, sz );
        source.release();

        //exporting image name from imagePath
        String[] parts = imgPath.split("/");
        String imgFilterName = parts[parts.length-1].replace(".jpg","_preview.jpg");

        Imgcodecs.imwrite(storageDir+imgFilterName, resizeimage);
        resizeimage.release();
        return storageDir+imgFilterName;

    }

    @Override
    public BufferedImage filter(String filterName, String imgPath) throws ServiceException {
        BufferedImage filteredImage;
        switch (filterName) {

            case "gaussian":
                filteredImage = filterGaussian(imgPath);
                break;
            case "sobel":
                filteredImage = filterSobel(imgPath);
                break;
            case "colorspace":
                filteredImage = filterColorSpace(imgPath);
                break;
            case "grayscale":
                filteredImage = filterGrayScale(imgPath);
                break;
            case "threshzero":
                filteredImage = filterThreshZero(imgPath);
                break;
            case "threshbinaryinvert":
                filteredImage = filterThreshBinaryInvert(imgPath);
                break;
            default:
                try {
                    filteredImage = imageHandler.openImage(imgPath);
                } catch (ImageHandlingException e) {
                    throw new ServiceException(e);
                }
        }
        return filteredImage;
    }

    /**
     * changes given image with GAUSSIAN filter
     *
     * @param imgPath the path of image to filter
     * @return BufferedImage filtered image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    private BufferedImage filterGaussian(String imgPath) throws ServiceException{
        LOGGER.debug("Entering filterGaussian->imgPath->{}",imgPath);

        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat destination = new Mat(source.rows(),source.cols(),source.type());
        //Gaussian kernel size -> 15 - 15 -> sigmaX = 0
        Imgproc.GaussianBlur(source, destination,new Size(25,25), 0);
        source.release();

        BufferedImage image;
        try {
            image = imageHandler.convertMatToBufferedImg(destination);
        } catch (ImageHandlingException e) {
            throw new ServiceException(e);
        }
        destination.release();
        return image;
    }

    /**
     * changes given image with GRAYSCALE filter
     *
     * @param imgPath the path of image to filter
     * @return BufferedImage filtered image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    private BufferedImage filterGrayScale(String imgPath) throws ServiceException{
        LOGGER.debug("Entering filterGrayScale->imgPath->{}",imgPath);


        Mat mat = Imgcodecs.imread(imgPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat mat1 = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC1);
        Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);
        mat.release();


        BufferedImage image;
        try {
            image = imageHandler.convertMatToBufferedImg(mat1);
        } catch (ImageHandlingException e) {
            throw new ServiceException(e);
        }
        mat1.release();

        return image;
    }

    /**
     * changes given image with COLORSPACE filter
     *
     * @param imgPath the path of image to filter
     * @return BufferedImage filtered image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    private BufferedImage filterColorSpace(String imgPath) throws ServiceException{
        LOGGER.debug("Entering filterColorSpace->imgPath->{}",imgPath);

        Mat mat = Imgcodecs.imread(imgPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat mat1 = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC3);
        Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2HSV);
        mat.release();

        BufferedImage image;
        try {
            image = imageHandler.convertMatToBufferedImg(mat1);
        } catch (ImageHandlingException e) {
            throw new ServiceException(e);
        }
        mat1.release();

        return image;

    }

    /**
     * changes given image with SOBEL filter
     *
     * @param imgPath the path of image to filter
     * @return BufferedImage filtered image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    private BufferedImage filterSobel(String imgPath) throws ServiceException{
        LOGGER.debug("Entering filterSobel->imgPath->{}",imgPath);
        int kernelSize = 3;
        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat destination = new Mat(source.rows(),source.cols(),source.type());
        Mat kernel = new Mat(kernelSize,kernelSize, CvType.CV_32F);

        kernel.put(0,0,-1);
        kernel.put(0,1,0);
        kernel.put(0,2,1);

        kernel.put(1,0,-2);
        kernel.put(1,1,0);
        kernel.put(1,2,2);

        kernel.put(2,0,-1);
        kernel.put(2,1,0);
        kernel.put(2,2,1);



        Imgproc.filter2D(source, destination, -1, kernel);
        source.release();

        BufferedImage image;
        try {
            image = imageHandler.convertMatToBufferedImg(destination);
        } catch (ImageHandlingException e) {
            LOGGER.error("filterSobel -", e);
            throw new ServiceException(e);
        }
        destination.release();

        return image;
    }

    /**
     * changes given image with THRESHZERO filter
     *
     * @param imgPath the path of image to filter
     * @return BufferedImage filtered image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    private BufferedImage filterThreshZero(String imgPath) throws ServiceException{
        LOGGER.debug("Entering filterThreshZero->imgPath->{}",imgPath);

        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(),source.cols(),source.type());
        Imgproc.threshold(source,destination,127,255,Imgproc.THRESH_TOZERO);

        BufferedImage image;
        try {
            image = imageHandler.convertMatToBufferedImg(destination);
        } catch (ImageHandlingException e) {
            throw new ServiceException(e);
        }
        source.release();
        destination.release();

        return image;
    }

    /**
     * changes given image with THRESHBINARYINVERT filter
     *
     * @param imgPath the path of image to filter
     * @return BufferedImage filtered image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    private BufferedImage filterThreshBinaryInvert(String imgPath) throws ServiceException{
        LOGGER.debug("Entering filterThreshBinaryInvert->imgPath->{}",imgPath);

        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(),source.cols(),source.type());
        Imgproc.threshold(source,destination,127,255,Imgproc.THRESH_BINARY_INV);

        BufferedImage image;
        try {
            image = imageHandler.convertMatToBufferedImg(destination);
        } catch (ImageHandlingException e) {
            throw new ServiceException(e);
        }
        source.release();
        destination.release();
        return image;
    }
}
