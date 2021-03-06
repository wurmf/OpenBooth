package org.openbooth.service;

import org.springframework.stereotype.Component;

/**
 * This class is responsible for storing non-persistent information
 */
@Component
public class InformationDistributor {

    private boolean timedShotEnabled = true;

    public synchronized boolean isTimedShotEnabled() {
        return timedShotEnabled;
    }

    public synchronized void setTimedShotEnabled(boolean timedShotEnabled) {
        this.timedShotEnabled = timedShotEnabled;
    }
}
