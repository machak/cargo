/*
 * ========================================================================
 *
 * Copyright 2004-2005 Vincent Massol.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ========================================================================
 */
package org.codehaus.cargo.intellijidea;

import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.intellij.j2ee.run.configuration.CommonModel;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import org.codehaus.cargo.container.ContainerException;
import org.codehaus.cargo.container.configuration.Configuration;
import org.codehaus.cargo.container.configuration.ConfigurationCapability;
import org.codehaus.cargo.container.property.GeneralPropertySet;
import org.codehaus.cargo.container.property.ServletPropertySet;
import org.codehaus.cargo.container.property.User;
import org.codehaus.cargo.generic.configuration.ConfigurationType;
import org.codehaus.cargo.generic.configuration.DefaultConfigurationFactory;

/**
 * Editor for persistent configuration data ({@link CargoContainerModel}) for an
 * application server. E.g. home and configuration directories.
 *
 * @version $Id: CargoTask.jagva 391 2005-07-04 13:07:42Z vmassol $
 * @see CargoApplicationServerHelper#createConfigurable()
 */
public class CargoConfigurationEditor extends SettingsEditor {
    /**
     * Panel that contains configuration controls..
     */
    private JPanel panel;
    /**
     * ConfigurationDir textfield.
     */
    private TextFieldWithBrowseButton configDir;

    /**
     * General properties panel.
     */
    private JPanel generalPanel;
    /**
     * Hostname textfield.
     */
    private JTextField hostnameTextField;
    /**
     * Hostname label.
     */
    private JLabel hostnameLabel;
    /**
     * JVMArgs TextField.
     */
    private JTextField jvmArgsTextField;
    /**
     * JVM args label.
     */
    private JLabel jvmArgsLabel;
    /**
     * Logging ComboBox.
     */
    private JComboBox loggingComboBox;
    /**
     * Logging Label.
     */
    private JLabel loggingLabel;

    /**
     * Servlet properties panel.
     */
    private JPanel servletPanel;
    /**
     * Users TextField.
     */
    private JTextField usersTextField;
    /**
     * Users Label.
     */
    private JLabel usersLabel;
    /**
     * Port TextField.
     */
    private JTextField portTextField;
    /**
     * Port Label.
     */
    private JLabel portLabel;

    /**
     * Constructor.
     *
     * @param cargoApplicationServerHelper cargoApplicationServerHelper
     */
    public CargoConfigurationEditor(
            final CargoApplicationServerHelper cargoApplicationServerHelper) {
        initChooser(configDir, "Configuration Directory",
                "Select configuration directory");
        initLoggingComboBox();

        final String containerId = cargoApplicationServerHelper.getCargoAppServerIntegration()
                .getContainerId();

        final Configuration configuration = new DefaultConfigurationFactory()
                .createConfiguration(containerId, ConfigurationType.STANDALONE);
        final ConfigurationCapability configurationCapability = configuration.getCapability();

        initServletPanel(configurationCapability);
        initGeneralPanel(configurationCapability);
    }

    /**
     * Initializes the general property panel.
     *
     * @param configurationCapability capabilities
     */
    private void initGeneralPanel(final ConfigurationCapability configurationCapability) {
        generalPanel.setVisible(configurationCapability.supportsProperty(
                GeneralPropertySet.HOSTNAME)
                || configurationCapability.supportsProperty(GeneralPropertySet.JVMARGS)
                || configurationCapability.supportsProperty(GeneralPropertySet.LOGGING));
        loggingComboBox.setVisible(configurationCapability.supportsProperty(
                GeneralPropertySet.LOGGING));
        loggingLabel.setVisible(configurationCapability.supportsProperty(
                GeneralPropertySet.LOGGING));
        hostnameTextField.setVisible(configurationCapability.supportsProperty(
                GeneralPropertySet.HOSTNAME));
        hostnameLabel.setVisible(configurationCapability.supportsProperty(
                GeneralPropertySet.HOSTNAME));
        jvmArgsTextField.setVisible(configurationCapability.supportsProperty(
                GeneralPropertySet.JVMARGS));
        jvmArgsLabel.setVisible(configurationCapability.supportsProperty(
                GeneralPropertySet.JVMARGS));
    }

    /**
     * Initializes the servlet specific property panel.
     *
     * @param configurationCapability capabilities
     */
    private void initServletPanel(final ConfigurationCapability configurationCapability) {
        servletPanel.setVisible(configurationCapability.supportsProperty(ServletPropertySet.PORT)
                || configurationCapability.supportsProperty(ServletPropertySet.USERS));
        usersTextField.setVisible(configurationCapability.supportsProperty(
                ServletPropertySet.USERS));
        usersLabel.setVisible(configurationCapability.supportsProperty(ServletPropertySet.USERS));
        portTextField.setVisible(
                configurationCapability.supportsProperty(ServletPropertySet.PORT));
        portLabel.setVisible(configurationCapability.supportsProperty(ServletPropertySet.PORT));
    }

    /**
     * Init the chooser.
     *
     * @param field       textField
     * @param title       title
     * @param description description
     */
    private void initChooser(TextFieldWithBrowseButton field, String title, String description) {
        field.getTextField().setEditable(true);
        field.addBrowseFolderListener(title,
                description,
                null,
                new FileChooserDescriptor(false, true, false, false, false, false));
    }

    /**
     * Init the logging combobox.
     */
    private void initLoggingComboBox() {
        loggingComboBox.setModel(new DefaultComboBoxModel(new String[]{"low", "medium", "high"}));
    }

    /**
     * Resets data. Transfers data from the passed object to the editor.
     *
     * @param data data
     */
    public void resetEditorFrom(Object data) {
        final CommonModel commonModel = (CommonModel) data;
        final CargoConfigurationModel cargoConfigurationModel
                = (CargoConfigurationModel) commonModel.getServerModel();

        configDir.setText(cargoConfigurationModel.getConfigDir());

        portTextField.setText(cargoConfigurationModel.getProperty(ServletPropertySet.PORT));
        usersTextField.setText(cargoConfigurationModel.getProperty(ServletPropertySet.USERS));

        hostnameTextField.setText(
                cargoConfigurationModel.getProperty(GeneralPropertySet.HOSTNAME));
        jvmArgsTextField.setText(cargoConfigurationModel.getProperty(GeneralPropertySet.JVMARGS));

        final String level = cargoConfigurationModel.getProperty(GeneralPropertySet.LOGGING);
        if (level != null) {
            loggingComboBox.setSelectedItem(level);
        } else {
            loggingComboBox.setSelectedItem("medium");
        }
    }

    /**
     * Transfers editor data to passed object.
     *
     * @param data persistent data
     * @throws com.intellij.openapi.options.ConfigurationException
     *          if something does not work
     */
    public void applyEditorTo(Object data) throws ConfigurationException {
        final CommonModel commonModel = (CommonModel) data;
        final CargoConfigurationModel cargoConfigurationModel
                = (CargoConfigurationModel) commonModel.getServerModel();

        checkConfigDir(configDir.getText());
        cargoConfigurationModel.setConfigDir(new File(configDir.getText()).getAbsoluteFile()
                .getAbsolutePath());

        checkPort(portTextField.getText());
        checkUsers(usersTextField.getText());

        cargoConfigurationModel.setProperty(ServletPropertySet.PORT, portTextField.getText());
        cargoConfigurationModel.setProperty(ServletPropertySet.USERS, usersTextField.getText());


        cargoConfigurationModel.setProperty(GeneralPropertySet.HOSTNAME,
                hostnameTextField.getText());
        cargoConfigurationModel.setProperty(GeneralPropertySet.JVMARGS,
                jvmArgsTextField.getText());
        cargoConfigurationModel.setProperty(GeneralPropertySet.LOGGING,
                (String) loggingComboBox.getSelectedItem());
    }

    /**
     * Checks validity of entered configuration directory.
     *
     * @param configDirString config directory
     * @throws ConfigurationException if the directory is not a valid directory or cannot be
     *                                created.
     */
    private void checkConfigDir(final String configDirString)
            throws ConfigurationException {
        if (configDirString == null || configDirString.length() == 0) {
            throw new ConfigurationException("You have to set a valid configuration directory.");
        }
        final File configDirFile = new File(configDirString).getAbsoluteFile();
        if ((!configDirFile.exists()) && (!configDirFile.mkdirs())) {
            throw new ConfigurationException("Cannot create configuration directory.");
        }
    }

    /**
     * Checks validity of the entered users.
     *
     * @param users users
     * @throws com.intellij.openapi.options.ConfigurationException
     *          Exception thrown,
     *          if the users String is not correctly formatted.
     */
    private void checkUsers(final String users) throws ConfigurationException {
        if (users != null && users.length() > 0) {
            try {
                User.parseUsers(users);
            } catch (ContainerException e) {
                throw new ConfigurationException("<html><p>Users '" + users
                        + "' has an invalid format.</p>"
                        + "<p>Correct format:</p>"
                        + "<p><code>name1:pwd1:role11,...,role1N|name2:pwd2:role21,"
                        + "...,role2N|...</code></p></html>");
            }
        }
    }

    /**
     * Checks whether the entered port is valid.
     *
     * @param port port to check
     * @throws com.intellij.openapi.options.ConfigurationException
     *          Exception that is thrown should
     *          the port not be valid
     */
    private void checkPort(final String port) throws ConfigurationException {
        if (port != null && port.length() > 0) {
            try {
                final int portInt = Integer.parseInt(port);
                if (portInt < 0 || portInt > 64 * 1024) {
                    throw new ConfigurationException("Port has to be a positive integer less than "
                            + (64 * 1024 + 1) + ".");
                }
            } catch (NumberFormatException nfe) {
                throw new ConfigurationException("Port '" + port + "' is invalid.");
            }
        }
    }

    /**
     * Creates the editor.
     *
     * @return editor panel
     */
    public JComponent createEditor() {
        return panel;
    }

    /**
     * Clears used resources.
     */
    public void disposeEditor() {
    }

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
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Configuration directory:");
        label1.setToolTipText(
                "Directory to which the container configuration will be written. Must be writable.");
        panel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        configDir = new TextFieldWithBrowseButton();
        panel.add(configDir, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_NORTH,
                GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null));
        servletPanel = new JPanel();
        servletPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(servletPanel, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                null, null));
        servletPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Servlet properties"));
        portLabel = new JLabel();
        portLabel.setText("Port:");
        portLabel.setToolTipText("Servlet container port");
        servletPanel.add(portLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        usersLabel = new JLabel();
        usersLabel.setText("Users:");
        usersLabel.setToolTipText("name1:pwd1:role11,...,role1N|name2:pwd2:role21,...,role2N|...");
        servletPanel.add(usersLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        portTextField = new JTextField();
        servletPanel.add(portTextField, new GridConstraints(0, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null,
                new Dimension(150, -1), null));
        usersTextField = new JTextField();
        servletPanel.add(usersTextField, new GridConstraints(1, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null,
                new Dimension(150, -1), null));
        generalPanel = new JPanel();
        generalPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(generalPanel, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                null, null));
        generalPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "General properties"));
        hostnameLabel = new JLabel();
        hostnameLabel.setText("Hostname:");
        hostnameLabel.setToolTipText("Container hostname");
        generalPanel.add(hostnameLabel, new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        hostnameTextField = new JTextField();
        hostnameTextField.setText("localhost");
        generalPanel.add(hostnameTextField, new GridConstraints(0, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null,
                new Dimension(150, -1), null));
        jvmArgsLabel = new JLabel();
        jvmArgsLabel.setText("JVM arguments:");
        jvmArgsLabel.setToolTipText("JVM arguments that will be passed to the container");
        generalPanel.add(jvmArgsLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        jvmArgsTextField = new JTextField();
        generalPanel.add(jvmArgsTextField, new GridConstraints(1, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null,
                new Dimension(150, -1), null));
        loggingLabel = new JLabel();
        loggingLabel.setText("Logging:");
        generalPanel.add(loggingLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        loggingComboBox = new JComboBox();
        generalPanel.add(loggingComboBox, new GridConstraints(2, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null,
                null));
        final Spacer spacer1 = new Spacer();
        panel.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null,
                null));
    }
}
