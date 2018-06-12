package org.openbooth.gui.specialCells;

import org.openbooth.entities.Position;
import org.openbooth.entities.Profile;
import org.openbooth.service.ProfileService;
import org.openbooth.service.exceptions.ServiceException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by macdnz on 16.12.16.
 */
public class CamPosComboBoxCell extends TableCell<Profile.PairCameraPosition, Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CamPosComboBoxCell.class);


    private ProfileService pservice;

    private ComboBox<Position> comboBox = new ComboBox<>();

    public CamPosComboBoxCell(TableColumn parent, ObservableList<Profile.PairCameraPosition> camPosList, ProfileService pservice, ObservableList<Position> posList, ObservableList<Profile> selectedProfile, Stage primaryStage) {
        this.pservice = pservice;
        this.comboBox.setItems(posList);
        this.comboBox.setPromptText("Auswählen");

        this.comboBox.setMinWidth(parent.getWidth());


        this.comboBox.valueProperty().addListener(new ChangeListener<Position>() {

            @Override
            public void changed(ObservableValue ov, Position t, Position selectedPos) {

                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        if(getIndex()<getTableView().getItems().size() && getIndex()>=0) {
                            Profile.PairCameraPosition currentCamPos = getTableView().getItems().get(getIndex());

                            LOGGER.debug("currentCambox id = " + currentCamPos.getId() + " Selected Pos ->" + (selectedPos == null ? "keine" : selectedPos.getName()));
                            LOGGER.debug(currentCamPos.getCamera().getLable() + "_" + currentCamPos.getPosition());

                            try {
                                if (selectedPos != null) {
                                    if (currentCamPos.getId() >= 0) {

                                        pservice.editPairCameraPosition(currentCamPos, currentCamPos.getCamera().getId(), selectedPos.getId(), false);
                                    } else {
                                        comboBox.getSelectionModel().clearSelection();
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Fehler Fenster");
                                        alert.setHeaderText("Sie haben diese Kamera für das ausgewählte Profil nicht aktiviert!");
                                        alert.setContentText("Bitte aktivieren Sie zuerst diese Kamera um Position auswählen zu können!");
                                        alert.initOwner(primaryStage);
                                        alert.show();
                                    }
                                }
                            } catch (ServiceException e) {
                                LOGGER.debug("Error->", e);
                            } catch (NullPointerException e) {
                                LOGGER.debug("NullPointer ->", e);
                            }
                        }
                    }});




            }
        });


    }


    //Display checkbox if the row is not empty
    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);

        if(empty) {
            setGraphic(null);
        }else{

            Profile.PairCameraPosition currentCamPos = getTableView().getItems().get(getIndex());
           try {

                int index2select = -1;
                int i = 0;
                for(Position pos: pservice.getAllPositions()) {

                    if(currentCamPos.getPosition()!=null) {
                        if (currentCamPos.getPosition().getId() == pos.getId())
                            index2select = i;
                    }
                    i++;
                }
                if(index2select!= -1)
                    this.comboBox.getSelectionModel().select(index2select);
                else
                    this.comboBox.getSelectionModel().clearSelection();
            } catch (ServiceException e) {
                LOGGER.error("CamPosComboBoxCell -> updateItem",e);
            }

            setGraphic(comboBox);
        }
    }
}
