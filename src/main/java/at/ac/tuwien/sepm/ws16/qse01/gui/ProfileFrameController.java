package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ProfileServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * The controller for the profileFrame.
 */
@Component
public class ProfileFrameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileFrameController.class);

    private ObservableList<Profile> pList = FXCollections.observableArrayList();

    private ProfileService pservice = null;

    private Profile activeProfile = null;

    @FXML
    private TableView<Profile> tv_profiles;
    @FXML
    private TableColumn<Profile,Integer> tc_id;
    @FXML
    private TableColumn<Profile,String> tc_name;
    @FXML
    private TableColumn<Profile,Boolean> tc_isActive;
    @FXML
    private Label label_activeProfile;
    @FXML
    private TextField tf_activeProfile;

    @FXML
    private Button bt_Add;
    @FXML
    private Button bt_Edit;
    @FXML
    private Button bt_Erase;
    @FXML
    private Button bt_SetActive;
    @FXML
    private Label label_profileName;
    @FXML
    private TextField tf_profileName;


    private SpringFXMLLoader springFXMLLoader;
    private Stage profileStage;

    @Autowired
    public ProfileFrameController(SpringFXMLLoader springFXMLLoader) throws ServiceException {
        this.springFXMLLoader = springFXMLLoader;
    }

    @FXML
    private void initialize(){
        LOGGER.debug("Initializing profile frame ...");
        try {
            //pservice = new ProfileServiceImpl();
            tc_id.setCellValueFactory(new PropertyValueFactory<Profile, Integer>("id"));
            tc_name.setCellValueFactory(new PropertyValueFactory<Profile, String>("name"));
            tc_isActive.setCellValueFactory(new PropertyValueFactory<Profile, Boolean>("isActive"));
            this.refreshTableViewProfiles(pservice.getAllProfiles());
            this.activeProfile = pservice.getActiveProfile();
            if (this.activeProfile != null)
            {String activeProfileName = this.activeProfile.getName();
            tf_activeProfile.setText(activeProfileName);}

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private void refreshTableViewProfiles(List<Profile> profileList){
        pList.clear();
        pList.addAll(profileList);
        tv_profiles.setItems(pList);
    }

    public void setProfileStage(Stage profileStage) {
        this.profileStage = profileStage;
    }

    @FXML
    private void bt_AddClicked(ActionEvent actionEvent) {
        LOGGER.debug("Button Add has been clicked");
        String name = tf_profileName.getText();
        Profile p = new Profile(name);
        try {
            pservice.add(p);
            this.refreshTableViewProfiles(pservice.getAllProfiles());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bt_EditClicked(ActionEvent actionEvent) {
        LOGGER.debug("Button Edit has been clicked");
        Profile p = tv_profiles.getSelectionModel().getSelectedItem();
        String name = tf_profileName.getText();
        p.setName(name);
        try {
            pservice.edit(p);
            this.refreshTableViewProfiles(pservice.getAllProfiles());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bt_EraseClicked(ActionEvent actionEvent) {
        LOGGER.debug("Button Erase has been clicked");
        Profile p = tv_profiles.getSelectionModel().getSelectedItem();
        if(p != null) try {
            pservice.erase(p);
            refreshTableViewProfiles(pservice.getAllProfiles());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bt_SetActiveClicked(ActionEvent actionEvent) {
        LOGGER.debug("Button Set Active has been clicked");
        Profile p = tv_profiles.getSelectionModel().getSelectedItem();
        if(p != null) try {
            pservice.setActiveProfile(p);
            tf_activeProfile.setText(p.getName());
            refreshTableViewProfiles(pservice.getAllProfiles());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
