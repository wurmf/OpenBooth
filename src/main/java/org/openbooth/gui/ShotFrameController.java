package org.openbooth.gui;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.openbooth.imageprocessing.ImageProcessingManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

@Controller
public class ShotFrameController {
    @FXML
    private Label countdownLabel;
    @FXML
    private ImageView shotView;

    private static ApplicationContext applicationContext;

    private static int counter;

    private static Timer timer;

    public ShotFrameController(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    public void refreshShot(BufferedImage img) {
        shotView.setImage(SwingFXUtils.toFXImage(img,null));
    }

    @FXML
    private void shotFrameClicked(){

        applicationContext.getBean(ImageProcessingManager.class).trigger();
    }

    public void startTimer(int counter){
        this.counter = counter;

        Platform.runLater(()
                -> countdownLabel.setText(""));
        Platform.runLater(()
                -> countdownLabel.setVisible(true));
        timer = new Timer();

        useTimer();
    }

    private void useTimer(){
        int delay = 1000;
        int period = 1000;

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                Platform.runLater(()
                        -> countdownLabel.setText(setText()));
            }

        }, delay, period);

    }
    private static final String setText() {
        String text = "";

        if(counter ==-1) {
            timer.cancel();
        }
        counter--;
        if (counter>0){
            text = ""+counter;
        }else if (counter ==0){
            text = "chees";
        }

        return text;
    }

}
