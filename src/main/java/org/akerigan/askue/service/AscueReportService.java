package org.akerigan.askue.service;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import org.akerigan.askue.domain.Device;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 8:29:44 (Europe/Moscow)
 */
public class AscueReportService {

    private static final Pattern DBF_FILE_NAME_PATTERN = Pattern.compile("[Dd]{2}\\d{6}\\.[Dd][Bb][Ff]");

    private DbService dbService;

    private static Log log = LogFactory.getLog(AscueReportService.class);

    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void importDbf(File dbfFile) throws FileNotFoundException, DBFException {
        if (dbfFile != null && dbfFile.exists()) {
            Matcher matcher = DBF_FILE_NAME_PATTERN.matcher(dbfFile.getName());
            if (!matcher.matches()) {
                throw new IllegalStateException("Неправильный dbf файл, имя файла должно начинаться " +
                        "с dd, затем 6 цифр даты, затем расширение dbf: " + dbfFile.getName()
                );
            }
            DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
            Date fileDate;
            try {
                fileDate = dateFormat.parse(dbfFile.getName().substring(2, 8));
            } catch (ParseException e) {
                log.error("", e);
                throw new IllegalStateException("Неправильная дата в имени файла: " + dbfFile.getName());
            }
            dbService.deleteMeasurements(fileDate);
            FileInputStream in = new FileInputStream(dbfFile);
            DBFReader dbfReader = new DBFReader(in);
            dbfReader.setCharactersetName("CP866");
            int fieldsCount = dbfReader.getFieldCount();
            if (fieldsCount != 50) {
                throw new IllegalStateException("Неправильный dbf файл: должно быть 50 колонок");
            }
            for (int i = 0; i < fieldsCount; ++i) {
                DBFField dbfField = dbfReader.getField(i);
                if (dbfField.getDataType() != DBFField.FIELD_TYPE_N) {
                    throw new IllegalStateException("Неправильный dbf файл: все колонки должны быть числового типа");
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

}
