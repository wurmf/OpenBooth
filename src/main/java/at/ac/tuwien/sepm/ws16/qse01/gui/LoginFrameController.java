package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.application.MainApplication;
import at.ac.tuwien.sepm.ws16.qse01.service.AdminUserService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

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
                closeLogin();
            } else{
                wrongCredentialsLabel.setVisible(true);
            }
        } catch (ServiceException e) {
            LOGGER.error("checkLogin - "+e);
        }
    }

    /**
     * Closes the login-frame and sets back all the values possibly changed during the time it was open.
     */
    @FXML
    public void closeLogin(){
        wrongCredentialsLabel.setVisible(false);
        adminField.setText("");
        passwordField.setText("");
    }
}
