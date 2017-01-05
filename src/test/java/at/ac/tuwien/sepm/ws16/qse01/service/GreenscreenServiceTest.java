package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by fabian on 04.01.17.
 */
public abstract class GreenscreenServiceTest {

    private GreenscreenService greenscreenService;

    protected void setGreenscreenService(GreenscreenService greenscreenService){
        this.greenscreenService = greenscreenService;
    }

    @Test
    public void getPreviewWithValidParameters() throws ServiceException{
        String srcImgPath = this.getClass().getResource("/greenscreen/images/greenscreen_woman2.jpg").getPath();
        String backgroundImgPath = this.getClass().getResource("/greenscreen/background/greenscreen_background2.png").getPath();

        BufferedImage result = greenscreenService.getGreenscreenPreview(srcImgPath, backgroundImgPath);

        File newImage = new File("/home/fabian/test.jpg");
        try {
            ImageIO.write(result, "jpg", newImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
