package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.ws16.qse01.entities.Logo;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.entities.RelativeRectangle;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.AutoCompleteTextField;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.Double2String;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.LogoButtonCell;
import at.ac.tuwien.sepm.ws16.qse01.gui.specialCells.LogoImgCell;
import at.ac.tuwien.sepm.ws16.qse01.service.BackgroundService;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by macdnz on 13.01.17.
 */
@Component("logo")
public class LogoFrameController extends SettingFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogoFrameController.class);


    /* Beginn of Logo Table Column FXML */
    @FXML
    private TableView tableLogo;
    @FXML
    private TableColumn colLogoID;
    @FXML
    private TableColumn colLogoName;
    @FXML
    private TableColumn colLogoX;
    @FXML
    private TableColumn colLogoY;
    @FXML
    private TableColumn colLogoBreite;
    @FXML
    private TableColumn colLogoHoehe;
    @FXML
    private TableColumn colLogoLogo;
    @FXML
    private TableColumn colLogoAktion;
    @FXML
    private AutoCompleteTextField txLogoName;
    @FXML
    private TextField txLogoLogo;
    @FXML
    private Button txLogoUpload;
    @FXML
    private Button txLogoAdd;

    /* LOGO TextFIELDS */
    @FXML
    private TextField txLogoX;
    @FXML
    private TextField txLogoY;
    @FXML
    private TextField txLogoBreite;
    @FXML
    private TextField txLogoHoehe;

    /* Vorschau FXML */
    @FXML
    private TextField txPreviewWidth;
    @FXML
    private TextField txPreviewHeight;
    @FXML
    private ImageView previewLogo;

    @Autowired
    public LogoFrameController(ProfileService pservice, LogoWatermarkService logoService, BackgroundService bservice, WindowManager windowmanager,ImageHandler imageHandler) throws ServiceException {
        super(pservice, logoService, bservice, windowmanager,imageHandler);
    }

    @FXML
    private void initialize() {

        tableLogo.setEditable(true);

        colLogoID.setCellValueFactory(new PropertyValueFactory<Profile.PairLogoRelativeRectangle, Integer>("id"));

        colLogoID.setCellFactory(tc -> {
                    TableCell<Profile.PairLogoRelativeRectangle, Integer> cell = new TableCell<Profile.PairLogoRelativeRectangle, Integer>() {
                        @Override
                        protected void updateItem(Integer item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(empty ? null : String.valueOf(item));
                        }
                    };
                    cell.setOnMouseClicked(e -> {
                        if (!cell.isEmpty() && selectedLogo != null) {
                            if (selectedLogo.getLogo().getId() == cell.getItem()) {
                                LOGGER.info("Logo Zeile auswählen oder abwählen -> selectedLogo->"+selectedLogo.getLogo().getId());
                                tableLogo.getSelectionModel().clearSelection();
                                selectedLogo = null;
                                try {
                                    previewLogo.setImage(SwingFXUtils.toFXImage(logoService.getPreviewForMultipleLogos(logoList, Integer.valueOf(txPreviewWidth.getText()), Integer.valueOf(txPreviewHeight.getText())), null));
                                } catch (ServiceException e1) {
                                    LOGGER.error("rowSelected->Error", e1);
                                }
                            }


                        }
                    });
                return cell;

                });

        colLogoName.setCellValueFactory(new PropertyValueFactory<Profile.PairLogoRelativeRectangle, Logo>("logo"));
        colLogoName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, String> p) {
                String newLabel = "null(No Logo Name)";
                if(p.getValue().getLogo()!=null)
                    newLabel = p.getValue().getLogo().getLabel();

                return new ReadOnlyObjectWrapper(newLabel);
            }
        });
        colLogoName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLogoName.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, String> t) {
                        try {
                            Profile.PairLogoRelativeRectangle p = ((Profile.PairLogoRelativeRectangle) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            );
                            if (t.getNewValue().compareTo("") != 0) {
                                if(p.getLogo()==null){
                                    Logo newLogo = pservice.addLogo(new Logo(t.getNewValue(),"noimage.jpg"));
                                    p.setLogo(newLogo);
                                }else {
                                    if(pservice.getNumberOfUsing(p.getLogo().getId())==1) {
                                        txLogoName.getEntries().remove(p.getLogo().getLabel().toLowerCase() + " #" + p.getLogo().getId());
                                        txLogoName.getImgViews().remove(p.getLogo().getLabel().toLowerCase() + " #" + p.getLogo().getId());

                                        p.getLogo().setLabel(t.getNewValue());

                                        ImageView imgView = new ImageView(new Image("file:" + p.getLogo().getPath(), 30, 30, true, true));
                                        imgView.setId((p.getLogo().getPath()));
                                        txLogoName.getEntries().add(p.getLogo().getLabel().toLowerCase() + " #" + p.getLogo().getId());
                                        txLogoName.getImgViews().put(p.getLogo().getLabel().toLowerCase() + " #" + p.getLogo().getId(), imgView);
                                    }else{
                                        int index = logoList.indexOf(p);
                                        logoList.remove(p);
                                        p.getLogo().setId(Integer.MIN_VALUE);
                                        p.getLogo().setLabel(t.getNewValue());
                                        Logo newLogo = pservice.addLogo(p.getLogo());
                                        p.setLogo(newLogo);


                                        logoList.add(index,p);

                                        ImageView imgView = new ImageView(new Image("file:" + p.getLogo().getPath(), 30, 30, true, true));
                                        imgView.setId((p.getLogo().getPath()));
                                        txLogoName.getEntries().add(p.getLogo().getLabel().toLowerCase()+" #"+p.getLogo().getId());
                                        txLogoName.getImgViews().put(p.getLogo().getLabel().toLowerCase()+" #"+p.getLogo().getId(),imgView);
                                        pservice.editPairLogoRelativeRectangle(p);

                                    }
                                }

                                LOGGER.info("Logo changed..."+p.getLogo().getId()+"_"+p.getLogo().getLabel()+"_"+p.getLogo().getPath());
                                pservice.editLogo(p.getLogo());
                            } else {
                                refreshTableLogo(pservice.getAllPairLogoRelativeRectangle(selectedProfile.getId()));
                            }

                        } catch (ServiceException e) {
                            try {
                                refreshTableProfiles(pservice.getAllProfiles());
                            } catch (ServiceException e1) {
                                LOGGER.error("Error: could not refresh the profile table: ",e1);
                            }

                        }

                    }
                });
            /* Logo Xstart Column */
        colLogoX.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getRelativeRectangle().getX());
            }
        });
        colLogoX.setCellFactory(TextFieldTableCell.forTableColumn(new Double2String("xstart")));
        colLogoX.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double> t) {
                        try {
                            Profile.PairLogoRelativeRectangle p = ((Profile.PairLogoRelativeRectangle) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            );
                            if (!t.getNewValue().isNaN()) {

                                p.getRelativeRectangle().setX(t.getNewValue());
                                pservice.editPairLogoRelativeRectangle(p);
                                changePreviewSize(txPreviewHeight.getText(),0);

                            } else {
                                refreshTableLogo(pservice.getAllPairLogoRelativeRectangle(selectedProfile.getId()));
                            }

                        } catch (ServiceException e) {
                            try {
                                refreshTableProfiles(pservice.getAllProfiles());
                            } catch (ServiceException e1) {
                                LOGGER.error("Error: could not refresh the profile table: ",e1);
                            }

                        }

                    }
                });

              /* Logo Ystart Column */
        colLogoY.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getRelativeRectangle().getY());
            }
        });
        colLogoY.setCellFactory(TextFieldTableCell.forTableColumn(new Double2String("ystart")));
        colLogoY.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double> t) {
                        try {
                            Profile.PairLogoRelativeRectangle p = ((Profile.PairLogoRelativeRectangle) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            );
                            if (!t.getNewValue().isNaN()) {

                                p.getRelativeRectangle().setY(t.getNewValue());
                                pservice.editPairLogoRelativeRectangle(p);
                                changePreviewSize(txPreviewHeight.getText(),0);
                            } else {
                                refreshTableLogo(pservice.getAllPairLogoRelativeRectangle(selectedProfile.getId()));
                            }

                        } catch (ServiceException e) {
                            try {
                                refreshTableProfiles(pservice.getAllProfiles());
                            } catch (ServiceException e1) {
                                LOGGER.error("Error: could not refresh the profile table: ",e1);
                            }

                        }

                    }
                });

              /* Logo Xstart Column */
        colLogoBreite.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getRelativeRectangle().getWidth());
            }
        });
        colLogoBreite.setCellFactory(TextFieldTableCell.forTableColumn(new Double2String("width")));
        colLogoBreite.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double> t) {
                        try {
                            Profile.PairLogoRelativeRectangle p = ((Profile.PairLogoRelativeRectangle) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            );
                            if (!t.getNewValue().isNaN()) {
                                p.getRelativeRectangle().setWidth(t.getNewValue());
                                pservice.editPairLogoRelativeRectangle(p);
                                changePreviewSize(txPreviewHeight.getText(),0);
                            } else {
                                refreshTableLogo(pservice.getAllPairLogoRelativeRectangle(selectedProfile.getId()));
                            }

                        } catch (ServiceException e) {
                            try {
                                refreshTableProfiles(pservice.getAllProfiles());
                            } catch (ServiceException e1) {
                                LOGGER.error("Error: could not refresh the profile table: ",e1);
                            }

                        }

                    }
                });
              /* Logo Xstart Column */
        colLogoHoehe.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Double> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getRelativeRectangle().getHeight());
            }
        });
        colLogoHoehe.setCellFactory(TextFieldTableCell.forTableColumn(new Double2String("height")));
        colLogoHoehe.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Profile.PairLogoRelativeRectangle, Double> t) {
                        try {
                            Profile.PairLogoRelativeRectangle p = ((Profile.PairLogoRelativeRectangle) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            );
                            if (!t.getNewValue().isNaN()) {
                                p.getRelativeRectangle().setHeight(t.getNewValue());
                                pservice.editPairLogoRelativeRectangle(p);
                                changePreviewSize(txPreviewHeight.getText(),0);
                            } else {
                                refreshTableLogo(pservice.getAllPairLogoRelativeRectangle(selectedProfile.getId()));
                            }

                        } catch (ServiceException e) {
                            try {
                                refreshTableProfiles(pservice.getAllProfiles());
                            } catch (ServiceException e1) {
                                LOGGER.error("Error: could not refresh the profile table: ",e1);
                            }

                        }

                    }
                });
             /* Logo Column */
        colLogoLogo.setStyle("-fx-alignment: CENTER;");
        colLogoLogo.setSortable(false);
        colLogoLogo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, String> p) {
                String logoPath = "noimage.jpg";
                if(p.getValue().getLogo()!=null)
                    logoPath = p.getValue().getLogo().getPath();
                return new ReadOnlyObjectWrapper(logoPath);
            }
        });
        colLogoLogo.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {

                return new LogoImgCell(logoList,pservice,imageHandler,windowManager.getStage(),txLogoName);

            }
        });
            /* Aktion Column */
        colLogoAktion.setStyle("-fx-alignment: CENTER;");
        colLogoAktion.setSortable(false);
        colLogoAktion.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Boolean>,
                        ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Profile.PairLogoRelativeRectangle, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });

        //Adding the X-Button to the cell
        colLogoAktion.setCellFactory(
                new Callback<TableColumn<Profile.PairLogoRelativeRectangle, Boolean>, TableCell<Profile.PairLogoRelativeRectangle, Boolean>>() {

                    @Override
                    public TableCell<Profile.PairLogoRelativeRectangle, Boolean> call(TableColumn<Profile.PairLogoRelativeRectangle, Boolean> p) {

                        return new LogoButtonCell(imageHandler,logoList,pservice,windowManager.getStage(),selectedProfile.getId(),previewLogo,txLogoName);
                    }

                });

            /* ###################
             *   Vorschau Teil
             *####################*/
        tableLogo.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Profile.PairLogoRelativeRectangle selectedLogo = (Profile.PairLogoRelativeRectangle) newSelection;
                LOGGER.info("Logo row selected..."+selectedLogo.getId());

                this.selectedLogo = selectedLogo;
                if (!selectedLogo.getLogo().getPath().isEmpty() && !selectedLogo.getLogo().getPath().equals("/images/noimage.png") && new File(selectedLogo.getLogo().getPath()).isFile()) {
                    try {
                        int width = Integer.parseInt(txPreviewWidth.getText());
                        int height = Integer.parseInt(txPreviewHeight.getText());
                        Image image = SwingFXUtils.toFXImage(logoService.getPreviewForLogo(selectedLogo.getLogo(), selectedLogo.getRelativeRectangle(), width, height), null);
                        previewLogo.setImage(image);
                    } catch (NumberFormatException e) {
                        LOGGER.error("Fehler: Bitte geben Sie eine Zahl an");
                    } catch (ServiceException e) {
                        LOGGER.error("Fehler: Bitte geben Sie eine Zahl an");
                    }
                } else
                    LOGGER.info("No Logo is uploaded...");

            }
        });

        txPreviewHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            changePreviewSize(newValue,0);

        });
        txPreviewWidth.textProperty().addListener((observable, oldValue, newValue) -> {
            changePreviewSize(newValue,1);
        });


        txLogoUpload.setBackground(imageHandler.getBackground("/images/upload1.png",50,50));
        txLogoUpload.setPrefWidth(50);
        txLogoUpload.setPrefHeight(50);


        txLogoAdd.setBackground(imageHandler.getBackground("/images/add3.png",50,50));
        txLogoAdd.setPrefWidth(50);
        txLogoAdd.setPrefHeight(50);

        txLogoName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && selectedProfile!=null && txLogoLogo.getText().compareTo("Hochladen...")!=0 && !txLogoX.getText().isEmpty() &&
                    !txLogoY.getText().isEmpty() && (!txLogoBreite.getText().isEmpty() || !txLogoHoehe.getText().isEmpty())){
                txLogoAdd.setBackground(imageHandler.getBackground("/images/add.png",50,50));
            }else
                txLogoAdd.setBackground(imageHandler.getBackground("/images/add3.png",50,50));

        });
        txLogoX.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && selectedProfile!=null && txLogoLogo.getText().compareTo("Hochladen...")!=0 && !txLogoName.getText().isEmpty() &&
                    !txLogoY.getText().isEmpty() && (!txLogoBreite.getText().isEmpty() || !txLogoHoehe.getText().isEmpty())){
                txLogoAdd.setBackground(imageHandler.getBackground("/images/add.png",50,50));
            }else
                txLogoAdd.setBackground(imageHandler.getBackground("/images/add3.png",50,50));

        });
        txLogoY.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && selectedProfile!=null && txLogoLogo.getText().compareTo("Hochladen...")!=0 && !txLogoX.getText().isEmpty() &&
                    !txLogoName.getText().isEmpty() && (!txLogoBreite.getText().isEmpty() || !txLogoHoehe.getText().isEmpty())){
                txLogoAdd.setBackground(imageHandler.getBackground("/images/add.png",50,50));
            }else
                txLogoAdd.setBackground(imageHandler.getBackground("/images/add3.png",50,50));

        });
        txLogoBreite.textProperty().addListener((observable, oldValue, newValue) -> {
            if((!newValue.isEmpty() || !txLogoHoehe.getText().isEmpty()) && selectedProfile!=null && txLogoLogo.getText().compareTo("Hochladen...")!=0 && !txLogoX.getText().isEmpty() &&
                    !txLogoY.getText().isEmpty() && !txLogoName.getText().isEmpty()){
                txLogoAdd.setBackground(imageHandler.getBackground("/images/add.png",50,50));
            }else
                txLogoAdd.setBackground(imageHandler.getBackground("/images/add3.png",50,50));

        });
        txLogoHoehe.textProperty().addListener((observable, oldValue, newValue) -> {
            if((!newValue.isEmpty() || !txLogoBreite.getText().isEmpty()) && selectedProfile!=null && txLogoLogo.getText().compareTo("Hochladen...")!=0 && !txLogoX.getText().isEmpty() &&
                    !txLogoY.getText().isEmpty() && !txLogoName.getText().isEmpty()){
                txLogoAdd.setBackground(imageHandler.getBackground("/images/add.png",50,50));
            }else
                txLogoAdd.setBackground(imageHandler.getBackground("/images/add3.png",50,50));

        });

    }




    @FXML
    private void fullScreenPreview(){
        try {
            Stage previewStage = new Stage();
            Group root = new Group();
            Scene scene = new Scene(root);

            ImageView prevView = new ImageView(previewLogo.getImage());
            prevView.setFitHeight(previewLogo.getImage().getHeight());
            prevView.setFitWidth(previewLogo.getImage().getWidth());
            root.getChildren().add(prevView);

            previewStage.setTitle("Preview Logo");
            previewStage.setWidth(prevView.getImage().getWidth());
            previewStage.setHeight(prevView.getImage().getHeight());
            previewStage.setScene(scene);
            previewStage.setFullScreen(false);
            previewStage.initOwner(windowManager.getStage());
            previewStage.show();
        }catch(NullPointerException e){
            LOGGER.info("fullScreenPreview ->",e);
        }
    }


    public void changePreviewSize(String newValue,int textField){
        if(selectedLogo!=null){
            try {
                int height = 0;
                int width = 0;
                if(textField==0) { //if textfield height changing
                    height = Integer.parseInt(newValue);
                    width  = Integer.parseInt(txPreviewWidth.getText());
                }else{
                    width = Integer.parseInt(newValue);
                    height = Integer.parseInt(txPreviewHeight.getText());
                }

                javafx.scene.image.Image image = SwingFXUtils.toFXImage(logoService.getPreviewForLogo(selectedLogo.getLogo(), selectedLogo.getRelativeRectangle(), width, height), null);
                previewLogo.setImage(image);
            } catch (NumberFormatException e) {
                LOGGER.error("Fehler: Bitte geben Sie eine Zahl an",e);
            } catch (ServiceException e) {
                LOGGER.error("Fehler: Bitte geben Sie eine Zahl an",e);
            }
        }
    }

    @FXML
    private void saveLogo(){
        LOGGER.error("Logo Add Button has been clicked");
        String name = txLogoName.getText();
        if(selectedProfile==null || name.trim().compareTo("") == 0 || txLogoLogo.getText().compareTo("Hochladen...") == 0 ||
                txLogoX.getText().isEmpty() || txLogoY.getText().isEmpty() || (txLogoBreite.getText().isEmpty() && txLogoHoehe.getText().isEmpty())){
            LOGGER.error("in error");
            showError("Sie müssen einen Namen eingeben, ein Logo hochladen und ein Profil auswählen!");
        }else {
            try {
                Logo newLogo = new Logo(name,txLogoLogo.getText());
                double width = 0.0;
                double height = 0.0;
                if(txLogoHoehe.getText().isEmpty() && !txLogoBreite.getText().isEmpty()){
                    width = Double.valueOf(txLogoBreite.getText());
                    height = logoService.calculateRelativeHeight(width,newLogo,Double.valueOf(txPreviewWidth.getText()),Double.valueOf(txPreviewHeight.getText()));
                    //(((width*100)/Double.valueOf(txPreviewWidth.getText()))*Double.valueOf(txPreviewHeight.getText()))/100;
                }else if(!txLogoHoehe.getText().isEmpty() && txLogoBreite.getText().isEmpty()){
                    height = Double.valueOf(txLogoHoehe.getText());
                    width = logoService.calculateRelativeWidth(height,newLogo,Double.valueOf(txPreviewWidth.getText()),Double.valueOf(txPreviewHeight.getText()));
                    //(((height*100)/Double.valueOf(txPreviewHeight.getText()))*Double.valueOf(txPreviewWidth.getText()))/100;
                }else if(txLogoHoehe.getText().isEmpty() && txLogoBreite.getText().isEmpty()){
                   // showError("Bitte in Breite und Höhe Eingabefelder nur Zahlen eingeben.");
                    throw new NumberFormatException();
                }else{
                    width = Double.valueOf(txLogoBreite.getText());
                    height = Double.valueOf(txLogoHoehe.getText());
                }

                RelativeRectangle newPosition = new RelativeRectangle(Double.valueOf(txLogoX.getText()),Double.valueOf(txLogoY.getText()),width,height);

                LOGGER.info("adding the new pairLogo to tableView...");

                if(txLogoLogo.getId().isEmpty()) //if given logo is not an existing logo
                    newLogo = pservice.addLogo(newLogo);
                else
                    newLogo = pservice.getLogo(Integer.valueOf(txLogoLogo.getId()));

                Profile.PairLogoRelativeRectangle p = pservice.addPairLogoRelativeRectangle(selectedProfile.getId(),newLogo.getId(),newPosition);

                logoList.add(p);

                txLogoName.getEntries().add(newLogo.getLabel().toLowerCase()+" #"+newLogo.getId());

                ImageView imgView =new ImageView(new Image("file:"+newLogo.getPath(),30,30,true,true));

                imgView.setId(newLogo.getPath());

                txLogoName.getImgViews().put(newLogo.getLabel().toLowerCase()+" #"+newLogo.getId(),imgView);
                txLogoName.clear();
                txLogoBreite.clear();
                txLogoHoehe.clear();
                txLogoX.clear();
                txLogoY.clear();
                txLogoLogo.setText("Hochladen...");
                txLogoUpload.setBackground(imageHandler.getBackground("/images/upload1.png",50,50));


            } catch (ServiceException e) {
                LOGGER.error("Fehler: Profil konnte nicht erstellt werden..."+e.getMessage());
            } catch (NumberFormatException e){
                showError("Bitte in Position Eingabefelder (Xstart,Ystart,Breite,Höhe) nur Zahlen eingeben.");
                LOGGER.error("Fehler: Bitte nur Zahlen eingeben. "+e.getMessage());
                //TODO: Dialogfenster öffnen.
            }
        }
    }

    @FXML
    private void logoUpload(){
        fileChooser.setTitle("Logo Hochladen...");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                //  new FileChooser.ExtensionFilter("All Images", "*.*")
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            txLogoLogo.setText(file.getAbsolutePath());
            txLogoLogo.setId("");
            txLogoUpload.setBackground(imageHandler.getBackground("/images/upload2.png",50,50));
            if(selectedProfile==null || txLogoName.getText().isEmpty() || txLogoLogo.getText().compareTo("Hochladen...") == 0 ||
                    txLogoX.getText().isEmpty() || txLogoY.getText().isEmpty() || (txLogoBreite.getText().isEmpty() && txLogoHoehe.getText().isEmpty())){
                txLogoAdd.setBackground(imageHandler.getBackground("/images/add3.png",50,50));
            }else
                txLogoAdd.setBackground(imageHandler.getBackground("/images/add.png",50,50));


        }
    }

    protected void refreshTableLogo(List<Profile.PairLogoRelativeRectangle> logoList,Profile selected){
        LOGGER.info("refreshing the Logo table...");
        selectedProfile = selected;
        this.logoList.clear();
        this.logoList.addAll(logoList);
        tableLogo.setItems(this.logoList);
        try {
            previewLogo.setImage( SwingFXUtils.toFXImage(logoService.getPreviewForMultipleLogos(logoList,Integer.valueOf(txPreviewWidth.getText()),Integer.valueOf(txPreviewHeight.getText())),null));
        } catch (ServiceException e) {
            LOGGER.error("refreshTablelogo->Error",e);
        }
    }
    protected void refreshLogoAutoComplete(Profile selected) throws ServiceException {
        selectedProfile = selected;
        txLogoName.getEntries().addAll(logo2StringArray(pservice.getAllLogosOfProfile(selectedProfile)));
        txLogoName.getImgViews().putAll(logo2imgViews(pservice.getAllLogosOfProfile(selectedProfile)));
        txLogoName.setTxLogoPath(txLogoLogo);
        txLogoName.setTxLogoUpload(txLogoUpload);
        txLogoName.setImageHandler(imageHandler);
    }

    protected List<String> logo2StringArray(List<Logo> logos){
        List<String> ret = new ArrayList<>();
        for (Logo logo:logos){
            ret.add(logo.getLabel().toLowerCase()+" #"+logo.getId());
        }
        return ret;
    }
    protected Map<String,ImageView> logo2imgViews(List<Logo> logos){
        Map<String,ImageView> ret = new HashMap<>();
        for (Logo logo:logos){
            String logoPath;
            if(new File(logo.getPath()).isFile())
                logoPath = logo.getPath();
            else
                logoPath = System.getProperty("user.dir")+"/src/main/resources/images/noimage.png";

            ImageView imgView =new ImageView(new Image("file:"+logoPath,30,30,true,true));

            imgView.setId((logoPath.contains("noimage.png")?"":logoPath));

            ret.put(logo.getLabel().toLowerCase()+" #"+logo.getId(),imgView);
        }
        return ret;
    }

}
