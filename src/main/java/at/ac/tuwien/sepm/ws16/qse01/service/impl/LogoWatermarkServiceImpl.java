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
        BufferedImage logoImage = openImageFromLogo(logo);


        double widthHeightRatio = (double)logoImage.getWidth() / (double)logoImage.getHeight();

        return relativeHeight * widthHeightRatio;
    }

    @Override
    public double calculateRelativeHeight(double relativeWidth, Logo logo) throws ServiceException{

        BufferedImage logoImage = openImageFromLogo(logo);


        double heightWidthRatio = (double)logoImage.getHeight() / (double)logoImage.getWidth();

        return relativeWidth * heightWidthRatio;
    }

    @Override
    public Image getPreviewForLogo(Logo logo, RelativeRectangle position, int imageWidth, int imageHeight) throws ServiceException{

        BufferedImage img = createPreviewImage(imageWidth, imageHeight);

        BufferedImage logoImage = openImageFromLogo(logo);

        addLogoAtPosition(img, logoImage, position);

        return img;
    }

    @Override
    public Image getPreviewForMultipleLogos(List<Logo> logos, List<RelativeRectangle> positions, int imageWidth, int imageHeight) throws ServiceException{

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
    public void addLogosCreateNewImage(String srcImgPath, String destImgPath) throws ServiceException {
        BufferedImage img = openImage(srcImgPath);

        List<Logo> logos = profileService.getAllLogosOfProfile();

        if(logos == null){
            LOGGER.error("addLogosCreateNewImage - logolist is null");
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
            addLogoAtPosition(img, cachedLogos.get(logo), curLogoPosition);
        }

        saveImage(img, destImgPath);
    }

    @Override
    public void addWatermarkCreateNewImage(String srcImgPath, String destImgPath) throws ServiceException {

        Logo watermark = profileService.getProfileWaterMark();

        if(watermark == null || watermark.getPath() == null){
            LOGGER.error("addWatermarkCreateNewImage - watermark or watermarkpath is null");
            throw new ServiceException("watermark or watermarkpath is null");
        }

        String newWatermarkPath = watermark.getPath();

        //check if cached and cache if not
        if(!newWatermarkPath.equals(currentWatermarkPath)){
            currentWatermarkPath = newWatermarkPath;
            cachedWatermark = openImageFromLogo(watermark);
        }

        BufferedImage img = openImage(srcImgPath);

        Graphics g = img.getGraphics();

        g.drawImage(cachedWatermark, 0, 0, img.getWidth(), img.getHeight(), null);

        //save image
        saveImage(img, destImgPath);

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

        double absoluteLogoHeight = relativeLogoHeight / (100d) * imgHeight;
        double absoluteLogoWidth = relativeLogoWidth / (100d) * imgWidth;

        Graphics2D g = img.createGraphics();

        g.drawImage(logo, absoluteXPosition, absoluteYPosition, (int)absoluteLogoWidth, (int)absoluteLogoHeight, null);
    }

    private RelativeRectangle calculatePosition(RelativeRectangle position, double imageWidth, double imageHeight, double logoWidth, double logoHeight) throws ServiceException{

        if(position == null){
            LOGGER.error("calculatePosition - RelativeRectangle is null");
            throw new ServiceException("RelativeRectangle is null");
        }

        double relativeLogoWidth = position.getWidth();
        double relativeLogoHeight = position.getHeight();

        double widthHeightRatio = logoWidth / logoHeight;

        //use absolute pixel values if both height and width are set to -1
        if((int)relativeLogoHeight == -1 && (int)relativeLogoWidth == -1){

            relativeLogoWidth = logoWidth / imageWidth * (100D);
            relativeLogoHeight = logoHeight / imageHeight * (100D);

        }else if((int)relativeLogoHeight == -1 && relativeLogoWidth > 0){

            relativeLogoHeight =  relativeLogoWidth / widthHeightRatio;

        }else if(relativeLogoHeight > 0 && (int)relativeLogoWidth == -1){

            relativeLogoWidth = widthHeightRatio * relativeLogoHeight;

        }else{
            LOGGER.error("calculatePosition - no valid logo width and height");
            throw new ServiceException("addLogoAtPosition - no valid logo width and height");
        }

        position.setHeight(relativeLogoHeight);
        position.setWidth(relativeLogoWidth);

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

        return img;
    }

    private BufferedImage openImageFromLogo(Logo logo) throws ServiceException{
        if(logo == null || logo.getPath() == null || logo.getPath().isEmpty()){
            LOGGER.error("openImageFromLogo - Logo or Logopath is null");
            throw new ServiceException("Logo or logopath is null or empty");
        }

        try {
            return  ImageIO.read(new File(logo.getPath()));
        } catch (IOException e) {
            LOGGER.error("openImageFromLogo - error loading {} " + e, logo.getPath());
            throw new ServiceException(e);
        }

    }

    private void saveImage(BufferedImage img, String destImgPath) throws ServiceException{
        if(destImgPath == null || destImgPath.isEmpty()){
            LOGGER.error("saveImage - destImgPath is null or empty");
            throw new ServiceException("destImgPath is null or empty");
        }

        try {
            String formatName = destImgPath.substring(destImgPath.lastIndexOf('.') + 1);
            File newImage = new File(destImgPath);
            ImageIO.write(img, formatName, newImage);
        } catch (IOException e) {
            LOGGER.error("saveImage - error during saving image" + e);
            throw new ServiceException(e);
        }

    }

    private BufferedImage openImage(String srcImgPath) throws ServiceException{
        if(srcImgPath == null || srcImgPath.isEmpty()){
            LOGGER.error("openImage - srcImgPath is null or empty");
            throw new ServiceException("srcImgPath is null or empty");
        }
        try {
            return ImageIO.read(new File(srcImgPath));
        } catch (IOException e) {
            LOGGER.error("openImage - error loading given image" + e);
            throw new ServiceException(e);
        }
    }
}
