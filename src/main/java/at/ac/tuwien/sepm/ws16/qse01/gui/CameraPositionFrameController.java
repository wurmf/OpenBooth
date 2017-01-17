package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.CamPosComboBoxCell;
import at.ac.tuwien.sepm.ws16.qse01.service.BackgroundService;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @FXML
    private TableView tableKamPos;
    @FXML
    private TableColumn colKamPosKamera;
    @FXML
    private TableColumn colKamPosPosition;


    @Autowired
    public CameraPositionFrameController(ProfileService pservice, LogoWatermarkService logoService, BackgroundService bservice, WindowManager windowmanager, ImageHandler imageHandler) throws ServiceException {
        super(pservice, logoService, bservice, windowmanager,imageHandler);
    }

    @FXML
    private void initialize() {

        tableKamPos.setEditable(true);
        colKamPosKamera.setCellValueFactory(new PropertyValueFactory<Profile.PairCameraPosition, String>("cameraLable"));

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
    }

    protected void refreshTableKameraPosition(List<Profile.PairCameraPosition> camposList,ObservableList<Position> posList,Profile selected){
        LOGGER.debug("refreshing the KameraPosition-Zuweisung table...");
        selectedProfile = selected;
        this.posList.clear();
        this.posList.addAll(posList);
        this.kamPosList.removeAll(kamPosList);
        this.kamPosList.addAll(camposList);
        tableKamPos.setItems(this.kamPosList);
    }
}
