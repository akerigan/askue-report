package com.jgoodies.sample;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.jgoodies.validation.view.ValidationResultViewFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

public class ValidationSample {

    protected static Log log = LogFactory.getLog(ValidationSample.class);

    private static JFrame frame;

    private static JLabel infoLabel;

    private static JTextArea infoArea;

    private static JComponent infoAreaPane;

    private static Domain domain;

    private static DomainModel model;

    private static JPanel panel;

    private static ValidationResultModel resultModel = new DefaultValidationResultModel();

    public static void main(String[] args) {
        domain = new Domain();
        model = new DomainModel(domain);

        JFormattedTextField code = BasicComponentFactory.createFormattedTextField(model.getModel("code"), NumberFormat
                .getInstance());
        JTextField name = BasicComponentFactory.createTextField(model.getModel("name"), false);

        ValidationComponentUtils.setMandatory(code, true);
        ValidationComponentUtils.setMessageKey(code, "Domain.CodeKey1");
        ValidationComponentUtils.setMessageKeys(name, "Domain.NameKey1", "Domain.NameKey2");
        ValidationComponentUtils.setInputHint(code, "Must be a positive integer");
        ValidationComponentUtils.setInputHint(name, "Name must be [5-10] long");

        JButton save = new JButton();
        save.setAction(new SaveAction());
        save.setText("save");

        ButtonBarBuilder2 buttons = new ButtonBarBuilder2();
        buttons.setDefaultButtonBarGapBorder();
        buttons.addGlue();
        buttons.addButton(save);

        FormLayout layout = new FormLayout("right:pref, 4dlu, 160dlu");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.append(buildInfoAreaPane(), 3);
        builder.appendSeparator("Separator");
        builder.append("Code", code);
        builder.nextLine();
        builder.append("Name", name);
        builder.nextLine();
        builder.append(ValidationResultViewFactory.createReportIconAndTextPane(resultModel), 3);
        builder.nextLine();
        builder.append(ValidationResultViewFactory.createReportList(resultModel), 3);
        builder.nextLine();
        builder.append(buttons.getPanel(), 3);
        panel = builder.getPanel();

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Task Manager UI");
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setSize(400, 400);
        centerOnScreen(frame);
        frame.setVisible(true);
        updateLayout();

        resultModel.addPropertyChangeListener(ValidationResultModel.PROPERTYNAME_RESULT, new ValidationChangeHandler());
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(new FocusChangeHandler());
        updateMandatory();
    }

    private static void centerOnScreen(JFrame component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        int x = (int) ((screenSize.getWidth() - paneSize.getWidth()) / 2);
        int y = (int) ((screenSize.getHeight() - paneSize.getHeight()) * 0.45);
        component.setLocation(x, y);
    }

    private static void updateLayout() {
        panel.revalidate();
    }

    private static void updateMandatory() {
        ValidationComponentUtils.updateComponentTreeMandatoryBorder(panel);
        ValidationComponentUtils.updateComponentTreeMandatoryBackground(panel);
    }

    private static JComponent buildInfoAreaPane() {
        infoLabel = new JLabel(ValidationResultViewFactory.getInfoIcon());
        infoArea = new JTextArea(1, 38);
        infoArea.setEditable(false);
        infoArea.setOpaque(false);

        FormLayout layout = new FormLayout("pref, $lcgap, default", "pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.add(infoLabel, cc.xy(1, 1));
        builder.add(infoArea, cc.xy(3, 1));

        infoAreaPane = builder.getPanel();
        infoAreaPane.setVisible(false);
        return infoAreaPane;
    }

    private static class SaveAction extends AbstractAction {

        public void actionPerformed(ActionEvent ae) {
            log.trace("SaveAction.actionPerformed");
            ValidationResult result = new DomainValidator().validate(domain);
            // this in turn fires the property change listeners that update the error panels
            resultModel.setResult(result);
            updateLayout();
        }

    }

    private static class DomainValidator implements Validator<Domain> {
        public ValidationResult validate(Domain domain) {
            log.trace("DomainValidator.validate");
            //"Domain" is used as prefix for every error added
            PropertyValidationSupport support = new PropertyValidationSupport(domain, "Domain");

            if (ValidationUtils.isBlank(domain.getName())) support.addError("NameKey1", "is mandatory");
            else if (!ValidationUtils.hasBoundedLength(domain.getName(), 5, 10))
                support.addError("NameKey2", "length must be in [5, 10]");

            if (domain.getCode() <= 0) support.addError("CodeKey1", "must be positive");
            return support.getResult();
        }
    }

    private static class FocusChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            log.trace("FocusChangeHandler.propertyChange");
            String propertyName = evt.getPropertyName();
            if (!"permanentFocusOwner".equals(propertyName)) return;
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            String focusHint = focusOwner instanceof JComponent ? (String) ValidationComponentUtils.getInputHint((JComponent) focusOwner) : null;
            infoArea.setText(focusHint);
            infoAreaPane.setVisible(focusHint != null);
            updateLayout();
        }
    }

    private static void updateComponentTreeMandatoryAndSeverity(ValidationResult result) {
        log.trace("updateComponentTreeMandatoryAndSeverity");
        ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(panel);
        ValidationComponentUtils.updateComponentTreeSeverityBackground(panel, result);
    }

    private static class ValidationChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            log.trace("ValidationChangeHandler.propertyChange");
            // this assigns the error/mandatory backgrounds
            updateComponentTreeMandatoryAndSeverity((ValidationResult) evt.getNewValue());
        }
    }

    public static class DomainModel extends PresentationModel {
        public DomainModel(Domain bean) {
            super(bean);
        }
    }

    public static class Domain extends Model {
        // fails with int! java.lang.IllegalArgumentException: argument type mismatch
        private long code;

        private String name;

        public long getCode() {
            return code;
        }

        public void setCode(long code) {
            long old = getCode();
            this.code = code;
            firePropertyChange("code", old, code);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            String old = getName();
            this.name = name;
            firePropertyChange("code", old, code);
        }
    }
}

