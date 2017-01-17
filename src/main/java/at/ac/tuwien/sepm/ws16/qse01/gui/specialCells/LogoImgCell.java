package at.ac.tuwien.sepm.ws16.qse01.gui.specialCells;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.ws16.qse01.entities.Logo;
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
 * Created by macdnz on 16.12.16.
 */
public class LogoImgCell extends TableCell<Profile.PairLogoRelativeRectangle, String>  {
    final static Logger LOGGER = LoggerFactory.getLogger(LogoImgCell.class);

    private  ObservableList<Profile.PairLogoRelativeRectangle> logoList;
    private ProfileService pservice;


    final ImageView img = new ImageView();
    final Button cellButton = new Button("edit");
    private Desktop desktop;

    public LogoImgCell(ObservableList<Profile.PairLogoRelativeRectangle> logoList, ProfileService pservice, ImageHandler imageHandler,Stage primaryStage) {
        this.logoList = logoList;
        this.pservice = pservice;

        img.setFitHeight(35);
        img.setFitWidth(35);



        cellButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Logo Hochladen...");
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
                        LOGGER.info("Logo Image uploading..."+file.getAbsolutePath());
                        Profile.PairLogoRelativeRectangle p = logoList.get(getIndex());
                        if(p.getLogo()==null){
                            Logo newLogo = pservice.addLogo(new Logo("null(No Logo Name)",file.getAbsolutePath()));
                            p.setLogo(newLogo);

                        }else
                            p.getLogo().setPath(file.getAbsolutePath());


                        /* UPDATE TABLEVIEW */
                        logoList.remove(getIndex());
                        logoList.add(getIndex(),p);

                        img.setImage(getImage(p.getLogo().getPath()));
                        HBox hb = new HBox(img,cellButton);
                        hb.setSpacing(10);
                        hb.setAlignment(Pos.CENTER);
                        setGraphic(hb);


                        pservice.editLogo(p.getLogo());


                    } catch (ServiceException e) {
                       LOGGER.error("LogoImgCell->ImgCell->",e);
                    }
                }

            }
        });
        img.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(logoList.get(getIndex()).getPath()==null)
                    imageHandler.popupImage(System.getProperty("user.dir") + "/src/main/resources/images/noimage.png",primaryStage);
                else
                    imageHandler.popupImage(logoList.get(getIndex()).getLogo().getPath(),primaryStage);

            }

        });
    }

    public Image getImage(String path) {

        if(new File(path).isFile()){
            return new Image("file:" + path, true);
        }else if(new File("file: "+System.getProperty("user.dir") + "/src/main/resources/"+path).isFile()){
            return new Image("file: "+System.getProperty("user.dir") + "/src/main/resources/"+path,true);
        }else
          return new Image("file: "+System.getProperty("user.dir") + "/src/main/resources/images/noimage.png", true);


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
