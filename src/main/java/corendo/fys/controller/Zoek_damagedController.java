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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import medewerkers.zoek_damage;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class Zoek_damagedController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    final ObservableList<zoek_damage> data = FXCollections.observableArrayList();

    @FXML
    private TableView<zoek_damage> tblTable;

    @FXML
    private TableColumn c1;

    @FXML
    private TableColumn c2;

    @FXML
    private TableColumn c3;

    @FXML
    private TableColumn c4;

    @FXML
    private JFXTextField txtzoeken;

    public void FillTable() {

        try {
            String query_luggage = "SELECT * FROM damagedluggage "
                    + "INNER JOIN passenger ON damagedluggage.Passenger_id = passenger.Passenger_id ";
            stmt = conn.prepareStatement(query_luggage);
            rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new zoek_damage(
                        rs.getString("damagedLuggage_id"),
                        rs.getString("Date"),
                        rs.getString("LuggageTag"),
                        rs.getString("Firstname"))
                );
                tblTable.setItems(data);
                c1.setCellValueFactory(new PropertyValueFactory("damagedLuggage_id"));
                c2.setCellValueFactory(new PropertyValueFactory("Date"));
                c3.setCellValueFactory(new PropertyValueFactory("LuggageTag"));
                c4.setCellValueFactory(new PropertyValueFactory("Firstname"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Zoek_damagedController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FillTable();

    }

}
