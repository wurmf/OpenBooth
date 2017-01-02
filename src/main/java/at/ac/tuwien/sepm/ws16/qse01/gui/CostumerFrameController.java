package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CostumerFrameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CostumerFrameController.class);

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

    private boolean buttoncreated;
    private Profile profile;
    private List<Button> buttonList;
    GridPane grid = new GridPane();

    private WindowManager windowmanager;
    private ShootingService shootingservice;
    private ProfileService profileservice;

    @Autowired
    public CostumerFrameController(WindowManager windowmanager, ShootingService shootingservice, ProfileService profileservice){
        this.windowmanager=windowmanager;
        this.shootingservice=shootingservice;
        this.profileservice=profileservice;
    }

    @FXML
    private void initialize(){
        try {
            buttonList= new ArrayList<>();
            leftbutton.setVisible(false);
            if (shootingservice.searchIsActive().getActive()) {
                profile = profileservice.get(shootingservice.searchIsActive().getProfileid());
            }
        } catch (ServiceException e) {
            showInformationDialog("Buttons konnten nicht geladen werden");
            LOGGER.error("initialise:",e.getMessage());
        }
    }

    public void switchtoMiniaturFrame(){
        windowmanager.showMiniatureFrame();
    }

    public void switchToLogin(ActionEvent actionEvent) {
        windowmanager.showAdminLogin();
    }

    public void switchToFilter(ActionEvent actionEvent) {
        try {
            rightbutton.setVisible(false);
            allpicturesview.setVisible(false);
            gridpanel.setVisible(false);
            leftbutton.setVisible(true);
            if (buttoncreated && profile.getId() == shootingservice.searchIsActive().getProfileid()) {
                loadButton();
            } else {
                LOGGER.debug("Profile id:",profile.getId()+"");
                if(profile.getId() != shootingservice.searchIsActive().getProfileid()){
                    profile= profileservice.get(shootingservice.searchIsActive().getProfileid());
                    LOGGER.debug("Profile id changed:",profile.getId()+"");
                }
                creatButtons();
            }
        }catch (ServiceException e) {
            showInformationDialog("Buttons konnten nicht geladen werden!");
            LOGGER.error("load buttons:",e.getMessage());
        }
    }

    public void switchToAllImages(ActionEvent actionEvent) {
        rightbutton.setVisible(true);
        allpicturesview.setVisible(true);
        gridpanel.setVisible(true);
        leftbutton.setVisible(false);
        setInvisible();
    }

    private void creatButtons(){
       // try {
            List<Profile.PairCameraPosition> pairList = profile.getPairCameraPositions();
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
        information.show();
    }
}
