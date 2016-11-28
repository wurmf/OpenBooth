package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.service.AdminUserService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.AdminUserServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Controller for the loginFrame
 */
public class LoginFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFrameController.class);

    private SpringFXMLLoader springFXMLLoader;
    private Stage primaryStage;
    private AdminUserService adminUserService;

    @FXML
    private TextField adminField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label wrongCredentialsLabel;

    @Autowired
    public LoginFrameController(SpringFXMLLoader springFXMLLoader) throws ServiceException{
        this.springFXMLLoader = springFXMLLoader;
        this.adminUserService=new AdminUserServiceImpl();
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void checkLogin(){
        String adminName=adminField.getText();
        String password=passwordField.getText();
        try {
            boolean correctLogin=adminUserService.checkLogin(adminName,password);
            if(correctLogin){
                //TODO: set Action on correct input
            } else{
                wrongCredentialsLabel.setVisible(true);
            }
        } catch (ServiceException e) {
            LOGGER.error("checkLogin - "+e);
        }
    }
    @FXML
    public void closeLogin(){

    }
}
