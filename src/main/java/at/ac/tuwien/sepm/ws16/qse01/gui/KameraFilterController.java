package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Kamera Filter frame controller
 */
@Component
public class KameraFilterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CostumerFrameController.class);

    @FXML
    private GridPane root;
    @FXML
    private Button singel;
    @FXML
    private Button serien;
    @FXML
    private Button ontime;
    @FXML
    private Label titel;

    int index;
    int fId;
    List buttonList;
    Integer[] currentMode;
    GridPane grid = new GridPane();
    boolean first=false;
    ImageView[] chousenimage;


    private ProfileService profileservice;
    private WindowManager wm;
    private ImageService imageService;
    private ShootingService shootingService;
    @Autowired

    public KameraFilterController(ProfileService profileService, WindowManager wm, ImageService imageService, ShootingService shootingService ){
        this.profileservice=profileService;
        this.wm=wm;
        this.imageService=imageService;
        this.shootingService = shootingService;
    }

    /**
     * creats the filter selection for all kameras
     * marks the chousen filter
     */
    private void creatButtons(){

        try {
            GridPane filter = new GridPane();
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            filter.prefWidth(scrollPane.getWidth() - scrollPane.getWidth() * 0.05);
            int columcount = 0;
            int rowcount = 0;
            for (int i = 0; i < 1; i++) {//imagefilter.size
                if (columcount == 6) {
                    rowcount++;
                    columcount = 0;
                }
                ImageView iv = new ImageView();
                iv.setFitHeight((scrollPane.getWidth() - scrollPane.getWidth() * 0.05) / 6);
                iv.setFitWidth((scrollPane.getWidth() - scrollPane.getWidth() * 0.05) / 6);//imagefilter.get(i).getImagepath()
                iv.setImage(new javafx.scene.image.Image(new FileInputStream(""), iv.getFitWidth(), iv.getFitHeight(), true, true));
                iv.setOnMouseClicked((MouseEvent mouseEvent) -> {
                   /* if(fId==){

                    }*/
                });
                filter.add(iv, columcount, rowcount);
                columcount++;
            }
            scrollPane.setVisible(true);
            scrollPane.setContent(filter);
            grid.add(scrollPane, 0, 0);
            root.add(grid, 0, 1);
        } catch (FileNotFoundException e) {
            LOGGER.error("Filter panel", e.getMessage());
        }
    }


    private void loadButton() {

        grid.setVisible(true);
    }

    /**
     * creats image buttons for green screen image and marks chousen one
     */
    private void creatGreenscreenButton(){
        try {
            if (shootingService.searchIsActive().getActive()) {
                List<Image> greenScreenImages = imageService.getAllImages(shootingService.searchIsActive().getId());
                //greenScreenImages.add();

                GridPane green = new GridPane();
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setFitToWidth(true);
                green.prefWidth(scrollPane.getWidth()-scrollPane.getWidth()*0.05);
                int columcount =0;
                int rowcount =0;
                for (Image greenScreenImage : greenScreenImages) {
                    if (columcount == 6) {
                        rowcount++;
                        columcount = 0;
                    }
                    ImageView iv = new ImageView();
                    iv.setFitHeight((scrollPane.getWidth() - scrollPane.getWidth() * 0.05) / 6);
                    iv.setFitWidth((scrollPane.getWidth() - scrollPane.getWidth() * 0.05) / 6);
                    iv.setImage(new javafx.scene.image.Image(new FileInputStream(greenScreenImage.getImagepath()), iv.getFitWidth(), iv.getFitHeight(), true, true));
                    iv.setOnMouseClicked((MouseEvent mouseEvent) -> {
                        //grau überegt
                        chousenimage[index] = iv;

                    });
                    green.add(iv, columcount, rowcount);
                    columcount++;
                }
                scrollPane.setVisible(true);
                scrollPane.setContent(green);
                root.add(scrollPane,0,1);
            }
        } catch (ServiceException e) {
            LOGGER.error("greenScreenButton:",e);
            wm.showScene(WindowManager.SHOW_CUSTOMERSCENE);
        } catch (FileNotFoundException e) {
            LOGGER.error("greenScreenButoon:",e);
        }
    }

    /**
     * goes back to costumer frame
     */
    public void onBackbuttonpressed() {
        wm.showScene(WindowManager.SHOW_CUSTOMERSCENE);
    }

    /**
     * on single image pressed
     */
    public void onsingelPressed() {
        currentMode[index]=0;

    }

    /**
     * on serien pictures pressed
     */
    public void onserienPressed() {
        currentMode[index]=1;
    }

    /**
     * on time image pressed
     */
    public void ontimerPressed() {
        currentMode[index]=2;
    }

    /**
     * gives currentMode
     * @return current mode (on time, single, serien)
     */
    public int getCurrentMode(){
        return  currentMode[index];
    }

    /**
     * initialisation of the frame
     */
    public void firstVisit() {
        try {
            buttonList = new ArrayList<>();
            currentMode = new Integer[profileservice.getAllPairCameraPositionOfProfile().size()];
            chousenimage = new ImageView[profileservice.getAllPairCameraPositionOfProfile().size()];
            first = true;
        }catch (ServiceException e) {
            LOGGER.debug("no camera pair found");
        }
    }

    /**
     * desides whether an new fiter imge is chousen or green screen
     * @param index current mode
     * @param idFilter current filter id
     * @param greenscreen boolean green screen or not
     */
    public void currentlychousen(int index, int idFilter, boolean greenscreen){
        this.index=index;
        fId=idFilter;
        titel.setText("");
        try {
            if (!first) {
                firstVisit();
            }
            switch (currentMode[index]) {
                case 0:
                    singel.setStyle("-fx-background-color: green;");
                    break;
                case 1:
                    serien.setStyle("-fx-background-color: green;");
                    break;
                case 2:
                    ontime.setStyle("-fx-background-color: green;");
                    singel.setStyle("-fx-background-color: green;");
                    break;
            }
            if (greenscreen) {
                    titel = new Label("Kamera " + profileservice.getAllPairCameraPositionOfProfile().get(index).getCamera().getId() + " Hintergrund auswahl");
                    creatGreenscreenButton();
            } else{
                if (buttonList.isEmpty()) {
                    titel = new Label("Kamera " + profileservice.getAllPairCameraPositionOfProfile().get(index).getCamera().getId() + " Filter auswahl");
                    creatButtons();
                } else {
                    titel = new Label("Kamera " + profileservice.getAllPairCameraPositionOfProfile().get(index).getCamera().getId() + " Filter auswahl");
                    loadButton();
                }
            }
        } catch (ServiceException e) {
            LOGGER.debug("no camera pair found");
        }
    }
}
