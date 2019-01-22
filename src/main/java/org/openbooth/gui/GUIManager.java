package org.openbooth.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.openbooth.trigger.KeyPressTrigger;
import org.openbooth.util.SpringFXMLLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GUIManager {

    private KeyPressTrigger keyPressTrigger;
    private SpringFXMLLoader springFXMLLoader;

    public GUIManager(KeyPressTrigger keyPressTrigger, SpringFXMLLoader springFXMLLoader) {
        this.keyPressTrigger = keyPressTrigger;
        this.springFXMLLoader = springFXMLLoader;
    }

    public void setUpGUI(Stage primaryStage) throws IOException {
        SpringFXMLLoader.FXMLWrapper wrapper = springFXMLLoader.loadAndWrap("/fxml/shotFrame.fxml", ShotFrameController.class);
        Parent parent = (Parent) wrapper.getLoadedObject();

        parent.addEventFilter(KeyEvent.KEY_PRESSED, keyPressTrigger::trigger);

        primaryStage.setScene(new Scene(parent));
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }

}
