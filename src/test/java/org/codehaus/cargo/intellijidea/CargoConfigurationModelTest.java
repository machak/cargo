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

import com.intellij.execution.ExecutionException;
import com.intellij.j2ee.serverInstances.DefaultServerInstance;
import com.intellij.openapi.module.ModuleType;

import org.codehaus.cargo.container.configuration.Configuration;
import org.codehaus.cargo.container.property.ServletPropertySet;

import junit.framework.TestCase;

/**
 * CargoConfigurationModelTest.
 * <p/>
 *
 * @version $Id: CargoTaskTest.java 414 2005-07-20 09:34:44Z vmassol $
 */
public class CargoConfigurationModelTest extends TestCase {

    private MockJettyIntegration mockJettyIntegration;
    private MockCommonModel commonModel;
    private CargoConfigurationModel cargoConfigurationModel;


    protected void setUp() throws Exception {
        if (ModuleType.WEB == null) {
            ModuleType.WEB = MockModuleType.WEB;
        }
        mockJettyIntegration = new MockJettyIntegration();
        commonModel = new MockCommonModel();
        commonModel.setIntegration(mockJettyIntegration);
        cargoConfigurationModel = new CargoConfigurationModel();
        commonModel.setServerModel(cargoConfigurationModel);
    }

    public void testGetContainerId() {
        cargoConfigurationModel.setCommonModel(commonModel);
        assertEquals(mockJettyIntegration.getContainerId(),
                cargoConfigurationModel.getContainerId());
    }

    public void testGetDefaultPort() {
        assertEquals(8080, cargoConfigurationModel.getDefaultPort());
    }

    public void testCreateServerInstance() throws ExecutionException {
        commonModel.setLocal(true);
        cargoConfigurationModel.setCommonModel(commonModel);
        assertTrue(cargoConfigurationModel.createServerInstance() instanceof CargoServerInstance);
        commonModel.setLocal(false);
        cargoConfigurationModel.setCommonModel(commonModel);
        assertTrue(cargoConfigurationModel.createServerInstance() instanceof DefaultServerInstance);
    }

    public void testGetEditor() {
        commonModel.setLocal(true);
        cargoConfigurationModel.setCommonModel(commonModel);
        assertTrue(cargoConfigurationModel.getEditor() instanceof CargoConfigurationEditor);
        commonModel.setLocal(false);
        cargoConfigurationModel.setCommonModel(commonModel);
        assertNull(cargoConfigurationModel.getEditor());
    }

    public void testGetDeploymentProvider() {
        commonModel.setLocal(true);
        cargoConfigurationModel.setCommonModel(commonModel);
        assertTrue(cargoConfigurationModel.getDeploymentProvider()
                instanceof CargoDeploymentProvider);
        commonModel.setLocal(false);
        cargoConfigurationModel.setCommonModel(commonModel);
        assertNull(cargoConfigurationModel.getDeploymentProvider());
    }

    public void testGetLocalPort() {
        commonModel.setIntegration(mockJettyIntegration);
        cargoConfigurationModel.setCommonModel(commonModel);
        cargoConfigurationModel.setConfigDir(System.getProperty("java.io.tmpdir"));
        cargoConfigurationModel.setProperty(ServletPropertySet.PORT, "1234");
        assertEquals(1234, cargoConfigurationModel.getLocalPort());
    }

    public void testGetCargoConfiguration() {
        commonModel.setIntegration(mockJettyIntegration);
        cargoConfigurationModel.setCommonModel(commonModel);
        final String configDir = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
        cargoConfigurationModel.setConfigDir(configDir);
        final String port = "1234";
        cargoConfigurationModel.setProperty(ServletPropertySet.PORT, port);
        Configuration cargoConfiguration = cargoConfigurationModel.getCargoConfiguration();
        assertEquals(new File(configDir), cargoConfiguration.getDir());
        assertEquals(port, cargoConfiguration.getPropertyValue(ServletPropertySet.PORT));
    }

}
