package at.ac.tuwien.sepm.ws16.qse01.gui;

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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.print.PrinterException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * full screen image view
 */
@Component
public class FullScreenImageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFrameController.class);
    @FXML
    private Pane planetop;
    @FXML
    private Pane planbottom;
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
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private ImageView image4;
    @FXML
    private ImageView image3;
    @FXML
    private ImageView ivfullscreenImage;

    private ImageView[]slide =new ImageView[3];
    private List<at.ac.tuwien.sepm.ws16.qse01.entities.Image> imageList;
    private int currentIndex=-1;
    private int activ;

    private ImageService imageService;
    private ShootingService shootingService;
    private WindowManager windowManager;
    private ImagePrinter imagePrinter;

    @Autowired
    public FullScreenImageController(WindowManager windowManager, ShootingService shootingService, ImageService imageService, ImagePrinter imagePrinter) throws ServiceException {
        this.imageService=imageService;
        this.shootingService= shootingService;
        this.windowManager=windowManager;
        this.imagePrinter=imagePrinter;
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
        try {
            if (imageList == null) {
                if(shootingService.searchIsActive().getActive()){
                    activ=shootingService.searchIsActive().getId();
                    imageList = imageService.getAllImages(activ);
                }
                //currentIndex=0;
            }
            if(currentIndex>=0&&imageList!=null&&!imageList.isEmpty()) {

                Image imlast=null;
                Image imnext=null;

                Image imonscreen = new Image(new FileInputStream(imageList.get(currentIndex).getImagepath()));
                 slide[1] = new ImageView(imonscreen);
                if (currentIndex != 0) {
                    imlast = new Image(new FileInputStream(imageList.get(currentIndex - 1).getImagepath()));
                    slide[0] = new ImageView(imlast);
                }else{
                    slide[0]= null;
                }
                if (currentIndex != imageList.size() - 1) {
                    imnext = new Image(new FileInputStream(imageList.get(currentIndex + 1).getImagepath()));
                    slide[2] = new ImageView(imnext);
                }else{
                    slide[2]=null;
                }

                ivfullscreenImage.setImage(slide[1].getImage());
                //ivfullscreenImage = slide[1];
            }
        } catch (ServiceException e) {
            LOGGER.debug("initialize - "+e);
           informationDialog("Bilder konnten nicht geladen werden");
        } catch (FileNotFoundException e) {
            LOGGER.debug("initialize - "+e);
            informationDialog("Der Speicherpfad konnte nicht gefunden werden");
        }
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
    public void onGetLastImage(ActionEvent actionEvent) {
        try {
            if(slide[0]!=null) {
                FadeTransition fadeIn = getFadeTransition(slide[0], 0.0, 1.0, 2000);
                fadeIn.play();
                //  FadeTransition fadeOut = getFadeTransition(slide[1], 1.0, 0.0, 2000);
                ivfullscreenImage.setImage(slide[0].getImage());
                currentIndex--;
                LOGGER.debug("Last1",currentIndex+"");

                if(currentIndex>0){
                    Image image = new Image(new FileInputStream(imageList.get(currentIndex).getImagepath()), 150, 0, true, true);
                    slide[2] = slide[1];
                    slide[1] = slide[0];
                    slide[0] = new ImageView(image);

                }else {
                    currentIndex=0;
                    slide[2] = slide[1];
                    slide[1] = slide[0];
                    slide[0] = null;

                }
            }
            //ivfullscreenImage.setImage(image);
        } catch (FileNotFoundException e) {
            LOGGER.debug("onGetLastImage - "+e);
            informationDialog("Das Speicherfile konnte nicht gefunden werden");
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
    public void onGetNextImage(ActionEvent actionEvent) {
        try {
            if(slide[2]!=null) {
                FadeTransition fadeOut = getFadeTransition(slide[2], 1.0, 0.0, 2000);
                fadeOut.play();
                ivfullscreenImage.setImage(slide[2].getImage());
                currentIndex++;

                LOGGER.debug("Next1",currentIndex+"");

                if(currentIndex<imageList.size()){
                    Image image = null;
                    image = new Image(new FileInputStream(imageList.get(currentIndex).getImagepath()), 150, 0, true, true);

                    slide[0] = slide[1];
                    slide[1] = slide[2];
                    slide[2] = new ImageView(image);

                }else {
                    currentIndex=imageList.size()-1;
                    slide[0] = slide[1];
                    slide[1] = slide[2];
                    slide[2]=null;
                }
            }
        } catch (FileNotFoundException e) {
        LOGGER.debug(e.getMessage());
        }
    }

    /**
     * to get an full screen image without buttons
     * and reload the buttons when pressed again
     * this is achieved by seting visiblety
     * @param actionEvent press action event
     */
    public void onfullscreen1(ActionEvent actionEvent) {

        if(planbottom.isVisible()){
            planbottom.setVisible(false);
            planetop.setVisible(false);
            button1.setVisible(false);
            button2.setVisible(false);
            image3.setVisible(false);
            image4.setVisible(false);
            button5.setVisible(false);
            button6.setVisible(false);
            button7.setVisible(false);
            button8.setVisible(false);
            button9.setVisible(false);
        } else {

            planbottom.setVisible(true);
            planetop.setVisible(true);
            button1.setVisible(true);
            button2.setVisible(true);
            image4.setVisible(true);
            image3.setVisible(true);
            button5.setVisible(true);
            button6.setVisible(true);
            button7.setVisible(true);
            button8.setVisible(true);
            button9.setVisible(true);
        }


    }

    /**
     * to get an full screen image without buttons
     * and reload the buttons when pressed again
     * this is achieved by seting visiblety
     * @param actionEvent press action event
     */
    public void onfullscreen2(ActionEvent actionEvent) {
        if(planbottom.isVisible()){
            planbottom.setVisible(false);
            planetop.setVisible(false);
            button1.setVisible(false);
            button2.setVisible(false);
            image3.setVisible(false);
            image4.setVisible(false);
            button5.setVisible(false);
            button6.setVisible(false);
            button7.setVisible(false);
            button8.setVisible(false);
            button9.setVisible(false);
        } else {

            planbottom.setVisible(true);
            planetop.setVisible(true);
            button1.setVisible(true);
            button2.setVisible(true);
            image4.setVisible(true);
            image3.setVisible(true);
            button5.setVisible(true);
            button6.setVisible(true);
            button7.setVisible(true);
            button8.setVisible(true);
            button9.setVisible(true);
        }


    }
}