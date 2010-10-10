package org.akerigan.askue.db;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 27.04.2010 22:55:45 (Europe/Moscow)
 */
public class StringMapper implements RowMapper<String> {

    private static StringMapper instance = new StringMapper();

    public StringMapper() {
        // prevents instantiation
    }

    public static StringMapper getInstance() {
        return instance;
    }

    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString(1);
    }
}
