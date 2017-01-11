package at.ac.tuwien.sepm.util;

import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides methods for opening and saving buffered images
 * and converting buffered images to mat and back
 */
public class ImageHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageHelper.class);

    private final List<String> supportedImageFormats = Arrays.asList("jpg", "jpeg", "bmp", "png");

    public BufferedImage openImage(String imagePath) throws ServiceException{

        if(imagePath == null || imagePath.isEmpty()){
            LOGGER.error("openImage - imagePath is null or empty");
            throw new ServiceException("imagePath is null or empty");
        }

        BufferedImage img;

        try {
            img = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            LOGGER.error("openImage - error loading given image " , e);
            throw new ServiceException(e);
        }
        LOGGER.debug("Image at {} opened", imagePath);
        return img;
    }

    public void saveImage(BufferedImage image, String destPath) throws ServiceException{
        if(destPath == null || destPath.isEmpty()){
            LOGGER.error("saveImage - destPath is null or empty");
            throw new ServiceException("destPath is null or empty");
        }

        try {
            String formatName = destPath.substring(destPath.lastIndexOf('.') + 1);
            if(!supportedImageFormats.contains(formatName)){
                LOGGER.error("Image format {} not supported", formatName);
                throw new ServiceException("Image format not supported");
            }
            File newImage = new File(destPath);
            ImageIO.write(image, formatName, newImage);
        } catch (IOException e) {
            LOGGER.error("saveImage - error during saving image - " , e);
            throw new ServiceException(e);
        }

        LOGGER.debug("Image saved to {}", destPath);
    }
}
