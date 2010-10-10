package org.akerigan.askue.gui;

import org.akerigan.askue.service.DbService;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.akerigan.askue.AppConstans.DATE_FORMAT_DATES_LIST;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 20:55:58 (Europe/Moscow)
 */
public class DatesListModel implements ListModel {

    private List<ListDataListener> dataListeners = new LinkedList<ListDataListener>();
    private Lock listenersLock = new ReentrantLock();

    private List<Date> dates;

    private DbService dbService;

    public DatesListModel(DbService dbService) {
        this.dbService = dbService;
        dates = dbService.getMeasumentsDates();
    }

    public void refresh() {
        dates = dbService.getMeasumentsDates();
        listenersLock.lock();
        try {
            for (ListDataListener dataListener : dataListeners) {
                dataListener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, dates.size() - 1));
            }
        } finally {
            listenersLock.unlock();
        }
    }

    public int getSize() {
        return dates.size();
    }

    public Object getElementAt(int index) {
        return DATE_FORMAT_DATES_LIST.format(dates.get(index));
    }

    public void addListDataListener(ListDataListener l) {
        listenersLock.lock();
        try {
            dataListeners.add(l);
        } finally {
            listenersLock.unlock();
        }
    }

    public void removeListDataListener(ListDataListener l) {
        listenersLock.lock();
        try {
            dataListeners.remove(l);
        } finally {
            listenersLock.unlock();
        }
    }
}
