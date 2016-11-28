package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.dao.SessionDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.SessionDAOImpl;
import at.ac.tuwien.sepm.ws16.qse01.entities.Session;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
*/
import javax.swing.*;

/**
 * The controller for the mainFrame.
 *
 * @author Dominik Moser
 */
//@Component
public class MainFrameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrameController.class);

    private SpringFXMLLoader springFXMLLoader;
    private Stage primaryStage;


  //  @Autowired
    public MainFrameController(SpringFXMLLoader springFXMLLoader) throws Exception {
        this.springFXMLLoader = springFXMLLoader;



    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    SessionDAO service = new SessionDAOImpl();


    /**
     * inizialize Mainfraim
     * tests whether there is still an session active
     * @autor Aniela
     */
    @FXML
    private void initialize(){
        Session session_isactive= null;
        try {
            session_isactive = service.search_isactive();

            if(session_isactive.getIsactiv()==true){
                in_case_of_restart();
            }
        } catch (PersistenceException e) {
            showingdialog("Ein fehler beim Starten des Programms ist aufgetreten.");
            LOGGER.info("MainFrameController:",e.getMessage());
         }
    }

    /**
     * linkes to admin log in and followup options
     *
     * @param actionEvent
     * @autor Aniela
     */
    public void on_StartSessionPressed(ActionEvent actionEvent) {
        //Log in opens

    }

    /**
     * in case of brakdown
     *
     * @autor Aniela
     */
    public void in_case_of_restart(){

        Object [] options={"Fortfahren", "alte Session beenden"};

        int chouse= JOptionPane.showOptionDialog(null,"Die Anwendung wurde unerwartet geschlossen,\n möchten sie die zuletzt geöffnete Session wieder her stellen? "
                ,"", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);

        if(chouse==0){

            //verlinken auf Kunden interface
            //yes
        } else if(chouse==1){
            service.end_session();
            showingdialog("Session wurde beendet");
        }

    }

    /**
     * stop programm
     *
     * @param actionEvent
     * @autor Aniela
     */
    public void on_EndPressed(ActionEvent actionEvent) {Platform.exit();
    }

    private JOptionPane showingdialog(String messege){
        JOptionPane dialog = new JOptionPane();
        dialog.showMessageDialog(null, messege);
        return dialog;
    }
}
