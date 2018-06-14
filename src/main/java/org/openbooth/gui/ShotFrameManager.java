package org.openbooth.gui;

import org.openbooth.entities.Position;
import org.openbooth.service.exceptions.ServiceException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openbooth.util.SpringFXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

/**
 * This class manages all shotframes
 */

@Component
public class ShotFrameManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShotFrameManager.class);
    private List<ShotFrameController> shotframes;
    private List<Stage> shotStages;
    private Map<Position, ShotFrameController> positonShotFrameMap;

    private SpringFXMLLoader springFXMLLoader;


    public ShotFrameManager(SpringFXMLLoader springFXMLLoader) {
        shotframes = new ArrayList<>();
        shotStages = new ArrayList<>();
        positonShotFrameMap = new HashMap<>();
        this.springFXMLLoader = springFXMLLoader;
    }
    public Map<Position,ShotFrameController> init(List<Position> positionList){
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

                try {
                    SpringFXMLLoader.FXMLWrapper<Object, ShotFrameController> shotFrameWrapper = springFXMLLoader.loadAndWrap("/fxml/shotFrame.fxml", ShotFrameController.class);
                    Parent root =  (Parent) shotFrameWrapper.getLoadedObject();
                    ShotFrameController shotFrameController = shotFrameWrapper.getController();
                    shotFrameController.initShotFrame(position.getId(),stage);

                    positonShotFrameMap.put(position,shotFrameController);
                    stage.setScene(new Scene(root, 400, 400));
                } catch (IOException e) {
                    LOGGER.error("shotFrame.fxml kann nicht geladen werden ",e);
                }
                stage.setFullScreen(false);
                stage.setX(x);
                stage.show();

                shotStages.add(stage);

                x += 200;
            }
        }
        return positonShotFrameMap;

    }

    public void refreshShot(int cameraID,String imgPath) {
        LOGGER.debug("ShotFrameManager->refreshshot with cameraID="+cameraID+", imgPath="+imgPath);
        getShotframe(cameraID).refreshShot(imgPath);

    }
    public void refreshShot(int cameraID,BufferedImage img) {
        LOGGER.debug("ShotFrameManager->refreshshot with cameraID="+cameraID);
        getShotframe(cameraID).refreshShot(img);

    }


    public void refreshShot(Position position, BufferedImage img){
        LOGGER.debug("ShotFrameManager->refreshshot with position="+position.getName());
        getShotframe(position).refreshShot(img);
    }

    public ShotFrameController getShotframe(int cameraID){
        for(ShotFrameController shotFrameController: shotframes){
            if(shotFrameController.getFrameID()==cameraID){
               return shotFrameController;
            }
        }
        return null;
    }

    public ShotFrameController getShotframe(Position position){
        for(Position pos: positonShotFrameMap.keySet()){
            if(pos.equals(position))
                return positonShotFrameMap.get(pos);
        }
        return null;
    }

    public void showCountdown(Position position,int countdown){
        LOGGER.debug("ShotFrameManager->showCountdown with position="+position.getName()+" and countdown = "+countdown);
        getShotframe(position).showCountdown(countdown);

    }
    public void closeFrames(){
        for(Stage stage: shotStages)
            stage.close();
    }
    public boolean isExpired(Position position){
        LOGGER.debug("ShotFrameManager->isExpired with position="+position.getName());
        return getShotframe(position).isExpired();
    }
}
