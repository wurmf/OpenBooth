package org.openbooth.gui;

import org.openbooth.util.ImageHandler;
import org.openbooth.entities.Background;
import org.openbooth.entities.Profile;
import org.openbooth.gui.specialCells.BackgroundButtonCell;
import org.openbooth.gui.specialCells.BackgroundImgCell;
import org.openbooth.service.BackgroundService;
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
@Component("greenscreenBackground")
public class GreenscreenBackgroundFrameController extends SettingFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GreenscreenBackgroundFrameController.class);



    /* Profile TEXTFIELDS for ADDING */
    @FXML
    private TextField txBackgroundName;
    @FXML
    private TextField txBackgroundPath;
    @FXML
    private Button txBackgroundUpload;
    @FXML
    private Button txBackgroundAdd;



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

    //private Background.Category selectedCategory;
   /* @FXML
    private ComboBox categoryCombo;
*/

    @Autowired
    public GreenscreenBackgroundFrameController(ProfileService pservice, LogoWatermarkService logoService, BackgroundService bservice, WindowManager windowmanager, ImageHandler imageHandler) throws ServiceException {
        super(pservice, logoService, bservice, windowmanager,imageHandler);
    }

    @FXML
    private void initialize() {

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

                                refreshTableBackground(pservice.getAllBackgroundOfProfile(selectedProfile.get(0).getId()));
                            } else {
                                refreshTableBackground(pservice.getAllBackgroundOfProfile(selectedProfile.get(0).getId()));
                            }

                        } catch (ServiceException e) {
                            try {
                                refreshTableBackground(pservice.getAllBackgroundOfProfile(selectedProfile.get(0).getId()));
                            } catch (ServiceException e1) {
                                LOGGER.error("Error: could not refresh the profile table: ",e1);
                            }

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

                return new BackgroundImgCell(backgroundList,bservice,imageHandler,windowManager.getStage());

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
                        return new BackgroundButtonCell(imageHandler,backgroundList,bservice,windowManager.getStage());
                    }

                });


        txBackgroundUpload.setBackground(GUIImageHelper.getButtonBackground(imageHandler,"/images/upload1.png",50,50));
        txBackgroundUpload.setPrefHeight(50);
        txBackgroundUpload.setPrefWidth(50);

        txBackgroundAdd.setBackground(GUIImageHelper.getButtonBackground(imageHandler,"/images/add.png",50,50));
        txBackgroundAdd.setPrefHeight(50);
        txBackgroundAdd.setPrefWidth(50);

        txBackgroundName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && selectedCategory!=null && selectedProfile !=null && txBackgroundPath.getText().compareTo("Hochladen...")!=0 ){
                txBackgroundAdd.setBackground(GUIImageHelper.getButtonBackground(imageHandler,"/images/add.png",50,50));
            }else
                txBackgroundAdd.setBackground(GUIImageHelper.getButtonBackground(imageHandler,"/images/add.png",50,50));

        });


    }




    @FXML
    private void backgroundUpload(){
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
            txBackgroundUpload.setBackground(GUIImageHelper.getButtonBackground(imageHandler,"/images/upload1.png",50,50));

            if(!txBackgroundName.getText().isEmpty() && selectedCategory!=null && selectedProfile !=null && txBackgroundPath.getText().compareTo("Hochladen...")!=0 ){
                txBackgroundAdd.setBackground(GUIImageHelper.getButtonBackground(imageHandler,"/images/add.png",50,50));
            }else
                txBackgroundAdd.setBackground(GUIImageHelper.getButtonBackground(imageHandler,"/images/add.png",50,50));
        }
    }
    @FXML
    private void saveBackground(){
        LOGGER.error("Position Add Button has been clicked");
        String name = txBackgroundName.getText();
        if(selectedCategory==null || name.trim().compareTo("") == 0 || txBackgroundPath.getText().compareTo("Hochladen...") == 0){
            showError("Sie müssen einen Namen eingeben, ein Hintergrundbild hochladen und eine Kategorie auswählen!");
        }else {
            Background p = new Background(name,txBackgroundPath.getText(),selectedCategory);


            try {
                LOGGER.debug("adding the new background to tableView...");

                bservice.add(p);
                backgroundList.add(p);


                txBackgroundPath.setText("Hochladen...");
                txBackgroundName.clear();
                txBackgroundUpload.setBackground(GUIImageHelper.getButtonBackground(imageHandler,"/images/upload1.png",50,50));


            } catch (ServiceException e) {
                LOGGER.error("Fehler: Hintergrund konnte nicht erstellt werden...",e);
            }
        }
    }

    public void refreshTableBackground(List<Background> backgroundList, ObservableList<Profile> selProfile, Background.Category selCategory){
        LOGGER.debug("refreshing the background table..."+backgroundList.size());
        selectedProfile = selProfile;
        selectedCategory = selCategory;
        this.backgroundList.clear();
        this.backgroundList.addAll(backgroundList);
        tableBackground.setItems(this.backgroundList);

        if(!txBackgroundName.getText().isEmpty() && selectedCategory!=null && selectedProfile !=null && txBackgroundPath.getText().compareTo("Hochladen...")!=0 ){
            txBackgroundAdd.setBackground(GUIImageHelper.getButtonBackground(imageHandler,"/images/add.png",50,50));
        }else
            txBackgroundAdd.setBackground(GUIImageHelper.getButtonBackground(imageHandler,"/images/add.png",50,50));

    }

    protected void refreshCategoryComboBox(List<Background.Category> categories,ObservableList<Profile> selected){
        LOGGER.debug("refreshing the categoryComboBox..."+categories.size());
        selectedProfile = selected;
        categoryList.clear();
        categoryList.addAll(categories);
       /* categoryCombo.setItems(categoryList);
        if(selectedCategory!=null)
            categoryCombo.getSelectionModel().select(selectedCategory);*/
    }
}
