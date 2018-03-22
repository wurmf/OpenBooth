package org.openbooth.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.openbooth.entities.Shooting;
import org.openbooth.gui.model.LoginRedirectorModel;
import org.openbooth.service.AdminUserService;
import org.openbooth.service.ShootingService;
import org.openbooth.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginFrameController {


    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFrameController.class);
    public TextField tf_password;
    public TextField tf_name;
    public Button b_confirm;
    private WindowManager windowManager;
    private AdminUserService adminUserService;
    private LoginRedirectorModel loginRedirectorModel;
    private ShootingService shootingService;
    private boolean firstLogin;
    private boolean password;
    private boolean name;

    @Autowired
    public LoginFrameController(ShootingService shootingService, AdminUserService adminUserService, WindowManager windowManager, LoginRedirectorModel loginRedirectorModel) throws ServiceException {
        this.adminUserService=adminUserService;
        this.windowManager=windowManager;
        this.loginRedirectorModel=loginRedirectorModel;
        this.shootingService =shootingService;
        firstLogin = true;
    }
    /**
     * Lets an AdminUserService instance check if the values given in the adminname- and password-TextField correspond to a saved admin-user.
     */
    @FXML
    public void onConfirmPressed(ActionEvent actionEvent) {
        String adminName=tf_name.getText();
        String password=tf_password.getText();
        try {
            boolean correctLogin=adminUserService.checkLogin(adminName,password);
            if(correctLogin){
                if(firstLogin){
                    Shooting activeShooting = shootingService.searchIsActive();
                    if(activeShooting.getActive()){
                        firstLogin=false;
                        windowManager.showScene(WindowManager.SHOW_RECOVERYSCENE);
                    } else{
                        windowManager.initMiniController();
                        firstLogin=false;
                        windowManager.showScene(WindowManager.SHOW_MAINSCENE);
                    }
                }else{
                    firstLogin=false;
                    windowManager.showScene(loginRedirectorModel.getNextScene());
                }
                resetValues();
            } else{
                wrongCredentialsLabel.setVisible(true);
            }
        } catch (ServiceException e) {
            LOGGER.error("checkLogin - ",e);
        }
    }

    @FXML
    public void onEnterName(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            if (!tf_name.getText().isEmpty()||!tf_name.getText().contains("Name")){
                name = true;
                if (password){
                    b_confirm.setVisible(true);
                }
            }else{
                b_confirm.setVisible(false);
                name = false;
            }
        }
    }

    @FXML
    public void onEnterPassword(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            if (!tf_password.getText().isEmpty()||!tf_password.getText().contains("Passwort")){
                password = true;
                if (name){
                    b_confirm.setVisible(true);
                }
            }else{
                b_confirm.setVisible(false);
                password = false;
            }
        }
    }

    /**
     * Empties the values in the two textfields and sets the wrongCredentialsLabel to invisible and sets the scene.
     */
    private void resetValues(){
        wrongCredentialsLabel.setVisible(false);
        tf_password.setText("");
        tf_name.setText("");
        b_confirm.setVisible(false);
    }
}
