package org.openbooth.gui;

import org.openbooth.util.ImageHandler;
import org.openbooth.entities.Profile;
import org.openbooth.gui.specialCells.ProfileButtonCell;
import org.openbooth.gui.specialCells.ProfileCheckboxCell;
import org.openbooth.gui.specialCells.ProfileImgCell;
import org.openbooth.service.BackgroundService;
import org.openbooth.service.CameraService;
import org.openbooth.service.LogoWatermarkService;
import org.openbooth.service.ProfileService;
import org.openbooth.service.exceptions.ServiceException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
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

    private CameraService cameraService;


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
    @FXML
    private Button txProfilUpload;
    @FXML
    private Button txProfilAdd;






    @Autowired
    public ProfileFrameController(CameraService cameraService, ProfileService pservice, LogoWatermarkService logoService, BackgroundService bservice, WindowManager windowmanager, ImageHandler imageHandler) throws ServiceException {
        super(pservice,logoService,bservice,windowmanager,imageHandler);
        this.cameraService = cameraService;
    }


    @FXML
    private void initialize(){
        LOGGER.debug("Initializing profile frame ...");

        double screenWidth= Screen.getPrimary().getBounds().getWidth();
        double screenHeight=Screen.getPrimary().getBounds().getHeight();
        containerGrid.setMinHeight(screenHeight-50);
        containerGrid.setPrefWidth(screenWidth-30);

        try {
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

                    return new ProfileImgCell(profList,pservice,imageHandler,windowManager.getStage());

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

                            return new ProfileButtonCell(imageHandler,profList,pservice,windowManager.getStage());
                        }

                    });

            this.refreshTableProfiles(pservice.getAllProfiles());


             /* ######################### */
            /* INITIALIZING selecting Profile   */
            /* ######################### */
            tableProfil.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectedProfile.clear();
                    selectedProfile.add((Profile) newSelection);

                    LOGGER.debug("Profile selected -> "+ selectedProfile);
                    try {
                        setControllers();
                        refreshTablePosition(pservice.getAllPositions());
                        this.posList.clear();
                        this.posList.addAll(pservice.getAllPositions());
                        refreshTableKameraPosition(pservice.getAllPairCamerasWithPositionByProfile(selectedProfile.get(0).getId()),this.posList);
                        refreshTableLogo(pservice.getAllPairLogoRelativeRectangle(selectedProfile.get(0).getId()));

                        refreshLogoAutoComplete(selectedProfile);

                        refreshTableCategory(pservice.getAllCategoryOfProfile(selectedProfile.get(0).getId()),bservice.getAllCategories());
                       // refreshCategoryComboBox(bservice.getAllCategories());// OfProfile(selectedProfile.getId()));
                    } catch (ServiceException e) {
                       LOGGER.error("Couldnt refreshing all tables with new selected profile",e);
                    }
                }
            });


            txProfilUpload.setBackground(imageHandler.getButtonBackground("/images/upload1.png",50,50));
            txProfilUpload.setPrefWidth(50);
            txProfilUpload.setPrefHeight(50);

            txProfilAdd.setBackground(imageHandler.getButtonBackground("/images/add.png",50,50));
            txProfilAdd.setPrefWidth(50);
            txProfilAdd.setPrefHeight(50);

            txProfilName.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!newValue.isEmpty()){
                    txProfilAdd.setBackground(imageHandler.getButtonBackground("/images/add.png",50,50));
                }else
                    txProfilAdd.setBackground(imageHandler.getButtonBackground("/images/add.png",50,50));

            });


        } catch (ServiceException e) {
            LOGGER.error("Couldnt initialize profile tableview",e);
        }
    }


    @FXML
    protected void saveProfil(){
        String name = txProfilName.getText();
        if(name.trim().compareTo("") == 0){
            LOGGER.error("in error");
            showError("Sie müssen einen Namen eingeben!");
        }else {

            Profile p = new Profile(name,txProfilDrucken.isSelected(),txProfilFilter.isSelected(),txProfilGreen.isSelected(),txProfilMobil.isSelected(),txProfilWatermark.getText());

            if (txProfilWatermark.getText().compareTo("Hochladen...") != 0) // wenn ein watermark ausgewählt ist
                p.setWatermark(txProfilWatermark.getText());

            try {
                LOGGER.debug("adding the new profil to tableView...");


                pservice.add(p);
                profList.add(p);

                txProfilDrucken.setSelected(false);
                txProfilFilter.setSelected(false);
                txProfilGreen.setSelected(false);
                txProfilMobil.setSelected(false);
                txProfilName.clear();
                txProfilWatermark.setText("Hochladen...");


                txProfilUpload.setBackground(imageHandler.getButtonBackground("/images/upload1.png",50,50));


            } catch (ServiceException e) {
                LOGGER.error("Fehler: Profil konnte nicht erstellt werden...",e);
            }
        }
    }

    @FXML
    private void watermarkUpload(){
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
            txProfilUpload.setBackground(imageHandler.getButtonBackground("/images/upload1.png",50,50));
        }
    }



    protected void refreshTableProfiles(List<Profile> profileList){
        LOGGER.debug("refreshing the profil table...");
        profList.clear();
        profList.addAll(profileList);
        tableProfil.setItems(profList);

    }

    protected ObservableList<Profile> getSelectedProfile(){
        return this.selectedProfile;
    }


}
