package org.openbooth.trigger;


import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class KeyPressTrigger {

    private TriggerManager triggerManager;

    private List<KeyCode> shotKeys = Arrays.asList(KeyCode.PAGE_DOWN, KeyCode.PAGE_UP, KeyCode.F1);

    public KeyPressTrigger(TriggerManager triggerManager) {
        this.triggerManager = triggerManager;
    }

    public void trigger(KeyEvent keyEvent){
        if(shotKeys.contains(keyEvent.getCode())) triggerManager.triggerShot();
    }
}
