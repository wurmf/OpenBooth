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
        if(image==null||image.getImagepath()==null){
            throw new PrinterException("Null or Empty Image-Object not allowed.");
        }
        try {
            File imageFile=new File(image.getImagepath());
            print(ImageIO.read(imageFile));
        } catch (IOException e) {
            LOGGER.error("print - "+e);
            throw new PrinterException("Unable to read file from given path");
        }
    }

    /**
     * Prints the given image object to a printer connected to the computer.
     * @param image the Image-object that shall be printed.
     * @throws PrinterException if an error occurs while printing.
     */
    public void print(BufferedImage image) throws PrinterException {
        if(image==null){
            throw new PrinterException("Null image not allowed.");
        }
        PrinterJob job=PrinterJob.getPrinterJob();
        job.setPrintable(new PrintableImage(image));
        job.print();
        LOGGER.debug("print - printing was succesful");
    }

    private class PrintableImage implements Printable{
        private BufferedImage image;
        private PrintableImage(BufferedImage image){
            this.image=image;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if(pageIndex==0&&this.image!=null) {
                Graphics2D g2d=(Graphics2D) graphics;
                double pageWidth=pageFormat.getImageableWidth();
                double pageHeight=pageFormat.getImageableHeight();
                double imageWidth=image.getWidth();
                double imageHeight=image.getHeight();

                if(imageWidth>imageHeight){
                    AffineTransform transform=new AffineTransform();
                    transform.translate(imageHeight/2, imageWidth/2);
                    transform.rotate(Math.PI/2);
                    transform.translate(-imageWidth/2,-imageHeight/2);
                    AffineTransformOp transformOp=new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                    image=transformOp.filter(image, null);
                    double help=imageHeight;
                    imageHeight=imageWidth;
                    imageWidth=help;
                }

                double scaleHeight=pageHeight/imageHeight;
                double scaleWidth=pageWidth/imageWidth;
                double scale;

                int xInset=(int) Math.round((pageFormat.getWidth()-pageFormat.getImageableWidth())/4);
                int yInset=(int) Math.round((pageFormat.getHeight()-pageFormat.getImageableHeight())/4);
                if(scaleHeight<scaleWidth){
                    scale=scaleHeight;
                    xInset=(int) Math.round((pageFormat.getWidth()-imageWidth*scale)/4);
                } else{
                    scale=scaleWidth;
                    yInset=(int) Math.round((pageFormat.getHeight()-imageHeight*scale)/4);
                }
                g2d.scale(scale,scale);
                graphics.drawImage(image,xInset,yInset,null);
                return PAGE_EXISTS;
            }
            return NO_SUCH_PAGE;
        }
    }
}
