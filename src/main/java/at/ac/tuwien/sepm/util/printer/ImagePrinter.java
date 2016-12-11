package at.ac.tuwien.sepm.util.printer;

import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

/**
 * Class that is used to print images on a printer connected to the computer.
 */
@Component
public class ImagePrinter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImagePrinter.class);
    /**
     * Prints the image defined in the path saved in the given Image-Entity.
     * @param image the Image-Entity that shall be printed.
     * @throws IOException if an error occurs while reading the file in which the actual image is stored.
     * @throws PrinterException if an error occurs while printing.
     */
    public void print(Image image) throws PrinterException {
        try {
            File imageFile=new File(image.getImagepath());
            print(ImageIO.read(imageFile));
        } catch (IOException e) {
            LOGGER.error("print - "+e);
            throw new PrinterException("Unable to read file from given path");
        }
        LOGGER.debug("Leaving first print method");
    }

    /**
     * Prints the given image object to a printer connected to the computer.
     * @param image the Image-object that shall be printed.
     * @throws PrinterException if an error occurs while printing.
     */
    public void print(BufferedImage image) throws PrinterException {
        PrinterJob job=PrinterJob.getPrinterJob();
        job.setPrintable(new PrintableImage(image));
        job.print();
        LOGGER.debug("Job done");
    }

    private class PrintableImage implements Printable{
        private BufferedImage image;
        private PrintableImage(BufferedImage image){
            this.image=image;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            LOGGER.debug("print in PrintableImage started");
            if(pageIndex==0&&this.image!=null) {
                Graphics2D g2d=(Graphics2D) graphics;
                double pageWidth=pageFormat.getWidth();
                double pageHeight=pageFormat.getHeight();
                double imageWidth=image.getWidth();
                double imageHeight=image.getHeight();
                double scaleHeight=pageHeight/imageHeight;
                double scaleWidth=pageWidth/imageWidth;
                //TODO: scale for smaller images and check for widescreen images
                double scale= (scaleHeight<scaleWidth)?scaleHeight:scaleWidth;

                if(imageWidth>imageHeight){
                    LOGGER.debug("Orientation set to Landscape ImageWidth: "+imageWidth+" | ImageHeight: "+imageHeight);
                    //TODO: fix rotation
                    //AffineTransform transform=new AffineTransform();
                    //transform.rotate(Math.PI/2,imageWidth/2, imageHeight/2);
                    //g2d.transform(transform);
                    pageFormat.setOrientation(PageFormat.PORTRAIT);
                    g2d.rotate(Math.PI/2,imageWidth/2, imageHeight/2);
                } else{
                    LOGGER.debug("Orientation set to Portrait ImageWidth: "+imageWidth+" | ImageHeight: "+imageHeight);
                    pageFormat.setOrientation(PageFormat.PORTRAIT);
                }
                g2d.scale(scale,scale);
                graphics.drawImage(image,0,0,null);
                return PAGE_EXISTS;
            }
            LOGGER.debug("print - NO_SUCH_PAGE");
            return NO_SUCH_PAGE;
        }
    }
}
