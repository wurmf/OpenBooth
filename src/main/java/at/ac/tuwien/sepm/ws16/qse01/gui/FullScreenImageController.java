package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.printer.ImagePrinter;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.FilterService;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.animation.FadeTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private Pane planetop;

    private GridPane planbottom;

    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private ImageView ivfullscreenImage;
    @FXML
    private Button saveFilteredButton;



    private boolean b3=true;
    private boolean b4=true;
    private boolean upperbutton=false;

    private at.ac.tuwien.sepm.ws16.qse01.entities.Image firstImage;

    private List<at.ac.tuwien.sepm.ws16.qse01.entities.Image> imageList;
    private int currentIndex=-1;
    private int activ;

    private Shooting activeShooting;
    private ImageView activeFilterImageView;
    private ImageView original;
    private String filteredImgPath= null;
    private boolean constraintInitialized = false;

    private FilterService filterService;
    private ImageService imageService;
    private ShootingService shootingService;
    private WindowManager windowManager;
    private ImagePrinter imagePrinter;
    private RefreshManager refreshManager;

    @Autowired
    public FullScreenImageController(WindowManager windowManager, ShootingService shootingService,FilterService filterService, ImageService imageService, ImagePrinter imagePrinter, RefreshManager refreshManager) throws ServiceException {
        this.filterService = filterService;
        this.imageService=imageService;
        this.shootingService= shootingService;
        this.windowManager=windowManager;
        this.imagePrinter=imagePrinter;
        this.refreshManager=refreshManager;

        this.activeShooting = shootingService.searchIsActive();
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
                at.ac.tuwien.sepm.ws16.qse01.entities.Image img= new at.ac.tuwien.sepm.ws16.qse01.entities.Image();
                if(!imageList.isEmpty()&&imageList.size()>currentIndex&&currentIndex>=0) {
                    imagePrinter.print(img);
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
     *
     * @param actionEvent press action event
     */
    @FXML
    public void onDeletePressed(ActionEvent actionEvent) {
        try {
            if(ivfullscreenImage!=null&&imageList!=null){
            Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                    "Möchten Sie das Bild tatsächlich löschen");
            alert.setHeaderText("Bild Löschen");
            alert.initOwner(windowManager.getStage());
            Optional<ButtonType> result =alert.showAndWait();
                if(result.isPresent()&&result.get()==ButtonType.OK){
                    at.ac.tuwien.sepm.ws16.qse01.entities.Image image=imageList.get(currentIndex);
                    imageService.delete(image.getImageID());

                    if(currentIndex<imageList.size()-1){
                        onNextImage();
                        currentIndex--;
                    }else{
                        if(currentIndex>0){
                            onLastImagePressed();
                        }else{
                            onClosePressed();
                        }
                    }
                    if(activ!=-1) {
                        imageList = imageService.getAllImages(activ);
                        refreshManager.notifyMiniatureFrameOfDelete(image);
                    }else{
                        LOGGER.debug("no active shooting");
                        informationDialog("Bild konnte nicht gelöscht werden.");
                    }
                }
            }
        } catch (ServiceException e) {
            LOGGER.debug("delete - ",e);
            informationDialog("Bild konnte nicht gelöscht werden.");
        }
    }

    /**
     * closes full screen and opens miniatur sceen again
     * before doing so it sets currentIndex to -1 to overcome possible null pointer exeptions
     *
     */
    @FXML
    public void onClosePressed(){
        windowManager.showScene(WindowManager.SHOW_MINIATURESCENE);
    }

    @FXML
    public void onFilter4Pressed(ActionEvent actionEvent) {

    }

    @FXML
    public void onFilter3Pressed(ActionEvent actionEvent) {

    }

    @FXML
    public void onFilter5Pressed(ActionEvent actionEvent) {

    }

    @FXML
    public void onFilter2Pressed(ActionEvent actionEvent) {

    }

    @FXML
    public void onFilter1Pressed(ActionEvent actionEvent) {

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
        LOGGER.debug("reach"+currentIndex);
        upperbutton = true;
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
                ivfullscreenImage.setImage(new Image(new FileInputStream(imageList.get(currentIndex).getImagepath()), base.getWidth(), base.getHeight(), true, true));
                ivfullscreenImage.setId(imageList.get(currentIndex).getImagepath());
                makePreviewFilter(imageList.get(currentIndex).getImagepath());
                saveFilteredButton.setVisible(false);
            } else {
                windowManager.showScene(WindowManager.SHOW_MINIATURESCENE);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        LOGGER.debug("reach"+currentIndex);
        upperbutton = true;
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

                ivfullscreenImage.setImage(new Image(new FileInputStream(imageList.get(currentIndex).getImagepath()),  base.getWidth(), base.getHeight(), true, true));
                ivfullscreenImage.setId(imageList.get(currentIndex).getImagepath());
                makePreviewFilter(imageList.get(currentIndex).getImagepath());
                saveFilteredButton.setVisible(false);

            } else {
                windowManager.showScene(WindowManager.SHOW_MINIATURESCENE);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * to get an full screen image without buttons
     * and reload the buttons when pressed again
     * this is achieved by seting visiblety
     * @param actionEvent press action event
     */
    public void onfullScreenPressed (ActionEvent actionEvent) {

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
       activ = -1;
        try {

            if (shootingService.searchIsActive().getActive()) {
                activ = shootingService.searchIsActive().getId();
                imageList = imageService.getAllImages(activ);
            }
            if (imageList != null) {
                LOGGER.debug("hear" + imageList.size());
                //currentIndex = imageList.indexOf(firstImage);
                for (int i = 0; i <imageList.size() ; i++) {
                    if(imageList.get(i).getImageID()==imgID){
                        currentIndex=i;
                    }
                    LOGGER.debug("hear" + imageList.get(i).getImageID());
                }

                if(currentIndex==0){
                    button4.setVisible(false);
                }if(currentIndex==imageList.size()-1){
                    button3.setVisible(false);
                }

                at.ac.tuwien.sepm.ws16.qse01.entities.Image img = imageService.read(imgID);

                ivfullscreenImage.setImage(new Image(new FileInputStream(img.getImagepath()), base.getWidth(), base.getHeight(), true, true));
                ivfullscreenImage.setId(img.getImagepath());

                makePreviewFilter(img.getImagepath());
            } else {
                windowManager.showScene(WindowManager.SHOW_MINIATURESCENE);
            }
        } catch (ServiceException e) {
            informationDialog("Bitte wenden Sie sich an den Betreiber");
            LOGGER.debug(e.getMessage());
        } catch (FileNotFoundException e) {
            LOGGER.debug(e.getMessage());
            informationDialog("Foto konnte nicht gefunden werden");
           // windowManager.showMiniatureFrame();
        }catch (NullPointerException e){
            LOGGER.error("no active shooting");
        }

    }

    /**
     * prepares filter-imageViews by getting all filters from service and showing the filter in small preview imageviews
     * @param imgOriginalPath path of the image to show
     */
    public void makePreviewFilter(String imgOriginalPath){
        LOGGER.info("Entering makePreviewFilter with imgPath->"+imgOriginalPath);

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

                        if(changeActiveFilter(imgView)) {
                            ivfullscreenImage.setImage(filteredImage);
                            filteredImage = null;
                            filterService.clear();
                            ivfullscreenImage.setFitHeight(base.getHeight());
                            ivfullscreenImage.setFitWidth(base.getWidth());
                        }

                    } catch (ServiceException e2) {
                        LOGGER.error("FilterOnClick-> "+e2.getMessage());
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

                constraintInitialized = true;
            }

            mainPane.add(planbottom,0,2);


        } catch (Exception e) {
           LOGGER.error("Error: ",e);
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
            try {
                ivfullscreenImage.setImage(new Image(new FileInputStream(ivfullscreenImage.getId()), base.getWidth(), base.getHeight(),true,true));
            } catch (FileNotFoundException e) {
                LOGGER.error("changeActiveFilter ->"+e.getMessage());
            }
            return false;
        }


    }

    /**
     * saves the filtered image in database...
     */
    @FXML
    public void saveFilteredImg(){
        LOGGER.info("Entering saveFilteredImg... "+filteredImgPath);
        try {


            activeFilterImageView.setFitHeight(80);
            activeFilterImageView.setPreserveRatio(false);


            saveFilteredButton.setVisible(false);

            File outputfile = null;
            System.out.println("storageDir ->"+activeShooting.getStorageDir());
            try {
                // retrieve image
                BufferedImage bi = filterService.filter(activeFilterImageView.getId(),ivfullscreenImage.getId()); //SwingFXUtils.fromFXImage(ivfullscreenImage.getImage(),null);

                //exporting image name from imagePath
                String[] parts = ivfullscreenImage.getId().split("/");
                String imgFilterName = parts[parts.length-1].replace(".jpg","_"+activeFilterImageView.getId()+".jpg");
                outputfile = new File(activeShooting.getStorageDir()+"/"+imgFilterName);

                ImageIO.write(bi, "jpg", outputfile);
            } catch (IOException e) {
                LOGGER.error("Saving FilteredImage ->",e);
            }



            activeFilterImageView = null;
            at.ac.tuwien.sepm.ws16.qse01.entities.Image newImage = imageService.create(new at.ac.tuwien.sepm.ws16.qse01.entities.Image(outputfile.getAbsolutePath(),activeShooting.getId()));
            refreshManager.notifyMiniatureFrameOfAdd(newImage);
            if((currentIndex+1)>=imageList.size())
                imageList.add(newImage);
            else {
                imageList.add(currentIndex + 1, newImage);
            }
            currentIndex = currentIndex + 1;
            button4.setVisible(true);
            LOGGER.info("Filtered image saved in DB...");

        } catch (ServiceException e) {
            LOGGER.error("saveFilteredImg->"+e.getMessage());
        }

    }

    /**
     * deleting created mini preview images in filesystem
     * @param imgPath imagepath to delete
     */
    public void deletePreviews(String imgPath){
        LOGGER.info("Entering deletePreviews -> with imgPath ="+imgPath);
        //exporting image name from imagePath
        String[] parts = imgPath.split("/");
        String imgFilterName = parts[parts.length-1].replace(".jpg","_preview.jpg");
        if(new File(activeShooting.getStorageDir()+imgFilterName).isFile())
            new File(activeShooting.getStorageDir()+imgFilterName).delete();
    }

}