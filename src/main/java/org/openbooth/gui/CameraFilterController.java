package org.openbooth.gui;

import javafx.scene.input.KeyEvent;
import org.openbooth.entities.Background;
import org.openbooth.service.FilterService;
import org.openbooth.service.ProfileService;
import org.openbooth.service.ShootingService;
import org.openbooth.service.exceptions.ServiceException;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import org.openbooth.util.CameraTrigger;
import org.openbooth.util.exceptions.TriggerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openbooth.entities.Profile.PairCameraPosition.SHOT_TYPE_MULTIPLE;
import static org.openbooth.entities.Profile.PairCameraPosition.SHOT_TYPE_SINGLE;
import static org.openbooth.entities.Profile.PairCameraPosition.SHOT_TYPE_TIMED;

/**
 * Kamera Filter frame controller
 */
@Component
public class CameraFilterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerFrameController.class);
    private static final String MODE_FRAME_COLOR = "green";


    @FXML
    private Button singleButton;
    @FXML
    private Button burstModeButton;
    @FXML
    private Button onTimeButton;
    @FXML
    private Label title;
    @FXML
    private ScrollPane filterScrollPanel;


    private GridPane filterGrid;
    private GridPane greenScreenGrid;
    private int index;
    private int currentMode;
    private ImageView activeIv = null;

    private FilterService filterService;
    private ProfileService profileservice;
    private WindowManager wm;
    private ShootingService shootingService;

    private Map<String,BufferedImage> filterMap = null;

    private CameraTrigger cameraTrigger;

    @Autowired

    public CameraFilterController(FilterService filterService, ProfileService profileService, WindowManager wm, ShootingService shootingService, CameraTrigger cameraTrigger){
        this.profileservice=profileService;
        this.wm=wm;
        this.shootingService = shootingService;
        this.filterService=filterService;
        this.cameraTrigger = cameraTrigger;
    }


    /**
     * Initialises chosen image and buttonList and finds current profile
     */
    @FXML
    private void initialize(){
        filterMap = new HashMap<>();
    }

    /**
     * Creates the filter selection for all cameras
     * Marks the chosen filter
     */
    private void createButtons(){
        try {
            filterGrid = new GridPane();
            filterGrid.prefWidth(Screen.getPrimary().getBounds().getWidth());
            filterGrid.setStyle("-fx-background-color: black;");
            filterScrollPanel.setStyle("-fx-background-color: black;");
            filterScrollPanel.setFitToWidth(true);
            filterScrollPanel.setFitToHeight(false);
            filterScrollPanel.prefWidth(Screen.getPrimary().getBounds().getWidth());

            filterGrid.getColumnConstraints().add(0,new ColumnConstraints());
            filterGrid.getRowConstraints().add(0,new RowConstraints());
            filterGrid.getColumnConstraints().add(1,new ColumnConstraints());
            filterGrid.getRowConstraints().add(1,new RowConstraints());
            filterGrid.getColumnConstraints().add(2,new ColumnConstraints());
            filterGrid.getRowConstraints().add(2,new RowConstraints());
            filterGrid.getColumnConstraints().add(3,new ColumnConstraints());
            filterGrid.getRowConstraints().add(3,new RowConstraints());
            filterGrid.getColumnConstraints().add(4,new ColumnConstraints());
            filterGrid.getRowConstraints().add(4,new RowConstraints());
            filterGrid.getColumnConstraints().add(5,new ColumnConstraints());
            filterGrid.getRowConstraints().add(5,new RowConstraints());

            filterGrid.getColumnConstraints().get(0).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filterGrid.getRowConstraints().get(0).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filterGrid.getColumnConstraints().get(1).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filterGrid.getRowConstraints().get(1).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filterGrid.getColumnConstraints().get(2).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filterGrid.getRowConstraints().get(2).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filterGrid.getColumnConstraints().get(3).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filterGrid.getRowConstraints().get(3).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filterGrid.getColumnConstraints().get(4).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filterGrid.getRowConstraints().get(4).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filterGrid.getColumnConstraints().get(5).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filterGrid.getRowConstraints().get(5).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);

            int columcount = 0;
            int rowcount = 0;


            if(filterMap ==null|| filterMap.isEmpty()){
                try {
                    String filterPreviewImagePath = wm.copyResource("/images/filterPreview.png");
                    filterMap = filterService.getAllFilteredImages(filterPreviewImagePath);
                } catch (IOException e) {
                    LOGGER.error("createButtons - could not copy preview filter image", e);
                }
            }

            for (Map.Entry<String, BufferedImage> filterEntity: filterMap.entrySet()) {

                if (columcount == 6) {
                    rowcount++;
                    columcount = 0;
                }
                if(rowcount>=6){
                    filterGrid.getRowConstraints().add(rowcount,new RowConstraints());

                    filterGrid.getRowConstraints().get(rowcount).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
                }
                ImageView iv = new ImageView();
                iv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/6-10);
                iv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/6-10);

                iv.setStyle("-fx-background-color: green;");
                iv.setStyle("-fx-padding: 5;");//imagefilter.get(i).getImagepath()
                iv.setImage(SwingFXUtils.toFXImage(filterEntity.getValue(), null));

                String filterName = profileservice.getActiveProfile().getPairCameraPositions().get(index).getFilterName();

                if (filterEntity.getKey().equals(filterName)) {

                    activeIv = iv;
                    activeIv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/6-40);
                    activeIv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/6-40);
                }

                iv.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    if(activeIv !=null){
                        activeIv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/6-10);
                        activeIv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/6-10);
                    }
                    activeIv =iv;
                    iv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/6-40);
                    iv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/6-40);

                    try {
                        profileservice.getActiveProfile().getPairCameraPositions().get(index).setFilterName(filterEntity.getKey());
                    } catch (ServiceException e) {
                        LOGGER.error("create button -",e);
                    }

                });
                filterGrid.add(iv, columcount, rowcount);
                columcount++;
            }

            filterGrid.setVisible(true);
            filterScrollPanel.setVisible(true);
            filterScrollPanel.setContent(filterGrid);
        } catch (ServiceException e) {
            LOGGER.error("createButtons - ", e);
            showInformationDialog("Es konnte keine Filterauswahl erstellt werden");
        }
    }

    /**
     * Creates image buttons for green screen image and marks chosen one
     */
    private void createGreenscreenButton(){

       FileInputStream fips=null;
        try {
            greenScreenGrid = new GridPane();
            greenScreenGrid.prefWidth(Screen.getPrimary().getBounds().getWidth());
            filterScrollPanel = new ScrollPane();
            greenScreenGrid.setStyle("-fx-background-color: black;");
            filterScrollPanel.setStyle("-fx-background-color: black;");
            filterScrollPanel.setFitToWidth(true);
            filterScrollPanel.setFitToHeight(false);
            filterScrollPanel.prefWidth(Screen.getPrimary().getBounds().getWidth());

            greenScreenGrid.getColumnConstraints().add(0, new ColumnConstraints());
            greenScreenGrid.getRowConstraints().add(0, new RowConstraints());
            greenScreenGrid.getColumnConstraints().add(1, new ColumnConstraints());
            greenScreenGrid.getRowConstraints().add(1, new RowConstraints());
            greenScreenGrid.getColumnConstraints().add(2, new ColumnConstraints());
            greenScreenGrid.getRowConstraints().add(2, new RowConstraints());
            greenScreenGrid.getColumnConstraints().add(3, new ColumnConstraints());
            greenScreenGrid.getRowConstraints().add(3, new RowConstraints());
            greenScreenGrid.getColumnConstraints().add(4, new ColumnConstraints());
            greenScreenGrid.getRowConstraints().add(4, new RowConstraints());
            greenScreenGrid.getRowConstraints().add(5, new RowConstraints());

            greenScreenGrid.getColumnConstraints().get(0).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greenScreenGrid.getRowConstraints().get(0).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
            greenScreenGrid.getColumnConstraints().get(1).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greenScreenGrid.getRowConstraints().get(1).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
            greenScreenGrid.getColumnConstraints().get(2).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greenScreenGrid.getRowConstraints().get(2).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
            greenScreenGrid.getColumnConstraints().get(3).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greenScreenGrid.getRowConstraints().get(3).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
            greenScreenGrid.getColumnConstraints().get(4).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greenScreenGrid.getRowConstraints().get(4).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
           // filterGrid.getColumnConstraints().get(5).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greenScreenGrid.getRowConstraints().get(5).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);

            int columcount = 0;
            int rowcount = 0;

            List<Background>  greenList =profileservice.getAllBackgroundOfProfile();
            shootingService.addUserDefinedBackgrounds(greenList);
            for (Background backround : greenList) {
                if (columcount == 5) {
                    rowcount++;
                    columcount = 0;
                    if(rowcount>=6){
                        greenScreenGrid.getRowConstraints().add(rowcount,new RowConstraints());
                        greenScreenGrid.getRowConstraints().get(rowcount).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
                    }
                }

                ImageView iv = new ImageView();
                iv.setFitHeight(Screen.getPrimary().getBounds().getWidth() / 5 - 10);
                iv.setFitWidth(Screen.getPrimary().getBounds().getWidth() / 5 - 10);


                iv.setStyle("-fx-padding: 5;");
                fips=new FileInputStream(backround.getPath());
                iv.setImage(new javafx.scene.image.Image(fips, iv.getFitHeight(), iv.getFitWidth(), true, true));
                fips.close();
                if(profileservice.getActiveProfile().getPairCameraPositions().get(index).getBackground()!=null) {
                    if (backround.getId()==profileservice.getActiveProfile().getPairCameraPositions().get(index).getBackground().getId()) {
                        activeIv = iv;
                        activeIv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/5-40);
                        activeIv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/5-40);
                    }
                }

                iv.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    if (activeIv != null) {
                        activeIv.setFitHeight(Screen.getPrimary().getBounds().getWidth() / 5 - 10);
                        activeIv.setFitWidth(Screen.getPrimary().getBounds().getWidth() / 5 - 10);
                    }

                    activeIv = iv;
                    iv.setFitHeight(Screen.getPrimary().getBounds().getWidth() / 5 - 40);
                    iv.setFitWidth(Screen.getPrimary().getBounds().getWidth() / 5 - 40);

                    try {
                        profileservice.getActiveProfile().getPairCameraPositions().get(index).setBackground(backround);
                    } catch (ServiceException e) {
                        LOGGER.error("creatgreenscreenbuttons - ",e);
                    }

                });
                greenScreenGrid.add(iv, columcount, rowcount);
                columcount++;
            }
            greenScreenGrid.setVisible(true);
            filterScrollPanel.setVisible(true);
            filterScrollPanel.setContent(greenScreenGrid);
        } catch (ServiceException|IOException e) {
            LOGGER.error("Camers Filter, greenscreen creat button- ", e);
        } finally{
            if(fips!=null){
                try {
                    fips.close();
                } catch (IOException e) {
                    LOGGER.debug("createGreenScreenButton - unable to close FileInputStream - ",e);
                }
            }
        }
    }

    /**
     * goes back to customer frame
     */
    @FXML
    public void onBackButtonPressed() {
        wm.showScene(WindowManager.SHOW_CUSTOMERSCENE);
    }

    /**
     * on single image pressed
     */
    @FXML
    public void onSinglePressed() {
        try {
            profileservice.getActiveProfile().getPairCameraPositions().get(index).setShotType(SHOT_TYPE_SINGLE);
            currentMode = SHOT_TYPE_SINGLE;
            markCurrentMode();
        } catch (ServiceException e) {
            LOGGER.error("onSinglePressed - ",e);
        }
    }

    /**
     * on serien pictures pressed
     */
    @FXML
    public void onSerialPressed() {
        try {
            profileservice.getActiveProfile().getPairCameraPositions().get(index).setShotType(SHOT_TYPE_MULTIPLE);
            currentMode = SHOT_TYPE_MULTIPLE;
            markCurrentMode();
        } catch (ServiceException e) {
            LOGGER.error("onSerialPressed - ", e);
        }
    }

    /**
     * on time image pressed
     */
    @FXML
    public void onTimerPressed() {
        try {

            profileservice.getActiveProfile().getPairCameraPositions().get(index).setShotType(SHOT_TYPE_TIMED);
            currentMode = SHOT_TYPE_TIMED;
            markCurrentMode();

        } catch (ServiceException e) {
            LOGGER.error("onTimerPressed - ",e );
        }
    }

    /**
     * decides whether an new filter image is chosen or green screen
     * @param index current mode
     * @param idFilter current filter id
     * @param greenscreen boolean green screen or not
     */
    public void currentlyChosen(int index, int idFilter, boolean greenscreen){
        try{
        this.index=index;
        title.setText("");
        greenScreenGrid =new GridPane();
        if(filterGrid ==null){
            filterGrid = new GridPane();
        }
            if(index>-1) {

                currentMode=profileservice.getActiveProfile().getPairCameraPositions().get(index).getShotType();
                markCurrentMode();
                if (greenscreen) {
                    filterGrid.setVisible(false);
                    greenScreenGrid.setVisible(true);
                    title.setText("Kamera " + profileservice.getActiveProfile().getPairCameraPositions().get(index).getPosition().getName() + " Hintergrundauswahl");
                    title.setVisible(true);
                    createGreenscreenButton();
                } else {
                        greenScreenGrid.setVisible(false);
                        filterGrid.setVisible(true);
                        title.setText("Kamera " + profileservice.getActiveProfile().getPairCameraPositions().get(index).getPosition().getName() + " Filterauswahl");
                        title.setVisible(true);
                        createButtons();
                }
            }
        } catch (ServiceException e) {
            LOGGER.debug("no simcam pair found", e);
            showInformationDialog("Es konnte keine Kamera gefunden werden.");
        }
    }


    /**
     * Marks the currently used mode in GUI (single shot, multiple shots, timed shot) and unmarks the old one
     */
    private void markCurrentMode(){
        switch (currentMode) {
            case SHOT_TYPE_MULTIPLE:
                burstModeButton.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf(MODE_FRAME_COLOR), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                onTimeButton.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                singleButton.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                break;
            case SHOT_TYPE_TIMED:
                onTimeButton.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf(MODE_FRAME_COLOR), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                singleButton.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                burstModeButton.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                break;
             default:
                 //SHOT_TYPE_SINGLE or anything else
                 singleButton.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf(MODE_FRAME_COLOR), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                 onTimeButton.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                 burstModeButton.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                 break;
        }
    }

    /**
     * information dialog
     *
     * @param info String to be shown as error message to the user
     */
    private void showInformationDialog(String info){
        Alert information = new Alert(Alert.AlertType.INFORMATION, info);
        information.setHeaderText("Ein Fehler ist Aufgetreten");
        information.initOwner(wm.getStage());
        information.show();
    }

    /**
     * Trigger shot while in filer mode
     * @param keyEvent of key pressed
     */
    @FXML
    public void triggerShot(KeyEvent keyEvent) {
        try {
            this.cameraTrigger.triggerShotIfCorrectKey(keyEvent);
        } catch (TriggerException e) {
            //TODO: add error message for the user
            LOGGER.error("Unable to take shot.",e);
        }
    }

}