package org.openbooth.gui;

import org.openbooth.entities.Profile;
import org.openbooth.entities.Shooting;
import org.openbooth.service.ProfileService;
import org.openbooth.service.ShootingService;
import org.openbooth.service.exceptions.ServiceException;
import org.openbooth.service.imageprocessing.ImageProcessingManager;
import com.sun.javafx.collections.ObservableListWrapper;
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
import java.util.LinkedList;
import java.util.List;


/**
 * Controlles the Shooting Admnisitration
 */
@Component
public class ShootingAdminController {

    private static final boolean ENABLE_DEBUG_PROFILE = false;
    private static final String DEBUG_PROFILE_NAME = "DebugProfile";
    private static final int DEBUG_PROFILE_ID = 1; //Welches bestehende Profil als DebugProfil aus der Datenbank ausgewählt werden soll

    @FXML
    private Label saveing;
    @FXML
    private Label finallsavingplace;
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
    private Label bgStorageDirLabel;

    private String path =null;
    private String bgPath="";
    private static final Logger LOGGER = LoggerFactory.getLogger(ShootingAdminController.class);


    private ShootingService shootingService;
    private ProfileService profileService;
    private ImageProcessingManager imageProcessingManager;
    private WindowManager windowManager;


    @Autowired
    public ShootingAdminController(ProfileService profileService, ShootingService shootingService, ImageProcessingManager imageProcessingManager, WindowManager windowManager) {
        this.shootingService = shootingService;
        this.profileService= profileService;
        this.imageProcessingManager = imageProcessingManager;
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

        } catch (ServiceException e) {
            LOGGER.error("initialize - ",e);
            showInformationDialog("Bitte erstellen Sie ein Profil");
        }
    }

    public void inactivemode(){
        try {
            setButtons();
            List<Profile> prof = profileService.getAllProfiles();
            if(prof!=null&&!prof.isEmpty()) {
                ObservableList<Profile> observableListProfile = fittingProfiles(prof);
                if(observableListProfile!=null||observableListProfile.isEmpty()){
                    profileChoiceBox.setItems(observableListProfile);
                    profileChoiceBox.setValue(observableListProfile.get(0));
                }
            }
        }catch (ServiceException e ){
            LOGGER.error("inactivemode - ",e);
        }
    }

    private  ObservableList<Profile> fittingProfiles(List<Profile> proflist){
        ObservableList<Profile> profileObservableList = new ObservableListWrapper<Profile>(new LinkedList<>());
        try {

            for(Profile profile: proflist) {
                boolean camerasFitPosition = imageProcessingManager.checkImageProcessing(profile);
                if (camerasFitPosition) {
                    profileObservableList.add(profile);
                }
            }

            if(ENABLE_DEBUG_PROFILE){
                Profile debugProfile = profileService.get(DEBUG_PROFILE_ID);
                debugProfile.setName(DEBUG_PROFILE_NAME);
                profileObservableList.add(debugProfile);
            }
        } catch (ServiceException e) {
            LOGGER.debug("fittingProfiles-",e);
        }
        return profileObservableList;
    }

    /**
     * Checks if there is an active shooting and sets visibility and contents of buttons and labels accordingly.
     * @throws ServiceException if an error occurs while retrieving the active shooting from the service layer.
     */
    private void setButtons() throws ServiceException{
        Shooting activeShooting=shootingService.searchIsActive();
        if (activeShooting!=null&&activeShooting.getActive()) {
            startButton.setVisible(false);
            stopButton.setVisible(true);
            storage.setVisible(false);
            canclebutton.setText("Fortsetzen");
            storageDirLabel.setVisible(true);
            storageDirLabel.setText("  " +activeShooting.getStorageDir());
            gridSave.setVisible(false);
            finallsavingplace.setText(activeShooting.getStorageDir());
            finallsavingplace.setVisible(true);

            saveing.setVisible(true);
            if(activeShooting.getBgPictureFolder()!=null && !activeShooting.getBgPictureFolder().isEmpty()){
                bgStorageDirLabel.setText(activeShooting.getBgPictureFolder());
            }

        } else {
            stopButton.setVisible(false);
            startButton.setVisible(true);
            storage.setVisible(true);
            storageDirLabel.setVisible(true);
            String userHome = System.getProperty("user.home");
            Path storagepath = Paths.get(userHome+"/fotostudio/Studio");
            storageDirLabel.setText(storagepath.toString());
            gridSave.setVisible(true);
            canclebutton.setText("Abbrechen");
            finallsavingplace.setVisible(false);
            saveing.setVisible(true);
        }
    }

    /**
     * when pressed a new shooting starts
     * to successfully start a new shooting a storage path gets selected and a profile must be selected
     */
    @FXML
    public void onStartShootingPressed() {
        LOGGER.debug("onStartShootingPressed - "+path);
        if (profileChoiceBox.getValue() != null) {
                try{
                    Profile profile = profileChoiceBox.getSelectionModel().getSelectedItem();


                    boolean isDebugProfile = ENABLE_DEBUG_PROFILE && profile.getId() == DEBUG_PROFILE_ID;

                    if(!imageProcessingManager.checkImageProcessing(profile) && !isDebugProfile){
                        return;
                    }

                    if(path==null || path.isEmpty()) {
                        path = shootingService.createPath();
                    }

                    Shooting shooting = new Shooting(0, profile.getId(), path,bgPath, true);

                    path = "";

                    shootingService.addShooting(shooting);
                    profileService.setActiveProfile(profile.getId());

                    if(!isDebugProfile){
                        imageProcessingManager.initImageProcessing();
                    }

                    windowManager.showScene(WindowManager.SHOW_CUSTOMERSCENE);

                } catch (ServiceException serviceExeption) {
                    LOGGER.error("onStartShootingPressed - ", serviceExeption);
                    showInformationDialog("Es konnte kein Shooting gestartet werden.");
                }
        } else {
            showInformationDialog("Bitte erstellen Sie ein neues Profil");        }
    }

    /**
     * Opens Mainframe again
     */
    @FXML
    public void onDemolitionPressed() {
        try {
            if (shootingService.searchIsActive().getActive()) {
                Profile profile = profileChoiceBox.getSelectionModel().getSelectedItem();
                if(shootingService.searchIsActive().getProfileid()==profile.getId()) {
                    Shooting shooting = new Shooting(shootingService.searchIsActive().getId(), profile.getId(), "",bgPath, true);
                    LOGGER.debug("Trying to persist shooting. shootingId: "+shooting.getId()+" | bgPath: "+shooting.getBgPictureFolder());
                    profileService.setActiveProfile(profile.getId());
                    profileService.resetActiveProfileNonPersistentAttributes();
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
            bgStorageDirLabel.setText("Keine Hintergründe definiert");
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
        information.initOwner(windowManager.getStage());
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
            imageProcessingManager.stopImageProcessing();
            profileService.resetActiveProfileNonPersistentAttributes();
            shootingService.endShooting();
            inactivemode();
            windowManager.showScene(WindowManager.SHOW_MAINSCENE);
        } catch (ServiceException e) {
            LOGGER.error("onStopShootingPressed - ",e);
            showInformationDialog("Shooting konnte nicht beendet werden!");
        }
    }
}