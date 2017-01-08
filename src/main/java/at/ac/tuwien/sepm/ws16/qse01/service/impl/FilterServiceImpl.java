package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.FilterService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilterServiceImpl implements FilterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterServiceImpl.class);

    private String storageDir;
    private Shooting activeShooting;

    private List<String> filterList;

    @Autowired
    public FilterServiceImpl(ShootingDAO shootingDAO) throws ServiceException {
        filterList = Arrays.asList("gaussian","grayscale","colorspace","sobel","threshzero","threshbinaryinvert");

        try {
            activeShooting = shootingDAO.searchIsActive();
        } catch (PersistenceException e) {
            throw new ServiceException("Error: "+e.getMessage());
        }

        String lib= "/lib/libopencv_java320.dylib";
        if(com.sun.javafx.PlatformUtil.isWindows())
            lib = "/lib/opencv_java320.dll";
        if(com.sun.javafx.PlatformUtil.isLinux())
            lib = "/lib/libopencv_java320.so";

        String libPath = this.getClass().getResource(lib).getPath();
        System.load(libPath);

        checkStorageDir();
    }
    public List<String> getExistingFilters(){
        return filterList;
    }

    @Override
    public Map<String,BufferedImage> getAllFilteredImages(String imgPath) throws ServiceException {
        LOGGER.info("Entering getAllFilteredImages->imgPath->"+imgPath);

        Map<String,BufferedImage> filteredImgPaths = new HashMap<>();

        filteredImgPaths.put("gaussian",filterGaussian(imgPath));
        filteredImgPaths.put("grayscale",filterGrayScale(imgPath));
        filteredImgPaths.put("colorspace",filterColorSpace(imgPath));
        filteredImgPaths.put("sobel",filterSobel(imgPath));
        filteredImgPaths.put("threshzero",filterThreshZero(imgPath));
        filteredImgPaths.put("threshbinaryinvert",filterThreshBinaryInvert(imgPath));

        return filteredImgPaths;
    }
    @Override
    public String resize(String imgPath,int width,int height){
        LOGGER.info("Entering resize->imgPath->"+imgPath);

        Mat source = Imgcodecs.imread(imgPath,  Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat resizeimage = new Mat();
        Size sz = new Size(width,height);
        Imgproc.resize( source, resizeimage, sz );

        //exporting image name from imagePath
        String[] parts = imgPath.split("/");
        String imgFilterName = parts[parts.length-1].replace(".jpg","_preview.jpg");

        Imgcodecs.imwrite(storageDir+imgFilterName, resizeimage);

        return storageDir+imgFilterName;

    }

    @Override
    public BufferedImage filterGaussian(String imgPath){
        LOGGER.info("Entering filterGaussian->imgPath->"+imgPath);

        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat destination = new Mat(source.rows(),source.cols(),source.type());
        //Gaussian kernel size -> 15 - 15 -> sigmaX = 0
        Imgproc.GaussianBlur(source, destination,new Size(15,15), 0);


        return getBufferedImage(destination);

    }

    @Override
    public BufferedImage filterGrayScale(String imgPath){
        LOGGER.info("Entering filterGrayScale->imgPath->"+imgPath);

        try {
            File input = new File(imgPath);
            BufferedImage image = ImageIO.read(input);

            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);

            mat.put(0, 0, data);

            Mat mat1 = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC1);
            Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);

            byte[] data1 = new byte[mat1.rows() * mat1.cols() * (int) (mat1.elemSize())];
            mat1.get(0, 0, data1);
            BufferedImage image1 = new BufferedImage(mat1.cols(), mat1.rows(), BufferedImage.TYPE_BYTE_GRAY);
            image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

            return image1;
        } catch (Exception e) {
            LOGGER.error("GrayScaleFilter -> : " + e.getMessage());
        }
        return null;
    }

    @Override
    public BufferedImage filterColorSpace(String imgPath){
        LOGGER.info("Entering filterColorSpace->imgPath->"+imgPath);

        try {
            File input = new File(imgPath);
            BufferedImage image = ImageIO.read(input);
            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            Mat mat = new Mat(image.getHeight(),image.getWidth(), CvType.CV_8UC3);
            mat.put(0, 0, data);

            Mat mat1 = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
            Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2HSV);

            byte[] data1 = new byte[mat1.rows()*mat1.cols()*(int)(mat1.elemSize())];
            mat1.get(0, 0, data1);
            BufferedImage image1 = new BufferedImage(mat1.cols(), mat1.rows(), 5);
            image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);


            return image1;

        } catch (Exception e) {
            LOGGER.error("ColorSpace -> : " + e.getMessage());
        }
        return null;
    }

    @Override
    public BufferedImage filterSobel(String imgPath){
        LOGGER.info("Entering filterSobel->imgPath->"+imgPath);
        int kernelSize = 3;
        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat destination = new Mat(source.rows(),source.cols(),source.type());
        Mat kernel = new Mat(kernelSize,kernelSize, CvType.CV_32F){
            {
                put(0,0,-1);
                put(0,1,0);
                put(0,2,1);

                put(1,0-2);
                put(1,1,0);
                put(1,2,2);

                put(2,0,-1);
                put(2,1,0);
                put(2,2,1);
            }
        };

        Imgproc.filter2D(source, destination, -1, kernel);

        return getBufferedImage(destination);
    }

    @Override
    public BufferedImage filterThreshZero(String imgPath){
        LOGGER.info("Entering filterThreshZero->imgPath->"+imgPath);

        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat destination = source;
        Imgproc.threshold(source,destination,127,255,Imgproc.THRESH_TOZERO);

        return getBufferedImage(destination);
    }

    @Override
    public BufferedImage filterThreshBinaryInvert(String imgPath){
        LOGGER.info("Entering filterThreshBinaryInvert->imgPath->"+imgPath);

        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat destination = source;
        Imgproc.threshold(source,destination,127,255,Imgproc.THRESH_BINARY_INV);

        return getBufferedImage(destination);
    }

    @Override
    public void checkStorageDir() throws ServiceException {
        if(new File(activeShooting.getStorageDir()).isDirectory())
            storageDir = activeShooting.getStorageDir()+"/";
       /* else
            throw new ServiceException("checkStorageDir-> StorageDir ist nicht vorhanden!"+activeShooting.getStorageDir());
            /*{
            storageDir = System.getProperty("user.dir") + "/shooting" + activeShooting.getId() + "/";
            Path storageDir = Paths.get(this.storageDir);
            try {
                Files.createDirectory(storageDir);
                LOGGER.info("directory created \n {} \n", storageDir);
            } catch (FileAlreadyExistsException e) {
                LOGGER.info("Directory " + e + " already exists \n");
            } catch (IOException e) {
                LOGGER.error("error creating directory " + e + "\n");
            }
        }*/
    }

    public BufferedImage getBufferedImage(Mat m) {


        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);

        m.get(0, 0, ((DataBufferByte)image.getRaster().getDataBuffer()).getData());
        return image;

    }
}
