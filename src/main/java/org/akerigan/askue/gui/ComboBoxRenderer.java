package org.akerigan.askue.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 10.10.2010 16:17:18 (Europe/Moscow)
 */
public class ComboBoxRenderer extends JComboBox implements TableCellRenderer {

    public ComboBoxRenderer(ComboBoxModel model) {
        super(model);
    }

    public JComponent getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column
    ) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        // Select the current value
        setSelectedItem(value);
        return this;
    }
}
