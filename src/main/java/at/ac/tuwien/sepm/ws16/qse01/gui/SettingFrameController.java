package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.BackgroundService;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
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
    protected final ObservableList<Background> backgroundList = FXCollections.observableArrayList();
    protected final ObservableList<Profile.PairCameraPosition> kamPosList = FXCollections.observableArrayList();
    protected final ObservableList<Profile.PairLogoRelativeRectangle> logoList = FXCollections.observableArrayList();

    protected final ObservableList<Background.Category> categoryList = FXCollections.observableArrayList();
    protected final ObservableList<Background.Category> categoryListOfProfile = FXCollections.observableArrayList();



    protected final FileChooser fileChooser = new FileChooser();


    protected Profile selectedProfile = null;
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
    public SettingFrameController(ProfileService pservice, LogoWatermarkService logoService, BackgroundService bservice, WindowManager windowmanager, ImageHandler imageHandler) throws ServiceException {
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
    protected void refreshLogoAutoComplete(Profile selectedProfile) throws ServiceException {
        logoController.refreshLogoAutoComplete(selectedProfile);
    }

    protected void refreshTablePosition(List<Position> positionList){
        positionController.refreshTablePosition(positionList,selectedProfile);
    }

    protected void refreshTableKameraPosition(List<Profile.PairCameraPosition> camposList){
        if(kamPosController!=null)
            kamPosController.refreshTableKameraPosition(camposList,positionController.getPosList(),profileController.getSelectedProfile());
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
       // if(greenscreenBackgroundController!=null)
            greenscreenBackgroundController.refreshTableBackground(backgrounds,profileController.getSelectedProfile(),greenscreenCategoryController.getSelectedCategory());
    }

    protected  void setControllers(){
        greenscreenCategoryController.setControllers(greenscreenBackgroundController);
    }





    @FXML
    protected void openMainFrame(){
        LOGGER.info("backButton clicked...");
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
