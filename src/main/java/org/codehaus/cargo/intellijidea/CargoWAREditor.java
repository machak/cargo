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

import java.awt.Insets;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intellij.j2ee.j2eeDom.J2EEModuleProperties;
import com.intellij.j2ee.run.configuration.CommonModel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.Factory;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

/**
 * Editor for additional deployment configurations.
 * Specifically this is the editor for the context path
 * a war is mapped to.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 * @see CargoDeploymentProvider#createAdditionalDeploymentSettingsEditor
 * @see CargoModuleDeploymentModel
 */
public class CargoWAREditor extends SettingsEditor {
    /**
     * Panel.
     */
    private JPanel panel;
    /**
     * Context path.
     */
    private JComboBox contextPath;

    /**
     * Constructor.
     *
     * @param configuration    the commonModel
     * @param moduleProperties J2EEModuleProperties
     */
    public CargoWAREditor(final CommonModel configuration,
                          final J2EEModuleProperties moduleProperties) {
        super(new Factory() {
            public Object create() {
                return new CargoModuleDeploymentModel(configuration, moduleProperties);
            }
        });
        initContextPath(moduleProperties);
    }

    /**
     * Initialize the context path combobox with probable paths.
     *
     * @param moduleProperties properties
     */
    private void initContextPath(final J2EEModuleProperties moduleProperties) {
        addContextPath("/");
        addContextPath("/" + moduleProperties.getModule().getName());
        addContextPath("/" + moduleProperties.getModule().getProject().getName());
        addContextPath("/" + moduleProperties.getModule().getProject().getName() + "/"
                + moduleProperties.getModule().getName());
    }

    /**
     * Resets the form using the passed object.
     *
     * @param settings {@link CargoModuleDeploymentModel}
     */
    public void resetEditorFrom(Object settings) {
        final CargoModuleDeploymentModel cargoModuleDeploymentModel
                = (CargoModuleDeploymentModel) settings;
        setSelectedContextPath(cargoModuleDeploymentModel.getContextPath(), true);
    }

    /**
     * Sets the editor values to the passed object.
     *
     * @param settings {@link CargoModuleDeploymentModel}
     * @throws ConfigurationException if the configuration is faulty
     */
    public void applyEditorTo(Object settings) throws ConfigurationException {
        ((CargoModuleDeploymentModel) settings).setContextPath(getSelectedContextPath());
    }

    /**
     * Creates the acctual editor panel.
     *
     * @return editor
     */
    public JComponent createEditor() {
        return panel;
    }

    /**
     * Cleans up.
     */
    public void disposeEditor() {
    }

    /**
     * Selected context path.
     *
     * @return selected context path
     */
    private String getSelectedContextPath() {
        final String item = (String) (((ComboBoxEditor) contextPath.getEditor()).getItem());
        return item;
    }

    /**
     * Adds an item to the combobox, if not found.
     *
     * @param contextPath contextPath
     */
    private void addContextPath(String contextPath) {
        final int itemCount = this.contextPath.getItemCount();
        for (int idx = 0; idx < itemCount; idx++) {
            final String path = (String) ((Object) this.contextPath.getItemAt(idx));
            if (contextPath.equals(path)) {
                return;
            }
        }
        this.contextPath.addItem(contextPath);
    }

    /**
     * Set selected context path.
     *
     * @param contextPath   contextPath
     * @param addIfNotFound flag
     */
    private void setSelectedContextPath(String contextPath, boolean addIfNotFound) {
        int itemCount = this.contextPath.getItemCount();
        for (int idx = 0; idx < itemCount; idx++) {
            String path = (String) ((Object) this.contextPath.getItemAt(idx));
            if (contextPath.equals(path)) {
                this.contextPath.setSelectedIndex(idx);
                return;
            }
        }
        if (addIfNotFound) {
            this.contextPath.addItem(contextPath);
            this.contextPath.setSelectedItem(contextPath);
        }
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
        panel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                null, null));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null,
                null));
        final JLabel label1 = new JLabel();
        label1.setText("Application context:");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        contextPath = new JComboBox();
        contextPath.setEditable(true);
        panel1.add(contextPath, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    }
}
