package com.jgoodies.sample;

import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 29.09.2010 22:15:41 (Europe/Moscow)
 */
public class BindingComboBoxTest {

    public static void main(String[] args) {

        MyBean bean = new MyBean();

        BeanAdapter<MyBean> adapter = new BeanAdapter<MyBean>(bean, true);

        ValueModel intModel = adapter.getValueModel("intValue");
        ValueModel stringModel = adapter.getValueModel("stringValue");

        List<String> possibleValues = new ArrayList<String>();
        possibleValues.add("First Value");
        possibleValues.add("Second Value");
        possibleValues.add("Third Value");
        JComboBox comboBox = new JComboBox(new ComboBoxAdapter<String>(possibleValues, stringModel));
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridLayout(2, 1));
        frame.getContentPane().add(comboBox);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

}
