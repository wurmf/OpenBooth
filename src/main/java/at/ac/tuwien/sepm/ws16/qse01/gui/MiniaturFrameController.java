package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ImageServiceImpl;
import javafx.fxml.FXML;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
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
    @Resource
    private ImageService imageService;
    @FXML
    private TilePane tile;
    @FXML
    private ScrollPane scrollPane;

    @Autowired
    public MiniaturFrameController(ImageService imageService) throws ServiceException {
        this.imageService = imageService;
    }

    public void init(Stage primaryStage) throws ServiceException {
        stage = primaryStage;
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        tile.setPadding(new Insets(15,15,15,15));

        tile.setHgap(15);



        List<String> listOfImages = imageService.getAllImagePaths(1);

        for (final String file : listOfImages) {
            ImageView imageView;
            try {
                imageView = createImageView(new File(System.getProperty("user.dir") + "/src/main/resources" + file));
                tile.getChildren().add(imageView);
            }catch (Exception e){
                LOGGER.debug("Fehler: "+e.getMessage());
            }
        }



    }

    private ImageView createImageView(final File imageFile) {

        ImageView imageView = null;
        try {

            final Image image = new Image(new FileInputStream(imageFile), 150, 0, true,
                    true);
            imageView = new ImageView(image);
           // imageView.setFitWidth(150);


            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {

                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){

                        if(mouseEvent.getClickCount() == 1){ // bei einem klick das bild öffnen
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

                        }
                    }
                }
            });
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return imageView;
    }
}
