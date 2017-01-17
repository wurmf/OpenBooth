package at.ac.tuwien.sepm.ws16.qse01.gui.specialCells;

import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by macdnz on 15.12.16.
 */
public class ProfileCheckboxCell extends TableCell<Profile, Boolean> {
    final static Logger LOGGER = LoggerFactory.getLogger(ProfileCheckboxCell.class);
    private  ObservableList<Profile> pList;
    private ProfileService pservice;
    private String checkboxTyp;
    private final CheckBox cellCheckbox = new CheckBox();

    public ProfileCheckboxCell(ObservableList<Profile> pList, ProfileService pservice,String checkboxTyp) {
        this.pList = pList;
        this.pservice = pservice;
       this.checkboxTyp = checkboxTyp;

        cellCheckbox.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                Profile currentProfile = (Profile) ProfileCheckboxCell.this.getTableView().getItems().get(ProfileCheckboxCell.this.getIndex());

                if(checkboxTyp.equals("mobil"))
                   currentProfile.setMobilEnabled(cellCheckbox.isSelected());
                else if (checkboxTyp.equals("green"))
                    currentProfile.setGreenscreenEnabled(cellCheckbox.isSelected());
                else if (checkboxTyp.equals("filter"))
                    currentProfile.setFilerEnabled(cellCheckbox.isSelected());
                else if (checkboxTyp.equals("drucken"))
                    currentProfile.setPrintEnabled(cellCheckbox.isSelected());

                LOGGER.debug("Checkbox "+checkboxTyp+" clicked...checkboxValue="+cellCheckbox.isSelected()+"->profil =>"+currentProfile.getName()+"_=>=>"+currentProfile.isFilerEnabled()+"_"+currentProfile.isGreenscreenEnabled()+"_"+currentProfile.isMobilEnabled()+"_"+currentProfile.isPrintEnabled());

                try {
                    pservice.edit(currentProfile);
                } catch (ServiceException e) {
                   LOGGER.error("ProfileCheckboxCell->Error bei updating checkbox data ",e);
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
