package org.openbooth.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;

/**
 * Controller for the shotFrame.
 *
 */
@Controller
@Scope("prototype")

public class ShotFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShotFrameController.class);
    @FXML
    private ImageView shotView;
    @FXML
    private Label countdownLabel;

    private int frameID;
    private Stage primaryStage;

    private int startTimeSec = 0;
    @FXML
    private void initialize(){
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        shotView.setFitHeight(screenHeight);

    }
    public void initShotFrame(int cameraID,Stage primaryStage)  {
        this.frameID  = cameraID;
        this.primaryStage = primaryStage;
    }

    /*
     Precondition: shootingID must be defined
     Postcondition: the last image will be showed
     */
    /**
     * Showing the last image taken.
     */
    @FXML
    public void refreshShot(String imgPath) {

        LOGGER.debug("refreshing Shot with imagepath = {}",imgPath);
        try {
            shotView.setImage(new Image(imgPath));
        }catch (Exception e){
            LOGGER.error("refreshShot(String) - Fehler - ",e);
        }
    }

    public void refreshShot(BufferedImage img) {
        try {
            shotView.setImage(SwingFXUtils.toFXImage(img,null));
        }catch (Exception e){
            LOGGER.error("refreshShot(BufferedImage) - Fehler - ",e);
        }
    }

    public void showCountdown(int countdown){
        createCounter(countdown).play();
    }

    private Timeline createCounter(int countdown){
        Timeline timeline = new Timeline();
        startTimeSec = countdown;
        KeyFrame keyframe = new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
                startTimeSec--;
                if (startTimeSec==0) {
                    timeline.stop();
                    countdownLabel.setVisible(false);
                }else{
                    countdownLabel.setText(String.valueOf(startTimeSec));
                    countdownLabel.setVisible(true);
                }

        });


        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyframe);
        return timeline;
    }
    public boolean isExpired(){
       return startTimeSec == 0;
    }

    public int getFrameID(){
        return frameID;
    }

    @FXML
    public void shotFrameClicked() {
        primaryStage.setFullScreen(true);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
    }
}
