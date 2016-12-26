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
    private ProfileService pservice;

    private final ComboBox posList = new ComboBox();

    public CamPosComboBoxCell(ObservableList<Profile.PairCameraPosition> camPosList, ProfileService pservice, ObservableList<Position> posList) {
        this.camPosList = camPosList;
        this.pservice = pservice;
        this.posList.setPromptText("Position auswählen");

        this.posList.valueProperty().addListener(new ChangeListener<Profile.PairCameraPosition>() {

            @Override
            public void changed(ObservableValue ov, Profile.PairCameraPosition t, Profile.PairCameraPosition selectedCamPos) {
                Profile.PairCameraPosition currentCamPos = (Profile.PairCameraPosition) getTableView().getItems().get(getIndex());
                currentCamPos.setPosition(selectedCamPos.getPosition());
                try {
                    pservice.editPairCameraPosition(currentCamPos,currentCamPos.getCamera().getId(),currentCamPos.getPosition().getId(),false);
                } catch (ServiceException e) {
                    LOGGER.debug("Error->"+e.getMessage());
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
            LOGGER.info("index =>"+getIndex());
            Profile.PairCameraPosition currentCamPos = (Profile.PairCameraPosition) getTableView().getItems().get(getIndex());
            try {

                int index2select = -1;
                int i = 0;
                for(Position pos: pservice.getAllPositions()) {
                    this.posList.getItems().add(pos.getName());
                    if(currentCamPos.getPosition().getId()==pos.getId())
                        index2select = i;
                    i++;
                }
                if(index2select!= -1)
                    this.posList.getSelectionModel().select(index2select);
            } catch (ServiceException e) {
                e.printStackTrace();
            }

            setGraphic(posList);
        }
    }
}
