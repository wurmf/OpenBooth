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
                storageDirLabel.setVisible(false);
            }else{
                stopButton.setVisible(false);
                startButton.setVisible(true);
                storage.setVisible(true);
                storageDirLabel.setVisible(true);
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
            LOGGER.debug("initialise -"+e);
            showInformationDialog("Bitte erstellen Sie ein Profil");
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
                        Path storagepath = null;

                        String resource = System.getProperty("user.home");

                        /*if(mobiel) {
                            storage = Paths.get(resource + "fotostudio/Mobil");
                        }else{*/
                        storagepath = Paths.get(resource + "/fotostudio/Studio");
                        // }
                        if (storagepath != null) {
                            try {
                                Files.createDirectories(storagepath);
                            }  catch (IOException e) {
                                LOGGER.error("creatin initial folder" + e);
                                showInformationDialog("Derspeicherort konnte nicht erstellt werden");
                            }
                            /*catch (FileAlreadyExistsException e) {
                                LOGGER.info("shooting folder already exists" + e);

                            }*/
                            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
                            Date date = new Date();
                            Path shootingstorage = Paths.get(storagepath + "/" + dateFormat.format(date));

                            try {
                                Files.createDirectories(shootingstorage);
                            } catch (FileAlreadyExistsException e) {
                                showInformationDialog("Derspeicherort konnte nicht neu angelegt werden, da er bereits vorhanden ist ");
                                LOGGER.info("shooting folder already exists" + e);

                            } catch (IOException e) {
                                LOGGER.error("creatin shooting folder file" + e);
                                showInformationDialog("Derspeicherort konnte nicht erstellt werden");
                            }
                            path = shootingstorage.toString();
                        }
                    }
                        Shooting shouting = new Shooting(0, (int) profile.getId(), path, true);

                        //storageDirLabel.setText("");
                        LOGGER.info("ShootingAdminController:", path);
                        path = "";

                        shootingService.addShooting(shouting);

                        windowManager.showMiniatureFrame();
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
        //storageDirLabel.setText("");
        windowManager.showMainFrame();
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

            storageDirLabel.setText(savefile.getPath());
            path = savefile.getPath();
        }catch (NullPointerException n){
            showInformationDialog("Kein Pfad gewählt");
        }
    }

    /**
     *Opens Profile administration
     *
     * @param actionEvent press action event
     */
    public void onEditPressed(ActionEvent actionEvent) {
        windowManager.showProfileScene();
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
            windowManager.showMainFrame();
            Alert information = new Alert(Alert.AlertType.INFORMATION, "Shooting wurde beendet");
            information.setHeaderText("Bestätigung");
            information.show();
        } catch (ServiceException e) {
            showInformationDialog("Shooting konnte nicht beendet werden!");
        }
    }
}
