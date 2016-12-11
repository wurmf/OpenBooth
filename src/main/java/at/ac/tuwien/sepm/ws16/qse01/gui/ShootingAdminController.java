package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.application.MainApplication;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Controlles the Shooting Admnisitration
 */
@Component
public class ShootingAdminController {

    @FXML
    private Label storageDirLabel;//storageDirLabel.setText();
    @FXML
    private ChoiceBox profileChoiceBox;

    String path =null;

    ShootingService shootingService;
    ProfileService profileService;
    WindowManager windowManager;

    @Autowired
    public ShootingAdminController(ProfileService profileService, ShootingService shootingService, WindowManager windowManager) throws Exception {
        this.shootingService = shootingService;
        this.profileService= profileService;
        this.windowManager = windowManager;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ShootingDAO.class);


    /**
     * inizialise cb_Profiles
     *
     */
    @FXML
    private void initialize() {

        try {
            List<Profile> prof = profileService.getAllProfiles();

       ObservableList<Profile> observableListProfile = FXCollections.observableList(prof);

        profileChoiceBox.setItems(observableListProfile);

       // profileChoiceBox.setCellFactory(new PropertyValueFactory<Profile,String>("name"));


        if(profileChoiceBox !=null){
            profileChoiceBox.setValue(observableListProfile.get(0));
        }
    } catch (ServiceException e) {
        showInformationDialog("Bitte erstellen Sie ein Profil");
    }
    }

    /**
     *
     * when pressed an new session starts(costumer interface opens)
     *
     * @param actionEvent
     *
*/
    public void onStartShootingPressed(ActionEvent actionEvent) {
        LOGGER.info(path);
        if (profileChoiceBox.getValue() != null) {
            if(path!=null) {

                try{

                    Profile profile = (Profile) profileChoiceBox.getSelectionModel().getSelectedItem();
                    Shooting shouting = new Shooting(0,profile.getId(), path, true);

                    storageDirLabel.setText("");
                    LOGGER.info("ShootingAdminController:", path);
                    path=null;

                    shootingService.addShooting(shouting);
                } catch (ServiceException serviceExeption) {
                    LOGGER.debug( serviceExeption.getMessage());
                    showInformationDialog("Es konnte keine Shooting erstellt werden.");
                }
            } else{
                showInformationDialog("Bitte wählen Sie einen speicher Pfad aus");
            }

        } else {
            showInformationDialog("Bitte erstellen Sie ein neues Profil");
        }
    }

            /**
             *
             * Opens Mainfframe again
             * @param actionEvent
             *
             */
    public void onDemolitionPressed(ActionEvent actionEvent) {
    storageDirLabel.setText("");
    windowManager.showMainFrame();
    }

    /**
     * find ordner of desire to save new project in
     * @param actionEvent
     *
     */
    public void onChooseStorageDirPressed(ActionEvent actionEvent) {

        try{
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Speicherort wählen");
            //directoryChooser.in
            //TODO: find way around
            File savefile =directoryChooser.showDialog(windowManager.getStage());

                storageDirLabel.setText(savefile.getPath());
                path = savefile.getPath();
        }catch (NullPointerException n){
            showInformationDialog("Kein Pfad gewählt");
        }
    }

    /**
     *Opens Profile edeting
     *
     * @param actionEvent
     *
     */
    public void onEditPressed(ActionEvent actionEvent) {
       windowManager.showProfileScene();
    }

    /**
     * information dialog
     * @param info
     */
    public void showInformationDialog(String info){
        Alert information = new Alert(Alert.AlertType.INFORMATION, info);
        information.setHeaderText("Ein Fehler ist Aufgetreten");
        //TODO: remove
        information.initOwner(windowManager.getStage());
        information.show();
    }

    /**
     * Shooting wird beendet
     * @param actionEvent
     */
    public void onStopShootingPressed(ActionEvent actionEvent) {
        try {
            shootingService.endShooting();
            Alert information = new Alert(Alert.AlertType.INFORMATION, "Shooting wurde beendet");
            information.setHeaderText("Bestätigung");
            //TODO: remove
            information.initOwner(windowManager.getStage());
            information.show();
        } catch (ServiceException e) {
            showInformationDialog("Shooting konnte nicht beendet werden!");
        }
    }
}

