package org.openbooth.gui;

import org.openbooth.entities.Position;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openbooth.util.SpringFXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * This class manages all shotframes
 */

@Component
public class ShotFrameManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShotFrameManager.class);
    private List<Stage> shotStages;
    private Map<Position, ShotFrameController> positonShotFrameMap;

    private SpringFXMLLoader springFXMLLoader;


    public ShotFrameManager(SpringFXMLLoader springFXMLLoader) {
        shotStages = new ArrayList<>();
        positonShotFrameMap = new HashMap<>();
        this.springFXMLLoader = springFXMLLoader;
    }
    public Map<Position,ShotFrameController> init(List<Position> positionList) throws IOException {
        Set<Position> oldPositions = positonShotFrameMap.keySet();

         /* Creating shotFrame */
        int numberOfPosition = 1;

        numberOfPosition += positionList.size();

        int x = 200;
        for(int i=1; i<numberOfPosition; i++) { // Anzahl der Kameras...
            Position position = positionList.get(i-1);
            if(!oldPositions.contains(position)) {
                Stage stage = new Stage();
                stage.setTitle("Shot Frame " + position.getName());

                SpringFXMLLoader.FXMLWrapper<Object, ShotFrameController> shotFrameWrapper = springFXMLLoader.loadAndWrap("/fxml/shotFrame.fxml", ShotFrameController.class);
                Parent root = (Parent) shotFrameWrapper.getLoadedObject();
                ShotFrameController shotFrameController = shotFrameWrapper.getController();
                shotFrameController.initShotFrame(stage);

                positonShotFrameMap.put(position,shotFrameController);
                stage.setWidth(400);
                stage.setHeight(400);
                stage.setScene(new Scene(root));
                stage.setFullScreen(false);
                stage.setX(x);
                stage.show();

                shotStages.add(stage);

                x += 200;
            }
        }
        return positonShotFrameMap;

    }


    public void closeFrames(){
        for(Stage stage: shotStages)
            stage.close();
    }
}
