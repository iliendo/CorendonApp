package corendo.fys.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import corendo.fys.jdbcDBconnection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class Gevonden_bagageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    File file;
    FileInputStream fis;

    @FXML
    private JFXTextField txtDatum;

    @FXML
    private JFXComboBox ddlLuchthaven;

    @FXML
    private JFXComboBox ddlMainColor;

    @FXML
    private JFXComboBox ddlSecondColor;

    @FXML
    private JFXTextField txtArrivedFlight;

    @FXML
    private JFXTextField txtLuggageTag;

    @FXML
    private JFXComboBox ddlLocationFound;

    @FXML
    private JFXTextField txtSize;

    @FXML
    private JFXComboBox ddlWeight;

    @FXML
    private JFXComboBox ddlLuggageType;

    @FXML
    private ImageView imgLuggage;
    private Image image;

    @FXML
    private JFXTextField txtTimeFound;

    @FXML
    private JFXComboBox ddlStatus;

    @FXML
    private JFXComboBox ddlMerk;

    @FXML
    private JFXTextField txtRegisNr;

    @FXML
    private JFXTextArea txt_area_bijzonder_merken;

    @FXML
    private JFXComboBox ddlSize;

    @FXML
    private JFXTextArea txtImagePath;

    @FXML
    void on_Opslaan(ActionEvent event) throws FileNotFoundException {

        // alle velden checken of ze ingevuld zijn.
        checkVelden();
        
        // insert de gevonden bagage
        InsertLuggageInfo();

        //clear_tekstVelden();
    }

    @FXML
    void on_Foto_bestand(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();

        //filter naar de jpg of png
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //open file dialog
        file = fileChooser.showOpenDialog(null);

        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            image = SwingFXUtils.toFXImage(bufferedImage, null);
            imgLuggage.setImage(image);
        } catch (IOException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * het insert de ingevulde waardes naar de database
     */
    private void InsertLuggageInfo() throws FileNotFoundException {
        try {

            String query_luggage = "INSERT INTO luggage (DateFound,TimeFound,"
                    + "LuggageType_id,Brand_id,MainColor_id,Status_id,Size_id,"
                    + "Weight_id,SecondColor_id,LuggageTag,Image,Location_Airport_id,"
                    + "Airport_id,Passenger_id,Flight,Features) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement stmt2 = conn.prepareStatement(query_luggage);

            int passengerId = -1;

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
            stmt2.setString(11, imgLuggage.toString());
            stmt2.setString(12, get_LocationAirport_id());
            stmt2.setString(13, get_Airport_id());
            stmt2.setInt(14, passengerId);
            stmt2.setString(15, txtArrivedFlight.getText());
            stmt2.setString(16, txt_area_bijzonder_merken.getText());

            stmt2.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Luggage has been added!");
            alert.showAndWait();

        } catch (SQLException ex) {
            Logger.getLogger(Gevonden_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    private static boolean nullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public void checkVelden() {
        String luggageType = ddlLuggageType.getSelectionModel().getSelectedItem().toString();
        String brand = ddlMerk.getSelectionModel().getSelectedItem().toString();
        String mainColor = ddlMainColor.getSelectionModel().getSelectedItem().toString();
        String status = ddlStatus.getSelectionModel().getSelectedItem().toString();
        String size = ddlSize.getSelectionModel().getSelectedItem().toString();
        String weight = ddlWeight.getSelectionModel().getSelectedItem().toString();
        String secondColor = ddlSecondColor.getSelectionModel().getSelectedItem().toString();
        String location = ddlLocationFound.getSelectionModel().getSelectedItem().toString();
        
        
        if (nullOrEmpty(luggageType) || nullOrEmpty(brand)
                || nullOrEmpty(mainColor) || nullOrEmpty(status)
                || nullOrEmpty(size) || nullOrEmpty(weight)
                || nullOrEmpty(secondColor) || nullOrEmpty(location)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Fill all text fields !");
            alert.showAndWait();
        }
    }
    
    
    
    
    

    /**
     * ****************************************************************************
     ***********************COMBOBOX VULLEN MET WAARDES
     * ****************************
 * **************************************************************************
     */
    private void clear_tekstVelden() {
        txtArrivedFlight.setText("");
        txtLuggageTag.setText("");
        txtSize.setText("");

        ddlLuggageType.getSelectionModel().clearSelection();
        ddlLuggageType.setValue(null);

        ddlMainColor.getSelectionModel().clearSelection();
        ddlMainColor.setValue(null);
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
            String sql = "select * from status where Status='Found' ";
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
     * om de lijst van de Locatie op vliegveld op te halen uit de database
     */
    private void populate_comboBox_LocationInAirport() {
        try {
            String sql = "select * from location_airport";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String l_list = rs.getString("Location_name");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                ddlLocationFound.getItems().addAll(data_lijst);
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
        populate_comboBox_LocationInAirport();
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
  **************************************************************************
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
            Logger.getLogger(Gevonden_bagageController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Gevonden_bagageController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Gevonden_bagageController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Gevonden_bagageController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Gevonden_bagageController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Gevonden_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public String get_Flight_id() {
        String id = null;
        try {

            String query = "Select Flight_id from flight where Flight_number ='" + ddlLuchthaven.getSelectionModel().getSelectedItem().toString() + "'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getString("Flight_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Gevonden_bagageController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Gevonden_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public String get_LocationAirport_id() {
        String id = null;
        try {

            String query = "Select Location_Airport_id from location_airport where Location_name ='" + ddlLocationFound.getSelectionModel().getSelectedItem().toString() + "'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getString("Location_Airport_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Gevonden_bagageController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Gevonden_bagageController.class.getName()).log(Level.SEVERE, null, ex);
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
  **************************************************************************
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        populate_tekstvelden_Date_and_Time();
        populate_All_ComboBoxes();

    }

}
