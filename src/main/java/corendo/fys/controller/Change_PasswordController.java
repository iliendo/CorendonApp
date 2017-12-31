package corendo.fys.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import corendo.fys.jdbcDBconnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 * Laat de ingelogde medewerker het huidig wachtwoord aanpassen
 *
 * @author Ilias Azagagh
 */
public class Change_PasswordController implements Initializable {

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtOldPassword;

    @FXML
    private JFXPasswordField txtNewPassword;

    @FXML
    private JFXPasswordField txtNewPasswordRepeat;

    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    @FXML
    void on_change_password(ActionEvent event) {

        if (txtNewPassword.getText().equals(txtNewPasswordRepeat.getText())) {
            String query = "UPDATE employee SET Password=? WHERE Email=?";
            try {
                stmt = conn.prepareStatement(query);
                stmt.setString(1, txtNewPassword.getText());
                stmt.setString(2, txtEmail.getText());

                stmt.executeUpdate();
                stmt.close();

            } catch (SQLException ex) {
                Logger.getLogger(Change_PasswordController.class.getName()).log(Level.SEVERE, null, ex);

                System.out.println("\n" + query);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The new passwords didn't match!");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText("Your password has been changed!");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
