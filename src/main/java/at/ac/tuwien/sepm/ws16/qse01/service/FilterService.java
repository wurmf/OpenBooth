package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Interface to implement filtering services.
 */
public interface FilterService {

    /**
     * getter methode for existing filter list
     * @return List list of existing filters
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    public List<String> getExistingFilters() throws ServiceException;
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
     * @param filterName the name of filter
     * @param imgPath the path of image to filter
     * @return BufferedImage filtered image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    BufferedImage filter(String filterName,String imgPath) throws ServiceException;

    /**
     * free memeory of all used objects
     */
    void clear();
}
