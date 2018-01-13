package corendo.fys.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import supervisor.Airport;
import supervisor.Brand;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class Supervisor_manage_airportController implements Initializable {

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
    private JFXTextField txtAirportNew;

    @FXML
    private Label lblOldAirport;

    @FXML
    void on_Update(ActionEvent event) {
        String query = "UPDATE airport SET Airport_name=? WHERE Airport_name='" + lblOldAirport.getText() + "'";
        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, txtAirportNew.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Update executed");

            stmt.execute();
            stmt.close();

            lblOldAirport.setText(null);
            txtAirportNew.setText(null);

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_manage_airportController.class.getName()).log(Level.SEVERE, null, ex);
        }

        refreshTable();
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
            Logger.getLogger(Supervisor_manage_airportController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            Logger.getLogger(Supervisor_manage_airportController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void onEdit() {
        if (tblAirport.getSelectionModel().getSelectedItem() != null) {
            Airport selectedAirport = tblAirport.getSelectionModel().getSelectedItem();
            lblOldAirport.setText(selectedAirport.getAirport_name());
        }

    }

    @FXML
    void on_table_click(MouseEvent event) {
        if (event.getClickCount() > 1) {
            onEdit();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FillTable();
    }

}
