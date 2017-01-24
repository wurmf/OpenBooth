package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.CamPosCheckbox;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.CamPosComboBoxCell;
import at.ac.tuwien.sepm.ws16.qse01.service.BackgroundService;
import at.ac.tuwien.sepm.ws16.qse01.service.CameraService;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by macdnz on 14.01.17.
 */
@Component("cameraposition")
public class CameraPositionFrameController extends SettingFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraPositionFrameController.class);
    public final ObservableList<Position> posList = FXCollections.observableArrayList();
    private CameraService cameraService;
    @FXML
    private TableView tableKamPos;
    @FXML
    private TableColumn colKamPosKamera;
    @FXML
    private TableColumn colKamPosPosition;
    @FXML
    private TableColumn colKamPosActivated;
    @FXML
    private TableColumn colKamPosGreenscreen;

    @Autowired
    public CameraPositionFrameController(CameraService cameraService,ProfileService pservice, LogoWatermarkService logoService, BackgroundService bservice, WindowManager windowmanager, ImageHandler imageHandler) throws ServiceException {
        super(pservice, logoService, bservice, windowmanager,imageHandler);
        this.cameraService = cameraService;
    }

    @FXML
    private void initialize() {

        tableKamPos.setEditable(true);
        colKamPosKamera.setCellValueFactory(new PropertyValueFactory<Profile.PairCameraPosition, String>("cameraLable"));
        colKamPosKamera.setCellFactory(TextFieldTableCell.forTableColumn());
        colKamPosKamera.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Profile.PairCameraPosition, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Profile.PairCameraPosition, String> t) {
                        try {
                            Profile.PairCameraPosition p = ((Profile.PairCameraPosition) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            );
                            if (t.getNewValue().compareTo("") != 0) {
                                p.getCamera().setLable(t.getNewValue());
                                cameraService.editCamera(p.getCamera());


                                refreshTableKameraPosition(pservice.getAllPairCamerasWithPositionByProfile(selectedProfile.get(0).getId()),posList, selectedProfile);
                            } else {
                                refreshTablePosition(pservice.getAllPositionsOfProfile(pservice.get(selectedProfile.get(0).getId())));
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
        colKamPosActivated.setStyle("-fx-alignment: CENTER;");
        colKamPosActivated.setSortable(false);
        colKamPosActivated.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Profile.PairCameraPosition, Boolean>,
                        ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Profile.PairCameraPosition, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });
        //Adding the checkbox to the cell
        colKamPosActivated.setCellFactory(
                new Callback<TableColumn<Profile.PairCameraPosition, Boolean>, TableCell<Profile.PairCameraPosition, Boolean>>() {

                    @Override
                    public TableCell<Profile.PairCameraPosition,Boolean> call(TableColumn<Profile.PairCameraPosition,Boolean> p) {
                        return new CamPosCheckbox(kamPosList,pservice,kamList, selectedProfile,"activated",windowManager.getStage());
                    }

                });

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
                        return new CamPosComboBoxCell(colKamPosPosition,kamPosList,pservice,posList, selectedProfile,windowManager.getStage());
                    }

                });

        colKamPosGreenscreen.setStyle("-fx-alignment: CENTER;");
        colKamPosGreenscreen.setSortable(false);
        colKamPosGreenscreen.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Profile.PairCameraPosition, Boolean>,
                        ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Profile.PairCameraPosition, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });
        //Adding the checkbox to the cell
        colKamPosGreenscreen.setCellFactory(
                new Callback<TableColumn<Profile.PairCameraPosition, Boolean>, TableCell<Profile.PairCameraPosition, Boolean>>() {

                    @Override
                    public TableCell<Profile.PairCameraPosition,Boolean> call(TableColumn<Profile.PairCameraPosition,Boolean> p) {
                        return new CamPosCheckbox(kamPosList,pservice,kamList, selectedProfile,"greenscreen",windowManager.getStage());
                    }

                });
    }

    public void refreshTableKameraPosition(List<Profile.PairCameraPosition> camposList, ObservableList<Position> posList, ObservableList<Profile> selectedID){
        LOGGER.debug("refreshing the KameraPosition-Zuweisung table..."+posList.size()+posList.toString());
        selectedProfile = selectedID;
        this.posList.clear();
        this.posList.addAll(posList);
        this.kamPosList.clear();
        this.kamPosList.addAll(camposList);
        tableKamPos.setItems(this.kamPosList);
    }
}
