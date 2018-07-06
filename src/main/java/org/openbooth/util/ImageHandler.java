package org.openbooth.util;

import org.openbooth.util.exceptions.ImageHandlingException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides methods for opening and saving buffered images
 * and converting buffered images to mat and back
 */

@Scope("prototype")
@Component
public class ImageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageHandler.class);

    private final List<String> supportedImageFormats = Arrays.asList("jpg", "jpeg", "bmp", "png");

    /**
     * Instantiates an ImageHandler using the given OpenCVLoader to load the OpenCV library
     * @param openCVLoader the given OpenCVLoader
     */
    public ImageHandler(OpenCVLoader openCVLoader) {
        openCVLoader.loadLibrary();
    }

    /**
     * Opens the image specified by imagePath as BufferedImage
     *
     * @param imagePath the given path, must specify a valid image
     * @return the given image opened as BufferedImage
     * @throws ImageHandlingException if an error during the file opening occurs
     */
    public BufferedImage openImage(String imagePath) throws ImageHandlingException {

        if (imagePath == null || imagePath.isEmpty()) {
            throw new ImageHandlingException("imagePath is null or empty");
        }

        BufferedImage img;

        try {
            img = ImageIO.read(new File(imagePath));
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            throw new ImageHandlingException(e);
        }

        LOGGER.debug("openImage - image from {} loaded", imagePath);
        return img;
    }

    /**
     * Opens the image specified by imagePath as InputStream
     *
     * @param inputStream an InputStream that provides the image.
     * @return the given image opened as BufferedImage
     * @throws ImageHandlingException if an error occurs while reading the image.
     */
    public BufferedImage openImage(InputStream inputStream) throws ImageHandlingException {
        if(inputStream == null){
            throw new ImageHandlingException("inputStream is null");
        }

        BufferedImage img;

        try{
            img = ImageIO.read(inputStream);
        }catch (IOException e) {
            throw new ImageHandlingException(e);
        }

        LOGGER.debug("openImage - image from input stream opened");
        return img;
    }

    /**
     * Saves the given BufferedImage to the location specified by destPath
     * <p>
     * Example of a valid image path: /home/fabian/image1.jpg
     * <p>
     * Supported file formats: jpg, bmp, png
     *
     * @param image    the given image, must specify a valid image
     * @param destPath the given destination path, must specify a valid image path including the filename and ending
     * @throws ImageHandlingException when an error during the image saving occurs
     */
    public void saveImage(BufferedImage image, String destPath) throws ImageHandlingException {
        if (destPath == null || destPath.isEmpty()) {
            throw new ImageHandlingException("destPath is null or empty");
        }

        try {
            String formatName = destPath.substring(destPath.lastIndexOf('.') + 1);
            if (!supportedImageFormats.contains(formatName)) {
                throw new ImageHandlingException("Image format "+ formatName +" not supported");
            }
            File newImage = new File(destPath);
            ImageIO.write(image, formatName, newImage);
        } catch (IOException | NullPointerException e) {
            throw new ImageHandlingException(e);
        }

        LOGGER.debug("Image saved to {}", destPath);
    }

    /**
     * Converts a given OpenCV Mat to a BufferedImage
     * The Mat must have either CV_8UC1 or CV_8UC3 as type (gray scale or 3 Byte unsigned int as color)
     * The given Mat can be released afterwards if necessary
     *
     * @param srcMat the given OpenCV Mat
     * @return the converted BufferedImage
     * @throws ImageHandlingException when an error during the conversion occurs
     */
    public BufferedImage convertMatToBufferedImg(Mat srcMat) throws ImageHandlingException {

        int bufferedImgType;

        if (srcMat.type() == CvType.CV_8UC3) {
            bufferedImgType = BufferedImage.TYPE_3BYTE_BGR;
        } else if (srcMat.type() == CvType.CV_8UC1) {
            bufferedImgType = BufferedImage.TYPE_BYTE_GRAY;
        } else {
            throw new ImageHandlingException("Image type was " + srcMat.type() + " but should be CV_8UC3=" + CvType.CV_8UC3 + " or CV_8UC1=" + CvType.CV_8UC1);
        }

        byte[] data = new byte[srcMat.rows() * srcMat.cols() * (int) (srcMat.elemSize())];
        srcMat.get(0, 0, data);
        BufferedImage result = new BufferedImage(srcMat.cols(), srcMat.rows(), bufferedImgType);

        if (bufferedImgType == BufferedImage.TYPE_3BYTE_BGR) {

            //Color Ordering for Mat is BGR but for BufferedImage is must be RGB so color ordering is changed
            byte b;
            for (int i = 0; i < data.length; i = i + 3) {
                b = data[i];
                data[i] = data[i + 2];
                data[i + 2] = b;
            }
        }


        result.getRaster().setDataElements(0, 0, srcMat.cols(), srcMat.rows(), data);

        return result;
    }

    /**
     * Converts a given BufferedImage to an OpenCV Mat
     * The BufferedImage must have type TYPE_3BYTE_BGR
     * @param srcImg the given BufferedImage
     * @return the converted Mat (Type CV_8UC3)
     * @throws ImageHandlingException when an error during the conversion occurs
     */
    public Mat convertBufferedImgToMat(BufferedImage srcImg) throws ImageHandlingException{

        if(srcImg.getType() != BufferedImage.TYPE_3BYTE_BGR){
            throw new ImageHandlingException("Color type was " + srcImg.getType() + " but should be TYPE_3BYTE_BGR=" + BufferedImage.TYPE_3BYTE_BGR);
        }

        byte[] data = ((DataBufferByte) srcImg.getRaster().getDataBuffer()).getData();

        Mat mat = new Mat(srcImg.getHeight(), srcImg.getWidth(), CvType.CV_8UC3);
        mat.put(0,0,data);

        return mat;
    }





}
