package org.openbooth.util.printer;

import org.openbooth.util.exceptions.OBPrinterException;
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
     * @param imagePath the image path that shall be printed
     * @throws OBPrinterException if an error occurs while printing.
     */
    public void print(String imagePath) throws OBPrinterException {
        if(imagePath==null || imagePath.isEmpty()){
            throw new IllegalArgumentException("image path null or empty");
        }
        try {
            File imageFile=new File(imagePath);
            print(ImageIO.read(imageFile));
        } catch (IOException e) {
            throw new OBPrinterException("Unable to read file from given path",e);
        }
    }

    /**
     * Prints the given image object to a printer connected to the computer.
     * @param image the Image-object that shall be printed.
     * @throws OBPrinterException if an error occurs while printing.
     */
    public void print(BufferedImage image) throws OBPrinterException {
        if(image==null){
            throw new OBPrinterException("Null image not allowed.");
        }
        PrinterJob job=PrinterJob.getPrinterJob();
        job.setPrintable(new PrintableImage(image));
        try {
            job.print();
        } catch (PrinterException e) {
            throw new OBPrinterException(e);
        }

        LOGGER.debug("print - printing was succesful");
    }

    private class PrintableImage implements Printable{
        private BufferedImage image;
        private PrintableImage(BufferedImage image){
            this.image=image;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
            if( pageIndex==0 && this.image!=null ) {
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

                    //Don't worry about IntelliJ, these lines are for interchanging width and height and are intentional.
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
