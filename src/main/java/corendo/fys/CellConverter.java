package corendo.fys;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Converteert de opgehaalde cell naar een String
 *
 * @author Ilias Azagagh
 */
public class CellConverter {

    public String getCellString(XSSFSheet currentSheet, int currentCell, int currentRow) {

        Cell cell = currentSheet.getRow(currentRow).getCell(currentCell, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
            return null;
        }
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            String numberString = NumberToTextConverter.toText(cell.getNumericCellValue());
            if (DateUtil.isCellDateFormatted(cell)) {
                numberString = cell.toString();
                // if the cell contained a time value, the apache library will not properly convert to string
                if (numberString.equals("31-Dec-1899")) {
                    // compute time in the day in seconds
                    int secsInDay = (int) ((cell.getDateCellValue().getTime() / 1000) % 86400);
                    if (secsInDay < 0) {
                        secsInDay += 86400;
                    }
                    // compute hours, minutes and format the string
                    int hours = secsInDay / 3600 + 1;
                    int minutes = (secsInDay % 3600) / 60;
                    numberString = String.format("%02d:%02d", hours, minutes);
                }
            }
            return numberString;
            /*} else if (cell.toString().contains(",")) {
            if (previousString.equals(cell.toString().indexOf(0, cell.toString().indexOf(",")))) {
                return cell.toString().substring(cell.toString().indexOf(",") + 1, cell.toString().length()).trim();
            } else {
                return cell.toString().substring(0, cell.toString().indexOf(","));
            }*/
        }
        return cell.toString();
    }

    public String getPassengerCellString(XSSFSheet currentSheet, int currentCell, int currentRow) {
        Cell cell = currentSheet.getRow(currentRow).getCell(currentCell, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
            return null;
        } else {
            return cell.toString().substring(0, cell.toString().indexOf(","));
        }
    }

    public String getCityCellString(XSSFSheet currentSheet, int currentCell, int currentRow) {
        Cell cell = currentSheet.getRow(currentRow).getCell(currentCell, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
            return null;
        } else {
            return cell.toString().substring(cell.toString().indexOf(",") + 1, cell.toString().length()).trim();
        }
    }

}
