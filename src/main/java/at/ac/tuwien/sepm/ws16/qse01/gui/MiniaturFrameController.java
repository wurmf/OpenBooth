package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for MiniaturFrame
 */
@Component
/*@Scope("prototype")*/
public class MiniaturFrameController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MiniaturFrameController.class);

    @Resource
    private ImageService imageService;
    @Resource
    private ShootingService shootingService;

    private WindowManager windowManager;
    @FXML
    private TilePane tile;
    @FXML
    private ScrollPane scrollPane;

    private Stage stage=null;
    private ImageView activeImageView = null;
    List<at.ac.tuwien.sepm.ws16.qse01.entities.Image> listOfImages=null;

    @Autowired
    public MiniaturFrameController(ImageService imageService, ShootingService shootingService,WindowManager windowManager) throws ServiceException {
        this.imageService = imageService;
        this.shootingService = shootingService;
        this.windowManager = windowManager;
    }

    public void init(Stage stage) throws ServiceException {
        this.stage=stage;
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        double screenWidth= Screen.getPrimary().getBounds().getWidth();
        double screenHeight=Screen.getPrimary().getBounds().getHeight();

        tile.setMinWidth(screenWidth);
        tile.setMinHeight(screenHeight-60);



        tile.setPadding(new Insets(20,20,20,20));

        tile.setHgap(20);
        tile.setVgap(20);

        if(shootingService.searchIsActive().getActive()) {
            LOGGER.debug("Miniaturansich -> Active Shooting ->" + shootingService.searchIsActive().getId());
            listOfImages = imageService.getAllImages(shootingService.searchIsActive().getId());//shootingService.searchIsActive().getId());//shootingService.searchIsActive().getId());
        }else{
            listOfImages = new ArrayList<>();
            return;
        }
      //  listOfImages = imageService.getAllImages(1);
        //List<at.ac.tuwien.sepm.ws16.qse01.entities.Image> listOfImages = imageService.getAllImages(3);//shootingService.searchIsActive().getId());

        for (final at.ac.tuwien.sepm.ws16.qse01.entities.Image img : listOfImages) {
            prepareHBox(img,-1);
        }

    }

    private ImageView createImageView(final File imageFile) {
        ImageView imageView;
        try {

            final Image image = new Image(new FileInputStream(imageFile), 150, 150, true,
                    true);
            imageView = new ImageView(image);
            imageView.setStyle("-fx-background-color: WHITE");

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
           LOGGER.error("createImageView->image not found : ",ex);
        }
        return null;
    }

    private void imageClicked1(ImageView imageView){
        if(!imageView.equals(activeImageView)) {
            ((VBox) imageView.getParent()).getChildren().get(1).setVisible(true);

            if(activeImageView!=null){

                activeImageView.setFitHeight(150);
                activeImageView.setFitWidth(150);
                activeImageView.setPreserveRatio(true);
                imageView.setStyle("-fx-background-color: BLACK");
                ((VBox) activeImageView.getParent()).getChildren().get(1).setVisible(false);
            }
            imageView.setPreserveRatio(false);
            imageView.setFitWidth(180);
            imageView.setFitHeight(180);
            imageView.setStyle("-fx-background-color: BLACK");
            activeImageView = imageView;

        }else{
            /*
            activeImageView.setFitHeight(150);
            activeImageView.setFitWidth(150);
            activeImageView.setPreserveRatio(true);
            imageView.setStyle("-fx-background-color: BLACK");
            ((VBox) activeImageView.getParent()).getChildren().get(1).setVisible(false);
            activeImageView = null;*/
            windowManager.showFullscreenImage(Integer.parseInt(imageView.getId()));
        }
    }

    public void backButtonClicked(){
        windowManager.showScene(WindowManager.SHOW_CUSTOMERSCENE);
        LOGGER.debug("backbutton cliked...");
    }

    public void notifyOfNewImage(at.ac.tuwien.sepm.ws16.qse01.entities.Image image,int index) {
        if(index==-1)
            listOfImages.add(image);
        else
            listOfImages.add(index,image);

        prepareHBox(image,index);
    }
    public void notifyOfDelete(at.ac.tuwien.sepm.ws16.qse01.entities.Image image){
        listOfImages.removeIf(img -> img.getImageID()==image.getImageID());

        List<Node> vboxList=tile.getChildren().stream()
                .filter(node -> node instanceof VBox)                                               //Filter for instances of VBox
                .map(node -> (VBox)node)                                                            //Cast these instances to VBoxes
                .map(vbox -> vbox.getChildren().get(0))                                             //Get the ImageViews from the VBoxes
                .filter(imageView -> imageView.getId().equals(String.valueOf(image.getImageID())))  //Filter the Stream for the one object that has the images id
                .map(Node::getParent)                                                               //map the resulting ImageViews back to its parent(Node-object)
                .collect(Collectors.toList());                                                      //Collect the stream to a list
        tile.getChildren().remove(vboxList.get(0));
    }

    private void prepareHBox(at.ac.tuwien.sepm.ws16.qse01.entities.Image img,int index){
        HBox hBox = new HBox();
        hBox.setPrefWidth(180);
        hBox.setSpacing(120);
        hBox.setVisible(false);
        hBox.setStyle("-fx-background-color: white;");

        ImageView fullscreen = new ImageView(new Image("/images/fullscreen3.jpg"));
        fullscreen.setFitHeight(30);
        fullscreen.setFitWidth(30);
        fullscreen.setOnMouseClicked(mouseEvent -> {
            ImageView imageView =(ImageView) ((VBox) (((ImageView) mouseEvent.getSource()).getParent().getParent())).getChildren().get(0);
            LOGGER.debug("fullscreen clicked...imageID = "+imageView.getId());

            windowManager.showFullscreenImage(Integer.parseInt(imageView.getId()));
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
            alert.initOwner(this.stage);
            Optional<ButtonType> result =alert.showAndWait();

            if(result.isPresent()&&result.get()==ButtonType.OK){
                ImageView imageView =(ImageView) ((VBox) (((ImageView) mouseEvent.getSource()).getParent().getParent())).getChildren().get(0);
                LOGGER.debug("Bild wird gelöscht -> imageID ="+imageView.getId());
                try {

                    imageService.delete(Integer.parseInt(imageView.getId())); //löschen aus Datenbank

                    tile.getChildren().remove(imageView.getParent());

                } catch (ServiceException e) {
                    LOGGER.error("prepareHBox->Beim Löschen Fehler aufgetreten: ",e);
                }

            }
        });


        hBox.getChildren().addAll(fullscreen,delete);


        ImageView imageView = null;
        try {
            if(new File(img.getImagepath()).isFile()) {
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

                if(index==-1)
                    tile.getChildren().add(vBox);
                else
                    tile.getChildren().add(index,vBox);
            }
        }catch (Exception e){
            LOGGER.error("Fehler: ",e);
        }
    }
}
