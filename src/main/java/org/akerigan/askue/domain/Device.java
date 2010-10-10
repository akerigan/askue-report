package org.akerigan.askue.domain;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 8:10:02 (Europe/Moscow)
 */
public class Device {

    private int id;
    private int feeder;
    private boolean reactive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFeeder() {
        return feeder;
    }

    public void setFeeder(int feeder) {
        this.feeder = feeder;
    }

    public boolean isReactive() {
        return reactive;
    }

    public void setReactive(boolean reactive) {
        this.reactive = reactive;
    }
}
