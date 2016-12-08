package at.ac.tuwien.sepm.ws16.qse01.application;

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


   /* @Resource
    private CameraService cameraService;*/

    public ShotFrameManager() throws ServiceException {

        shotframes = new ArrayList<>();
    }
    public void init(){
        //TODO: die aktive kameras aus der datenbank holen
         /* Creating shotFrame */
        int anz = 2;
        int x = 200;
        for(int i=0; i<anz; i++) { // Anzahl der Kameras...
            Stage stage = new Stage();
            stage.setTitle("Shot Frame "+(i+1));

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/fxml/shotFrame.fxml"));
                Parent root = loader.load();
                ShotFrameController shotFrameController = loader.getController();
                shotFrameController.initShotFrame((i+1));
                shotframes.add(shotFrameController);

                stage.setScene(new Scene(root ,400,400));
            } catch (IOException e) {
               LOGGER.debug("shotFrame.xml kann nicht geladen werden +"+e.getMessage());
            }
            stage.setFullScreen(false);
            stage.setX(x);
            stage.show();

            x += 200;
        }
    }

    public void refreshShot(int cameraID,String imgPath) {
        LOGGER.debug("ShotFrameManager->refershhot with cameraID="+cameraID+", imgPath="+imgPath);
        for(ShotFrameController shotFrameController: shotframes){
            if(shotFrameController.getFrameID()==cameraID){
                shotFrameController.refreshShot(imgPath);
            }
        }
    }
}
