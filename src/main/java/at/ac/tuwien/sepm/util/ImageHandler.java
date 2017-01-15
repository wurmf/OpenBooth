package at.ac.tuwien.sepm.util;

import at.ac.tuwien.sepm.util.exceptions.ImageHandlingException;
import at.ac.tuwien.sepm.util.exceptions.LibraryLoadingException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
@Component
public class ImageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageHandler.class);

    private final List<String> supportedImageFormats = Arrays.asList("jpg", "jpeg", "bmp", "png");

    /**
     * Instantiates an imagehandler using the given OpenCVLoader to load the opencv library
     * @param openCVLoader the given OpenCVLoader
     * @throws LibraryLoadingException if the opencv library could not be loaded
     */
    public ImageHandler(OpenCVLoader openCVLoader) throws LibraryLoadingException {
        openCVLoader.loadLibrary();
    }

    /**
     * Opens the image specified by imagePath as BufferedImage
     *
     * @param imagePath thi given path, must specify a valid image
     * @return the given image opened as BufferedImage
     * @throws ImageHandlingException if an error during the file opening occurs
     */
    public BufferedImage openImage(String imagePath) throws ImageHandlingException {

        if (imagePath == null || imagePath.isEmpty()) {
            LOGGER.error("openImage - imagePath is null or empty");
            throw new ImageHandlingException("imagePath is null or empty");
        }

        BufferedImage img;

        try {
            img = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            LOGGER.error("openImage - error loading given image ", e);
            throw new ImageHandlingException(e);
        }
        LOGGER.debug("Image at {} opened", imagePath);
        return img;
    }

    /**
     * Saves the given BufferedImage to the location specified by destPath
     * <p>
     * Example of a valid image path: /home/fabian/image1.jpg
     * <p>
     * Supported file formats: jpg, bmp, png
     *
     * @param image    th given image, must specify a valid image
     * @param destPath the given destination path, must specify a valid image path including the filename and ending
     * @throws ImageHandlingException when an error during the image saving occurs
     */
    public void saveImage(BufferedImage image, String destPath) throws ImageHandlingException {
        if (destPath == null || destPath.isEmpty()) {
            LOGGER.error("saveImage - destPath is null or empty");
            throw new ImageHandlingException("destPath is null or empty");
        }

        try {
            String formatName = destPath.substring(destPath.lastIndexOf('.') + 1);
            if (!supportedImageFormats.contains(formatName)) {
                LOGGER.error("Image format {} not supported", formatName);
                throw new ImageHandlingException("Image format not supported");
            }
            File newImage = new File(destPath);
            ImageIO.write(image, formatName, newImage);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("saveImage - error during saving image - ", e);
            throw new ImageHandlingException(e);
        }

        LOGGER.debug("Image saved to {}", destPath);
    }

    /**
     * Converts a given opencv Mat to a BufferedImage
     * The Mat must have either CV_8UC1 or CV_8UC3 as type (Grayscale or 3 Byte unsigned int as color)
     * The given Mat can be released afterwards if necessary
     *
     * @param srcMat the given opencv Mat
     * @return the converted BufferedImage
     * @throws ImageHandlingException when an error during the conversion occurs
     */
    public BufferedImage convertMatToBufferedImg(Mat srcMat) throws ImageHandlingException {

        if (srcMat == null) {
            LOGGER.error("convertMatToBufferedImg - given srcMat is null");
            throw new ImageHandlingException("Given Mat is null");
        }

        int bufferedImgType;

        if (srcMat.type() == CvType.CV_8UC3) {
            bufferedImgType = BufferedImage.TYPE_3BYTE_BGR;
        } else if (srcMat.type() == CvType.CV_8UC1) {
            bufferedImgType = BufferedImage.TYPE_BYTE_GRAY;
        } else {
            LOGGER.error("convertMatToBufferedImg - color type must be CV_8UC3 or CV_8UCM but was {}", srcMat.type());
            throw new ImageHandlingException("could not convert Mat to BufferedImage");
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
     * Converts a given BufferedImage to an opencv Mat
     * The BufferedImage must have type TYPE_3BYTE_BGR
     * @param srcImg the given BufferedImage
     * @return the converted Mat (Type CV_8UC3)
     * @throws ImageHandlingException when an error during the conversion occurs
     */
    public Mat convertBufferedImgToMat(BufferedImage srcImg) throws ImageHandlingException{

        if(srcImg == null){
            LOGGER.error("convertBufferedImgToMat - given srcImg is null");
            throw new ImageHandlingException("Given BufferedImage is null");
        }

        if(srcImg.getType() != BufferedImage.TYPE_3BYTE_BGR){
            LOGGER.error("convertBufferedImgToMat - color type should be TYPE_3BYTE_BGR but was {}", srcImg.getType());
            throw new ImageHandlingException("could not convert BufferedImage to matrix");
        }


        byte[] data = ((DataBufferByte) srcImg.getRaster().getDataBuffer()).getData();

        Mat mat = new Mat(srcImg.getHeight(), srcImg.getWidth(), CvType.CV_8UC3);
        mat.put(0,0,data);

        return mat;
    }

    /**
     * shows the given image in own stage (popup window)
     * @param imgPath path of the image
     * @param stage primary stage - owner of this window
     */
    public void popupImage(String imgPath,Stage stage){
        try {
            Stage previewStage = new Stage();
            Group root = new Group();
            Scene scene = new Scene(root);

            ImageView prevView = new ImageView(new Image("file:"+imgPath));
            /*prevView.setFitHeight(previewLogo.getImage().getHeight());
            prevView.setFitWidth(previewLogo.getImage().getWidth());*/
            root.getChildren().add(prevView);

            previewStage.setTitle("Preview Image");
            previewStage.setWidth(prevView.getImage().getWidth());
            previewStage.setHeight(prevView.getImage().getHeight());
            previewStage.setScene(scene);
            previewStage.setFullScreen(false);
            previewStage.initOwner(stage);
            previewStage.show();
        }catch(NullPointerException e){
            LOGGER.error("popupImage ->",e);
        }
    }
}
