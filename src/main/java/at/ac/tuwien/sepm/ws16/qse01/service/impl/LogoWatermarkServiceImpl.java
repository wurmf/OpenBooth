package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.entities.Logo;
import at.ac.tuwien.sepm.ws16.qse01.entities.RelativeRectangle;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class LogoWatermarkServiceImpl implements LogoWatermarkService{

    private static final Logger LOGGER = LoggerFactory.getLogger(LogoWatermarkServiceImpl.class);

    private final ProfileService profileService;
    private final Map<Logo, BufferedImage> cachedLogos = new HashMap<>();
    private String currentWatermarkPath = null;
    private BufferedImage cachedWatermark = null;

    @Autowired
    public LogoWatermarkServiceImpl(ProfileService profileService){
        this.profileService = profileService;
    }

    @Override
    public double calculateRelativeWidth(double relativeHeight, Logo logo) throws ServiceException{
        //TODO: IMPLEMENT
        return 0;
    }

    @Override
    public double calculateRelativeHeight(double relativeWidth, Logo logo) throws ServiceException{
        //TODO: IMPLEMENT
        return 0;
    }

    @Override
    public Image getPreviewForLogo(Logo logo, RelativeRectangle position, int imageWidth, int imageHeight) {
        //TODO: IMPLEMENT
        return null;
    }

    @Override
    public Image getPreviewForMultipleLogos(List<Logo> logos, List<RelativeRectangle> positions, int imageWidth, int imageHeight) {
        //TODO: IMPLEMENT
        return null;
    }

    @Override
    public void addLogosCreateNewImage(String srcImgPath, String destImgPath) throws ServiceException {

        BufferedImage img;

        try {
            img = ImageIO.read(new File(srcImgPath));
        } catch (IOException e) {
            LOGGER.error("addLogosCreateNewImage - error loading {} " + e, srcImgPath);
            throw new ServiceException(e);
        }

        List<Logo> logos = profileService.getAllLogosOfProfile();
        for(Logo logo : logos){
            //check if cached and cache if not
            //comparison with equals in logo entity
            try {
                if(!cachedLogos.containsKey(logo)){
                    BufferedImage logoImage = ImageIO.read(new File(logo.getPath()));
                    cachedLogos.put(logo, logoImage);
                }
                //add current logo
                RelativeRectangle curLogoPosition = profileService.getRelativeRectangleOfLogoOfProfile(logo);
                addLogoAtPosition(img, cachedLogos.get(logo), curLogoPosition);
            } catch (IOException e) {
                LOGGER.error("addLogosCreateNewImage - error during reading logo -" + e);
                throw new ServiceException(e);
            }
        }


        //save image
        try {
            saveImage(img, destImgPath);
        } catch (IOException e) {
            LOGGER.error("addLogosCreateNewImage - error during saving new image" + e);
            throw new ServiceException(e);
        }


    }

    @Override
    public void addWatermarkCreateNewImage(String srcImgPath, String destImgPath) throws ServiceException {

        Logo watermark = profileService.getProfileWaterMark();
        String newWatermarkPath = watermark.getPath();

        if(!newWatermarkPath.equals(currentWatermarkPath)){
            currentWatermarkPath = newWatermarkPath;
            try {
                cachedWatermark = ImageIO.read(new File(srcImgPath));
            } catch (IOException e) {
                LOGGER.error("addWatermarkCreateNewImage - error loading watermark" + e);
                throw new ServiceException(e);
            }
        }

        BufferedImage img;

        try {
            img = ImageIO.read(new File(srcImgPath));
        } catch (IOException e) {
            LOGGER.error("addWatermarkCreateNewImage - error loading given image" + e);
            throw new ServiceException(e);
        }

        Graphics g = img.getGraphics();

        g.drawImage(cachedWatermark, 0, 0, img.getWidth(), img.getHeight(), null);

        //save image
        try {
            saveImage(img, destImgPath);
        } catch (IOException e) {
            LOGGER.error("addWatermarkCreateNewImage - error during saving new image" + e);
            throw new ServiceException(e);
        }


    }

    private void saveImage(BufferedImage img, String destImgPath) throws IOException{
        String formatName = destImgPath.substring(destImgPath.lastIndexOf('.') + 1);
        File newImage = new File(destImgPath);
        ImageIO.write(img, formatName, newImage);
    }

    private void addLogoAtPosition(BufferedImage img, BufferedImage logo, RelativeRectangle position) throws ServiceException{

        double imgWidth = img.getWidth();
        double imgHeight = img.getHeight();

        double relativeXPosition = position.getX();
        double relativeYPosition = position.getY();

        int absoluteXPosition = (int)(imgWidth * relativeXPosition / (100d));
        int absoluteYPosition = (int)(imgHeight * relativeYPosition / (100d));

        double relativeLogoWidth = position.getWidth();
        double relativeLogoHeight = position.getHeight();

        double widthHeightRatio = (double)logo.getWidth() / (double)logo.getHeight();


        Graphics g = img.getGraphics();

        if((int)relativeLogoHeight == -1 && (int)relativeLogoWidth == -1){
            g.drawImage(logo, absoluteXPosition, absoluteYPosition, null);
        }else if((int)relativeLogoHeight == -1 && relativeLogoWidth > 0){
            double absoluteLogoWidth = relativeLogoWidth / (100d) * imgWidth;
            double absoluteLogoHeight =  absoluteLogoWidth / widthHeightRatio;
            g.drawImage(logo, absoluteXPosition, absoluteYPosition, (int)absoluteLogoWidth, (int)absoluteLogoHeight, null);
        }else if(relativeLogoHeight > 0 && (int)relativeLogoWidth == -1){
            double absoluteLogoHeight = relativeLogoHeight / (100d) * imgHeight;
            double absoluteLogoWidth = widthHeightRatio * absoluteLogoHeight;
            g.drawImage(logo, absoluteXPosition, absoluteYPosition, (int)absoluteLogoWidth, (int)absoluteLogoHeight, null);
        }else if(relativeLogoHeight > 0 && relativeLogoWidth > 0){
            double absoluteLogoHeight = relativeLogoHeight / (100d) * imgHeight;
            double absoluteLogoWidth = relativeLogoWidth / (100d) * imgWidth;
            g.drawImage(logo, absoluteXPosition, absoluteYPosition, (int)absoluteLogoWidth, (int)absoluteLogoHeight, null);
        }else{
            LOGGER.error("addLogoAtPosition - no valid logo width and height");
            throw new ServiceException("addLogoAtPosition - no valid logo width and height");
        }

    }
}
