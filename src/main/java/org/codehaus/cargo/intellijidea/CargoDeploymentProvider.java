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

import com.intellij.j2ee.deployment.DeploymentManager;
import com.intellij.j2ee.deployment.DeploymentMethod;
import com.intellij.j2ee.deployment.DeploymentModel;
import com.intellij.j2ee.deployment.DeploymentProvider;
import com.intellij.j2ee.deployment.DeploymentStatus;
import com.intellij.j2ee.j2eeDom.J2EEModuleProperties;
import com.intellij.j2ee.run.configuration.CommonModel;
import com.intellij.j2ee.serverInstances.J2EEServerInstance;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * CargoDeploymentProvider.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public class CargoDeploymentProvider implements DeploymentProvider {
    /**
     * Supported deployment method.
     */
    private static final DeploymentMethod[] SUPPORTED_DEPLOYMENT_METHODS
            = new DeploymentMethod[]{new DeploymentMethod("Cargo", true, false)};

    /**
     * Deploy configured applications (wars/ears).
     *
     * @param project         IntelliJIdea project
     * @param instance        server instance ({@link CargoServerInstance})
     * @param deploymentModel deploymentModel
     */
    public void doDeploy(Project project, J2EEServerInstance instance,
                         DeploymentModel deploymentModel) {
        setDeploymentStatus(instance, deploymentModel, DeploymentStatus.DEPLOYED);
    }

    /**
     * Signal deployment status to the {@link DeploymentManager}.
     *
     * @param instance server instance ({@link CargoServerInstance})
     * @param model    deployment model
     * @param status   deployment status
     */
    private void setDeploymentStatus(J2EEServerInstance instance, DeploymentModel model,
                                     DeploymentStatus status) {
        CommonModel configuration = ((CargoServerInstance) instance).getCommonModel();
        CargoConfigurationModel cargoConfigurationModel
                = (CargoConfigurationModel) configuration.getServerModel();
        J2EEModuleProperties item = (J2EEModuleProperties) model.getModuleProperties();
        DeploymentManager.getInstance(cargoConfigurationModel.getProject())
                .setDeploymentStatus(item, status, configuration, instance);
    }

    /**
     * Creates a new deployment model - {@link CargoModuleDeploymentModel}.
     *
     * @param commonModel          model
     * @param j2eeModuleProperties module properties
     * @return a new deployment model
     */
    public DeploymentModel createNewDeploymentModel(CommonModel commonModel,
                                                    J2EEModuleProperties j2eeModuleProperties) {
        return new CargoModuleDeploymentModel(commonModel, j2eeModuleProperties);
    }

    /**
     * Creates an editor for custom depoyment settings.
     *
     * @param commonModel      model
     * @param moduleProperties module properties
     * @return {@link CargoWAREditor}
     */
    public SettingsEditor createAdditionalDeploymentSettingsEditor(CommonModel commonModel,
                                                                   J2EEModuleProperties
                                                                           moduleProperties) {
        if (ModuleType.WEB.equals(moduleProperties.getModule().getModuleType())) {
            return new CargoWAREditor(commonModel, moduleProperties);
        }
        return null;
    }

    /**
     * Start undeploying.
     *
     * @param activeInstance  server instance
     * @param deploymentModel deployment model
     */
    public void startUndeploy(final J2EEServerInstance activeInstance,
                              final DeploymentModel deploymentModel) {
        // currently we do not support this, so we don't change the status
        final Module module = deploymentModel.getModuleProperties().getModule();
        Messages.showErrorDialog("Undeploy is not supported, yet.", "Error undeploying module '"
                + module.getName() + "'");
        //setDeploymentStatus(activeInstance, model, DeploymentStatus.NOT_DEPLOYED);
    }

    /**
     * Update deployment status
     *
     * @param instance server instance
     * @param model    deployment model
     */
    public void updateDeploymentStatus(J2EEServerInstance instance, DeploymentModel model) {
        // do nothing.
    }

    /**
     * Get help id.
     *
     * @return help id
     */
    public String getHelpId() {
        // this is a better than nothing help reference
        return "webLogic.deployment";
    }

    /**
     * Get available deployment methods.
     *
     * @return deployment methods.
     */
    public DeploymentMethod[] getAvailableMethods() {
        return SUPPORTED_DEPLOYMENT_METHODS;
    }

}
