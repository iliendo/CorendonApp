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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import supervisor.Airport;
import supervisor.LuggageType;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class Supervisor_manage_luggageTypeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    final ObservableList<LuggageType> data = FXCollections.observableArrayList();
    
    
    @FXML
    private TableView tblView;

    @FXML
    private TableColumn tblC_id;

    @FXML
    private TableColumn tblC_Luggage;

    @FXML
    private JFXTextField txtnew;

    @FXML
    private JFXTextField txtold;

    @FXML
    void on_Add(ActionEvent event) {

    }

    @FXML
    void on_Delete(ActionEvent event) {

    }

    @FXML
    void on_Update(ActionEvent event) {

    }

    @FXML
    void on_table_click(MouseEvent event) {

    }
    
    
    
    /**
     * Tableview vullen met data uit de database
     */
    public void FillTable() {

        try {
            String query = "select * from luggage";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new LuggageType(
                        rs.getInt("LuggageType_id"),
                        rs.getString("LuggageType")
                ));
                tblView.setItems(data);
                tblC_id.setCellValueFactory(new PropertyValueFactory<>("LuggageType_id"));
                tblC_Luggage.setCellValueFactory(new PropertyValueFactory<>("LuggageType"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_manage_luggageTypeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * iedere keer als iets wordt toegevoegd, aangepast of verwijderd, wordt de
     * tableview gerefreshed
     */
    public void refreshTable() {
        data.clear();
        try {
            String query = "select * from luggage";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new LuggageType(
                        rs.getInt("LuggageType_id"),
                        rs.getString("LuggageType")
                ));
                tblView.setItems(data);
                tblC_id.setCellValueFactory(new PropertyValueFactory<>("LuggageType_id"));
                tblC_Luggage.setCellValueFactory(new PropertyValueFactory<>("LuggageType"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_manage_brandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FillTable();
    }    
    
}
