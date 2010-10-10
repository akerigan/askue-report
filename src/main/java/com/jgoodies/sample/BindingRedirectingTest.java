package com.jgoodies.sample;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.value.Trigger;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.factories.ButtonBarFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 29.09.2010 22:24:36 (Europe/Moscow)
 */
public class BindingRedirectingTest {

    public static void main(String[] args) {

        List<MyBean> possibleValues = createBeans();

        final Trigger trigger = new Trigger();
        // set the presentation model up to the first bean.
        PresentationModel<MyBean> beanPresentationModel = new PresentationModel<MyBean>(
                possibleValues.get(0), trigger);

        // create a property adapter for the presentation model 'bean' property.
        ValueModel beanProperty = new PropertyAdapter<PresentationModel<MyBean>>(beanPresentationModel, "bean");
        // wire our new combobox up to that property adapter.
        JComboBox comboBox = new JComboBox(new ComboBoxAdapter<MyBean>(possibleValues,
                beanProperty));

        // Get buffered model objects.
        ValueModel nameModel = beanPresentationModel.getBufferedModel("name");
        ValueModel booleanModel = beanPresentationModel
                .getBufferedModel("booleanValue");
        ValueModel stringModel = beanPresentationModel
                .getBufferedModel("stringValue");
        // creates a JCheckBox with the property adapter providing the underlying
        // model.
        JLabel label = BasicComponentFactory.createLabel(nameModel);
        JCheckBox box = BasicComponentFactory.createCheckBox(booleanModel,
                "Boolean Value");
        JTextField field = BasicComponentFactory.createTextField(stringModel);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                System.out.println("OK Button Pressed");
                trigger.triggerCommit();
            }
        });

        // First, disable the OK button.
        okButton.setEnabled(false);
        // Note here that we wire the OK 'enabled' state to the presentation model
        // 'buffering' state.
        PropertyConnector.connect(beanPresentationModel, "buffering", okButton,
                "enabled");

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                trigger.triggerFlush();
            }
        });

        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridLayout(5, 1));
        frame.getContentPane().add(comboBox);
        frame.getContentPane().add(label);
        frame.getContentPane().add(box);
        frame.getContentPane().add(field);
        frame.getContentPane().add(
                ButtonBarFactory.buildOKCancelBar(okButton, cancelButton));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * @return list of beans
     */
    private static List<MyBean> createBeans() {
        // create a couple beans.
        MyBean bean1 = new MyBean();
        bean1.setName("Bean 1");
        bean1.setBooleanValue(true);
        bean1.setStringValue("tweedle-dee");
        MyBean bean2 = new MyBean();
        bean2.setName("Bean 2");
        bean2.setBooleanValue(false);
        bean2.setStringValue("tweedle-dum");
        List<MyBean> possibleValues = new ArrayList<MyBean>();
        possibleValues.add(bean1);
        possibleValues.add(bean2);
        return possibleValues;
    }

}
