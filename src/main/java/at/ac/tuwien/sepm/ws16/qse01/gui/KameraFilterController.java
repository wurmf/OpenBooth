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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Kamera Filter frame controller
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
    int currentMode;
    GridPane grid = new GridPane();
    ImageView[] chousenimage;
    private Profile profile;

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
            GridPane filter = new GridPane();
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            filter.prefWidth(scrollPane.getWidth() - scrollPane.getWidth() * 0.05);
            int columcount = 0;
            int rowcount = 0;
            Map<String,BufferedImage> filtermap = filterService.getAllFilteredImages("/images/dummies/p1");
            for (Map.Entry<String, BufferedImage> filterentety: filtermap.entrySet()) {//imagefilter.size
                if (columcount == 6) {
                    rowcount++;
                    columcount = 0;
                }
                ImageView iv = new ImageView();
                iv.setFitHeight((scrollPane.getWidth() - scrollPane.getWidth() * 0.05) / 6);
                iv.setFitWidth((scrollPane.getWidth() - scrollPane.getWidth() * 0.05) / 6);//imagefilter.get(i).getImagepath()
                iv.setImage(SwingFXUtils.toFXImage(filterentety.getValue(),null));
                iv.setOnMouseClicked((MouseEvent mouseEvent) -> {
                   /* if(fId==){

                    }*/
                });
                filter.add(iv, columcount, rowcount);
                columcount++;
            }
            scrollPane.setVisible(true);
            scrollPane.setContent(filter);
            grid.add(scrollPane, 0, 0);
            root.add(grid, 0, 1);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


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
     * desides whether an new fiter imge is chousen or green screen
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
               if (greenscreen) {
                    titel.setText("Kamera " + profileservice.getAllPairCameraPositionOfProfile().get(index).getCamera().getId() + " Hintergrund auswahl");
                    titel.setVisible(true);
                    creatGreenscreenButton();
                } else {
                    if (buttonList.isEmpty()) {
                        titel.setText("Kamera " + profileservice.getAllPairCameraPositionOfProfile().get(index).getCamera().getId() + " Filter auswahl");
                        titel.setVisible(true);
                        creatButtons();
                    } else {
                        titel.setText("Kamera " + profileservice.getAllPairCameraPositionOfProfile().get(index).getCamera().getId() + " Filter auswahl");
                        titel.setVisible(true);
                        loadButton();
                    }
                }

            }
        } catch (ServiceException e) {
            LOGGER.debug("no camera pair found", e);
        }
    }

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
