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

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.SettingsEditor;

import junit.framework.TestCase;

/**
 * CargoDeploymentProviderTest.
 * <p/>
 *
 * @version $Id: CargoTaskTest.java 414 2005-07-20 09:34:44Z vmassol $
 */
public class CargoDeploymentProviderTest extends TestCase {

    protected void setUp() throws Exception {
        if (ModuleType.WEB == null) {
            ModuleType.WEB = MockModuleType.WEB;
        }
    }

    public void testCreateAdditionalDeploymentSettingsEditorForWAR() {
        CargoDeploymentProvider cargoDeploymentProvider = new CargoDeploymentProvider();
        MockJ2EEModuleProperties j2EEModuleProperties = new MockJ2EEModuleProperties();
        MockModule module = new MockModule();
        module.setName("Name");
        MockProject project = new MockProject();
        project.setName("Name");
        module.setProject(project);
        module.setModuleType(ModuleType.WEB);
        j2EEModuleProperties.setModule(module);
        SettingsEditor settingsEditor = cargoDeploymentProvider
                .createAdditionalDeploymentSettingsEditor(null, j2EEModuleProperties);
        assertNotNull(settingsEditor);
    }

    public void testCreateAdditionalDeploymentSettingsEditorForEAR() {
        CargoDeploymentProvider cargoDeploymentProvider = new CargoDeploymentProvider();
        MockJ2EEModuleProperties j2EEModuleProperties = new MockJ2EEModuleProperties();
        MockModule module = new MockModule();
        module.setName("Name");
        MockProject project = new MockProject();
        project.setName("Name");
        module.setProject(project);
        module.setModuleType(ModuleType.J2EE_APPLICATION);
        j2EEModuleProperties.setModule(module);
        SettingsEditor settingsEditor = cargoDeploymentProvider
                .createAdditionalDeploymentSettingsEditor(null, j2EEModuleProperties);
        assertNull(settingsEditor);
    }

    public void testGetHelpId() {
        CargoDeploymentProvider cargoDeploymentProvider = new CargoDeploymentProvider();
        assertEquals("webLogic.deployment", cargoDeploymentProvider.getHelpId());
    }
}
