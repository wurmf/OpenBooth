package org.openbooth.gui.specialCells;

import org.openbooth.gui.GUIImageHelper;
import org.openbooth.util.ImageHandler;
import org.openbooth.entities.Position;
import org.openbooth.entities.Profile;
import org.openbooth.gui.CameraPositionFrameController;
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
 * Created by macdnz on 16.12.16.
 */
public class PositionButtonCell extends TableCell<Position, Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PositionButtonCell.class);


    private final Button cellButton;

    public PositionButtonCell(ImageHandler imageHandler, ObservableList<Position> posList, ObservableList<Profile.PairCameraPosition> kamPosList, ObservableList<Profile> selectedProfilID, ProfileService pservice, Stage primaryStage, CameraPositionFrameController cameraPositionFrameController) {
        cellButton = new Button();
        cellButton.setBackground(GUIImageHelper.getButtonBackground(imageHandler,"/images/delete.png",50,50));
        cellButton.setPrefWidth(40);
        cellButton.setPrefHeight(40);
        cellButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                // get Selected Item
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Diese Position wird mit den zugehörigen Daten endgültig gelöscht!");
                alert.setContentText("Sind Sie sicher, dass Sie diese Position mit den zugehörigen Daten löschen wollen?");
                alert.initOwner(primaryStage);
                ButtonType butJa = new ButtonType("Ja");
                ButtonType butNein = new ButtonType("Abbrechen");
                alert.getButtonTypes().setAll(butJa,butNein);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == butJa){
                    Position currentPosition = (Position) PositionButtonCell.this.getTableView().getItems().get(PositionButtonCell.this.getIndex());

                    //remove selected item from the table list
                    posList.remove(currentPosition);
                    try {
                        pservice.erasePosition(currentPosition);

                        // refreshing kamPos TableView
                        kamPosList.clear();

                        kamPosList.addAll(pservice.getAllPairCamerasWithPositionByProfile(selectedProfilID.get(0).getId()));

                        cameraPositionFrameController.refreshTableKameraPosition(kamPosList,posList,selectedProfilID);

                    } catch (ServiceException e) {
                        LOGGER.error("PositionButtonCell->Löschen Button -> Position konnte nicht gelöscht werden.",e);
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
