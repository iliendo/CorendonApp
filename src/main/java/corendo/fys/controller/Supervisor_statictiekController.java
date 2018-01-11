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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import java.util.Calendar;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

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
    private PieChart pieChartDrieDagen;
    
    @FXML
    private PieChart pieChartEenWeek;
    
    @FXML
    private PieChart pieChartEenMaand;
    
    @FXML
    private Label pieChartName;

    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    Calendar cal = Calendar.getInstance();
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int month = cal.get(Calendar.MONTH) + 1;
    int year = cal.get(Calendar.YEAR);

    int minDrieDagen = day - 3;
    int minZevenDagen = day - 7;
    int minMonth = month - 1;
   
    String threeDays = String.valueOf(minDrieDagen);
    String sevenDays = String.valueOf(minZevenDagen);
    String jaar = String.valueOf(year);
    String maand = String.valueOf(month);

    String drieDagen = jaar + "-" + maand + "-" + threeDays;
    String eenWeek = jaar + "-" + maand + "-" + sevenDays;
    
    public int getTotalLostDrieDagen() {
        int count = 0;
        String query = "SELECT COUNT(Status_id) FROM luggage where Status_id='1' AND DateFound > '"+drieDagen+"'";
        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_statictiekController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public int getTotalFoundDrieDagen() {
        int count = 0;
        String query = "SELECT COUNT(Status_id) FROM luggage where Status_id='2' AND DateFound > '"+drieDagen+"'";
        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_statictiekController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }
    
    public int getTotalLostEenWeek() {
        int count = 0;
        String query = "SELECT COUNT(Status_id) FROM luggage where Status_id='1' AND DateFound > '"+eenWeek+"'";
        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_statictiekController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public int getTotalFoundEenWeek() {
        int count = 0;
        String query = "SELECT COUNT(Status_id) FROM luggage where Status_id='2' AND DateFound > '"+eenWeek+"'";
        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_statictiekController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }
    
    public int getTotalLostEenMaand() {
        String minMaand;
        if(minMonth == 0){
            minMaand = "01";
        }else{
            minMaand = String.valueOf(minMonth);
        }
        String eenMaand = jaar + "-" + minMaand;
        int count = 0;
        String query = "SELECT COUNT(Status_id) FROM luggage where Status_id='1' AND DateFound > '"+eenMaand+"'";
        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_statictiekController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public int getTotalFoundEenMaand() {
        String minMaand;
        if(minMonth == 0){
            minMaand = "01";
        }else{
            minMaand = String.valueOf(minMonth);
        }
        String eenMaand = jaar + "-" + minMaand;
        int count = 0;
        String query = "SELECT COUNT(Status_id) FROM luggage where Status_id='2' AND DateFound > '"+eenMaand+"'";
        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_statictiekController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    ObservableList<PieChart.Data> pieChartDataEenWeek
            = FXCollections.observableArrayList(
                    new PieChart.Data("Lost", getTotalLostEenWeek()),
                    new PieChart.Data("Found", getTotalFoundEenWeek()),
                    new PieChart.Data("Dammaged", 15));
    final PieChart chart2 = new PieChart(pieChartDataEenWeek);
    
    
    ObservableList<PieChart.Data> pieChartData
            = FXCollections.observableArrayList(
                    new PieChart.Data("Lost", getTotalLostDrieDagen()),
                    new PieChart.Data("Found", getTotalFoundDrieDagen()),
                    new PieChart.Data("Dammaged", 15));
    final PieChart chart = new PieChart(pieChartData);
  
    
    ObservableList<PieChart.Data> pieChartDataEenMaand
            = FXCollections.observableArrayList(
                    new PieChart.Data("Lost", getTotalLostEenMaand()),
                    new PieChart.Data("Found", getTotalFoundEenMaand()),
                    new PieChart.Data("Dammaged", 15));
    final PieChart chart3 = new PieChart(pieChartDataEenMaand);
    
    @FXML
    private void drieDagen(ActionEvent event) {
        pieChartName.setText("Drie dagen");
        pieChartDrieDagen.setVisible(true);
        pieChartEenWeek.setVisible(false);
        pieChartEenMaand.setVisible(false);
        pieChartDrieDagen.setData(this.pieChartData);
 
    }
    
    @FXML
    private void eenWeek(ActionEvent event) {
        pieChartName.setText("Zeven dagen");
        pieChartDrieDagen.setVisible(false);
        pieChartEenWeek.setVisible(true);
        pieChartEenWeek.setData(this.pieChartDataEenWeek);
      
    }
    
    @FXML
    private void eenMaand(ActionEvent event) {
        pieChartName.setText("Een maand");
        pieChartDrieDagen.setVisible(false);
        pieChartEenWeek.setVisible(false);
        pieChartEenMaand.setVisible(true);
        pieChartEenMaand.setData(this.pieChartDataEenMaand);
      
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
    }

}
