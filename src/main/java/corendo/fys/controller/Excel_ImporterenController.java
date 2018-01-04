package corendo.fys.controller;

import com.jfoenix.controls.JFXListView;
import corendo.fys.CellConverter;
import corendo.fys.jdbcDBconnection;
import corendo.fys.IdChecker;
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

    private String filePath;

    private int tempSheetCounter = 100;

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

        CellConverter cellConverter = new CellConverter();

        IdChecker idChecker = new IdChecker();

        int k = 0;

        for (int j = 0; j < tempSheetCounter; j++) {

            // Tallies what the current sheet is that is imported
            XSSFSheet currentSheet = workbook.getSheetAt(j);

            // Checks what the last filled row is in the workbook
            int totalRowCount = currentSheet.getLastRowNum();

            // Checks what the last sheet in the workbook is
            int totalSheetCount = workbook.getNumberOfSheets();

            tempSheetCounter = totalSheetCount;

            System.out.println(totalRowCount);

            // Starts at row 5 and prints every first value from each row from the first column
            for (int i = 0; i < totalRowCount; i++) {
                String luggageId = cellConverter.getCellString(currentSheet, 0, i);
                String dateFound = cellConverter.getCellString(currentSheet, 1, i);
                String timeFound = cellConverter.getCellString(currentSheet, 2, i);
                String luggageType = cellConverter.getCellString(currentSheet, 3, i);
                String brand = cellConverter.getCellString(currentSheet, 4, i);
                String arrivedFlight = cellConverter.getCellString(currentSheet, 5, i);
                String luggageTag = cellConverter.getCellString(currentSheet, 6, i);
                String locationFound = cellConverter.getCellString(currentSheet, 7, i);
                String mainColor = cellConverter.getCellString(currentSheet, 8, i);
                String secondColor = cellConverter.getCellString(currentSheet, 9, i);
                String size = cellConverter.getCellString(currentSheet, 10, i);
                String weight = cellConverter.getCellString(currentSheet, 11, i);
                String passengerName = cellConverter.getPassengerCellString(currentSheet, 12, i);
                String city = cellConverter.getCityCellString(currentSheet, 12, i);
                String otherCharacteristics = cellConverter.getCellString(currentSheet, 13, i);

                try {
                    String insertQuery = "INSERT INTO luggage_import (luggage_id, DateFound, TimeFound, LuggageType, Brand, ArrivedFlight, LuggageTag, LocationFound, MainColor, SecondColor, Size, Weight, PassengerName, City, OtherCharacteristics) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    String selectQuery = "SELECT luggage_id FROM luggage_import WHERE luggage_id='" + luggageId + "'";

                    stmt = conn.prepareStatement(selectQuery);
                    rs = stmt.executeQuery();
                    if (luggageId.isEmpty()) {
                        System.out.println("Value is empty!");
                    } else if (!idChecker.isCharacter(luggageId)) {
                        System.out.println("Not an ID");
                    } else if (rs.next()) {
                        System.out.println(k++ + " Entry already exists!!!");
                    } else if (luggageId.isEmpty()) {
                        System.out.println("Value is empty!");
                    } else {
                        stmt = conn.prepareStatement(insertQuery);
                        stmt.setString(1, luggageId);
                        stmt.setString(2, dateFound);
                        stmt.setString(3, timeFound);
                        stmt.setString(4, luggageType);
                        stmt.setString(5, brand);
                        stmt.setString(6, arrivedFlight);
                        stmt.setString(7, luggageTag);
                        stmt.setString(8, locationFound);
                        stmt.setString(9, mainColor);
                        stmt.setString(10, secondColor);
                        stmt.setString(11, size);
                        stmt.setString(12, weight);
                        stmt.setString(13, passengerName);
                        stmt.setString(14, city);
                        stmt.setString(15, otherCharacteristics);

                        stmt.execute();
                        stmt.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Excel_ImporterenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        // Clears the file picker
        listView.getItems().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
