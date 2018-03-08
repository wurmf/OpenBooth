package org.openbooth.service.impl;

import org.openbooth.camera.CameraHandler;
import org.openbooth.service.ProfileService;
import org.openbooth.service.RemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * RemoteService Implementation
 */
@Service
public class RemoteServiceImpl implements RemoteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteServiceImpl.class);
    private Thread thread = null;
    private RemoteRunnable remoteRunnable;
    @Resource
    private ProfileService profileService;
    @Resource
    private CameraHandler cameraHandler;
    private boolean running;

    @Autowired
    public RemoteServiceImpl(CameraHandler cameraHandler, ProfileService profileService){
        LOGGER.debug("constructor with parameters cameraHandler{} and profileService {}",cameraHandler,profileService);
        this.cameraHandler = cameraHandler;
        this.profileService = profileService;
        this.remoteRunnable = null;
        this.running = false;
    }
    @Override
    public void start() {
        LOGGER.debug("start");
        remoteRunnable = new RemoteRunnable(cameraHandler,profileService);
        thread = new Thread(remoteRunnable);
        thread.start();
        this.running = true;
    }
    @Override
    public void stop() {
        LOGGER.debug("stop");
        if (thread != null){
            remoteRunnable.stopExecuting();
            thread.interrupt();
            }
            this.running = false;
        }

    @Override
    public boolean isRunning() {
        return running;
    }
}
