package org.openbooth.gui;

import org.openbooth.camera.CameraTrigger;
import org.openbooth.entities.Profile;
import org.openbooth.service.FilterService;
import org.openbooth.service.ProfileService;
import org.openbooth.service.ShootingService;
import org.openbooth.service.exceptions.ServiceException;
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
import org.openbooth.util.exceptions.TriggerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
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
    private CameraTrigger cameraTrigger;

    @Autowired
    public CustomerFrameController(FilterService filterservice, WindowManager windowmanager, ShootingService shootingservice, ProfileService profileservice, CameraTrigger cameraTrigger){
        this.windowmanager=windowmanager;
        this.shootingservice=shootingservice;
        this.profileservice=profileservice;
        this.filterservice=filterservice;
        this.cameraTrigger=cameraTrigger;

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
                if (shootingservice.searchIsActive().isActive()) {
                    profile = profileservice.get(shootingservice.searchIsActive().getProfileid());
                }
                List<Profile.PairCameraPosition> pairList = profileservice.getAllPairCameraPositionOfProfile();
                if (pairList == null || pairList.isEmpty()) {
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
                    createButtons();
                }
                filterChouseside = false;
            }
            List<Profile.PairCameraPosition> pcps = profileservice.getActiveProfile().getPairCameraPositions();
            if (pcps == null || pcps.isEmpty()) {
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
            if(shootingservice.searchIsActive().isActive()){
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
                    createButtons();
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
    private void createButtons(){
       try {

            if (shootingservice.searchIsActive().isActive()) {
                profile = profileservice.get(shootingservice.searchIsActive().getProfileid());
            }
            List<Profile.PairCameraPosition> pairList = profile.getPairCameraPositions();
            if (pairList==null||pairList.isEmpty()) {
                rightbutton.setVisible(false);
            }else {

                int column = (int) ((float) pairList.size() / 3.0f);
                int width;
                if(pairList.size()>3){
                     width = (int) ((float) gridpanel.getWidth() / 6) - 10;
                }else if(pairList.size()>2){
                    width = (int) ((float) gridpanel.getWidth() / 5) - 10;
                }else{
                    width = (int) ((float) gridpanel.getWidth()) - 10;
                }
                int height = (int) ((float) gridpanel.getWidth() / 6) - 7;
                int countrow = 1;
                int countcolumn = 0;
                grid = new GridPane();

                for (int i = 0; i < pairList.size(); i++) {
                    GridPane gp = new GridPane();
                    String name = "Kamera " + pairList.get(i).getCamera().getId() + "  " + pairList.get(i).getPosition().getName();

                    int shot =profileservice.getActiveProfile().getPairCameraPositions().get(i).getShotType();


                    ImageView imageView = new ImageView();
                    imageView.setVisible(true);
                    imageView.prefHeight(height);
                    imageView.prefWidth(20);
                    if(!pairList.get(i).isGreenScreenReady()){

                        if(profileservice.getActiveProfile().getPairCameraPositions().get(i).getFilterName()==null|| profileservice.getActiveProfile().getPairCameraPositions().get(i).getFilterName().equals("")){
                            imageView.setImage(new Image("/images/filterPreview.png", imageView.getFitHeight(), imageView.getFitWidth(), true, true));

                        }else {
                            imageView.setImage(SwingFXUtils.toFXImage(filterList.get(profileservice.getActiveProfile().getPairCameraPositions().get(i).getFilterName()), null));

                        }
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
                            top.setPrefHeight(height/2);
                            top.setStyle("-fx-background-color: transparent");
                            grid.add(top, countcolumn,0);
                        }
                        countrow=countrow+2;
                    }else{
                        if(countcolumn<2){
                            countcolumn=+2;
                            if(countrow==1){
                                GridPane top = new GridPane();
                                top.setPrefHeight(height/4);
                                top.setStyle("-fx-background-color: transparent");
                                grid.add(top, countcolumn,0);
                            }
                        }else{
                            countrow++;
                            countcolumn=+2;
                        }
                    }

                    Button filter = new Button();
                    filter.setText(name);
                    filter.setVisible(true);
                    filter.setPrefWidth(width - height/2);
                    filter.setPrefHeight(height/2);

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


                    iv2.setFitWidth(height/4);
                    iv2.setFitHeight(height/4);
                    imageView.setFitWidth(height/1.5);
                    imageView.setFitHeight(height/1.5);


                    Group blend = new Group(
                            imageView,
                            iv2
                    );


                    gp.prefWidth(width);
                    gp.prefHeight(height);


                    String url = "file:" + pairList.get(i).getPosition().getButtonImagePath();
                    ImageView buttonimage = new ImageView(new Image(url));
                    buttonimage.setFitWidth(width-height/2);
                    buttonimage.setFitHeight(height/1.5);
                    gp.add(buttonimage,0,0);

                    gp.add(filter, 0, 0);
                    gp.add(blend,1,0);
                    grid.add(gp, countcolumn, countrow);
                    GridPane fill = new GridPane();
                    fill.setPrefHeight(height/2);
                    fill.setStyle("-fx-background-color: transparent");
                    if(pairList.size()>3) {
                        GridPane fillside = new GridPane();
                        fill.setPrefWidth(20);
                        fill.setStyle("-fx-background-color: transparent");
                        grid.add(fillside,countcolumn+1, countrow);
                    }
                    grid.add(fill, countcolumn,countrow+1);
                }
                basicpane.add(grid, 1, 0);
                isButtoncreated = true;
            }
       } catch (ServiceException e) {
           LOGGER.error("createButtons - ",e);
           showInformationDialog(e.getMessage());
       }catch (IOException e) {
           LOGGER.error("createButtons -", e);
       }
    }

    /**
     * if no changes occurred the buttons can be loaded
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
    private void showInformationDialog(String info){
        Alert information = new Alert(Alert.AlertType.INFORMATION, info);
        information.setHeaderText("Ein Fehler ist Aufgetreten");

        information.initOwner(windowmanager.getStage());
        information.show();
    }

    public void triggerShot(KeyEvent keyEvent){

        try {
            this.cameraTrigger.triggerShotIfCorrectKey(keyEvent);
        } catch (TriggerException e) {
            //TODO: add error message for the user
            LOGGER.error("Unable to take shot.",e);
        }

    }

}
