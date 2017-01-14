package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.entities.Logo;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.AutoCompleteTextField;
import at.ac.tuwien.sepm.ws16.qse01.service.BackgroundService;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    protected final ObservableList<Profile> profList = FXCollections.observableArrayList();
    protected final ObservableList<Position> posList = FXCollections.observableArrayList();
    protected final ObservableList<Background> backgroundList = FXCollections.observableArrayList();
    protected final ObservableList<Profile.PairCameraPosition> kamPosList = FXCollections.observableArrayList();
    protected final ObservableList<Profile.PairLogoRelativeRectangle> logoList = FXCollections.observableArrayList();



    protected final FileChooser fileChooser = new FileChooser();




    @FXML
    protected TableView tableBackground;
    @FXML
    protected TableView tablePosition;
    @FXML
    protected TableView tableLogo;
    @FXML
    protected TableView tableProfil;
    @FXML
    protected TableView tableKamPos;

    @FXML
    protected AutoCompleteTextField txLogoName;
    @FXML
    protected TextField txLogoLogo;




    protected Profile.PairLogoRelativeRectangle selectedLogo = null;
    protected Profile selectedProfile = null;

    @Autowired
    public SettingFrameController(ProfileService pservice,LogoWatermarkService logoService,BackgroundService bservice,WindowManager windowmanager) throws ServiceException {
        this.pservice = pservice;
        this.bservice = bservice;
        this.logoService = logoService;
        this.windowManager = windowmanager;
    }

    public SettingFrameController(){

    }




    @FXML
    protected abstract void initialize();

    protected void refreshTableProfiles(List<Profile> profileList){
        LOGGER.info("refreshing the profil table...");
        profList.clear();
        profList.addAll(profileList);
        tableProfil.setItems(profList);

    }
    protected void refreshLogoAutoComplete(Profile selectedProfile) throws ServiceException {
        txLogoName.getEntries().addAll(logo2StringArray(pservice.getAllLogosOfProfile(selectedProfile)));
        txLogoName.getImgViews().putAll(logo2imgViews(pservice.getAllLogosOfProfile(selectedProfile)));
        txLogoName.setTxLogoPath(txLogoLogo);
    }

    protected void refreshTablePosition(List<Position> positionList){
        LOGGER.info("refreshing the position table...");
        posList.clear();
        posList.addAll(positionList);
        tablePosition.setItems(posList);
    }
    protected void refreshTableBackground(List<Background> backgroundList){
        LOGGER.info("refreshing the background table...");
        this.backgroundList.clear();
        this.backgroundList.addAll(backgroundList);
        tableBackground.setItems(this.backgroundList);
    }

    protected void refreshTableKameraPosition(List<Profile.PairCameraPosition> camposList){
        LOGGER.info("refreshing the KameraPosition-Zuweisung table...");
        this.kamPosList.removeAll(kamPosList);
        this.kamPosList.addAll(camposList);
        tableKamPos.setItems(this.kamPosList);
    }
    protected void refreshTableLogo(List<Profile.PairLogoRelativeRectangle> logoList){
        LOGGER.info("refreshing the Logo table...");
        this.logoList.clear();
        this.logoList.addAll(logoList);
        tableLogo.setItems(this.logoList);
    }




    @FXML
    protected abstract void backgroundUpload();





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
    protected List<String> logo2StringArray(List<Logo> logos){
        List<String> ret = new ArrayList<>();
        for (Logo logo:logos){
            ret.add(logo.getLabel().toLowerCase()+" #"+logo.getId());
        }
        return ret;
    }
    protected Map<String,ImageView> logo2imgViews(List<Logo> logos){
        Map<String,ImageView> ret = new HashMap<>();
        for (Logo logo:logos){
            String logoPath;
            if(new File(logo.getPath()).isFile())
                logoPath = logo.getPath();
            else
                logoPath = System.getProperty("user.dir")+"/src/main/resources/images/noimage.png";

            ImageView imgView =new ImageView(new Image("file:"+logoPath,30,30,true,true));

            imgView.setId((logoPath.contains("noimage.png")?"":logoPath));

            ret.put(logo.getLabel().toLowerCase()+" #"+logo.getId(),imgView);
        }
        return ret;
    }

    @FXML
    protected abstract void positionUpload();
    @FXML
    protected abstract void savePosition();
    @FXML
    protected abstract void fullScreenPreview();
    @FXML
    protected abstract void saveLogo();
    @FXML
    protected abstract void logoUpload();
    @FXML
    protected abstract void watermarkUpload();
    @FXML
    protected abstract void saveProfil();



}
