package org.openbooth.service.impl;

import org.openbooth.service.RemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Remote Service Tester
 */

public class RemoteServiceTesterImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteServiceTesterImpl.class);

    public static void main(String[] args) throws InterruptedException {
        LOGGER.debug("remoteServiceTester started");
        RemoteService remoteService = new RemoteServiceImpl(null,null);
        remoteService.start();
        LOGGER.debug("remoteServiceTester begins listening to input");
        TimeUnit.SECONDS.sleep(30);
        LOGGER.debug("remoteServiceTester stops listening to input");
        remoteService.stop();
        TimeUnit.SECONDS.sleep(30);
        LOGGER.debug("remoteServiceTester ended now");
    }
}

