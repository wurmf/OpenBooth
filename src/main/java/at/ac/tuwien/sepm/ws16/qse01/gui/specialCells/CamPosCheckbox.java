package at.ac.tuwien.sepm.ws16.qse01.gui.specialCells;

import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
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
public class CamPosCheckbox extends TableCell<Profile.PairCameraPosition, Boolean> {
    final static Logger LOGGER = LoggerFactory.getLogger(CamPosCheckbox.class);
    private  ObservableList<Camera> kamList;
    private ObservableList<Profile.PairCameraPosition> kamposList;
    private ProfileService pservice;
    private final CheckBox cellCheckbox = new CheckBox();
    private Profile selectedProfile;
    private String checkboxTyp;

    public CamPosCheckbox(ObservableList<Profile.PairCameraPosition> kamposList, ProfileService pservice, ObservableList<Camera> kamList, Profile selectedProfile) {
        this.kamposList = kamposList;
        this.pservice = pservice;
        this.selectedProfile = selectedProfile;
        this.kamList = kamList;


        cellCheckbox.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                Profile.PairCameraPosition currentCamPos = (Profile.PairCameraPosition) CamPosCheckbox.this.getTableView().getItems().get(CamPosCheckbox.this.getIndex());
                try {
                    if(cellCheckbox.isSelected()) {
                        int posID;
                        if(currentCamPos.getPosition()!=null)
                            posID = currentCamPos.getPosition().getId();
                        else
                            posID = pservice.getAllPositions().get(0).getId();
                        kamposList.remove(currentCamPos);
                        currentCamPos =pservice.addPairCameraPosition(selectedProfile.getId(),currentCamPos.getCamera().getId(),posID,false);
                        kamposList.add(currentCamPos);
                    }else {
                        pservice.erasePairCameraPosition(currentCamPos);

                        /*bservice.deletePairProfileCategory(selectedProfile.getId(), currentCategory.getId());*/
                        /*kamposList.remove(currentCamPos);*/
                    }


                    LOGGER.debug("Checkbox for CamPosActivated "+currentCamPos.getId()+" clicked...checkboxValue="+cellCheckbox.isSelected()+"->profil =>"+selectedProfile.getName());

                } catch (ServiceException e) {
                   LOGGER.debug("KamposTable->ActivatedCheckbox->Error bei updating checkbox data",e);
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
            Profile.PairCameraPosition currentCamPos = ( Profile.PairCameraPosition) CamPosCheckbox.this.getTableView().getItems().get(CamPosCheckbox.this.getIndex());
           if(currentCamPos.getId()>=0) {
                cellCheckbox.setSelected(true);
            }else
                cellCheckbox.setSelected(false);

            setGraphic(cellCheckbox);
        }
    }
}
