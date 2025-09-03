package org.example.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {

    public static List<String[]> readExcel(String filePath, String sheetName) {
        List<String[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rows = sheet.iterator();

            // Bỏ qua header
            if (rows.hasNext()) rows.next();

            while (rows.hasNext()) {
                Row row = rows.next();
                String[] rowData = new String[row.getLastCellNum()];
                boolean isEmpty = true;

                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData[i] = getCellValue(cell);
                    if (!rowData[i].isEmpty()) {
                        isEmpty = false;
                    }
                }

                // chỉ add khi có ít nhất 1 ô không rỗng
                if (!isEmpty) {
                    data.add(rowData);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Format ngày theo yyyy-MM-dd
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(cell.getDateCellValue());
                } else {
                    double value = cell.getNumericCellValue();
                    if (value == (long) value) {
                        return String.valueOf((long) value);
                    } else {
                        return String.valueOf(value);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    try {
                        double value = cell.getNumericCellValue();
                        return String.valueOf(value);
                    } catch (Exception ex) {
                        return cell.getCellFormula();
                    }
                }
            case BLANK:
            default:
                return "";
        }
    }

    public static void writeTestResults(String filePath, String sheetName, int rowIndex, String actualResult, String status) {
        try (FileInputStream fis = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rowIndex);

            // Create or update cell for ActualResult (column 7, index 6)
            Cell actualResultCell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            actualResultCell.setCellValue(actualResult);

            // Create or update cell for Status (column 8, index 7)
            Cell statusCell = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            statusCell.setCellValue(status);

            // Write changes back to the file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

