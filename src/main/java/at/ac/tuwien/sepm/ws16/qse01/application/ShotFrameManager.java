package at.ac.tuwien.sepm.ws16.qse01.application;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Deniz on 30.11.16.
 */
public class ShotFrameManager implements Runnable {
    private int numberFrames;
    private SpringFXMLLoader springFXMLLoader;
    private Stage primaryStage;

    public ShotFrameManager(int numberFrames,SpringFXMLLoader springFXMLLoader,Stage primaryStage){
        this.numberFrames = numberFrames;
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = primaryStage;

    }

    @Override
    public void run() {
        final int[] x = {200};
        for(int i=0; i<numberFrames; i++) { // Anzahl der Kameras...
            System.out.println("i = "+i);
            int finalI = i;
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                 //   new Thread() {
                 //       public void run() {
                            Stage stage = new Stage();
                            stage.setTitle("Shot Frame " + (finalI + 1));
                            try {
                                stage.setScene(new Scene((Parent) springFXMLLoader.load("/fxml/shotFrame.fxml"), 400, 400));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // stage.setFullScreen(true);
                            stage.initModality(Modality.WINDOW_MODAL);
                            stage.setFullScreen(false);
                            stage.initOwner(primaryStage);
                            stage.setX(x[0]);
                            stage.show();

                            x[0] += 200;
                   //     }
                   // }.start();
                }
            });

        }
    }
}
