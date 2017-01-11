package at.ac.tuwien.sepm.util;

import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
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

    public ImageHelper(OpenCVLoader openCVLoader) throws ServiceException{
        openCVLoader.loadLibrary();
    }

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

    public BufferedImage convertMatToBufferedImg(Mat srcMat) throws ServiceException{

        if(srcMat == null){
            LOGGER.error("convertMatToBufferedImg - given srcMat is null");
            throw new ServiceException("Given Mat is null");
        }

        int bufferedImgType;

        if(srcMat.type() == CvType.CV_8UC3){
            bufferedImgType = BufferedImage.TYPE_3BYTE_BGR;
        } else if (srcMat.type() == CvType.CV_8UC1){
            bufferedImgType = BufferedImage.TYPE_BYTE_GRAY;
        }else {
            LOGGER.error("convertMatToBufferedImg - color type must be CV_8UC3 or CV_8UCM but was {}", srcMat.type());
            throw new ServiceException("could not convert Mat to BufferedImage");
        }

        byte[] data = new byte[srcMat.rows() * srcMat.cols() * (int)(srcMat.elemSize())];
        srcMat.get(0, 0, data);
        BufferedImage result = new BufferedImage(srcMat.cols(),srcMat.rows(), bufferedImgType);

        if(bufferedImgType == BufferedImage.TYPE_3BYTE_BGR) {

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

    public Mat convertBufferedImgToMat(BufferedImage srcImg) throws ServiceException{

        if(srcImg == null){
            LOGGER.error("convertBufferedImgToMat - given srcImg is null");
            throw new ServiceException("Given BufferedImage is null");
        }

        if(srcImg.getType() != BufferedImage.TYPE_3BYTE_BGR){
            LOGGER.error("convertBufferedImgToMat - color type should be TYPE_3BYTE_BGR but was {}", srcImg.getType());
            throw new ServiceException("could not convert BufferedImage to matrix");
        }


        byte[] data = ((DataBufferByte) srcImg.getRaster().getDataBuffer()).getData();

        Mat mat = new Mat(srcImg.getHeight(), srcImg.getWidth(), CvType.CV_8UC3);
        mat.put(0,0,data);

        return mat;
    }
}
