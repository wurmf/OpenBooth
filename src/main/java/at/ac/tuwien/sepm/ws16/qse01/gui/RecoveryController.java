package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.ImageProcessingManager;
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

    private ShootingService shootingService;
    private WindowManager windowmanager;
    private ImageProcessingManager imageProcessingManager;
    private ProfileService profileService;

    @Autowired
    public RecoveryController(ShootingService shootingService, WindowManager windowmanager, ImageProcessingManager imageProcessingManager, ProfileService profileService) {
        this.shootingService = shootingService;
        this.windowmanager = windowmanager;
        this.imageProcessingManager = imageProcessingManager;
        this.profileService = profileService;
    }


    /**
     * Ends an active shooting if available
     */
    public void onEndShootingPressed() {
        try {
            shootingService.endShooting();
            windowmanager.initMiniController();
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
        try {
            windowmanager.notifyActiveShootingAvailable();
            profileService.setActiveProfile(shootingService.searchIsActive().getProfileid());
            boolean camerasFitPosition = imageProcessingManager.checkImageProcessing(profileService.getActiveProfile());
            if (camerasFitPosition) {
                imageProcessingManager.initImageProcessing();
                windowmanager.initMiniController();
                windowmanager.showScene(WindowManager.SHOW_CUSTOMERSCENE);
            } else{
                showInformationDialog("Das Kamerasetup des Studios passt nicht zum gewählten Profil. Das Shooting kann nicht fortgesetzt werden.");
                onEndShootingPressed();
            }
        } catch(ServiceException e){
            LOGGER.error("onRecoveryPressed - error in service ", e);
            showInformationDialog("Das Shooting konnte nicht wiederhergestellt werden.");
            onEndShootingPressed();
        }
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
