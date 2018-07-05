package org.openbooth.gui.specialcells;

import org.openbooth.gui.GUIImageHelper;
import org.openbooth.util.ImageHandler;
import org.openbooth.entities.Logo;
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
 * Created by macdnz on 16.12.16.
 */
public class LogoImgCell extends TableCell<Profile.PairLogoRelativeRectangle, String>  {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogoImgCell.class);

    private  ObservableList<Profile.PairLogoRelativeRectangle> logoList;
    private ProfileService pservice;


    private final ImageView img = new ImageView();
    private final ImageView cellImgView;

    public LogoImgCell(ObservableList<Profile.PairLogoRelativeRectangle> logoList, ProfileService pservice, ImageHandler imageHandler,Stage primaryStage,AutoCompleteTextField txLogoName) {
        this.logoList = logoList;
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
                        HBox hb = new HBox(img,cellImgView);
                        hb.setSpacing(10);
                        hb.setAlignment(Pos.CENTER);
                        setGraphic(hb);


                        ImageView imgView = new ImageView(new Image("file:" + p.getLogo().getPath(), 30, 30, true, true));
                        imgView.setId((p.getLogo().getPath()));
                        if(pservice.getNumberOfUsing(p.getLogo().getId())==1) {
                            pservice.editLogo(p.getLogo());

                            txLogoName.getImgViews().remove(p.getLogo().getLabel().toLowerCase() + " #" + p.getLogo().getId());
                            txLogoName.getImgViews().put(p.getLogo().getLabel().toLowerCase() + " #" + p.getLogo().getId(), imgView);
                        }else {

                            p.getLogo().setId(Integer.MIN_VALUE);
                            Logo newLogo = pservice.addLogo(p.getLogo());
                            p.setLogo(newLogo);

                            logoList.remove(getIndex());
                            logoList.add(getIndex(),p);


                            //txLogoName.getImgViews().remove(p.getLogo().getLabel().toLowerCase()+" #"+p.getLogo().getId());

                            txLogoName.getEntries().add(p.getLogo().getLabel().toLowerCase()+" #"+p.getLogo().getId());
                            txLogoName.getImgViews().put(p.getLogo().getLabel().toLowerCase()+" #"+p.getLogo().getId(),imgView);
                            pservice.editPairLogoRelativeRectangle(p);
                        }

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
                    GUIImageHelper.popupImage(System.getProperty("user.dir") + "/src/main/resources/images/noimage.png",primaryStage);
                else
                    GUIImageHelper.popupImage(logoList.get(getIndex()).getLogo().getPath(),primaryStage);

            }

        });
    }

    private Image getImage(String path) {

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
            HBox hb = new HBox(img,cellImgView);
            hb.setSpacing(10);
            hb.setAlignment(Pos.CENTER);
            setGraphic(hb);
        }
    }
}
