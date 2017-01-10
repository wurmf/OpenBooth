package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Moatzgeile Sau on 04.01.2017.
 */
@Component
public class KameraFilterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CostumerFrameController.class);

    @FXML
    private Label titel;

    //@Autowired
    public KameraFilterController(){

    }

    private void creatButtons(){
        // try {
        /*List<Profile.PairCameraPosition> pairList = profile.getPairCameraPositions();
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

    public void onBackbuttonpressed(ActionEvent actionEvent) {
    }

    public void onsingelPressed(ActionEvent actionEvent) {
    }

    public void onserienPressed(ActionEvent actionEvent) {
    }

    public void ontimerPressed(ActionEvent actionEvent) {
    }

    public String getCurrentMode(){
        return  "";
    }
}
