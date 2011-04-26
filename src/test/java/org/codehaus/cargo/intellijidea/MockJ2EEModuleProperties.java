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

import javax.swing.Icon;

import com.intellij.j2ee.appServerIntegrations.AppServerIntegration;
import com.intellij.j2ee.appServerIntegrations.AppServerIntegrationListener;
import com.intellij.j2ee.appServerIntegrations.ApplicationServer;
import com.intellij.j2ee.j2eeDom.DeploymentDescriptorMetaData;
import com.intellij.j2ee.j2eeDom.J2EEDeploymentDescriptor;
import com.intellij.j2ee.j2eeDom.J2EEDeploymentItem;
import com.intellij.j2ee.j2eeDom.J2EEModuleListener;
import com.intellij.j2ee.j2eeDom.J2EEModuleProperties;
import com.intellij.j2ee.module.ContainerElement;
import com.intellij.j2ee.module.LibraryLink;
import com.intellij.j2ee.module.ModuleContainer;
import com.intellij.j2ee.module.ModuleLink;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;

import org.jdom.Element;

/**
 * MockJ2EEModuleProperties.
 * <p/>
 *
 * @version $Id: CargoTaskTest.java 414 2005-07-20 09:34:44Z vmassol $
 */
public class MockJ2EEModuleProperties extends J2EEModuleProperties {

    private Module module;

    public void setModule(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public String getPresentableName() {
        return null;
    }

    public Icon getIcon() {
        return null;
    }

    public void addModuleListener(J2EEModuleListener j2EEModuleListener) {
    }

    public void removeModuleListener(J2EEModuleListener j2EEModuleListener) {
    }

    public Module[] getContainingIdeaModules() {
        return new Module[0];
    }

    public AppServerIntegration getAppServerIntegration() {
        return null;
    }

    public ApplicationServer getApplicationServer() {
        return null;
    }

    public J2EEDeploymentDescriptor[] getDeploymentDescriptors() {
        return new J2EEDeploymentDescriptor[0];
    }

    public void addAppServerIntegrationListener(AppServerIntegrationListener listener) {
    }

    public void removeAppServerIntegrationListener(AppServerIntegrationListener listener) {
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    public void moduleAdded() {
    }

    public String getComponentName() {
        return null;
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    public J2EEDeploymentDescriptor getMainDeploymentDescriptor() {
        return null;
    }

    public J2EEDeploymentItem[] getDeploymentItems() {
        return new J2EEDeploymentItem[0];
    }

    public J2EEDeploymentItem findDeploymentDescriptor(
            DeploymentDescriptorMetaData mainDeploymentDescriptorDescription) {
        return null;
    }

    /* ==== Methods new in IDEA 5.0 ==== */

    public void disposeModifiableModel() {
    }

    public ModuleLink[] getContainingModules() {
        return new ModuleLink[0];
    }

    public LibraryLink[] getContainingLibraries() {
        return new LibraryLink[0];
    }

    public ContainerElement[] getElements() {
        return new ContainerElement[0];
    }

    public void setElements(ContainerElement[] elements) {
    }

    public void addElement(ContainerElement element) {
    }

    public void removeModule(Module module) {
    }

    public void containedEntriesChanged() {
    }

    public void startEdit(ModifiableRootModel rootModel) {
    }

    public ModuleContainer getModifiableModel() {
        return null;
    }

    public void commit(ModifiableRootModel model) throws ConfigurationException {
    }

    public boolean isModified(ModifiableRootModel model) {
        return false;
    }

    public void readExternal(Element element) throws InvalidDataException {
    }

    public void writeExternal(Element element) throws WriteExternalException {
    }


}
