package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Controlles the Shooting Admnisitration
 */
@Component
public class ShootingAdminController {

    @FXML
    private Label saveing;
    @FXML
    private Label save1;
    @FXML
    private Label finallsavingplace;
    @FXML
    private GridPane gridbase;
    @FXML
    private GridPane gridSave;
    @FXML
    private Button canclebutton;
    @FXML
    private Button storage;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Label storageDirLabel;//storageDirLabel.setText();

    @FXML
    private ChoiceBox profileChoiceBox;

    private String path =null;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShootingDAO.class);


    private ShootingService shootingService;
    private ProfileService profileService;
    private WindowManager windowManager;

    @Autowired
    public ShootingAdminController(ProfileService profileService, ShootingService shootingService, WindowManager windowManager) throws Exception {
        this.shootingService = shootingService;
        this.profileService= profileService;
        this.windowManager = windowManager;
    }

    /**
     * inizialise shooting Admin Controller
     * depending on whether there is an activ shooting or not some funktions are unusable
     * to achieve this, they are set invisible/visible
     * furthermore this methodes initialises profileChoiceBox
     *
     * catches Service Exeption caused by Service methodes
     */
    @FXML
    private void initialize() {

        try {
            if(shootingService.searchIsActive().getActive()){
                startButton.setVisible(false);
                stopButton.setVisible(true);
                storage.setVisible(false);
                canclebutton.setText("Fortsetzen");
                storageDirLabel.setVisible(false);
                gridSave.setVisible(false);

                finallsavingplace.setText(shootingService.searchIsActive().getStorageDir());
                finallsavingplace.setVisible(true);
                save1.setVisible(true);
                saveing.setVisible(false);

            }else{
                stopButton.setVisible(false);
                startButton.setVisible(true);
                storage.setVisible(true);
                storageDirLabel.setVisible(true);
                gridSave.setVisible(true);
                canclebutton.setText("Abbrechen");
                finallsavingplace.setVisible(false);
                save1.setVisible(false);
                saveing.setVisible(true);
            }
            String resource = System.getProperty("user.home");

             /*if(mobiel) {
                        storage = Paths.get(resource + "fotostudio/Mobil");
            }else{*/
            Path storagepath = Paths.get(resource+"/fotostudio/Studio");
            // }
            storageDirLabel.setText(storagepath.toString());
            List<Profile> prof = profileService.getAllProfiles();
            if(prof!=null&&!prof.isEmpty()) {
                ObservableList<Profile> observableListProfile = FXCollections.observableList(prof);

                profileChoiceBox.setItems(observableListProfile);

                if (profileChoiceBox != null) {
                    profileChoiceBox.setValue(observableListProfile.get(0));
                }
            }

        } catch (ServiceException e) {
            LOGGER.debug("initialize - ",e);
            showInformationDialog("Bitte erstellen Sie ein Profil");
        }
    }

    public void inactivemode(){
        try {
            if (shootingService.searchIsActive().getActive()) {
                startButton.setVisible(false);
                stopButton.setVisible(true);
                storage.setVisible(false);
                canclebutton.setText("Fortsetzen");
                storageDirLabel.setVisible(false);
                gridSave.setVisible(false);
                finallsavingplace.setText(shootingService.searchIsActive().getStorageDir());
                finallsavingplace.setVisible(true);
                save1.setVisible(true);
                saveing.setVisible(false);

            } else {
                stopButton.setVisible(false);
                startButton.setVisible(true);
                storage.setVisible(true);
                storageDirLabel.setVisible(true);
                gridSave.setVisible(true);
                canclebutton.setText("Abbrechen");
                finallsavingplace.setVisible(false);
                save1.setVisible(false);
                saveing.setVisible(true);
            }
        }catch (ServiceException e ){
            e.printStackTrace();
        }

    }
    /**
     * when pressed an new shooting starts
     * to successfully start an new shooting an storage path gets selected and an profile must be selected
     *
     * @param actionEvent press action event
     *
     */
    @FXML
    public void onStartShootingPressed(ActionEvent actionEvent) {
        LOGGER.info(path);
        if (profileChoiceBox.getValue() != null) {
                try{
                    Profile profile = (Profile) profileChoiceBox.getSelectionModel().getSelectedItem();

                    if(path==null) {
                        try {
                            path = shootingService.createPath();
                        }catch (ServiceException s){
                            showInformationDialog(s.getMessage());
                        }
                    }
                        Shooting shouting = new Shooting(0, (int) profile.getId(), path, true);

                        //storageDirLabel.setText("");
                        LOGGER.info("ShootingAdminController:", path);
                        path = "";

                        shootingService.addShooting(shouting);

                        windowManager.showScene(WindowManager.SHOW_CUSTOMERSCENE);
                        windowManager.initShotFrameManager();
                } catch (ServiceException serviceExeption) {
                    LOGGER.debug( serviceExeption.getMessage());
                    showInformationDialog("Es konnte keine Shooting erstellt werden.");
                }
        } else {
            showInformationDialog("Bitte erstellen Sie ein neues Profil");
        }
    }

    /**
     * Opens Mainfframe again
     * @param actionEvent press action event
     */
    public void onDemolitionPressed(ActionEvent actionEvent) {
        try {
            if (shootingService.searchIsActive().getActive()) {
                Profile profile = (Profile) profileChoiceBox.getSelectionModel().getSelectedItem();
                if(shootingService.searchIsActive().getProfileid()!=profile.getId()) {
                    Shooting shooting = new Shooting(shootingService.searchIsActive().getId(), profile.getId(), "", true);
                    shootingService.updateProfile(shooting);
                }
                windowManager.showScene(WindowManager.SHOW_CUSTOMERSCENE);
            } else {
                windowManager.showScene(WindowManager.SHOW_MAINSCENE);
            }
        } catch (ServiceException e) {
            LOGGER.error("onDemolitionPressed - restart kein aktives shooting - ",e);
            showInformationDialog("Shooting konnte nicht mehr hergestellt werden");
        }
    }

    /*
     * finds the directory of desire to save new project in
     *
     * catches NullPointer Exception that could be caused by empty files
     *
     * @param actionEvent press action event
*/
    public void onChooseStorageDirPressed(ActionEvent actionEvent) {

        try{
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Speicherort wählen");
            //directoryChooser.in
            File savefile =directoryChooser.showDialog(windowManager.getStage());
            path = savefile.getPath();
            storageDirLabel.setText(savefile.getPath());
        }catch (NullPointerException n){
            showInformationDialog("Default Pfad ausgewählt");
        }

    }

    /**
     * information dialog
     *
     * @param info String to be shown as error message to the user
     */
    public void showInformationDialog(String info){
        Alert information = new Alert(Alert.AlertType.INFORMATION, info);
        information.setHeaderText("Ein Fehler ist Aufgetreten");
        information.show();
    }

    /**
     * Shooting ends
     * to do so the service methode endShooting gets called
     * and main frame gets called
     *
     * catches ServiceException caused by Service methodes
     *
     * @param actionEvent press action event
     */
    public void onStopShootingPressed(ActionEvent actionEvent) {
        try {
            shootingService.endShooting();
            windowManager.showScene(WindowManager.SHOW_MAINSCENE);
            Alert information = new Alert(Alert.AlertType.INFORMATION, "Shooting wurde beendet");
            information.setHeaderText("Bestätigung");
            information.show();
        } catch (ServiceException e) {
            showInformationDialog("Shooting konnte nicht beendet werden!");
        }
    }
}
