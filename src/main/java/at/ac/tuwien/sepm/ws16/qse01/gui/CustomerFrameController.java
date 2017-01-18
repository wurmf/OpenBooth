package at.ac.tuwien.sepm.ws16.qse01.gui;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


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

    private WindowManager windowmanager;
    private ShootingService shootingservice;
    private ProfileService profileservice;
    private FilterService filterservice;

    @Autowired
    public CustomerFrameController(FilterService filterservice, WindowManager windowmanager, ShootingService shootingservice, ProfileService profileservice){
        this.windowmanager=windowmanager;
        this.shootingservice=shootingservice;
        this.profileservice=profileservice;
        this.filterservice=filterservice;

        miniLastVisit = false;
        isButtoncreated =false;
        isRefreshed=false;
        filterChouseside =false;
    }

    @FXML
    private void initialize(){
        try {
            leftbutton.setVisible(false);
            if (shootingservice.searchIsActive().getActive()) {
                profile = profileservice.get(shootingservice.searchIsActive().getProfileid());
            }

            if (profile.getId() != shootingservice.searchIsActive().getProfileid()) {
              //  LOGGER.debug("Profile id:" + profile.getId() + "");
                if (profile.getId() != shootingservice.searchIsActive().getProfileid()) {
                    profile = profileservice.get(shootingservice.searchIsActive().getProfileid());
                //    LOGGER.debug("Profile id changed:" + profile.getId() + "");
                }
            }
            if(!profile.isGreenscreenEnabled()&&!profile.isFilerEnabled()){
                rightbutton.setVisible(false);
            }
        } catch (ServiceException e) {
            showInformationDialog("Buttons konnten nicht geladen werden");
            LOGGER.error("initialise:",e);
        } catch (NullPointerException n){
            LOGGER.error("no active shooting:",n);
        }
    }

    public void switchToMiniaturFrame(){

        miniLastVisit = true;

        windowmanager.showScene(WindowManager.SHOW_MINIATURESCENE);
    }

    public void switchToLogin() {

        try {
            windowmanager.showAdminLogin(WindowManager.SHOW_SHOOTINGSCENE, WindowManager.SHOW_CUSTOMERSCENE);
            rightbutton.setVisible(true);
            if (!allpicturesview.isVisible()) {
                if (shootingservice.searchIsActive().getActive()) {
                    profile = profileservice.get(shootingservice.searchIsActive().getProfileid());
                }
                List<Profile.PairCameraPosition> pairList = profile.getPairCameraPositions();
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

    public void refresh (){
        try {
            if(!miniLastVisit) {
                isRefreshed = true;
                if(filterChouseside){
                    basicpane.getChildren().remove(grid);
                    grid = new GridPane();
                   // gridpanel = new GridPane();
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

    public void switchToFilter() {
        try {
            if(shootingservice.searchIsActive().getActive()){
                if(profile.getId()!=shootingservice.searchIsActive().getProfileid()) {
                    profile = profileservice.get(shootingservice.searchIsActive().getProfileid());
                    isButtoncreated=false;
                }
            }
            if(profile.isFilerEnabled()||profile.isGreenscreenEnabled()) {
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

    public void switchToAllImages() {
        rightbutton.setVisible(true);
        allpicturesview.setVisible(true);
        gridpanel.setVisible(true);
        leftbutton.setVisible(false);
        setInvisible();
    }

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
                int width = (int) ((float) gridpanel.getWidth() / (float) (column)) - 5;
                int high = (int) ((float) gridpanel.getHeight() / 3) - 7;
                int countrow = 0;
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
                        String resource = System.getProperty("user.home");
                        if (profileservice.getActiveProfile().getPairCameraPositions().get(i).getFilterName()!=null) {

                            imageView.setImage(SwingFXUtils.toFXImage(filterservice.filter(profileservice.getActiveProfile().getPairCameraPositions().get(i).getFilterName(), System.getProperty("user.dir") + "/src/main/resources/images/studio.jpg"), null));
                        }else {
                            imageView.setImage(new Image("/images/studio.jpg", imageView.getFitHeight(), imageView.getFitWidth(), true, true));
                       }
                    }else{
                        if(profileservice.getActiveProfile().getPairCameraPositions().get(i).getBackground()!=null) {
                            if (profileservice.getActiveProfile().getPairCameraPositions().get(i).getBackground().getPath() != null) {


                                FileInputStream file = new FileInputStream(profileservice.getActiveProfile().getPairCameraPositions().get(i).getBackground().getPath());
                                Image ima = new Image(file, imageView.getFitHeight(), imageView.getFitWidth(), true, true);
                                file.close();
                                imageView.setImage(ima);
                            }else{
                                imageView.setImage(new Image("/images/studio.jpg", imageView.getFitHeight(), imageView.getFitWidth(), true, true));
                            }
                        }else{
                            imageView.setImage(new Image("/images/studio.jpg", imageView.getFitHeight(), imageView.getFitWidth(), true, true));
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

                    iv2.setFitHeight(high/4);
                    iv2.setFitWidth(high/4);
                    imageView.setFitHeight(high/2);
                    imageView.setFitWidth(high/2);

                    //imageView.setBlendMode(BlendMode.DIFFERENCE);

                    Group blend = new Group(
                                imageView,
                                iv2
                        );


                    if (countrow < 2) {
                        countrow++;
                    } else {
                        countrow = 0;
                        if (countcolumn < column) {
                            countcolumn++;
                        } else {
                           LOGGER.debug("not enoth columns" + column);
                        }
                    }
                    Button filter = new Button();
                    filter.setText(name);
                    filter.setVisible(true);
                    filter.setPrefWidth(width - 20);
                    filter.setPrefHeight(high);
                    String url = pairList.get(i).getCameraLable();
                   // LOGGER.debug("url costumer: " + url);
                    filter.setStyle("-fx-background-image: url('" + url + "') " );
                    filter.setStyle("-fx-background-size: 100%" );
                    filter.setStyle("-fx-background-color: transparent" );
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
                    gp.prefWidth(width);
                    gp.prefHeight(high);
                    gp.add(filter, 0, 0);
                    gp.add(blend, 1, 0);
                    grid.add(gp, countcolumn, countrow);
                    // Image image = new Image(pairList.get(i).getCameraLable());
                  //  LOGGER.debug("count calls "+i+"");
                }
                basicpane.add(grid, 1, 0);
                isButtoncreated = true;
            }
       } catch (ServiceException e) {
           LOGGER.error("creatButtons - ",e);
           showInformationDialog(e.getMessage());
        } catch (FileNotFoundException e) {
           LOGGER.error("creatButtons -", e);
       } catch (IOException e) {
           LOGGER.error("creatButtons -", e);
       }
    }

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
}
