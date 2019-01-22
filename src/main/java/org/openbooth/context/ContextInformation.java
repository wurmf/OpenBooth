package org.openbooth.context;

import org.springframework.stereotype.Component;

/**
 * This class is responsible for storing non-persistent information
 */
@Component
public class ContextInformation {
    

    private ShotType shotType = ShotType.SINGLE;

    public synchronized void setShotTypeTo(ShotType shotType){ this.shotType = shotType; }

    public synchronized ShotType getShotType(){return shotType; }

}
