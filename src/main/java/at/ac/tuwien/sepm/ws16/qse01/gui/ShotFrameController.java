package at.ac.tuwien.sepm.ws16.qse01.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

/**
 * Controller for the shotFrame.
 *
 */
/*@Controller*/

public class ShotFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShotFrameController.class);
    @FXML
    private BorderPane pane;
    @FXML
    private ImageView shotView;
    @FXML
    private Label countdownLabel;

    private int frameID;
    private Stage primaryStage;

    @FXML
    private void initialize(){
        double screenWidth= Screen.getPrimary().getBounds().getWidth();
        double screenHeight=Screen.getPrimary().getBounds().getHeight();
        pane.setPrefHeight(screenHeight);
        pane.setPrefWidth(screenWidth);
        shotView.setFitHeight(screenHeight);
        shotView.setFitWidth(screenWidth);

    }
    public void initShotFrame(int cameraID,Stage primaryStage)  {
        this.frameID  = cameraID;
        this.primaryStage = primaryStage;
       // showCountdown(10);
    }

    /*
     Precondition: shootingID must be defined
     Postcondition: the last image will be showed
     */
    /** showing the last image taken
     *
     */
    @FXML
    public void refreshShot(String imgPath) {

        LOGGER.debug("refreshing Shot with imagepath = "+imgPath);
        try {
            shotView.setImage(new Image(imgPath));
        }catch (Exception e){
            LOGGER.debug("refreshShot(String) - Fehler - ",e);
        }
    }

    public void refreshShot(BufferedImage img) {

        LOGGER.debug("refreshing Shot with Buffered img = "+img);
        try {
            shotView.setImage(SwingFXUtils.toFXImage(img,null));
        }catch (Exception e){
            LOGGER.debug("refreshShot(BufferedImage) - Fehler - ",e);
        }
    }
    public void showCountdown(int countdown){
        countdownLabel.setText(String.valueOf(countdown));
        countdownLabel.setVisible(true);
        double screenWidth= Screen.getPrimary().getBounds().getWidth();
        double screenHeight=Screen.getPrimary().getBounds().getHeight()-100;
        countdownLabel.setPrefHeight(screenHeight/2);
        // countdownLabel.setPrefWidth((screenWidth/2));

        int paddingBottom =  -((Double)(screenHeight/2)).intValue();
        countdownLabel.setPadding(new Insets(0, 0,paddingBottom,0));
        createCounter(countdown).play();



    }
    public Timeline createCounter(int countdown){
        Timeline timeline = new Timeline();
        final int[] startTimeSec = {countdown};
        KeyFrame keyframe = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                startTimeSec[0]--;



                if (startTimeSec[0]==0) {
                    timeline.stop();
                    countdownLabel.setVisible(false);
                }

                countdownLabel.setText(String.valueOf(startTimeSec[0]));

            }
        });


        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyframe);
        return timeline;
    }

    public int getFrameID(){
        return frameID;
    }

    @FXML
    public void shotFrameClicked(MouseEvent mouseEvent) {
        primaryStage.setFullScreen(true);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
    }
}
