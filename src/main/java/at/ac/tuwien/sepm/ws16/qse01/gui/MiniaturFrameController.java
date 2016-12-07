package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ImageServiceImpl;
import com.sun.tools.internal.jxc.gen.config.Classes;
import com.sun.tools.javac.util.Log;
import javafx.event.Event;
import javafx.fxml.FXML;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by macdnz on 30.11.16.
 */
@Component
@Scope("prototype")
public class MiniaturFrameController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MiniaturFrameController.class);

    private Stage stage;
    private Stage primaryStage;

    @Resource
    private ImageService imageService;
    @FXML
    private TilePane tile;
    @FXML
    private ScrollPane scrollPane;

    private ImageView activeImageView = null;

    @Autowired
    public MiniaturFrameController(ImageService imageService) throws ServiceException {
        this.imageService = imageService;
    }

    public void init(Stage primaryStage,Stage stage) throws ServiceException {
        this.primaryStage = primaryStage;
        this.stage = stage;
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);



        tile.setPadding(new Insets(20,20,20,20));

        tile.setHgap(20);
        tile.setVgap(20);



       /* HBox menu = new HBox();
         menu.setPrefWidth(tile.getPrefWidth());
        ImageView backbutton = new ImageView( getClass().getResource("/images/back.png").toExternalForm());
        backbutton.setFitHeight(60);
        backbutton.setFitWidth(60);
        backbutton.setX(20);

        menu.getChildren().add(backbutton);
        tile.getChildren().add(menu);*/


        List<at.ac.tuwien.sepm.ws16.qse01.entities.Image> listOfImages = imageService.getAllImages(1);

        for (final at.ac.tuwien.sepm.ws16.qse01.entities.Image img : listOfImages) {

            HBox hBox = new HBox();
            hBox.setSpacing(120);
            hBox.setVisible(false);
            hBox.setStyle("-fx-background-color: #dddddd;");

            ImageView fullscreen = new ImageView( getClass().getResource("/images/fullscreen2.png").toExternalForm());
            fullscreen.setFitHeight(30);
            fullscreen.setFitWidth(30);
            fullscreen.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {
                    LOGGER.debug("fullscreen clicked...");
                }
            });

            ImageView delete = new ImageView( getClass().getResource( "/images/delete3.png").toExternalForm());
            delete.setFitHeight(30);
            delete.setFitWidth(30);
            delete.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {
                    LOGGER.debug("delete button clicked...");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Bild Löschen");
                    alert.setContentText("Möchten Sie das Bild tatsächlich löschen");
                    alert.initModality(Modality.WINDOW_MODAL);
                    alert.initOwner(stage);
                    Optional<ButtonType> result =alert.showAndWait();

                    if(result.isPresent()&&result.get()==ButtonType.OK){
                        ImageView imageView =(ImageView) ((VBox) (((ImageView) mouseEvent.getSource()).getParent().getParent())).getChildren().get(0);
                        LOGGER.debug("Bild wird gelöscht -> imageID ="+imageView.getId());
                        try {

                            imageService.delete(Integer.parseInt(imageView.getId()));
                            tile.getChildren().remove(imageView.getParent());
                            //TODO: das Foto ausm Filesystem löschen. -> FotoDAO->FIleSystem
                        } catch (ServiceException e) {
                            LOGGER.debug("Beim Löschen Fehler aufgetreten: "+e.getMessage());
                        }

                    }
                }
            });


            hBox.getChildren().addAll(fullscreen,delete);


            ImageView imageView = null;
            try {
                if(new File(img.getImagepath()).isFile()) {
                    imageView = createImageView(new File(img.getImagepath()));
                }else if(new File(System.getProperty("user.dir") + "/src/main/resources" + img.getImagepath()).isFile()){
                    imageView = createImageView(new File(System.getProperty("user.dir") + "/src/main/resources" + img.getImagepath()));
                }else {
                    LOGGER.debug("Foto in der DB wurde im Filesystem nicht gefunden und daher gelöscht ->"+img.toString());
                    imageService.delete(img.getImageID());
                }
                if(imageView!=null){
                    VBox vBox = new VBox();
                    LOGGER.debug("imageview id = "+String.valueOf(img.getImageID()));
                    imageView.setId(String.valueOf(img.getImageID()));
                    vBox.getChildren().addAll(imageView,hBox);
                    tile.getChildren().add(vBox);
                }
            }catch (Exception e){
                LOGGER.debug("Fehler: "+e.getMessage());
            }
        }
      //  ImageView firstImageView = (ImageView) ((VBox) tile.getChildren().get(0)).getChildren().get(1);
       // imageClicked1(firstImageView);



    }

    private ImageView createImageView(final File imageFile) {
        ImageView imageView = null;
        try {

            final Image image = new Image(new FileInputStream(imageFile), 150, 0, true,
                    true);
           imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);


            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {

                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                        ImageView imgView = (ImageView) mouseEvent.getSource();

                        if(mouseEvent.getClickCount() == 1) { // bei einem klick buttons anzeigen

                            imageClicked1(imgView);
                        }
                       /* }else if(mouseEvent.getClickCount() == 2) { // bei doppelklick das bild öffnen
                            try {
                                BorderPane borderPane = new BorderPane();
                                ImageView imageView = new ImageView();
                                Image image = new Image(new FileInputStream(imageFile));
                                imageView.setImage(image);
                                imageView.setStyle("-fx-background-color: BLACK");
                                imageView.setFitHeight(stage.getHeight() - 10);
                                imageView.setPreserveRatio(true);
                                imageView.setSmooth(true);
                                imageView.setCache(true);
                                borderPane.setCenter(imageView);
                                borderPane.setStyle("-fx-background-color: BLACK");
                                Stage newStage = new Stage();
                                newStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
                                newStage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
                                newStage.setTitle(imageFile.getName());
                                Scene scene = new Scene(borderPane,Color.BLACK);
                                newStage.setScene(scene);
                                newStage.show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }*/
                    }
                }
            });
            return imageView;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void imageClicked1(ImageView imageView){
        if(!imageView.equals(activeImageView)) {
            ((HBox)((VBox) imageView.getParent()).getChildren().get(1)).setVisible(true);
            if(activeImageView!=null){
                activeImageView.setFitHeight(150);
                activeImageView.setFitWidth(150);
                ((HBox)((VBox) activeImageView.getParent()).getChildren().get(1)).setVisible(false);
            }
            imageView.setFitWidth(180);
            imageView.setFitHeight(180);
            activeImageView = imageView;

        }
    }
    public ImageView getActiveImageView(){
        return activeImageView;
    }
    public void backButtonClicked(){
        LOGGER.debug("backbutton cliked...");
    }
}
