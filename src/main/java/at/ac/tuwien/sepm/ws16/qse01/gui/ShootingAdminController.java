package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.nio.file.Path;
import java.nio.file.Paths;
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
    private Label storageDirLabel;
    @FXML
    private ChoiceBox<Profile> profileChoiceBox;
    @FXML
    private Button bgStorage;
    @FXML
    private Label bgStorageDirLabel;

    private String path =null;
    private String bgPath="";
    private static final Logger LOGGER = LoggerFactory.getLogger(ShootingDAO.class);


    private ShootingService shootingService;
    private ProfileService profileService;
    private WindowManager windowManager;

    @Autowired
    public ShootingAdminController(ProfileService profileService, ShootingService shootingService, WindowManager windowManager) {
        this.shootingService = shootingService;
        this.profileService= profileService;
        this.windowManager = windowManager;
    }

    /**
     * Initialise shooting Admin Controller
     * Depending on whether there is an active shooting or not some functions are unusable
     * To achieve this, they are set invisible/visible
     * Furthermore this method initialises profileChoiceBox
     *
     * Catches ServiceException caused by Service methods
     */
    @FXML
    private void initialize() {

        try {
            setButtons();
            String userHome = System.getProperty("user.home");

            Path storagepath = Paths.get(userHome+"/fotostudio/Studio");
            storageDirLabel.setText(storagepath.toString());
            List<Profile> prof = profileService.getAllProfiles();
            if(prof!=null&&!prof.isEmpty()) {
                ObservableList<Profile> observableListProfile = FXCollections.observableList(prof);
                profileChoiceBox.setItems(observableListProfile);
                profileChoiceBox.setValue(observableListProfile.get(0));
            }

        } catch (ServiceException e) {
            LOGGER.debug("initialize - ",e);
            showInformationDialog("Bitte erstellen Sie ein Profil");
        }
    }

    public void inactivemode(){
        try {
            setButtons();
        }catch (ServiceException e ){
            LOGGER.error("inactivemode - ",e);
        }

    }

    /**
     * Checks if there is an active shooting and sets visibility and contents of buttons and labels accordingly.
     * @throws ServiceException if an error occurs while retrieving the active shooting from the service layer.
     */
    private void setButtons() throws ServiceException{
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
            String bgPathAcitveShooting=shootingService.searchIsActive().getBgPictureFolder();
            if(bgPathAcitveShooting!=null && !bgPathAcitveShooting.isEmpty()){
                bgStorageDirLabel.setText(bgPathAcitveShooting);
            }

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
    }
    /**
     * when pressed a new shooting starts
     * to successfully start a new shooting a storage path gets selected and a profile must be selected
     */
    @FXML
    public void onStartShootingPressed() {
        LOGGER.info(path);
        if (profileChoiceBox.getValue() != null) {
                try{
                    Profile profile = profileChoiceBox.getSelectionModel().getSelectedItem();

                    if(path==null) {
                        try {
                            path = shootingService.createPath();
                        }catch (ServiceException s){
                            LOGGER.error("onStartShootingPressed - ",s);
                            showInformationDialog("Beim erstellen des Speicherpfades ist ein Fehler aufgetreten.");
                        }
                    }
                    Shooting shouting = new Shooting(0, profile.getId(), path,bgPath, true);

                    LOGGER.info("ShootingAdminController:", path);
                    path = "";

                    shootingService.addShooting(shouting);

                    windowManager.showScene(WindowManager.SHOW_CUSTOMERSCENE);
                    windowManager.initShotFrameManager();
                } catch (ServiceException serviceExeption) {
                    LOGGER.debug("onStartShootingPressed - ",serviceExeption);
                    showInformationDialog("Es konnte keine Shooting erstellt werden.");
                }
        } else {
            showInformationDialog("Bitte erstellen Sie ein neues Profil");
        }
    }

    /**
     * Opens Mainframe again
     */
    @FXML
    public void onDemolitionPressed() {
        try {
            if (shootingService.searchIsActive().getActive()) {
                Profile profile = profileChoiceBox.getSelectionModel().getSelectedItem();
                if(shootingService.searchIsActive().getProfileid()!=profile.getId()) {
                    Shooting shooting = new Shooting(shootingService.searchIsActive().getId(), profile.getId(), "",bgPath, true);
                    shootingService.update(shooting);
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

    /**
     * finds the directory of desire to save new project in
     */
    @FXML
    public void onChooseStorageDirPressed() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Speicherort wählen");
        File savefile =directoryChooser.showDialog(windowManager.getStage());
        if(savefile!=null){
            path = savefile.getPath();
            storageDirLabel.setText(savefile.getPath());
        } else {
            showInformationDialog("Default Pfad ausgewählt");
        }
    }

    /**
     * Opens a DirectoryChooser that lets the user choose a folder in which background images are located.
     */
    @FXML
    public void onChooseBgStorageDirPressed(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Quellordner auswählen");
        File sourceFolder =directoryChooser.showDialog(windowManager.getStage());
        if(sourceFolder!=null) {
            bgPath = sourceFolder.getPath();
            bgStorageDirLabel.setText(bgPath);
        } else{
            storageDirLabel.setText("Keine Hintergründe definiert");
        }
    }
    /**
     * information dialog
     *
     * @param info String to be shown as error message to the user
     */
    private void showInformationDialog(String info){
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
     */
    @FXML
    public void onStopShootingPressed() {
        try {
            shootingService.endShooting();
            windowManager.showScene(WindowManager.SHOW_MAINSCENE);
            Alert information = new Alert(Alert.AlertType.INFORMATION, "Shooting wurde beendet");
            information.setHeaderText("Bestätigung");
            information.show();
        } catch (ServiceException e) {
            LOGGER.error("onStopShootingPressed - ",e);
            showInformationDialog("Shooting konnte nicht beendet werden!");
        }
    }
}
