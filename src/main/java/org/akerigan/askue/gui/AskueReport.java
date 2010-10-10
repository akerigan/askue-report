package org.akerigan.askue.gui;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.akerigan.askue.service.AscueReportService;
import org.akerigan.askue.service.DbService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 01.10.2010 22:16:58 (Europe/Moscow)
 */
public class AskueReport {

    private static Log log = LogFactory.getLog(AskueReport.class);

    private AskueReport() {
        // prevents instantiation
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/appContext.xml");

        DbService dbService = (DbService) ctx.getBean("dbService");
        AscueReportService askueService = (AscueReportService) ctx.getBean("askueService");

        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }

        JFrame frame = new JFrame();
        frame.setTitle("Отчеты АСКУЭ :: Менеджер данных");
        frame.setSize(600, 350);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setIconImage(createImage("/icons/finance.png"));

        DatesListModel datesListModel = new DatesListModel(dbService);
        DevicesTableModel devicesModel = new DevicesTableModel(dbService);

        frame.setJMenuBar(createMenuBar(askueService, datesListModel, devicesModel));
        frame.getContentPane().add(buildMainPanel(datesListModel, devicesModel));
        frame.pack();

        locateOnOpticalScreenCenter(frame);
        frame.setVisible(true);
    }

    public static JMenuBar createMenuBar(
            AscueReportService service,
            DatesListModel datesListModel,
            DevicesTableModel devicesModel
    ) {
        JMenuBar result = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        result.add(fileMenu);

        JMenuItem importMenuItem = new JMenuItem(
                "Импорт dbf", createImageIcon("/icons/import.png")
        );
        fileMenu.add(importMenuItem);
        importMenuItem.addActionListener(new ImportActionListener(service, datesListModel, devicesModel));

        fileMenu.addSeparator();

        JMenuItem closeMenuItem = new JMenuItem(
                "Закрыть", createImageIcon("/icons/close.png")
        );
        fileMenu.add(closeMenuItem);
        closeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        return result;
    }

    static class ImportActionListener implements ActionListener {

        private AscueReportService service;
        private DatesListModel datesListModel;
        private DevicesTableModel devicesModel;

        public ImportActionListener(
                AscueReportService service,
                DatesListModel datesListModel,
                DevicesTableModel devicesModel
        ) {
            this.service = service;
            this.datesListModel = datesListModel;
            this.devicesModel = devicesModel;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fc = new JFileChooser();
            fc.setMultiSelectionEnabled(true);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Database files (*.dbf)", "dbf");
            fc.setFileFilter(filter);
            int returnVal = fc.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                for (File file : fc.getSelectedFiles()) {
                    log.info("Importing: " + file.toString());
                    try {
                        service.importDbf(file);
                    } catch (Exception e) {
                        log.error("Error importing file: " + file.toString(), e);
                        JOptionPane.showMessageDialog(
                                null, e.getMessage(), "Ошибка импорта файла", JOptionPane.ERROR_MESSAGE);
                    }
                }
                datesListModel.refresh();
                devicesModel.refresh();
            }
        }

    }

    /**
     * Locates the given component on the screen's center.
     *
     * @param component the component to be centered
     */
    public static void locateOnOpticalScreenCenter(Component component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation(
                (screenSize.width - paneSize.width) / 2,
                (int) ((screenSize.height - paneSize.height) * 0.45));
    }

    private static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = AskueReport.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            log.error("Couldn't find file: " + path);
            return null;
        }
    }

    private static Image createImage(String path) {
        java.net.URL imgURL = AskueReport.class.getResource(path);
        if (imgURL != null) {
            return Toolkit.getDefaultToolkit().createImage(imgURL);
        } else {
            log.error("Couldn't find file: " + path);
            return null;
        }
    }

    private static JPanel buildDatePanel(DatesListModel listModel) {
        FormLayout layout = new FormLayout(
                "fill:110dlu:grow, p",
                "p, 1dlu, fill:100dlu:grow"
        );

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        JList list = new JList(listModel);
        JButton exportButton = createButton("/icons/export.png", "Экспорт в Excel");
        exportButton.addActionListener(new ExportActionListener(list));

        builder.addTitle("Импортированные даты:", cc.xy(1, 1));
        builder.add(exportButton, cc.xy(2, 1));
        builder.add(new JScrollPane(list), cc.xyw(1, 3, 2));

        return builder.getPanel();
    }

    private static class ExportActionListener implements ActionListener {

        private JList list;

        public ExportActionListener(JList list) {
            this.list = list;
        }

        public void actionPerformed(ActionEvent e) {
            Object[] selectedValues = list.getSelectedValues();
            if (selectedValues.length == 0) {
                JOptionPane.showMessageDialog(null, "Не выбрано ни одной даты", "Информация", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Object selectedValue : selectedValues) {
                    System.out.println("selectedValue = " + selectedValue);
                }
                JOptionPane.showMessageDialog(null, "Данные успешно экспортированы", "Информация", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }

    private static JPanel buildFeedersPanel(DbService dbService) {
        FormLayout layout = new FormLayout(
                "fill:110dlu:grow, p, p",
                "p, 1dlu, fill:100dlu:grow"
        );

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        FeedersTableModel tableModel = new FeedersTableModel(dbService);
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton addButton = createButton("/icons/add.png", "Добавить фидер");
        addButton.addActionListener(new AddFeederActionListener(table));

        JButton removeButton = createButton("/icons/remove.png", "Удалить фидер");
        removeButton.addActionListener(new RemoveFeederActionListener(table));

        builder.addTitle("Фидеры:", cc.xy(1, 1));
        builder.add(addButton, cc.xy(2, 1));
        builder.add(removeButton, cc.xy(3, 1));
        builder.add(new JScrollPane(table), cc.xyw(1, 3, 3));

        return builder.getPanel();
    }

    private static class AddFeederActionListener implements ActionListener {

        private JTable table;

        public AddFeederActionListener(JTable table) {
            this.table = table;
        }

        public void actionPerformed(ActionEvent e) {
            FeedersTableModel tableModel = (FeedersTableModel) table.getModel();
            int selectedRow = table.getSelectedRow();
            tableModel.addNewFeeder(selectedRow);
            table.requestFocus();
            setSelection(table, selectedRow, 1);
        }
    }

    private static class RemoveFeederActionListener implements ActionListener {

        private JTable table;

        public RemoveFeederActionListener(JTable table) {
            this.table = table;
        }

        public void actionPerformed(ActionEvent e) {
            FeedersTableModel tableModel = (FeedersTableModel) table.getModel();
            int selectedRow = table.getSelectedRow();
            int decision = JOptionPane.showConfirmDialog(null, "Удалить фидер?", "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (decision == JOptionPane.YES_OPTION) {
                if (tableModel.removeFeeder(selectedRow)) {
                    setSelection(table, selectedRow, 1);
                } else {
                    JOptionPane.showMessageDialog(
                            null, "Невозможно удалить фидер: есть ассоциированные с ним счетчики",
                            "Удаление фидера", JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
            table.requestFocus();
        }
    }

    private static JComponent buildMainPanel(DatesListModel listModel, DevicesTableModel devicesModel) {
        return new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                true,
                buildDatePanel(listModel),
                new JSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        true, buildFeedersPanel(listModel.getDbService()),
                        buildDevicesPanel(devicesModel)
                ));
    }

    private static void setSelection(JTable table, int selectedRow, int selectedColumn) {
        int rowCount = table.getRowCount();
        if (rowCount > 0) {
            table.removeRowSelectionInterval(0, rowCount - 1);
            if (rowCount <= selectedRow) {
                selectedRow = rowCount - 1;
            } else if (selectedRow < 0) {
                selectedRow = 0;
            }
            table.addRowSelectionInterval(selectedRow, selectedRow);
            table.setEditingRow(selectedRow);
            if (selectedColumn >= 0) {
                table.setEditingColumn(selectedColumn);
            }
        }
    }

    private static JButton createButton(String resourcePath, String tooltipText) {
        ImageIcon icon = createImageIcon(resourcePath);
        JButton button = new JButton(icon);
        button.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        button.setToolTipText(tooltipText);
        return button;
    }

    private static JPanel buildDevicesPanel(DevicesTableModel tableModel) {
        FormLayout layout = new FormLayout(
                "fill:110dlu:grow",
                "p, 1dlu, fill:100dlu:grow"
        );

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        builder.addTitle("Счетчики:", cc.xy(1, 1));
        builder.add(new JScrollPane(table), cc.xy(1, 3));

        return builder.getPanel();
    }

}


