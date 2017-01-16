package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.PositionButtonCell;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.PositionImgCell;
import at.ac.tuwien.sepm.ws16.qse01.service.BackgroundService;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Created by osboxes on 11.12.16.
 */
@Component("position")
public class PositionFrameController extends SettingFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PositionFrameController.class);


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

    @Autowired
    public PositionFrameController(ProfileService pservice, LogoWatermarkService logoService, BackgroundService bservice, WindowManager windowmanager,ImageHandler imageHandler) throws ServiceException {
        super(pservice, logoService, bservice, windowmanager,imageHandler);
    }


    @FXML
    private void initialize(){


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


                                refreshTableKameraPosition(pservice.getAllPairCameraPositionOfProfile(selectedProfile.getId()));
                            } else {
                                refreshTablePosition(pservice.getAllPositionsOfProfile(selectedProfile));
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
             /* Bild Column */
        colPositionBild.setStyle("-fx-alignment: CENTER;");
        colPositionBild.setSortable(false);
        colPositionBild.setCellValueFactory(new PropertyValueFactory<Position, String>("buttonImagePath"));
        colPositionBild.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {

                return new PositionImgCell(posList,pservice,imageHandler,windowManager.getStage());

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
                        return new PositionButtonCell(posList,kamPosList,selectedProfile.getId(),pservice,windowManager.getStage());
                    }

                });

    }


    @FXML
    private void savePosition(){
        LOGGER.error("Position Add Button has been clicked");
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

                kamPosList.clear();
                kamPosList.addAll(pservice.getAllPairCameraPositionOfProfile(selectedProfile.getId()));

                txPositionBild.setText("Hochladen...");
                txPositionName.clear();


            } catch (ServiceException e) {
                LOGGER.error("Fehler: Profil konnte nicht erstellt werden..."+e.getMessage());
            }
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

    protected void refreshTablePosition(List<Position> positionList,Profile selected){
        LOGGER.info("refreshing the position table...");
        selectedProfile = selected;
        posList.clear();
        posList.addAll(positionList);
        tablePosition.setItems(posList);

    }

    protected ObservableList<Position> getPosList(){
        return this.posList;
    }


}
