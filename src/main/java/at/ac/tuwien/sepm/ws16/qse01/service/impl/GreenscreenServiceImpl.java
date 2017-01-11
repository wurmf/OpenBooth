package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.util.ImageHelper;
import at.ac.tuwien.sepm.util.OpenCVLoader;
import at.ac.tuwien.sepm.ws16.qse01.service.GreenscreenService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.*;

/**
 * Created by fabian on 04.01.17.
 */
@Service
public class GreenscreenServiceImpl implements GreenscreenService{

    private static Logger LOGGER = LoggerFactory.getLogger(GreenscreenServiceImpl.class);

    private ImageHelper imageHelper;


    @Autowired
    public GreenscreenServiceImpl(OpenCVLoader openCVLoader, ImageHelper imageHelper) throws ServiceException{
        openCVLoader.loadLibrary();
        this.imageHelper = imageHelper;
    }

    @Override
    public BufferedImage getGreenscreenPreview(String srcImgPath, String backgroundPath) throws ServiceException {


        BufferedImage srcImg = imageHelper.openImage(srcImgPath);
        BufferedImage backgroundImg = imageHelper.openImage(backgroundPath);

        Mat srcImgMat = imageHelper.convertBufferedImgToMat(srcImg);
        Mat backgroundMat = imageHelper.convertBufferedImgToMat(backgroundImg);

        Mat yccMat = new Mat(srcImgMat.rows(), srcImgMat.cols(), CvType.CV_8UC3);
        Imgproc.cvtColor(srcImgMat,yccMat,Imgproc.COLOR_RGB2YCrCb);


        double[] rgbKeyColor = srcImgMat.get(0,800);
        double[] yccKeyColor = yccMat.get(0,800);
        double[] tolerances = calcTolerances(yccMat, yccKeyColor);

        LOGGER.debug("tolerances : {}", tolerances);

        Mat result = new Mat(srcImgMat.rows(), srcImgMat.cols(), CvType.CV_8UC3);

        for(int x = 0; x < srcImgMat.rows(); x++){
            for(int y = 0; y < srcImgMat.cols(); y++){
                double[] yccPixel = yccMat.get(x,y);
                double[] rgbPixelFG = srcImgMat.get(x, y);
                double mask = calculateMask(yccPixel, rgbPixelFG, yccKeyColor, rgbKeyColor, tolerances);
                mask = 1 - mask;

                if(x == 100 && y == 100){
                    int w = 1;
                }

                double[] rgbPixelBG = backgroundMat.get(x, y);

                double[] newPixel = new double[3];
                double[] greenScreenColor = (mask == 1 ? rgbPixelFG : rgbKeyColor);
                for(int i = 0; i < 3; i++){
                    newPixel[i] = max(rgbPixelFG[i] - mask * greenScreenColor[i], 0) + mask * rgbPixelBG[i];
                }

                result.put(x,y,newPixel);
            }
        }


        return imageHelper.convertMatToBufferedImg(result);
    }

    private double calculateMask(double[] yccImgColor, double[] rgbImgColor, double[] yccKeyColor, double[]rgbKeyColor, double[] tolerances){

        double yccDiff;
        double r =  sqrt(pow(yccKeyColor[1] - yccImgColor[1], 2)  + pow(yccKeyColor[2] - yccImgColor[2], 2));
        if(r < tolerances[0]){
            yccDiff = 0;
        }else if(r < tolerances[1]){
            yccDiff = pow((r - tolerances[0])/(tolerances[1] - tolerances[0]),1);
        }else{
            yccDiff = 1;
        }
/*
        double x = rgbImgColor[1] - rgbImgColor[2];
        if(x > tolerances[2]){
            return 0;
        }else if (x < tolerances[3]){
            return 1;
        }else {
            return yccDiff;
        }
*/
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

}
