package at.ac.tuwien.sepm.ws16.qse01.gui.specialCells;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
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
 * Created by macdnz on 16.12.16.
 */
public class PositionImgCell extends TableCell<Position, String> {
    final static Logger LOGGER = LoggerFactory.getLogger(PositionImgCell.class);

    private  ObservableList<Position> posList;
    private ProfileService pservice;


    final ImageView img = new ImageView();
    final ImageView cellImgView;

    public PositionImgCell(ObservableList<Position> posList, ProfileService pservice, ImageHandler imageHandler,Stage primaryStage) {
        this.posList = posList;
        this.pservice = pservice;

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

                        Position p = posList.get(getIndex());

                        p.setButtonImagePath(file.getAbsolutePath());


                        /* UPDATE TABLEVIEW */
                        posList.remove(getIndex());
                        posList.add(getIndex(),p);

                        img.setImage(getImage(p.getButtonImagePath()));
                        HBox hb = new HBox(img,cellImgView);
                        hb.setSpacing(10);
                        hb.setAlignment(Pos.CENTER);
                        setGraphic(hb);

                        pservice.editPosition(p);


                    } catch (ServiceException e) {
                       LOGGER.error("PositionImgCell->Update position Image",e);
                    }
                }

            }
        });
        img.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(posList.get(getIndex()).getButtonImagePath()==null)
                    imageHandler.popupImage(System.getProperty("user.dir") + "/src/main/resources/images/noimage.png",primaryStage);
                else
                    imageHandler.popupImage(posList.get(getIndex()).getButtonImagePath(),primaryStage);

            }

        });
    }

    public Image getImage(String path) {
        Image img = null;
        if(path == null) {
            return new Image("file: "+System.getProperty("user.dir") + "/src/main/resources/images/noimage.png", true);
        }else
            return new javafx.scene.image.Image("file:"+path,true);


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
