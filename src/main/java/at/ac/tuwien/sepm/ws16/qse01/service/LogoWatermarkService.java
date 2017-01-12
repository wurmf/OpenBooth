package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Logo;
import at.ac.tuwien.sepm.ws16.qse01.entities.RelativeRectangle;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;


/**
 * This class is used for adding logos or a watermark to pictures
 */
public interface LogoWatermarkService {

    /*
    precondition:   the logo must specify a valid image
                    0 < relativeHeight
                    imageWidth and ImageHeight must be > 0

    postcondition: 0 < return value
     */
    /**
     * Calculates the relative width by calculating the absolute height (through the gives imageHeight) and multiplying it
     * with the aspect ratio of the given logo and then converting
     * it back to the relative value(through the given imageWidth).
     * @param relativeHeight the given relative height in %
     * @param logo the given logo
     * @param imageWidth the given imageWidth
     * @param imageHeight the give imageHeight
     * @return the calculated relative width in %
     * @throws ServiceException if any error during the calculation occurs
     */
    double calculateRelativeWidth(double relativeHeight, Logo logo, double imageWidth, double imageHeight) throws ServiceException;

    /*
    precondition:   the logo must specify a valid image
                    0 < relativeHeight
                    imageWidth and ImageHeight must be > 0

    postcondition: 0 < return value
    */
    /**
     * Calculates the relative height by calculating the absolute width (through the gives imageWidth) and multiplying it
     * with the aspect ratio of the given logo and then converting
     * it back to the relative value(through the given imageHeight).
     * @param relativeWidth the given relative width in %
     * @param logo the given logo
     * @param imageWidth the given imageWidth
     * @param imageHeight the give imageHeight
     * @return the calculated relative height in %
     * @throws ServiceException if any error during the calculation occurs
     */
    double calculateRelativeHeight(double relativeWidth, Logo logo, double imageWidth, double imageHeight) throws ServiceException;

    /*
    precondition:   the logo must specify a valid image,
                    the values specified by position must satisfy following conditions:
                        -> 0 <= x,y <= 100
                        -> 0 < height,width <= 100 OR height, width == -1
                    imageWidth and ImageHeight must be > 0
    postcondition: a valid image is returned
     */
    /**
     * Shows a preview of the resulting image, if the given logo would be added at the position specified by the given
     * RelativeRectangle. imageWidth and imageHeight must be set correctly.
     *
     * If the width or the height of the relative rectangle, representing a logo position is set to
     * -1 the width or height will be automatically scaled according to the given logo.
     *
     * If both width and length are set to -1 the absolute pixel values will be used
     *
     * @param logo the given logo, which will be contained in the preview
     * @param position specifies the position of the logo
     * @param imageWidth specifies the width of the resulting image in pixel
     * @param imageHeight specifies the height of the resulting image in pixel
     * @return the preview where the logo is added at the specified position
     * @throws ServiceException if an error during the image processing or opening of the logo image occurs
     * */
    BufferedImage getPreviewForLogo(Logo logo, RelativeRectangle position, int imageWidth, int imageHeight) throws ServiceException;

    /*
    precondition:   all logos must specify valid images,
                    the values specified by the positions must satisfy following conditions:
                        -> 0 <= x,y <= 100
                        -> 0 < height,width <= 100 OR height, width == -1
                    imageWidth and ImageHeight must be > 0
                    logos and positions must have the same length
    postcondition: a valid image is returned
     */
    /**
     * Shows a preview of the resulting image if all of the given logos would be added at the positions specified by the given
     * RelativeRectangles. imageWidth and imageHeight must be set correctly.
     * The list of logos and RelativeRectangles must be in corresponding order,
     * therefore the first RelativeRectangle in the RelativeRectangle list specifies the position for the first logo in the logo list
     *
     * If the width or the height of a relative rectangle, representing a logo position is set to
     * -1 it will be automatically scaled according to the given logo.
     *
     * If both width and length are set to -1 the absolute pixel values will be used
     *
     * @param logos the given logos, which will be contained in the preview
     * @param positions the list of RelativeRectangles, which specify the position of the logos
     * @param imageWidth specifies the width of the resulting image in pixel
     * @param imageHeight specifies the height of the resulting image in pixel
     * @return the preview where all logos are added at the specified positions
     * @throws ServiceException if an error during the image processing or opening one of the logo images occurs
     */
    BufferedImage getPreviewForMultipleLogos(List<Logo> logos, List<RelativeRectangle> positions, int imageWidth, int imageHeight) throws ServiceException;

    /*
     precondition:  a valid image is given, all logos must be a valid images,
    */
    /**
     * Adds the logos specified by the logo paths and logo positions given by the profile of the
     * active shooting to the given image, the given image will therefore be modified
     *
     * The logos are cached, so if the logo changes during a shooting it must have a different filename/path.
     *
     * If the width or the height of a relative rectangle, representing a logo position is set to
     * -1 it will be automatically scaled according to the given logo.
     *
     * If both width and length are set to -1 the absolute pixel values will be used
     *
     * @return the given image with the logos included
     * @throws ServiceException if an error during image processing, saving or opening occurs
     */
     BufferedImage addLogosToImage(BufferedImage srcImg) throws ServiceException;

     /*
     precondition:  a valid image at srcImgPath exists, the watermark must be a valid image,
      */
    /**
     * Adds a watermark specified by the profile of the active shooting
     * to the image specified by the srcImgPath
     *
     * @param srcImgPath specifies the image where the watermark should be added
     * @return the given image with the watermark included
     * @throws ServiceException if an error during the image processing, saving or opening occurs
     */
     BufferedImage addWatermarkToImage(String srcImgPath) throws ServiceException;
}
