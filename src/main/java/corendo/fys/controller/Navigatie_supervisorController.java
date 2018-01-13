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

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class Navigatie_supervisorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane show_me_user_info;

    @FXML
    private JFXButton meOption;

    @FXML
    private StackPane all_content_supervisor;

    @FXML
    private JFXButton formulier_wijziging;

    @FXML
    private Label lblMedewerkerCountry;

    @FXML
    private Label lblMedewerkerName;

    @FXML
    private ComboBox languageBox;

    @FXML
    private Label lblWelkom;

    @FXML
    void on_Me_User(ActionEvent event) {
        if (event.getSource() == meOption) {
            show_me_user_info.setVisible(true);
        }
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
    void on_log_out(ActionEvent event) throws IOException {
        Parent parent1 = FXMLLoader.load(getClass().getResource("/corendo/fys/Login_Medewerker.fxml"));
        Scene scene = new Scene(parent1);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }

    @FXML
    void dashbord_medewerker(MouseEvent event) {
        show_me_user_info.setVisible(false);
    }

    @FXML
    void on_change_password(ActionEvent event) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("Change_Password.fxml", "nl");
        } else {
            veranderContentNodeWithResource("Change_Password.fxml", "en");
        }
        Language.currentPage = "Change_Password.fxml";

    }

    @FXML
    void on_Airports(ActionEvent event) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("supervisor_add_airport.fxml", "nl");
        } else {
            veranderContentNodeWithResource("supervisor_add_airport.fxml", "en");
        }
        Language.currentPage = "supervisor_add_airport.fxml";

    }

    @FXML
    void on_formulier_wijzig(ActionEvent event) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("Supervisor_Formulier_wijzigen.fxml", "nl");
        } else {
            veranderContentNodeWithResource("Supervisor_Formulier_wijzigen.fxml", "en");
        }
        Language.currentPage = "Supervisor_Formulier_wijzigen.fxml";

    }

    @FXML
    void on_medewerker_toevoegen(ActionEvent event) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("Supervisor_medewerker_toevoegen.fxml", "nl");
        } else {
            veranderContentNodeWithResource("Supervisor_medewerker_toevoegen.fxml", "en");
        }
        Language.currentPage = "Supervisor_medewerker_toevoegen.fxml";
    }

    @FXML
    void on_statistic(ActionEvent event) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("supervisor_statictiek.fxml", "nl");
        } else {
            veranderContentNodeWithResource("supervisor_statictiek.fxml", "en");
        }
        Language.currentPage = "supervisor_statictiek.fxml";
    }

    @FXML
    void on_update_brands(ActionEvent event) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("supervisor_manage_brand.fxml", "nl");
        } else {
            veranderContentNodeWithResource("supervisor_manage_brand.fxml", "en");
        }
        Language.currentPage = "supervisor_manage_brand.fxml";
    }

    @FXML
    void on_update_employee(ActionEvent event) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("Supervisor_medewerker_updaten.fxml", "nl");
        } else {
            veranderContentNodeWithResource("Supervisor_medewerker_updaten.fxml", "en");
        }
        Language.currentPage = "Supervisor_medewerker_updaten.fxml";
    }

    @FXML
    void on_add_brands(ActionEvent event) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("supervisor_add_brand.fxml", "nl");
        } else {
            veranderContentNodeWithResource("supervisor_add_brand.fxml", "en");
        }
        Language.currentPage = "supervisor_add_brand.fxml";
    }

    @FXML
    void on_airport(ActionEvent event) {
        if (Language.taal == 1) {
            veranderContentNodeWithResource("supervisor_manage_airport.fxml", "nl");
        } else {
            veranderContentNodeWithResource("supervisor_manage_airport.fxml", "en");
        }
        Language.currentPage = "supervisor_manage_airport.fxml";
    }

    public void setInfo(String name, String country) {
        this.lblMedewerkerName.setText(name);
        this.lblMedewerkerCountry.setText(country);
        this.lblWelkom.setText("Welcome, " + name);
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
        all_content_supervisor.getChildren().setAll(parent);
    }

    public void veranderContentNode(String schermFileName) {
        Parent parent;
        try {
            parent = AppUtilities.loadScreen(schermFileName);
        } catch (IOException ex) {
            ex.printStackTrace();
            // TODO show error
            return;
        }
        all_content_supervisor.getChildren().setAll(parent);
    }

}
