package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.FilterService;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.KameraFilterService;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Kamera Filter frame controller
 */
@Component
public class KameraFilterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CostumerFrameController.class);
    public ScrollPane filterscrollplanel;
    private GridPane filtergrid;

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

    private int index;
    private int fId;
    private List buttonList;
    private int currentMode;
    private GridPane grid = new GridPane();
    private ImageView[] chousenimage;
    private Profile profile;
    private ImageView activiv=null;
    private javafx.scene.image.Image activeim=null;
    private ImageView oldactive=null;
    private javafx.scene.image.Image oldactivim=null;

    private FilterService filterService;
    private ProfileService profileservice;
    private WindowManager wm;
    private ImageService imageService;
    private ShootingService shootingService;
    private KameraFilterService kameraFilterService;

    @Autowired

    public KameraFilterController(KameraFilterService kameraFilterService, FilterService filterService, ProfileService profileService, WindowManager wm, ImageService imageService, ShootingService shootingService ){
        this.profileservice=profileService;
        this.wm=wm;
        this.imageService=imageService;
        this.shootingService = shootingService;
        this.filterService=filterService;
        this.kameraFilterService = kameraFilterService;
    }


    /**
     * inizialises chousenimage and buttonList
     * and findes aktuell profile
     */
    @FXML
    private void initialize(){
        try {
            buttonList = new ArrayList<>();
            profile = profileservice.get(shootingService.searchIsActive().getProfileid());
            chousenimage = new ImageView[profile.getPairCameraPositions().size()];

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    /**
     * creats the filter selection for all kameras
     * marks the chousen filter
     */
    private void creatButtons(){

        try {
            filtergrid = new GridPane();
            filtergrid.prefWidth(Screen.getPrimary().getBounds().getWidth());
            filterscrollplanel= new ScrollPane();
            filtergrid.setStyle("-fx-background-color: black;");
            filterscrollplanel.setStyle("-fx-background-color: black;");
            filterscrollplanel.setFitToWidth(true);
            filterscrollplanel.setFitToHeight(false);
            filterscrollplanel.prefWidth(Screen.getPrimary().getBounds().getWidth());

            filtergrid.getColumnConstraints().add(0,new ColumnConstraints());
            filtergrid.getRowConstraints().add(0,new RowConstraints());
            filtergrid.getColumnConstraints().add(1,new ColumnConstraints());
            filtergrid.getRowConstraints().add(1,new RowConstraints());
            filtergrid.getColumnConstraints().add(2,new ColumnConstraints());
            filtergrid.getRowConstraints().add(2,new RowConstraints());
            filtergrid.getColumnConstraints().add(3,new ColumnConstraints());
            filtergrid.getRowConstraints().add(3,new RowConstraints());
            filtergrid.getColumnConstraints().add(4,new ColumnConstraints());
            filtergrid.getRowConstraints().add(4,new RowConstraints());
            filtergrid.getColumnConstraints().add(5,new ColumnConstraints());
            filtergrid.getRowConstraints().add(5,new RowConstraints());

            filtergrid.getColumnConstraints().get(0).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getRowConstraints().get(0).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getColumnConstraints().get(1).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getRowConstraints().get(1).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getColumnConstraints().get(2).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getRowConstraints().get(2).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getColumnConstraints().get(3).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getRowConstraints().get(3).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getColumnConstraints().get(4).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getRowConstraints().get(4).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getColumnConstraints().get(5).setPrefWidth(Screen.getPrimary().getBounds().getWidth()/6);
            filtergrid.getRowConstraints().get(5).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);

            int columcount = 0;
            int rowcount = 0;

           Map<String,BufferedImage> filtermap = filterService.getAllFilteredImages("/images/dummies/p1.jpg");
            //List<Image> imlist= imageService.getAllImages(shootingService.searchIsActive().getId());
            for (Map.Entry<String, BufferedImage> filterentety: filtermap.entrySet()) {//imagefilter.size
           // for(Image im : imlist){
                if (columcount == 6) {
                    rowcount++;
                    columcount = 0;
                }
                ImageView iv = new ImageView();
              iv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/6-10);
              iv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/6-10);//imagefilter.get(i).getImagepath()
              iv.setImage(SwingFXUtils.toFXImage(filterentety.getValue(), null));
              //  iv.setImage(new javafx.scene.image.Image(new FileInputStream(im.getImagepath()), iv.getFitHeight(), iv.getFitWidth(), true, true));
                iv.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    if(activiv!=null&& activeim!=null){
                       activiv.setImage(activeim);
                    }
                    activiv=iv;
                    activeim=iv.getImage();
                    iv.setBlendMode(BlendMode.GREEN);

                   /* if(fId==){

                    }*/
                });
                filtergrid.add(iv, columcount, rowcount);
                columcount++;
            }
            filtergrid.setVisible(true);
            filterscrollplanel.setVisible(true);
            filterscrollplanel.setContent(filtergrid);
            root.add(filterscrollplanel,0,1);
        } catch (ServiceException e) {
            LOGGER.error("CreatButtons", e);
        }
    }

    /**
     * loads the button
     */
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
        unmark();

        singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        currentMode = 0;
        kameraFilterService.setcurrent(index,currentMode);

    }

    /**
     * on serien pictures pressed
     */
    public void onserienPressed() {
        unmark();

        serien.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        currentMode =1;
        kameraFilterService.setcurrent(index,currentMode);
    }

    /**
     * on time image pressed
     */
    public void ontimerPressed() {
        unmark();

        ontime.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        currentMode=2;
        kameraFilterService.setcurrent(index,currentMode);
    }

    /**
     * gives currentMode
     * @return current mode (on time, single, serien)
     */
    public int getCurrentMode(){
        return  currentMode;
    }

    /**
     * decides whether an new filter image is chosen or green screen
     * @param index current mode
     * @param idFilter current filter id
     * @param greenscreen boolean green screen or not
     */
    public void currentlychousen(int index, int idFilter, boolean greenscreen){

        this.index=index;
        currentMode=kameraFilterService.getcurrent(index);
        fId=idFilter;
        titel.setText("");
        try {
            if(index>-1) {
                if (profile != profileservice.get(shootingService.searchIsActive().getProfileid())) {
                    profile = profileservice.get(shootingService.searchIsActive().getProfileid());

                }
                markfirst();
                if (greenscreen) {
                    titel.setText("Kamera " + profile.getPairCameraPositions().get(index).getPosition().getName() + " Hintergrund auswahl");
                    titel.setVisible(true);
                    creatGreenscreenButton();
                } else {
                    if (buttonList.isEmpty()) {
                        titel.setText("Kamera " +  profile.getPairCameraPositions().get(index).getPosition().getName()  + " Filter auswahl");
                        titel.setVisible(true);
                        creatButtons();
                    } else {
                        titel.setText("Kamera " +  profile.getPairCameraPositions().get(index).getPosition().getName()  + " Filter auswahl");
                        titel.setVisible(true);
                        loadButton();
                    }
                }


            }
        } catch (ServiceException e) {
            LOGGER.debug("no camera pair found", e);
        }
    }


    /**
     * markes the currently used item in gui
     */
    private void markfirst(){
        switch (currentMode) {
            case 0:
                singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                ontime.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                serien.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                break;
            case 1:
                serien.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                ontime.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                break;
            case 2:
                ontime.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                serien.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                break;
        }
    }

    /**
     * unmarks the old model
     * so the new model can be marked
     */
    private void unmark(){
        try {
            int i= currentMode;
            switch (i) {
                case 0:
                    singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                    break;
                case 1:
                    serien.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                    break;
                case 2:
                    ontime.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                    singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("transparent"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                    break;
            }
        }catch (NullPointerException n){
            LOGGER.error("unmark ",n);
        }
    }
}
