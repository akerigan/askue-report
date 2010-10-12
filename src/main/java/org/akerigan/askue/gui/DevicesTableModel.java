package org.akerigan.askue.gui;

import org.akerigan.askue.domain.Device;
import org.akerigan.askue.service.DbService;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.akerigan.askue.AppConstans.*;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 22:20:09 (Europe/Moscow)
 */
public class DevicesTableModel implements TableModel {

    private List<TableModelListener> modelListeners = new LinkedList<TableModelListener>();
    private Lock listenersLock = new ReentrantLock();

    private DbService dbService;
    private List<Device> devices;
    private Map<Integer, String> feedersIdMap;
    private Map<String, Integer> feedersNameMap;

    public DevicesTableModel(DbService dbService) {
        this.dbService = dbService;
        refresh();
    }

    public void refresh() {
        feedersIdMap = dbService.getFeedersIdMap();
        feedersNameMap = dbService.getFeedersNameMap();
        devices = new LinkedList<Device>(dbService.getDevices());
        fireEvent(new TableModelEvent(
                this, 0, devices.size() - 1,
                TableModelEvent.ALL_COLUMNS,
                TableModelEvent.INSERT
        ));
    }

    public int getRowCount() {
        return devices.size();
    }

    public int getColumnCount() {
        return 4;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return COLUMN_DEVICE_CODE;
            case 1:
                return COLUMN_DEVICE_FEEDER;
            case 2:
                return COLUMN_DEVICE_REACTIVE;
            case 3:
                return COLUMN_DEVICE_ENABLED;
            default:
                return "";
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return Boolean.class;
            case 3:
                return Boolean.class;
            default:
                return null;
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex > 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Device device = devices.get(rowIndex);
        if (device != null) {
            switch (columnIndex) {
                case 0:
                    return device.getId();
                case 1:
                    String feederName = feedersIdMap.get(device.getFeeder());
                    return feederName != null ? feederName : "";
                case 2:
                    return device.isReactive();
                case 3:
                    return device.isEnabled();
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Device device = devices.get(rowIndex);
        if (device != null) {
            switch (columnIndex) {
                case 0:
                    Integer deviceId = (Integer) value;
                    device.setId(deviceId);
                    dbService.addDevice(deviceId);
                case 1:
                    String stringValue = (String) value;
                    if (feedersNameMap.containsKey(stringValue)) {
                        int feederId = feedersNameMap.get(stringValue);
                        if (device.getFeeder() != feederId) {
                            device.setFeeder(feederId);
                            dbService.setDeviceFeeder(device.getId(), feederId);
                        }
                    } else {
                        fireEvent(new TableModelEvent(this, rowIndex, rowIndex, columnIndex, TableModelEvent.UPDATE));
                    }
                    break;
                case 2:
                    Boolean reactive = (Boolean) value;
                    if (reactive != device.isReactive()) {
                        device.setReactive(reactive);
                        dbService.setDeviceReactive(device.getId(), reactive);
                        fireEvent(new TableModelEvent(this, rowIndex, rowIndex, columnIndex, TableModelEvent.UPDATE));
                    }
                    break;
                case 3:
                    Boolean enabled = (Boolean) value;
                    if (enabled != device.isEnabled()) {
                        device.setEnabled(enabled);
                        dbService.setDeviceEnabled(device.getId(), enabled);
                        fireEvent(new TableModelEvent(this, rowIndex, rowIndex, columnIndex, TableModelEvent.UPDATE));
                    }
                    break;
            }
        }
    }

    public void addTableModelListener(TableModelListener l) {
        listenersLock.lock();
        try {
            modelListeners.add(l);
        } finally {
            listenersLock.unlock();
        }
    }

    public void removeTableModelListener(TableModelListener l) {
        listenersLock.lock();
        try {
            modelListeners.remove(l);
        } finally {
            listenersLock.unlock();
        }
    }

    private void fireEvent(TableModelEvent event) {
        listenersLock.lock();
        try {
            for (TableModelListener modelListener : modelListeners) {
                modelListener.tableChanged(event);
            }
        } finally {
            listenersLock.unlock();
        }
    }
}
