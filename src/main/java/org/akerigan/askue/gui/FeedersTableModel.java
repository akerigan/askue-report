package org.akerigan.askue.gui;

import org.akerigan.askue.domain.Feeder;
import org.akerigan.askue.service.DbService;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.akerigan.askue.AppConstans.*;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 22:20:09 (Europe/Moscow)
 */
public class FeedersTableModel implements TableModel {

    private List<TableModelListener> modelListeners = new LinkedList<TableModelListener>();
    private Lock listenersLock = new ReentrantLock();

    private FeedersComboBoxModel comboBoxModel;
    private DevicesTableModel tableModel;
    private DbService dbService;
    private List<Feeder> feeders;

    public FeedersTableModel(FeedersComboBoxModel comboBoxModel, DevicesTableModel tableModel) {
        this.comboBoxModel = comboBoxModel;
        this.tableModel = tableModel;
        dbService = comboBoxModel.getDbService();
        this.feeders = new LinkedList<Feeder>(dbService.getFeeders());
    }

    public int getRowCount() {
        return feeders.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return COLUMN_FEEDER_CODE;
            case 1:
                return COLUMN_FEEDER_NAME;
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
            default:
                return null;
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex > 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < feeders.size()) {
            Feeder feeder = feeders.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return feeder.getId();
                case 1:
                    return feeder.getName();
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < feeders.size()) {
            Feeder feeder = feeders.get(rowIndex);
            switch (columnIndex) {
                case 1:
                    String name = (String) value;
                    if (!name.equals(feeder.getName())) {
                        Set<String> feedersNames = new HashSet<String>(dbService.getFeedersNameMap().keySet());
                        feedersNames.remove(feeder.getName());
                        if (feedersNames.contains(name)) {
                            int idx = 1;
                            do {
                                name = value + DUBLICATE_FEEDER_NAME_PART_1
                                        + idx + DUBLICATE_FEEDER_NAME_PART_2;
                                idx += 1;
                            } while (feedersNames.contains(name));
                        }
                        if (feeder.getId() == 0) {
                            feeder.setId(dbService.addFeeder(name));
                            fireEvent(new TableModelEvent(
                                    this, rowIndex, rowIndex,
                                    TableModelEvent.ALL_COLUMNS,
                                    TableModelEvent.INSERT
                            ));
                        } else {
                            dbService.setFeederName(feeder.getId(), name);
                        }
                        feeder.setName(name);
                        comboBoxModel.refresh();
                        tableModel.refresh();
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

    public void addNewFeeder(int currentRow) {
        int row = currentRow + 1;
        feeders.add(row, new Feeder());
        fireEvent(new TableModelEvent(
                this, row, row,
                TableModelEvent.ALL_COLUMNS,
                TableModelEvent.INSERT
        ));
    }

    public boolean removeFeeder(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < feeders.size()) {
            Feeder feeder = feeders.get(rowIndex);
            if (dbService.removeFeeder(feeder.getId())) {
                feeders.remove(rowIndex);
                fireEvent(new TableModelEvent(
                        this, rowIndex, rowIndex,
                        TableModelEvent.ALL_COLUMNS,
                        TableModelEvent.DELETE
                ));
                comboBoxModel.refresh();
                tableModel.refresh();
                return true;
            }
        }
        return false;
    }

}
