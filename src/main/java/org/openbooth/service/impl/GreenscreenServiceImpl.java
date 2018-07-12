package org.openbooth.service.impl;

import org.openbooth.util.ImageHandler;
import org.openbooth.util.OpenCVLoader;
import org.openbooth.util.exceptions.ImageHandlingException;
import org.openbooth.service.GreenscreenService;
import org.openbooth.service.exceptions.ServiceException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;

/**
 * This class implements a greenscreen service
 */
@Service
@Scope("prototype")
public class GreenscreenServiceImpl implements GreenscreenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreenscreenServiceImpl.class);

    private ImageHandler imageHandler;

    private Map<ImageSize, Map<String, Mat>> imageSizeCachedBackgroundsMap = new HashMap<>();


    @Autowired
    public GreenscreenServiceImpl(OpenCVLoader openCVLoader, ImageHandler imageHandler) {
        openCVLoader.loadLibrary();
        this.imageHandler = imageHandler;
    }

    @Override
    public BufferedImage applyGreenscreen(BufferedImage srcImg, String background) throws ServiceException {
        LOGGER.debug("Entering applyGreenscreen method");

        if(srcImg == null || background == null){
            LOGGER.error("applyGreenscreen - given image or given background path is null, image: {} background: {}", srcImg, background);
            throw new ServiceException("Given image or background is null");
        }

        Mat srcImgMat;
        try {
            srcImgMat = imageHandler.convertBufferedImgToMat(srcImg);
        } catch (ImageHandlingException e) {
            LOGGER.error("applyGreenscreen - could not convert given image", e);
            throw new ServiceException("Could not convert given image");
        }

        Mat backgroundMat = getCachedBackground(srcImg, background);


        Mat yccFGMat = new Mat(srcImgMat.rows(), srcImgMat.cols(), CvType.CV_8UC3);
        Imgproc.cvtColor(srcImgMat,yccFGMat,Imgproc.COLOR_RGB2YCrCb);


        double[] rgbKeyColor = srcImgMat.get(0,0);
        double[] yccKeyColor = yccFGMat.get(0,0);
        double[] tolerances = calcTolerances(yccFGMat, yccKeyColor);

        LOGGER.debug("tolerances : {}", tolerances);

        Mat resultMat = new Mat(srcImgMat.rows(), srcImgMat.cols(), CvType.CV_8UC3);

        for(int x = 0; x < srcImgMat.rows(); x++){
            for(int y = 0; y < srcImgMat.cols(); y++){
                double[] yccFGColor = yccFGMat.get(x,y);
                double[] rgbFGColor = srcImgMat.get(x, y);
                double mask = calculateMask(yccFGColor, yccKeyColor, tolerances);
                mask = 1 - mask;


                double[] rgbBGColor = backgroundMat.get(x, y);

                double[] rgbNewColor = new double[3];
                double[] greenScreenColor =  (int)mask == 1 ? rgbFGColor : rgbKeyColor;
                for(int i = 0; i < 3; i++){
                    rgbNewColor[i] = max(rgbFGColor[i] - mask * greenScreenColor[i], 0) + mask * rgbBGColor[i];
                }

                resultMat.put(x,y,rgbNewColor);
            }
        }

        BufferedImage newImage;
        try {
            newImage = imageHandler.convertMatToBufferedImg(resultMat);
        } catch (ImageHandlingException e) {
            LOGGER.error("applyGreenscreen - Could not convert result Mat to image", e);
            throw new ServiceException("Could not convert result Mat");
        }

        return newImage;
    }

    private double calculateMask(double[] yccImgColor, double[] yccKeyColor, double[] tolerances){

        double yccDiff;
        double r =  sqrt(pow(yccKeyColor[1] - yccImgColor[1], 2)  + pow(yccKeyColor[2] - yccImgColor[2], 2));
        if(r < tolerances[0]){
            yccDiff = 0;
        }else if(r < tolerances[1]){
            yccDiff = pow((r - tolerances[0])/(tolerances[1] - tolerances[0]),1);
        }else{
            yccDiff = 1;
        }

        return yccDiff;

    }

    private double[] calcTolerances(Mat yccMat, double[] yccKeyColor){
        double maxRadius = 0;
        for(int x = 0; x < yccMat.rows(); x++){
            for(int y = 0; y < yccMat.cols(); y++){
                double[] yccPixel = yccMat.get(x,y);
                double r = sqrt(pow(yccPixel[1] - yccKeyColor[1], 2) + pow(yccPixel[2] - yccKeyColor[2], 2));

                if(r > maxRadius){
                    maxRadius = r;
                }
            }
        }
        double[] tolerances = new double[2];
        tolerances[0] = maxRadius / 4;
        tolerances[1] = maxRadius * (6.0/12.0);
        return tolerances;
    }

    private Mat getCachedBackground(BufferedImage srcImg, String background) throws ServiceException{
        ImageSize imageSize = new ImageSize(srcImg.getWidth(), srcImg.getHeight());

        Map<String, Mat> cachedBackgrounds;

        //check if cached backgrounds exist for this image size
        if(imageSizeCachedBackgroundsMap.containsKey(imageSize)){
            cachedBackgrounds = imageSizeCachedBackgroundsMap.get(imageSize);
        }else{
            cachedBackgrounds = new HashMap<>();
            imageSizeCachedBackgroundsMap.put(imageSize, cachedBackgrounds);
        }


        Mat backgroundMat;

        //check if background is already cached
        if(cachedBackgrounds.containsKey(background)){
            backgroundMat = cachedBackgrounds.get(background);
        }else{
            BufferedImage backgroundImg;

            //Open backgroundimage
            try {
                backgroundImg = imageHandler.openImage(background);
            } catch (ImageHandlingException e) {
                LOGGER.error("applyGreenscreen - could not open background image", e);
                throw new ServiceException("Could not open background image");
            }

            //Convert backgroundimage to Mat
            try {
                backgroundMat = imageHandler.convertBufferedImgToMat(backgroundImg);
            } catch (ImageHandlingException e) {
                LOGGER.error("applyGreenscreen - could not convert background image", e);
                throw new ServiceException("Could not convert background image");
            }
            //resize background if it is not the same size as the source image
            if(!imageSize.isSameSizeAs(backgroundMat)){
                Mat resizedBackgroundMat = new Mat();
                Size newSize = new Size(imageSize.getWidth(), imageSize.getHeight());
                Imgproc.resize(backgroundMat, resizedBackgroundMat, newSize);
                backgroundMat.release();
                backgroundMat = resizedBackgroundMat;
            }

            //Cache new background
            cachedBackgrounds.put(background, backgroundMat);
        }

        return backgroundMat;
    }

    private class ImageSize{
        private int height;
        private int width;

        public ImageSize(int width, int height){
            this.height = height;
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public boolean isSameSizeAs(Mat mat){
            int matWidth = mat.cols();
            int matHeight = mat.rows();

            return height == matHeight && width == matWidth;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (o == null || getClass() != o.getClass())
                return false;

            ImageSize imageSize = (ImageSize) o;

            if (height != imageSize.height)
                return false;

            return width == imageSize.width;
        }

        @Override
        public int hashCode() {
            int result = height;
            result = 31 * result + width;
            return result;
        }
    }

}
