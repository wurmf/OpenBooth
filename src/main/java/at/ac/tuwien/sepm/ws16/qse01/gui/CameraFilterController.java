package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.entities.*;
import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.service.*;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.CameraFilterService;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Kamera Filter frame controller
 */
@Component
public class CameraFilterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerFrameController.class);


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
    @FXML
    private ScrollPane filterscrollplanel;


    private GridPane filtergrid;
    private GridPane greengrid;
    private int index;
    private int fId;
    private List buttonList;
    private int currentMode;
    private GridPane grid = new GridPane();
    private ImageView[] chousenimage;
    private Profile profile;
    private ImageView activiv=null;

    private FilterService filterService;
    private ProfileService profileservice;
    private WindowManager wm;
    private ImageService imageService;
    private ShootingService shootingService;
    private CameraFilterService cameraFilterService;



    @Autowired

    public CameraFilterController(CameraFilterService cameraFilterService, FilterService filterService, ProfileService profileService, WindowManager wm, ImageService imageService, ShootingService shootingService ){
        this.profileservice=profileService;
        this.wm=wm;
        this.imageService=imageService;
        this.shootingService = shootingService;
        this.filterService=filterService;
        this.cameraFilterService = cameraFilterService;
    }


    /**
     * inizialises chousenimage and buttonList
     * and findes aktuell profile
     */
    @FXML
    private void initialize(){
        try {
            if(shootingService.searchIsActive().getActive()) {
                buttonList = new ArrayList<>();
                profile = profileservice.get(shootingService.searchIsActive().getProfileid());
                chousenimage = new ImageView[profile.getPairCameraPositions().size()];

            }
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
            Image image1 = null;
            if ( imageService.getAllImages(shootingService.searchIsActive().getId())!=null&&!imageService.getAllImages(shootingService.searchIsActive().getId()).isEmpty() ){
                image1= imageService.getAllImages(shootingService.searchIsActive().getId()).get(1);
            }
            Map<String,BufferedImage> filtermap=null;
            if(image1!=null){
                filtermap = filterService.getAllFilteredImages(image1.getImagepath());
            }else {
                String resource = System.getProperty("user.home");
                filtermap = filterService.getAllFilteredImages(resource+"/images/studio.jpg");
            }
           // List<Image> imlist= imageService.getAllImages(shootingService.searchIsActive().getId());
            for (Map.Entry<String, BufferedImage> filterentety: filtermap.entrySet()) {//imagefilter.size
           // for(Image im : imlist){
                if (columcount == 6) {
                    rowcount++;
                    columcount = 0;
                }
                if(rowcount>=6){
                    filtergrid.getRowConstraints().add(rowcount,new RowConstraints());

                    filtergrid.getRowConstraints().get(rowcount).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
                }
                ImageView iv = new ImageView();
              iv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/6-10);
              iv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/6-10);

                iv.setStyle("-fx-background-color: green;");
                iv.setStyle("-fx-padding: 5;");//imagefilter.get(i).getImagepath()
              iv.setImage(SwingFXUtils.toFXImage(filterentety.getValue(), null));

                if(profile.getPairCameraPositions().get(index).getFilterName()!=null) {
                    if (filterentety.getKey().equals(profile.getPairCameraPositions().get(index).getFilterName())) {

                        activiv = iv;
                        activiv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/6-40);
                        activiv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/6-40);
                    }
                }
              //  iv.setImage(new javafx.scene.image.Image(new FileInputStream(im.getImagepath()), iv.getFitHeight(), iv.getFitWidth(), true, true));
                iv.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    if(activiv!=null){
                        activiv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/6-10);
                        activiv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/6-10);
                        //activiv.setStyle("-fx-background-color: black;");
                       //activiv.setBlendMode(BlendMode.BLUE);
                    }
                    activiv=iv;
                    iv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/6-30);
                    iv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/6-30);
                    //iv.setStyle("-fx-background-color: green;");
                    //iv.setBlendMode(BlendMode.GREEN);
                    profile.getPairCameraPositions().get(index).setFilterName(filterentety.getKey());
                   /* if(fId==){

                    }*/
                });
                filtergrid.add(iv, columcount, rowcount);
                columcount++;
                buttonList.add(iv);
            }
            filtergrid.setVisible(true);
            filterscrollplanel.setVisible(true);
            filterscrollplanel.setContent(filtergrid);
            root.add(filterscrollplanel,0,1);
        } catch (ServiceException e) {
            LOGGER.error("creatButtons - ", e);
            showInformationDialog("Es konnte keine Filterauswahl erstellt werden");
        }
    }

    /**
     * loads the button
     */
    private void loadButton() {

        filtergrid.setVisible(true);
    }

    /**
     * creats image buttons for green screen image and marks chousen one
     */
    private void createGreenscreenButton(){
       /* try {
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
*/
       FileInputStream fips=null;
        try {
            greengrid = new GridPane();
            greengrid.prefWidth(Screen.getPrimary().getBounds().getWidth());
            filterscrollplanel = new ScrollPane();
            greengrid.setStyle("-fx-background-color: black;");
            filterscrollplanel.setStyle("-fx-background-color: black;");
            filterscrollplanel.setFitToWidth(true);
            filterscrollplanel.setFitToHeight(false);
            filterscrollplanel.prefWidth(Screen.getPrimary().getBounds().getWidth());

            greengrid.getColumnConstraints().add(0, new ColumnConstraints());
            greengrid.getRowConstraints().add(0, new RowConstraints());
            greengrid.getColumnConstraints().add(1, new ColumnConstraints());
            greengrid.getRowConstraints().add(1, new RowConstraints());
            greengrid.getColumnConstraints().add(2, new ColumnConstraints());
            greengrid.getRowConstraints().add(2, new RowConstraints());
            greengrid.getColumnConstraints().add(3, new ColumnConstraints());
            greengrid.getRowConstraints().add(3, new RowConstraints());
            greengrid.getColumnConstraints().add(4, new ColumnConstraints());
            greengrid.getRowConstraints().add(4, new RowConstraints());
         //   filtergrid.getColumnConstraints().add(5, new ColumnConstraints());
            greengrid.getRowConstraints().add(5, new RowConstraints());

            greengrid.getColumnConstraints().get(0).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greengrid.getRowConstraints().get(0).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
            greengrid.getColumnConstraints().get(1).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greengrid.getRowConstraints().get(1).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
            greengrid.getColumnConstraints().get(2).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greengrid.getRowConstraints().get(2).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
            greengrid.getColumnConstraints().get(3).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greengrid.getRowConstraints().get(3).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
            greengrid.getColumnConstraints().get(4).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greengrid.getRowConstraints().get(4).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);
           // filtergrid.getColumnConstraints().get(5).setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 4);
            greengrid.getRowConstraints().get(5).setPrefHeight(Screen.getPrimary().getBounds().getWidth() / 6);

            int columcount = 0;
            int rowcount = 0;

            List<Background>  greenList =profileservice.getAllBackgroundOfProfile();
            shootingService.addUserDefinedBackgrounds(greenList);
            // List<Image> imlist= imageService.getAllImages(shootingService.searchIsActive().getId());
            for (Background backround : greenList) {//imagefilter.size
                // for(Image im : imlist){
                if (columcount == 5) {
                    rowcount++;
                    columcount = 0;
                    if(rowcount>=6){
                        greengrid.getRowConstraints().add(rowcount,new RowConstraints());
                        greengrid.getRowConstraints().get(rowcount).setPrefHeight(Screen.getPrimary().getBounds().getWidth()/6);
                    }
                }
               // profileservice.getAllBackgroundOfProfile().get(1).getPath()

                ImageView iv = new ImageView();
                iv.setFitHeight(Screen.getPrimary().getBounds().getWidth() / 5 - 10);
                iv.setFitWidth(Screen.getPrimary().getBounds().getWidth() / 5 - 10);

                //iv.setStyle("-fx-background-color: green;");
                iv.setStyle("-fx-padding: 5;");//imagefilter.get(i).getImagepath()
                fips=new FileInputStream(backround.getPath());
                iv.setImage(new javafx.scene.image.Image(fips, iv.getFitHeight(), iv.getFitWidth(), true, true));
                fips.close();
                if(profile.getPairCameraPositions().get(index).getBackground()!=null) {
                    if (backround.getPath().equals(profile.getPairCameraPositions().get(index).getBackground())) {
                        activiv = iv;
                        activiv.setFitHeight(Screen.getPrimary().getBounds().getWidth()/5-40);
                        activiv.setFitWidth(Screen.getPrimary().getBounds().getWidth()/5-40);
                    }
                }
                //  iv.setImage(new javafx.scene.image.Image(new FileInputStream(im.getImagepath()), iv.getFitHeight(), iv.getFitWidth(), true, true));
                iv.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    if (activiv != null) {
                        activiv.setFitHeight(Screen.getPrimary().getBounds().getWidth() / 5 - 10);
                        activiv.setFitWidth(Screen.getPrimary().getBounds().getWidth() / 5 - 10);
                        //activiv.setStyle("-fx-background-color: black;");
                        //activiv.setBlendMode(BlendMode.BLUE);
                    }
                    activiv = iv;
                    iv.setFitHeight(Screen.getPrimary().getBounds().getWidth() / 5 - 30);
                    iv.setFitWidth(Screen.getPrimary().getBounds().getWidth() / 5 - 30);

                    //iv.setStyle("-fx-background-color: green;");
                    //iv.setBlendMode(BlendMode.GREEN);
                    profile.getPairCameraPositions().get(index).setBackground(backround);

                });
                greengrid.add(iv, columcount, rowcount);
                columcount++;
            }
            greengrid.setVisible(true);
            filterscrollplanel.setVisible(true);
            filterscrollplanel.setContent(greengrid);
            root.add(filterscrollplanel, 0, 1);
        } catch (ServiceException|IOException e) {
            LOGGER.error("Camers Filter, greenscreen creat button- ", e);
        } finally{
            if(fips!=null){
                try {
                    fips.close();
                } catch (IOException e) {
                    LOGGER.debug("createGreenScreenButton - unable to close FileInputStream - ",e);
                }
            }
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
    public void onSinglePressed() {
        unmark();

        profile.getPairCameraPositions().get(index).setShotType(Profile.PairCameraPosition.SHOT_TYPE_SINGLE);
        singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        currentMode = 0;
    }

    /**
     * on serien pictures pressed
     */
    public void onSerialPressed() {
        unmark();

        profile.getPairCameraPositions().get(index).setShotType(Profile.PairCameraPosition.SHOT_TYPE_MULTIPLE);
        serien.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        currentMode =1;
    }

    /**
     * on time image pressed
     */
    public void onTimerPressed() {
        unmark();

        profile.getPairCameraPositions().get(index).setShotType(Profile.PairCameraPosition.SHOT_TYPE_TIMED);

        ontime.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        singel.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("green"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        currentMode=2;
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
    public void currentlyChosen(int index, int idFilter, boolean greenscreen){
        try{

            buttonList = new LinkedList<>();
        this.index=index;
            fId=idFilter;
        titel.setText("");
        greengrid=new GridPane();
        if(filtergrid==null){
            filtergrid = new GridPane();
        }
            if(index>-1) {
                profile = profileservice.getActiveProfile();
                if (profile.getId()!=profileservice.getActiveProfile().getId()){
                    buttonList.clear();
                }
                currentMode=profile.getPairCameraPositions().get(index).getShotType();
                markfirst();
                if (greenscreen) {
                    filtergrid.setVisible(false);
                    greengrid.setVisible(true);
                    titel.setText("Kamera " + profile.getPairCameraPositions().get(index).getPosition().getName() + " Hintergrund auswahl");
                    titel.setVisible(true);
                    createGreenscreenButton();
                } else {
                   // if (buttonList.isEmpty()) {
                        greengrid.setVisible(false);
                        filtergrid.setVisible(true);
                        titel.setText("Kamera " + profile.getPairCameraPositions().get(index).getPosition().getName() + " Filter auswahl");
                        titel.setVisible(true);
                        creatButtons();
                    /*} else {
                        greengrid.setVisible(false);
                        filtergrid.setVisible(true);
                        titel.setText("Kamera " + profile.getPairCameraPositions().get(index).getPosition().getName() + " Filter auswahl");
                        titel.setVisible(true);
                        loadButton();
                    }*/
                }
            }
        } catch (ServiceException e) {
            LOGGER.debug("no camera pair found", e);
            showInformationDialog("Es konnte keine Kamera gefunden werden.");
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
            LOGGER.error("unmark Button",n);
        }
    }

    /**
     * information dialog
     *
     * @param info String to be shown as error message to the user
     */
    public void showInformationDialog(String info){
        Alert information = new Alert(Alert.AlertType.INFORMATION, info);
        information.setHeaderText("Ein Fehler ist Aufgetreten");
        information.show();
    }
}
