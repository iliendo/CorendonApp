package corendo.fys.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import corendo.fys.jdbcDBconnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;
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
import javafx.scene.layout.AnchorPane;
import supervisor.Employee;
import java.security.*;
import java.math.*;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Ilias Azagagh
 */
public class Supervisor_medewerker_updatenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    PreparedStatement stmt_get = null;
    ResultSet rs_get = null;

    final ObservableList<Employee> data = FXCollections.observableArrayList();

    @FXML
    private TableView<Employee> tbl_Employee;

    @FXML
    private TableColumn tblC_FirstName;

    @FXML
    private TableColumn tblC_Surname;

    @FXML
    private TableColumn tblC_Email;

    @FXML
    private TableColumn tblC_Function;

    @FXML
    private TableColumn tblC_Country;

    @FXML
    private Text txtFirstname;

    @FXML
    private JFXTextField txtSurname;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXComboBox ddlFunction;

    @FXML
    private JFXComboBox ddlCountry;

    @FXML
    private JFXButton meOption;

    @FXML
    private AnchorPane show_me_user_info;

    @FXML
    private JFXTextField txtFirstnameNew;

    @FXML
    private JFXTextField txtSurnameNew;

    @FXML
    private JFXTextField txtEmailNew;

    @FXML
    private JFXComboBox ddlFunctionNew;

    @FXML
    private JFXComboBox ddlCountryNew;

    @FXML
    void on_Me_User(ActionEvent event) {
        if (event.getSource() == meOption) {
            show_me_user_info.setVisible(true);
        }
    }

    /**
     * Vult de table met gegevens uit de database
     */
    public void FillTable() {

        try {
            String function = "SELECT * FROM employee INNER JOIN function ON employee.Function_id = function.function_id INNER JOIN country ON employee.Country_id = country.Country_id";
            stmt = conn.prepareStatement(function);
            rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new Employee(
                        rs.getString("Firstname"),
                        rs.getString("Lastname"),
                        rs.getString("Email"),
                        rs.getString("Function_name"),
                        rs.getString("Country_name")
                ));
                tbl_Employee.setItems(data);
                tblC_FirstName.setCellValueFactory(new PropertyValueFactory<>("Firstname"));
                tblC_Surname.setCellValueFactory(new PropertyValueFactory<>("Lastname"));
                tblC_Email.setCellValueFactory(new PropertyValueFactory<>("Email"));
                tblC_Function.setCellValueFactory(new PropertyValueFactory<>("Function_name"));
                tblC_Country.setCellValueFactory(new PropertyValueFactory("Country_name"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_manage_brandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Refreshed de tableview wanneer aangeroepen
     */
    public void refreshTable() {
        data.clear();

        try {
            String function = "SELECT * FROM employee INNER JOIN function ON employee.Function_id = function.function_id INNER JOIN country ON employee.Country_id = country.Country_id";

            stmt = conn.prepareStatement(function);
            rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new Employee(
                        rs.getString("Firstname"),
                        rs.getString("Lastname"),
                        rs.getString("Email"),
                        rs.getString("Function_name"),
                        rs.getString("Country_name")
                ));
                tbl_Employee.setItems(data);
                tblC_FirstName.setCellValueFactory(new PropertyValueFactory<>("Firstname"));
                tblC_Surname.setCellValueFactory(new PropertyValueFactory<>("Lastname"));
                tblC_Email.setCellValueFactory(new PropertyValueFactory<>("Email"));
                tblC_Function.setCellValueFactory(new PropertyValueFactory<>("Function_name"));
                tblC_Country.setCellValueFactory(new PropertyValueFactory("Country_name"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_manage_brandController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        if (tbl_Employee.getSelectionModel().getSelectedItem() != null) {
            Employee selectedEmployee = tbl_Employee.getSelectionModel().getSelectedItem();
            txtFirstname.setText(selectedEmployee.getFirstname());
        }
    }

    @FXML
    void on_Update_Employee(ActionEvent event) {
        String query = "UPDATE employee WHERE Firstname='" + txtFirstname.getText() + "' , Lastname='" + txtSurname.getText() + "' , Email='" + txtEmail.getText() + "' SET Firstname=? Lastname=? Email=?";

        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, txtFirstnameNew.getText());
            stmt.setString(2, txtSurnameNew.getText());
            stmt.setString(3, txtEmailNew.getText());

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_medewerker_toevoegenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String get_Function_id() {
        String function = null;
        try {

            String query_get = "Select Function_id from function where Function_name ='" + ddlFunction.getSelectionModel().getSelectedItem().toString() + "'";
            stmt_get = conn.prepareStatement(query_get);
            rs_get = stmt_get.executeQuery();
            while (rs_get.next()) {
                function = rs_get.getString("Function_id");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_medewerker_toevoegenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return function;
    }

    public String get_Country_id() {
        String country = null;
        try {

            String query_get = "Select Country_id from country where Country_name ='" + ddlCountry.getSelectionModel().getSelectedItem().toString() + "'";
            stmt_get = conn.prepareStatement(query_get);
            rs_get = stmt_get.executeQuery();
            while (rs_get.next()) {
                country = rs_get.getString("Country_id");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_medewerker_toevoegenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return country;
    }

    /**
     * alle waardes van Country in de database halen die waardes koppelen met
     * ddlCountry
     */
    private void populate_comboBox_Country() {
        try {
            String query = "select * from country";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                //db_LuggageType.add(rs1.getString("LuggageType"));
                String l_list = rs.getString("Country_Name");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                ddlCountry.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    /**
     * alle waardes van Function in de database halen die waardes koppelen met
     * ddlFunction
     */
    private void populate_comboBox_Function() {
        try {
            String query = "select * from function";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                //db_LuggageType.add(rs1.getString("LuggageType"));
                String l_list = rs.getString("Function_name");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                ddlFunction.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public static String hash() {
        String generatePassword = randomPassword();
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(generatePassword.getBytes(), 0, generatePassword.length());
            String hash = new BigInteger(1, m.digest()).toString(16);

            return hash;

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Supervisor_medewerker_toevoegenController.class.getName()).log(Level.SEVERE, null, ex);

            return null;
        }

    }

    /**
     * Genereert een random wachtwoord
     *
     * @return het random wachtwoord
     */
    public static String randomPassword() {
        String passwordCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()";
        StringBuilder passwordBuilder = new StringBuilder();
        Random randomizer = new Random();
        while (passwordBuilder.length() < 8) {
            int index = (int) (randomizer.nextFloat() * passwordCharacters.length());
            passwordBuilder.append(passwordCharacters.charAt(index));
        }
        String generatedPassword = passwordBuilder.toString();

        return generatedPassword;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // get values from country database table
        populate_comboBox_Country();

        // get values from function database table
        populate_comboBox_Function();

        // Fill the table with values
        FillTable();
    }

}
