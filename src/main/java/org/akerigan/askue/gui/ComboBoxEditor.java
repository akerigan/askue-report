package org.akerigan.askue.gui;

import javax.swing.*;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 10.10.2010 16:18:36 (Europe/Moscow)
 */
public class ComboBoxEditor extends DefaultCellEditor {

    public ComboBoxEditor(ComboBoxModel model) {
        super(new JComboBox(model));
    }

}