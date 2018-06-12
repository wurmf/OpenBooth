package org.openbooth.gui;

import org.openbooth.util.ImageHandler;
import org.openbooth.entities.Background;
import org.openbooth.entities.Camera;
import org.openbooth.entities.Position;
import org.openbooth.entities.Profile;
import org.openbooth.service.BackgroundService;
import org.openbooth.service.LogoWatermarkService;
import org.openbooth.service.ProfileService;
import org.openbooth.service.exceptions.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * Controller for Setting Frame .
 */
@Controller
public abstract class SettingFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingFrameController.class);

    protected WindowManager windowManager;
    @Resource
    protected ProfileService pservice;
    @Resource
    protected BackgroundService bservice;
    @Resource
    protected LogoWatermarkService logoService;
    protected ImageHandler imageHandler;

    protected final ObservableList<Profile> profList = FXCollections.observableArrayList();
    protected final ObservableList<Position> posList = FXCollections.observableArrayList();
    protected final ObservableList<Camera> kamList = FXCollections.observableArrayList();
    protected final ObservableList<Profile.PairCameraPosition> kamPosList = FXCollections.observableArrayList();

    protected final ObservableList<Background> backgroundList = FXCollections.observableArrayList();
    protected final ObservableList<Profile.PairLogoRelativeRectangle> logoList = FXCollections.observableArrayList();

    protected final ObservableList<Background.Category> categoryList = FXCollections.observableArrayList();
    protected final ObservableList<Background.Category> categoryListOfProfile = FXCollections.observableArrayList();




    protected final FileChooser fileChooser = new FileChooser();


    protected ObservableList<Profile> selectedProfile = FXCollections.observableArrayList();
    protected Background.Category selectedCategory = null;


    @FXML
    protected GridPane containerGrid;

    @FXML
    protected PositionFrameController positionController;
    @FXML
    protected ProfileFrameController profileController;
    @FXML
    protected LogoFrameController logoController;
    @FXML
    protected GreenscreenBackgroundFrameController greenscreenBackgroundController;
    @FXML
    protected GreenscreenCategoryFrameController greenscreenCategoryController;
    @FXML
    protected CameraPositionFrameController kamPosController;






    protected Profile.PairLogoRelativeRectangle selectedLogo = null;


    @Autowired
    public SettingFrameController(ProfileService pservice, LogoWatermarkService logoService, BackgroundService bservice, WindowManager windowmanager, ImageHandler imageHandler) {
        this.pservice = pservice;
        this.bservice = bservice;
        this.logoService = logoService;
        this.windowManager = windowmanager;
        this.imageHandler = imageHandler;
    }
    
    @FXML
    private void initialize(){
        double screenWidth= Screen.getPrimary().getBounds().getWidth();
        double screenHeight=Screen.getPrimary().getBounds().getHeight();
        containerGrid.setMinHeight(screenHeight-50);
        containerGrid.setPrefWidth(screenWidth);
    }


    protected void refreshTableProfiles(List<Profile> profileList){
        profileController.refreshTableProfiles(profileList);
    }
    protected void refreshLogoAutoComplete(ObservableList<Profile> selectedProfile) throws ServiceException {
        logoController.refreshLogoAutoComplete(selectedProfile);
    }

    protected void refreshTablePosition(List<Position> positionList){
        positionController.refreshTablePosition(positionList, selectedProfile);
    }

    protected void refreshTableKameraPosition(List<Profile.PairCameraPosition> camposList,ObservableList<Position> posList){
            kamPosController.refreshTableKameraPosition(camposList,posList,profileController.getSelectedProfile());
    }
    protected void refreshTableLogo(List<Profile.PairLogoRelativeRectangle> logoList){
        logoController.refreshTableLogo(logoList,profileController.getSelectedProfile());
    }

    protected void refreshCategoryComboBox(List<Background.Category> categories){
        if(greenscreenCategoryController!=null)
            greenscreenBackgroundController.refreshCategoryComboBox(categories,profileController.getSelectedProfile());
    }
    protected void refreshTableCategory(List<Background.Category> categoriesOfProfile,List<Background.Category> categories){
        greenscreenCategoryController.refreshTableCategory(categoriesOfProfile,categories,profileController.getSelectedProfile());
    }
    protected void refreshTableBackground(List<Background> backgrounds){

            greenscreenBackgroundController.refreshTableBackground(backgrounds,profileController.getSelectedProfile(),greenscreenCategoryController.getSelectedCategory());
    }

    protected  void setControllers(){
        greenscreenCategoryController.setControllers(greenscreenBackgroundController);
        positionController.setControllers(kamPosController);
    }





    @FXML
    protected void openMainFrame(){
        windowManager.showScene(WindowManager.SHOW_MAINSCENE);
    }
    protected void showError(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Ungültige Eingabe");
        alert.setContentText(msg);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(windowManager.getStage());
        alert.show();
    }




}
