package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Logo;
import at.ac.tuwien.sepm.ws16.qse01.entities.RelativeRectangle;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.awt.Image;
import java.util.List;


/**
 * This class is used for adding a logo or a watermark to pictures
 */
public interface LogoWatermarkService {

    /**
     * Calculates the relative width by multiplying the given relative width with the aspect ratio of the given logo
     * @param relativeHeight the given relative height
     * @param logo the given logo
     * @throws ServiceException if any error during the calculation occurs
     */
    void calculateRelativeWidth(double relativeHeight, Logo logo) throws ServiceException;

    /**
     * Calculates the relative height by multiplying the given relative width with the aspect ratio of the given logo
     * @param relativeWidth the given relative width
     * @param logo the given logo
     * @throws ServiceException if any error during the calculation occurs
     * */
    void calculateRelativeHeight(double relativeWidth, Logo logo) throws ServiceException;

    /**
     * Shows a preview of the resulting image, if the given logo would be added at the position specified by the given
     * RelativeRectangle. imageWidth and imageHeigth must be set correctly.
     * @param logo
     * @param position
     * @param imageWidth
     * @param imageHeight
     * @return
     * @throws ServiceException if an error during the image processing or opening of the logo image occurs
     * */
    Image getPreviewForLogo(Logo logo, RelativeRectangle position, int imageWidth, int imageHeight) throws ServiceException;

    /**
     * Shows a preview of the resulting image if all of the given logos would be added at the positions specified by the given
     * RelativeRectangles. imageWidth and imageHeight must be set correctly.
     * The list of logos and RelativeRectangles must be in corresponding order,
     * therefore the first RelativeRectangle in the RelativeRectangle list specifies the position for the first logo in the logo list
     * @param logos
     * @param positions
     * @param imageWidth
     * @param imageHeight
     * @return
     * @throws ServiceException if an error during the image processing or opening one of the logo images occurs
     */
    Image getPreviewForMultipleLogos(List<Logo> logos, List<RelativeRectangle> positions, int imageWidth, int imageHeight) throws ServiceException;

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
