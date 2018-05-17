package org.openbooth.gui;

import org.openbooth.util.ImageHandler;
import org.openbooth.util.KeyHandler;
import org.openbooth.util.TempStorageHandler;
import org.openbooth.util.exceptions.ImageHandlingException;
import org.openbooth.util.printer.ImagePrinter;
import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.exeptions.CameraException;
import org.openbooth.entities.Camera;
import org.openbooth.entities.Profile;
import org.openbooth.service.FilterService;
import org.openbooth.service.ImageService;
import org.openbooth.service.ProfileService;
import org.openbooth.service.ShootingService;
import org.openbooth.service.exceptions.ServiceException;
import javafx.animation.FadeTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * full screen image view
 */
@Component
public class FullScreenImageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullScreenImageController.class);

    @FXML
    private GridPane base;
    @FXML
    private GridPane mainPane;
    @FXML
    private GridPane wholePane;
    @FXML
    private Pane planetop;
    @FXML
    private AnchorPane forCropping;

    private GridPane planbottom;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button9;
    @FXML
    private Button button7;
    @FXML
    private Button button8;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button2;
    @FXML
    private Button button13;
    @FXML
    private ImageView image4;
    @FXML
    private ImageView image3;
    @FXML
    private ImageView ivfullscreenImage;
    @FXML
    private Button saveFilteredButton;
    @FXML
    private Button button1;
    @FXML
    private Button button11;
    @FXML
    private Button button12;




    private boolean b3=true;
    private boolean b4=true;
    private boolean upperbutton=false;

    private List<org.openbooth.entities.Image> imageList;
    private int currentIndex=-1;
    private int activ;

    private ImageView activeFilterImageView;
    private ImageView original;
    private String filteredImgPath= null;
    private boolean constraintInitialized = false;

    private FilterService filterService;
    private ImageService imageService;
    private ShootingService shootingService;
    private WindowManager windowManager;
    private ImagePrinter imagePrinter;
    private Rectangle cropRectangle = null;
    private Circle resizeHandleNW = null;
    private Circle resizeHandleSE = null;
    private boolean cropping=false;
    private RefreshManager refreshManager;
    private ImageHandler imageHandler;
    private CameraHandler cameraHandler;
    private ProfileService profileservice;

    String tempStorageDir;

    @Autowired
    public FullScreenImageController(WindowManager windowManager, ShootingService shootingService, FilterService filterService, ImageService imageService, ImagePrinter imagePrinter, ImageHandler imageHandler, RefreshManager refreshManager, TempStorageHandler tempStorageHandler, CameraHandler cameraHandler, ProfileService profileservice) throws ServiceException {
        this.filterService = filterService;
        this.imageService=imageService;
        this.shootingService= shootingService;
        this.windowManager=windowManager;
        this.imagePrinter=imagePrinter;
        this.refreshManager=refreshManager;
        this.imageHandler = imageHandler;
        this.cameraHandler = cameraHandler;
        this.profileservice = profileservice;

        this.tempStorageDir = tempStorageHandler.getTempStoragePath();
    }

    /**
     * iniziaising full screen image view
     * if the List == null and there is an activ shooting avalible the imageList gets initialised
     * if the list is not empty, the chosen image gets displayed
     *
     * catches ServiceException which can be thrown by all metodes requering an Service class
     * catches FileNotFoundException which can be thrown by all FileInputStream´s
     */
    @FXML
    private void initialize(){
        planbottom = new GridPane();
        planbottom.setPrefHeight(100);
        planbottom.setStyle("-fx-background-color: rgba(50,50, 50, 0.5)");
    }

    @FXML
    public void onPrintPressed(){
        LOGGER.debug("onPrintPressed() - print pressed");
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                "Möchten Sie das Bild drucken?");
        alert.setHeaderText("Bild drucken");
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(windowManager.getStage());
        Optional<ButtonType> result =alert.showAndWait();
        if(result.isPresent()&&result.get()==ButtonType.OK){
            boolean failure=false;
            try {
                if(!imageList.isEmpty()&&imageList.size()>currentIndex&&currentIndex>=0) {
                    imagePrinter.print(imageList.get(currentIndex));
                } else{
                    LOGGER.error("onPrintPressed - Index out of bounds or list empty.");
                    failure=true;
                }
            } catch (PrinterException e) {
                LOGGER.error("onPrintPressed - ", e);
                failure=true;
            }
            if(failure){
                Alert alertError= new Alert(Alert.AlertType.ERROR,
                        "Während des Druckvorgangs ist ein Fehler aufgetreten. Leider kann das Foto nicht gedruckt werden.");
                alertError.setHeaderText("Fehler beim Druckvorgang");
                alertError.initOwner(windowManager.getStage());
                alertError.show();
            }
        }
    }

    /**
     * deletes the chosen image and decides which image should be shown next
     * in case the user deleted the last image there is no next image, so the last image will be shown
     * in case the user deleted the first image it will do vice versa
     * in case the user deleted the only image, the full screen image show will close
     *
     * catches ServiceException which can be drown by all service methodes
     * catches FileNotFoundException witch can be drown by all file methodes
     */
    @FXML
    public void onDeletePressed() {
    try {
            if(ivfullscreenImage!=null&&imageList!=null){

                windowManager.showDeleteScene(true ,new Image(new FileInputStream(imageList.get(currentIndex).getImagepath()), 500, 500, true, true));

            }
        } catch (FileNotFoundException e) {
            LOGGER.error("onDeletePressed - ", e);
        }
    }

    /**
     * notification if image should be deleted
     * @param delete notification
     */
    public void shouldBeDeleted(boolean delete){
        try {
            if (delete) {
                org.openbooth.entities.Image image = imageList.get(currentIndex);
                imageService.delete(image.getImageID());

                if (currentIndex < imageList.size() - 1) {
                    onNextImage();
                    currentIndex--;
                } else {
                    if (currentIndex > 0) {
                        onLastImagePressed();
                    } else {
                        onClosePressed();
                    }
                }
                if (activ != -1) {
                    imageList = imageService.getAllImages(activ);
                    refreshManager.notifyMiniatureFrameOfDelete(image);
                } else {
                    LOGGER.debug("no active shooting");
                    informationDialog("Bild konnte nicht gelöscht werden.");
                }
            }
        } catch (ServiceException e) {
            LOGGER.error("onDeletePressed - ",e);
            informationDialog("Bild konnte nicht gelöscht werden.");
        }
    }

    /**
     * if cropping then stop cropping else
     * closes full screen and opens miniatur sceen again
     * before doing so it sets currentIndex to -1 to overcome possible null pointer exeptions
     */
    @FXML
    public void onClosePressed(){
        if(cropping)
        {
            cropping = false;
            cropRectangle.setVisible(false);
            resizeHandleNW.setVisible(false);
            resizeHandleSE.setVisible(false);
            cropRectangle = null;
            resizeHandleSE = null;
            resizeHandleNW = null;
        }
        windowManager.showScene(WindowManager.SHOW_MINIATURESCENE);
    }

    /**
     * information dialog for error messages
     *
     * @param info String to be presented to the user as error message
     */
    public void informationDialog(String info){
        Alert information = new Alert(Alert.AlertType.INFORMATION, info);
        information.setHeaderText("Ein Fehler ist Aufgetreten");
        information.initOwner(windowManager.getStage());
        information.show();
    }

    /**
     * basic transition method to be used in both directions (in and out)
     *
     * @param imageView the imageView the transition is made on
     * @param fromValue sets the from value of the translation
     * @param toValue sets the to value of the translation
     * @param durationInMilliseconds sets the time it takes
     * @return transision to be performed
     */
    public FadeTransition getFadeTransition(ImageView imageView, double fromValue, double toValue, int durationInMilliseconds) {

        FadeTransition ft = new FadeTransition(Duration.millis(durationInMilliseconds), imageView);
        ft.setFromValue(fromValue);
        ft.setToValue(toValue);

        return ft;

    }

    /**
     * gets last image (image when swiped left)
     * there by using transitions for visual purpose
     *
     * catches FillNotFound exeption
     *
     */
    public void onLastImagePressed() {
        upperbutton = true;
        FileInputStream fips=null;
        try {
            deletePreviews(ivfullscreenImage.getId());

            if (currentIndex-1 > -1) {
                if (currentIndex - 1 > 0) {
                    currentIndex = currentIndex - 1;
                }else if (currentIndex - 1 == 0) {
                    currentIndex = 0;
                    b4=false;
                    button4.setVisible(false);
                }
                if(!button3.isVisible()&& imageList.size()>1){
                    b3=true;
                    button3.setVisible(true);
                }
                fips=new FileInputStream(imageList.get(currentIndex).getImagepath());
                ivfullscreenImage.setImage(new Image(fips, base.getWidth(), base.getHeight(), true, true));
                ivfullscreenImage.setId(imageList.get(currentIndex).getImagepath());
                makePreviewFilter(imageList.get(currentIndex).getImagepath());
                saveFilteredButton.setVisible(false);
            } else {
                windowManager.showScene(WindowManager.SHOW_MINIATURESCENE);
            }
        } catch (IOException e) {
            LOGGER.error("onLastImagePressed - ",e);
            Alert alertError= new Alert(Alert.AlertType.ERROR,
                    "Das vorhergehende Bild konnte nicht gelesen werden.");
            alertError.setHeaderText("Fehler beim Lesevorgang");
            alertError.initOwner(windowManager.getStage());
            alertError.show();
        } finally{
            if(fips!=null){
                try {
                    fips.close();
                } catch (IOException e) {
                    LOGGER.error("onLastImagePressed - ",e);
                }
            }
        }

    }

    /**
     * get next image (image when deleted or swiped right)
     * there by using transitions for visual purpose
     *
     * catches FillNotFound exeption
     *
     */
    public void onNextImage() {
        upperbutton = true;
        FileInputStream fips=null;
        try {
            deletePreviews(ivfullscreenImage.getId());

            if (currentIndex+1 < imageList.size()) {
                if (currentIndex + 1 < imageList.size()-1) {
                    currentIndex = currentIndex +1;
                }else if (currentIndex + 1 == imageList.size()-1) {
                    currentIndex = imageList.size()-1;
                    b3=false;
                    button3.setVisible(false);
                }
                if(!button4.isVisible()&& imageList.size()>1){
                    b4=true;
                    button4.setVisible(true);
                }
                fips=new FileInputStream(imageList.get(currentIndex).getImagepath());
                ivfullscreenImage.setImage(new Image(fips,  base.getWidth(), base.getHeight(), true, true));
                ivfullscreenImage.setId(imageList.get(currentIndex).getImagepath());
                makePreviewFilter(imageList.get(currentIndex).getImagepath());
                saveFilteredButton.setVisible(false);

            } else {
                windowManager.showScene(WindowManager.SHOW_MINIATURESCENE);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("onNextImage - ",e);
            Alert alertError= new Alert(Alert.AlertType.ERROR,
                    "Das nächste Bild konnte nicht gelesen werden.");
            alertError.setHeaderText("Fehler beim Lesevorgang");
            alertError.initOwner(windowManager.getStage());
            alertError.show();
        } finally{
            if(fips!=null){
                try {
                    fips.close();
                } catch (IOException e) {
                    LOGGER.error("onNextImage - ",e);
                }
            }
        }

    }

    /**
     * to get an full screen image without buttons
     * and reload the buttons when pressed again
     * this is achieved by seting visiblety
     */
    public void onfullScreenPressed () {

        if (!upperbutton) {
            if (planbottom.isVisible()) {
                planbottom.setVisible(false);
                planetop.setVisible(false);
                button3.setVisible(false);
                button4.setVisible(false);
            } else {
                planbottom.setVisible(true);
                planetop.setVisible(true);
                if(b3){
                    button3.setVisible(true);
                }
                if(b4){
                    button4.setVisible(true);
                }
            }
        }
        upperbutton=false;
    }

    /**
     * defines the first image and initialises the image list
     * @param imgID image id given from miniaturframe
     */
    public void changeImage(int imgID){
        LOGGER.info("Entering changeImage with imgID ="+imgID);

        activ = -1;
        FileInputStream fips=null;
        try {

            if (shootingService.searchIsActive().getActive()) {
                activ = shootingService.searchIsActive().getId();
                this.imageList = imageService.getAllImages(activ);
            }
            if (imageList != null) {
                LOGGER.debug("imagelist->size" + imageList.size());
                for (int i = 0; i <imageList.size() ; i++) {
                    if(imageList.get(i).getImageID()==imgID){
                        currentIndex=i;
                    }
                }

                if(currentIndex==0){
                    button4.setVisible(false);
                }
                if(currentIndex==(imageList.size()-1)){
                    button3.setVisible(false);
                }

                org.openbooth.entities.Image img = imageService.read(imgID);
                fips=new FileInputStream(img.getImagepath());
                ivfullscreenImage.setImage(new Image(fips, base.getWidth(), base.getHeight(), true, true));
                ivfullscreenImage.setId(img.getImagepath());

                makePreviewFilter(img.getImagepath());
            } else {
                windowManager.showScene(WindowManager.SHOW_MINIATURESCENE);
            }
        } catch (ServiceException e) {
            informationDialog("Bitte wenden Sie sich an den Betreiber.");
            LOGGER.error("changeImage - ",e);
        } catch (FileNotFoundException e) {
            LOGGER.error("changeImage - ",e);
            informationDialog("Foto konnte nicht gefunden werden.");
        }catch (NullPointerException e){
            LOGGER.error("changeImage - no active shooting - ",e);
            informationDialog("Ein Fehler ist im Programm aufgetreten.");
        } finally{
            if(fips!=null){
                try {
                    fips.close();
                } catch (IOException e) {
                    LOGGER.error("changeImage - ", e);
                }
            }
        }

    }

    /**
     * prepares filter-imageViews by getting all filters from service and showing the filter in small preview imageviews
     * @param imgOriginalPath path of the image to show
     */
    public void makePreviewFilter(String imgOriginalPath){
        LOGGER.debug("Entering makePreviewFilter with imgPath->", imgOriginalPath);

        try {
            String imgPath = filterService.resize(imgOriginalPath,80,80);

            int counter= 1;
            List<String> allfilters = filterService.getExistingFilters();

            double imageFilterConstraint = (Screen.getPrimary().getBounds().getWidth() - (allfilters.size() * 100)) / 2;
            planbottom.getChildren().clear();
            if(!constraintInitialized) {
                ColumnConstraints con = new ColumnConstraints();
                con.setPrefWidth(imageFilterConstraint);
                planbottom.getColumnConstraints().add(con);
            }

            for(String filter: allfilters){
                ImageView imageView = new ImageView(SwingFXUtils.toFXImage(filterService.filter(filter,imgPath),null));
                if(counter == 1) {
                    imageView.setFitHeight(100);
                    original = imageView;
                    activeFilterImageView = original;

                }

                imageView.setId(filter);
                imageView.setOnMouseClicked(e -> {
                    Image filteredImage;
                    ImageView imgView =(ImageView) e.getSource();
                    try {
                        filteredImage = SwingFXUtils.toFXImage(filterService.filter(imgView.getId(),ivfullscreenImage.getId()),null);
                        System.gc();
                        if(changeActiveFilter(imgView)) {
                            ivfullscreenImage.setImage(filteredImage);
                            filteredImage = null;
                            ivfullscreenImage.setFitHeight(base.getHeight());
                            ivfullscreenImage.setFitWidth(base.getWidth());
                        }

                    } catch (ServiceException e2) {
                        LOGGER.error("makePreviewFilter - FilterOnClick-> ",e2);
                    }

                });
                planbottom.add(imageView, counter, 0);
                if(!constraintInitialized) {
                    ColumnConstraints con = new ColumnConstraints();
                    con.setPrefWidth(100);
                    planbottom.getColumnConstraints().add(con);
                }

                counter++;
            }
            if(!constraintInitialized) {
                ColumnConstraints con = new ColumnConstraints();
                con.setPrefWidth(imageFilterConstraint);
                planbottom.getColumnConstraints().add(con);


                mainPane.add(planbottom,0,2);

                constraintInitialized = true;
            }




        } catch (ServiceException e) {
           LOGGER.error("makePreviewFilter - exception in service ",e);
        }
    }

    /**
     * changes the size of the preview imagefilter (which is clicked) so user knows which filter is actually active.
     * if the active filter is clicked,then the filter effect will be removed.
     * @param imageView clicked imageView of filter
     * @return it returns false, if the active filter is clicked, else true.
     */
    public boolean changeActiveFilter(ImageView imageView){
        if(!imageView.equals(activeFilterImageView)) {
            saveFilteredButton.setVisible(true);
            if(activeFilterImageView!=null){
                activeFilterImageView.setFitHeight(80);
                activeFilterImageView.setPreserveRatio(false);
            }

            imageView.setFitHeight(100);
            imageView.setPreserveRatio(false);

            activeFilterImageView = imageView;

            return true;
        }else{
            activeFilterImageView.setFitHeight(80);
            activeFilterImageView.setPreserveRatio(false);

            original.setFitHeight(100);

            activeFilterImageView = original;
            saveFilteredButton.setVisible(false);
            FileInputStream fips=null;
            try {
                fips=new FileInputStream(ivfullscreenImage.getId());
                ivfullscreenImage.setImage(new Image(fips, base.getWidth(), base.getHeight(),true,true));
            } catch (FileNotFoundException e) {
                LOGGER.error("changeActiveFilter - ",e);
            } finally{
                if(fips!=null){
                    try {
                        fips.close();
                    } catch (IOException e) {
                        LOGGER.error("changeActiveFilter - ",e);
                    }
                }
            }
            return false;
        }


    }

    /**
     * saves the filtered image in database...
     */
    @FXML
    public void saveFilteredImg(){
        if(cropping)
        {
            onCheckPressed();
        }
        else
        {
            LOGGER.debug("Entering saveFilteredImg... "+filteredImgPath);
            try {



                activeFilterImageView.setFitHeight(80);
                activeFilterImageView.setPreserveRatio(false);


                saveFilteredButton.setVisible(false);

                //exporting image name from imagePath
                String[] parts = ivfullscreenImage.getId().split("/");
                String imgFilterName = parts[parts.length-1].replace(".jpg","_"+activeFilterImageView.getId()+".jpg");

                String destPath = shootingService.searchIsActive().getStorageDir()+imgFilterName;

                imageHandler.saveImage(filterService.filter(activeFilterImageView.getId(),ivfullscreenImage.getId()),destPath);

                activeFilterImageView = null;
                int shootingID = shootingService.searchIsActive().getId();
                org.openbooth.entities.Image newImage = imageService.create(new org.openbooth.entities.Image(destPath, shootingID));


                if((currentIndex+1)>=imageList.size()) {
                    imageList.add(newImage);
                    refreshManager.notifyMiniatureFrameOfAdd(newImage, -1);
                }else {
                    imageList.add(currentIndex + 1, newImage);
                    refreshManager.notifyMiniatureFrameOfAdd(newImage,currentIndex+1);
                }

                currentIndex = currentIndex + 1;
                button4.setVisible(true);
                LOGGER.debug("Filtered image saved in DB...");

            } catch (ImageHandlingException | ServiceException e) {
                LOGGER.error("saveFilteredImg - ",e);
            }
        }
    }

    /**
     * deleting created mini preview images in filesystem
     * @param imgPath imagepath to delete
     */
    public void deletePreviews(String imgPath){

        LOGGER.debug("Entering deletePreviews -> with imgPath ="+imgPath);
        //exporting image name from imagePath
        String[] parts = imgPath.split("/");
        String imgFilterName = parts[parts.length-1].replace(".jpg","_preview.jpg");

        File previewFile=new File(tempStorageDir+imgFilterName);
        if(previewFile.isFile())
            previewFile.delete();
    }

    public void onCropPressed()
    {
        cropping=true;
        saveFilteredButton.setVisible(true);
        LOGGER.debug("Crop Button clicked");
        if(cropRectangle==null)
        {
            cropRectangle=createDraggableRectangle((forCropping.getWidth()/2)-200, (forCropping.getHeight()/2)-300, 400, 600);
            forCropping.getChildren().add(cropRectangle);
        }
        else if(cropRectangle.isVisible())
        {
            cropRectangle.setVisible(false);
            resizeHandleNW.setVisible(false);
            resizeHandleSE.setVisible(false);
            saveFilteredButton.setVisible(false);
        }
        else
        {
            cropRectangle.setVisible(true);
            resizeHandleNW.setVisible(true);
            resizeHandleSE.setVisible(true);
        }

    }

    public void triggerShot(KeyEvent keyEvent){



        String keystoke = keyEvent.getText();

        int index = KeyHandler.getIndexForKeyEvent(keyEvent);
        String messageString = "";


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

    private void onCheckPressed()
    {
        cropping = false;
        cropRectangle.setVisible(false);
        resizeHandleNW.setVisible(false);
        resizeHandleSE.setVisible(false);
        saveFilteredButton.setVisible(false);

        int x = (int)cropRectangle.localToScene(cropRectangle.getBoundsInLocal()).getMinX() - (int)ivfullscreenImage.localToScene(ivfullscreenImage.getBoundsInLocal()).getMinX();
        int y = (int)cropRectangle.localToScene(cropRectangle.getBoundsInLocal()).getMinY() - (int)ivfullscreenImage.localToScene(ivfullscreenImage.getBoundsInLocal()).getMinY();
        int maxX =  (int)ivfullscreenImage.localToScene(ivfullscreenImage.getBoundsInLocal()).getMaxX() - (int)ivfullscreenImage.localToScene(ivfullscreenImage.getBoundsInLocal()).getMinX();
        int maxY =  (int)ivfullscreenImage.localToScene(ivfullscreenImage.getBoundsInLocal()).getMaxY() - (int)ivfullscreenImage.localToScene(ivfullscreenImage.getBoundsInLocal()).getMinY();
        try {
            org.openbooth.entities.Image newImage = imageService.crop(imageList.get(currentIndex),activeFilterImageView.getId(), x, x + (int)cropRectangle.getWidth(), y, y + (int)cropRectangle.getHeight(), maxX, maxY);


            if((currentIndex+1)>=imageList.size()) {
                imageList.add(newImage);
                refreshManager.notifyMiniatureFrameOfAdd(newImage,-1);
            }else {
                imageList.add(currentIndex + 1, newImage);
                refreshManager.notifyMiniatureFrameOfAdd(newImage,currentIndex+1);
            }

            currentIndex = currentIndex + 1;
            button4.setVisible(true);

            /* changes image of activeImageView */
            ivfullscreenImage.setImage(new Image("file:"+newImage.getImagepath(), base.getWidth(), base.getHeight(),true,true));


        } catch (ServiceException e) {
            LOGGER.error("onCheckPressed" , e);
        }


    }

    private Rectangle createDraggableRectangle(double x, double y, double width, double height) {
        final double handleRadius = 10 ;

        Rectangle rect = new Rectangle(x, y, width, height);
        rect.setOpacity(0.1);
        rect.setVisible(true);

        resizeHandleNW = new Circle(handleRadius, Color.BLACK);
        resizeHandleNW.centerXProperty().bind(rect.xProperty());
        resizeHandleNW.centerYProperty().bind(rect.yProperty());
        resizeHandleNW.setVisible(true);

        resizeHandleSE = new Circle(handleRadius, Color.BLACK);
        resizeHandleSE.centerXProperty().bind(rect.xProperty().add(rect.widthProperty()));
        resizeHandleSE.centerYProperty().bind(rect.yProperty().add(rect.heightProperty()));
        resizeHandleSE.setVisible(true);

        rect.parentProperty().addListener((obs, oldParent, newParent) -> {
            for (Circle c : Arrays.asList(resizeHandleNW, resizeHandleSE)) {
                Pane currentParent = (Pane)c.getParent();
                if (currentParent != null) {
                    currentParent.getChildren().remove(c);
                }
                ((Pane)newParent).getChildren().add(c);
            }
        });

        Wrapper<Point2D> mouseLocation = new Wrapper<>();

        setUpDragging(resizeHandleNW, mouseLocation) ;
        setUpDragging(resizeHandleSE, mouseLocation) ;

        resizeHandleNW.setOnDragDetected(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newX = rect.getX() + deltaX ;
                if (newX >= handleRadius
                        && newX <= rect.getX() + rect.getWidth() - handleRadius) {
                    rect.setX(newX);
                    rect.setWidth(rect.getWidth() - deltaX);
                }
                double newY = rect.getY() + deltaY ;
                if (newY >= handleRadius
                        && newY <= rect.getY() + rect.getHeight() - handleRadius) {
                    rect.setY(newY);
                    rect.setHeight(rect.getHeight() - deltaY);
                }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
        });

        resizeHandleSE.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newMaxX = rect.getX() + rect.getWidth() + deltaX ;
                if (newMaxX >= rect.getX()
                        && newMaxX <= rect.getParent().getBoundsInLocal().getWidth() - handleRadius) {
                    rect.setWidth(rect.getWidth() + deltaX);
                }
                double newMaxY = rect.getY() + rect.getHeight() + deltaY ;
                if (newMaxY >= rect.getY()
                        && newMaxY <= rect.getParent().getBoundsInLocal().getHeight() - handleRadius) {
                    rect.setHeight(rect.getHeight() + deltaY);
                }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
        });

        return rect ;
    }

    private void setUpDragging(Circle circle, Wrapper<Point2D> mouseLocation) {

        circle.setOnDragDetected(event -> {
            circle.getParent().setCursor(Cursor.CLOSED_HAND);
            mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
        });

        circle.setOnMouseReleased(event -> {
            circle.getParent().setCursor(Cursor.DEFAULT);
            mouseLocation.value = null ;
        });
    }
    static class Wrapper<T> { T value ; }
}