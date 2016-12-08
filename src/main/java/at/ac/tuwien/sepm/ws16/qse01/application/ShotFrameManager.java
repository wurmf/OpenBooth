package at.ac.tuwien.sepm.ws16.qse01.application;

import at.ac.tuwien.sepm.ws16.qse01.gui.MainFrameController;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ImageServiceImpl;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ShootingServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
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
 * Created by macdnz on 07.12.16.
 */

@Component
public class ShotFrameManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrameController.class);
    private Stage primaryStage;
    private List<ShotFrameController> shotframes;
    @Resource
    private ImageService imageService;

   @Resource
    private ShootingService shootingService;

   /* @Resource
    private CameraService cameraService;*/

    @FXML
    private ImageView shotView;

    @Autowired
    public ShotFrameManager(ImageService imageService,ShootingService shootingService) throws ServiceException {

        this.imageService = imageService;
        this.shootingService = shootingService;
        shotframes = new ArrayList<ShotFrameController>();
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
                Parent root = (Parent) loader.load();
                ShotFrameController shotFrameController = loader.getController();
                shotFrameController.initShotFrame((i+1),imageService,shootingService);
                shotframes.add(shotFrameController);

                stage.setScene(new Scene(root ,400,400));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //stage.initModality(Modality.WINDOW_MODAL);
            stage.setFullScreen(false);
          //  stage.initOwner(primaryStage);
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
    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
}
