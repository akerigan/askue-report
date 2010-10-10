package org.akerigan.askue.db;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 27.04.2010 22:55:45 (Europe/Moscow)
 */
public class DateMapper implements RowMapper<Date> {

    private static DateMapper instance = new DateMapper();

    public DateMapper() {
        // prevents instantiation
    }

    public static DateMapper getInstance() {
        return instance;
    }

    public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getDate(1);
    }
}
