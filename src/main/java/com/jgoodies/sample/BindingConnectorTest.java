package com.jgoodies.sample;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 29.09.2010 22:03:59 (Europe/Moscow)
 */
public class BindingConnectorTest {

    public static void main(String[] args) {

        MyBean bean = new MyBean();

        BeanAdapter<MyBean> adapter = new BeanAdapter<MyBean>(bean, true);

        ValueModel stringModel = adapter.getValueModel("stringValue");
        JLabel label = BasicComponentFactory.createLabel(stringModel);

        JRadioButton button1 = BasicComponentFactory.createRadioButton(stringModel, "value1", "Value 1");
        JRadioButton button2 = BasicComponentFactory.createRadioButton(stringModel, "value2", "Value 2");

        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridLayout(3, 1));
        frame.getContentPane().add(label);
        frame.getContentPane().add(button1);
        frame.getContentPane().add(button2);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

}
