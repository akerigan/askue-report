package org.akerigan.askue.service;

import org.akerigan.askue.db.*;
import org.akerigan.askue.domain.Device;
import org.akerigan.askue.domain.Feeder;
import org.akerigan.askue.domain.Measurement;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import java.util.*;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 7:41:17 (Europe/Moscow)
 */
public class DbService {

    protected SimpleJdbcTemplate template;

    public void setTemplate(SimpleJdbcTemplate template) {
        this.template = template;
    }

    public void init() {
        Set<String> tables = new TreeSet<String>(
                template.query("select table_name from information_schema.tables", StringMapper.getInstance()));
        if (!tables.contains("FEEDER")) {
            template.update("create table feeder(id identity primary key, name varchar(256))");
        }
        if (!tables.contains("DEVICE")) {
            template.update("create table device(id int, feeder int, reactive bool)");
        } else {
            try {
                template.update("alter table device add enabled bool");
            } catch (DataAccessException ignored) {
            }
        }
        if (!tables.contains("MEASUREMENT")) {
            template.update("create table measurement(id identity primary key, device int, " +
                    "read_date date, period int, readout decimal)"
            );
        }
        Set<String> indexes = new TreeSet<String>(
                template.query("select index_name from information_schema.indexes", StringMapper.getInstance()));
        if (!indexes.contains("FEEDER_NAME_IDX")) {
            template.update("create unique index feeder_name_idx ON feeder(name)");
        }
        if (!indexes.contains("MEASUREMENT_DEVICE_IDX")) {
            template.update("create index measurement_device_idx ON measurement(device)");
        }
        if (!indexes.contains("MEASUREMENT_DATE_IDX")) {
            template.update("create index measurement_date_idx ON measurement(read_date)");
        }
    }

    public List<Feeder> getFeeders() {
        return template.query("select id, name from feeder order by name", FeederMapper.getInstance());
    }

    public Map<Integer, String> getFeedersIdMap() {
        LinkedHashMap<Integer, String> result = new LinkedHashMap<Integer, String>();
        for (Feeder feeder : getFeeders()) {
            result.put(feeder.getId(), feeder.getName());
        }
        return result;
    }

    public Map<String, Integer> getFeedersNameMap() {
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Feeder feeder : getFeeders()) {
            result.put(feeder.getName(), feeder.getId());
        }
        return result;
    }

    public int addFeeder(String name) {
        template.update("insert into feeder (name) values (?)", name);
        return template.queryForInt("select id from feeder where name=?", name);
    }

    public void setFeederName(int id, String name) {
        template.update("update feeder set name=? where id=?", name, id);
    }

    public boolean removeFeeder(int id) {
        int usagesCount = template.queryForInt("select count(*) from device where feeder=?", id);
        if (usagesCount > 0) {
            return false;
        }
        template.update("delete from feeder where id=?", id);
        return true;
    }

    public List<Device> getDevices() {
        return template.query("select id, feeder, reactive, enabled from device order by id", DeviceMapper.getInstance());
    }

    public Map<Integer, Device> getDevicesMap() {
        LinkedHashMap<Integer, Device> result = new LinkedHashMap<Integer, Device>();
        for (Device device : getDevices()) {
            result.put(device.getId(), device);
        }
        return result;
    }

    public void addDevice(int deviceId) {
        template.update("insert into device (id) values (?)", deviceId);
    }

    public void setDeviceFeeder(int id, int feederId) {
        template.update("update device set feeder=? where id=?", feederId, id);
    }

    public void setDeviceReactive(int id, boolean reactive) {
        template.update("update device set reactive=? where id=?", reactive, id);
    }

    public void setDeviceEnabled(int id, boolean enabled) {
        template.update("update device set enabled=? where id=?", enabled, id);
    }

    public void addMeasurement(int deviceId, Date date, int period, float readout) {
        template.update("insert into measurement (device, read_date, period, readout) values (?,?,?,?)",
                deviceId, date, period, readout
        );
    }

    public void deleteMeasurements(Date date) {
        template.update("delete from measurement where read_date=?", date);
    }

    public List<Date> getMeasumentsDates() {
        return template.query("select distinct read_date from measurement order by read_date", DateMapper.getInstance());
    }

    public List<Measurement> getMeasuments(Date date) {
        return template.query("select id, device, read_date, period, readout from measurement " +
                "where read_date=? order by period, device", MeasurementMapper.getInstance(), date);
    }

}
