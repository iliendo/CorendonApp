package corendo.fys.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import corendo.fys.Password;
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
import javafx.scene.layout.AnchorPane;
import supervisor.Employee;

/**
 * FXML Controller class Hierin kan je medewerkers toevoegen
 *
 * @author Ilias Azagagh
 */
public class Supervisor_medewerker_toevoegenController implements Initializable {

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
    private JFXTextField txtFirstname;

    @FXML
    private JFXTextField txtSurname;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXComboBox ddlFunction;

    @FXML
    private JFXComboBox ddlCountry;

    @FXML
    private JFXButton meOption;

    @FXML
    private AnchorPane show_me_user_info;

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
            txtSurname.setText(selectedEmployee.getFirstname());
            txtEmail.setText(selectedEmployee.getEmail());
            ddlFunction.setValue(selectedEmployee.getFunction_name());
            ddlCountry.setValue(selectedEmployee.getCountry_name());
        }
    }

    /**
     * Maakt een nieuwe medewerker aan in de database
     *
     * @param event
     */
    @FXML
    void on_Create_Employee(ActionEvent event) {
        Password password = new Password();
        if (checkInput()) {
            if (checkEmail()) {
                try {
                    String query = "INSERT INTO employee (Firstname,Lastname,Email,Password,"
                            + "Function_id,Country_id) VALUES(?,?,?,?,?,?)";

                    String randomPassword = password.getRandomPassword();

                    stmt = conn.prepareStatement(query);
                    stmt.setString(1, txtFirstname.getText());
                    stmt.setString(2, txtSurname.getText());
                    stmt.setString(3, txtEmail.getText());
                    stmt.setString(4, password.getHashedPassword(randomPassword));
                    stmt.setString(5, get_Function_id());
                    stmt.setString(6, get_Country_id());

                    stmt.execute();
                    stmt.close();

                    Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                    confirmation.setTitle("Information");
                    confirmation.setContentText("The Password is: " + randomPassword + "\nWrite down Your Password!!!");
                    confirmation.showAndWait();

                    refreshTable();
                } catch (SQLException ex) {
                    Logger.getLogger(Supervisor_medewerker_toevoegenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    /**
     * Verwijderd een medewerker mits alle benodigde velden zijn ingevuld
     */
    @FXML
    void on_Delete_Employee(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setContentText("Are you sure you want to delete: " + txtEmail.getText() + " ? \nThis action CANNOT be undone!");

        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            try {
                String query = "DELETE FROM employee WHERE Firstname=? AND Lastname=? AND Email=?";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, txtFirstname.getText());
                stmt.setString(2, txtSurname.getText());
                stmt.setString(3, txtEmail.getText());

                stmt.executeUpdate();
                stmt.close();

                Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                confirmation.setTitle("Information");
                confirmation.setContentText(txtEmail.getText() + " is deleted!");

                confirmation.showAndWait();

            } catch (SQLException ex) {
                Logger.getLogger(Supervisor_medewerker_toevoegenController.class.getName()).log(Level.SEVERE, null, ex);
            }

            refreshTable();

        } else if (action.get() == ButtonType.CANCEL) {
            Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
            cancelAlert.setTitle("Information");
            cancelAlert.setContentText(txtEmail.getText() + " is not deleted!");

            action = cancelAlert.showAndWait();
        }

    }

    /*@FXML
    void on_Update_Employee(ActionEvent event) {
        String query = "UPDATE employee WHERE Firstname='" + txtFirstname.getText() + "' SET Firstname=? ";

        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, txtFirstname.getText());

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_medewerker_toevoegenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
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

    private boolean checkInput() {
        boolean state = true;
        
        if (txtFirstname.getText().isEmpty()) {
            missingInfoAlert("Firstname");
            state = false;
        } else if (txtSurname.getText().isEmpty()) {
            missingInfoAlert("Surname");
            state = false;
        } else if (txtEmail.getText().isEmpty()) {
            missingInfoAlert("Email");
            state = false;
        } else if (ddlFunction.getValue()== null) {
            missingInfoAlert("Function");
            state = false;
        } else if (ddlCountry.getValue()== null) {
            missingInfoAlert("Country");
            state = false;
        }

        return state;
    }

    /**
     * Controleert of het email adres al bestaat in de database
     *
     * @return true als het email adres bestaat anders false
     */
    private boolean checkEmail() {
        boolean state = false;
        try {
            String email;

            String controlQuery = "SELECT email FROM employee WHERE email = '" + txtEmail.getText() + "'";
            stmt = conn.prepareStatement(controlQuery);
            rs = stmt.executeQuery();

            if (rs.next()) {
                email = rs.getString("email");
                if (!email.contains(txtEmail.getText() + "@corendon")) {

                    Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                    confirmation.setTitle("Information");
                    confirmation.setContentText(email + " is not a valid @corendon emailadres!");

                    confirmation.showAndWait();
                } else if (email.equals(txtEmail.getText())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Email already exists!");
                    alert.showAndWait();
                }
            } else {
                state = true;

            }
        } catch (SQLException ex) {
            Logger.getLogger(Supervisor_medewerker_toevoegenController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return state;
    }

    private void missingInfoAlert(String asset) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(asset + " needs to be filled!");
        alert.showAndWait();
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
