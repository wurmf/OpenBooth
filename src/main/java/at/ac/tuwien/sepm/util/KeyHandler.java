package at.ac.tuwien.sepm.util;

import javafx.scene.input.KeyCode;

import javafx.scene.input.KeyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyHandler.class);

    public static int getIndexForKeyEvent(KeyEvent keyEvent){

        LOGGER.debug("triggerShot with keyEventCharacter " + keyEvent.getCharacter());

        if(keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.UP){
            return 0;
        }

        String keystoke = keyEvent.getText();

        int index = -1;

        switch (keystoke){
            case "1" : index = 0;break;
            case "2" : index = 1;break;
            case "3" : index = 2;break;
            case "4" : index = 3;break;
            case "5" : index = 4;break;
            case "6" : index = 5;break;
            case "7" : index = 6;break;
            case "8" : index = 7;break;
            case "9" : index = 8;break;
            default: index = -1;
        }

        return index;

    }
}
