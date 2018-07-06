package org.openbooth.gui.specialcells;

import org.openbooth.gui.GUIImageHelper;
import org.openbooth.util.ImageHandler;
import org.openbooth.entities.Profile;
import org.openbooth.service.ProfileService;
import org.openbooth.service.exceptions.ServiceException;
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
 * Created by macdnz on 15.12.16.
 */
public class ProfileButtonCell extends TableCell<Profile, Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileButtonCell.class);


    private final Button cellButton;

    public ProfileButtonCell(ImageHandler imageHandler,ObservableList<Profile> pList, ProfileService pservice, Stage primaryStage) {

        cellButton = new Button();
        cellButton.setBackground(GUIImageHelper.getButtonBackground(imageHandler,"/images/delete.png",40,40));
        cellButton.setPrefWidth(40);
        cellButton.setPrefHeight(40);

        cellButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                // get Selected Item
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Dieses Profil wird mit den zugehörigen Daten endgültig gelöscht!");
                alert.setContentText("Sind Sie sicher, dass Sie dieses Profil mit den zugehörigen Daten löschen wollen?");
               alert.initOwner(primaryStage);
                ButtonType butJa = new ButtonType("Ja");
                ButtonType butNein = new ButtonType("Abbrechen");
                alert.getButtonTypes().setAll(butJa,butNein);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == butJa){
                    Profile currentProfile = ProfileButtonCell.this.getTableView().getItems().get(ProfileButtonCell.this.getIndex());

                    //remove selected item from the table list
                    pList.remove(currentProfile);
                    try {
                        pservice.erase(currentProfile);
                    } catch (ServiceException e) {
                        e.printStackTrace();
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