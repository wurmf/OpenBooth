package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.SessionDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.ServiceExeption;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.SessionControllService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ProfileServiceImpl;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.SessionControllServiceImpl;
import at.ac.tuwien.sepm.ws16.qse01.entities.Session;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Aniela on 23.11.2016.
 */
public class Session_Admin_Controller {

    @FXML
    private Label lb_storageplace;//lb_storageplace.setText();
    @FXML
    private ChoiceBox cb_Profile;

    String path ="";

    SessionControllService sessionService;
    ProfileService profileService;
    public Session_Admin_Controller() throws Exception {
        sessionService=new SessionControllServiceImpl();
        profileService= new ProfileServiceImpl();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionDAO.class);



    /**
     * inizialise cb_Profiles
     * @autor Aniela
     */
    @FXML
    private void initialize() {

        List<Profile> prof = null;
        try {
            prof = profileService.getAllProfiles();
        } catch (ServiceException e) {
            showingdialog(e.getMessage());
        }
        List<String > s= new LinkedList<>();
        ObservableList<String> obj = new ObservableListWrapper<String>(s);
        for (int i = 0; i <prof.size() ; i++) {
           obj.add(prof.get(i).getName());
        }
        cb_Profile.setItems(obj);
        if(cb_Profile!=null){
            cb_Profile.setValue(obj.get(0));
        }

    }


    /**
     *
     * when pressed an new session starts(costumer interface opens)
     *
     * @param actionEvent
     * @autor Aniela
     */
    public void on_StartSessoionPressed(ActionEvent actionEvent) {
        List<Profile> prof = null;
        try {
            prof = profileService.getAllProfiles();
        } catch (ServiceException e) {
            showingdialog(e.getMessage());
        }
        int i = 0;
            if (cb_Profile.getValue() == null) {
                while (cb_Profile.getValue() != prof.get(i).getName()) {
                    i++;
                }
                Session session = new Session(prof.get(i).getId(), path, true);

                LOGGER.info("Session_Admin_Controller:", session);

                try {
                    sessionService.add_session(session);
                } catch (ServiceExeption serviceExeption) {
                    LOGGER.info("session erstellen", serviceExeption.getMessage());
                    showingdialog("Es konnte keine Session erstellt werden.");
                }
            } else {
                showingdialog("Bitte erstellen sie ein neues Profil");
            }


    }

    /**
     *
     * Opens Mainfframe again
     * @param actionEvent
     * @autor Aniela
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
            showingdialog("Es konnte nicht zur Hauptseite gewechselt werden. ");
            LOGGER.info("Session_Admin_Controller: on_DemolitionPressed",s.getMessage());
        }

    }

    /**
     * find ordner of desire to save new project in
     * @param actionEvent
     * @autor Aniela
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
            LOGGER.info("Session_Admin_Controller: Path ",path);
        } else {
            showingdialog("Noch keinen Ordner ausgewählt");
            LOGGER.info("Session_Admin_Controller:on_FinedPressed");
        }
    }

    /**
     * deletes chosen Profile
     *
     * @param actionEvent
     * @autor Aniela
     */
    public void on_DeleteButtonPressed(ActionEvent actionEvent) {

        are_you_sure((String) cb_Profile.getValue());

        //delete from DAO
    }

    /**
     *Edit chosen Profile
     *
     * @param actionEvent
     * @autor Aniela
     */
    public void on_EditPressed(ActionEvent actionEvent) {
    }

    /**
     *Creat new Profile
     *
     * @param actionEvent
     * @autor Aniela
     */
    public void on_NewPressed(ActionEvent actionEvent) {
    }

    /**
     * check if you want to delete
     *
     * @param profil
     * @autor Aniela
     */
    public void are_you_sure(String profil){

        Object [] options={"Ja", "Nein"};

        int chouse= JOptionPane.showOptionDialog(null,"Möchten sie das gewählte Profiel tatsächlich löschen? "
                ,"", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);

        if(chouse==0){


            //profileService.erase();
            initialize();
            //
            //yes
        }

    }

    private JOptionPane showingdialog(String messege){
        JOptionPane dialog = new JOptionPane();
        dialog.showMessageDialog(null, messege);
        return dialog;
    }
}

