package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.CategoryButtonCell;
import at.ac.tuwien.sepm.ws16.qse01.service.BackgroundService;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by macdnz on 13.01.17.
 */
@Component("greenscreenCategory")
public class GreenscreenCategoryFrameController extends SettingFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GreenscreenCategoryFrameController.class);

    /* Profile TEXTFIELDS for ADDING */
    @FXML
    private TextField txCategoryName;



    /* BEGINN OF Category-Background Table Column FXML */
    @FXML
    private TableView tableCategory;
    @FXML
    private TableColumn colCategoryID;
    @FXML
    private TableColumn colCategoryName;
    @FXML
    private TableColumn colCategoryAction;


    @Autowired
    public GreenscreenCategoryFrameController(ProfileService pservice, LogoWatermarkService logoService, BackgroundService bservice, WindowManager windowmanager) throws ServiceException {
        super(pservice, logoService, bservice, windowmanager);
    }

    @FXML
    private void initialize() {
        tableCategory.setEditable(true);
        colCategoryID.setCellValueFactory(new PropertyValueFactory<Background.Category, Integer>("id"));

        colCategoryName.setCellValueFactory(new PropertyValueFactory<Background.Category, String>("name"));
        colCategoryName.setCellFactory(TextFieldTableCell.forTableColumn());
        colCategoryName.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Background.Category, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Background.Category, String> t) {
                        try {
                            Background.Category p = ((Background.Category) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            );
                            if (t.getNewValue().compareTo("") != 0) {
                                p.setName(t.getNewValue());
                                bservice.editCategory(p);

                                 refreshCategoryComboBox(pservice.getAllCategoryOfProfile(selectedProfile.getId()));
                            } else {
                                refreshTableCategory(pservice.getAllCategoryOfProfile(selectedProfile.getId()));
                            }

                        } catch (ServiceException e) {

                            LOGGER.error("Error: could not refresh the profile table: ",e);


                        }

                    }
                });

            /* Aktion Column */
        colCategoryAction.setStyle("-fx-alignment: CENTER;");
        colCategoryAction.setSortable(false);
        colCategoryAction.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Background.Category, Boolean>,
                        ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Background.Category, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });

        //Adding the X-Button to the cell
        colCategoryAction.setCellFactory(
                new Callback<TableColumn<Background.Category, Boolean>, TableCell<Background.Category, Boolean>>() {

                    @Override
                    public TableCell<Background.Category, Boolean> call(TableColumn<Background.Category, Boolean> p) {
                        return new CategoryButtonCell(categoryList,bservice,windowManager.getStage());
                    }

                });



    }




    @FXML
    private void saveCategory(){
        LOGGER.error("Category Add Button has been clicked");
        String name = txCategoryName.getText();
        if(name.trim().compareTo("") == 0){
            showError("Sie müssen einen Namen eingeben!");
        }else {
            Background.Category p = new Background.Category(name);


            try {
                LOGGER.info("adding the new Category to tableView...");

                bservice.addCategory(p);
                categoryList.add(p);
                refreshCategoryComboBox(categoryList);
                txCategoryName.clear();


            } catch (ServiceException e) {
                LOGGER.error("Fehler: Kategorie konnte nicht erstellt werden..."+e.getMessage());
            }
        }
    }

    protected void refreshTableCategory(List<Background.Category> categoryList,Profile selected){
        LOGGER.info("refreshing the category table..."+categoryList.size());
        selectedProfile = selected;
        this.categoryList.clear();
        this.categoryList.addAll(categoryList);
        tableCategory.setItems(this.categoryList);
    }

}
