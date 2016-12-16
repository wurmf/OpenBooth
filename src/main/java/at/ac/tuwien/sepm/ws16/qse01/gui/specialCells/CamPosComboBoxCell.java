package at.ac.tuwien.sepm.ws16.qse01.gui.specialCells;

import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
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

        this.posList.getItems().addAll(posList);

    }

    //Display checkbox if the row is not empty
    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);

        if(empty) {
            setGraphic(null);
        }else{

            setGraphic(posList);
        }
    }
}
