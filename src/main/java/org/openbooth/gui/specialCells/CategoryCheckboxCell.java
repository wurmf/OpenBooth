package org.openbooth.gui.specialCells;

import org.openbooth.entities.Background;
import org.openbooth.entities.Profile;
import org.openbooth.service.BackgroundService;
import org.openbooth.service.exceptions.ServiceException;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryCheckboxCell.class);
    private ObservableList<Background.Category> categoryListOfProfile;

    private final CheckBox cellCheckbox = new CheckBox();


    public CategoryCheckboxCell(ObservableList<Background.Category> categoryListOfProfile, BackgroundService bservice, ObservableList<Background.Category> categoryList, ObservableList<Profile>  selectedProfile) {
        this.categoryListOfProfile = categoryListOfProfile;

        cellCheckbox.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                Background.Category currentCategory = CategoryCheckboxCell.this.getTableView().getItems().get(CategoryCheckboxCell.this.getIndex());
                try {
                    if(cellCheckbox.isSelected()) {
                        bservice.createPairProfileCategory(selectedProfile.get(0).getId(), currentCategory.getId());
                        categoryListOfProfile.add(currentCategory);
                    }else {
                        /*bservice.deletePairProfileCategory(selectedProfile.getId(), currentCategory.getId());
                        categoryListOfProfile.remove(currentCategory);*/
                    }


                    LOGGER.debug("Checkbox for Category "+currentCategory.getId()+" clicked...checkboxValue="+cellCheckbox.isSelected()+"->profil =>"+selectedProfile.get(0).getId());

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
            Background.Category currentCategory = CategoryCheckboxCell.this.getTableView().getItems().get(CategoryCheckboxCell.this.getIndex());
            if(categoryListOfProfile.contains(currentCategory)) {
                cellCheckbox.setSelected(true);
            }else
                cellCheckbox.setSelected(false);

            setGraphic(cellCheckbox);
        }
    }
}
