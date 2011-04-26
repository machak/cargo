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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.j2ee.deployment.DeploymentModel;
import com.intellij.j2ee.deployment.DeploymentProvider;
import com.intellij.j2ee.j2eeDom.J2EEModuleProperties;
import com.intellij.j2ee.run.configuration.CommonModel;
import com.intellij.j2ee.run.configuration.ServerModel;
import com.intellij.j2ee.run.execution.DefaultOutputProcessor;
import com.intellij.j2ee.run.execution.OutputProcessor;
import com.intellij.j2ee.serverInstances.DefaultServerInstance;
import com.intellij.j2ee.serverInstances.J2EEServerInstance;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.WriteExternalException;

import org.codehaus.cargo.container.configuration.Configuration;
import org.codehaus.cargo.container.property.ServletPropertySet;
import org.codehaus.cargo.generic.configuration.ConfigurationType;
import org.codehaus.cargo.generic.configuration.DefaultConfigurationFactory;
import org.jdom.Element;

/**
 * Extended {@link ServerModel}.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public class CargoConfigurationModel implements ServerModel {

    /**
     * Default port.
     */
    private static final int DEFAULT_PORT = 8080;
    /**
     * CommonModel
     */
    private CommonModel commonModel;
    /**
     * Deployment provider.
     */
    private CargoDeploymentProvider cargoDeploymentProvider = new CargoDeploymentProvider();
    /**
     * Config dir.
     */
    private String configDir;
    /**
     * Property map.
     */
    private Map properties;

    /**
     * No arg constructor.
     */
    public CargoConfigurationModel() {
        this.properties = new HashMap();
    }

    /**
     * Returns the container id.
     *
     * @return container id
     */
    public String getContainerId() {
        return ((AbstractCargoAppServerIntegration) commonModel.getIntegration())
                .getContainerId();
    }

    /**
     * Get default port.
     *
     * @return default port.
     */
    public int getDefaultPort() {
        return DEFAULT_PORT;
    }

    /**
     * Sets the common model.
     * When is this called by IntelliJIdea?
     *
     * @param commonModel commonModel
     */
    public void setCommonModel(CommonModel commonModel) {
        this.commonModel = commonModel;
    }

    /**
     * Factory method for creating a {@link CargoServerInstance}.
     *
     * @return new {@link CargoServerInstance}
     * @throws ExecutionException should something go wrong
     */
    public J2EEServerInstance createServerInstance() throws ExecutionException {
        J2EEServerInstance serverInstance = commonModel.isLocal()
                ? new CargoServerInstance(commonModel)
                : new DefaultServerInstance(commonModel);
        return serverInstance;
    }

    /**
     * Get a settings editor.
     *
     * @return null if remote, CargoConfigurationEditor otherwise
     */
    public SettingsEditor getEditor() {
        if (!commonModel.isLocal()) {
            return null;
        }
        final AbstractCargoAppServerIntegration cargoAppServerIntegration
                = ((AbstractCargoAppServerIntegration) commonModel.getIntegration());
        return new CargoConfigurationEditor((CargoApplicationServerHelper)
                cargoAppServerIntegration.getApplicationServerHelper());
    }

    /**
     * Get deployment provider.
     *
     * @return deployment provider or null, if there is none.
     */
    public DeploymentProvider getDeploymentProvider() {
        return commonModel.isLocal()
                ? cargoDeploymentProvider
                : null;
    }

    /**
     * Local port.
     *
     * @return local port
     */
    public int getLocalPort() {
        // what's the difference to commonModel.getPort() ??
        return Integer.parseInt(getCargoConfiguration().getPropertyValue(ServletPropertySet.PORT));
    }

    /**
     * Get addresses to check.
     *
     * @return addresses to check
     */
    public List getAddressesToCheck() {
        List result = new ArrayList();
        result.add(new Pair((commonModel.getHost()), new Integer(commonModel.getPort())));
        return result;
    }

    /**
     * Returns the default URL for a browser
     *
     * @return default url
     */
    public String getDefaultUrlForBrowser() {
        StringBuilder result = new StringBuilder();
        result.append("http://");
        result.append(commonModel.getHost());
        result.append(':');
        result.append(String.valueOf(commonModel.getPort()));
        String defaultContext = getDefaultContext();
        if (defaultContext != null && !defaultContext.equals("/")) {
            if (!defaultContext.startsWith("/")) {
                result.append("/");
            }
            result.append(defaultContext);
        }
        result.append("/");
        return result.toString();
    }

    /**
     * Get default context.
     *
     * @return default context
     */
    private String getDefaultContext() {
        Module[] modules = (Module[]) ModuleManager.getInstance(
                (Project) commonModel.getProject()).getModules();
        for (int i = 0; i < modules.length; i++) {
            Module module = modules[i];
            if (module.getModuleType().equals(ModuleType.WEB)) {
                J2EEModuleProperties moduleProperties = (J2EEModuleProperties)
                        J2EEModuleProperties.getInstance(module);
                if (moduleProperties.getAppServerIntegration()
                        instanceof AbstractCargoAppServerIntegration) {
                    DeploymentModel deploymentModel = commonModel.getDeploymentModel(
                            moduleProperties);
                    if (deploymentModel != null) {
                        return ((CargoModuleDeploymentModel) deploymentModel).getContextPath();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Create {@link OutputProcessor}
     *
     * @param j2EEOSProcessHandlerWrapper processhandler
     * @param serverInstance              server instance
     * @return output processor
     */
    public OutputProcessor createOutputProcessor(ProcessHandler j2EEOSProcessHandlerWrapper,
                                                 J2EEServerInstance serverInstance) {
        return new DefaultOutputProcessor(j2EEOSProcessHandlerWrapper);
    }

    /**
     * Checks configuration
     *
     * @throws RuntimeConfigurationException if the configuration is faulty
     */
    public void checkConfiguration() throws RuntimeConfigurationException {
    }

    /**
     * Get project.
     *
     * @return project
     */
    public Project getProject() {
        return commonModel.getProject();
    }

    /**
     * Indicates whether this model describes a local server.
     *
     * @return true or false
     */
    public boolean isLocal() {
        return commonModel.isLocal();
    }

    /**
     * Name of the model.
     *
     * @return name
     */
    public String getName() {
        return commonModel.getName();
    }

    /**
     * Get Cargo configuration.
     *
     * @return Cargo configuration
     */
    public Configuration getCargoConfiguration() {
        final Configuration cargoConfiguration = new DefaultConfigurationFactory()
                .createConfiguration(getContainerId(), ConfigurationType.STANDALONE,
                        new File(configDir));
        for (Iterator propertyNames = this.getPropertyNames(); propertyNames.hasNext(); ) {
            final String name = (String) propertyNames.next();
            cargoConfiguration.setProperty(name, this.getProperty(name));
        }
        return cargoConfiguration;
    }

    /**
     * Get config dir.
     *
     * @return config dir
     */
    public String getConfigDir() {
        return configDir;
    }

    /**
     * Sets config dir.
     *
     * @param configDir configuration dir
     */
    public void setConfigDir(String configDir) {
        this.configDir = configDir;
    }

    /**
     * Sets a property.
     *
     * @param key   key
     * @param value value
     */
    public void setProperty(String key, String value) {
        if (value == null || value.length() == 0) {
            properties.remove(key);
        } else {
            properties.put(key, value);
        }
    }

    /**
     * Gets a property.
     *
     * @param key key
     * @return the value or null
     */
    public String getProperty(String key) {
        return (String) properties.get(key);
    }

    /**
     * Iterator over the property names.
     *
     * @return iterator over property names
     */
    public Iterator getPropertyNames() {
        return properties.keySet().iterator();
    }

    /**
     * Reads from persistent storage.
     *
     * @param parentNode parent node
     * @throws InvalidDataException if the data is not valid
     */
    public void readExternal(Element parentNode) throws InvalidDataException {
        configDir = getElement("configDir", parentNode);

        for (Iterator i = parentNode.getChildren("property").iterator(); i.hasNext(); ) {
            final Element element = (Element) i.next();
            final String name = ((String) element.getAttributeValue("name"));
            if (name == null) {
                throw new InvalidDataException();
            }
            final String value = ((String) element.getAttributeValue("value"));
            setProperty(name, value);
        }
    }

    /**
     * Helper method to get an element.
     *
     * @param name       name
     * @param parentNode parent node
     * @return element value (text)
     */
    private String getElement(final String name, Element parentNode) {
        String res = null;
        Element homeDirElement = parentNode.getChild(name);
        if (homeDirElement != null) {
            res = homeDirElement.getText();
        }
        return res;
    }

    /**
     * Writes to persistent storage.
     *
     * @param parentNode parent node
     * @throws WriteExternalException if the data cannot be written
     */
    public void writeExternal(Element parentNode) throws WriteExternalException {
        addElement("configDir", configDir, parentNode);

        for (Iterator iterator = properties.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry entry = (Map.Entry) iterator.next();
            final Element element = new Element("property");
            parentNode.addContent(element);
            element.setAttribute("name", (String) entry.getKey());
            element.setAttribute("value", (String) entry.getValue());
        }

    }

    /**
     * Helpermethod to add an Element.
     *
     * @param name       name
     * @param value      value
     * @param parentNode parent node
     */
    private void addElement(final String name, final String value, Element parentNode) {
        Element homeDirElement = new Element(name);
        homeDirElement.setText(value);
        parentNode.addContent(homeDirElement);
    }

    /**
     * Clone.
     *
     * @return a clone
     * @throws CloneNotSupportedException if cloning is not support
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
