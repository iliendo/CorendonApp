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

    XSSFSheet currentSheet;

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
        // Filepath of the selected file
        FileInputStream file = new FileInputStream(new File(filePath));

        // Specified which file to open
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int sheetCount = workbook.getNumberOfSheets();

        int k = 0;

        for (int j = 0; j < sheetCount; j++) {

            currentSheet = workbook.getSheetAt(j);
            // Checks what the last filled row is in the file
            int rowCount = currentSheet.getLastRowNum();
            System.out.println(rowCount);

            // Starts at row 5 and prints every first value from each row from the first column
            for (int i = 4; i < rowCount; i++) {
                String luggageId = currentSheet.getRow(i).getCell(0).toString();
                String dateFound = currentSheet.getRow(i).getCell(1).toString();
//                String timeFound = currentSheet.getRow(i).getCell(2).toString();
//                String luggageType = currentSheet.getRow(i).getCell(3).toString();
//                String brand = currentSheet.getRow(i).getCell(4).toString();
//                String arrivedFlight = currentSheet.getRow(i).getCell(5).toString();
//                String luggageTag = currentSheet.getRow(i).getCell(6).toString();
//                String locationFound = currentSheet.getRow(i).getCell(7).toString();
//                String mainColor = currentSheet.getRow(i).getCell(8).toString();
//                String secondColor = currentSheet.getRow(i).getCell(9).toString();
//                String size = currentSheet.getRow(i).getCell(10).toString();
//                String weight = currentSheet.getRow(i).getCell(11).toString();
//                String passengerName = currentSheet.getRow(i).getCell(12).toString().substring(0, currentSheet.getRow(i).getCell(12).toString().indexOf(","));
//                String city = currentSheet.getRow(i).getCell(12).toString().substring(currentSheet.getRow(i).getCell(12).toString().indexOf(",") + 1, currentSheet.getRow(i).getCell(12).toString().length()).trim();
//                String otherCharacteristics = currentSheet.getRow(i).getCell(13).toString();

                try {
                    String insertQuery = "INSERT INTO luggage_import (luggage_id, DateFound) VALUES(?,?)";
                    String selectQuery = "SELECT luggage_id FROM luggage_import WHERE luggage_id='" + luggageId + "'";

                    stmt = conn.prepareStatement(selectQuery);
                    rs = stmt.executeQuery();

                    if (rs.next()) {
                        System.out.println(k++ + " Entry already exists!!!");
                    } else if(luggageId.isEmpty()){
                        System.out.println("Value is empty!");
                    } else {
                        stmt = conn.prepareStatement(insertQuery);
                        stmt.setString(1, luggageId);
                        stmt.setString(2, dateFound);
//                        stmt.setString(3, currentSheet.getRow(i).getCell(2).toString());
//                        stmt.setString(4, currentSheet.getRow(i).getCell(3).toString());
//                        stmt.setString(5, currentSheet.getRow(i).getCell(4).toString());
//                        stmt.setString(6, currentSheet.getRow(i).getCell(5).toString());
//                        stmt.setString(7, currentSheet.getRow(i).getCell(6).toString());
//                        stmt.setString(8, currentSheet.getRow(i).getCell(7).toString());
//                        stmt.setString(9, currentSheet.getRow(i).getCell(8).toString());
//                        stmt.setString(10, currentSheet.getRow(i).getCell(9).toString());
//                        stmt.setString(11, currentSheet.getRow(i).getCell(10).toString());
//                        stmt.setString(12, currentSheet.getRow(i).getCell(11).toString());
//                        stmt.setString(13, currentSheet.getRow(i).getCell(12).toString().substring(0, currentSheet.getRow(i).getCell(12).toString().indexOf(",")));
//                        stmt.setString(14, currentSheet.getRow(i).getCell(12).toString().substring(currentSheet.getRow(i).getCell(12).toString().indexOf(",") + 1, currentSheet.getRow(i).getCell(12).toString().length()).trim());
//                        stmt.setString(15, currentSheet.getRow(i).getCell(13).toString());

                        stmt.execute();
                        stmt.close();
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Excel_ImporterenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        listView.getItems().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
