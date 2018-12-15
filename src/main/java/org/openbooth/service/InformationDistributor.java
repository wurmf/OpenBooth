package org.openbooth.service;

import org.springframework.stereotype.Component;

/**
 * This class is responsible for passing non-persistent information
 */
@Component
public class InformationDistributor {

    private boolean timedShotEnabled = true;

    public boolean isTimedShotEnabled() {
        return timedShotEnabled;
    }

    public synchronized void setTimedShotEnabled(boolean timedShotEnabled) {
        this.timedShotEnabled = timedShotEnabled;
    }
}
