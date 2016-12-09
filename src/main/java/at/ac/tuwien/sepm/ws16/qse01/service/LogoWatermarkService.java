package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

/**
 * This class is used for adding a logo or a watermark to pictures
 */
public interface LogoWatermarkService {

    /**
     * Adds the logos specified by the logo paths and logo positions given by the profile of the
     * active shooting to the image specified by the given image path.
     * The logos are cached, so if the logo changes during a shooting it must have a different filename/path.
     *
     * The original image will be overwritten.
     * @param srcImgPath the image to which the logos will be added.
     * @throws ServiceException if an error during image processing, saving or opening occurs
     */
     void addLogosToImage(String srcImgPath) throws ServiceException;

    /**
     * Adds a watermark to the image specified by the srcImgPath and saves it to the location specified by
     * destImgPath.
     * Example for destImgPath /home/fabian/shooting1/img_with_watermark.jpg
     * @param srcImgPath specifies the image where the watermark should be added
     * @param destImgPath spefies the path and filename of the resulting image
     * @throws ServiceException if an error during the image processing, saving or opening occurs
     */
     void addWatermarkCreateNewImage(String srcImgPath, String destImgPath) throws ServiceException;
}
