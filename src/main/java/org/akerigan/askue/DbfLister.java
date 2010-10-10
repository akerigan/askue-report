package org.akerigan.askue;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 28.09.2010 21:20:34 (Europe/Moscow)
 */
public class DbfLister {

    public static void main(String[] args) throws FileNotFoundException, DBFException {
        FileInputStream in = new FileInputStream("/tmp/ASKUE/dd100924.dbf");
        DBFReader dbfReader = new DBFReader(in);
        dbfReader.setCharactersetName(AppConstans.ENCODING_DBF_DEFAULT);
        int fieldsCount = dbfReader.getFieldCount();
        for (int i = 0; i < fieldsCount; ++i) {
            DBFField dbfField = dbfReader.getField(i);
            System.out.println("dbfField = " + dbfField);
        }
        int recordsCount = dbfReader.getRecordCount();
        for (int i = 0; i < recordsCount; ++i) {
            Object[] record = dbfReader.nextRecord();
            for (Object o : record) {
                System.out.print(o);
                System.out.print(';');
            }
            System.out.print('\n');
        }
    }

}
