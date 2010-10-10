package com.jgoodies.sample;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 29.09.2010 21:54:21 (Europe/Moscow)
 */
public class BindingRadioTest {

    public static void main(String[] args) {
        MyBean bean = new MyBean();

        // Bean adapter is an adapter that can create many value model objects for a single
        // bean. It is more efficient than the property adapter. The 'true' once again means
        // we want it to observe our bean for changes.
        BeanAdapter<MyBean> adapter = new BeanAdapter<MyBean>(bean, true);

        // set the string value to one of the radio button's associated values
        bean.setStringValue("value1");

        ValueModel booleanModel = adapter.getValueModel("booleanValue");
        ValueModel stringModel = adapter.getValueModel("stringValue");
        // creates a JCheckBox with the property adapter providing the underlying model.
        JCheckBox box = BasicComponentFactory.createCheckBox(booleanModel, "Boolean Value");
        // create two radio buttons... 1st one binds 'value1' to 'stringValue'
        // 2nd one binds 'value2'.
        JRadioButton button1 = BasicComponentFactory.createRadioButton(stringModel, "value1", "Value 1");
        JRadioButton button2 = BasicComponentFactory.createRadioButton(stringModel, "value2", "Value 2");
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridLayout(2, 1));
        frame.getContentPane().add(box);
        frame.getContentPane().add(button1);
        frame.getContentPane().add(button2);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

}
