package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.*;
import at.ac.tuwien.sepm.ws16.qse01.service.CameraService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * Controller for Setting Frame .
 */
@Controller
public class SettingFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileFrameController.class);
    private SpringFXMLLoader springFXMLLoader;
    private WindowManager windowManager;
    @Resource
    private ProfileService pservice;
    @Resource
    private CameraService cameraService;

    private final ObservableList<Profile> profList = FXCollections.observableArrayList();

    private final ObservableList<Position> posList = FXCollections.observableArrayList();

    private final ObservableList<Profile.PairCameraPosition> kamPosList = FXCollections.observableArrayList();

    private final FileChooser fileChooser = new FileChooser();

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


     /* BEGINN OF Kamera & Logo Verwaltung FXML */
     @FXML
     private ComboBox profilList;

     /* BEGINN OF Position Table Column FXML */
     @FXML
     private TableView tablePosition;
     @FXML
     private TableColumn colPositionID;
     @FXML
     private TableColumn colPositionName;
     @FXML
     private TableColumn colPositionBild;
     @FXML
     private TableColumn colPositionAktion;

    /* Profile TEXTFIELDS for ADDING */
    @FXML
    private TextField txPositionName;
    @FXML
    private TextField txPositionBild;

    /* BEGINN OF KameraPosition-Zuweisung Table Column FXML */
    @FXML
    private TableView tableKamPos;
    @FXML
    private TableColumn colKamPosID;
    @FXML
    private TableColumn colKamPosKamera;
    @FXML
    private TableColumn colKamPosPosition;

    @Autowired
    public SettingFrameController(SpringFXMLLoader springFXMLLoader, ProfileService pservice, CameraService cameraService,WindowManager windowmanager) throws ServiceException {
        this.springFXMLLoader = springFXMLLoader;
        this.pservice = pservice;
        this.cameraService = cameraService;
        this.windowManager = windowmanager;

    }

    private void refreshTableProfiles(List<Profile> profileList){
        LOGGER.info("refreshing the profil table...");
        profList.clear();
        profList.addAll(profileList);
        tableProfil.setItems(profList);
    }
    private void refreshTablePosition(List<Position> positionList){
        LOGGER.info("refreshing the position table...");
        posList.clear();
        posList.addAll(positionList);
        tablePosition.setItems(posList);
    }

    private void refreshTableKameraPosition(List<Camera> camList){
        LOGGER.info("refreshing the KameraPosition-Zuweisung table...");
        this.kamPosList.clear();
        for(Camera cam: camList){
            try {
                this.kamPosList.add(new Profile.PairCameraPosition(cam,pservice.getPositionOfCameraOfProfile(cam),true));
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
        //this.kamPosList.addAll(kamPosList);
        tablePosition.setItems(this.kamPosList);
    }

    @FXML
    private void initialize(){
        LOGGER.debug("Initializing profil frame ...");
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
                                    //new EntityException("Vorname", "Vorname darf nicht leer sein.");
                                    refreshTableProfiles(pservice.getAllProfiles());
                                }

                            } catch (ServiceException e) {
                                try {
                                    refreshTableProfiles(pservice.getAllProfiles());
                                } catch (ServiceException e1) {
                                   LOGGER.debug("Error: could not refresh the profile table: "+e1.getMessage());
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
            /* INITIALIZING ComboBox Profil-List */
            /* ######################### */
            profilList.getItems().addAll(pservice.getAllProfiles());
            profilList.valueProperty().addListener(new ChangeListener<Profile>() {
                @Override
                public void changed(ObservableValue ov, Profile t, Profile selectedProfil) {
                    try {
                        refreshTablePosition(pservice.getAllPositionsOfProfile(selectedProfil));
                      //  refreshTableKameraPosition(pservice.getAllCamerasOfProfile(selectedProfil));
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }
            });

            /* ######################### */
            /* INITIALIZING Position Table */
            /* ######################### */

            tablePosition.setEditable(true);

            colPositionID.setCellValueFactory(new PropertyValueFactory<Position, Integer>("id"));

            colPositionName.setCellValueFactory(new PropertyValueFactory<Position, String>("name"));
            colPositionName.setCellFactory(TextFieldTableCell.forTableColumn());
            colPositionName.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Position, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Position, String> t) {
                            try {
                                Position p = ((Position) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())
                                );
                                if (t.getNewValue().compareTo("") != 0) {
                                    p.setName(t.getNewValue());
                                    pservice.editPosition(p);
                                } else {
                                    //new EntityException("Vorname", "Vorname darf nicht leer sein.");
                                    refreshTablePosition(pservice.getAllPositionsOfProfile(((Profile)profilList.getSelectionModel().getSelectedItem())));
                                }

                            } catch (ServiceException e) {
                                try {
                                    refreshTableProfiles(pservice.getAllProfiles());
                                } catch (ServiceException e1) {
                                    LOGGER.debug("Error: could not refresh the profile table: "+e1.getMessage());
                                }

                            }

                        }
                    });
             /* Bild Column */
            colPositionBild.setStyle("-fx-alignment: CENTER;");
            colPositionBild.setSortable(false);
            colPositionBild.setCellValueFactory(new PropertyValueFactory<Position, String>("buttonImagePath"));
            colPositionBild.setCellFactory(new Callback<TableColumn, TableCell>() {
                @Override
                public TableCell call(TableColumn p) {

                    return new PositionImgCell(posList,pservice);

                }
            });
            /* Aktion Column */
            colPositionAktion.setStyle("-fx-alignment: CENTER;");
            colPositionAktion.setSortable(false);
            colPositionAktion.setCellValueFactory(
                    new Callback<TableColumn.CellDataFeatures<Position, Boolean>,
                            ObservableValue<Boolean>>() {

                        @Override
                        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Position, Boolean> p) {
                            return new SimpleBooleanProperty(p.getValue() != null);
                        }
                    });

            //Adding the X-Button to the cell
            colPositionAktion.setCellFactory(
                    new Callback<TableColumn<Position, Boolean>, TableCell<Position, Boolean>>() {

                        @Override
                        public TableCell<Position, Boolean> call(TableColumn<Position, Boolean> p) {

                            return new PositionButtonCell(posList,pservice,windowManager.getStage());
                        }

                    });

           // this.refreshTablePosition(pservice.getAllPositionsOfProfile((Profile)profilList.getItems().get(0)));

             /* ######################### */
            /* INITIALIZING KameraPosition Zuweisung TABLE */
            /* ######################### */
            tableKamPos.setEditable(true);
            colKamPosKamera.setCellValueFactory(new PropertyValueFactory<Profile.PairCameraPosition, String>("cameraLabel"));
           /* colKamPosKamera.setCellFactory(TextFieldTableCell.forTableColumn());
            colKamPosKamera.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Position, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Position, String> t) {
                            try {
                                Profile.PairCameraPosition p = ((Profile.PairCameraPosition) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())
                                );
                                if (t.getNewValue().compareTo("") != 0) {
                                    p.getCamera().setLable(t.getNewValue());
                                    cameraService.(p);
                                } else {
                                    //new EntityException("Vorname", "Vorname darf nicht leer sein.");
                                    refreshTablePosition(pservice.getAllPositionsOfProfile(((Profile)profilList.getSelectionModel().getSelectedItem())));
                                }

                            } catch (ServiceException e) {
                                try {
                                    refreshTableProfiles(pservice.getAllProfiles());
                                } catch (ServiceException e1) {
                                    LOGGER.debug("Error: could not refresh the profile table: "+e1.getMessage());
                                }

                            }

                        }
                    });*/
            colKamPosPosition.setStyle("-fx-alignment: CENTER;");
            colKamPosPosition.setSortable(false);
            colKamPosPosition.setCellValueFactory(
                    new Callback<TableColumn.CellDataFeatures<Profile.PairCameraPosition, Boolean>,
                            ObservableValue<Boolean>>() {

                        @Override
                        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Profile.PairCameraPosition, Boolean> p) {
                            return new SimpleBooleanProperty(p.getValue() != null);
                        }
                    });
            //Adding the checkbox to the cell
            colKamPosPosition.setCellFactory(
                    new Callback<TableColumn<Profile.PairCameraPosition, Boolean>, TableCell<Profile.PairCameraPosition, Boolean>>() {

                        @Override
                        public TableCell<Profile.PairCameraPosition, Boolean> call(TableColumn<Profile.PairCameraPosition, Boolean> p) {

                            return new CamPosComboBoxCell(kamPosList,pservice,posList);
                        }

                    });

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void logoUpload(){
        //TODO
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
        }
    }
    @FXML
    private void positionUpload(){
        fileChooser.setTitle("Bild für Position Hochladen...");
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
            txPositionBild.setText(file.getAbsolutePath());
        }
    }
    @FXML
    private void saveProfil(){
        LOGGER.debug("Profil Add Button has been clicked");
        String name = txProfilName.getText();
        if(name.trim().compareTo("") == 0){
            LOGGER.debug("in error");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Ungültige Eingabe");
            alert.setContentText("Sie müssen einen Namen eingeben!");
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(windowManager.getStage());
           alert.show();
        }else {
            Profile p = new Profile(name,txProfilDrucken.isSelected(),txProfilFilter.isSelected(),txProfilGreen.isSelected(),txProfilMobil.isSelected());

            if (txProfilWatermark.getText().compareTo("Hochladen...") != 0) // wenn ein watermark ausgewählt ist
                p.setWatermark(txProfilWatermark.getText());

            try {
                LOGGER.info("adding the new profil to tableView...");
                //TODO: Nachdem profildao methode  keine exception wirft, profList.add nach pservice.add ausführen.
                profList.add(p);
                pservice.add(p);

             //   profList.add(p);
            } catch (ServiceException e) {
               LOGGER.debug("Fehler: Profil konnte nicht erstellt werden..."+e.getMessage());
            }
        }
    }
    @FXML
    private void saveLogo(){
        //TODO
    }
    @FXML
    private void savePosition(){
        LOGGER.debug("Position Add Button has been clicked");
        String name = txPositionName.getText();
        if(name.trim().compareTo("") == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Ungültige Eingabe");
            alert.setContentText("Sie müssen einen Namen eingeben!");
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(windowManager.getStage());
            alert.show();
        }else {
            Position p = new Position(name);

            if (txPositionBild.getText().compareTo("Hochladen...") != 0) // wenn ein bild ausgewählt ist
                p.setButtonImagePath(txPositionBild.getText());

            try {
                LOGGER.info("adding the new position to tableView...");
                //TODO: Nachdem profildao methode  keine exception wirft, posList.add nach pservice.add ausführen.
                posList.add(p);
                pservice.addPosition(p);

                //   posList.add(p);
            } catch (ServiceException e) {
                LOGGER.debug("Fehler: Profil konnte nicht erstellt werden..."+e.getMessage());
            }
        }
    }



}
