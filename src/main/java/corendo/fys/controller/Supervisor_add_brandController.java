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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import supervisor.Brand;

/**
 * FXML Controller class
 *
 * @author Gabriel & Ilias
 */
public class Supervisor_add_brandController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    final ObservableList<Brand> data = FXCollections.observableArrayList();

    @FXML
    private TableView<Brand> tblBrand;

    @FXML
    private TableColumn tblC_id;

    @FXML
    private TableColumn tblC_Brand;

    @FXML
    private JFXTextField txtBrand;

    @FXML
    private JFXTextField txtBrandNew;

    /**
     *
     * @param event om een brand toe te voegen in de database
     */
    @FXML
    void on_Add(ActionEvent event) {
        String query = "INSERT INTO brand (Brand) VALUES (?)";
        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, txtBrand.getText());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_manage_brandController.class.getName()).log(Level.SEVERE, null, ex);
        }

        refreshTable();
        txtBrand.setText(null);
    }

    /**
     *
     * @param event om de geselecteerde waarde aan te passen in de database
     */
    @FXML
    void on_Update(ActionEvent event) {
        String query = "UPDATE brand SET Brand=? WHERE Brand='" + txtBrand.getText() + "'";
        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, txtBrandNew.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Update executed");

            stmt.execute();
            stmt.close();

            txtBrand.setText(null);
            txtBrandNew.setText(null);
            


        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_manage_brandController.class.getName()).log(Level.SEVERE, null, ex);
        }

        refreshTable();
    }

    /**
     * om een record te verwijderen in de database
     *
     * @param event
     */
    @FXML
    void on_Delete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to delete " + txtBrand.getText() + "?");

        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            try {
                String query = "delete from brand where Brand = ?";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, txtBrand.getText());

                stmt.executeUpdate();
                stmt.close();

            } catch (SQLException ex) {
                Logger.getLogger(Supervisor_manage_brandController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        refreshTable();
        txtBrand.setText(null);

    }

    /**
     * om de tableview klikbaar te maken en methode "onEdit" uit te voeren
     *
     * @param event
     */
    @FXML
    void on_table_click(MouseEvent event) {
        if (event.getClickCount() > 1) {
            onEdit();
        }
    }

    /**
     * geselecteerde kolom halen en plaatsen in een tekstveld
     */
    public void onEdit() {
        if (tblBrand.getSelectionModel().getSelectedItem() != null) {
            Brand selectedBrand = tblBrand.getSelectionModel().getSelectedItem();
            txtBrand.setText(selectedBrand.getBrand());
        }
    }

    /**
     * Tableview vullen met data uit de database
     */
    public void FillTable() {

        try {
            String query = "select * from brand";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new Brand(
                        rs.getInt("Brand_id"),
                        rs.getString("Brand")
                ));
                tblBrand.setItems(data);
                tblC_id.setCellValueFactory(new PropertyValueFactory<>("Brand_id"));
                tblC_Brand.setCellValueFactory(new PropertyValueFactory<>("Brand"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_manage_brandController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * iedere keer als iets wordt toegevoegd, aangepast of verwijderd, wordt de
     * tableview gerefreshed
     */
    public void refreshTable() {
        data.clear();
        try {
            String query = "select * from brand";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new Brand(
                        rs.getInt("Brand_id"),
                        rs.getString("Brand")
                ));
                tblBrand.setItems(data);
                tblC_id.setCellValueFactory(new PropertyValueFactory<>("Brand_id"));
                tblC_Brand.setCellValueFactory(new PropertyValueFactory<>("Brand"));
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
