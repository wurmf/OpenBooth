package at.ac.tuwien.sepm.ws16.qse01.gui.specialCells;

import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.BackgroundService;
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
public class CategoryCheckboxCell extends TableCell<Background.Category, Boolean> {
    final static Logger LOGGER = LoggerFactory.getLogger(CategoryCheckboxCell.class);
    private  ObservableList<Background.Category> categoryList;
    private ObservableList<Background.Category> categoryListOfProfile;
    private BackgroundService bservice;
    private final CheckBox cellCheckbox = new CheckBox();
    private Profile selectedProfile;

    public CategoryCheckboxCell(ObservableList<Background.Category> categoryListOfProfile, BackgroundService bservice, ObservableList<Background.Category> categoryList, Profile selectedProfile) {
        this.categoryListOfProfile = categoryListOfProfile;
        this.bservice = bservice;
        this.selectedProfile = selectedProfile;
        this.categoryList = categoryList;

        cellCheckbox.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                Background.Category currentCategory = (Background.Category) CategoryCheckboxCell.this.getTableView().getItems().get(CategoryCheckboxCell.this.getIndex());
                try {
                    if(cellCheckbox.isSelected()) {
                        bservice.createPairProfileCategory(selectedProfile.getId(), currentCategory.getId());
                        categoryListOfProfile.add(currentCategory);
                    }else {
                        /*bservice.deletePairProfileCategory(selectedProfile.getId(), currentCategory.getId());
                        categoryListOfProfile.remove(currentCategory);*/
                    }


                    LOGGER.debug("Checkbox for Category "+currentCategory.getId()+" clicked...checkboxValue="+cellCheckbox.isSelected()+"->profil =>"+selectedProfile.getName());

                } catch (ServiceException e) {
                   LOGGER.debug("Error bei updating checkbox data",e);
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
            Background.Category currentCategory = (Background.Category) CategoryCheckboxCell.this.getTableView().getItems().get(CategoryCheckboxCell.this.getIndex());
            if(categoryListOfProfile.contains(currentCategory)) {
                cellCheckbox.setSelected(true);
            }else
                cellCheckbox.setSelected(false);

            setGraphic(cellCheckbox);
        }
    }
}
