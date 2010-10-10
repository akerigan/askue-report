package org.akerigan.askue.db;

import org.akerigan.askue.domain.Device;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 8:21:45 (Europe/Moscow)
 */
public class DeviceMapper implements RowMapper<Device> {

    private static DeviceMapper instance = new DeviceMapper();

    private DeviceMapper() {
        // prevents instantiation
    }

    public static DeviceMapper getInstance() {
        return instance;
    }

    public Device mapRow(ResultSet resultSet, int i) throws SQLException {
        Device result = new Device();
        result.setId(resultSet.getInt(1));
        result.setFeeder(resultSet.getInt(2));
        result.setReactive(resultSet.getBoolean(3));
        return result;
    }
}
