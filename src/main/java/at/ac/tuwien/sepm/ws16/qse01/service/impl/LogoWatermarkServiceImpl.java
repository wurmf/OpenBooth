package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class LogoWatermarkServiceImpl implements LogoWatermarkService{

    @Override
    public void addLogosToImage(String srcImgPath) throws ServiceException {
        
    }

    @Override
    public void addWatermarkCreateNewImage(String srcImgPath, String destImgPath) throws ServiceException {

    }
}
