package corendo.fys.controller;

import com.jfoenix.controls.JFXListView;
import corendo.fys.jdbcDBconnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Importeren van bestaande Excel bestanden naar de database
 *
 * @author Ilias Azagagh
 */
public class Excel_ImporterenController implements Initializable {

    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    PreparedStatement stmt_get = null;
    ResultSet rs_get = null;

    @FXML
    private JFXListView listView;

    private String fileLocation;

    private String filePath;

    @FXML
    void on_choose_file(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MS Excel Files", "*.xlsx"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            listView.getItems().clear();
            listView.getItems().add(selectedFile.getName());
            filePath = selectedFile.getAbsolutePath();
        }
    }

    @FXML
    void on_import_file(ActionEvent event) throws IOException {
        // Filepath
        File file = new File(filePath);

        FileInputStream fis = new FileInputStream(file);

        // Specified which file to open
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        // Specified which sheet to open
        XSSFSheet sheet1 = workbook.getSheetAt(0);

        // Checks what the last filled row is in the file
        int rowCount = sheet1.getLastRowNum();

        

        // Starts at row 5 and prints every first value from each row from the first column
        for (int i = 4; i < rowCount; i++) {
            try {
                String query = "insert INTO luggage_import (luggage_id) VALUES(?)";
                
                stmt = conn.prepareStatement(query);
                stmt.setString(1, sheet1.getRow(i).getCell(0).getStringCellValue().toString());
                System.out.println(sheet1.getRow(i).getCell(0).getStringCellValue().toString());
                
                stmt.execute();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Excel_ImporterenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        listView.getItems().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
