package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.application.MainApplication;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ShootingServiceImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


//TODO: Remove everything Swing related! We have to use JavaFX-Classes

/**
 * The controller for the mainFrame.
 *
 * @author Dominik Moser
 */
@Component
public class MainFrameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrameController.class);

    private SpringFXMLLoader springFXMLLoader;
    private Stage primaryStage;
    private MainApplication mainApp;

    ShootingService shootingService;

    @Autowired
    public MainFrameController(SpringFXMLLoader springFXMLLoader) throws Exception {
        this.springFXMLLoader = springFXMLLoader;
        shootingService = new ShootingServiceImpl();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void setStageAndMain(Stage primaryStage, MainApplication mainApp){
        this.primaryStage = primaryStage;
        this.mainApp = mainApp;
    }


    /**
     * inizialize Mainfraim
     * tests whether there is still an session active
     */
    @FXML
    private void initialize(){
        Shooting activeShooting = null;
        try {
            activeShooting = shootingService.searchIsActive();

            if(activeShooting.getActive()==true){
                showRecoveryDialog();
            }
        } catch (ServiceException e) {
            showInformationDialog("Ein fehler beim Starten des Programms ist aufgetreten.");
            LOGGER.info("MainFrameController:",e.getMessage());
         }
    }

    /**
     * linkes to admin log in and followup options
     *
     * @param actionEvent
     */
    public void onStartShootingPressed(ActionEvent actionEvent) {
        //mainApp.showShootingAdministration();
        mainApp.showAdminLogin();
    }

    /**
     * in case of brakdown
     *
     */
    public void showRecoveryDialog(){

        Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                "Möchten sie die zuletzt geöffnete Session wieder her stellen?");
        alert.setHeaderText("Die Anwendung wurde unerwartet geschlossen");
        alert.initOwner(primaryStage);
        Optional<ButtonType> result =alert.showAndWait();
        if(result.isPresent()&&result.get()==ButtonType.OK){

            //verlinken auf Kunden interface
            //yes
        } else {
            try {
                shootingService.endShooting();
                showInformationDialog("Shooting wurde beendet");
            } catch (ServiceException e) {
                showInformationDialog("Shooting konnte nicht beendet werden!");
            }
        }
    }

    /**
     * stop programm
     *
     * @param actionEvent
     */
    public void onEndPressed(ActionEvent actionEvent) {Platform.exit();
    }

    /**
     * information dialog
     * @param info
     */
    public void showInformationDialog(String info){
        Alert information = new Alert(Alert.AlertType.INFORMATION, info);
        information.initOwner(primaryStage);
        information.setHeaderText("Ein Fehler ist Aufgetreten");

    }
}
