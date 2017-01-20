package at.ac.tuwien.sepm.ws16.qse01.service;

/**
 * RemoteService Interface
 */
public interface RemoteService {

    /**
     * start a trigger sequence listener
     */
    void start();

    /**
     * stop trigger sequence listener
     */
    void stop();

    /**
     * find out, if trigger sequence listener is running
     * @return true if running
     */
    boolean isRunning();
}
