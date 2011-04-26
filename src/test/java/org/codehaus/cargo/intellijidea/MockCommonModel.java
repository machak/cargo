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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.ConfigurationInfoProvider;
import com.intellij.execution.configurations.ConfigurationPerRunnerSettings;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.JavaProgramRunner;
import com.intellij.execution.runners.RunnerInfo;
import com.intellij.j2ee.appServerIntegrations.AppServerIntegration;
import com.intellij.j2ee.appServerIntegrations.ApplicationServer;
import com.intellij.j2ee.deployment.DeploymentModel;
import com.intellij.j2ee.j2eeDom.J2EEModuleProperties;
import com.intellij.j2ee.run.configuration.CommonModel;
import com.intellij.j2ee.run.configuration.ServerModel;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;

import org.jdom.Element;

/**
 * Mock CommonModel.
 * <p/>
 *
 * @version $Id: CargoTaskTest.java 414 2005-07-20 09:34:44Z vmassol $
 */
public class MockCommonModel extends RunConfigurationBase implements CommonModel {
    private ServerModel serverModel;
    private AppServerIntegration appServerIntegration;
    private boolean local;

    public MockCommonModel() {
        super(null, null, null);
    }

    public DeploymentModel getDeploymentModel(J2EEModuleProperties moduleProperties) {
        return null;
    }

    public AppServerIntegration getIntegration() {
        return appServerIntegration;
    }

    public void setIntegration(AppServerIntegration integration) {
        this.appServerIntegration = integration;
    }

    public boolean isLocal() {
        return this.local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public ApplicationServer getApplicationServer() {
        return null;
    }

    public String getHost() {
        return null;
    }

    public int getPort() {
        return 0;
    }

    public void setServerModel(ServerModel serverModel) {
        this.serverModel = serverModel;
    }

    public ServerModel getServerModel() {
        return this.serverModel;
    }

    public void initialize() {
    }

    public SettingsEditor getConfigurationEditor() {
        return null;
    }

    public ConfigurationType getType() {
        return null;
    }

    public JDOMExternalizable createRunnerSettings(ConfigurationInfoProvider provider) {
        return null;
    }

    public SettingsEditor getRunnerSettingsEditor(JavaProgramRunner runner) {
        return null;
    }


    public RunProfileState getState(DataContext context, RunnerInfo runnerInfo,
                                    RunnerSettings runnerSettings,
                                    ConfigurationPerRunnerSettings configurationSettings)
            throws ExecutionException {
        return null;
    }

    public void checkConfiguration() throws RuntimeConfigurationException {
    }

    // return modules to compile before run. Null or empty list to make project
    public Module[] getModules() {
        return new Module[0];
    }

    public void readExternal(Element element) throws InvalidDataException {
    }

    public void writeExternal(Element element) throws WriteExternalException {
    }


}
