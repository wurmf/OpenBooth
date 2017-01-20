package at.ac.tuwien.sepm.ws16.qse01.gui.specialCells;

import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by macdnz on 16.12.16.
 */
public class CamPosComboBoxCell extends TableCell<Profile.PairCameraPosition, Boolean> {
    final static Logger LOGGER = LoggerFactory.getLogger(CamPosComboBoxCell.class);

    private  ObservableList<Profile.PairCameraPosition> camPosList;
    private  ObservableList<Position> posList;
    private ProfileService pservice;

    private ComboBox<Position> comboBox = new ComboBox();

    public CamPosComboBoxCell(ObservableList<Profile.PairCameraPosition> camPosList, ProfileService pservice, ObservableList<Position> posList) {
        this.camPosList = camPosList;
        this.posList = posList;
        this.pservice = pservice;
        this.comboBox.setItems(posList);
        this.comboBox.setPromptText("Position auswählen");

        this.comboBox.valueProperty().addListener(new ChangeListener<Position>() {

            @Override
            public void changed(ObservableValue ov, Position t, Position selectedPos) {
                if(getIndex()<getTableView().getItems().size() && getIndex()>=0) {
                    Profile.PairCameraPosition currentCamPos = (Profile.PairCameraPosition) getTableView().getItems().get(getIndex());
                    currentCamPos.setPosition(selectedPos);
                    LOGGER.debug("Selected Pos ->" + (selectedPos == null ? "keine" : selectedPos.getName()));
                    LOGGER.debug(currentCamPos.getCamera().getLable() + "_" + currentCamPos.getPosition());

                    try {
                        if(currentCamPos.getPosition()!=null)
                            pservice.editPairCameraPosition(currentCamPos, currentCamPos.getCamera().getId(), currentCamPos.getPosition().getId(), false);
                    } catch (ServiceException e) {
                        LOGGER.debug("Error->", e);
                    } catch (NullPointerException e) {
                        LOGGER.debug("NullPointer ->", e);
                    }
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
            Profile.PairCameraPosition currentCamPos = (Profile.PairCameraPosition) getTableView().getItems().get(getIndex());
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
            } catch (ServiceException e) {
                LOGGER.error("CamPosComboBoxCell -> updateItem",e);
            }

            setGraphic(comboBox);
        }
    }
}
