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

}
