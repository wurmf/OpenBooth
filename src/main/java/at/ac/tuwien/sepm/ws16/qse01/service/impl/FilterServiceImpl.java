package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.util.ImageHelper;
import at.ac.tuwien.sepm.util.OpenCVLoader;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.FilterService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
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
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilterServiceImpl implements FilterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterServiceImpl.class);

    private BufferedImage bufferedImage;
    private String storageDir;
    private Shooting activeShooting;

    private List<String> filterList;

    private ImageHelper imageHelper;

    @Autowired
    public FilterServiceImpl(ShootingDAO shootingDAO, OpenCVLoader openCVLoader, ImageHelper imageHelper) throws ServiceException {
        filterList = Arrays.asList("original","gaussian","grayscale","colorspace","sobel","threshzero","threshbinaryinvert");

        try {
            activeShooting = shootingDAO.searchIsActive();
        } catch (PersistenceException e) {
            throw new ServiceException("Error: "+e.getMessage());
        }

        openCVLoader.loadLibrary();

        this.imageHelper = imageHelper;

        checkStorageDir();
    }
    @Override
    public List<String> getExistingFilters(){
        return filterList;
    }

    @Override
    public Map<String,BufferedImage> getAllFilteredImages(String imgPath) throws ServiceException {
        LOGGER.info("Entering getAllFilteredImages->imgPath->"+imgPath);

        Map<String,BufferedImage> filteredImgPaths = new HashMap<>();
        filteredImgPaths.put("original", SwingFXUtils.fromFXImage(new Image("file:"+imgPath),null));
        for(String filterName: filterList)
            filteredImgPaths.put(filterName,filter(filterName,imgPath));

        return filteredImgPaths;
    }
    @Override
    public String resize(String imgPath,int width,int height){
        LOGGER.info("Entering resize->imgPath->"+imgPath);

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
                filteredImage = SwingFXUtils.fromFXImage(new Image("file:"+imgPath),null);
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
    public BufferedImage filterGaussian(String imgPath) throws ServiceException{
        LOGGER.info("Entering filterGaussian->imgPath->"+imgPath);

        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat destination = new Mat(source.rows(),source.cols(),source.type());
        //Gaussian kernel size -> 15 - 15 -> sigmaX = 0
        Imgproc.GaussianBlur(source, destination,new Size(25,25), 0);
        source.release();

        BufferedImage image = imageHelper.convertMatToBufferedImg(destination);
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
    public BufferedImage filterGrayScale(String imgPath) throws ServiceException{
        LOGGER.info("Entering filterGrayScale->imgPath->"+imgPath);


        Mat mat = Imgcodecs.imread(imgPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat mat1 = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC1);
        Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);
        mat.release();


        BufferedImage image = imageHelper.convertMatToBufferedImg(mat1);
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
    public BufferedImage filterColorSpace(String imgPath) throws ServiceException{
        LOGGER.info("Entering filterColorSpace->imgPath->"+imgPath);

        Mat mat = Imgcodecs.imread(imgPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat mat1 = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC3);
        Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2HSV);
        mat.release();

        BufferedImage image = imageHelper.convertMatToBufferedImg(mat1);
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
    public BufferedImage filterSobel(String imgPath) throws ServiceException{
        LOGGER.info("Entering filterSobel->imgPath->"+imgPath);
        int kernelSize = 3;
        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);

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
        source.release();

        BufferedImage image =  imageHelper.convertMatToBufferedImg(destination);
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
    public BufferedImage filterThreshZero(String imgPath) throws ServiceException{
        LOGGER.info("Entering filterThreshZero->imgPath->"+imgPath);

        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat destination = source;
        Imgproc.threshold(source,destination,127,255,Imgproc.THRESH_TOZERO);

        BufferedImage image = imageHelper.convertMatToBufferedImg(destination);
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
    public BufferedImage filterThreshBinaryInvert(String imgPath) throws ServiceException{
        LOGGER.info("Entering filterThreshBinaryInvert->imgPath->"+imgPath);

        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat destination = source;
        Imgproc.threshold(source,destination,127,255,Imgproc.THRESH_BINARY_INV);

        BufferedImage image = imageHelper.convertMatToBufferedImg(destination);
        destination.release();
        return image;
    }

    /**
     * checks if storage directory of active shooting exists. If it doesnt exist, then
     * it will create a storage directory.
     *
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    public void checkStorageDir() throws ServiceException {
        if(new File(activeShooting.getStorageDir()).isDirectory())
            storageDir = activeShooting.getStorageDir()+"/";
        else
            throw new ServiceException("checkStorageDir-> StorageDir ist nicht vorhanden!"+activeShooting.getStorageDir());

    }


    @Override
    public void clear(){
        if(bufferedImage!=null)
            bufferedImage.flush();
        bufferedImage = null;
        System.gc();
    }

}
