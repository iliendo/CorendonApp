package corendo.fys.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jfoenix.controls.JFXTextField;
import corendo.fys.CheckVelden;
import corendo.fys.jdbcDBconnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import supervisor.Airport;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class Supervisor_add_airportController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    final ObservableList<Airport> data = FXCollections.observableArrayList();

    @FXML
    private TableView<Airport> tblAirport;

    @FXML
    private TableColumn tblC_id;

    @FXML
    private TableColumn tblC_Airport;

    @FXML
    private JFXTextField txtAirport;

    @FXML
    private Label lblDeleteAirport;

    @FXML
    void on_Add(ActionEvent event) {
        if (!checkVeld()) {
            try {
                String query = "INSERT INTO airport (Airport_name) VALUES(?)";

                stmt = conn.prepareStatement(query);
                stmt.setString(1, txtAirport.getText());

                stmt.execute();

            } catch (SQLException ex) {
                Logger.getLogger(Supervisor_add_airportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        refreshTable();

    }

    @FXML
    void on_Delete(ActionEvent event) {
        String justMessageText = "dubble click Airport to delete";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setContentText("Are you sure you want to delete ? \nThis action CANNOT be undone!");

        Optional<ButtonType> action = alert.showAndWait();

        if (!(lblDeleteAirport.getText().equals(justMessageText))) {
            if (action.get() == ButtonType.OK) {
                try {
                    String query = "DELETE FROM airport WHERE Airport_name=?";
                    stmt = conn.prepareStatement(query);
                    stmt.setString(1, lblDeleteAirport.getText());

                    stmt.executeUpdate();
                    stmt.close();

                    Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                    confirmation.setTitle("Information");
                    confirmation.setContentText(lblDeleteAirport.getText() + " is deleted!");

                    confirmation.showAndWait();
                    
                    lblDeleteAirport.setText(justMessageText);

                } catch (SQLException ex) {
                    Logger.getLogger(Supervisor_medewerker_toevoegenController.class.getName()).log(Level.SEVERE, null, ex);
                }

                refreshTable();

            } else if (action.get() == ButtonType.CANCEL) {
                Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                cancelAlert.setTitle("Information");
                cancelAlert.setContentText(lblDeleteAirport.getText() + " is not deleted!");

                action = cancelAlert.showAndWait();
            }
        } else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please select one of the airports to delete !");
            alert.showAndWait();
        }
    }

    @FXML
    void on_table_click(MouseEvent event) {
        if (event.getClickCount() > 1) {
            onEdit();
        }
    }

    public boolean checkVeld() {
        CheckVelden checkvelden = new CheckVelden();
        if (checkvelden.nullOrEmpty(txtAirport.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please, Fill the airport textbox !");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    public void FillTable() {

        try {
            String query = "select * from airport";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new Airport(
                        rs.getInt("Airport_id"),
                        rs.getString("Airport_name")
                ));
                tblAirport.setItems(data);
                tblC_id.setCellValueFactory(new PropertyValueFactory<>("Airport_id"));
                tblC_Airport.setCellValueFactory(new PropertyValueFactory<>("Airport_name"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_add_airportController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void onEdit() {
        if (tblAirport.getSelectionModel().getSelectedItem() != null) {
            Airport airport = tblAirport.getSelectionModel().getSelectedItem();
            lblDeleteAirport.setText(airport.getAirport_name());
        }
    }

    public void refreshTable() {
        data.clear();
        try {
            String query = "select * from airport";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new Airport(
                        rs.getInt("Airport_id"),
                        rs.getString("Airport_name")
                ));
                tblAirport.setItems(data);
                tblC_id.setCellValueFactory(new PropertyValueFactory<>("Airport_id"));
                tblC_Airport.setCellValueFactory(new PropertyValueFactory<>("Airport_name"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_add_airportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FillTable();
    }

}
