package at.ac.tuwien.sepm.ws16.qse01.gui.specialCells;

import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

/**
 * Created by macdnz on 15.12.16.
 */
public class ProfileCheckboxCell extends TableCell<Profile, Boolean> {
    private  ObservableList<Profile> pList;
    private ProfileService pservice;
    private String checkboxTyp;
    private final CheckBox cellCheckbox = new CheckBox();

    public ProfileCheckboxCell(ObservableList<Profile> pList, ProfileService pservice,String checkboxTyp) {
        this.pList = pList;
        this.pservice = pservice;
       this.checkboxTyp = checkboxTyp;

    }

    //Display checkbox if the row is not empty
    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);

        if(empty) {
            setGraphic(null);
        }else{
            Profile currentProfile = (Profile) ProfileCheckboxCell.this.getTableView().getItems().get(ProfileCheckboxCell.this.getIndex());
            if(checkboxTyp.equals("mobil"))
                cellCheckbox.setSelected(currentProfile.isMobilEnabled());
            else if (checkboxTyp.equals("green"))
                cellCheckbox.setSelected(currentProfile.isGreenscreenEnabled());
            else if (checkboxTyp.equals("filter"))
                cellCheckbox.setSelected(currentProfile.isFilerEnabled());
            else if (checkboxTyp.equals("drucken"))
                cellCheckbox.setSelected(currentProfile.isPrintEnabled());

            setGraphic(cellCheckbox);
        }
    }
}
