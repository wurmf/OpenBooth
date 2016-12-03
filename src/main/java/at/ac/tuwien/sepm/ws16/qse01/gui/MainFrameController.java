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

import javax.swing.*;
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

    ShootingService service;
    @Autowired
    public MainFrameController(SpringFXMLLoader springFXMLLoader) throws Exception {
        this.springFXMLLoader = springFXMLLoader;
        service= new ShootingServiceImpl();
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
        Shooting shouting_isactive = null;
        try {
            shouting_isactive = service.search_isactive();

            if(shouting_isactive.getIsactiv()==true){
                in_case_of_restart();
            }
        } catch (ServiceException e) {
            informationDialog("Ein fehler beim Starten des Programms ist aufgetreten.");
            LOGGER.info("MainFrameController:",e.getMessage());
         }
    }

    /**
     * linkes to admin log in and followup options
     *
     * @param actionEvent
     */
    public void on_StartSessionPressed(ActionEvent actionEvent) {
        //mainApp.showShootingAdministration();
        mainApp.showAdminLogin();
    }

    /**
     * in case of brakdown
     *
     */
    public void in_case_of_restart(){

        Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                "Möchten sie die zuletzt geöffnete Session wieder her stellen?");
        alert.setHeaderText("Die Anwendung wurde unerwartet geschlossen");
        Optional<ButtonType> result =alert.showAndWait();
        if(result.isPresent()&&result.get()==ButtonType.OK){

            //verlinken auf Kunden interface
            //yes
        } else {
            service.end_session();
            informationDialog("Session wurde beendet");
        }
    }

    /**
     * stop programm
     *
     * @param actionEvent
     */
    public void on_EndPressed(ActionEvent actionEvent) {Platform.exit();
    }

    /**
     * information dialog
     * @param info
     */
    public void informationDialog(String info){
        Alert information = new Alert(Alert.AlertType.INFORMATION, info);
        information.setHeaderText("Ein Fehler ist Aufgetreten");

    }
}
