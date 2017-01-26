package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.FilterService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class CustomerFrameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerFrameController.class);

    @FXML
    private GridPane basicpane;
    @FXML
    private GridPane gridpanel;
    @FXML
    private Button allpicturesview;
    @FXML
    private Button rightbutton;
    @FXML
    private Button leftbutton;

    private Profile profile;
    private GridPane grid = new GridPane();
    private Profile profileold =null;
    private boolean isButtoncreated;
    private boolean isRefreshed;
    private boolean miniLastVisit;
    private boolean filterChouseside;
    private Map<String, BufferedImage> filterList;

    private WindowManager windowmanager;
    private ShootingService shootingservice;
    private ProfileService profileservice;
    private FilterService filterservice;
    private CameraHandler cameraHandler;

    @Autowired
    public CustomerFrameController(FilterService filterservice, WindowManager windowmanager, ShootingService shootingservice, ProfileService profileservice, CameraHandler cameraHandler){
        this.windowmanager=windowmanager;
        this.shootingservice=shootingservice;
        this.profileservice=profileservice;
        this.filterservice=filterservice;
        this.cameraHandler=cameraHandler;

        miniLastVisit = false;
        isButtoncreated =false;
        isRefreshed=false;
        filterChouseside =false;
        filterList= new HashMap<>();
    }

    @FXML
    private void initialize(){

            leftbutton.setVisible(false);
    }

    /**
     * switches to miniatur frame when pressed
     */
    public void switchToMiniaturFrame(){

        miniLastVisit = true;

        windowmanager.showScene(WindowManager.SHOW_MINIATURESCENE);
    }

    /**
     * switch to login when pressed
     */
    public void switchToLogin() {

        try {
            windowmanager.showAdminLogin(WindowManager.SHOW_SHOOTINGSCENE, WindowManager.SHOW_CUSTOMERSCENE);
            rightbutton.setVisible(true);
            if (!allpicturesview.isVisible()) {
                if (shootingservice.searchIsActive().getActive()) {
                    profile = profileservice.get(shootingservice.searchIsActive().getProfileid());
                }
                List<Profile.PairCameraPosition> pairList = profileservice.getAllPairCameraPositionOfProfile();
                if (pairList.isEmpty() || pairList.size() == 0) {
                    rightbutton.setVisible(false);
                }
                allpicturesview.setVisible(true);
                gridpanel.setVisible(true);
                leftbutton.setVisible(false);
                setInvisible();
            }
        } catch (ServiceException e) {
            LOGGER.error("switchToLogin",e);
        }
}

    /**
     * decides if something has chanced and adapts chances
     */
    public void refresh (){
        try {
            if(!miniLastVisit) {
                isRefreshed = true;
                if(filterChouseside){
                    basicpane.getChildren().remove(grid);
                    grid = new GridPane();
                    creatButtons();
                }
                filterChouseside = false;
            }
            if (profileservice.getActiveProfile().getPairCameraPositions().isEmpty() || profileservice.getActiveProfile().getPairCameraPositions().size() == 0) {
                rightbutton.setVisible(false);
            }
            miniLastVisit=false;
        } catch (ServiceException e) {
            LOGGER.error("refresh -", e);
        }
    }


    /**
     * if right button gets pressed
     * the controller loads the filter buttons and there  pictures
     */
    public void switchToFilter() {
        try {
            if(shootingservice.searchIsActive().getActive()){
                if(profile!=null) {
                    if ( profile.getId() != shootingservice.searchIsActive().getProfileid()) {
                        profile = profileservice.get(shootingservice.searchIsActive().getProfileid());
                        isButtoncreated = false;
                        filterList.clear();
                    }
                }else{
                    profile = profileservice.get(shootingservice.searchIsActive().getProfileid());
                    isButtoncreated = false;
                    filterList.clear();
                }
            }

            if(filterList.isEmpty()){
                try {
                    String filterPreviewImagePath = windowmanager.copyResource("/images/filterPreview.png");
                    filterList = filterservice.getAllFilteredImages(filterPreviewImagePath);
                } catch (IOException e) {
                    LOGGER.error("switchToFilter - could not copy preview filter image", e);
                }
            }
            if(profileservice.getActiveProfile().isFilerEnabled()||profileservice.getActiveProfile().isGreenscreenEnabled()) {
                rightbutton.setVisible(false);
                allpicturesview.setVisible(false);
                gridpanel.setVisible(false);
                leftbutton.setVisible(true);
                if (!isRefreshed&&profileold != null && isButtoncreated && profileold.getId() == shootingservice.searchIsActive().getProfileid()) {
                    loadButton();
                } else {
                    isRefreshed=false;
                    if (profile.getId() != shootingservice.searchIsActive().getProfileid()) {
                        profile = profileservice.get(shootingservice.searchIsActive().getProfileid());
                    }
                    profileold = profile;
                    creatButtons();
                }
            }else {

                // go over window manager!!!!!???
                rightbutton.setVisible(false);
            }
        }catch (ServiceException e) {
            showInformationDialog("Buttons konnten nicht geladen werden!");
            LOGGER.error("load buttons:",e);
        }catch (NullPointerException n){
            LOGGER.error("active shooting:",n);
        }
    }

    /**
     * sets gridpane containing filterbuttons to invisible and
     * button to switch to miniaturframe visible
     */
    public void switchToAllImages() {
        rightbutton.setVisible(true);
        allpicturesview.setVisible(true);
        gridpanel.setVisible(true);
        leftbutton.setVisible(false);
        setInvisible();
    }

    /**
     * if a change oared or a new profile got installed
     * the filter buttons get created
     */
    private void creatButtons(){
       try {

            if (shootingservice.searchIsActive().getActive()) {
                profile = profileservice.get(shootingservice.searchIsActive().getProfileid());
            }
            List<Profile.PairCameraPosition> pairList = profile.getPairCameraPositions();
            if (pairList==null||pairList.isEmpty()) {
                rightbutton.setVisible(false);
            }else {

              //  LOGGER.debug("buttons:" + buttonList.size() + "");
               // LOGGER.debug("pair:"+pairList.size()+"");
                int column = (int) ((float) pairList.size() / 3.0f);
                int width;
                if(pairList.size()>3){
                     width = (int) ((float) gridpanel.getWidth() / 6) - 10;
                }else if(pairList.size()>2){
                    width = (int) ((float) gridpanel.getWidth() / 5) - 10;
                }else{
                    width = (int) ((float) gridpanel.getWidth()) - 10;
                }
                int high = (int) ((float) gridpanel.getWidth() / 6) - 7;
                int countrow = 1;
                int countcolumn = 0;
                grid = new GridPane();

                for (int i = 0; i < pairList.size(); i++) {
                    GridPane gp = new GridPane();
                    String name = "Kamera " + pairList.get(i).getCamera().getId() + "  " + pairList.get(i).getPosition().getName();

                    int shot =profileservice.getActiveProfile().getPairCameraPositions().get(i).getShotType();


                    ImageView imageView = new ImageView();
                    imageView.setVisible(true);
                    imageView.prefHeight(high);
                    imageView.prefWidth(20);
                    if(!pairList.get(i).isGreenScreenReady()){
                       // if (profileservice.getActiveProfile().getPairCameraPositions().get(i).getFilterName()!=null) {

                        if(profileservice.getActiveProfile().getPairCameraPositions().get(i).getFilterName()==null|| profileservice.getActiveProfile().getPairCameraPositions().get(i).getFilterName().equals("")){
                            imageView.setImage(new Image("/images/filterPreview.png", imageView.getFitHeight(), imageView.getFitWidth(), true, true));

                        }else {
                            imageView.setImage(SwingFXUtils.toFXImage(filterList.get(profileservice.getActiveProfile().getPairCameraPositions().get(i).getFilterName()), null));

                        } //}else {
                         //   imageView.setImage(new Image("/images/studio.jpg", imageView.getFitHeight(), imageView.getFitWidth(), true, true));
                     //  }
                    }else{
                        if(profileservice.getActiveProfile().getPairCameraPositions().get(i).getBackground()!=null) {
                            if (profileservice.getActiveProfile().getPairCameraPositions().get(i).getBackground().getPath() != null) {


                                FileInputStream file = new FileInputStream(profileservice.getActiveProfile().getPairCameraPositions().get(i).getBackground().getPath());
                                Image ima = new Image(file, imageView.getFitHeight(), imageView.getFitWidth(), true, true);
                                file.close();
                                imageView.setImage(ima);
                            }else{
                                imageView.setImage(new Image("/images/backrounddefault.png", imageView.getFitHeight(), imageView.getFitWidth(), true, true));
                            }
                        }else{
                            imageView.setImage(new Image("/images/backrounddefault.png", imageView.getFitHeight(), imageView.getFitWidth(), true, true));
                        }
                    }

                    Image i2;
                    if(shot==0){
                       i2= new Image("/images/singleshot.png");
                    }else if(shot == 1){
                        i2= new Image("/images/multishot.png");
                    }else {
                        i2= new Image("/images/timer.png");
                    }
                    ImageView iv2 = new ImageView();
                    iv2.setImage(i2);


                    if(pairList.size()<=3){
                        if(countrow==1){
                            GridPane top = new GridPane();
                            top.setPrefHeight(high/2);
                            top.setStyle("-fx-background-color: transparent");
                            grid.add(top, countcolumn,0);
                        }
                        countrow=countrow+2;
                    }else{
                        if(countcolumn<2){
                            countcolumn=+2;
                            if(countrow==1){
                                GridPane top = new GridPane();
                                top.setPrefHeight(high/4);
                                top.setStyle("-fx-background-color: transparent");
                                grid.add(top, countcolumn,0);
                            }
                        }else{
                            countrow++;
                            countcolumn=+2;
                        }
                    }
                  /*  if (countrow < 2) {
                        countrow++;
                    } else {
                        countrow = 0;
                        if (countcolumn < column) {
                            countcolumn++;
                        } else {
                           LOGGER.debug("not enoth columns" + column);
                        }
                    }*/
                    Button filter = new Button();
                    filter.setText(name);
                    //filter.setStyle("-fx-background-color: GRAY");
                    filter.setVisible(true);
                    filter.setPrefWidth(width - high/2);
                    filter.setPrefHeight(high/2);

                    double size;
                    if(column==0){
                        size =allpicturesview.getFont().getSize();
                    }else{
                        size= (int)(allpicturesview.getFont().getSize() / column);
                    }
                    filter.setStyle("-fx-font-size:" + size + "px " );

                    final int index = i;
                    filter.setOnMouseClicked((MouseEvent mouseEvent) -> {
                        //kamera Filter controller fiter id
                        filterChouseside =true;
                        windowmanager.showKameraFilterSceen(index,1,pairList.get(index).isGreenScreenReady());
                    });


                    iv2.setFitWidth(high/4);
                    iv2.setFitHeight(high/4);
                    imageView.setFitWidth(high/1.5);
                    imageView.setFitHeight(high/1.5);

                    //imageView.setBlendMode(BlendMode.DIFFERENCE);

                    Group blend = new Group(
                            imageView,
                            iv2
                    );


                    gp.prefWidth(width);
                    gp.prefHeight(high);


                    String url = "file:" + pairList.get(i).getPosition().getButtonImagePath();
                    ImageView buttonimage = new ImageView(new Image(url));
                    buttonimage.setFitWidth(width-high/2);
                    buttonimage.setFitHeight(high/1.5);
                    gp.add(buttonimage,0,0);

                    gp.add(filter, 0, 0);
                    gp.add(blend,1,0);
                    //gp.add(iv2, 1, 0);
                    //gp.add(imageView,2,0);
                    grid.add(gp, countcolumn, countrow);
                    GridPane fill = new GridPane();
                    fill.setPrefHeight(high/2);
                    fill.setStyle("-fx-background-color: transparent");
                    if(pairList.size()>3) {
                        GridPane fillside = new GridPane();
                        fill.setPrefWidth(20);
                        fill.setStyle("-fx-background-color: transparent");
                        grid.add(fillside,countcolumn+1, countrow);
                    }
                    grid.add(fill, countcolumn,countrow+1);
                    // Image image = new Image(pairList.get(i).getCameraLable());
                  //  LOGGER.debug("count calls "+i+"");
                }
                basicpane.add(grid, 1, 0);
                isButtoncreated = true;
            }
       } catch (ServiceException e) {
           LOGGER.error("creatButtons - ",e);
           showInformationDialog(e.getMessage());
       }catch (IOException e) {
           LOGGER.error("creatButtons -", e);
       }
    }

    /**
     * if no chanches ocured the buttons can be loaded
     */
    private void loadButton() {

        grid.setVisible(true);
    }

    private void setInvisible(){
        grid.setVisible(false);
    }

    /**
     * information dialog
     *
     * @param info String to be shown as error message to the user
     */
    public void showInformationDialog(String info){
        Alert information = new Alert(Alert.AlertType.INFORMATION, info);
        information.setHeaderText("Ein Fehler ist Aufgetreten");

        information.initOwner(windowmanager.getStage());
        information.show();
    }

    public void triggerShot(KeyEvent keyEvent){
        String keystoke = keyEvent.getText();

        int index = -1;
        String messageString = "";

        switch (keystoke){
            case "1" : index = 0;break;
            case "2" : index = 1;break;
            case "3" : index = 2;break;
            case "4" : index = 3;break;
            case "5" : index = 4;break;
            case "6" : index = 5;break;
            case "7" : index = 6;break;
            case "8" : index = 7;break;
            case "9" : index = 8;break;
            default: index = -1;return;
        }
        LOGGER.debug("triggerShot with keyEventCharacter " + keystoke);
        int numberOfPositions = 0;
        int numberOfCameras = 0;
        Profile.PairCameraPosition pairCameraPosition = null;
        Profile activeProfile = null;

        List<Camera> cameras = new ArrayList<>();
        try {
            if (profileservice != null) {activeProfile = profileservice.getActiveProfile();
                numberOfPositions = activeProfile.getPairCameraPositions().size();}
        } catch (ServiceException e) {
            activeProfile = null;
            LOGGER.error("Active Profile couldn't be determined, thus null value will be assumed", e);
        }
        String os = System.getProperty("os.name");
        try {
            if (cameraHandler != null && !os.startsWith("Windows")) {cameras = cameraHandler.getCameras();numberOfCameras = cameras.size();}
        } catch (CameraException e) {
            cameras = new ArrayList<>();
            LOGGER.error("Cameras couldn't be determined, thus an empty List will be assumed", e);
        }


        if(index >= 0)
        {messageString = "triggerCall - Attempting to trigger camera object at paitcameraposition list index " + index + " because of valid trigger sequence{}";}
        else
        {messageString = "triggerCall - No action is attempted to be triggered associated to trigger sequence{}";}
        LOGGER.debug(messageString,keystoke);

        if( numberOfPositions > index && index >= 0 ){
            messageString = "triggerCall - Camera is at this index present and an image capture is triggered";
            //cameraHandler.captureImage(cameras.get(cameraIndex));
            pairCameraPosition = activeProfile.getPairCameraPositions().get(index);
            int shotType = pairCameraPosition.getShotType();
            Camera camera = pairCameraPosition.getCamera();
            if (shotType == Profile.PairCameraPosition.SHOT_TYPE_MULTIPLE){
                if (cameras.contains(camera)) {
                    cameraHandler.setSerieShot(camera,true);
                    LOGGER.debug("triggerCall - multiple shot has been set");
                }
                else {LOGGER.debug("triggerCall - multiple shot setting not possible, cause no cameraHandler available");}
                }
            else if (shotType == Profile.PairCameraPosition.SHOT_TYPE_TIMED) {
                if (cameras.contains(camera)) {
                    cameraHandler.setCountdown(camera,8);
                    LOGGER.debug("triggerCall - timed shot has been set");
                } else {
                    LOGGER.debug("triggerCall - timed shot setting not possible, cause no cameraHandler available");
                }
            } else {
                cameraHandler.setCountdown(camera, 0);
                cameraHandler.setSerieShot(camera, false);
                LOGGER.debug("triggerCall - standard shot will be kept set");
            }

            if (cameras.contains(camera)){
                cameraHandler.captureImage(camera);
            return;
            }
            else {
                LOGGER.debug("triggerCall - Camera that has been triggered is not in cameraHandlers list");
                return;
                }
        }
        else if(index >= 0){
                messageString = "triggerCall - No camera at this index found, so no action will be triggered";
        }
        else {
            messageString = "triggerCall - Trigger sequence is invalid";
        }
        LOGGER.debug(messageString);
    }
}
