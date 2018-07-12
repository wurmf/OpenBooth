package org.openbooth.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;

@Controller
public class ShotFrameController {

    @FXML
    private ImageView shotView;

    public void refreshShot(BufferedImage img) {
        shotView.setImage(SwingFXUtils.toFXImage(img,null));
    }

    @FXML
    private void shotFrameClicked(){};
}
