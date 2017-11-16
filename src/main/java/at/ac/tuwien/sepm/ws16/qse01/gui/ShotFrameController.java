package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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

    private ProfileService profileservice;
    private CameraHandler cameraHandler;

    final int[] startTimeSec = new int[]{0};
    @Autowired
    public ShotFrameController(CameraHandler cameraHandler, ProfileService profileService){
        this.profileservice = profileService;
        this.cameraHandler=cameraHandler;
    }

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

        double screenWidth= Screen.getPrimary().getBounds().getWidth();
        double screenHeight=Screen.getPrimary().getBounds().getHeight()-100;
        countdownLabel.setPrefHeight(screenHeight/2);

        int paddingBottom =  -((Double)(screenHeight/2)).intValue();
        countdownLabel.setPadding(new Insets(0, 0,paddingBottom,0));
        createCounter(countdown).play();
    }

    public Timeline createCounter(int countdown){
        Timeline timeline = new Timeline();
        startTimeSec[0] = countdown;
        KeyFrame keyframe = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                startTimeSec[0]--;
                if (startTimeSec[0]==0) {
                    timeline.stop();
                    countdownLabel.setVisible(false);
                }else{
                    countdownLabel.setText(String.valueOf(startTimeSec[0]));
                    countdownLabel.setVisible(true);
                }
            }
        });


        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyframe);
        return timeline;
    }
    public boolean isExpired(){
       return startTimeSec[0]==0?true:false;
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

    public void triggerShot(KeyEvent keyEvent){
        String keystoke = keyEvent.getText();

        int index = -1;
        String messageString = "";

        switch (keystoke){
            case "1" : index = 0;break;
            case "2" : index = 1;break;
            case "3" : index = 2;break;
            case "4" : index = 3;break;
            case "5" : index = 4;break;
            case "6" : index = 5;break;
            case "7" : index = 6;break;
            case "8" : index = 7;break;
            case "9" : index = 8;break;
            default: index = -1;return;
        }
        LOGGER.debug("triggerShot with keyEventCharacter " + keystoke);
        int numberOfPositions = 0;
        int numberOfCameras = 0;
        Profile.PairCameraPosition pairCameraPosition = null;
        Profile activeProfile = null;

        List<Camera> cameras = new ArrayList<>();
        try {
            if (profileservice != null) {activeProfile = profileservice.getActiveProfile();
                numberOfPositions = activeProfile.getPairCameraPositions().size();}
        } catch (ServiceException e) {
            activeProfile = null;
            LOGGER.error("Active Profile couldn't be determined, thus null value will be assumed", e);
        }
        String os = System.getProperty("os.name");
        try {
            if (cameraHandler != null && !os.startsWith("Windows")) {cameras = cameraHandler.getCameras();numberOfCameras = cameras.size();}
        } catch (CameraException e) {
            cameras = new ArrayList<>();
            LOGGER.error("Cameras couldn't be determined, thus an empty List will be assumed", e);
        }


        if(index >= 0)
        {messageString = "triggerCall - Attempting to trigger camera object at paitcameraposition list index " + index + " because of valid trigger sequence{}";}
        else
        {messageString = "triggerCall - No action is attempted to be triggered associated to trigger sequence{}";}
        LOGGER.debug(messageString,keystoke);

        if( numberOfPositions > index && index >= 0 ){
            messageString = "triggerCall - Camera is at this index present and an image capture is triggered";
            //cameraHandler.captureImage(cameras.get(cameraIndex));
            pairCameraPosition = activeProfile.getPairCameraPositions().get(index);
            int shotType = pairCameraPosition.getShotType();
            Camera camera = pairCameraPosition.getCamera();
            if (shotType == Profile.PairCameraPosition.SHOT_TYPE_MULTIPLE){
                if (cameras.contains(camera)) {
                    cameraHandler.setSerieShot(camera,true);
                    LOGGER.debug("triggerCall - multiple shot has been set");
                }
                else {LOGGER.debug("triggerCall - multiple shot setting not possible, cause no cameraHandler available");}
            }
            else if (shotType == Profile.PairCameraPosition.SHOT_TYPE_TIMED) {
                if (cameras.contains(camera)) {
                    cameraHandler.setCountdown(camera,8);
                    LOGGER.debug("triggerCall - timed shot has been set");
                } else {
                    LOGGER.debug("triggerCall - timed shot setting not possible, cause no cameraHandler available");
                }
            } else {
                cameraHandler.setCountdown(camera, 0);
                cameraHandler.setSerieShot(camera, false);
                LOGGER.debug("triggerCall - standard shot will be kept set");
            }

            if (cameras.contains(camera)){
                cameraHandler.captureImage(camera);
                return;
            }
            else {
                LOGGER.debug("triggerCall - Camera that has been triggered is not in cameraHandlers list");
                return;
            }
        }
        else if(index >= 0){
            messageString = "triggerCall - No camera at this index found, so no action will be triggered";
        }
        else {
            messageString = "triggerCall - Trigger sequence is invalid";
        }
        LOGGER.debug(messageString);
    }
}
