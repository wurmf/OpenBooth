package at.ac.tuwien.sepm.ws16.qse01.gui.specialCells;

import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.BackgroundService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by macdnz on 16.12.16.
 */
public class CategoryButtonCell extends TableCell<Background.Category, Boolean> {
    final static Logger LOGGER = LoggerFactory.getLogger(CategoryButtonCell.class);

    private  ObservableList<Background.Category> categories;
    private ObservableList<Background.Category> categoryListOfProfile;
    private Profile selectedProfile;
    private BackgroundService bservice;

    private final Button cellButton = new Button("X");

    public CategoryButtonCell(ObservableList<Background.Category> categoryListOfProfile, Profile selectedProfile, ObservableList<Background.Category> categories, BackgroundService bservice, Stage primaryStage) {
        this.categories = categories;
        this.categoryListOfProfile = categoryListOfProfile;
        this.selectedProfile = selectedProfile;
        this.bservice = bservice;

        cellButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                // get Selected Item
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Dieser Hintergrund wird mit den zugehörigen Daten endgültig gelöscht!");
                alert.setContentText("Sind Sie sicher, dass Sie dieser Background mit den zugehörigen Daten löschen wollen?");
                alert.initOwner(primaryStage);
                ButtonType butJa = new ButtonType("Ja");
                ButtonType butNein = new ButtonType("Abbrechen");
                alert.getButtonTypes().setAll(butJa,butNein);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == butJa){
                    Background.Category currentCategory = (Background.Category) CategoryButtonCell.this.getTableView().getItems().get(CategoryButtonCell.this.getIndex());

                    //remove selected item from the table list
                    categories.remove(currentCategory);
                    try {

                        bservice.eraseCategory(currentCategory);
                        bservice.deletePairProfileCategory(selectedProfile.getId(),currentCategory.getId());


                    } catch (ServiceException e) {
                        LOGGER.error("Kategorie konnte nicht von db gelöscht werden",e);
                    }



                    setGraphic(null);
                }

            }
        });
    }

    //Display button if the row is not empty
    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);

        if(empty) {
            setGraphic(null);
        }else{
            setGraphic(cellButton);
        }
    }
}