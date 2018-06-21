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
import org.openbooth.entities.Profile;
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

/**
 * Kamera Filter frame controller
 */
@Component
public class CameraFilterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerFrameController.class);


    @FXML
    private GridPane root;
    @FXML
    private Button singel;
    @FXML
    private Button serien;
    @FXML
    private Button ontime;
    @FXML
    private Label titel;
    @FXML
    private ScrollPane filterscrollplanel;


    private GridPane filtergrid;
    private GridPane greengrid;
    private int index;
    private int currentMode;
    private ImageView activiv=null;

    private FilterService filterService;
    private ProfileService profileservice;
    private WindowManager wm;
    private ShootingService shootingService;

    private Map<String,BufferedImage> filtermap=null;

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
     * inizialises chousenimage and buttonList
     * and findes aktuell profile
     */
    @FXML
    private void initialize(){
        filtermap = new HashMap<>();
    }

    /**
     * creats the filter selection for all kameras
     * marks the chousen filter
     */
    private void creatButtons(){
        try {
            filtergrid = new GridPane();
            filtergrid.prefWidth(Screen.getPrimary().getBounds().getWidth());
            filtergrid.setStyle("-fx-background-color: black;");
            filterscrollplanel.setStyle("-fx-background-color: black;");
            filterscrollplanel.setFitToWidth(true);
            filterscrollplanel.setFitToHeight(false);
            filterscrollplanel.prefWidth(Screen.getPrimary().getBounds().getWidth());

            filtergrid.getColumnConstraints().add(0,new ColumnConstraints());
            filtergrid.getRowConstraints().add(0,new RowConstraints());
            filtergrid.getColumnConstraints().add(1,new ColumnConstraints());
            filtergrid.getRowConstraints().add(1,new RowConstraints());
            filtergrid.getColumnConstraints().add(2,new ColumnConstraints());
            filtergrid.getRowConstraints().add(2,new RowConstraints());
            filtergrid.getColumnConstraints().add(3,new ColumnConstraints());
            filtergrid.getRowConstraints().add(3,new RowConstraints());
            filtergrid.getColumnConstraints().add(4,new ColumnConstraints());
            filtergrid.getRowConstraints().add(4,new RowConstraints());
            filtergrid.getColumnConstraints().add(5,new ColumnConstraints());
            filtergrid.getRowConstraints().add(5,new RowConstraints());

            filtergrid.getColumnConstraints().get(0).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getRowConstraints().get(0).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getColumnConstraints().get(1).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getRowConstraints().get(1).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getColumnConstraints().get(2).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getRowConstraints().get(2).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getColumnConstraints().get(3).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getRowConstraints().get(3).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getColumnConstraints().get(4).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getRowConstraints().get(4).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getColumnConstraints().get(5).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getRowConstraints().get(5).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);

            int columcount = 0;
            int rowcount = 0;


            if(filtermap==null||filtermap.isEmpty()){
                try {
                    String filterPreviewImagePath = wm.copyResource("/images/filterPreview.png");
                    filtermap = filterService.getAllFilteredImages(filterPreviewImagePath);
                } catch (IOException e) {
                    LOGGER.error("createButtons - could not copy preview filter image", e);
                }
            }

            for (Map.Entry<String, BufferedImage> filterentety: filtermap.entrySet()) {

                if (columcount == 6) {
                    rowcount++;
                    columcount = 0;
                }
                if(rowcount>=6){
                    filtergrid.getRowConstraints().add(rowcount,new RowConstraints());

                    filtergrid.getRowConstraints().get(rowcount).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
                }
                ImageView iv = new ImageView();
              iv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/6-10);
              iv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/6-10);

                iv.setStyle("-fx-background-color: green;");
                iv.setStyle("-fx-padding: 5;");//imagefilter.get(i).getImagepath()
              iv.setImage(SwingFXUtils.toFXImage(filterentety.getValue(), null));

                if(profileservice.getActiveProfile().getPairCameraPositions().get(index).getFilterName()!=null) {
                    if (filterentety.getKey().equals(profileservice.getActiveProfile().getPairCameraPositions().get(index).getFilterName())) {

                        activiv = iv;
                        activiv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/6-40);
                        activiv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/6-40);
                    }
                }

                iv.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    if(activiv!=null){
                        activiv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/6-10);
                        activiv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/6-10);
                    }
                    activiv=iv;
                    iv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/6-40);
                    iv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/6-40);

                    try {
                        profileservice.getActiveProfile().getPairCameraPositions().get(index).setFilterName(filterentety.getKey());
                    } catch (ServiceException e) {
                        LOGGER.error("create button -",e);
                    }

                });
                filtergrid.add(iv, columcount, rowcount);
                columcount++;
            }
            filtergrid.setVisible(true);
            filterscrollplanel.setVisible(true);
            filterscrollplanel.setContent(filtergrid);
            root.add(filterscrollplanel,0,1);
        } catch (ServiceException e) {
            LOGGER.error("creatButtons - ", e);
            showInformationDialog("Es konnte keine Filterauswahl erstellt werden");
        }
    }

    /**
     * loads the button
     */
    private void loadButton() {

        filtergrid.setVisible(true);
    }

    /**
     * creats image buttons for green screen image and marks chousen one
     */
    private void createGreenscreenButton(){

       FileInputStream fips=null;
        try {
            greengrid = new GridPane();
            greengrid.prefWidth(Screen.getPrimary().getBounds().getWidth());
            filterscrollplanel = new ScrollPane();
            greengrid.setStyle("-fx-background-color: black;");
            filterscrollplanel.setStyle("-fx-background-color: black;");
            filterscrollplanel.setFitToWidth(true);
            filterscrollplanel.setFitToHeight(false);
            filterscrollplanel.prefWidth(Screen.getPrimary().getBounds().getWidth());

            greengrid.getColumnConstraints().add(0, new ColumnConstraints());
            greengrid.getRowConstraints().add(0, new RowConstraints());
            greengrid.getColumnConstraints().add(1, new ColumnConstraints());
            greengrid.getRowConstraints().add(1, new RowConstraints());
            greengrid.getColumnConstraints().add(2, new ColumnConstraints());
            greengrid.getRowConstraints().add(2, new RowConstraints());
            greengrid.getColumnConstraints().add(3, new ColumnConstraints());
            greengrid.getRowConstraints().add(3, new RowConstraints());
            greengrid.getColumnConstraints().add(4, new ColumnConstraints());
            greengrid.getRowConstraints().add(4, new RowConstraints());
            greengrid.getRowConstraints().add(5, new RowConstraints());

            greengrid.getColumnConstraints().get(0).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greengrid.getRowConstraints().get(0).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
            greengrid.getColumnConstraints().get(1).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greengrid.getRowConstraints().get(1).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
            greengrid.getColumnConstraints().get(2).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greengrid.getRowConstraints().get(2).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
            greengrid.getColumnConstraints().get(3).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greengrid.getRowConstraints().get(3).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
            greengrid.getColumnConstraints().get(4).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greengrid.getRowConstraints().get(4).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
           // filtergrid.getColumnConstraints().get(5).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greengrid.getRowConstraints().get(5).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);

            int columcount = 0;
            int rowcount = 0;

            List<Background>  greenList =profileservice.getAllBackgroundOfProfile();
            shootingService.addUserDefinedBackgrounds(greenList);
            for (Background backround : greenList) {
                if (columcount == 5) {
                    rowcount++;
                    columcount = 0;
                    if(rowcount>=6){
                        greengrid.getRowConstraints().add(rowcount,new RowConstraints());
                        greengrid.getRowConstraints().get(rowcount).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
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
                        activiv = iv;
                        activiv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/5-40);
                        activiv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/5-40);
                    }
                }

                iv.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    if (activiv != null) {
                        activiv.setFitHeight(Screen.getPrimary().getBounds().getWidth() / 5 - 10);
                        activiv.setFitWidth(Screen.getPrimary().getBounds().getWidth() / 5 - 10);
                    }

                    activiv = iv;
                    iv.setFitHeight(Screen.getPrimary().getBounds().getWidth() / 5 - 40);
                    iv.setFitWidth(Screen.getPrimary().getBounds().getWidth() / 5 - 40);

                    try {
                        profileservice.getActiveProfile().getPairCameraPositions().get(index).setBackground(backround);
                    } catch (ServiceException e) {
                        LOGGER.error("creatgreenscreenbuttons - ",e);
                    }

                });
                greengrid.add(iv, columcount, rowcount);
                columcount++;
            }
            greengrid.setVisible(true);
            filterscrollplanel.setVisible(true);
            filterscrollplanel.setContent(greengrid);
            root.add(filterscrollplanel, 0, 1);
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
    public void onBackbuttonpressed() {
        wm.showScene(WindowManager.SHOW_CUSTOMERSCENE);
    }

    /**
     * on single image pressed
     */
    public void onSinglePressed() {
        try {
            unmark();

            profileservice.getActiveProfile().getPairCameraPositions().get(index).setShotType(Profile.PairCameraPosition.SHOT_TYPE_SINGLE);
            singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));

            currentMode = 0;
        } catch (ServiceException e) {
            LOGGER.error("onSinglePressed - ",e);
        }
    }

    /**
     * on serien pictures pressed
     */
    public void onSerialPressed() {
        try {
            unmark();

            profileservice.getActiveProfile().getPairCameraPositions().get(index).setShotType(Profile.PairCameraPosition.SHOT_TYPE_MULTIPLE);
            serien.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));

            currentMode = 1;
        } catch (ServiceException e) {
            LOGGER.error("onSerialPressed - ", e);
        }
    }

    /**
     * on time image pressed
     */
    public void onTimerPressed() {
        try {
            unmark();

            profileservice.getActiveProfile().getPairCameraPositions().get(index).setShotType(Profile.PairCameraPosition.SHOT_TYPE_TIMED);

            ontime.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));

            currentMode = 2;
        } catch (ServiceException e) {
            LOGGER.error("onTimerPressed - ",e );
        }
    }

    /**
     * gives currentMode
     * @return current mode (on time, single, serien)
     */
    public int getCurrentMode(){
        return  currentMode;
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
        titel.setText("");
        greengrid=new GridPane();
        if(filtergrid==null){
            filtergrid = new GridPane();
        }
            if(index>-1) {

                currentMode=profileservice.getActiveProfile().getPairCameraPositions().get(index).getShotType();
                markfirst();
                if (greenscreen) {
                    filtergrid.setVisible(false);
                    greengrid.setVisible(true);
                    titel.setText("Kamera " + profileservice.getActiveProfile().getPairCameraPositions().get(index).getPosition().getName() + " Hintergrund auswahl");
                    titel.setVisible(true);
                    createGreenscreenButton();
                } else {
                        greengrid.setVisible(false);
                        filtergrid.setVisible(true);
                        titel.setText("Kamera " + profileservice.getActiveProfile().getPairCameraPositions().get(index).getPosition().getName() + " Filter auswahl");
                        titel.setVisible(true);
                        creatButtons();
                }
            }
        } catch (ServiceException e) {
            LOGGER.debug("no camera pair found", e);
            showInformationDialog("Es konnte keine Kamera gefunden werden.");
        }
    }


    /**
     * markes the currently used item in gui
     */
    private void markfirst(){
        switch (currentMode) {
            case 0:
                singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                ontime.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                serien.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                break;
            case 1:
                serien.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                ontime.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                break;
            case 2:
                ontime.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                serien.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                break;
        }
    }

    /**
     * unmarks the old model
     * so the new model can be marked
     */
    private void unmark(){
        try {
            int i= currentMode;
            switch (i) {
                case 0:
                    singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                    break;
                case 1:
                    serien.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                    break;
                case 2:
                    ontime.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                    break;
            }
        }catch (NullPointerException n){
            LOGGER.error("unmark Button",n);
        }
    }

    /**
     * information dialog
     *
     * @param info String to be shown as error message to the user
     */
    public void showInformationDialog(String info){
        Alert information = new Alert(Alert.AlertType.INFORMATION, info);
        information.setHeaderText("Ein Fehler ist Aufgetreten");
        information.initOwner(wm.getStage());
        information.show();
    }

    /**
     * Trigger shot while in filer mode
     * @param keyEvent of key pressed
     */
    public void triggerShot(KeyEvent keyEvent) {
        try {
            this.cameraTrigger.triggerShotIfCorrectKey(keyEvent);
        } catch (TriggerException e) {
            //TODO: add error message for the user
            LOGGER.error("Unable to take shot.",e);
        }
    }

}