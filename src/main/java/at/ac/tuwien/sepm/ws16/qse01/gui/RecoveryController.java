package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *Recovery dialog when brake down
 */
@Component
public class RecoveryController {


    private static final Logger LOGGER = LoggerFactory.getLogger(RecoveryController.class);

    ShootingService shootingService;
    WindowManager windowmanager;

    @Autowired
    public RecoveryController(ShootingService shootingService, WindowManager windowmanager) {
        this.shootingService = shootingService;
        this.windowmanager = windowmanager;
    }


    /**
     * Ends an active shooting if available
     */
    public void onEndShootingPressed() {
        try {
            shootingService.endShooting();
            windowmanager.showScene(WindowManager.SHOW_MAINSCENE);
            LOGGER.info("showRecoveryDialog - shooting stopped");
        } catch (ServiceException e) {
            LOGGER.error("showRecoveryDialog - ",e);
            showInformationDialog("Shooting konnte nicht beendet werden!");
        }
    }

    /**
     * if the customer wants to continue the shooting, he gets linked to the
     * customer page of the active shooting
     */
    public void onRecoveryPressed() {
        windowmanager.notifyActiveShootingAvailable();
        windowmanager.showScene(WindowManager.SHOW_CUSTOMERSCENE);
    }


    /**
     * information dialog
     * @param info information to be displayed
     */
    public void showInformationDialog(String info){
        Alert information = new Alert(Alert.AlertType.INFORMATION, info);
        information.setHeaderText("Ein Fehler ist Aufgetreten");
        information.showAndWait();
    }
}
