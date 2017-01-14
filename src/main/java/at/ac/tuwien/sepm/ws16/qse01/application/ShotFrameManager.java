package at.ac.tuwien.sepm.ws16.qse01.application;

import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.gui.MainFrameController;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages all shotframes
 */

@Component
public class ShotFrameManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrameController.class);
    private List<ShotFrameController> shotframes;
    private List<Stage> shotStages;



    public ShotFrameManager() throws ServiceException {
        shotframes = new ArrayList<>();
        shotStages = new ArrayList<>();
    }
    public void init(List<Camera> cameraList){

         /* Creating shotFrame */
        int numberOfCameras = 1;

        numberOfCameras += cameraList.size();

        int x = 200;
        for(int i=1; i<numberOfCameras; i++) { // Anzahl der Kameras...
            Stage stage = new Stage();
            stage.setTitle("Shot Frame "+i);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/fxml/shotFrame.fxml"));
                Parent root = loader.load();
                ShotFrameController shotFrameController = loader.getController();
                shotFrameController.initShotFrame(cameraList.get(i-1).getId());
                shotframes.add(shotFrameController);

                stage.setScene(new Scene(root ,400,400));
            } catch (IOException e) {
               LOGGER.debug("shotFrame.xml kann nicht geladen werden +"+e.getMessage());
            }
            stage.setFullScreen(false);
            stage.setX(x);
            stage.show();

            shotStages.add(stage);

            x += 200;
        }
    }

    public void refreshShot(int cameraID,String imgPath) {
        LOGGER.debug("ShotFrameManager->refreshshot with cameraID="+cameraID+", imgPath="+imgPath);
        getShotframe(cameraID).refreshShot(imgPath);

    }
    public void refreshShot(int cameraID,BufferedImage img) {
        LOGGER.debug("ShotFrameManager->refreshshot with cameraID="+cameraID);
        getShotframe(cameraID).refreshShot(img);

    }
    public ShotFrameController getShotframe(int cameraID){
        for(ShotFrameController shotFrameController: shotframes){
            if(shotFrameController.getFrameID()==cameraID){
               return shotFrameController;
            }
        }
        return null;
    }
    public void closeFrames(){
        for(Stage stage: shotStages)
            stage.close();
    }
}
