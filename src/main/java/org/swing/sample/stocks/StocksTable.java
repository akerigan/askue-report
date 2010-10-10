package org.swing.sample.stocks;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 0:04:46 (Europe/Moscow)
 */
public class StocksTable extends JFrame

{

    protected JTable m_table;

    protected StockTableData m_data;

    protected JLabel m_title;

    public StocksTable() {

        super("Stocks Table");

        setSize(600, 340);

        m_data = new StockTableData();

        m_title = new JLabel(m_data.getTitle(),

                new ImageIcon("money.gif"), SwingConstants.LEFT);

        m_title.setFont(new Font("TimesRoman", Font.BOLD, 24));

        m_title.setForeground(Color.black);

        getContentPane().add(m_title, BorderLayout.NORTH);

        m_table = new JTable();

        m_table.setAutoCreateColumnsFromModel(false);

        m_table.setModel(m_data);

        for (int k = 0; k < StockTableData.m_columns.length; k++) {

            DefaultTableCellRenderer renderer = new

                    DefaultTableCellRenderer();

            renderer.setHorizontalAlignment(

                    StockTableData.m_columns[k].m_alignment);

            TableColumn column = new TableColumn(k,

                    StockTableData.m_columns[k].m_width, renderer, null);

            m_table.addColumn(column);

        }

        JTableHeader header = m_table.getTableHeader();

        header.setUpdateTableInRealTime(false);

        JScrollPane ps = new JScrollPane();

        ps.getViewport().add(m_table);

        getContentPane().add(ps, BorderLayout.CENTER);

        WindowListener wndCloser = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {

                System.exit(0);

            }

        };

        addWindowListener(wndCloser);

        setVisible(true);

    }

    public static void main(String argv[]) {

        new StocksTable();

    }

}
