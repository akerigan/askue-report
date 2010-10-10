package org.akerigan.askue.gui;

import org.akerigan.askue.service.DbService;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 10.10.2010 16:21:37 (Europe/Moscow)
 */
public class FeedersComboBoxModel implements ComboBoxModel {

    private List<ListDataListener> listeners = new LinkedList<ListDataListener>();
    private Lock listenersLock = new ReentrantLock();

    private DbService dbService;

    private LinkedList<String> feederNames;

    private int selectedIndex;

    public FeedersComboBoxModel(DbService dbService) {
        this.dbService = dbService;
        refresh();
    }

    public DbService getDbService() {
        return dbService;
    }

    public void refresh() {
        feederNames = new LinkedList<String>(dbService.getFeedersNameMap().keySet());
        fireEvent(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, feederNames.size() - 1));
    }

    @SuppressWarnings({"SuspiciousMethodCalls"})
    public void setSelectedItem(Object item) {
        int index = feederNames.indexOf(item);
        if (index >= 0 && index != selectedIndex) {
            selectedIndex = index;
            fireEvent(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, selectedIndex, selectedIndex));
        }
    }

    public Object getSelectedItem() {
        if (feederNames.size() > 0) {
            return feederNames.get(selectedIndex);
        } else {
            return "";
        }
    }

    public int getSize() {
        return feederNames.size();
    }

    public Object getElementAt(int index) {
        return feederNames.get(index);
    }

    public void addListDataListener(ListDataListener l) {
        listenersLock.lock();
        try {
            listeners.add(l);
        } finally {
            listenersLock.unlock();
        }
    }

    public void removeListDataListener(ListDataListener l) {
        listenersLock.lock();
        try {
            listeners.remove(l);
        } finally {
            listenersLock.unlock();
        }
    }

    private void fireEvent(ListDataEvent e) {
        listenersLock.lock();
        try {
            for (ListDataListener listener : listeners) {
                listener.contentsChanged(e);
            }
        } finally {
            listenersLock.unlock();
        }
    }
}
