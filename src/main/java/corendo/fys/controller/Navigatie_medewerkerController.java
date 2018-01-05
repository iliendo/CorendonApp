package corendo.fys.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jfoenix.controls.JFXButton;
import corendo.fys.AppUtilities;
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
 * @author Gabriel
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
    void on_Me_User(ActionEvent event) {
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
    void on_change_password(ActionEvent event) {
        veranderContentNode("Change_Password.fxml");
    }

    @FXML
    void dashbord_medewerker(MouseEvent event) {
        show_me_user_info.setVisible(false);
    }

    @FXML
    void on_Gevonden_bagage(ActionEvent event) {
        veranderContentNode("Gevonden_bagage.fxml");
    }

    @FXML
    void on_verloren_bagage(ActionEvent event) {
        veranderContentNode("Verloren_bagage.fxml");
    }

    @FXML
    void on_Zoeken(ActionEvent event) {
        veranderContentNode("Zoeken.fxml");
    }

    @FXML
    void on_import_register(ActionEvent event) {
        veranderContentNode("Excel_Importeren.fxml");
    }

    public void setInfo(String name, String country) {
        this.lblMedewerkerName.setText(name);
        this.lblMedewerkerCountry.setText(country);
    }

    @FXML
    void on_Language(ActionEvent event) {
        if (languageBox.getValue() == "Dutch") {
            veranderContentNodeWithResource("Verloren_bagage.fxml", "nl");
        } else {
            veranderContentNodeWithResource("Verloren_bagage.fxml", "en");
        }
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
