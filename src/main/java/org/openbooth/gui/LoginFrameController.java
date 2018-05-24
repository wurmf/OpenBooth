package org.openbooth.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import org.springframework.stereotype.Component;

@Component
public class LoginFrameController {


    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFrameController.class);
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private Button confirmButton;
    @FXML
    private Button abortButton;
    @FXML
    public Label wrongPoNLabel;
    private WindowManager windowManager;
    private AdminUserService adminUserService;
    private LoginRedirectorModel loginRedirectorModel;
    private ShootingService shootingService;
    private boolean firstLogin;

    @Autowired
    public LoginFrameController(ShootingService shootingService, AdminUserService adminUserService, WindowManager windowManager, LoginRedirectorModel loginRedirectorModel) {
        this.adminUserService=adminUserService;
        this.windowManager=windowManager;
        this.loginRedirectorModel=loginRedirectorModel;
        this.shootingService =shootingService;
        this.firstLogin = true;

    }
    /**
     * Lets an AdminUserService instance check if the values given in the adminname- and passwordEntered-TextField correspond to a saved admin-user.
     */



    @FXML
    public void onConfirmPressed() {
        String adminName= nameTextField.getText();
        String passwordInput = passwordTextField.getText();
        try {
            boolean correctLogin=adminUserService.checkLogin(adminName,passwordInput);
            if(correctLogin){
                if(firstLogin){
                    Shooting activeShooting = shootingService.searchIsActive();
                    if(activeShooting.getActive()){
                        windowManager.showScene(WindowManager.SHOW_RECOVERYSCENE);
                    } else{
                        windowManager.initMiniController();
                        windowManager.showScene(WindowManager.SHOW_MAINSCENE);
                    }
                    firstLogin=false;
                }else{
                    windowManager.showScene(loginRedirectorModel.getNextScene());
                }
                resetValues();
            } else{
                wrongPoNLabel.setVisible(true);
            }
        } catch (ServiceException e) {
            LOGGER.error("checkLogin - ",e);
        }
    }

    @FXML
    public void checkInputs(KeyEvent keyEvent){
        if(!passwordTextField.getText().isEmpty() && !nameTextField.getText().isEmpty()){
            if(keyEvent.getCode()==KeyCode.ENTER){
                onConfirmPressed();
            }
            confirmButton.setVisible(true);
        } else {
            confirmButton.setVisible(false);
        }
    }

    /**
     * Empties the values in the two textfields and sets the wrongCredentialsLabel to invisible and sets the scene.
     */
    private void resetValues(){
        wrongPoNLabel.setVisible(false);
        passwordTextField.setText("");
        nameTextField.setText("");
        confirmButton.setVisible(false);
    }

    /*
    abort action
     */
    public void onAbortPressed(ActionEvent actionEvent) {

        System.exit(0);


    }
}
