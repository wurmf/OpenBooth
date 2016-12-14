package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for MiniaturFrame
 */
@Component
/*@Scope("prototype")*/
public class MiniaturFrameController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MiniaturFrameController.class);

    @Resource
    private ImageService imageService;
    private WindowManager windowManager;
    @FXML
    private TilePane tile;
    @FXML
    private ScrollPane scrollPane;

    private ImageView activeImageView = null;

    @Autowired
    public MiniaturFrameController(ImageService imageService, WindowManager windowManager) throws ServiceException {
        this.imageService = imageService;
        this.windowManager = windowManager;
    }

    public void init(Stage stage) throws ServiceException {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);



        tile.setPadding(new Insets(20,20,20,20));

        tile.setHgap(20);
        tile.setVgap(20);



        List<at.ac.tuwien.sepm.ws16.qse01.entities.Image> listOfImages = imageService.getAllImages(1);

        for (final at.ac.tuwien.sepm.ws16.qse01.entities.Image img : listOfImages) {

            HBox hBox = new HBox();
            hBox.setSpacing(120);
            hBox.setVisible(false);
            hBox.setStyle("-fx-background-color: #dddddd;");

            ImageView fullscreen = new ImageView(new Image("/images/fullscreen3.jpg"));
            fullscreen.setFitHeight(30);
            fullscreen.setFitWidth(30);
            fullscreen.setOnMouseClicked(mouseEvent -> {
                LOGGER.debug("fullscreen clicked...");
                windowManager.showFullscreenImage();
            });

            ImageView delete = new ImageView( getClass().getResource( "/images/delete3.png").toExternalForm());
            delete.setFitHeight(30);
            delete.setFitWidth(30);
            delete.setOnMouseClicked((MouseEvent mouseEvent) -> {
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

                        imageService.delete(Integer.parseInt(imageView.getId())); //löschen aus Datenbank




                        tile.getChildren().remove(imageView.getParent());
                        //TODO: das Foto ausm Filesystem löschen. -> FotoDAO->FIleSystem
                        new File(String.valueOf(imageView.getUserData())).delete();
                    } catch (ServiceException e) {
                        LOGGER.debug("Beim Löschen Fehler aufgetreten: "+e.getMessage());
                    }

                }
            });


            hBox.getChildren().addAll(fullscreen,delete);


            ImageView imageView = null;
            try {
                if(new File(img.getImagepath()).isFile()) {
                    imageView = createImageView(new File(img.getImagepath()));
                }else if(new File(System.getProperty("user.dir") + "/src/main/resources" + img.getImagepath()).isFile()){
                    img.setImagepath(System.getProperty("user.dir") + "/src/main/resources" + img.getImagepath());
                    imageView = createImageView(new File(img.getImagepath()));
                }else {
                    LOGGER.debug("Foto in der DB wurde im Filesystem nicht gefunden und daher gelöscht ->"+img.toString());
                    imageService.delete(img.getImageID());
                }
                if(imageView!=null){
                    VBox vBox = new VBox();
                    LOGGER.debug("imageview id = "+img.getImageID());
                    imageView.setId(String.valueOf(img.getImageID()));
                    imageView.setUserData(img.getImagepath());
                    vBox.getChildren().addAll(imageView,hBox);
                    tile.getChildren().add(vBox);
                }
            }catch (Exception e){
                LOGGER.debug("Fehler: "+e.getMessage());
            }
        }

    }

    private ImageView createImageView(final File imageFile) {
        ImageView imageView;
        try {

            final Image image = new Image(new FileInputStream(imageFile), 150, 0, true,
                    true);
           imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);


            imageView.setOnMouseClicked(mouseEvent -> {

                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    ImageView imgView = (ImageView) mouseEvent.getSource();

                    if(mouseEvent.getClickCount() == 1) { // bei einem klick buttons anzeigen

                        imageClicked1(imgView);
                    }
                }
            });
            return imageView;
        } catch (FileNotFoundException ex) {
           LOGGER.debug("image not found : "+ex.getMessage());
        }
        return null;
    }

    private void imageClicked1(ImageView imageView){
        if(!imageView.equals(activeImageView)) {
            ((VBox) imageView.getParent()).getChildren().get(1).setVisible(true);
            if(activeImageView!=null){
                activeImageView.setFitHeight(150);
                activeImageView.setFitWidth(150);
                ((VBox) activeImageView.getParent()).getChildren().get(1).setVisible(false);
            }
            imageView.setFitWidth(180);
            imageView.setFitHeight(180);
            activeImageView = imageView;

        }
    }

    public void backButtonClicked(){
        windowManager.showMainFrame();
        LOGGER.debug("backbutton cliked...");
    }
}
