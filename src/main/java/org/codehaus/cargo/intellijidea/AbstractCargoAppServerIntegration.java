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

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import com.intellij.j2ee.appServerIntegrations.AppServerIntegration;
import com.intellij.j2ee.appServerIntegrations.ApplicationServerHelper;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.util.IconLoader;

import org.codehaus.cargo.container.Container;
import org.codehaus.cargo.container.ContainerCapability;
import org.codehaus.cargo.container.deployable.DeployableType;
import org.codehaus.cargo.generic.DefaultContainerFactory;


/**
 * App server integration component that is registered with IntelliJIdea.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public abstract class AbstractCargoAppServerIntegration extends AppServerIntegration {
    /**
     * Icon.
     */
    //public static final Icon ICON = IconLoader.getIcon("/runConfigurations/web_app.png");
    public static final Icon ICON = IconLoader.getIcon("/org/codehaus/cargo/intellijidea/icon.png");

    /**
     * ApplicationServerHelper
     */
    private ApplicationServerHelper applicationServerHelper;
    /**
     * Container.
     */
    private Container container;
    /**
     * Supported module types.
     */
    private ModuleType[] supportedModuleTypes;

    /**
     * Constructor.
     *
     * @param containerId container id
     */
    public AbstractCargoAppServerIntegration(String containerId) {
        this(new DefaultContainerFactory().createContainer(containerId));
    }

    /**
     * Constructor.
     *
     * @param container container
     */
    public AbstractCargoAppServerIntegration(Container container) {
        this.container = container;
        this.applicationServerHelper = createApplicationServerHelper();
        setSupportedModuleTypes(getSupportedModuleTypes(container));
    }

    /**
     * Factory method for creating an Application Server Helper.
     * In order to support specific container features in the
     * configuration dialogs you might want to overwrite this method
     * in a subclass.
     *
     * @return application server helper.
     * @see #getApplicationServerHelper()
     */
    protected ApplicationServerHelper createApplicationServerHelper() {
        return new CargoApplicationServerHelper(this);
    }

    /**
     * Returns the IntellijIdea module types that are supported by this container.
     * Basically a mapping between {@link ModuleType} and {@link ContainerCapability}
     *
     * @param container Container in question
     * @return array of supported module types
     */
    private static ModuleType[] getSupportedModuleTypes(Container container) {
        ContainerCapability containerCapability = container.getCapability();
        List capabilities = new ArrayList();
        if (containerCapability.supportsDeployableType(DeployableType.EAR)) {
            capabilities.add(ModuleType.J2EE_APPLICATION);
        }
        if (containerCapability.supportsDeployableType(DeployableType.WAR)) {
            capabilities.add(ModuleType.WEB);
        }
        ModuleType[] moduleTypes = (ModuleType[]) capabilities.toArray(new ModuleType[capabilities.size()]);
        return moduleTypes;
    }

    /**
     * Get icon.
     *
     * @return icon
     */
    public Icon getIcon() {
        return ICON;
    }

    /**
     * Get presentable name for this app server integration.
     *
     * @return presentable name
     */
    public String getPresentableName() {
        return "Cargo " + container.getName();
    }


    /**
     * Component name.
     *
     * @return name
     */
    public String getComponentName() {
        return getClass().getName() + "." + container.getId();
    }

    /**
     * Init component.
     */
    public void initComponent() {
    }

    /**
     * Dispose component.
     */
    public void disposeComponent() {
    }

    /**
     * Returns the application server helper
     *
     * @return helper
     */
    public ApplicationServerHelper getApplicationServerHelper() {
        return applicationServerHelper;
    }

    /**
     * Returns the supported module types.
     *
     * @return module types
     */
    public ModuleType[] getSupportedModuleTypes() {
        return supportedModuleTypes;
    }

    /**
     * Set supported module types.
     *
     * @param moduleTypes module types
     */
    private void setSupportedModuleTypes(ModuleType[] moduleTypes) {
        this.supportedModuleTypes = moduleTypes;
    }

    /**
     * Returns the Cargo container id.
     *
     * @return container id
     */
    public String getContainerId() {
        return container.getId();
    }
}
