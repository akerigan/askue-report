package org.akerigan.askue.domain;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 8:10:02 (Europe/Moscow)
 */
public class Device {

    private int id;
    private int feeder;
    private boolean reactive;
    private boolean enabled;

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        Device device = (Device) o;
        return this == o || !(o == null || getClass() != o.getClass())
                && id == device.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
