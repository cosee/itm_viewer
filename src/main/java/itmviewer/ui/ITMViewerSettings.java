package itmviewer.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import itmviewer.state.ITMSettingsState;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class ITMViewerSettings implements Configurable {
    public static final String ITM_VIEWER_SETTINGS_NAME = "ITM Viewer Settings";
    private JLabel tclHost;
    private JTextField tclHostInput;
    private JTextField tclPortInput;
    private JLabel tclPort;
    private JSpinner errorChannel;
    private JSpinner debugChannel;
    private JSpinner infoChannel;
    private JSpinner warnChannel;
    private JPanel root;
    private JCheckBox debugChannelCheckBox;
    private JCheckBox infoChannelCheckBox;
    private JCheckBox warnChannelCheckBox;
    private JCheckBox errorChannelCheckBox;
    final SpinnerNumberModel error_model = new SpinnerNumberModel(3, 0, 31, 1);
    final SpinnerNumberModel warn_model = new SpinnerNumberModel(2, 0, 31, 1);
    final SpinnerNumberModel info_model = new SpinnerNumberModel(1, 0, 31, 1);
    final SpinnerNumberModel debug_model = new SpinnerNumberModel(0, 0, 31, 1);

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root = new JPanel();
        root.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1));
        root.setBorder(BorderFactory.createTitledBorder("ITM Viewer Settings"));
        tclHost = new JLabel();
        tclHost.setText("OpenOCD Tcl Host");
        root.add(tclHost, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tclHostInput = new JTextField();
        tclHostInput.setText("127.0.0.1");
        root.add(tclHostInput, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tclPort = new JLabel();
        tclPort.setText("OpenOcd Tcl Port");
        root.add(tclPort, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(114, 30), null, 0, false));
        tclPortInput = new JTextField();
        tclPortInput.setText("6666");
        root.add(tclPortInput, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 30), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("ITM Error Channel");
        root.add(label1, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorChannel = new JSpinner();
        root.add(errorChannel, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("ITM Debug Channel");
        root.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        debugChannel = new JSpinner();
        root.add(debugChannel, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("ITM Info Channel");
        root.add(label3, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        infoChannel = new JSpinner();
        root.add(infoChannel, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("ITM Warn Channel");
        root.add(label4, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        warnChannel = new JSpinner();
        root.add(warnChannel, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        root.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title)
    String getDisplayName() {
        return ITM_VIEWER_SETTINGS_NAME;
    }

    @Override
    public @Nullable
    JComponent createComponent() {
        int debugLogLevelPort = ITMSettingsState.getLogLevelPort(ITMSettingsState.LoggingLevel.DEBUG);
        int infoLogLevelPort = ITMSettingsState.getLogLevelPort(ITMSettingsState.LoggingLevel.INFO);
        int warnLogLevelPort = ITMSettingsState.getLogLevelPort(ITMSettingsState.LoggingLevel.WARN);
        int errorLogLevelPort = ITMSettingsState.getLogLevelPort(ITMSettingsState.LoggingLevel.ERROR);

        debug_model.setValue(debugLogLevelPort);
        info_model.setValue(infoLogLevelPort);
        warn_model.setValue(warnLogLevelPort);
        error_model.setValue(errorLogLevelPort);

        debugChannelCheckBox.setSelected(ITMSettingsState.getLogLevelEnabled(ITMSettingsState.LoggingLevel.DEBUG));
        infoChannelCheckBox.setSelected(ITMSettingsState.getLogLevelEnabled(ITMSettingsState.LoggingLevel.INFO));
        warnChannelCheckBox.setSelected(ITMSettingsState.getLogLevelEnabled(ITMSettingsState.LoggingLevel.WARN));
        errorChannelCheckBox.setSelected(ITMSettingsState.getLogLevelEnabled(ITMSettingsState.LoggingLevel.ERROR));

        errorChannel.setModel(error_model);
        warnChannel.setModel(warn_model);
        infoChannel.setModel(info_model);
        debugChannel.setModel(debug_model);

        String savedHost = ITMSettingsState.getTclHost();
        if (savedHost != null) {
            tclHostInput.setText(savedHost);
        }
        String savedPort = ITMSettingsState.getTclPort();
        if (savedPort != null) {
            tclPortInput.setText(savedPort);
        }
        return root;
    }

    @Override
    public boolean isModified() {
        //TODO: dont be lazy and check if something checked
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        String tclHostVal = tclHostInput.getText();
        ITMSettingsState.setTclHost(tclHostVal);
        Integer debugPort = (Integer) debug_model.getValue();
        Integer infoPort = (Integer) info_model.getValue();
        Integer warnPort = (Integer) warn_model.getValue();
        Integer errorPort = (Integer) error_model.getValue();

        Set<Integer> channels = new LinkedHashSet<>();
        channels.add(errorPort);
        channels.add(warnPort);
        channels.add(infoPort);
        channels.add(debugPort);

        if (channels.size() != 4) {
            throw new ConfigurationException("Please provide different ports for each log level");
        }
        if (tclHostInput.getText() == null || tclHostInput.getText().length() == 0) {
            throw new ConfigurationException("Host is invalid");
        }
        if (tclPortInput.getText() == null
                || tclPortInput.getText().length() == 0
                || !NumberUtils.isNumber(tclPortInput.getText())) {
            throw new ConfigurationException("Port is invalid");
        }

        ITMSettingsState.setChannelPort(ITMSettingsState.LoggingLevel.DEBUG, debugPort);
        ITMSettingsState.setChannelPort(ITMSettingsState.LoggingLevel.INFO, infoPort);
        ITMSettingsState.setChannelPort(ITMSettingsState.LoggingLevel.WARN, warnPort);
        ITMSettingsState.setChannelPort(ITMSettingsState.LoggingLevel.ERROR, errorPort);

        ITMSettingsState.setLogLevelEnabled(ITMSettingsState.LoggingLevel.DEBUG, debugChannelCheckBox.isSelected());
        ITMSettingsState.setLogLevelEnabled(ITMSettingsState.LoggingLevel.INFO, infoChannelCheckBox.isSelected());
        ITMSettingsState.setLogLevelEnabled(ITMSettingsState.LoggingLevel.WARN, warnChannelCheckBox.isSelected());
        ITMSettingsState.setLogLevelEnabled(ITMSettingsState.LoggingLevel.ERROR, errorChannelCheckBox.isSelected());

        ITMSettingsState.setTclHost(tclHostInput.getText());
        ITMSettingsState.setTclPort(tclPortInput.getText());


    }
}
