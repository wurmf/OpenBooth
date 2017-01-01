package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.ImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
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
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class includes services for images.
 */
@Service
public class ImageServiceImpl implements ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    private ImageDAO dao;
    private ShootingDAO shootingDAO;
    private String storageDir;
    private Shooting activeShooting;

    public ImageServiceImpl() throws ServiceException{
        try {
            this.dao = new JDBCImageDAO(H2Handler.getInstance());
        } catch (PersistenceException e) {
            throw new ServiceException("Error: "+e.getMessage());
        }
    }
    @Autowired
    public ImageServiceImpl(ImageDAO imageDAO, ShootingDAO shootingDAO) throws ServiceException {
        this.dao = imageDAO;
        this.shootingDAO = shootingDAO;
        try {
            activeShooting = shootingDAO.searchIsActive();
        } catch (PersistenceException e) {
            throw new ServiceException("Error: "+e.getMessage());
        }

        System.out.println(System.getProperty("java.library.path"));

        // System.loadLibrary(Core.NATIVE_LIBRARY_NAME); = opencv_300
        String lib= "/.lib/libopencv_java320.dylib";
        if(com.sun.javafx.PlatformUtil.isWindows())
            lib = "/.lib/opencv_java320.dll";
        if(com.sun.javafx.PlatformUtil.isLinux())
            lib = "/.lib/opencv_java320.so"; //TODO lib-file for linux must be saved in proj_ws16

        System.load(System.getProperty("user.dir")+lib);
        checkStorageDir();

    }

    @Override
    public Image create(Image f) throws ServiceException {
        try{
            LOGGER.debug("Entering create method in Service with parameters {}"+f);
            return dao.create(f);
         } catch (PersistenceException e) {
            throw new ServiceException("Error! Creating in service layer has failed.:" + e.getMessage());
        }
    }

    @Override
    public Image read(int id) throws ServiceException {
        try {
            LOGGER.debug("Entering read method in Service with image id = "+id);
            return dao.read(id);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Reading in service layer has failed.:" + e.getMessage());
        }

    }

    @Override
    public void update(Image img) throws ServiceException {
        try {
            LOGGER.debug("Entering update method in Service with image = "+img.toString());
            dao.update(img);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Updating in service layer has failed.:" + e.getMessage());
        }
    }

    @Override
    public void delete(int imageID) throws ServiceException {
        try {
            LOGGER.debug("Entering delete method in Service with imageID = " + imageID);
            dao.delete(imageID);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Deleting in service layer has failed.:" + e.getMessage());
        }
    }

    @Override
    public String getLastImgPath(int shootingid) throws ServiceException  {
        try {
            LOGGER.debug("Entering getLastImgPath method in Service with shootingid = " + shootingid);
            return dao.getLastImgPath(shootingid);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! getLastImgPath in service layer has failed.:" + e.getMessage());
        }
    }

    @Override
    public List<Image> getAllImages(int shootingid) throws ServiceException {
        try {
            LOGGER.debug("Entering getAllImages method in Service with shootingid = " + shootingid);
            return dao.getAllImages(shootingid);
        }catch(PersistenceException e){
            throw new ServiceException("Error! Showing all images in service layer has failed.:" + e.getMessage());
        }
    }


    @Override
    public int getNextImageID() throws ServiceException {
        try {
            LOGGER.debug("Entering getNextImageID method in Service ");
            return dao.getNextImageID();
        } catch (PersistenceException e) {
            throw new ServiceException("Error! getNextImageId in service layer has failed.:" + e.getMessage());
        }
    }
    @Override
    public Map<String,String> getAllFilteredImages(String imgPath) throws ServiceException {
        Map<String,String> filteredImgPaths = new HashMap<String,String>();

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
        Mat source = Imgcodecs.imread(imgPath,
                Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat resizeimage = new Mat();
        Size sz = new Size(width,height);
        Imgproc.resize( source, resizeimage, sz );

        //exporting image name from imagePath
        String[] parts = imgPath.split("/");
        String imgFilterName = parts[parts.length-1].replace(".jpg","_preview.jpg");

        System.out.println(storageDir+imgFilterName);
        Imgcodecs.imwrite(storageDir+imgFilterName, resizeimage);

        return storageDir+imgFilterName;

    }

    @Override
    public String filterGaussian(String imgPath){
        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);

        //    Mat source = Imgcodecs.imread(System.getProperty("user.dir") + "/src/main/resources/"+imgPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat destination = new Mat(source.rows(),source.cols(),source.type());
        //Gaussian kernel size -> 45 - 45 -> sigmaX = 0
        Imgproc.GaussianBlur(source, destination,new Size(15,15), 0);

        //exporting image name from imagePath
        String[] parts = imgPath.split("/");
        String imgFilterName = parts[parts.length-1].replace(".jpg","_1.jpg");

        System.out.println(storageDir+imgFilterName);
        Imgcodecs.imwrite(storageDir+imgFilterName, destination);
        return storageDir+imgFilterName;
      /*  // If not preview then save it in DB!
        if(!preview){
            try {
                imageService.create(new at.ac.tuwien.sepm.ws16.qse01.entities.Image(storageDir+imgFilterName,activeShooting.getId()));
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }*/
        //   return filteredImg;
    }

    @Override
    public String filterGrayScale(String imgPath){
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

            //exporting image name from imagePath
            String[] parts = imgPath.split("/");
            String imgFilterName = parts[parts.length-1].replace(".jpg","_2.jpg");

            File output = new File(storageDir+imgFilterName);

            ImageIO.write(image1, "jpg", output);

            return storageDir+imgFilterName;
        } catch (Exception e) {
            LOGGER.error("GrayScaleFilter -> : " + e.getMessage());
        }
        return null;
    }

    @Override
    public String filterColorSpace(String imgPath){
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

            //exporting image name from imagePath
            String[] parts = imgPath.split("/");
            String imgFilterName = parts[parts.length-1].replace(".jpg","_3.jpg");

            File output = new File(storageDir+imgFilterName);
            ImageIO.write(image1, "jpg", output);

            return storageDir+imgFilterName;

        } catch (Exception e) {
            LOGGER.error("ColorSpace -> : " + e.getMessage());
        }
        return null;
    }

    @Override
    public String filterSobel(String imgPath){
        int kernelSize = 9;
        //   Mat source = Imgcodecs.imread(System.getProperty("user.dir") + "/src/main/resources/"+imgPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
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

        //exporting image name from imagePath
        String[] parts = imgPath.split("/");
        String imgFilterName = parts[parts.length-1].replace(".jpg","_4.jpg");

        System.out.println(storageDir+imgFilterName);
        Imgcodecs.imwrite(storageDir+imgFilterName, destination);

        return storageDir+imgFilterName;
    }

    @Override
    public String filterThreshZero(String imgPath){
        int kernelSize = 9;
        //     Mat source = Imgcodecs.imread(System.getProperty("user.dir") + "/src/main/resources/"+imgPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);
        //  Mat destination = new Mat(source.rows(),source.cols(),source.type());
        Mat destination = source;
        Imgproc.threshold(source,destination,127,255,Imgproc.THRESH_TOZERO);

        //exporting image name from imagePath
        String[] parts = imgPath.split("/");
        String imgFilterName = parts[parts.length-1].replace(".jpg","_5.jpg");

        System.out.println(storageDir+imgFilterName);
        Imgcodecs.imwrite(storageDir+imgFilterName, destination);

        return storageDir+imgFilterName;
    }

    @Override
    public String filterThreshBinaryInvert(String imgPath){
        int kernelSize = 9;
        //       Mat source = Imgcodecs.imread(System.getProperty("user.dir") + "/src/main/resources/"+imgPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat source = Imgcodecs.imread(imgPath,Imgcodecs.CV_LOAD_IMAGE_COLOR);
        //Mat destination = new Mat(source.rows(),source.cols(),source.type());
        Mat destination = source;
        Imgproc.threshold(source,destination,127,255,Imgproc.THRESH_BINARY_INV);

        //exporting image name from imagePath
        String[] parts = imgPath.split("/");
        String imgFilterName = parts[parts.length-1].replace(".jpg","_6.jpg");

        System.out.println(storageDir+imgFilterName);
        Imgcodecs.imwrite(storageDir+imgFilterName, destination);

        return storageDir+imgFilterName;
    }

    @Override
    public void checkStorageDir(){
        if(new File(activeShooting.getStorageDir()).isDirectory())
            storageDir = activeShooting.getStorageDir()+"/";
        else {
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
        }
    }


}
