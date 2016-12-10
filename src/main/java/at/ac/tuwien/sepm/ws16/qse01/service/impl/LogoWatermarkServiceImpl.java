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
    private BufferedImage cachedWatermarrk = null;

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

        List<PairLogoRelativeRectangle> pairList = profileService.getListPairLogoRelativeRelativeRectangle();

        //cache all Logos
        for(pair : pairList){
            Logo curLogo = pair.getLogo();
            //comparison with equals in Logo
            if(!cachedLogos.containsKey(curLogo)){
                BufferedImage logoImage = ImageIO.read(curLogo.getPath());
                cachedLogos.put(curLogo, logoImage);
            }
        }

        for(pair : pairList){
            BufferedImage curLogoImg = cachedLogos.get(pair.getLogo());
            addLogoAtPosition(img, curLogoImg, pair.getRelativeRectangle());
        }

        try {
            String formatName = destImgPath.substring(destImgPath.lastIndexOf('.'));
            File newImage = new File(destImgPath);
            ImageIO.write(img, formatName, newImage);
        } catch (IOException e) {
            LOGGER.error("addLogoCreateNewImage - error during saving new image" + e);
            throw new ServiceException(e);
        }


    }

    @Override
    public void addWatermarkCreateNewImage(String srcImgPath, String destImgPath) throws ServiceException {

    }

    private void addLogoAtPosition(BufferedImage img, BufferedImage logo, RelativeRectangle position){

        double imgWidth = img.getWidth();
        double imgHeight = img.getHeight();

        double relativeXPosition = position.getX();
        double relativeYPosition = position.getY();

        int absoluteXPosition = (int)(imgWidth * relativeXPosition / (100d));
        int absoluteYPosition = (int)(imgHeight * relativeYPosition / (100d));

        double logoRelativeWidth = position.getWidth();
        double logoRelativeHeight = position.getHeight();

        LogoServiceImageObserver observer = new LogoServiceImageObserver(this);

        Graphics g = img.getGraphics();

        if(logoRelativeHeight == -1 && logoRelativeWidth == -1){
            g.drawImage(logo, absoluteXPosition, absoluteYPosition, observer);
            this.wait();
        }else if(logoRelativeHeight == -1 && logoRelativeWidth > 0){
            
        }
    }

    private class LogoServiceImageObserver implements ImageObserver{
        private LogoWatermarkService instanceToNotify;

        private LogoServiceImageObserver(LogoWatermarkService instanceToNotify){
            this.instanceToNotify = instanceToNotify;
        }

        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            instanceToNotify.notify();
            return false;
        }
    }
}
