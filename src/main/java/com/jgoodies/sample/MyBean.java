package com.jgoodies.sample;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

import java.beans.PropertyChangeListener;

public class MyBean {
    // Note you don't HAVE to use this class - you can use
    // java.beans.PropertyChangeSupport if you want.
    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    private String name;
    private boolean booleanValue;
    private String stringValue;
    private int intValue;

    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }

    public void removePropertyChangeListener(PropertyChangeListener x) {
        changeSupport.removePropertyChangeListener(x);
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        String oldName = name;
        this.name = newName;
        changeSupport.firePropertyChange("name", oldName, newName);
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean newValue) {
        System.out.println("Boolean value set: " + newValue);
        boolean oldValue = booleanValue;
        booleanValue = newValue;
        changeSupport.firePropertyChange("booleanValue", oldValue, newValue);
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String newValue) {
        System.out.println("String value set: " + newValue);
        String oldValue = stringValue;
        this.stringValue = newValue;
        changeSupport.firePropertyChange("stringValue", oldValue, newValue);
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int newValue) {
        System.out.println("Int value set: " + newValue);
        int oldValue = intValue;
        intValue = newValue;
        changeSupport.firePropertyChange("intValue", oldValue, newValue);
    }

    public String toString() {
        return getName();
    }

}