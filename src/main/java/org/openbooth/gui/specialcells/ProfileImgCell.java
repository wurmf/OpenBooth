package org.openbooth.gui.specialcells;

import org.openbooth.gui.GUIImageHelper;
import org.openbooth.util.ImageHandler;
import org.openbooth.entities.Profile;
import org.openbooth.service.ProfileService;
import org.openbooth.service.exceptions.ServiceException;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by macdnz on 15.12.16.
 */
public class ProfileImgCell extends TableCell<Profile, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileImgCell.class);

    private  ObservableList<Profile> pList;
    private ProfileService pservice;

    final ImageView img;
    final ImageView cellImgView;
    
    public ProfileImgCell(ObservableList<Profile> pList, ProfileService pservice, ImageHandler imageHandler, Stage primaryStage) {
        this.pList = pList;
        this.pservice = pservice;

        img = new ImageView();
        img.setFitHeight(35);
        img.setFitWidth(35);

        cellImgView = new ImageView(new Image("file:"+ this.getClass().getResource("/images/edit.png").getPath())); //new Button("edit");
        cellImgView.setFitHeight(35);
        cellImgView.setFitWidth(35);
        cellImgView.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
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
                        LOGGER.error("ProfileImgCell->Error bei updating Img Cell ",e);
                    }
                }

            }
        });
        img.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if(pList.get(getIndex()).getWatermark()==null)
                    GUIImageHelper.popupImage(System.getProperty("user.dir") + "/src/main/resources/images/noimage.png",primaryStage);
                else
                    GUIImageHelper.popupImage(pList.get(getIndex()).getWatermark(),primaryStage);

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
            HBox hb = new HBox(img,cellImgView);
            hb.setSpacing(10);
            hb.setAlignment(Pos.CENTER);
            setGraphic(hb);
        }
    }
}