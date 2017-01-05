package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.awt.image.BufferedImage;

/**
 * Created by fabian on 04.01.17.
 */
public interface GreenscreenService {

    BufferedImage getGreenscreenPreview(String srcImgPath, String backgroundPath) throws ServiceException;
}
