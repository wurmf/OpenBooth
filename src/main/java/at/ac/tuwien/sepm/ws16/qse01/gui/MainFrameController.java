package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.CameraService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ShootingServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


//TODO: Remove everything Swing related! We have to use JavaFX-Classes

/**
 * The controller for the mainFrame.
 *
 */
@Component
public class MainFrameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrameController.class);

    private WindowManager windowManager;

    ShootingService shootingService;
    CameraService cameraService;

    @Autowired
    public MainFrameController(ShootingServiceImpl shootingService, WindowManager windowManager, CameraService cameraService) throws Exception {
        this.shootingService = shootingService;
        this.windowManager = windowManager;
        this.cameraService = cameraService;
    }


    /**
     * inizialize Mainfraim by
     * tests whether there is still an shooting active or not
     * in case there is still an open shooting the RecoveryDialog will reload it
     *
     * catches ServiceException caused by all Service methodes
     */
    @FXML
    private void initialize(){
        Shooting activeShooting = null;
        try {
            activeShooting = shootingService.searchIsActive();

            if(activeShooting.getActive()){
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
     * @param actionEvent press action event
     */
    public void onStartShootingPressed(ActionEvent actionEvent) {
        windowManager.showAdminLogin();
    }

    /**
     * in case of a breakdown the Recovery Dialog will tell the user about an still active
     * shooting and gives them the option to continue to ether close or reloade this shooting
     *in case of reload the user gets directly to the costumer interface
     *
     * caches ServiceException eventualy caused by endShooting
     *
     */
    public void showRecoveryDialog(){

        Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                "Möchten sie die zuletzt geöffnete Session wieder her stellen?");
        alert.setHeaderText("Die Anwendung wurde unerwartet geschlossen");
        Optional<ButtonType> result =alert.showAndWait();
        if(result.isPresent()&&result.get()==ButtonType.OK){
            windowManager.notifyActiveShootingAvailable();
        } else {
            try {
                shootingService.endShooting();
                showInformationDialog("Shooting wurde beendet");
            } catch (ServiceException e) {
                LOGGER.debug("recovery-"+e);
                showInformationDialog("Shooting konnte nicht beendet werden!");
            }
        }
    }

    /**
     * programm gets shot down
     *
     * @param actionEvent press action event
     */
    public void onEndPressed(ActionEvent actionEvent) {
        windowManager.closeStages();
    }

    /**
     * information dialog to show user error messages
     *
     * @param info String that gives the usere an error message
     */
    public void showInformationDialog(String info){
        Alert information = new Alert(Alert.AlertType.INFORMATION, info);
        information.setHeaderText("Ein Fehler ist Aufgetreten");

    }
}
