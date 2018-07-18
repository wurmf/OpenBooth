package org.openbooth.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import org.openbooth.operating.Operator;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;

@Controller
public class ShotFrameController {

    @FXML
    private ImageView shotView;

    private ApplicationContext applicationContext;

    public ShotFrameController(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    public void refreshShot(BufferedImage img) {
        shotView.setImage(SwingFXUtils.toFXImage(img,null));
    }

    @FXML
    private void shotFrameClicked(){
        applicationContext.getBean(Operator.class).trigger();
    }
}
