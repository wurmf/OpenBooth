package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ImageServiceImpl;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ProfileServiceImpl;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ShootingServiceImpl;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

//TODO: Remove everything Swing related! We have to use JavaFX-Classes

/**
 * Created by Aniela on 23.11.2016.
 */
@Component
public class ShootingAdminController {
    private SpringFXMLLoader springFXMLLoader;
    private Stage primaryStage;

    @FXML
    private Label lb_storageplace;//lb_storageplace.setText();
    @FXML
    private ChoiceBox cb_Profile;

    String path ="";

    ShootingService sessionService;
    ProfileService profileService;

    @Autowired
    public ShootingAdminController(SpringFXMLLoader springFXMLLoader) throws Exception {
        sessionService = new ShootingServiceImpl();
        profileService= new ProfileServiceImpl();
        this.springFXMLLoader = springFXMLLoader;
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

        cb_Profile.setItems(observableListProfile);

       // cb_Profile.setCellFactory(new PropertyValueFactory<Profile,String>("name"));


        if(cb_Profile!=null){
            cb_Profile.setValue(observableListProfile.get(0));
        }
    } catch (ServiceException e) {
        informationDialog("Bitte erstellen Sie ein Profil");
    }
    }

    /**
     *
     * when pressed an new session starts(costumer interface opens)
     *
     * @param actionEvent
     *
     */
    public void on_StartSessoionPressed(ActionEvent actionEvent) {

        if (cb_Profile.getValue() != null) {
         Profile profile = (Profile) cb_Profile.getSelectionModel().getSelectedItem();
            Shooting shouting = new Shooting(profile.getId(), path, true);

                LOGGER.info("ShootingAdminController:", shouting);

                try {
                    sessionService.add_session(shouting);
                } catch (ServiceException serviceExeption) {
                    LOGGER.info("shouting erstellen", serviceExeption.getMessage());
                    informationDialog("Es konnte keine Shooting erstellt werden.");
                }
            } else {
                informationDialog("Bitte erstellen sie ein neues Profil");
            }
    }

    /**
     *
     * Opens Mainfframe again
     * @param actionEvent
     *
     */
    public void on_DemolitionPressed(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainframe.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
           // stage.setTitle("Main Frame");
            stage.setScene(new Scene(root1));
            stage.show();
        }catch (IOException s){
            informationDialog("Es konnte nicht zur Hauptseite gewechselt werden. ");
            LOGGER.info("ShootingAdminController: on_DemolitionPressed",s.getMessage());
        }

    }

    /**
     * find ordner of desire to save new project in
     * @param actionEvent
     *
     */
    public void on_FinedPressed(ActionEvent actionEvent) {

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("C:"));// to adapt
        chooser.setDialogTitle("Speicher ort Wählen");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
           path = chooser.getSelectedFile().toString();
            lb_storageplace.setText(path);
            LOGGER.info("ShootingAdminController: Path ",path);
        } else {
            informationDialog("Noch keinen Ordner ausgewählt");
            LOGGER.info("ShootingAdminController:on_FinedPressed");
        }
    }

    /**
     *Opens Profile edeting
     *
     * @param actionEvent
     *
     */
    public void on_EditPressed(ActionEvent actionEvent) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profileFrame.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            // stage.setTitle("Main Frame");
            stage.setScene(new Scene(root1));
            stage.show();
        }catch (IOException s){
            informationDialog("Es konnte nicht zu den Profilen gewechselt werden. ");
            LOGGER.info("ShootingAdminController: on_DemolitionPressed",s.getMessage());
        }

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

