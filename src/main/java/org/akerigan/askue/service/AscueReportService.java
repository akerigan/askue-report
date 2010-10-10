package org.akerigan.askue.service;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import org.akerigan.askue.AppConstans;
import org.akerigan.askue.domain.Device;
import org.akerigan.askue.domain.Measurement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.model.InternalSheet;
import org.apache.poi.hssf.usermodel.*;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static org.akerigan.askue.AppConstans.*;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 8:29:44 (Europe/Moscow)
 */
public class AscueReportService {

    private DbService dbService;

    private static Log log = LogFactory.getLog(AscueReportService.class);

    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void importDbf(File dbfFile) throws FileNotFoundException, DBFException {
        if (dbfFile != null && dbfFile.exists()) {
            Matcher matcher = PATTERN_DBF_FILE_NAME.matcher(dbfFile.getName());
            if (!matcher.matches()) {
                throw new IllegalStateException(MESSAGE_INVALID_DBF_NAME + dbfFile.getName()
                );
            }
            DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
            Date fileDate;
            try {
                fileDate = dateFormat.parse(dbfFile.getName().substring(2, 8));
            } catch (ParseException e) {
                log.error("", e);
                throw new IllegalStateException(MESSAGE_INVALID_DBF_NAME_DATE + dbfFile.getName());
            }
            dbService.deleteMeasurements(fileDate);
            FileInputStream in = new FileInputStream(dbfFile);
            DBFReader dbfReader = new DBFReader(in);
            dbfReader.setCharactersetName(ENCODING_DBF_DEFAULT);
            int fieldsCount = dbfReader.getFieldCount();
            if (fieldsCount != 50) {
                throw new IllegalStateException(MESSAGE_INVALID_DBF_COLUMNS_COUNT);
            }
            for (int i = 0; i < fieldsCount; ++i) {
                DBFField dbfField = dbfReader.getField(i);
                if (dbfField.getDataType() != DBFField.FIELD_TYPE_N) {
                    throw new IllegalStateException(MESSAGE_INVALID_DBF_COLUMNS_TYPE);
                }
            }
            Map<Integer, Device> devicesMap = dbService.getDevicesMap();
            int recordsCount = dbfReader.getRecordCount();
            for (int i = 0; i < recordsCount; ++i) {
                Object[] record = dbfReader.nextRecord();
                int deviceId = ((Double) record[0]).intValue();
                if (!devicesMap.containsKey(deviceId)) {
                    dbService.addDevice(deviceId);
                    Device device = new Device();
                    device.setId(deviceId);
                    devicesMap.put(deviceId, device);
                }
                for (int j = 1; j < 49; ++j) {
                    dbService.addMeasurement(deviceId, fileDate, j, ((Double) record[j]).floatValue());
                }
            }
        }
    }

    public void exportToExcel(File excelFile, List<Date> selectedDates) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFCellStyle centeredStyle = wb.createCellStyle();
        centeredStyle.setBorderBottom((short) 1);
        centeredStyle.setBorderTop((short) 1);
        centeredStyle.setBorderLeft((short) 1);
        centeredStyle.setBorderRight((short) 1);
        centeredStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        centeredStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        centeredStyle.setWrapText(true);

        HSSFSheet activeSheet = wb.createSheet(SHEET_ACTIVE);
        activeSheet.setColumnWidth(0, WIDTH_COLUMN_PERIOD);
        activeSheet.setDefaultColumnWidth(WIDTH_COLUMN_DEFAULT);
        activeSheet.createFreezePane(1, 1);
        activeSheet.setMargin(InternalSheet.TopMargin, 0.7);
        activeSheet.setMargin(InternalSheet.BottomMargin, 0.5);
        activeSheet.setMargin(InternalSheet.LeftMargin, 0.5);
        activeSheet.setMargin(InternalSheet.RightMargin, 0.5);
        activeSheet.getPrintSetup().setLandscape(true);
        HSSFHeader activeHeader = activeSheet.getHeader();
        activeHeader.setRight("Страница " + HSSFHeader.page() + " из " + HSSFHeader.numPages());

        HSSFSheet reactiveSheet = wb.createSheet(SHEET_REACTIVE);
        reactiveSheet.setColumnWidth(0, WIDTH_COLUMN_PERIOD);
        reactiveSheet.setDefaultColumnWidth(WIDTH_COLUMN_DEFAULT);
        reactiveSheet.createFreezePane(1, 1);
        reactiveSheet.setMargin(InternalSheet.TopMargin, 0.7);
        reactiveSheet.setMargin(InternalSheet.BottomMargin, 0.5);
        reactiveSheet.setMargin(InternalSheet.LeftMargin, 0.5);
        reactiveSheet.setMargin(InternalSheet.RightMargin, 0.5);
        reactiveSheet.getPrintSetup().setLandscape(true);
        HSSFHeader reactiveHeader = reactiveSheet.getHeader();
        reactiveHeader.setRight("Страница " + HSSFHeader.page() + " из " + HSSFHeader.numPages());

        wb.setRepeatingRowsAndColumns(0, 0, 0, 0, 0);
        wb.setRepeatingRowsAndColumns(1, 0, 0, 0, 0);

        int activeRowIdx = 0;
        int reactiveRowIdx = 0;

        HSSFRow activeRow = activeSheet.createRow(activeRowIdx);
        activeRow.setHeight(HEIGHT_ROW_DEFAULT);
        HSSFCell activeCell = activeRow.createCell(0);
        activeCell.setCellValue(SHEET_COLUMN_PERIOD);
        activeCell.setCellStyle(centeredStyle);

        HSSFRow reactiveRow = reactiveSheet.createRow(reactiveRowIdx);
        reactiveRow.setHeight(HEIGHT_ROW_DEFAULT);
        HSSFCell reactiveCell = reactiveRow.createCell(0);
        reactiveCell.setCellValue(SHEET_COLUMN_PERIOD);
        reactiveCell.setCellStyle(centeredStyle);

        activeRowIdx += 1;
        reactiveRowIdx += 1;

        int activeColumnIdx = 1;
        int reactiveColumnIdx = 1;

        Map<Integer, String> feedersIdMap = dbService.getFeedersIdMap();
        Map<Integer, Device> devicesIdsMap = dbService.getDevicesMap();
        Map<Device, Integer> activeColumnsMap = new HashMap<Device, Integer>();
        Map<Device, Integer> reactiveColumnsMap = new HashMap<Device, Integer>();

        for (Device device : devicesIdsMap.values()) {
            String feederName = feedersIdMap.get(device.getFeeder());
            if (device.isReactive()) {
                reactiveCell = reactiveRow.createCell(reactiveColumnIdx);
                reactiveCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                reactiveCell.setCellValue(feederName + SUFFIX_REACTIVE);
                reactiveCell.setCellStyle(centeredStyle);
                reactiveColumnsMap.put(device, reactiveColumnIdx);
                reactiveColumnIdx += 1;
            } else {
                activeCell = activeRow.createCell(activeColumnIdx);
                activeCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                activeCell.setCellValue(feederName + SUFFIX_ACTIVE);
                activeCell.setCellStyle(centeredStyle);
                activeColumnsMap.put(device, activeColumnIdx);
                activeColumnIdx += 1;
            }
        }

        for (Date selectedDate : selectedDates) {
            String dateAsString = AppConstans.DATE_FORMAT_SHORT.format(selectedDate);
            List<Measurement> measurements = dbService.getMeasuments(selectedDate);
            if (measurements.size() > 0) {
                for (int period = 0; period < 48; ++period) {
                    String periodAsString = dateAsString
                            + AppConstans.PERIOD_STRING.get(period);
                    activeRow = activeSheet.createRow(activeRowIdx + period);
                    reactiveRow = reactiveSheet.createRow(reactiveRowIdx + period);
                    for (int j = 0, max = activeColumnsMap.size() + 1; j < max; ++j) {
                        activeCell = activeRow.createCell(j);
                        if (j == 0) {
                            activeCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            activeCell.setCellValue(periodAsString);
                        } else {
                            activeCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            activeCell.setCellValue(0d);
                        }
                        activeCell.setCellStyle(centeredStyle);
                    }
                    for (int j = 0, max = reactiveColumnsMap.size() + 1; j < max; ++j) {
                        reactiveCell = reactiveRow.createCell(j);
                        if (j == 0) {
                            reactiveCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            reactiveCell.setCellValue(periodAsString);
                        } else {
                            reactiveCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            reactiveCell.setCellValue(0d);
                        }
                        activeCell.setCellStyle(centeredStyle);
                        reactiveCell.setCellStyle(centeredStyle);
                    }
                }
                for (Measurement measurement : measurements) {
                    Device device = devicesIdsMap.get(measurement.getDevice());
                    int period = measurement.getPeriod();
                    if (device.isReactive()) {
                        reactiveRow = reactiveSheet.getRow(reactiveRowIdx + period - 1);
                        reactiveCell = reactiveRow.getCell(reactiveColumnsMap.get(device));
                        reactiveCell.setCellValue(measurement.getReadout());
                    } else {
                        activeRow = activeSheet.getRow(activeRowIdx + period - 1);
                        activeCell = activeRow.getCell(activeColumnsMap.get(device));
                        activeCell.setCellValue(measurement.getReadout());
                    }
                }
                activeRowIdx += 48;
                reactiveRowIdx += 48;
            }
        }

        // Write the output to a file
        FileOutputStream fileOut;
        String excelFileName = excelFile.getName();
        if (!excelFileName.toLowerCase().endsWith(".xls")) {
            fileOut = new FileOutputStream(excelFile.toString() + ".xls");
        } else {
            fileOut = new FileOutputStream(excelFile);
        }
        wb.write(fileOut);
        fileOut.close();
    }

}
