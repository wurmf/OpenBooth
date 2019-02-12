package org.openbooth.gui;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
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

    private int counter;

    private Timer timer;


    void resizeShotViewToStage(Stage stage){
        shotView.setPreserveRatio(true);

        shotView.fitWidthProperty().bind(stage.widthProperty());
        shotView.fitHeightProperty().bind(stage.heightProperty());
    }

    public void refreshShot(BufferedImage img) {
        shotView.setImage(SwingFXUtils.toFXImage(img,null));
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

    public boolean isCountDownFinished(){
        return counter <= 0;
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
    private String setText() {
        String text = "";

        if(counter ==-1) {
            timer.cancel();
        }
        counter--;
        if (counter>0){
            text = ""+counter;
        }else if (counter ==0){
            text = "Cheese!";
        }

        return text;
    }

}
