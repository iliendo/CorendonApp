package corendo.fys.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import corendo.fys.Password;
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
    private JFXPasswordField txtOldPassword;

    @FXML
    private JFXPasswordField txtNewPassword;

    @FXML
    private JFXPasswordField txtNewPasswordRepeat;

    Login_MedewerkerController employeeEmail = new Login_MedewerkerController();
    Password hashedPassword = new Password();

    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    @FXML
    void on_change_password(ActionEvent event) {

        String oldPassQuery = "SELECT * FROM employee WHERE email ='" + employeeEmail.getEmail() + "' AND password= '" + hashedPassword.getHashedPassword(txtOldPassword.getText()) + "'";

        try {
            stmt = conn.prepareStatement(oldPassQuery);
            rs = stmt.executeQuery();

            if (txtOldPassword.getText().isEmpty() || txtNewPassword.getText().isEmpty() || txtNewPasswordRepeat.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Fill in all the fields!");

                alert.showAndWait();
            } else if (rs.next() && txtNewPassword.getText().equals(txtNewPasswordRepeat)) {
                String query = "UPDATE employee SET Password=? WHERE Email=? AND Password=?";
                try {
                    stmt = conn.prepareStatement(query);
                    stmt.setString(1, hashedPassword.getHashedPassword(txtNewPassword.getText()));
                    stmt.setString(2, employeeEmail.getEmail());
                    stmt.setString(3, hashedPassword.getHashedPassword(txtOldPassword.getText()));

                    stmt.executeUpdate();
                    stmt.close();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setContentText("The new password has been set!");

                    alert.showAndWait();

                } catch (SQLException ex) {
                    Logger.getLogger(Change_PasswordController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (!txtNewPassword.getText().equals(txtNewPasswordRepeat)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The new passwords didn't match or the old password is wrong!");

                alert.showAndWait();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Change_PasswordController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
