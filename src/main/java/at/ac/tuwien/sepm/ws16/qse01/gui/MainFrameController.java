package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.application.MainApplication;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ShootingServiceImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;


//TODO: Remove everything Swing related! We have to use JavaFX-Classes

/**
 * The controller for the mainFrame.
 *
 * @author Dominik Moser
 */
@Component
public class MainFrameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrameController.class);

    private SpringFXMLLoader springFXMLLoader;
    private Stage primaryStage;
    private MainApplication mainApp;

    ShootingService service;
    @Autowired
    public MainFrameController(SpringFXMLLoader springFXMLLoader) throws Exception {
        this.springFXMLLoader = springFXMLLoader;
        service= new ShootingServiceImpl();
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void setStageAndMain(Stage primaryStage, MainApplication mainApp){
        this.primaryStage = primaryStage;
        this.mainApp = mainApp;
    }


    /**
     * inizialize Mainfraim
     * tests whether there is still an session active
     */
    @FXML
    private void initialize(){
        Shooting shouting_isactive = null;
        try {
            shouting_isactive = service.search_isactive();

            if(shouting_isactive.getIsactiv()==true){
                in_case_of_restart();
            }
        } catch (ServiceException e) {
            showingdialog("Ein fehler beim Starten des Programms ist aufgetreten.");
            LOGGER.info("MainFrameController:",e.getMessage());
         }
    }

    /**
     * linkes to admin log in and followup options
     *
     * @param actionEvent
     */
    public void on_StartSessionPressed(ActionEvent actionEvent) {
        mainApp.showAdminLogin();
    }

    /**
     * in case of brakdown
     *
     */
    public void in_case_of_restart(){

        Object [] options={"Fortfahren", "alte Shooting beenden"};

        int chouse= JOptionPane.showOptionDialog(null,"Die Anwendung wurde unerwartet geschlossen,\n möchten sie die zuletzt geöffnete Shooting wieder her stellen? "
                ,"", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);

        if(chouse==0){

            //verlinken auf Kunden interface
            //yes
        } else if(chouse==1){
            service.end_session();
            showingdialog("Shooting wurde beendet");
        }

    }

    /**
     * stop programm
     *
     * @param actionEvent
     */
    public void on_EndPressed(ActionEvent actionEvent) {Platform.exit();
    }

    /**
     *information Panel
     *
     * @param messege
     * @return JOptionPane
     */
    private JOptionPane showingdialog(String messege){
        JOptionPane dialog = new JOptionPane();
        dialog.showMessageDialog(null, messege);
        return dialog;
    }
}
