package org.openbooth.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;
import java.util.List;

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

    private Stage primaryStage;

    private int startTimeSec = 0;
    @FXML
    private void initialize(){
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        shotView.setFitHeight(screenHeight);

    }
    void initShotFrame(Stage primaryStage)  {
        this.primaryStage = primaryStage;
        this.primaryStage.setResizable(true);
    }

    public void refreshShot(BufferedImage img) {
        shotView.setImage(SwingFXUtils.toFXImage(img,null));
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

    @FXML
    public void shotFrameClicked() {

        Screen screen = getScreenOfWindow();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        StackPane root = (StackPane) primaryStage.getScene().getRoot();
        Node child = root.getChildren().get(0);

        ImageView iv;

        if (child instanceof ImageView){
            iv = (ImageView) child;
        } else {
            iv = (ImageView) root.getChildren().get(1);
        }

        iv.setFitHeight(bounds.getHeight());
        iv.setFitWidth(bounds.getWidth());
        primaryStage.setFullScreen(true);

    }

    private Screen getScreenOfWindow(){
        double stageX = primaryStage.getX();
        double stageY = primaryStage.getY();
        double stageWidth = primaryStage.getWidth();
        double stageHeight = primaryStage.getHeight();

        List<Screen> intersectScreens = Screen.getScreensForRectangle(stageX, stageY, stageWidth, stageHeight);

        Screen chosenScreen;

        if(intersectScreens.isEmpty()){
            chosenScreen = Screen.getPrimary();
            LOGGER.debug("getScreenOfWindow - stage of ShotFrame outside of all screens, primary was selected as target screen. Resolution: {} x {}", chosenScreen.getBounds().getWidth(), chosenScreen
            .getBounds().getHeight());
        } else {
            chosenScreen = intersectScreens.get(0);
            LOGGER.debug("getScreenOfWindow - resolution of selected screen: {} x {}", chosenScreen.getBounds().getWidth(), chosenScreen
                    .getBounds().getHeight());
        }

        return chosenScreen;
    }
}
