package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moatzgeile Sau on 04.01.2017.
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

    private void creatButtons(){
        // try {
        /*List<Profile.PairCameraPosition> pairList = profile.getPairCameraPositions();
        if (pairList.isEmpty()) {
            return;
        }
        LOGGER.debug("buttons:"+ buttonList.size() +"");
        int column =(int)((float)pairList.size()/3.0f);
        int width = (int)((float)gridpanel.getWidth()/(float)column)-5;
        int high = (int)((float)gridpanel.getHeight()/3)-7;
        int countrow=0;
        int countcolumn=0;

        for (int i = 0; i < pairList.size(); i++) {
            GridPane gp = new GridPane();
            String name = "Kamera "+pairList.get(i).getCamera().getId()+ "  "  + pairList.get(i).getPosition().getName();
            ImageView imageView = new ImageView();
            imageView.setVisible(true);
            imageView.prefHeight(high);
            imageView.prefWidth(20);

            //imageView.setImage(camera.getFiler);
            //imageView.setImage(new javafx.scene.image.Image(new FileInputStream(pairList.get(i).getCameraLable()), width, high, true, true));
            if(countrow<2){
                countrow++;
            }else {
                countrow=0;
                if(countcolumn<column){
                    countcolumn++;
                }else{
                    LOGGER.debug("not enoth columns"+ column);
                }
            }
            Button filter = new Button();
            filter.setText(name);
            filter.setVisible(true);
            filter.setPrefWidth(width-20);
            filter.setPrefHeight(high);
            String url = pairList.get(i).getCameraLable();
            LOGGER.debug("url costumer: "+url);
            filter.setStyle("-fx-background-image: url('"+url+"'); " +
                    "   -fx-background-size: 100%;" +
                    "   -fx-background-color: transparent;" +
                    "   -fx-font-size:"+ allpicturesview.getFont().getSize()/column+"px;");
            filter.setOnMouseClicked((MouseEvent mouseEvent) -> {

            });
            buttonList.add(filter);
            gp.prefWidth(width);
            gp.prefHeight(high);
            gp.add(filter,0,0);
            gp.add(imageView,1,0);
            grid.add(gp,countcolumn,countrow);
            // Image image = new Image(pairList.get(i).getCameraLable());
        }

        basicpane.add(grid,1,0);
        buttoncreated = true;
       /* } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }


    private void loadButton() {

        grid.setVisible(true);
    }

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
                for (int i = 0; i <greenScreenImages.size() ; i++) {
                    if(columcount==6){
                        rowcount++;
                        columcount=0;
                    }
                    ImageView iv = new ImageView();
                    iv.setFitHeight((scrollPane.getWidth()-scrollPane.getWidth()*0.05)/6);
                    iv.setFitWidth((scrollPane.getWidth()-scrollPane.getWidth()*0.05)/6);
                    iv.setImage(new javafx.scene.image.Image(new FileInputStream(greenScreenImages.get(i).getImagepath()), iv.getFitWidth(), iv.getFitHeight(), true, true));
                    iv.setOnMouseClicked((MouseEvent mouseEvent) -> {
                        //grau überegt
                        chousenimage[index]=iv;

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
            wm.showScene(wm.SHOW_CUSTOMERSCENE);
        } catch (FileNotFoundException e) {
            LOGGER.error("greenScreenButoon:",e);
        }
    }

    public void onBackbuttonpressed() {
        wm.showScene(wm.SHOW_CUSTOMERSCENE);
    }

    public void onsingelPressed() {
        currentMode[index]=0;

    }

    public void onserienPressed() {
        currentMode[index]=1;
    }

    public void ontimerPressed() {
        currentMode[index]=2;
    }

    public int getCurrentMode(){
        return  currentMode[index];
    }

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

    public void currentlychousen(int index, int idFilter, boolean greenscreen){
        this.index=index;
        fId=idFilter;
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
