package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.entities.Logo;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.entities.RelativeRectangle;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.*;
import at.ac.tuwien.sepm.ws16.qse01.service.CameraService;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
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
    private LogoWatermarkService logoService;
    @Resource
    private CameraService cameraService;

    private final ObservableList<Profile> profList = FXCollections.observableArrayList();

    private final ObservableList<Position> posList = FXCollections.observableArrayList();

    private final ObservableList<Profile.PairCameraPosition> kamPosList = FXCollections.observableArrayList();
    private final ObservableList<Profile.PairLogoRelativeRectangle> logoList = FXCollections.observableArrayList();

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
    private TableColumn colKamPosKamera;
    @FXML
    private TableColumn colKamPosPosition;

    /* Beginn of Logo Table Column FXML */
    @FXML
    private TableView tableLogo;
    @FXML
    private TableColumn colLogoID;
    @FXML
    private TableColumn colLogoName;
    @FXML
    private TableColumn colLogoX;
    @FXML
    private TableColumn colLogoY;
    @FXML
    private TableColumn colLogoBreite;
    @FXML
    private TableColumn colLogoHoehe;
    @FXML
    private TableColumn colLogoLogo;
    @FXML
    private TableColumn colLogoAktion;

    /* LOGO TextFIELDS */
    @FXML
    private TextField txLogoName;
    @FXML
    private TextField txLogoX;
    @FXML
    private TextField txLogoY;
    @FXML
    private TextField txLogoBreite;
    @FXML
    private TextField txLogoHoehe;
    @FXML
    private TextField txLogoLogo;

    /* Vorschau FXML */
    @FXML
    private TextField txPreviewWidth;
    @FXML
    private TextField txPreviewHeight;
    @FXML
    private ImageView previewLogo;

    private Profile.PairLogoRelativeRectangle selectedLogo = null;

    @Autowired
    public SettingFrameController(SpringFXMLLoader springFXMLLoader, ProfileService pservice,LogoWatermarkService logoService,CameraService cameraService,WindowManager windowmanager) throws ServiceException {
        this.springFXMLLoader = springFXMLLoader;
        this.pservice = pservice;
        this.cameraService = cameraService;
        this.windowManager = windowmanager;
        this.logoService = logoService;
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
                        refreshTablePosition(pservice.getAllPositions());
                        int selectedProfilID = ((Profile)profilList.getSelectionModel().getSelectedItem())==null?0:((Profile)profilList.getSelectionModel().getSelectedItem()).getId();
                        refreshTableKameraPosition(pservice.getAllPairCameraPositionOfProfile(selectedProfil.getId()));

                        /* kamPosList.removeAll(kamPosList);
                        kamPosList.addAll(pservice.getAllPairCameraPositionOfProfile(selectedProfilID));
                        tableKamPos.setItems(kamPosList);*/
                        refreshTableLogo(pservice.getAllPairLogoRelativeRectangle(selectedProfilID));
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

                                    int selectedProfilID = ((Profile)profilList.getSelectionModel().getSelectedItem())==null?0:((Profile)profilList.getSelectionModel().getSelectedItem()).getId();
                                    kamPosList.removeAll(kamPosList);
                                    kamPosList.addAll(pservice.getAllPairCameraPositionOfProfile(selectedProfilID));
                                   // refreshTableKameraPosition(pservice.getAllPairCameraPositionOfProfile(selectedProfilID));
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
                            int selectedProfilID = ((Profile)profilList.getSelectionModel().getSelectedItem())==null?0:((Profile)profilList.getSelectionModel().getSelectedItem()).getId();
                            return new PositionButtonCell(posList,kamPosList,selectedProfilID,pservice,windowManager.getStage());
                        }

                    });

           // this.refreshTablePosition(pservice.getAllPositionsOfProfile((Profile)profilList.getItems().get(0)));

             /* ######################### */
            /* INITIALIZING KameraPosition Zuweisung TABLE */
            /* ######################### */
            tableKamPos.setEditable(true);
            colKamPosKamera.setCellValueFactory(new PropertyValueFactory<Profile.PairCameraPosition, String>("cameraLable"));
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
            //Adding the combobox to the cell
            colKamPosPosition.setCellFactory(
                    new Callback<TableColumn<Profile.PairCameraPosition, Boolean>, TableCell<Profile.PairCameraPosition, Boolean>>() {

                        @Override
                        public TableCell<Profile.PairCameraPosition, Boolean> call(TableColumn<Profile.PairCameraPosition, Boolean> p) {

                            return new CamPosComboBoxCell(kamPosList,pservice,posList);
                        }

                    });

              /* ######################### */
            /* INITIALIZING Logo TABLE */
            /* ######################### */
            tableLogo.setEditable(true);

            colLogoID.setCellValueFactory(new PropertyValueFactory<Profile.PairLogoRelativeRectangle, Integer>("id"));

            colLogoName.setCellValueFactory(new PropertyValueFactory<Profile.PairLogoRelativeRectangle, Logo>("logo"));
            colLogoName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, String> p) {
                    return new ReadOnlyObjectWrapper(p.getValue().getLogo().getLabel());
                }
            });
            colLogoName.setCellFactory(TextFieldTableCell.forTableColumn());
            colLogoName.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, String> t) {
                            try {
                                Profile.PairLogoRelativeRectangle p = ((Profile.PairLogoRelativeRectangle) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())
                                );
                                if (t.getNewValue().compareTo("") != 0) {

                                    p.getLogo().setLabel(t.getNewValue());
                                    System.out.println(p.getLogo().getId()+"_"+p.getLogo().getLabel()+"_"+p.getLogo().getPath());
                                   pservice.editPairLogoRelativeRectangle(p);
                                } else {
                                    refreshTableLogo(pservice.getAllPairLogoRelativeRectangle(((Profile)profilList.getSelectionModel().getSelectedItem()).getId()));
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
            /* Logo Xstart Column */
           colLogoX.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double>, ObservableValue<Double>>() {
                public ObservableValue<Double> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double> p) {
                    return new ReadOnlyObjectWrapper(p.getValue().getRelativeRectangle().getX());
                }
            });
            colLogoX.setCellFactory(TextFieldTableCell.forTableColumn(new Double2String("xstart")));
            colLogoX.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double> t) {
                            try {
                                Profile.PairLogoRelativeRectangle p = ((Profile.PairLogoRelativeRectangle) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())
                                );
                                if (!t.getNewValue().isNaN()) {

                                     p.getRelativeRectangle().setX(t.getNewValue());
                                    pservice.editPairLogoRelativeRectangle(p);

                                } else {
                                    refreshTableLogo(pservice.getAllPairLogoRelativeRectangle(((Profile)profilList.getSelectionModel().getSelectedItem()).getId()));
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

              /* Logo Ystart Column */
            colLogoY.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double>, ObservableValue<Double>>() {
                public ObservableValue<Double> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double> p) {
                    return new ReadOnlyObjectWrapper(p.getValue().getRelativeRectangle().getY());
                }
            });
            colLogoY.setCellFactory(TextFieldTableCell.forTableColumn(new Double2String("ystart")));
            colLogoY.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double> t) {
                            try {
                                Profile.PairLogoRelativeRectangle p = ((Profile.PairLogoRelativeRectangle) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())
                                );
                                if (!t.getNewValue().isNaN()) {

                                     p.getRelativeRectangle().setY(t.getNewValue());
                                     pservice.editPairLogoRelativeRectangle(p);
                                } else {
                                    refreshTableLogo(pservice.getAllPairLogoRelativeRectangle(((Profile)profilList.getSelectionModel().getSelectedItem()).getId()));
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

              /* Logo Xstart Column */
            colLogoBreite.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double>, ObservableValue<Double>>() {
                public ObservableValue<Double> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double> p) {
                    return new ReadOnlyObjectWrapper(p.getValue().getRelativeRectangle().getWidth());
                }
            });
            colLogoBreite.setCellFactory(TextFieldTableCell.forTableColumn(new Double2String("width")));
            colLogoBreite.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double> t) {
                            try {
                                Profile.PairLogoRelativeRectangle p = ((Profile.PairLogoRelativeRectangle) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())
                                );
                                if (!t.getNewValue().isNaN()) {
                                    p.getRelativeRectangle().setWidth(t.getNewValue());
                                    pservice.editPairLogoRelativeRectangle(p);
                                } else {
                                    refreshTableLogo(pservice.getAllPairLogoRelativeRectangle(((Profile)profilList.getSelectionModel().getSelectedItem()).getId()));
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
              /* Logo Xstart Column */
            colLogoHoehe.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double>, ObservableValue<Double>>() {
                public ObservableValue<Double> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double> p) {
                    return new ReadOnlyObjectWrapper(p.getValue().getRelativeRectangle().getHeight());
                }
            });
            colLogoHoehe.setCellFactory(TextFieldTableCell.forTableColumn(new Double2String("height")));
            colLogoHoehe.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double> t) {
                            try {
                                Profile.PairLogoRelativeRectangle p = ((Profile.PairLogoRelativeRectangle) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())
                                );
                                if (!t.getNewValue().isNaN()) {
                                    p.getRelativeRectangle().setHeight(t.getNewValue());
                                    pservice.editPairLogoRelativeRectangle(p);
                                } else {
                                    refreshTableLogo(pservice.getAllPairLogoRelativeRectangle(((Profile)profilList.getSelectionModel().getSelectedItem()).getId()));
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
             /* Logo Column */
            colLogoLogo.setStyle("-fx-alignment: CENTER;");
            colLogoLogo.setSortable(false);
            colLogoLogo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, String> p) {
                    return new ReadOnlyObjectWrapper(p.getValue().getLogo().getPath());
                }
            });
            colLogoLogo.setCellFactory(new Callback<TableColumn, TableCell>() {
                @Override
                public TableCell call(TableColumn p) {

                    return new LogoImgCell(logoList,pservice);

                }
            });
            /* Aktion Column */
            colLogoAktion.setStyle("-fx-alignment: CENTER;");
            colLogoAktion.setSortable(false);
            colLogoAktion.setCellValueFactory(
                    new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Boolean>,
                            ObservableValue<Boolean>>() {

                        @Override
                        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Boolean> p) {
                            return new SimpleBooleanProperty(p.getValue() != null);
                        }
                    });

            //Adding the X-Button to the cell
            colLogoAktion.setCellFactory(
                    new Callback<TableColumn<Profile.PairLogoRelativeRectangle, Boolean>, TableCell<Profile.PairLogoRelativeRectangle, Boolean>>() {

                        @Override
                        public TableCell<Profile.PairLogoRelativeRectangle, Boolean> call(TableColumn<Profile.PairLogoRelativeRectangle, Boolean> p) {

                            return new LogoButtonCell(logoList,pservice,windowManager.getStage());
                        }

                    });

            /* ###################
             *   Vorschau Teil
             *####################*/
            tableLogo.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    Profile.PairLogoRelativeRectangle selectedLogo = (Profile.PairLogoRelativeRectangle) newSelection;
                    LOGGER.info("Logo row selected..."+selectedLogo.getLogo().getPath());

                    this.selectedLogo = selectedLogo;
                    if (!selectedLogo.getLogo().getPath().isEmpty() && !selectedLogo.getLogo().getPath().equals("/images/noimage.png") && new File(selectedLogo.getLogo().getPath()).isFile()) {
                        try {
                            int width = Integer.parseInt(txPreviewWidth.getText());
                            int height = Integer.parseInt(txPreviewHeight.getText());
                            javafx.scene.image.Image image = SwingFXUtils.toFXImage(logoService.getPreviewForLogo(selectedLogo.getLogo(), selectedLogo.getRelativeRectangle(), width, height), null);
                            previewLogo.setImage(image);
                        } catch (NumberFormatException e) {
                            LOGGER.error("Fehler: Bitte geben Sie eine Zahl an");
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }
                    }else
                        LOGGER.info("No Logo is uploaded...");
                }
            });

            txPreviewHeight.textProperty().addListener((observable, oldValue, newValue) -> {
                if(selectedLogo!=null){
                    try {
                        int height = Integer.parseInt(newValue);
                        int width = Integer.parseInt(txPreviewWidth.getText());
                        javafx.scene.image.Image image = SwingFXUtils.toFXImage(logoService.getPreviewForLogo(selectedLogo.getLogo(), selectedLogo.getRelativeRectangle(), width, height), null);
                        previewLogo.setImage(image);
                    } catch (NumberFormatException e) {
                        LOGGER.error("Fehler: Bitte geben Sie eine Zahl an");
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }
            });
            txPreviewWidth.textProperty().addListener((observable, oldValue, newValue) -> {
                if(selectedLogo!=null){
                    try {
                        int width = Integer.parseInt(newValue);
                        int height = Integer.parseInt(txPreviewHeight.getText());
                        javafx.scene.image.Image image = SwingFXUtils.toFXImage(logoService.getPreviewForLogo(selectedLogo.getLogo(), selectedLogo.getRelativeRectangle(), width, height), null);
                        previewLogo.setImage(image);
                    } catch (NumberFormatException e) {
                        LOGGER.error("Fehler: Bitte geben Sie eine Zahl an");
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (ServiceException e) {
            e.printStackTrace();
        }
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

    private void refreshTableKameraPosition(List<Profile.PairCameraPosition> camposList){
        LOGGER.info("refreshing the KameraPosition-Zuweisung table...");
        this.kamPosList.removeAll(kamPosList);
      /*  for(Profile.PairCameraPosition cp: camposList){
            System.out.println("Kamera->"+cp.getCamera()+"_pos->"+cp.getPosition().getName());
        }*/
       /* this.kamPosList.add(new Profile.PairCameraPosition(new Camera(1,"camera1","22","asdf","asdf"),new Position("Oben"),true));
        this.kamPosList.add(new Profile.PairCameraPosition(new Camera(2,"camera2","22","asdf","asdf"),new Position("Unten"),true));
       pservice.get*/
        /* for(Camera cam: camList){
            LOGGER.debug("kamera => "+cam.getLable());
            try {
                this.kamPosList.add(new Profile.PairCameraPosition(cam,pservice.getPositionOfCameraOfProfile(cam),true));
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }*/
        this.kamPosList.addAll(camposList);
        tableKamPos.setItems(this.kamPosList);
    }
    private void refreshTableLogo(List<Profile.PairLogoRelativeRectangle> logoList){
        LOGGER.info("refreshing the Logo table...");
        this.logoList.clear();
       // this.logoList.add(new Profile.PairLogoRelativeRectangle(new Logo("logo1","/images/noimage.png"),new RelativeRectangle(100,200,200,200)));
       // this.logoList.add(new Profile.PairLogoRelativeRectangle(new Logo("Logo2","/images/noimage.png"),new RelativeRectangle(100,200,200,200)));
       /* for(Camera cam: camList){
            LOGGER.debug("kamera => "+cam.getLable());
            try {
                this.kamPosList.add(new Profile.PairCameraPosition(cam,pservice.getPositionOfCameraOfProfile(cam),true));
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }*/
        this.logoList.addAll(logoList);
        tableLogo.setItems(this.logoList);
    }

    @FXML
    private void logoUpload(){
        fileChooser.setTitle("Logo Hochladen...");
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
            txLogoLogo.setText(file.getAbsolutePath());
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
            showError("Sie müssen einen Namen eingeben!");
        }else {

            Profile p = new Profile(name,txProfilDrucken.isSelected(),txProfilFilter.isSelected(),txProfilGreen.isSelected(),txProfilMobil.isSelected(),txProfilWatermark.getText());

            if (txProfilWatermark.getText().compareTo("Hochladen...") != 0) // wenn ein watermark ausgewählt ist
                p.setWatermark(txProfilWatermark.getText());

            try {
                LOGGER.info("adding the new profil to tableView...");


                pservice.add(p);
                profList.add(p);

             //   profList.add(p);
            } catch (ServiceException e) {
               LOGGER.debug("Fehler: Profil konnte nicht erstellt werden..."+e.getMessage());
            }
        }
    }
    @FXML
    private void saveLogo(){
        LOGGER.debug("Logo Add Button has been clicked");
        String name = txLogoName.getText();
        if(name.trim().compareTo("") == 0 || txLogoLogo.getText().compareTo("Hochladen...") == 0){
            LOGGER.debug("in error");
            showError("Sie müssen einen Namen eingeben und ein Logo hochladen!");
        }else {
            try {
                Logo newLogo = new Logo(name,txLogoLogo.getText());
                RelativeRectangle newPosition = new RelativeRectangle(Double.valueOf(txLogoX.getText()),Double.valueOf(txLogoY.getText()), Double.valueOf(txLogoHoehe.getText()),Double.valueOf(txLogoBreite.getText()));

                LOGGER.info("adding the new pairLogo to tableView...");

                newLogo = pservice.addLogo(newLogo);

                Profile.PairLogoRelativeRectangle p = new Profile.PairLogoRelativeRectangle(newLogo,newPosition);

                logoList.add(p);

                int selectedProfilID = ((Profile)profilList.getSelectionModel().getSelectedItem())==null?0:((Profile)profilList.getSelectionModel().getSelectedItem()).getId();

                pservice.addPairLogoRelativeRectangle(selectedProfilID,newLogo.getId(),newPosition);


            } catch (ServiceException e) {
                LOGGER.debug("Fehler: Profil konnte nicht erstellt werden..."+e.getMessage());
            } catch (NumberFormatException e){
                showError("Bitte in Position Eingabefelder (Xstart,>Ystart,Breite,Höhe) nur Zahlen eingeben.");
                LOGGER.error("Fehler: Bitte nur Zahlen eingeben. "+e.getMessage());
                //TODO: Dialogfenster öffnen.
            }
        }
    }

    @FXML
    private void savePosition(){
        LOGGER.debug("Position Add Button has been clicked");
        String name = txPositionName.getText();
        if(name.trim().compareTo("") == 0){
            showError("Sie müssen einen Namen eingeben!");
        }else {
            Position p = new Position(name);

            if (txPositionBild.getText().compareTo("Hochladen...") != 0) // wenn ein bild ausgewählt ist
                p.setButtonImagePath(txPositionBild.getText());

            try {
                LOGGER.info("adding the new position to tableView...");

                pservice.addPosition(p);
                posList.add(p);

                int selectedProfilID = ((Profile)profilList.getSelectionModel().getSelectedItem())==null?0:((Profile)profilList.getSelectionModel().getSelectedItem()).getId();
                kamPosList.clear();
                kamPosList.addAll(pservice.getAllPairCameraPositionOfProfile(selectedProfilID));

            } catch (ServiceException e) {
                LOGGER.debug("Fehler: Profil konnte nicht erstellt werden..."+e.getMessage());
            }
        }
    }

    @FXML
    public void openMainFrame(){
        LOGGER.info("backButton clicked...");
        windowManager.showShootingAdministration();
    }
    public void showError(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Ungültige Eingabe");
        alert.setContentText(msg);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(windowManager.getStage());
        alert.show();
    }

    @FXML
    public void fullScreenPreview(){
       // try {
            Stage previewStage = new Stage();
            Group root = new Group();
            Scene scene = new Scene(root);

          /*  HBox box = new HBox();
            box.getChildren().add(iv1);
            box.getChildren().add(iv2);
            box.getChildren().add(iv3);*/
            ImageView prevView = new ImageView(previewLogo.getImage());
            prevView.setFitHeight(prevView.getImage().getHeight());
            prevView.setFitWidth(prevView.getImage().getWidth());
            root.getChildren().add(prevView);

            previewStage.setTitle("Preview Logo");
            previewStage.setWidth(prevView.getImage().getWidth());
            previewStage.setHeight(prevView.getImage().getHeight());
            previewStage.setScene(scene);
        previewStage.setFullScreen(false);
        previewStage.initOwner(windowManager.getStage());
            previewStage.show();
            //Desktop.getDesktop().open(new File(System.getProperty("user.dir")+"/preview.jpg"));
      /*  } catch (IOException e) {
            LOGGER.error("fullscreenPreview ->"+e.getMessage());
        }*/
    }

}
