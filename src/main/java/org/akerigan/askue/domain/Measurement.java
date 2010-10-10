package org.akerigan.askue.domain;

import java.util.Date;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 8:15:24 (Europe/Moscow)
 */
public class Measurement {

    private int id;
    private int device;
    private Date readDate;
    private int period;
    private double readout;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public Date getReadDate() {
        return readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public double getReadout() {
        return readout;
    }

    public void setReadout(double readout) {
        this.readout = readout;
    }
}
