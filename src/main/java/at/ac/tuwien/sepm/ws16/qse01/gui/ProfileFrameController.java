package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.ProfileButtonCell;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.ProfileCheckboxCell;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.ProfileImgCell;
import at.ac.tuwien.sepm.ws16.qse01.service.BackgroundService;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.List;

/**
 * The controller for the profileFrame.
 */
@Controller
@Primary
public class ProfileFrameController extends SettingFrameController{

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileFrameController.class);




    /* BEGINN OF PROFILE Table Column FXML */
    @FXML
    private TableView tableProfil;

    @FXML
    private TableColumn colProfilID;
    @FXML
    private TableColumn colProfilName;
    @FXML
    private TableColumn colProfilMobil;
    @FXML
    private TableColumn colProfilGreen;
    @FXML
    private TableColumn colProfilFilter;
    @FXML
    private TableColumn colProfilDrucken;
    @FXML
    private TableColumn colProfilWatermark;
    @FXML
    private TableColumn colProfilAktion;

    /* Profile TEXTFIELDS for ADDING */
    @FXML
    private TextField txProfilName;
    @FXML
    private CheckBox txProfilMobil;
    @FXML
    private CheckBox txProfilGreen;
    @FXML
    private CheckBox txProfilFilter;
    @FXML
    private CheckBox txProfilDrucken;
    @FXML
    private TextField txProfilWatermark;






    @Autowired
    public ProfileFrameController(ProfileService pservice, LogoWatermarkService logoService, BackgroundService bservice, WindowManager windowmanager) throws ServiceException {
        super(pservice,logoService,bservice,windowmanager);
    }


    @FXML
    protected void initialize(){
        LOGGER.debug("Initializing profile frame ...");
        try {
           /* ######################### */
            /* INITIALIZING PROFIL TABLE */
            /* ######################### */
            tableProfil.setEditable(true);

            colProfilID.setCellValueFactory(new PropertyValueFactory<Profile, Integer>("id"));

            colProfilName.setCellValueFactory(new PropertyValueFactory<Profile, String>("name"));
            colProfilName.setCellFactory(TextFieldTableCell.forTableColumn());
            colProfilName.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Profile, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Profile, String> t) {
                            try {
                                Profile p = ((Profile) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())
                                );
                                if (t.getNewValue().compareTo("") != 0) {
                                    p.setName(t.getNewValue());
                                    pservice.edit(p);
                                } else {
                                    refreshTableProfiles(pservice.getAllProfiles());
                                }

                            } catch (ServiceException e) {
                                try {
                                    refreshTableProfiles(pservice.getAllProfiles());
                                } catch (ServiceException e1) {
                                    LOGGER.error("Error: could not refresh the profile table: ",e1);
                                }

                            }

                        }
                    });

            /* MobileVersion Column */
            colProfilMobil.setStyle("-fx-alignment: CENTER;");
            colProfilMobil.setSortable(false);
            colProfilMobil.setCellValueFactory(
                    new Callback<TableColumn.CellDataFeatures<Profile, Boolean>,
                            ObservableValue<Boolean>>() {

                        @Override
                        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Profile, Boolean> p) {
                            return new SimpleBooleanProperty(p.getValue() != null);
                        }
                    });
            //Adding the checkbox to the cell
            colProfilMobil.setCellFactory(
                    new Callback<TableColumn<Profile, Boolean>, TableCell<Profile, Boolean>>() {

                        @Override
                        public TableCell<Profile, Boolean> call(TableColumn<Profile, Boolean> p) {

                            return new ProfileCheckboxCell(profList,pservice,"mobil");
                        }

                    });

            /* Greenscreen Column */
            colProfilGreen.setStyle("-fx-alignment: CENTER;");
            colProfilGreen.setSortable(false);
            colProfilGreen.setCellValueFactory(
                    new Callback<TableColumn.CellDataFeatures<Profile, Boolean>,
                            ObservableValue<Boolean>>() {

                        @Override
                        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Profile, Boolean> p) {
                            return new SimpleBooleanProperty(p.getValue() != null);
                        }
                    });

            //Adding the checkbox to the cell
            colProfilGreen.setCellFactory(
                    new Callback<TableColumn<Profile, Boolean>, TableCell<Profile, Boolean>>() {

                        @Override
                        public TableCell<Profile, Boolean> call(TableColumn<Profile, Boolean> p) {

                            return new ProfileCheckboxCell(profList,pservice,"green");
                        }

                    });
             /* Filter Column */
            colProfilFilter.setStyle("-fx-alignment: CENTER;");
            colProfilFilter.setSortable(false);
            colProfilFilter.setCellValueFactory(
                    new Callback<TableColumn.CellDataFeatures<Profile, Boolean>,
                            ObservableValue<Boolean>>() {

                        @Override
                        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Profile, Boolean> p) {
                            return new SimpleBooleanProperty(p.getValue() != null);
                        }
                    });
            //Adding the checkbox to the cell
            colProfilFilter.setCellFactory(
                    new Callback<TableColumn<Profile, Boolean>, TableCell<Profile, Boolean>>() {

                        @Override
                        public TableCell<Profile, Boolean> call(TableColumn<Profile, Boolean> p) {

                            return new ProfileCheckboxCell(profList,pservice,"filter");
                        }

                    });
              /* Drucken Column */
            colProfilDrucken.setStyle("-fx-alignment: CENTER;");
            colProfilDrucken.setSortable(false);
            colProfilDrucken.setCellValueFactory(
                    new Callback<TableColumn.CellDataFeatures<Profile, Boolean>,
                            ObservableValue<Boolean>>() {

                        @Override
                        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Profile, Boolean> p) {
                            return new SimpleBooleanProperty(p.getValue() != null);
                        }
                    });
            //Adding the checkbox to the cell
            colProfilDrucken.setCellFactory(
                    new Callback<TableColumn<Profile, Boolean>, TableCell<Profile, Boolean>>() {

                        @Override
                        public TableCell<Profile, Boolean> call(TableColumn<Profile, Boolean> p) {

                            return new ProfileCheckboxCell(profList,pservice,"drucken");
                        }

                    });
            /* Watermark Column */
            colProfilWatermark.setStyle("-fx-alignment: CENTER;");
            colProfilWatermark.setSortable(false);
            colProfilWatermark.setCellValueFactory(new PropertyValueFactory<Profile, String>("watermark"));
            colProfilWatermark.setCellFactory(new Callback<TableColumn, TableCell>() {
                @Override
                public TableCell call(TableColumn p) {

                    return new ProfileImgCell(profList,pservice);

                }
            });
            /* Aktion Column */
            colProfilAktion.setStyle("-fx-alignment: CENTER;");
            colProfilAktion.setSortable(false);
            colProfilAktion.setCellValueFactory(
                    new Callback<TableColumn.CellDataFeatures<Profile, Boolean>,
                            ObservableValue<Boolean>>() {

                        @Override
                        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Profile, Boolean> p) {
                            return new SimpleBooleanProperty(p.getValue() != null);
                        }
                    });

            //Adding the X-Button to the cell
            colProfilAktion.setCellFactory(
                    new Callback<TableColumn<Profile, Boolean>, TableCell<Profile, Boolean>>() {

                        @Override
                        public TableCell<Profile, Boolean> call(TableColumn<Profile, Boolean> p) {

                            return new ProfileButtonCell(profList,pservice,windowManager.getStage());
                        }

                    });

            this.refreshTableProfiles(pservice.getAllProfiles());


             /* ######################### */
            /* INITIALIZING selecting Profile   */
            /* ######################### */
            tableProfil.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectedProfile = (Profile) newSelection;
                    selectedProfileID = selectedProfile.getId();

                    LOGGER.info("Profile selected -> "+selectedProfile.getName()+"_"+selectedProfile.getId());
                    System.out.println("profile selected....");
                    try {
                        refreshTablePosition(pservice.getAllPositions());
                        refreshTableKameraPosition(pservice.getAllPairCameraPositionOfProfile(selectedProfile.getId()));

                        refreshTableLogo(pservice.getAllPairLogoRelativeRectangle(selectedProfile.getId()));

                        refreshLogoAutoComplete(selectedProfile);

                        refreshTableBackground(pservice.getAllBackgroundOfProfile(selectedProfileID));

                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void saveProfil(){
        LOGGER.error("Profil Add Button has been clicked");
        String name = txProfilName.getText();
        if(name.trim().compareTo("") == 0){
            LOGGER.error("in error");
            showError("Sie müssen einen Namen eingeben!");
        }else {

            Profile p = new Profile(name,txProfilDrucken.isSelected(),txProfilFilter.isSelected(),txProfilGreen.isSelected(),txProfilMobil.isSelected(),txProfilWatermark.getText());

            if (txProfilWatermark.getText().compareTo("Hochladen...") != 0) // wenn ein watermark ausgewählt ist
                p.setWatermark(txProfilWatermark.getText());

            try {
                LOGGER.info("adding the new profil to tableView...");


                pservice.add(p);
                profList.add(p);

                txProfilDrucken.setSelected(false);
                txProfilFilter.setSelected(false);
                txProfilGreen.setSelected(false);
                txProfilMobil.setSelected(false);
                txProfilName.clear();
                txProfilWatermark.setText("Hochladen...");

            } catch (ServiceException e) {
                LOGGER.error("Fehler: Profil konnte nicht erstellt werden..."+e.getMessage());
            }
        }
    }

    @Override
    protected void watermarkUpload(){
        fileChooser.setTitle("Watermark Hochladen...");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                //  new FileChooser.ExtensionFilter("All Images", "*.*")
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            txProfilWatermark.setText(file.getAbsolutePath());
        }
    }





    @Override
    protected void backgroundUpload() {

    }

    @Override
    protected void positionUpload() {
        System.out.println("klicked...");

    }

    @Override
    protected void savePosition() {

    }

    @Override
    protected void fullScreenPreview() {

    }

    @Override
    protected void saveLogo() {

    }

    @Override
    protected void logoUpload() {

    }

    protected void refreshTableProfiles(List<Profile> profileList){
        LOGGER.info("refreshing the profil table...");
        profList.clear();
        profList.addAll(profileList);
        tableProfil.setItems(profList);

    }
}
