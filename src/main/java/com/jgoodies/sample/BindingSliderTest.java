package com.jgoodies.sample;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 29.09.2010 21:57:55 (Europe/Moscow)
 */
public class BindingSliderTest {

    public static void main(String[] args) {

        MyBean bean = new MyBean();

        // Bean adapter is an adapter that can create many value model objects for a single
        // bean. It is more efficient than the property adapter. The 'true' once again means
        // we want it to observe our bean for changes.
        BeanAdapter<MyBean> adapter = new BeanAdapter<MyBean>(bean, true);

        ValueModel intModel = adapter.getValueModel("intValue");
        ValueModel stringModel = adapter.getValueModel("stringValue");
        // creates a slider and a new bounded range adapter.
        // the range adapter is given our integer value model, and a range of 0-500.
        JSlider slider = new JSlider(new BoundedRangeAdapter(intModel, 0, 0, 500));
        JRadioButton button1 = BasicComponentFactory.createRadioButton(stringModel, "value1", "Value 1");
        JRadioButton button2 = BasicComponentFactory.createRadioButton(stringModel, "value2", "Value 2");
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridLayout(2, 1));
        frame.getContentPane().add(slider);
        frame.getContentPane().add(button1);
        frame.getContentPane().add(button2);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

}
