package at.ac.tuwien.sepm.ws16.qse01.gui.specialCells;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by macdnz on 16.12.16.
 */
public class LogoButtonCell extends TableCell<Profile.PairLogoRelativeRectangle, Boolean> {
    final static Logger LOGGER = LoggerFactory.getLogger(LogoButtonCell.class);


    private final Button cellButton;

    public LogoButtonCell(ImageHandler imageHandler, ObservableList<Profile.PairLogoRelativeRectangle> posList, ProfileService pservice, Stage primaryStage, ObservableList<Profile> profileID, ImageView preview, AutoCompleteTextField txLogoName) {

        cellButton = new Button();
        cellButton.setBackground(imageHandler.getButtonBackground("/images/delete4.png",40,40));
        cellButton.setPrefWidth(40);
        cellButton.setPrefHeight(40);
        cellButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                // get Selected Item
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Dieses Logo wird mit den zugehörigen Daten endgültig gelöscht!");
                alert.setContentText("Sind Sie sicher, dass Sie dieses Logo mit den zugehörigen Daten löschen wollen?");
                alert.initOwner(primaryStage);
                ButtonType butJa = new ButtonType("Ja");
                ButtonType butNein = new ButtonType("Abbrechen");
                alert.getButtonTypes().setAll(butJa,butNein);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == butJa){
                    Profile.PairLogoRelativeRectangle currentPairLogo = (Profile.PairLogoRelativeRectangle) LogoButtonCell.this.getTableView().getItems().get(LogoButtonCell.this.getIndex());

                    //remove selected item from the table list
                    posList.remove(currentPairLogo);
                   try {

                       //if Logo is used just by this pairlogo relation, then delete it from database

                        if(pservice.getNumberOfUsing(currentPairLogo.getLogo().getId())==1) {
                            pservice.eraseLogo(currentPairLogo.getLogo());
                        }
                        if(pservice.getNumberOfUsingByProfile(currentPairLogo.getLogo().getId(),profileID.get(0).getId())==1)
                            txLogoName.getEntries().remove(currentPairLogo.getLogo().getLabel().toLowerCase()+" #"+currentPairLogo.getLogo().getId());

                       pservice.erasePairLogoRelativeRectangle(currentPairLogo);

                       if(posList.size()==0)
                           preview.setImage(null);
                    } catch (ServiceException e) {
                       LOGGER.error("LogoButtonCell->Löschen Button -> Logo konnte nicht gelöscht werden.",e);
                    }


                    setGraphic(null);
                }

            }
        });
    }

    //Display button if the row is not empty
    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);

        if(empty) {
            setGraphic(null);
        }else{
            setGraphic(cellButton);
        }
    }
}
