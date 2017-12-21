package corendo.fys.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import corendo.fys.jdbcDBconnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class Supervisor_statictiekController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private BarChart barChart;
    
    @FXML
    private CategoryAxis xStatus;

    @FXML
    private NumberAxis yStatus;
    
    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    
    public int getTotalLost(){
        int count = 0;
        String query = "SELECT COUNT(Status_id) FROM luggage where Status_id='1'";
        try{
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while(rs.next()){
                count = rs.getInt(1);
            }
            
        }catch (SQLException ex) {
            Logger.getLogger(Supervisor_statictiekController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }
    
    public int getTotalFound(){
        int count = 0;
        String query = "SELECT COUNT(Status_id) FROM luggage where Status_id='2'";
        try{
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while(rs.next()){
                count = rs.getInt(1);
            }
            
        }catch (SQLException ex) {
            Logger.getLogger(Supervisor_statictiekController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        XYChart.Series set1 = new XYChart.Series<>();
        set1.getData().add(new XYChart.Data("Lost",getTotalLost()));
        set1.getData().add(new XYChart.Data("Found",getTotalFound()));
        set1.getData().add(new XYChart.Data("Damaged",15));
        
        barChart.getData().addAll(set1);
    }    
    
}
