package org.openbooth.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.exceptions.ImageHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GUIImageHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GUIImageHelper.class);

    /**
     * shows the given image in own stage (popup window)
     * @param imgPath path of the image
     * @param stage primary stage - owner of this window
     */
    public static void popupImage(String imgPath,Stage stage){
        try {
            Stage previewStage = new Stage();
            Group root = new Group();
            Scene scene = new Scene(root);

            ImageView prevView = new ImageView(new Image("file:"+imgPath));
            root.getChildren().add(prevView);

            previewStage.setTitle("Preview Image");
            previewStage.setWidth(prevView.getImage().getWidth());
            previewStage.setHeight(prevView.getImage().getHeight());
            previewStage.setScene(scene);
            previewStage.setFullScreen(false);
            previewStage.initOwner(stage);
            previewStage.show();
        }catch(NullPointerException e){
            LOGGER.error("popupImage ->",e);
        }
    }

    /**
     * creates scene.layout.Background object with given input data
     * @param resourcePath path of icon in resources directory
     * @param width width of icon
     * @param height height of icon
     * @return Background returns Background object created with given data
     */
    public static Background getButtonBackground(ImageHandler imageHandler, String resourcePath, int width, int height){
        Image image = null ;
        try {
            image =  SwingFXUtils.toFXImage(imageHandler.openImage(GUIImageHelper.class.getResourceAsStream(resourcePath)),null);
        } catch (ImageHandlingException e) {
            LOGGER.error("getButtonBackground->Error",e);
        }
        BackgroundSize backgroundSize = new BackgroundSize(width, height, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        return new javafx.scene.layout.Background(backgroundImage);
    }
}
