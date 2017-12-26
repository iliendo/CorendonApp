package corendo.fys.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import corendo.fys.jdbcDBconnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import medewerkers.zoek_luggage;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class ZoekenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    @FXML
    private TableView<zoek_luggage> tblLuggage;

    @FXML
    private TableColumn regisNr;

    @FXML
    private TableColumn dateFound;

    @FXML
    private TableColumn timeFound;

    @FXML
    private TableColumn luggageType;

    @FXML
    private TableColumn brand;

    @FXML
    private TableColumn Firstname;

    @FXML
    private TableColumn status;

    @FXML
    private JFXComboBox<?> ddlStatus;

    @FXML
    private JFXTextField txt_zoek_status;

    @FXML
    private JFXTextField txt_zoek_name;

    @FXML
    private JFXTextField txt_zoek_luggageType;

    @FXML
    private JFXTextField txt_zoek_brand;

    @FXML
    private BorderPane fullStatusDetailsContent;

    @FXML
    private Label lblRegisNr;

    @FXML
    private Label lblDateFound;

    @FXML
    private Label lblTimeFound;

    @FXML
    private Label lblLuggageType;

    @FXML
    private Label lblBrand;

    @FXML
    private Label lblPassenger;

    @FXML
    private Label lblStatus;

    @FXML
    void on_Close(ActionEvent event) {
        fullStatusDetailsContent.setVisible(false);
    }

    @FXML
    void on_table_click(MouseEvent event) {
        if (event.getClickCount() > 1) {
            fullStatusDetailsContent.setVisible(true);
            try {
                zoek_luggage zoek = (zoek_luggage) tblLuggage.getSelectionModel().getSelectedItem();
                String query = "SELECT * FROM luggage "
                        + "INNER JOIN luggagetype ON luggage.LuggageType_id = luggagetype.LuggageType_id "
                        + "INNER JOIN brand ON luggage.Brand_id = brand.Brand_id "
                        + "INNER JOIN passenger ON luggage.Passenger_id = passenger.Passenger_id "
                        + "INNER JOIN status ON luggage.Status_id = status.Status_id "
                        + "WHERE Luggage_id = ?";

                stmt = conn.prepareStatement(query);
                stmt.setString(1, zoek.getLuggage_id());
                rs = stmt.executeQuery();
                while (rs.next()) {
                    lblRegisNr.setText(rs.getString("Luggage_id"));
                    lblDateFound.setText(rs.getString("DateFound"));
                    lblTimeFound.setText(rs.getString("TimeFound"));
                    lblLuggageType.setText(rs.getString("LuggageType"));
                    lblBrand.setText(rs.getString("Brand"));
                    lblPassenger.setText(rs.getString("Firstname"));
                    lblStatus.setText(rs.getString("Status"));

                }
                stmt.close();

            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    final ObservableList<zoek_luggage> data = FXCollections.observableArrayList();
    FilteredList<zoek_luggage> filteredData = new FilteredList<>(data);

    @FXML
    void on_zoek_status(KeyEvent event) {

    }

    public void FillTable() {
        try {
            String query_luggage = "SELECT * FROM luggage "
                    + "INNER JOIN luggagetype ON luggage.LuggageType_id = luggagetype.LuggageType_id "
                    + "INNER JOIN brand ON luggage.Brand_id = brand.Brand_id "
                    + "INNER JOIN passenger ON luggage.Passenger_id = passenger.Passenger_id "
                    + "INNER JOIN status ON luggage.Status_id = status.Status_id";

            stmt = conn.prepareStatement(query_luggage);
            rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new zoek_luggage(
                        rs.getString("Luggage_id"),
                        rs.getString("DateFound"),
                        rs.getString("TimeFound"),
                        rs.getString("LuggageType"),
                        rs.getString("Brand"),
                        rs.getString("Firstname"),
                        rs.getString("Status"))
                );
                tblLuggage.setItems(data);
                regisNr.setCellValueFactory(new PropertyValueFactory("Luggage_id"));
                dateFound.setCellValueFactory(new PropertyValueFactory("DateFound"));
                timeFound.setCellValueFactory(new PropertyValueFactory("TimeFound"));
                luggageType.setCellValueFactory(new PropertyValueFactory("LuggageType"));
                brand.setCellValueFactory(new PropertyValueFactory("Brand"));
                Firstname.setCellValueFactory(new PropertyValueFactory("Firstname"));
                status.setCellValueFactory(new PropertyValueFactory("Status"));

            }

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void zoekStatus() {
        FilteredList<zoek_luggage> filteredData = new FilteredList<>(data, p -> true);
        txt_zoek_status.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(luggage -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (luggage.getStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                }
                return false; // Does not match.
            });
        });

        SortedList<zoek_luggage> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblLuggage.comparatorProperty());
        tblLuggage.setItems(sortedData);
    }

    public void zoekName() {
        FilteredList<zoek_luggage> filteredData = new FilteredList<>(data, p -> true);
        txt_zoek_name.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(luggage -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (luggage.getFirstname().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                }
                return false; // Does not match.
            });
        });

        SortedList<zoek_luggage> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblLuggage.comparatorProperty());
        tblLuggage.setItems(sortedData);
    }

    public void zoekLuggage() {
        FilteredList<zoek_luggage> filteredData = new FilteredList<>(data, p -> true);
        txt_zoek_luggageType.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(luggage -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (luggage.getLuggageType().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                }
                return false; // Does not match.
            });
        });

        SortedList<zoek_luggage> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblLuggage.comparatorProperty());
        tblLuggage.setItems(sortedData);
    }

    public void zoekBrand() {
        FilteredList<zoek_luggage> filteredData = new FilteredList<>(data, p -> true);
        txt_zoek_brand.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(luggage -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (luggage.getBrand().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                }
                return false; // Does not match.
            });
        });

        SortedList<zoek_luggage> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblLuggage.comparatorProperty());
        tblLuggage.setItems(sortedData);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FillTable();

        FilteredList<zoek_luggage> filteredData = new FilteredList<>(data, p -> true);

        txt_zoek_status.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(luggage -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (luggage.getStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        txt_zoek_name.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(luggage -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (luggage.getFirstname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        txt_zoek_luggageType.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(luggage -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (luggage.getLuggageType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        txt_zoek_brand.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(luggage -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (luggage.getBrand().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<zoek_luggage> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblLuggage.comparatorProperty());
        tblLuggage.setItems(sortedData);
    }
}
