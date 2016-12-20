package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.service.AdminUserService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Controller for the loginFrame
 */
@Controller
public class LoginFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFrameController.class);

    private WindowManager windowManager;
    private AdminUserService adminUserService;
    @FXML
    private TextField adminField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label wrongCredentialsLabel;


    @Autowired
    public LoginFrameController(AdminUserService adminUserService, WindowManager windowManager) throws ServiceException{
        this.adminUserService=adminUserService;
        this.windowManager=windowManager;
    }

    /**
     * Lets an AdminUserService instance check if the values given in the adminname- and password-TextField correspond to a saved admin-user.
     */
    @FXML
    public void checkLogin(){
        String adminName=adminField.getText();
        String password=passwordField.getText();
        try {
            boolean correctLogin=adminUserService.checkLogin(adminName,password);
            if(correctLogin){
                windowManager.showShootingAdministration();
                resetValues();
            } else{
                wrongCredentialsLabel.setVisible(true);
            }
        } catch (ServiceException e) {
            LOGGER.error("checkLogin - "+e);
        }
    }

    @FXML
    public void onEnter(KeyEvent keyEvent){
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            checkLogin();
        }
    }
    /**
     * Closes the login-frame and sets back all the values possibly changed during the time it was open.
     */
    @FXML
    public void closeLogin(){
        resetValues();
        windowManager.showMainFrame();
    }

    /**
     * Empties the values in the two textfields and sets the wrongCredentialsLabel to invisible.
     */
    private void resetValues(){
        wrongCredentialsLabel.setVisible(false);
        adminField.setText("");
        passwordField.setText("");
    }
}
