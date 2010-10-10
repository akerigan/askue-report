package org.akerigan.askue.db;

import org.akerigan.askue.domain.Measurement;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 8:27:00 (Europe/Moscow)
 */
public class MeasurementMapper implements RowMapper<Measurement> {

    private static MeasurementMapper instance = new MeasurementMapper();

    private MeasurementMapper() {
    }

    public static MeasurementMapper getInstance() {
        return instance;
    }

    public Measurement mapRow(ResultSet resultSet, int i) throws SQLException {
        Measurement result = new Measurement();
        result.setId(resultSet.getInt(1));
        result.setDevice(resultSet.getInt(2));
        result.setReadDate(resultSet.getDate(3));
        result.setPeriod(resultSet.getInt(4));
        result.setReadout(resultSet.getDouble(5));
        return result;
    }

}
