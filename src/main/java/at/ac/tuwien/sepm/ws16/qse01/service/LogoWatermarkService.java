package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

/**
 * This class is used for adding a logo or a watermark to pictures
 */
public interface LogoWatermarkService {

    /**
     * Adds the logos specified by the logo paths and logo positions given by the profile of the
     * active shooting to the image specified by the given image path und save it to the location specified
     * by destImgPath
     * The logos are cached, so if the logo changes during a shooting it must have a different filename/path.
     *
     * If the width or the height of a relative rectangle, representing a logo position is set to
     * -1 it will be automatically scaled according to the given logo.
     *
     * If both width and length are set to -1 the absolute pixel values will be used
     *
     * If srcImgPath = destImgPath, the image located at srcImgPath will be overwritten.
     * Example for destImgPath /home/fabian/shooting1/img1_with_logos.jpg
     * @param srcImgPath the image to which the logos will be added.
     * @throws ServiceException if an error during image processing, saving or opening occurs
     */
     void addLogosCreateNewImage(String srcImgPath, String destImgPath) throws ServiceException;

    /**
     * Adds a watermark specified by the profile of the active shooting
     * to the image specified by the srcImgPath and saves it to the location specified by destImgPath.
     *
     * If srcImgPath = destImgPath, the image located at srcImgPath will be overwritten.
     * Example for destImgPath /home/fabian/shooting1/img1_with_watermark.jpg
     * @param srcImgPath specifies the image where the watermark should be added
     * @param destImgPath specifies the path and filename of the resulting image
     * @throws ServiceException if an error during the image processing, saving or opening occurs
     */
     void addWatermarkCreateNewImage(String srcImgPath, String destImgPath) throws ServiceException;
}
