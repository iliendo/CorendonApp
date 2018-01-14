package corendo.fys.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jfoenix.controls.JFXButton;
import corendo.fys.AppUtilities;
import corendo.fys.Language;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Locale;

/**
 * FXML Controller class
 *
 * @author ricardopolenta
 */
public class Navigatie_medewerkerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXButton meOption;

    @FXML
    private AnchorPane show_me_user_info;

    @FXML
    private StackPane all_content;

    @FXML
    private Label lblMedewerkerName;

    @FXML
    private Label lblMedewerkerCountry;

    @FXML
    private ComboBox languageBox;

    @FXML
    void on_Me_User(ActionEvent event
    ) {
        if (event.getSource() == meOption) {
            show_me_user_info.setVisible(true);
        }
    }

    @FXML
    void on_logOut(ActionEvent event) throws IOException {
        Parent parent1 = FXMLLoader.load(getClass().getResource("/corendo/fys/Login_Medewerker.fxml"));
        Scene scene = new Scene(parent1);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }

    @FXML
    void on_change_password(ActionEvent event
    ) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("Change_Password.fxml", "nl");
        } else {
            veranderContentNodeWithResource("Change_Password.fxml", "en");
        }
        Language.currentPage = "Change_Password.fxml";
    }

    @FXML
    void dashbord_medewerker(MouseEvent event
    ) {
        show_me_user_info.setVisible(false);
    }

    @FXML
    void on_Gevonden_bagage(ActionEvent event
    ) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("Gevonden_bagage.fxml", "nl");
        } else {
            veranderContentNodeWithResource("Gevonden_bagage.fxml", "en");
        }
        Language.currentPage = "Gevonden_bagage.fxml";
    }

    @FXML
    void on_verloren_bagage(ActionEvent event
    ) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("Verloren_bagage.fxml", "nl");
        } else {
            veranderContentNodeWithResource("Verloren_bagage.fxml", "en");
        }
        Language.currentPage = "Verloren_bagage.fxml";
    }

    @FXML
    void on_Zoeken(ActionEvent event
    ) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("Zoeken.fxml", "nl");
        } else {
            veranderContentNodeWithResource("Zoeken.fxml", "en");
        }
        Language.currentPage = "Zoeken.fxml";
    }

    @FXML
    void on_import_register(ActionEvent event
    ) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("Excel_Importeren.fxml", "nl");
        } else {
            veranderContentNodeWithResource("Excel_Importeren.fxml", "en");
        }
        Language.currentPage = "Excel_Importeren.fxml";
    }

    public void setInfo(String name, String country) {
        this.lblMedewerkerName.setText(name);
        this.lblMedewerkerCountry.setText(country);
    }

    @FXML
    void on_Language(ActionEvent event) {
        if (languageBox.getValue() == "Dutch") {
            veranderContentNodeWithResource(Language.currentPage, "nl");
            Language.taal = 1;
        } else {
            veranderContentNodeWithResource(Language.currentPage, "en");
            Language.taal = 2;
        }
    }

    @FXML
    void on_Damage(ActionEvent event) {
        veranderContentNode("SchadeClaim.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        String dutch = "Dutch";
//        String english = "English";
        languageBox.getItems().addAll("Dutch", "English");
    }

    public void veranderContentNodeWithResource(String schermFileName, String language) {
        Parent parent;
        try {
            parent = AppUtilities.loadScreen(schermFileName, language);
        } catch (IOException ex) {
            // TODO show error
            return;
        }
        all_content.getChildren().setAll(parent);
    }

    public void veranderContentNode(String schermFileName) {
        Parent parent;
        try {
            parent = AppUtilities.loadScreen(schermFileName);
        } catch (IOException ex) {
            // TODO show error
            return;
        }
        all_content.getChildren().setAll(parent);
    }
}
