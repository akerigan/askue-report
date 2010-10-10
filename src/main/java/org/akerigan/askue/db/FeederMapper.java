package org.akerigan.askue.db;

import org.akerigan.askue.domain.Feeder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 8:17:42 (Europe/Moscow)
 */
public class FeederMapper implements RowMapper<Feeder> {

    private static FeederMapper instance = new FeederMapper();

    private FeederMapper() {
        // prevents instantiation
    }

    public static FeederMapper getInstance() {
        return instance;
    }

    public Feeder mapRow(ResultSet resultSet, int i) throws SQLException {
        Feeder result = new Feeder();
        result.setId(resultSet.getInt(1));
        result.setName(resultSet.getString(2));
        return result;
    }

}
