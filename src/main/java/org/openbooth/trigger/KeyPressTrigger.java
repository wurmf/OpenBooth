package org.openbooth.trigger;


import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class KeyPressTrigger {

    private TriggerManager triggerManager;

    private List<KeyCode> singleShotKeys = Arrays.asList(KeyCode.PAGE_UP, KeyCode.F1);
    private List<KeyCode> timedShotKeys = Arrays.asList(KeyCode.PAGE_DOWN, KeyCode.F2);

    public KeyPressTrigger(TriggerManager triggerManager) {
        this.triggerManager = triggerManager;
    }

    public void trigger(KeyEvent keyEvent){
        if(singleShotKeys.contains(keyEvent.getCode())){
            triggerManager.triggerSingleShot();
        }else if(timedShotKeys.contains(keyEvent.getCode())){
            triggerManager.triggerTimedShot();
        }
    }
}
