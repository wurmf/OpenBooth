package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Interface to implement filtering services.
 */
public interface FilterService {

    /**
     * converts given image to filtered image with existing filters.
     *
     * @param imgPath the path of image to convert
     * @return Map list of all image paths of filtered images
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    Map<String,BufferedImage> getAllFilteredImages(String imgPath) throws ServiceException;

    /**
     * resizes given image to given size ( width - height )
     *
     * @param imgPath the path of image to resize
     * @return String image path of resized image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    String resize(String imgPath,int width,int height) throws ServiceException;

    /**
     * changes given image with GAUSSIAN filter
     *
     * @param imgPath the path of image to filter
     * @return BufferedImage filtered image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    BufferedImage filterGaussian(String imgPath) throws ServiceException;

    /**
     * changes given image with GRAY SCALE filter
     *
     * @param imgPath the path of image to filter
     * @return BufferedImage filtered image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    BufferedImage filterGrayScale(String imgPath) throws ServiceException;

    /**
     * changes given image with COLOR SPACE filter
     *
     * @param imgPath the path of image to filter
     * @return BufferedImage filtered image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    BufferedImage filterColorSpace(String imgPath) throws ServiceException;

    /**
     * changes given image with SOBEL filter
     *
     * @param imgPath the path of image to filter
     * @return BufferedImage filtered image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    BufferedImage filterSobel(String imgPath) throws ServiceException;

    /**
     * changes given image with THRESH ZERO filter
     *
     * @param imgPath the path of image to filter
     * @return BufferedImage filtered image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    BufferedImage filterThreshZero(String imgPath) throws ServiceException;

    /**
     * changes given image with THRESH BINARY INVERT filter
     *
     * @param imgPath the path of image to filter
     * @return BufferedImage filtered image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    BufferedImage filterThreshBinaryInvert(String imgPath) throws ServiceException;

    /**
     * checks if storage directory of active shooting exists. If it doesnt exist, then
     * it will create a storage directory.
     *
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    void checkStorageDir() throws ServiceException;
}
