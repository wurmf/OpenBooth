package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.BackgroundButtonCell;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.BackgroundImgCell;
import at.ac.tuwien.sepm.ws16.qse01.service.BackgroundService;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
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
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Created by macdnz on 13.01.17.
 */
@Component("greenscreen")
public class GreenscreenFrameController extends SettingFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GreenscreenFrameController.class);

    /* Profile TEXTFIELDS for ADDING */
    @FXML
    private TextField txBackgroundName;
    @FXML
    private TextField txBackgroundPath;



    /* BEGINN OF Category-Background Table Column FXML */
    @FXML
    private TableView tableBackground;
    @FXML
    private TableColumn colBackgroundID;
    @FXML
    private TableColumn colBackgroundName;
    @FXML
    private TableColumn colBackgroundPath;
    @FXML
    private TableColumn colBackgroundAction;

    @FXML
    private ComboBox greenscreenCategory;


    @Autowired
    public GreenscreenFrameController(ProfileService pservice, LogoWatermarkService logoService, BackgroundService bservice, WindowManager windowmanager) throws ServiceException {
        super(pservice, logoService, bservice, windowmanager);
    }

    @Override
    protected void initialize() {

            /* ######################### */
            /* INITIALIZING Greenscreen Background TABLE */
            /* ######################### */
        tableBackground.setEditable(true);
        colBackgroundID.setCellValueFactory(new PropertyValueFactory<Background, Integer>("id"));

        colBackgroundName.setCellValueFactory(new PropertyValueFactory<Background, String>("name"));
        colBackgroundName.setCellFactory(TextFieldTableCell.forTableColumn());
        colBackgroundName.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Background, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Background, String> t) {
                        try {
                            Background p = ((Background) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            );
                            if (t.getNewValue().compareTo("") != 0) {
                                p.setName(t.getNewValue());
                                bservice.edit(p);

                                // refreshTableBackground(bservice.get getAllBackgroundsOfCategoryAndProfile(selectedProfile.getId(),selectedCategory));
                            } else {
                                //TODO: change this line on refreshTableBackground!
                                refreshTablePosition(pservice.getAllPositionsOfProfile(selectedProfile));
                            }

                        } catch (ServiceException e) {
                            /*try {
                                refreshTableProfiles(pservice.getAllProfiles());
                            } catch (ServiceException e1) {
                                LOGGER.error("Error: could not refresh the profile table: ",e1);
                            }*/

                        }

                    }
                });
             /* Bild Column */
        colBackgroundPath.setStyle("-fx-alignment: CENTER;");
        colBackgroundPath.setSortable(false);
        colBackgroundPath.setCellValueFactory(new PropertyValueFactory<Background, String>("path"));
        colBackgroundPath.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {

                return new BackgroundImgCell(backgroundList,pservice);

            }
        });
            /* Aktion Column */
        colBackgroundAction.setStyle("-fx-alignment: CENTER;");
        colBackgroundAction.setSortable(false);
        colBackgroundAction.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Background, Boolean>,
                        ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Background, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });

        //Adding the X-Button to the cell
        colBackgroundAction.setCellFactory(
                new Callback<TableColumn<Background, Boolean>, TableCell<Background, Boolean>>() {

                    @Override
                    public TableCell<Background, Boolean> call(TableColumn<Background, Boolean> p) {
                        return new BackgroundButtonCell(backgroundList,pservice,windowManager.getStage());
                    }

                });
        //listener for greenscreen category combobox
        greenscreenCategory.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldCat, String newCat) {
                LOGGER.info("Kategorie ausgewählt -> "+newCat);
                //TODO: refreshTableBackground(pservice.getAllBa);


            }});


    }



    @Override
    protected void positionUpload() {

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

    @Override
    protected void watermarkUpload() {

    }

    @Override
    protected void saveProfil() {

    }

    @Override
    protected void backgroundUpload(){
        fileChooser.setTitle("Hintergrund Hochladen...");
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
            txBackgroundPath.setText(file.getAbsolutePath());
        }
    }

    protected void refreshTableBackground(List<Background> backgroundList){
        LOGGER.info("refreshing the background table..."+backgroundList.size());
        this.backgroundList.clear();
        this.backgroundList.addAll(backgroundList);
        tableBackground.setItems(this.backgroundList);
    }

}
