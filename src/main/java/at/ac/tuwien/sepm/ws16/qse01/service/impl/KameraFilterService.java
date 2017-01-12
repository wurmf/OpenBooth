package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Moatzgeile Sau on 12.01.2017.
 */
@Service
public class KameraFilterService {

    private Integer[] current;
    private Profile profile;

    ShootingService shootingService;
    ProfileService profileService;

    @Autowired
    public KameraFilterService(ShootingService shootingService, ProfileService profileService) {
        this.shootingService = shootingService;
        this.profileService = profileService;
        try {
            profile = profileService.get(shootingService.searchIsActive().getProfileid());
            current = new Integer[profile.getPairCameraPositions().size()];
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public void setcurrent(int index, int modus ){
        current[index]=modus;
    }

    public int getcurrent(int index){
        if(current[index]==null){
            current[index]=0;
        }
        return current[index];
    }
}