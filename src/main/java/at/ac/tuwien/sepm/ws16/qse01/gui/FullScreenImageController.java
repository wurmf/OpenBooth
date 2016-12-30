package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.util.printer.ImagePrinter;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private Pane planetop;
    @FXML
    private Pane planbottom;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private ImageView ivfullscreenImage;


    private boolean b3=true;
    private boolean b4=true;
    private boolean upperbutton=false;

    private at.ac.tuwien.sepm.ws16.qse01.entities.Image firstImage;

    private ImageView[]slide =new ImageView[3];
    private List<at.ac.tuwien.sepm.ws16.qse01.entities.Image> imageList;
    private int currentIndex=-1;
    private int activ;

    private ImageService imageService;
    private ShootingService shootingService;
    private WindowManager windowManager;
    private ImagePrinter imagePrinter;
    private SpringFXMLLoader springFXMLLoader;

    @Autowired
    public FullScreenImageController(SpringFXMLLoader springFXMLLoader,WindowManager windowManager, ShootingService shootingService, ImageService imageService, ImagePrinter imagePrinter) throws ServiceException {
        this.imageService=imageService;
        this.shootingService= shootingService;
        this.windowManager=windowManager;
        this.imagePrinter=imagePrinter;
        this.springFXMLLoader = springFXMLLoader;
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
                LOGGER.error("onPrintPressed - "+ e);
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
                //delete
                if(currentIndex+1<imageList.size()) {
                    slide[1] = slide[2];
                    imageService.delete(imageList.get(currentIndex).getImageID());
                    if (currentIndex+2<imageList.size()) {
                        at.ac.tuwien.sepm.ws16.qse01.entities.Image nextim = imageList.get(currentIndex + 2);
                        imageList = imageService.getAllImages(activ);
                        Image image = new Image(new FileInputStream(nextim.getImagepath()));
                        slide[2] = new ImageView(image);
                        if(imageList.indexOf(nextim)>0){
                            currentIndex = imageList.indexOf(nextim) - 1;
                        }else{
                            currentIndex=0;
                        }

                    }else {
                        slide[2]=null;
                        imageList = imageService.getAllImages(activ);
                        if(imageList.size()>0){
                            currentIndex=imageList.size()-1;
                        }else {
                            currentIndex=0;
                        }
                    }
                    ivfullscreenImage.setImage(slide[1].getImage());

                }else if(currentIndex-1>-1) {
                    slide[1] = slide[0];
                    imageService.delete(imageList.get(currentIndex).getImageID());
                    if (currentIndex-2>-1) {
                        at.ac.tuwien.sepm.ws16.qse01.entities.Image nextim = imageList.get(currentIndex - 2);
                        imageList = imageService.getAllImages(activ);
                        Image image = new Image(new FileInputStream(nextim.getImagepath()));
                        slide[0] = new ImageView(image);
                        if(imageList.indexOf(nextim)<imageList.size()-1);
                        currentIndex = imageList.indexOf(nextim)+1;
                    }else {
                        slide[0]=null;
                        imageList = imageService.getAllImages(activ);
                        currentIndex=0;
                    }
                    ivfullscreenImage.setImage(slide[1].getImage());
            }else {
                    slide = null;
                    ivfullscreenImage = null;
                    imageService.delete(imageList.get(currentIndex).getImageID());
                    windowManager.showMiniatureFrame();
                }
            }
            }
        } catch (ServiceException e) {
            LOGGER.debug("delete - "+e);
            informationDialog("Bild konnte nicht gelöscht werden.");
        } catch (FileNotFoundException e) {
            LOGGER.debug("delete - "+e);
            informationDialog("Der Speicher Ort konnte nicht gefunden werden!");
        }
    }

    /**
     * closes full screen and opens miniatur sceen again
     * before doing so it sets currentIndex to -1 to overcome possible null pointer exeptions
     *
     * @param actionEvent press action event
     */
    @FXML
    public void onClosePressed(ActionEvent actionEvent) {
        currentIndex=-1;
        windowManager.showMiniatureFrame();
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
     * @param actionEvent swipe action event
     */
    public void onLastImagePressed(ActionEvent actionEvent) {
        LOGGER.debug("reach"+currentIndex);
        upperbutton = true;
        try {
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
            } else {
                windowManager.showMiniatureFrame();
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
     * @param actionEvent swipe action event
     */
    public void onNextImage(ActionEvent actionEvent) {
        LOGGER.debug("reach"+currentIndex);
        upperbutton = true;
        try {
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
            } else {
                windowManager.showMiniatureFrame();
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

                ivfullscreenImage.setImage(new Image(new FileInputStream(imageService.read(imgID).getImagepath()), base.getWidth(), base.getHeight(), true, true));

            } else {
                windowManager.showMiniatureFrame();
            }
        } catch (ServiceException e) {
            informationDialog("Bitte wenden Sie sich an den Betreiber");
            LOGGER.debug(e.getMessage());
        } catch (FileNotFoundException e) {
            LOGGER.debug(e.getMessage());
            informationDialog("Foto konnte nicht gefunden werden");
        }

    }

}