package org.openbooth.gui.specialcells;

import org.openbooth.entities.Profile;
import org.openbooth.service.ProfileService;
import org.openbooth.service.exceptions.ServiceException;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by macdnz on 15.12.16.
 */
public class CamPosCheckbox extends TableCell<Profile.PairCameraPosition, Boolean> {
    final static Logger LOGGER = LoggerFactory.getLogger(CamPosCheckbox.class);

    private final CheckBox cellCheckbox = new CheckBox();
    private String checkboxTyp;

    public CamPosCheckbox(ObservableList<Profile.PairCameraPosition> kamposList, ProfileService pservice, ObservableList<Profile> selectedProfile, String checkboxTyp, Stage primaryStage) {
        this.checkboxTyp = checkboxTyp;

        cellCheckbox.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                Profile.PairCameraPosition currentCamPos = CamPosCheckbox.this.getTableView().getItems().get(CamPosCheckbox.this.getIndex());
                try {
                    if(cellCheckbox.isSelected()) {
                        if(checkboxTyp.equals("greenscreen")) {
                            if (currentCamPos.getPosition() != null)
                                pservice.editPairCameraPosition(currentCamPos, currentCamPos.getCamera().getId(), currentCamPos.getPosition().getId(), true);
                            else{
                                cellCheckbox.setSelected(false);
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Fehler Fenster");
                                alert.setHeaderText("Sie haben keine Position ausgewählt!");
                                alert.setContentText("Bitte wählen Sie zuerst eine Position um Greenscreen zu aktivieren!");
                                alert.initOwner(primaryStage);
                                alert.show();
                            }

                        }else {
                            int posID;
                            if (currentCamPos.getPosition() != null)
                                posID = currentCamPos.getPosition().getId();
                            else
                                posID = pservice.getAllPositions().get(0).getId();

                            currentCamPos = pservice.addPairCameraPosition(selectedProfile.get(0).getId(), currentCamPos.getCamera().getId(), posID, false);
                            kamposList.clear();
                            kamposList.addAll(pservice.getAllPairCamerasWithPositionByProfile(selectedProfile.get(0).getId())); //,posList, selectedProfile);
                            getTableView().setItems(kamposList);


                        }
                    }else {
                        if(checkboxTyp.equals("greenscreen")){
                            pservice.editPairCameraPosition(currentCamPos,currentCamPos.getCamera().getId(),currentCamPos.getPosition().getId(),true);
                        }else {
                            pservice.erasePairCameraPosition(currentCamPos);


                            currentCamPos = new Profile.PairCameraPosition(selectedProfile.get(0).getId(), currentCamPos.getCamera(), null, false);
                            kamposList.clear();
                            kamposList.addAll(pservice.getAllPairCamerasWithPositionByProfile(selectedProfile.get(0).getId())); //,posList, selectedProfile);
                            getTableView().setItems(kamposList);
                        }
                    }


                    LOGGER.debug("Checkbox for "+checkboxTyp+ "_campos ID ="+currentCamPos.getId()+" clicked...checkboxValue="+cellCheckbox.isSelected()+"->profil =>"+selectedProfile.get(0).getId());

                } catch (ServiceException e) {
                   LOGGER.debug("KamposTable->ActivatedCheckbox->Error bei updating checkbox data,selectedProfile ="+selectedProfile.get(0).getId(),e);
                }


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
            Profile.PairCameraPosition currentCamPos = CamPosCheckbox.this.getTableView().getItems().get(CamPosCheckbox.this.getIndex());

            if(checkboxTyp.equals("greenscreen")){
                if(currentCamPos.isGreenScreenReady())
                    cellCheckbox.setSelected(true);
                else
                    cellCheckbox.setSelected(false);
            }else {
                if (currentCamPos.getId() >= 0) {
                    cellCheckbox.setSelected(true);
                } else
                    cellCheckbox.setSelected(false);
            }

            setGraphic(cellCheckbox);
        }
    }
}
