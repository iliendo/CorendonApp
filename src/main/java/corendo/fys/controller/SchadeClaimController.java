package corendo.fys.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.Statement;
import corendo.fys.jdbcDBconnection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class SchadeClaimController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    
    @FXML
    private JFXTextField txtDatum;

    @FXML
    private JFXTextField txtTijd;

    @FXML
    private JFXComboBox ddlLuchthaven;

    @FXML
    private JFXComboBox ddlLocationFound;

    @FXML
    private JFXTextField txtGeaariveerdeVlucht;

    @FXML
    private JFXComboBox ddlTypeBagage;

    @FXML
    private JFXComboBox ddlPrimaireKleur;

    @FXML
    private JFXComboBox ddlSize;

    @FXML
    private JFXComboBox ddlGewicht;

    @FXML
    private JFXComboBox ddlMerk;

    @FXML
    private JFXComboBox ddlSecondaireKleur;

    @FXML
    private JFXTextField txtBagageTag;

    @FXML
    private ImageView imgLuggage;

    @FXML
    private Label lblImagePath;
    
        @FXML
    private JFXTextField txtFirstname;

    @FXML
    private JFXTextField txtSurname;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtPhoneNr;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtZipcode;

    @FXML
    private JFXTextField txtCity;

    @FXML
    private JFXTextField txtCountry;
    
    File file;
    Image image;
    FileInputStream fis;

    @FXML
    void on_Opslaan(ActionEvent event) throws FileNotFoundException {
        //check of alle velden zijn ingevuld
        checkVelden();
        
        // methode om de schadeclaim te inserten
        InsertDamagedLuggage();
        
        // maak alle velden leeg als een schadeclaim is toegevoegd
        clear_tekstVelden();
    }

    @FXML
    void on_Reset(ActionEvent event) {
        clear_tekstVelden();
    }

    @FXML
    void on_UploadPhoto(ActionEvent event) {
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
            lblImagePath.setText(file.getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void InsertDamagedLuggage() throws FileNotFoundException {
        
        fis = new FileInputStream(file);
        
        
        try {
            String query_passenger = "INSERT INTO passenger (Firstname,Lastname,Email,"
                    + "PhoneNr,Address,Zipcode,City,Country_name) "
                    + "VALUES (?,?,?,?,?,?,?,?)";

            stmt = conn.prepareStatement(query_passenger, Statement.RETURN_GENERATED_KEYS);
            

            stmt.setString(1, txtFirstname.getText());
            stmt.setString(2, txtSurname.getText());
            stmt.setString(3, txtEmail.getText());
            stmt.setString(4, txtPhoneNr.getText());
            stmt.setString(5, txtAddress.getText());
            stmt.setString(6, txtZipcode.getText());
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
            
            
            String query_luggage = "INSERT INTO damagedluggage (Date,Time,"
                    + "LuggageTag,Image,Flight,Passenger_id,LuggageType_id,"
                    + "Airport_id,MainColor_id,Brand_id,Size_id,Weight_id,"
                    + "SecondColor_id) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            PreparedStatement stmt2 = conn.prepareStatement(query_luggage);
            
            stmt2.setString(1, txtDatum.getText());
            stmt2.setString(2, txtTijd.getText());
            stmt2.setString(3, txtBagageTag.getText());
            stmt2.setBinaryStream(4, (InputStream)fis, (int)file.length());
            stmt2.setString(5, txtGeaariveerdeVlucht.getText());
            stmt2.setLong(6, passengerId);
            stmt2.setString(7, get_TypeBagage());
            stmt2.setString(8, get_Airport_id());
            stmt2.setString(9, get_PrimaireKleur_id());
            stmt2.setString(10, get_Brand_id());
            stmt2.setString(11, get_Size_id());
            stmt2.setString(12, get_Gewicht_id());
            stmt2.setString(13, get_SecondaryKleur_id());

            stmt2.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Damaged has been added!");
            alert.showAndWait();

        } catch (SQLException ex) {
            Logger.getLogger(Verloren_bagageController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private static boolean nullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public boolean checkVelden() {
        String flight = txtGeaariveerdeVlucht.getText();
        String firstname = txtFirstname.getText();
        String lastname = txtSurname.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String city = txtCity.getText();
        String zipcode = txtZipcode.getText();
        String phoneNr = txtPhoneNr.getText();
        String luggageTag = txtBagageTag.getText();
        String country = txtCountry.getText();

        if (nullOrEmpty(flight) || nullOrEmpty(firstname)
                || nullOrEmpty(lastname) || nullOrEmpty(address)
                || nullOrEmpty(email) || nullOrEmpty(city)
                || nullOrEmpty(zipcode) || nullOrEmpty(phoneNr)
                || nullOrEmpty(luggageTag) || nullOrEmpty(country)
                || ddlSecondaireKleur.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Fill all text fields !");
            alert.showAndWait();

            return false;
        }
        return true;
    }
    
    private void clear_tekstVelden() {
        txtGeaariveerdeVlucht.setText(null);
        txtFirstname.setText(null);
        txtSurname.setText(null);
        txtAddress.setText(null);
        txtEmail.setText(null);
        txtCity.setText(null);
        txtZipcode.setText(null);
        txtPhoneNr.setText(null);
        txtBagageTag.setText(null);
        txtCountry.setText(null);

        ddlTypeBagage.getSelectionModel().clearSelection();
        ddlTypeBagage.setValue(null);

        ddlPrimaireKleur.getSelectionModel().clearSelection();
        ddlPrimaireKleur.setValue(null);

        ddlLuchthaven.getSelectionModel().clearSelection();
        ddlLuchthaven.setValue(null);

        ddlMerk.getSelectionModel().clearSelection();
        ddlMerk.setValue(null);

        ddlSecondaireKleur.getSelectionModel().clearSelection();
        ddlSecondaireKleur.setValue(null);

        ddlSize.getSelectionModel().clearSelection();
        ddlSize.setValue(null);

        ddlGewicht.getSelectionModel().clearSelection();
        ddlGewicht.setValue(null);

    }
    
    
    public String get_TypeBagage() {
        String id = null;
        try {

            String query = "Select LuggageType_id from luggagetype where LuggageType ='" + ddlTypeBagage.getSelectionModel().getSelectedItem().toString() + "'";
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

    public String get_PrimaireKleur_id() {
        String id = null;
        try {

            String query = "Select MainColor_id from maincolor where Color ='" + ddlPrimaireKleur.getSelectionModel().getSelectedItem().toString() + "'";
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

    public String get_Gewicht_id() {
        String id = null;
        try {

            String query = "Select Weight_id from weight where Weight ='" + ddlGewicht.getSelectionModel().getSelectedItem().toString() + "'";
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

    public String get_SecondaryKleur_id() {
        String id = null;
        try {

            String query = "Select SecondColor_id from secondcolor where Color ='" + ddlSecondaireKleur.getSelectionModel().getSelectedItem().toString() + "'";
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
    
    private void populate_comboBox_LocatieFound() {
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
    
    private void populate_comboBox_TypeBagage() {
        try {
            String sql = "select * from luggagetype";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String l_list = rs.getString("LuggageType");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                
                ddlTypeBagage.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }
    
    private void populate_comboBox_PrimaireKleur() {
        try {
            String sql = "select * from maincolor";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String l_list = rs.getString("Color");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                
                ddlPrimaireKleur.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }
    
    private void populate_comboBox_Merk() {
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
    
    private void populate_comboBox_SecondaryKleur() {
        try {
            String sql = "select * from secondcolor";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String l_list = rs.getString("Color");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                
                ddlSecondaireKleur.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }
    
    private void populate_comboBox_Gewicht() {
        try {
            String sql = "select * from weight";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String l_list = rs.getString("Weight");
                ObservableList<String> data_lijst = FXCollections.observableArrayList(l_list);
                
                ddlGewicht.getItems().addAll(data_lijst);
            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }
    
    private void allComboBox(){
        
        // vul de comboBox airport
        populate_comboBox_Airport();
        
        // vul de comboBox Location found
        populate_comboBox_LocatieFound();
        
        // vul de comboBox Type Bagage
        populate_comboBox_TypeBagage();
        
        // vul de comboBox Primary kleur
        populate_comboBox_PrimaireKleur();
        
        // vul de comboBox Merk
        populate_comboBox_Merk();
        
        // vul de comboBox Secondary kleur
        populate_comboBox_SecondaryKleur();
        
        // vul de comboBox Secondary kleur
        populate_comboBox_SecondaryKleur();
        
        // vul de comboBox size
        populate_comboBox_Size();
        
        // vul de comboBox Gewicht
        populate_comboBox_Gewicht();
    }
    
    private void populate_tekstvelden_Date_and_Time() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        txtDatum.setText(dtf.format(localDate));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        txtTijd.setText(sdf.format(cal.getTime()));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // allComboBox method word aangeroepen
        allComboBox();
        
        // methode om de datum en de tijd te vullen
        populate_tekstvelden_Date_and_Time();
        
        
    }    
    
}
