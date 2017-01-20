package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.CameraService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ShootingServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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
    public MainFrameController(ShootingServiceImpl shootingService, WindowManager windowManager, CameraService cameraService) {
        this.shootingService = shootingService;
        this.windowManager = windowManager;
        this.cameraService = cameraService;
    }


    /**
     * initialize Mainframe by
     * tests whether there is still a shooting active or not
     * in case there is still an open shooting the RecoveryDialog will reload it
     *
     * catches ServiceException caused by all Service methodes
     */
    @FXML
    private void initialize(){
        Shooting activeShooting = null;
    }

    /**
     * linkes to admin log in and followup options
     *
     * @param actionEvent press action event
     */
    public void onStartShootingPressed(ActionEvent actionEvent) {
        windowManager.showScene(WindowManager.SHOW_SHOOTINGSCENE);
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
     *Opens Profile administration
     *
     * @param actionEvent press action event
     */

    public void onEditPressed(ActionEvent actionEvent) {
        windowManager.showScene(WindowManager.SHOW_SETTINGSCENE);
    }

    /**
     * information dialog to show user error messages
     *
     * @param info String that gives the usere an error message
     */
    public void showInformationDialog(String info){
        Alert information = new Alert(Alert.AlertType.INFORMATION, info);
        information.setHeaderText("Ein Fehler ist Aufgetreten");
        information.showAndWait();
    }
}
