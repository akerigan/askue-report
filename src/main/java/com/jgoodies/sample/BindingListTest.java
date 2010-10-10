package com.jgoodies.sample;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 29.09.2010 22:09:29 (Europe/Moscow)
 */
public class BindingListTest {

    public static void main(String[] args) {

        MyBean bean = new MyBean();

        BeanAdapter<MyBean> adapter = new BeanAdapter<MyBean>(bean, true);

        ValueModel intModel = adapter.getValueModel("intValue");
        ValueModel stringModel = adapter.getValueModel("stringValue");

        List<String> possibleValues = new ArrayList<String>();
        possibleValues.add("First Value");
        possibleValues.add("Second Value");
        possibleValues.add("Third Value");
        JList jlist = BasicComponentFactory.createList(new SelectionInList<String>(possibleValues, stringModel, intModel));
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridLayout(2, 1));
        frame.getContentPane().add(jlist);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

}
