package at.ac.tuwien.sepm.ws16.qse01.gui.specialCells;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;

/**
 * Created by macdnz on 15.12.16.
 */
public class ProfileImgCell extends TableCell<Profile, String> {
    final static Logger LOGGER = LoggerFactory.getLogger(ProfileImgCell.class);

    private  ObservableList<Profile> pList;
    private ProfileService pservice;

    final ImageView img = new ImageView();
    final Button cellButton = new Button("edit");
    private Desktop desktop;
    
    public ProfileImgCell(ObservableList<Profile> pList, ProfileService pservice,ImageHandler imageHandler,Stage primaryStage) {
        this.pList = pList;
        this.pservice = pservice;

        img.setFitHeight(35);
        img.setFitWidth(35);


        cellButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                // System.out.println("img changed..");
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Bild Hochladen...");
                fileChooser.setInitialDirectory(
                        new File(System.getProperty("user.home"))
                );
                fileChooser.getExtensionFilters().addAll(
                        // new FileChooser.ExtensionFilter("All Images", "*.*"),
                        new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                        new FileChooser.ExtensionFilter("PNG", "*.png")
                );
                File file = fileChooser.showOpenDialog(new Stage());
                if (file != null) {
                    try {

                        Profile p = pList.get(getIndex());

                        p.setWatermark(file.getAbsolutePath());

                        pservice.edit(p);

                        pList.removeAll(pList);
                        pList.addAll(pservice.getAllProfiles());


                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        img.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if(pList.get(getIndex()).getWatermark()==null)
                    imageHandler.popupImage(System.getProperty("user.dir") + "/src/main/resources/images/noimage.png",primaryStage);
                else
                    imageHandler.popupImage(pList.get(getIndex()).getWatermark(),primaryStage);

            }

        });
    }

    public Image getImage(String path) {
        Image img = null;
        if(path == null) {
            return new Image("file: "+System.getProperty("user.dir") + "/src/main/resources/images/noimage.png", true);
        }else
            return new Image("file:"+path,true);


    }

    //Display button if the row is not empty
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if(empty) {
            setGraphic(null);
        }else{
            img.setImage(getImage(item));
            HBox hb = new HBox(img,cellButton);
            hb.setSpacing(10);
            hb.setAlignment(Pos.CENTER);
            setGraphic(hb);
        }
    }
}
