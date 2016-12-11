package at.ac.tuwien.sepm.ws16.qse01.service.impl;

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
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
public class LogoWatermarkServiceImpl implements LogoWatermarkService{

    private static Logger LOGGER = LoggerFactory.getLogger(LogoWatermarkServiceImpl.class);

    private ProfileService profileService;
    private Map<Logo, BufferedImage> cachedLogos = new HashMap<>();
    private String currentWatermarkPath = null;
    private BufferedImage cachedWatermark = null;

    @Autowired
    public LogoWatermarkServiceImpl(ProfileService profileService){
        this.profileService = profileService;
    }

    @Override
    public void addLogosCreateNewImage(String srcImgPath, String destImgPath) throws ServiceException {

        BufferedImage img;

        try {
            img = ImageIO.read(new File(srcImgPath));
        } catch (IOException e) {
            LOGGER.error("addLogosCreateNewImage - error loading given image" + e);
            throw new ServiceException(e);
        }

        List<Logo> logos = profileService.getAllLogosOfActiveProfile();
        for(logo : logos){
            //check if cached and cache if not
            //comparison with equals in logo entity
            if(!cachedLogos.containsKey(logo)){
                BufferedImage logoImage = ImageIO.read(logo.getPath());
                cachedLogos.put(logo, logoImage);
            }
            //add current logo
            RelativeRectangle curLogoPosition = profileService.getRelativeRectangleOfLogoOfActiveProfile(logo);
            addLogoAtPosition(img, cachedLogos.get(logo), curLogoPosition);
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

        Logo watermark = profileService.getWaterMarkOfActiveProfile();
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

        g.drawImage(cachedWatermark, 0, 0, img.getWidth(), img.getHeight(), new LogoWatermarkServiceImageObserver(this));
        try {
            this.wait();
        } catch (InterruptedException e) {
            LOGGER.error("addWatermarkCreateNewImage -" + e);
            throw new ServiceException(e);
        }

        //save image
        try {
            saveImage(img, destImgPath);
        } catch (IOException e) {
            LOGGER.error("addWatermarkCreateNewImage - error during saving new image" + e);
            throw new ServiceException(e);
        }


    }

    private void saveImage(BufferedImage img, String destImgPath) throws IOException{
        String formatName = destImgPath.substring(destImgPath.lastIndexOf('.'));
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

        LogoWatermarkServiceImageObserver observer = new LogoWatermarkServiceImageObserver(this);

        Graphics g = img.getGraphics();

        try {
            if(relativeLogoHeight == -1 && relativeLogoWidth == -1){
                g.drawImage(logo, absoluteXPosition, absoluteYPosition, observer);
                this.wait();
            }else if(relativeLogoHeight == -1 && relativeLogoWidth > 0){
                double absoluteLogoWidth = relativeLogoWidth / (100d) * imgWidth;
                double absoluteLogoHeight = relativeLogoHeight/relativeLogoWidth * absoluteLogoWidth;
                g.drawImage(logo, absoluteXPosition, absoluteYPosition, (int)absoluteLogoWidth, (int)absoluteLogoHeight, observer);
                this.wait();
            }else if(relativeLogoHeight > 0 && relativeLogoWidth == -1){
                double absoluteLogoHeigth = relativeLogoHeight / (100d) * imgHeight;
                double absoluteLogoWidth = relativeLogoWidth / relativeLogoHeight * absoluteLogoHeigth;
                g.drawImage(logo, absoluteXPosition, absoluteYPosition, (int)absoluteLogoWidth, (int)absoluteLogoHeigth, observer);
                this.wait();
            }else if(relativeLogoHeight > 0 && relativeLogoWidth > 0){
                double absoluteLogoHeigth = relativeLogoHeight / (100d) * imgHeight;
                double absoluteLogoWidth = relativeLogoWidth / (100d) * imgWidth;
                this.wait();
            }else{
                LOGGER.error("addLogoAtPosition - no valid logo width and height");
                throw new ServiceException("addLogoAtPosition - no valid logo width and heigh");
            }
        } catch (InterruptedException e) {
            LOGGER.error("addLogoAtPosition -" + e);
            throw new ServiceException(e);
        }
    }

    private class LogoWatermarkServiceImageObserver implements ImageObserver{
        private LogoWatermarkService instanceToNotify;

        private LogoWatermarkServiceImageObserver(LogoWatermarkService instanceToNotify){
            this.instanceToNotify = instanceToNotify;
        }

        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            instanceToNotify.notify();
            return false;
        }
    }
}
