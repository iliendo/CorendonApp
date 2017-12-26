package corendo.fys.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.Statement;
import corendo.fys.jdbcDBconnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class Verloren_bagageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    @FXML
    private JFXTextField txtDatum;

    @FXML
    private JFXComboBox ddlLuchthaven;

    @FXML
    private JFXTextField txtFirstname;

    @FXML
    private JFXTextField txtLastname;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCity;

    @FXML
    private JFXTextField txtZipCode;

    @FXML
    private JFXTextField txtPhoneNr;

    @FXML
    private JFXComboBox ddlMainColor;

    @FXML
    private JFXComboBox ddlSecondColor;

    @FXML
    private JFXTextField txtArrivedFlight;

    @FXML
    private JFXTextField txtLuggageTag;

    @FXML
    private JFXTextField txtCountry;

    @FXML
    private JFXComboBox ddlWeight;

    @FXML
    private JFXComboBox ddlLuggageType;

    @FXML
    private JFXTextField txtTimeFound;

    @FXML
    private JFXComboBox ddlStatus;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXComboBox ddlMerk;

    @FXML
    private JFXTextArea txt_area_bijzonder_merken;

    @FXML
    private JFXComboBox ddlSize;

    @FXML
    void on_Opslaan(ActionEvent event) {
        // alle velden checken of ze ingevuld zijn.
        checkVelden();

        // insert de verloren bagage maar eerst check of de ingevulde waardes al bestaan in de database
        // InsertLuggageInfo();
        machting_and_insert();

        // maak alle velden leeg
        clear_tekstVelden();
    }

    @FXML
    void on_Me_User(ActionEvent event) throws IOException {
        Parent parent1 = FXMLLoader.load(getClass().getResource("/corendo/fys/Login_Medewerker.fxml"));
        Scene scene = new Scene(parent1);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }

    /**
     * het insert de ingevulde waardes naar de database
     */
    public void machting_and_insert() {
        int m_LuggageType = 0;
        int m_Brand = 0;
        String m_MainColor = null;
        String m_Size = null;
        String m_Weight = null;
        String m_SecondColor = null;
        String m_LuggageTag = null;
        String m_ArrivedFlight = null;

        try {
            String query = "SELECT * FROM luggage";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                m_LuggageType = rs.getInt("LuggageType_id");
                m_Brand = rs.getInt("Brand_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (get_LuggageType_id().equals(m_LuggageType)
                && get_Brand_id().equals(m_Brand)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("The data you have entered shows that your baggage is already registered in our database.");
            alert.showAndWait();
        } else {
            InsertLuggageInfo();
        }

    }

    /*
    public void machting_and_insert() {
        try {
            String m_Firstname = null;
            String query = "SELECT * FROM luggage INNER JOIN passenger ON luggage.Passenger_id = passenger.Passenger_id";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                m_Firstname = rs.getString("Firstname");

                if (m_Firstname.equals(txtFirstname.getText())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("The data you have entered shows that your baggage is already registered in our database.");
                    alert.showAndWait();

                    break;
                } else {
                    InsertLuggageInfo();
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     */
 /*
    public void machting_and_insert() {
        try {
            String m_Firstname = null;
            String m_Lastname = null;
            String m_Country = null;
            String m_Email = null;
            String m_TypeBagage = null;
            String m_Color = null;
            String m_Merk = null;

            String query = "SELECT * FROM luggage "
                    + "INNER JOIN passenger ON luggage.Passenger_id = passenger.Passenger_id "
                    + "INNER JOIN luggagetype ON luggage.LuggageType_id = luggagetype.LuggageType_id "
                    + "INNER JOIN maincolor ON luggage.MainColor_id = maincolor.MainColor_id "
                    + "INNER JOIN brand ON luggage.Brand_id = brand.Brand_id";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                m_Firstname = rs.getString("Firstname");
                m_Lastname = rs.getString("Lastname");
                m_Country = rs.getString("Country_name");
                m_Email = rs.getString("Email");

                m_TypeBagage = rs.getString("LuggageType");
                m_Color = rs.getString("Color");
                m_Merk = rs.getString("Brand");

                if (m_Firstname.equals(txtFirstname.getText())
                       /* && m_Lastname.equals(txtLastname.getText())
                        && m_Country.equals(txtCountry.getText())
                        && m_Email.equals(txtEmail.getText())
                        && m_TypeBagage.equals(ddlLuggageType.getSelectionModel().getSelectedItem().toString())
                        && m_Color.equals(ddlMainColor.getSelectionModel().getSelectedItem().toString())
                        && m_Merk.equals(ddlMerk.getSelectionModel().getSelectedItem().toString()) )  {
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("The data you have entered shows that your baggage is already registered in our database.");
                    alert.showAndWait();
                    break;
                } else {
                    InsertLuggageInfo();
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     */
    private void InsertLuggageInfo() {
        try {
            String query_passenger = "INSERT INTO passenger (Firstname,Lastname,Email,"
                    + "PhoneNr,Address,Zipcode,City,Country_name) "
                    + "VALUES (?,?,?,?,?,?,?,?)";

            String query_luggage = "INSERT INTO luggage (DateFound,TimeFound,"
                    + "LuggageType_id,Brand_id,MainColor_id,Status_id,Size_id,"
                    + "Weight_id,SecondColor_id,LuggageTag,Image,Location_Airport_id,"
                    + "Airport_id,Passenger_id,Flight,Features) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            stmt = conn.prepareStatement(query_passenger, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement stmt2 = conn.prepareStatement(query_luggage);

            stmt.setString(1, txtFirstname.getText());
            stmt.setString(2, txtLastname.getText());
            stmt.setString(3, txtEmail.getText());
            stmt.setString(4, txtPhoneNr.getText());
            stmt.setString(5, txtAddress.getText());
            stmt.setString(6, txtZipCode.getText());
            stmt.setString(7, txtCity.getText());
            stmt.setString(8, txtCountry.getText());

            long passengerId = 0;
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert Passenger error: no rows were affected.");
            }
            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    passengerId = rs.getLong(1);
                    System.out.println(passengerId);
                }
            }
            stmt2.setString(1, txtDatum.getText());
            stmt2.setString(2, txtTimeFound.getText());
            stmt2.setString(3, get_LuggageType_id());
            stmt2.setString(4, get_Brand_id());
            stmt2.setString(5, get_MainColor_id());
            stmt2.setString(6, get_Status_id());
            stmt2.setString(7, get_Size_id());
            stmt2.setString(8, get_Weight_id());
            stmt2.setString(9, get_SecondColor_id());
            stmt2.setString(10, txtLuggageTag.getText());
            stmt2.setString(11, "no image");
            stmt2.setString(12, "1");
            stmt2.setString(13, get_Airport_id());
            stmt2.setLong(14, passengerId);
            stmt2.setString(15, txtArrivedFlight.getText());
            stmt2.setString(16, txt_area_bijzonder_merken.getText());

            stmt2.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Luggage has been added!");
            alert.showAndWait();

        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * ****************************************************************************
     ***********************COMBOBOX VULLEN MET WAARDES
     * ****************************
     * **************************************************************************
     */
    private void clear_tekstVelden() {
        txtArrivedFlight.setText(null);
        txtFirstname.setText(null);
        txtLastname.setText(null);
        txtAddress.setText(null);
        txtEmail.setText(null);
        txtCity.setText(null);
        txtZipCode.setText(null);
        txtPhoneNr.setText(null);
        txtLuggageTag.setText(null);
        txtCountry.setText(null);
        txt_area_bijzonder_merken.setText(null);

        ddlLuggageType.getSelectionModel().clearSelection();
        ddlLuggageType.setValue(null);

        ddlMainColor.getSelectionModel().clearSelection();
        ddlMainColor.setValue(null);

        ddlLuchthaven.getSelectionModel().clearSelection();
        ddlLuchthaven.setValue(null);

        ddlMerk.getSelectionModel().clearSelection();
        ddlMerk.setValue(null);

        ddlSecondColor.getSelectionModel().clearSelection();
        ddlSecondColor.setValue(null);

        ddlSize.getSelectionModel().clearSelection();
        ddlSize.setValue(null);

        ddlWeight.getSelectionModel().clearSelection();
        ddlWeight.setValue(null);

    }

    private static boolean nullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public void checkVelden() {
        String flight = txtArrivedFlight.getText();
        String firstname = txtFirstname.getText();
        String lastname = txtLastname.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String city = txtCity.getText();
        String zipcode = txtZipCode.getText();
        String phoneNr = txtPhoneNr.getText();
        String luggageTag = txtLuggageTag.getText();
        String country = txtCountry.getText();

        if (nullOrEmpty(flight) || nullOrEmpty(firstname)
                || nullOrEmpty(lastname) || nullOrEmpty(address)
                || nullOrEmpty(email) || nullOrEmpty(city)
                || nullOrEmpty(zipcode) || nullOrEmpty(phoneNr)
                || nullOrEmpty(luggageTag) || nullOrEmpty(country)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Fill all text fields !");
            alert.showAndWait();
        }
    }

    /**
     * om de lijst van de type bagage op te halen uit de database
     */
    private void populate_comboBox_LuggageType() {
        try {
            String sql = "select * from luggagetype";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String list_db = rs.getString("LuggageType");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(list_db);
                ddlLuggageType.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }

    }

    /**
     * om de lijst van de Brand op te halen uit de database
     */
    private void populate_comboBox_Brand() {
        try {
            String sql = "select * from brand";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String l_list = rs.getString("Brand");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                ddlMerk.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    /**
     * om de lijst van de Maincolor op te halen uit de database
     */
    private void populate_comboBox_MainColor() {
        try {
            String sql = "select * from maincolor";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String l_list = rs.getString("Color");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                ddlMainColor.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    /**
     * om de lijst van de Status op te halen uit de database
     */
    private void populate_comboBox_Status() {
        try {
            String sql = "select * from status where Status='Lost'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String l_list = rs.getString("Status");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                ddlStatus.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    /**
     * om de lijst van de Size op te halen uit de database
     */
    private void populate_comboBox_Size() {
        try {
            String sql = "select * from size";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String l_list = rs.getString("Size");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                ddlSize.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    /**
     * om de lijst van de Weight op te halen uit de database
     */
    private void populate_comboBox_Weight() {
        try {
            String sql = "select * from weight";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String l_list = rs.getString("Weight");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                ddlWeight.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    /**
     * om de lijst van de SecondColor op te halen uit de database
     */
    private void populate_comboBox_SecondColor() {
        try {
            String sql = "select * from secondcolor";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String l_list = rs.getString("Color");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                ddlSecondColor.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    /**
     * om de lijst van de Airport op te halen uit de database
     */
    private void populate_comboBox_Airport() {
        try {
            String sql = "select * from airport";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String l_list = rs.getString("Airport_name");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                ddlLuchthaven.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    /**
     * the current datum en tijd ophalen
     */
    private void populate_tekstvelden_Date_and_Time() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        txtDatum.setText(dtf.format(localDate));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        txtTimeFound.setText(sdf.format(cal.getTime()));
    }

    private void populate_All_ComboBoxes() {
        populate_comboBox_LuggageType();
        populate_comboBox_Brand();
        populate_comboBox_MainColor();
        populate_comboBox_Status();
        populate_comboBox_Size();
        populate_comboBox_Weight();
        populate_comboBox_SecondColor();
        populate_comboBox_Airport();

    }

    /**
     * ****************************************************************************
     ***********************EINDE COMBOBOX VULLEN MET WAARDES
     * **********************
     * **************************************************************************
     */
    /**
     * *************************************************************************
     *******************************GET ID's
     * ***********************************
     * *************************************************************************
     */
    /**
     *
     * @return
     */
    public String get_LuggageType_id() {
        String id = null;
        try {

            String query = "Select LuggageType_id from luggagetype where LuggageType ='" + ddlLuggageType.getSelectionModel().getSelectedItem().toString() + "'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getString("LuggageType_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public String get_Brand_id() {
        String id = null;
        try {

            String query = "Select Brand_id from brand where Brand ='" + ddlMerk.getSelectionModel().getSelectedItem().toString() + "'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getString("Brand_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public String get_MainColor_id() {
        String id = null;
        try {

            String query = "Select MainColor_id from maincolor where Color ='" + ddlMainColor.getSelectionModel().getSelectedItem().toString() + "'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getString("MainColor_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public String get_Status_id() {
        String id = null;
        try {

            String query = "Select Status_id from status where Status ='" + ddlStatus.getSelectionModel().getSelectedItem().toString() + "'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getString("Status_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public String get_Size_id() {
        String id = null;
        try {

            String query = "Select Size_id from size where Size ='" + ddlSize.getSelectionModel().getSelectedItem().toString() + "'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getString("Size_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public String get_Weight_id() {
        String id = null;
        try {

            String query = "Select Weight_id from weight where Weight ='" + ddlWeight.getSelectionModel().getSelectedItem().toString() + "'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getString("Weight_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public String get_Flight_id() {
        String id = null;
        try {

            String query = "Select Flight_id from flight where Flight_number ='" + ddlWeight.getSelectionModel().getSelectedItem().toString() + "'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getString("Flight_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public String get_SecondColor_id() {
        String id = null;
        try {

            String query = "Select SecondColor_id from secondcolor where Color ='" + ddlSecondColor.getSelectionModel().getSelectedItem().toString() + "'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getString("SecondColor_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public String get_Airport_id() {
        String id = null;
        try {

            String query = "Select Airport_id from airport where Airport_name ='" + ddlLuchthaven.getSelectionModel().getSelectedItem().toString() + "'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getString("Airport_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

//    
//    public String get_Passenger_id() {
//        String id = null;
//        try {
//
//            String query = "Select Passenger_id from passenger where Firstname ='" + txtFirstname.getText() + "'";
//            stmt = conn.prepareStatement(query);
//            rs = stmt.executeQuery();
//            while (rs.next()) {
//                id = rs.getString("Passenger_id");
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return id;
//    }
    /**
     * *************************************************************************
     *******************************EINDE GET ID's
     * ***********************************
     * *************************************************************************
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        populate_tekstvelden_Date_and_Time();
        populate_All_ComboBoxes();

    }

}
