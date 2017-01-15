package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.util.exceptions.ImageHandlingException;
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
    private final ImageHandler imageHandler;
    private final Map<Logo, BufferedImage> cachedLogos = new HashMap<>();
    private String currentWatermarkPath = null;
    private BufferedImage cachedWatermark = null;

    @Autowired
    public LogoWatermarkServiceImpl(ProfileService profileService, ImageHandler imageHandler){
        this.imageHandler = imageHandler;
        this.profileService = profileService;
    }

    @Override
    public double calculateRelativeWidth(double relativeHeight, Logo logo, double imageWidth, double imageHeight) throws ServiceException{
        BufferedImage logoImage = openImageFromLogo(logo);

        if(relativeHeight <= 0){
            LOGGER.error("calculateRelativeWidth - relativeHeight must be > 0");
            throw new ServiceException("relativeHeight must be > 0");
        }

        if(imageHeight <= 0 || imageWidth <= 0){
            LOGGER.error("calculateRelativeWidth - imageWidth or imageHeight <= 0");
            throw new ServiceException("imageWidth or imageHeight is <= 0");
        }

        double widthHeightRatio = (double)logoImage.getWidth() / (double)logoImage.getHeight();

        double absoluteHeight = relativeHeight / (100D) * imageHeight;
        double absoluteWidth = absoluteHeight * widthHeightRatio;

        return absoluteWidth / imageWidth * (100D);
    }

    @Override
    public double calculateRelativeHeight(double relativeWidth, Logo logo, double imageWidth, double imageHeight) throws ServiceException{

        BufferedImage logoImage = openImageFromLogo(logo);

        if(relativeWidth <= 0){
            LOGGER.error("calculateRelativeHeight - relativeWidth must be > 0");
            throw new ServiceException("relativeWidth must be > 0");
        }

        if(imageHeight <= 0 || imageWidth <= 0){
            LOGGER.error("calculateRelativeHeight - imageWidth or imageHeight <= 0");
            throw new ServiceException("imageWidth or imageHeight <= 0");
        }

        double heightWidthRatio = (double)logoImage.getHeight() / (double)logoImage.getWidth();

        double absoluteWidth = relativeWidth / (100D) * imageWidth;
        double absoluteHeight = absoluteWidth * heightWidthRatio;

        return absoluteHeight / imageHeight * (100D);
    }

    @Override
    public BufferedImage getPreviewForLogo(Logo logo, RelativeRectangle position, int imageWidth, int imageHeight) throws ServiceException{
        LOGGER.debug("Entering getPreviewForLogo method");

        BufferedImage img = createPreviewImage(imageWidth, imageHeight);

        BufferedImage logoImage = openImageFromLogo(logo);

        addLogoAtPosition(img, logoImage, position);

        return img;
    }

    @Override
    public BufferedImage getPreviewForMultipleLogos(List<Logo> logos, List<RelativeRectangle> positions, int imageWidth, int imageHeight) throws ServiceException{
        LOGGER.debug("Entering getPreviewFroMultipleLogos method");

        if(logos.size() != positions.size()){
            LOGGER.error("getPreviewForMultipleLogos - logo list and position list have different length");
            throw new ServiceException("logo list and position list have different length");
        }

        BufferedImage img = createPreviewImage(imageWidth, imageHeight);

        //iterate through logos
        for(int i=0; i<logos.size(); i++){
            BufferedImage logoImage = openImageFromLogo(logos.get(i));
            addLogoAtPosition(img, logoImage, positions.get(i));
        }

        return img;
    }

    @Override
    public BufferedImage addLogosToImage(BufferedImage srcImg) throws ServiceException {
        LOGGER.debug("Entering addLogosToImage method");

        if(srcImg == null){
            LOGGER.error("addLogosToImage - given image is null");
            throw new ServiceException("given image is null");
        }

        List<Logo> logos = profileService.getAllLogosOfProfile();

        if(logos == null){
            LOGGER.error("addLogosToImage - logolist is null");
            throw new ServiceException("logolist is null");
        }

        for(Logo logo : logos){
            //check if cached and cache if not
            //comparison with equals in logo entity

            if(!cachedLogos.containsKey(logo)){
                BufferedImage logoImage = openImageFromLogo(logo);
                cachedLogos.put(logo, logoImage);
            }
            //add current logo
            RelativeRectangle curLogoPosition = profileService.getRelativeRectangleOfLogoOfProfile(logo);
            addLogoAtPosition(srcImg, cachedLogos.get(logo), curLogoPosition);
        }

        return srcImg;
    }

    @Override
    public BufferedImage addWatermarkToImage(String srcImgPath) throws ServiceException {

        Logo watermark = profileService.getProfileWaterMark();

        if(watermark == null || watermark.getPath() == null){
            LOGGER.error("addWatermarkToImage - watermark or watermarkpath is null");
            throw new ServiceException("watermark or watermarkpath is null");
        }

        String newWatermarkPath = watermark.getPath();

        //check if cached and cache if not
        if(!newWatermarkPath.equals(currentWatermarkPath)){
            currentWatermarkPath = newWatermarkPath;
            cachedWatermark = openImageFromLogo(watermark);
        }

        BufferedImage img;
        try {
            img = imageHandler.openImage(srcImgPath);
        } catch (ImageHandlingException e) {
            throw new ServiceException(e);
        }

        Graphics g = img.getGraphics();

        g.drawImage(cachedWatermark, 0, 0, img.getWidth(), img.getHeight(), null);

        return img;

    }



    private void addLogoAtPosition(BufferedImage img, BufferedImage logo, RelativeRectangle position) throws ServiceException{
        double imgWidth = img.getWidth();
        double imgHeight = img.getHeight();

        double relativeXPosition = position.getX();
        double relativeYPosition = position.getY();

        int absoluteXPosition = (int)(imgWidth * relativeXPosition / (100d));
        int absoluteYPosition = (int)(imgHeight * relativeYPosition / (100d));



        RelativeRectangle calculatedPosition = calculatePosition(position, imgWidth, imgHeight, logo.getWidth(), logo.getHeight());

        double relativeLogoWidth = calculatedPosition.getWidth();
        double relativeLogoHeight = calculatedPosition.getHeight();

        if(relativeLogoWidth <= 0 || relativeLogoHeight <= 0){
            LOGGER.error("addLogoAtPosition- no valid logo width and height");
            throw new ServiceException("no valid logo width and height");
        }

        double absoluteLogoHeight = relativeLogoHeight / (100d) * imgHeight;
        double absoluteLogoWidth = relativeLogoWidth / (100d) * imgWidth;

        Graphics2D g = img.createGraphics();

        g.drawImage(logo, absoluteXPosition, absoluteYPosition, (int)absoluteLogoWidth, (int)absoluteLogoHeight, null);
        g.dispose();
        LOGGER.debug("logo with width {} and height {} added at X: {} Y: {} | Imagewidth: {} Imageheight: {}", absoluteLogoWidth, absoluteLogoHeight, absoluteXPosition, absoluteYPosition, imgWidth, imgHeight);
    }

    private RelativeRectangle calculatePosition(RelativeRectangle position, double imageWidth, double imageHeight, double logoWidth, double logoHeight) throws ServiceException{

        if(position == null){
            LOGGER.error("calculatePosition - RelativeRectangle is null");
            throw new ServiceException("RelativeRectangle is null");
        }

        double relativeLogoWidth = position.getWidth();
        double relativeLogoHeight = position.getHeight();

        double widthHeightRatio = logoWidth / logoHeight;

        double absoluteCalculatedLogoWidth = relativeLogoWidth / (100D) * imageWidth;
        double absoluteCalculatedLogoHeight = relativeLogoHeight / (100D) * imageHeight;

        //use absolute pixel values if both height and width are set to -1
        if((int)relativeLogoHeight == -1 && (int)relativeLogoWidth == -1){

            absoluteCalculatedLogoWidth = logoWidth;
            absoluteCalculatedLogoHeight = logoHeight;

        }else if((int)relativeLogoHeight == -1 && relativeLogoWidth > 0){

            absoluteCalculatedLogoHeight =  absoluteCalculatedLogoWidth / widthHeightRatio;

        }else if(relativeLogoHeight > 0 && (int)relativeLogoWidth == -1){

            absoluteCalculatedLogoWidth = widthHeightRatio * absoluteCalculatedLogoHeight;

        }

        relativeLogoWidth = absoluteCalculatedLogoWidth / imageWidth * (100D);
        relativeLogoHeight = absoluteCalculatedLogoHeight / imageHeight * (100D);

        LOGGER.debug("given logo position and dimension {}", position);

        position.setHeight(relativeLogoHeight);
        position.setWidth(relativeLogoWidth);

        LOGGER.debug("calculated logo position and dimension: {} ", position);

        return position;
    }

    private BufferedImage createPreviewImage(int imageWidth, int imageHeight) throws ServiceException{
        if(imageHeight <= 0 || imageWidth <= 0){
            LOGGER.error("createPreviewImage - imageWidth or imageHeight <= 0");
            throw new ServiceException("imageWidth or imageHeight <= 0");
        }


        //Fill Previewimage with color
        BufferedImage img = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setPaint(new Color(160, 160, 160));
        g.fillRect(0, 0, img.getWidth(), img.getHeight());

        LOGGER.debug("Previewimage background with {}px width and {}px height created", imageWidth, imageHeight);
        return img;
    }

    private BufferedImage openImageFromLogo(Logo logo) throws ServiceException{
        if(logo == null || logo.getPath() == null || logo.getPath().isEmpty()){
            LOGGER.error("openImageFromLogo - Logo or Logopath is null");
            throw new ServiceException("Logo or logopath is null or empty");
        }

        BufferedImage img;

        try {
             img = ImageIO.read(new File(logo.getPath()));
        } catch (IOException e) {
            LOGGER.error("openImageFromLogo - error loading {} " + e, logo.getPath());
            throw new ServiceException(e);
        }

        LOGGER.debug("Image from Logo {} opened", logo);
        return img;
    }

}
