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
    void on_Me_User(ActionEvent event) {
        if (event.getSource() == meOption) {
            show_me_user_info.setVisible(true);
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

    }
    
        @FXML
    void on_change_password(ActionEvent event) {
        veranderContentNode("Change_Password.fxml");
    }
    
    @FXML
    void on_Airports(ActionEvent event) {
        veranderContentNode("supervisor_add_airport.fxml");
    }

    @FXML
    void on_formulier_wijzig(ActionEvent event) {
        veranderContentNode("Supervisor_Formulier_wijzigen.fxml");
    }

    @FXML
    void on_medewerker_toevoegen(ActionEvent event) {
        veranderContentNode("Supervisor_medewerker_toevoegen.fxml");
    }

    @FXML
    void on_statistic(ActionEvent event) {
        veranderContentNode("supervisor_statictiek.fxml");
    }

    @FXML
    void on_update_brands(ActionEvent event) {
        veranderContentNode("supervisor_manage_brand.fxml");
    }

    @FXML
    void on_update_employee(ActionEvent event) {
        veranderContentNode("Supervisor_medewerker_updaten.fxml");
    }

    @FXML
    void on_add_brands(ActionEvent event) {
        veranderContentNode("supervisor_add_brand.fxml");
    }

    @FXML
    void on_airport(ActionEvent event) {
        veranderContentNode("supervisor_manage_airport.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
