package at.ac.tuwien.sepm.ws16.qse01.application;

import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.gui.MainFrameController;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.CameraService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    private boolean isClosed=false;
    @Resource
    private ProfileService profileService;
    @Resource
    private ShootingService shootingService;
    @Resource
    private CameraService cameraService;


    @Autowired
    public ShotFrameManager(ProfileService profileService, ShootingService shootingService, CameraService cameraService) throws ServiceException {
        this.profileService = profileService;
        this.shootingService = shootingService;
        this.cameraService = cameraService;
        shotframes = new ArrayList<>();
        shotStages = new ArrayList<>();
    }
    public void init(){

         /* Creating shotFrame */
        List<Camera> cameraList=null;
        int anz = 1;
        try {
             cameraList=cameraService.getActiveCameras();
            anz += cameraList.size();
        } catch (ServiceException e) {
           LOGGER.debug("Fehler: die anzahl der kameras konnte nicht gelesen werden");
        }
        int x = 200;
        for(int i=1; i<anz; i++) { // Anzahl der Kameras...
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
        LOGGER.debug("ShotFrameManager->refershhot with cameraID="+cameraID+", imgPath="+imgPath);
        for(ShotFrameController shotFrameController: shotframes){
            if(shotFrameController.getFrameID()==cameraID){
                shotFrameController.refreshShot("file:" + imgPath);
            }
        }
    }
    public void closeFrames(){
        isClosed=true;
        for(Stage stage: shotStages)
            stage.close();
    }

    public boolean isClosed() {
        return isClosed;
    }
}
